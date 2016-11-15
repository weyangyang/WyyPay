package db.utils;

import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
/**
 * 数据库批量操作工具类，及数据库关闭方法dbClose();
 * @author liyusheng
 *
 */
public class DBUtils {
	/**
	 * 批量插入数据，先更新后插入
	 * @param dbBeanList 数据bean list
	 * @param feldName 数据库bean变量名
	 * @return
	 */
	public static long insertAll(final List<? extends BaseDbBean> dbBeanList, final String feldName) {
		if (TextUtils.isEmpty(feldName)) {
			return insertAll(dbBeanList);
		}
		return dbExcute(dbBeanList, new DBExcuteInterf() {
			@Override
			public long[] excute(SQLiteDatabase db, String strTableName, long rows) {
				int type = 0;
				for (int i = 0; i < dbBeanList.size(); i++) {
					String selectionArg = null;
					String column = "";
					try {
						selectionArg = dbBeanList.get(i).getClass().getField(feldName).get(dbBeanList.get(i)).toString();
						ColumnAnnotation anno = dbBeanList.get(i).getClass().getField(feldName).getAnnotation(ColumnAnnotation.class);
						column = anno.column();
					} catch (IllegalAccessException e) {
					} catch (NoSuchFieldException e) {
					}
					int row = db.update(strTableName, ((BaseDbBean) dbBeanList.get(i)).contentValues(), column + "=?",
							new String[] { selectionArg });
					type = TableDataListener.TYPE_BATCH_UPDATE;
					if (row > 0) {
						rows++;
					} else {
						long nums = db.insert(strTableName, null, ((BaseDbBean) dbBeanList.get(i)).contentValues());
						type = TableDataListener.TYPE_BATCH_INSERT;
						if (nums >= 0) {
							rows++;
						}
					}
				}
				long[] params = { type, rows };
				return params;
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void notifyChange(int type, BaseDbBean dbBean, String tableName) {
		try {
			List<TableDataListener> observerList = BaseDbBean.observersMap.get(tableName);
			if (observerList != null) {
				for (TableDataListener observer : observerList) {
					switch (type) {
					case TableDataListener.TYPE_BATCH_INSERT:
						observer.notifyDataChange(TableDataListener.TYPE_BATCH_INSERT, dbBean);
						break;
					case TableDataListener.TYPE_BATCH_UPDATE:
						observer.notifyDataChange(TableDataListener.TYPE_BATCH_UPDATE, dbBean);
						break;
					case TableDataListener.TYPE_BATCH_REPLACE:
						observer.notifyDataChange(TableDataListener.TYPE_BATCH_REPLACE, dbBean);
						break;
					case TableDataListener.TYPE_BATCH_DELETE:
						observer.notifyDataChange(TableDataListener.TYPE_BATCH_DELETE, dbBean);
						break;
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public static long replaceAll(final ArrayList<? extends BaseDbBean> dbBeanList) {
		return dbExcute(dbBeanList, new DBExcuteInterf() {
			@Override
			public long[] excute(SQLiteDatabase db, String strTableName, long rows) {
				for (int i = 0; i < dbBeanList.size(); i++) {
					long nums = db.replace(strTableName, null, ((BaseDbBean) dbBeanList.get(i)).contentValues());
					if (nums >= 0) {
						rows++;
					}
				}
				long[] params = { TableDataListener.TYPE_BATCH_REPLACE, rows };
				return params;
			}
		});
	}

	private static long dbExcute(List<? extends BaseDbBean> dbBeanList, DBExcuteInterf dbExcuteInterf) {
		long rows = 0;
		if (dbBeanList != null && dbBeanList.size() > 0) {
			SQLiteDatabase db = BaseDbBean.getDb();
			final BaseDbBean dbBean = (BaseDbBean) dbBeanList.get(0);
			dbBean.setSQLiteDatabaseInstance(db);
			String tableName = "";
			try {
				tableName = dbBean.getClass().getField("TABLE_NAME").get(null).toString();
			} catch (IllegalAccessException e1) {
				rows = -1;
			} catch (IllegalArgumentException e1) {
				rows = -1;
			} catch (NoSuchFieldException e1) {
				rows = -1;
			}
			db.beginTransaction(); // 手动设置开始事务
			int type = 0;
			try {
				long[] params = dbExcuteInterf.excute(db, tableName, rows);
				if(params!=null){
					type = (int) params[0];
					rows = params[1];
				}
				if (rows > 0) {
					db.setTransactionSuccessful(); // 设置事务处理成功，不设置会自动回滚不提交
				}
			} catch (IllegalArgumentException e) {
				rows = -1;
			} finally {
				db.endTransaction(); // 处理完成
				// db.close();
			}
			if (rows > 0) {
				notifyChange(type, dbBean, tableName);
			}
			return rows;
		}

		return rows;
	}

	public static long insertAll(final List<? extends BaseDbBean> dbBeanList) {

		return dbExcute(dbBeanList, new DBExcuteInterf() {
			@Override
			public long[] excute(SQLiteDatabase db, String strTableName, long rows) {
				for (int i = 0; i < dbBeanList.size(); i++) {
					long nums = db.insert(strTableName, null, ((BaseDbBean) dbBeanList.get(i)).contentValues());
					if (nums >= 0) {
						rows++;
					}
				}
				long[] params = { TableDataListener.TYPE_BATCH_INSERT, rows };
				return params;
			}
		});
	}
	/**
	 * 批量删除
	 * @param dbBeanList
	 * @param whereKey null 表示删除所有行.
	 * @return
	 */
	public static long deleteAll(final ArrayList<? extends BaseDbBean> dbBeanList, final String whereKey) {
		return dbExcute(dbBeanList, new DBExcuteInterf() {
			@Override
			public long[] excute(SQLiteDatabase db, String strTableName, long rows) {
				if (whereKey == null) {
					rows = db.delete(strTableName, null, null);
					long[] params = { TableDataListener.TYPE_BATCH_DELETE, rows };
					return params;
				}
				for (int i = 0; i < dbBeanList.size(); i++) {
					String value = null;
					try {
						value = dbBeanList.get(i).getClass().getField(whereKey).get(dbBeanList.get(i)).toString();
					} catch (IllegalAccessException e) {
					} catch (NoSuchFieldException e) {
					}
					rows = db.delete(strTableName, whereKey + "=?", new String[] { value });
				}
				long[] params = { TableDataListener.TYPE_BATCH_DELETE, rows };
				return params;
			}
		});
	}
	/**
	 * 批量更新
	 * @param dbBeanList 需更新的table bean 集合
	 * @param feldName 更新条件变量名key  (=?)
	 * @param keys 需要更新的列名字符数组
	 * @return 影响了多少行数据
	 */
	public static long updateAll(final ArrayList<? extends BaseDbBean> dbBeanList, final String feldName) {

		return dbExcute(dbBeanList, new DBExcuteInterf() {
			@Override
			public long[] excute(SQLiteDatabase db, String strTableName, long rows) {
				if(TextUtils.isEmpty(feldName)){
					return null;
				}
				for (int i = 0; i < dbBeanList.size(); i++) {
					String value = "";
					String column = "";
					try {
						value = dbBeanList.get(i).getClass().getField(feldName).get(dbBeanList.get(i)).toString();
						ColumnAnnotation anno = dbBeanList.get(i).getClass().getField(feldName).getAnnotation(ColumnAnnotation.class);
						column = anno.column();
					} catch (IllegalAccessException e) {
					} catch (NoSuchFieldException e) {
					}
					rows += db.update(strTableName, ((BaseDbBean) dbBeanList.get(i)).contentValues(), column + "=?",
							new String[] { value });
				}
				long[] params = { TableDataListener.TYPE_BATCH_UPDATE, rows };
				return params;

			}
		});
	}
	/**
	 * 清空一张表里的数据.
	 * @param strTableName
	 * @return
	 */
	public static int drop(String strTableName) {
		try {
			int rows = BaseDbBean.getDb().delete(strTableName, null, null);
			return rows;
		} catch(Exception e) {
		}
		return -1;
	}
	public static void dbClose() {
		SQLiteDatabase db = BaseDbBean.getSQLiteDatabaseInstance();
		if (db != null && db.isOpen()) {
			db.close();
		}
	}

	interface DBExcuteInterf {
		long[] excute(SQLiteDatabase db, String strTableName, long rows);
	}
	
}
