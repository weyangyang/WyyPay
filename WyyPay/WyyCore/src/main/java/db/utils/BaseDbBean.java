package db.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.CancellationSignal;
import android.os.OperationCanceledException;
import android.text.TextUtils;

public class BaseDbBean implements Cloneable {
	public static final String _ID = "_id";
	@ColumnAnnotation(column = _ID, info = _ID)
	public long _id = -1;
	private static Context mContext;

	public static void init(Context context,
			Class<? extends BaseDbBean>... dbBeans) {
		mContext = context;
		for (int i = 0; i < dbBeans.length; i++) {
			mDbBeans.add(dbBeans[i]);
		}
	}

	public static void setDBConfig(String dbName, int dbVersion) {
		DBConfig.getInstance().setDbName(dbName);
		DBConfig.getInstance().setDbVersion(dbVersion);
	}

	public static void setDBUpgrade(int oldVersion, List<Object> listExec) {
		DBConfig.getInstance().setUpgradeExec(oldVersion, listExec);
	}

	private static List<Class<? extends BaseDbBean>> mDbBeans = new ArrayList<Class<? extends BaseDbBean>>();

	static List<Class<? extends BaseDbBean>> getDbBeans() {
		return mDbBeans;
	}

	static HashMap<String, List<TableDataListener>> observersMap = new HashMap<String, List<TableDataListener>>();

	public static void registerContentObserver(String tableName,
			TableDataListener observer) {
		List<TableDataListener> observerList = observersMap.get(tableName);
		if (observerList == null) {
			observerList = new ArrayList<TableDataListener>();
			observersMap.put(tableName, observerList);
		}
		observerList.add(observer);
	}

	public static void unregisterContentObserver(String tableName,
			TableDataListener observer) {
		List<TableDataListener> observerList = observersMap.get(tableName);
		if (observerList != null) {
			observerList.remove(observer);
		}
	}

	static android.database.sqlite.SQLiteDatabase mSQLiteDatabase;

	static SQLiteDatabase getDb() {
		if (mSQLiteDatabase != null && mSQLiteDatabase.isOpen()) {
			return mSQLiteDatabase;
		} else {
			mSQLiteDatabase = new CommonDbHelper(mContext)
					.getReadableDatabase();
		}
		return mSQLiteDatabase;
	}

	static SQLiteDatabase getSQLiteDatabaseInstance() {
		return mSQLiteDatabase;
	}

	void setSQLiteDatabaseInstance(SQLiteDatabase db) {
		mSQLiteDatabase = db;
	}

