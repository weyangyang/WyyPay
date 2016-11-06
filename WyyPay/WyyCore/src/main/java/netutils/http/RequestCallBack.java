package netutils.http;


/**
 * @param <T>
 *            目前泛型支持 String,File
 */
public abstract class RequestCallBack<T> {

	private boolean progress = true;
	private int rate = 1000 * 2;// 每秒
	private boolean userStop = false;
	private boolean isDelete = false;
	public void setUserStop(boolean stop) {
		userStop = stop;
	}
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	public boolean isDelete() {
		return isDelete;
	}
	public boolean isUserStop() {
		return userStop;
	}
	public boolean isProgress() {
		return progress;
	}

	public int getRate() {
		return rate;
	}

	/**
	 * 设置进度,而且只有设置了这个了以后，onLoading才能有效。
	 * 
	 * @param progress
	 *            是否启用进度显示
	 * @param rate
	 *            进度更新频率
	 */
	public RequestCallBack<T> progress(boolean progress, int rate) {
		this.progress = progress;
		this.rate = rate;
		return this;
	}

	public void onStart() {
	};

	/**
	 * onLoading方法有效progress
	 * 
	 * @param count
	 * @param current
	 */
	public void onLoading(long count, long current) {
	};

	public abstract void onSuccess(T t);

	public void onFailure(Throwable t, int errorNo, String strMsg){};
}
