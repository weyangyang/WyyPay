package com.wyy.pay.bean;

import java.io.Serializable;

public class GetUpgradeDataBean implements Serializable {
	private static final long serialVersionUID = -6240542895699971013L;
	private String strVersion;
	private String strDescription;
	private String strUrl;
	private String strReleaseDate;
	private boolean isForceUpgrade;
	private String strForceUpgrade;
	private int intVersionCode;
	private long apkSize;
	private String apkMD5;
	private String strChannel;

	public String getStrChannel() {
		return strChannel;
	}

	public void setStrChannel(String strChannel) {
		this.strChannel = strChannel;
	}

	public String getApkMD5() {
		return apkMD5;
	}

	public void setApkMD5(String apkMD5) {
		this.apkMD5 = apkMD5;
	}

	public long getApkSize() {
		return apkSize;
	}

	public void setApkSize(long apkSize) {
		this.apkSize = apkSize;
	}

	public int getIntVersionCode() {
		return intVersionCode;
	}
	public void setIntVersionCode(int intVersionCode) {
		this.intVersionCode = intVersionCode;
	}
	public String getStrVersion() {
		return strVersion;
	}
	public void setStrVersion(String strVersion) {
		this.strVersion = strVersion;
	}
	public String getStrDescription() {
		return strDescription;
	}
	public void setStrDescription(String strDescription) {
		this.strDescription = strDescription;
	}
	public String getStrUrl() {
		return strUrl;
	}
	public void setStrUrl(String strUrl) {
		this.strUrl = strUrl;
	}
	public String getStrReleaseDate() {
		return strReleaseDate;
	}
	public void setStrReleaseDate(String strReleaseDate) {
		this.strReleaseDate = strReleaseDate;
	}
	
	public boolean isForceUpgrade() {
		return isForceUpgrade;
	}
	public void setForceUpgrade(boolean isForceUpgrade) {
		this.isForceUpgrade = isForceUpgrade;
	}
	public String getStrForceUpgrade() {
		return strForceUpgrade;
	}
	public void setStrForceUpgrade(String strForceUpgrade) {
		this.strForceUpgrade = strForceUpgrade;
	}

	@Override
	public String toString() {
		return "GetUpgradeDataBean{" +
				"strVersion='" + strVersion + '\'' +
				", strDescription='" + strDescription + '\'' +
				", strUrl='" + strUrl + '\'' +
				", strReleaseDate='" + strReleaseDate + '\'' +
				", isForceUpgrade=" + isForceUpgrade +
				", strForceUpgrade='" + strForceUpgrade + '\'' +
				", intVersionCode=" + intVersionCode +
				", apkSize='" + apkSize + '\'' +
				", apkMD5='" + apkMD5 + '\'' +
				", strChannel='" + strChannel + '\'' +
				'}';
	}
}
