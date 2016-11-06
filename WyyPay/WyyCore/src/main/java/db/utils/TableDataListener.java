package db.utils;

import android.os.Handler;

public abstract class TableDataListener<T> {
	public static final int TYPE_ADD = 1;
	public static final int TYPE_DELETE = 2;
	public static final int TYPE_UPDATE = 3;
	public static final int TYPE_RAW_DELETE = 4;
	public static final int TYPE_RAW_UPDATE = 5;
	public static final int TYPE_BATCH_INSERT = 6;
	public static final int TYPE_BATCH_UPDATE = 7;
	public static final int TYPE_BATCH_REPLACE = 8;
	public static final int TYPE_BATCH_DELETE= 9;
	public abstract void onDataChanged(int type, T obj);
	private Handler handler;
	
	public TableDataListener(Handler handler) {
		this.handler = handler;
	}

	public void notifyDataChange(final int type, final T obj) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				onDataChanged(type, obj);
			}
		});
	}
}
