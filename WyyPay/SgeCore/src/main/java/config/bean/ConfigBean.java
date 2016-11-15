package config.bean;

import android.content.Context;

import java.io.Serializable;

import xtcore.utils.PreferenceUtils;

public class ConfigBean implements Serializable {

	private static final long serialVersionUID = -2321162315023483354L;
	private static final ConfigBean mConfigBean = new ConfigBean();

	private ConfigBean() {
	};

	public static final synchronized ConfigBean getInstance() {
		return mConfigBean;
	}

	private Context context;
	/**
	 * 设备信息的唯一标识
	 */

	/**
	 * 每次应用程序启动时，生成一个唯一的id
	 */
	private String strSession;
	

	public String getStrSession() {
		return strSession;
	}

	 void setStrSession(String strSession) {
		this.strSession = strSession;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}
	public void createUID(){
		XTCoreConfig.createUID(context);
	}
	/**
	 * 返回当前日志的SeqID.
	 * @return seqID.
	 */
	public int getSeqId() {
		return PreferenceUtils.getPrefInt(ConfigBean.getInstance().getContext(), PreferenceUtils.SP_SEQ_NUM,1);
	}
}