	public ContentValues contentValues() {
		ContentValues values = new ContentValues();
		Field fields[] = getClass().getFields();
		for (Field f : fields) {
			ColumnAnnotation anno = f.getAnnotation(ColumnAnnotation.class);
			if (anno != null && !f.getName().equals(_ID)) {
				Type type = f.getGenericType();
				String defaultValue = anno.defaultValue();
				try {
					if (type.equals(int.class) || type.equals(Integer.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Integer.parseInt(defaultValue) == f
									.getInt(this)) {
								continue;
							} else {
								values.put(anno.column(), f.getInt(this));
							}
						}
					} else if (type.equals(short.class)
							|| type.equals(Short.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Short.parseShort(defaultValue) == f
									.getShort(this)) {
								continue;
							} else {
								values.put(anno.column(), f.getShort(this));
							}
						}
					} else if (type.equals(byte.class)
							|| type.equals(Byte.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Byte.parseByte(defaultValue) == f.getByte(this)) {
								continue;
							} else {
								values.put(anno.column(), f.getByte(this));
							}
						}
					} else if (type.equals(long.class)
							|| type.equals(Long.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Long.parseLong(defaultValue) == f.getLong(this)) {
								continue;
							} else {
								values.put(anno.column(), f.getLong(this));
							}
						}
					} else if (type.equals(boolean.class)
							|| type.equals(Boolean.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Boolean.parseBoolean(defaultValue) == f
									.getBoolean(this)) {
								continue;
							} else {
								values.put(anno.column(),
										f.getBoolean(this) ? 1 : 0);
							}
						}
					} else if (type.equals(float.class)
							|| type.equals(Float.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Float.parseFloat(defaultValue) == f
									.getFloat(this)) {
								continue;
							} else {
								values.put(anno.column(), f.getFloat(this));
							}
						}
					} else if (type.equals(double.class)
							|| type.equals(Double.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (Double.parseDouble(defaultValue) == f
									.getDouble(this)) {
								continue;
							} else {
								values.put(anno.column(), f.getDouble(this));
							}
						}
					} else if (type.equals(String.class)) {
						if (!TextUtils.isEmpty(defaultValue)) {
							if (defaultValue.equals((String) f.get(this))) {
								continue;
							} else {
								values.put(anno.column(), (String) f.get(this));
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return values;
	}

	public BaseDbBean parseCursor(Cursor cursor) {
		Field fields[] = getClass().getFields();
		for (Field f : fields) {
			ColumnAnnotation anno = f.getAnnotation(ColumnAnnotation.class);
			if (anno != null) {
				Type type = f.getGenericType();
				try {
					int column = cursor.getColumnIndex(anno.column());
					if (column < 0) {
						continue;
					}
					if (type.equals(int.class) || type.equals(Integer.class)) {
						f.set(this, cursor.getInt(column));
					} else if (type.equals(short.class)
							|| type.equals(Short.class)) {
						f.set(this, cursor.getShort(column));
					} else if (type.equals(byte.class)
							|| type.equals(Byte.class)) {
						f.set(this, (byte) cursor.getInt(column));
					} else if (type.equals(long.class)
							|| type.equals(Long.class)) {
						f.set(this, cursor.getLong(column));
					} else if (type.equals(boolean.class)
							|| type.equals(Boolean.class)) {
						f.set(this, cursor.getInt(column) == 1 ? true : false);
					} else if (type.equals(float.class)
							|| type.equals(Float.class)) {
						f.set(this, cursor.getFloat(column));
					} else if (type.equals(double.class)
							|| type.equals(Double.class)) {
						f.set(this, cursor.getDouble(column));
					} else if (type.equals(String.class)) {
						f.set(this, cursor.getString(column));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return this;
	}

	/**
	 * Query the given table, returning a {@link Cursor} over the result set.
	 * 
	 * @param columns
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is discouraged to prevent reading data from
	 *            storage that isn't going to be used.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param groupBy
	 *            A filter declaring how to group rows, formatted as an SQL
	 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *            will cause the rows to not be grouped.
	 * @param having
	 *            A filter declare which row groups to include in the cursor, if
	 *            row grouping is being used, formatted as an SQL HAVING clause
	 *            (excluding the HAVING itself). Passing null will cause all row
	 *            groups to be included, and is required when row grouping is
	 *            not being used.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * 
	 * @return A {@link Cursor} object, which is positioned before the first
	 *         entry. Note that {@link Cursor}s are not synchronized, see the
	 *         documentation for more details.
	 * @see Cursor
	 */
	public Cursor queryCursor(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy);
		} catch (Exception e) {

		}
		return cursor;

	}

	/**
	 * Query the given table
	 * 
	 * @param columns
	 *            A list of which columns to return. Passing null will return
	 *            all columns, which is discouraged to prevent reading data from
	 *            storage that isn't going to be used.
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @param groupBy
	 *            A filter declaring how to group rows, formatted as an SQL
	 *            GROUP BY clause (excluding the GROUP BY itself). Passing null
	 *            will cause the rows to not be grouped.
	 * @param having
	 *            A filter declare which row groups to include in the cursor, if
	 *            row grouping is being used, formatted as an SQL HAVING clause
	 *            (excluding the HAVING itself). Passing null will cause all row
	 *            groups to be included, and is required when row grouping is
	 *            not being used.
	 * @param orderBy
	 *            How to order the rows, formatted as an SQL ORDER BY clause
	 *            (excluding the ORDER BY itself). Passing null will use the
	 *            default sort order, which may be unordered.
	 * @return list contain bean extends of BaseDbBean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList query(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		ArrayList results = new ArrayList();
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy);
			while (cursor.moveToNext()) {
				results.add(((BaseDbBean) clone()).parseCursor(cursor));
			}

		} catch (Exception e) {
		}finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// db.close();

		return results;
	}
	/**
	 * Runs the provided SQL and returns a {@link Cursor} over the result set.
	 *
	 * @param sql the SQL query. The SQL string must not be ; terminated
	 * @param selectionArgs You may include ?s in where clause in the query,
	 *     which will be replaced by the values from selectionArgs. The
	 *     values will be bound as Strings.
	 * @return A {@link Cursor} object, which is positioned before the first entry. Note that
	 * {@link Cursor}s are not synchronized, see the documentation for more details.
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs) {
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql,selectionArgs);
//			while (cursor.moveToNext()) {
//				results.add(((BaseDbBean) clone()).parseCursor(cursor));
//			}

		} catch (Exception e) {
		}

		// db.close();

		return cursor;
	}
	/**
	 * Runs the provided SQL and returns a {@link Cursor} over the result set.
	 *
	 * @param sql the SQL query. The SQL string must not be ; terminated
	 * @param selectionArgs You may include ?s in where clause in the query,
	 *     which will be replaced by the values from selectionArgs. The
	 *     values will be bound as Strings.
	 * @param cancellationSignal A signal to cancel the operation in progress, or null if none.
	 * If the operation is canceled, then {@link OperationCanceledException} will be thrown
	 * when the query is executed.
	 * @return A {@link Cursor} object, which is positioned before the first entry. Note that
	 * {@link Cursor}s are not synchronized, see the documentation for more details.
	 */
	public Cursor rawQuery(String sql, String[] selectionArgs,CancellationSignal cancellationSignal) {
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql,selectionArgs,cancellationSignal);
		} catch (Exception e) {
		}

		// db.close();

		return cursor;
	}
	public void execSql(String sql)throws SQLException {
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		db.execSQL(sql);
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList query(String[] columns, String selection,
						   String[] selectionArgs, String groupBy, String having,
						   String orderBy, int limit) {
		ArrayList results = new ArrayList();
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		Cursor cursor = null;
		try {
			cursor = db.query(getTableName(), columns, selection,
					selectionArgs, groupBy, having, orderBy, limit +"");
			while (cursor.moveToNext()) {
				results.add(((BaseDbBean) clone()).parseCursor(cursor));
			}

		} catch (Exception e) {
		}finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// db.close();

		return results;
	}

	/**
	 * SELECT table2.about,table2.courseID,table2.enrollStart FROM table1,
	 * table2 WHERE table1.type=1 AND table1.courseID=table2.courseID order by
	 * table2.indexss limit 10000 offset 0;
	 * 
	 * uniteQuery("table1", "table2", new
	 * String[]{"table2.about","table2.courseID","table2.enrollStart"},
	 * "table1.type=? and table1.courseID=?",new
	 * String[]{"1","table2.courseID"});
	 * 
	 * 两个表联合查询，返回一个innerJoinTable bean arrayList集合 调用时，请用innerJoinTable
	 * bean的实例调用
	 * 
	 * @param fromTable
	 *            表1
	 * @param innerJoinTable
	 *            表2
	 * @param innerJoinTableColumns
	 *            要显示的表2列名数组
	 * @param whereClause
	 *            查询条件
	 * @param whereArgs
	 * @return innerJoinTable bean arrayList集合
	 */
	@SuppressWarnings("rawtypes")
	public ArrayList uniteQuery(String fromTable, String innerJoinTable,
			String[] innerJoinTableColumns, String whereClause,
			String[] whereArgs) {
		return uniteQuery(fromTable, innerJoinTable, innerJoinTableColumns,
				whereClause, whereArgs, null, 0, 0);
	}

	/**
	 * SELECT table2.about,table2.courseID,table2.enrollStart FROM table1,
	 * table2 WHERE table1.type=1 AND table1.courseID=table2.courseID order by
	 * table2.indexss limit 10000 offset 0;
	 * 
	 * uniteQuery("table1", "table2", new
	 * String[]{"table2.about","table2.courseID","table2.enrollStart"},
	 * "table1.type=? and table1.courseID=?",new
	 * String[]{"1","table2.courseID"},"table2.indexss",0,0);
	 * 
	 * 两个表联合查询，返回一个innerJoinTable bean arrayList集合 调用时，请用innerJoinTable
	 * bean的实例调用
	 * 
	 * @param fromTable
	 *            表1
	 * @param innerJoinTable
	 *            表2
	 * @param innerJoinTableColumns
	 *            要显示的表2列名数组
	 * @param whereClause
	 *            查询条件
	 * @param whereArgs
	 * @param orderBy
	 *            排序
	 * @param offset
	 * @param limit
	 * @return innerJoinTable bean arrayList集合
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList uniteQuery(String fromTable, String innerJoinTable,String[] innerJoinTableColumns,String whereClause,
			String[] whereArgs,String orderBy,int offset,int limit) {
		ArrayList results = new ArrayList();
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		StringBuilder sql = new StringBuilder(120);
		sql.append("SELECT ");
		if (innerJoinTableColumns != null && innerJoinTableColumns.length > 0) {
			String[] temp = new String[innerJoinTableColumns.length];
			for (int k = 0; k < innerJoinTableColumns.length; k++) {
				if(k<innerJoinTableColumns.length-1){
					temp[k] = innerJoinTable+"."+innerJoinTableColumns[k] +" , ";
				}else{
					temp[k] = innerJoinTable+"."+innerJoinTableColumns[k];
				}
				sql.append(temp[k]);
			}
		}else{
			sql.append(" * ");
		}
		sql.append(" FROM ");
		if(TextUtils.isEmpty(fromTable)){
			  throw new IllegalArgumentException("fromTable is  null");
		}
			sql.append(fromTable);
			sql.append(", ");
			if(TextUtils.isEmpty(innerJoinTable)){
				  throw new IllegalArgumentException("fromTable is  null");
			}
			sql.append(innerJoinTable);
		if (!TextUtils.isEmpty(whereClause)&& whereArgs != null && whereArgs.length > 0) {
			sql.append(" WHERE ");
			whereClause = whereClause.replace("?", "%s");
			whereClause = String.format(whereClause, whereArgs);
			sql.append(whereClause);
		}
		if(!TextUtils.isEmpty(orderBy)){
			sql.append(" ORDER BY ");
			sql.append(orderBy);
		}
		if(limit!=0&&limit>0){
			sql.append(" LIMIT ");
			sql.append(limit);
			}
		if(offset!=0&&offset>0){
			sql.append(" OFFSET ");
			sql.append(offset);
		}
			sql.append(";");
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql.toString(),new String[]{});
			while (cursor.moveToNext()) {
				results.add(((BaseDbBean) clone()).parseCursor(cursor));
			}
			//cursor.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (cursor != null) {
				cursor.close();
			}
		}
		return results;

	}

	public BaseDbBean querySingle(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		Cursor cursor = null;
		try {
			cursor = db.query(getClass().getField("TABLE_NAME")
					.get(null).toString(), columns, selection, selectionArgs,
					groupBy, having, orderBy, "1");
			if (cursor.moveToNext()) {
				return parseCursor(cursor);
			}

		} catch (Exception e) {
			return null;
		}finally {
			if (cursor != null) {
				cursor.close();
			}
		}

		// db.close();
		return null;
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param whereClause
	 *            whereClause the optional WHERE clause to apply when deleting.
	 *            Passing null will delete all rows.
	 * @param whereArgs
	 *            You may include ?s in the where clause, which will be replaced
	 *            by the values from whereArgs. The values will be bound as
	 *            Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int statementDelete(String whereClause, String[] whereArgs) {
		return statementDelete(getTableName(), whereClause, whereArgs);
	}

	/**
	 * Convenience method for deleting rows in the database.
	 * 
	 * @param table
	 *            the table to delete from
	 * @param whereClause
	 *            whereClause the optional WHERE clause to apply when deleting.
	 *            Passing null will delete all rows.
	 * @param whereArgs
	 *            You may include ?s in the where clause, which will be replaced
	 *            by the values from whereArgs. The values will be bound as
	 *            Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 *         otherwise. To remove all rows and get a count pass "1" as the
	 *         whereClause.
	 */
	public int statementDelete(String table, String whereClause,
			String[] whereArgs) {
		int rows = 0;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		StringBuilder sql = new StringBuilder(120);
		sql.append("DELETE FROM ");
		sql.append(table);
		if (!TextUtils.isEmpty(whereClause) && whereArgs != null
				&& whereArgs.length > 0) {
			String[] temp = new String[whereArgs.length];
			for (int k = 0; k < whereArgs.length; k++) {
				temp[k] = "'" + whereArgs[k] + "'";
			}
			sql.append(" WHERE ");
			whereClause = whereClause.replace("?", "%s");
			whereClause = String.format(whereClause, temp);
			sql.append(whereClause);
		}
		SQLiteStatement statement = null;
		try {
			statement = db.compileStatement(sql.toString());
			db.beginTransaction();
			rows = statement.executeUpdateDelete();
			if (rows > 0) {
				db.setTransactionSuccessful();
				notifyChange(TableDataListener.TYPE_ADD);
			}
		} catch (Exception e) {
		} finally {
			if (statement != null) {
				statement.close();
				db.endTransaction();
			}
		}
		return rows;
	}

	@Deprecated
	public boolean delete() {
		boolean result = false;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		try {
			int rows = db.delete(getClass().getField("TABLE_NAME").get(null)
					.toString(), _ID + "=?",
					new String[] { String.valueOf(_id) });
			if (rows > 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// db.close();
		if (result) {
			notifyChange(TableDataListener.TYPE_DELETE);
		}
		return result;
	}

	public int rawDelete(String whereClause, String[] whereArgs) {
		int rows = 0;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		try {
			rows = db.delete(getClass().getField("TABLE_NAME").get(null)
					.toString(), whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// db.close();
		if (rows > 0) {
			notifyChange(TableDataListener.TYPE_RAW_DELETE);
		}
		return rows;
	}
	public int rawUpdate(String wherekey) {
		if (TextUtils.isEmpty(wherekey)) {
			return -1;
		}
		String value = null;
		try {
			value = getClass().getField(wherekey).get(this).toString();
		} catch (IllegalAccessException e) {
			return -1;
		} catch (IllegalArgumentException e) {
			return -1;
		} catch (NoSuchFieldException e) {
			return -1;
		}
		return rawUpdate(contentValues(), wherekey + "=?",
				new String[] { value });

	}

	public int rawUpdate(String whereClause, String[] whereArgs) {
		int rows = 0;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		try {
			rows = db.update(getClass().getField("TABLE_NAME").get(null)
					.toString(), contentValues(), whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// db.close();
		if (rows > 0) {
			notifyChange(TableDataListener.TYPE_RAW_UPDATE);
		}

		return rows;

	}

	public int rawUpdate(ContentValues values, String whereClause,
			String[] whereArgs) {
		int rows = 0;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		try {
			rows = db.update(getClass().getField("TABLE_NAME").get(null)
					.toString(), values, whereClause, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// db.close();
		if (rows > 0) {
			notifyChange(TableDataListener.TYPE_RAW_UPDATE);
		}
		return rows;
	}

	@Deprecated
	public boolean update() {
		boolean result = false;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		try {
			int rows = db.update(getClass().getField("TABLE_NAME").get(null)
					.toString(), contentValues(), _ID + "=?",
					new String[] { String.valueOf(_id) });
			if (rows > 0) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// db.close();
		if (result) {
			notifyChange(TableDataListener.TYPE_UPDATE);
		}
		return result;
	}

	@Deprecated
	public boolean insert() {
		return insert(false);
	}

	@Deprecated
	public boolean insert(boolean updateIfExist) {

		return insert(updateIfExist, null, null);

	}

	public boolean insert(boolean updateIfExist, String column,
			String selectionArg) {
		if (TextUtils.isEmpty(selectionArg)) {
			selectionArg = _id + "";
		}
		if (TextUtils.isEmpty(column)) {
			column = _ID;
		}
		boolean result = false;
		int type = 0;
		// boolean isInsert = true;
		SQLiteDatabase db = getDb();
		setSQLiteDatabaseInstance(db);
		String tableName = getTableName();
		if (updateIfExist) {
			int rows = rawUpdate(contentValues(), column + "=?",
					new String[] { selectionArg });
			if (rows > 0) {
				result = true;
				type = TableDataListener.TYPE_UPDATE;
			} else {
				type = TableDataListener.TYPE_ADD;
				long id = db.insert(tableName, null, contentValues());
				if (id <= 0) {
					result = false;
				} else {
					_id = id;
					result = true;
				}

			}
		} else {
			type = TableDataListener.TYPE_ADD;
			long id = db.insert(tableName, null, contentValues());
			if (id <= 0) {
				result = false;
			} else {
				_id = id;
				result = true;
			}
		}

		// db.close();
		if (result) {
			notifyChange(type);
		}

		return result;
	}

	private String getTableName() {
		String tableName = "";
		try {
			tableName = getClass().getField("TABLE_NAME").get(null).toString();
		} catch (IllegalAccessException e1) {
		} catch (IllegalArgumentException e1) {
		} catch (NoSuchFieldException e1) {
		}
		return tableName;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void notifyChange(int type) {
		try {
			List<TableDataListener> observerList = observersMap
					.get(getTableName());
			if (observerList != null) {
				for (TableDataListener observer : observerList) {
					switch (type) {
					case TableDataListener.TYPE_ADD:
						observer.notifyDataChange(TableDataListener.TYPE_ADD,
								this);
						break;
					case TableDataListener.TYPE_UPDATE:
						observer.notifyDataChange(
								TableDataListener.TYPE_UPDATE, this);
						break;
					case TableDataListener.TYPE_RAW_UPDATE:
						observer.notifyDataChange(
								TableDataListener.TYPE_RAW_UPDATE, this);
						break;
					case TableDataListener.TYPE_RAW_DELETE:
						observer.notifyDataChange(
								TableDataListener.TYPE_RAW_DELETE, this);
						break;
					case TableDataListener.TYPE_DELETE:
						observer.notifyDataChange(
								TableDataListener.TYPE_DELETE, this);
						break;
					}
				}
			}
		} catch (Exception e) {
		}
	}
}
