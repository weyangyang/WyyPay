package db.utils;

import java.lang.reflect.Field;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

public class CommonDbHelper extends SQLiteOpenHelper {
	public CommonDbHelper(Context context) {
		super(context, DBConfig.getInstance().getDbName(), null, DBConfig
				.getInstance().getDbVersion());
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		List<Class<? extends BaseDbBean>> dbBeans = BaseDbBean.getDbBeans();
		for (Class<? extends BaseDbBean> clazz : dbBeans) {
			try {
				db.execSQL(createTableCmd(clazz));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion >= newVersion) {
			return;
		}
		List<Object> strExecs = DBConfig.getInstance().upgradeExec
				.get(oldVersion);
		if (strExecs != null && strExecs.size() > 0) {
			for (Object strExec : strExecs) {
				if (strExec instanceof String) {
					if (!TextUtils.isEmpty((String)strExec)) {
						try {
							db.execSQL((String)strExec);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				if (strExec instanceof Class) {
					try {
						db.execSQL("DROP TABLE IF EXISTS "
								+ ((Class)strExec).getDeclaredField("TABLE_NAME").get(null));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			onCreate(db); // 防止有些新表创建.
			return;
		}
		List<Class<? extends BaseDbBean>> dbBeans = BaseDbBean.getDbBeans();
		for (Class<? extends BaseDbBean> clazz : dbBeans) {
			try {
				db.execSQL("DROP TABLE IF EXISTS "
						+ clazz.getDeclaredField("TABLE_NAME").get(null));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		onCreate(db);
	}

	private String createTableCmd(Class<? extends BaseDbBean> clazz) {
		StringBuilder cmd = new StringBuilder();

		try {
			cmd.append("create table if not exists ")
					.append(clazz.getDeclaredField("TABLE_NAME").get(null))
					.append("(");

			//Field fields[] = clazz.getDeclaredFields();
			Field fields[] = clazz.getFields();
			for (Field f : fields) {
				ColumnAnnotation anno = f.getAnnotation(ColumnAnnotation.class);
				if (anno != null) {
					String info = anno.info();
					if (info.equals("_id")) {
						info = "integer primary key autoincrement";
					}
					cmd.append(anno.column()).append(" ").append(info)
							.append(",");
				}
			}
			cmd.deleteCharAt(cmd.length() - 1);
			cmd.append(")");
		} catch (Exception e) {
			e.printStackTrace();
			cmd = null;
		}

		return cmd.toString();
	}
}
