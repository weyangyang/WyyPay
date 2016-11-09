package com.wyy.pay.ui;

import android.app.Activity;
import android.app.DownloadManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.SDUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.net.bean.GetUpgradeDataBean;

import java.io.File;

import xtcore.utils.Md5Util;
import xtcore.utils.PreferenceUtils;
import xtcore.utils.SystemUtils;

public class ApkUpdateActivity extends Activity {
	private CheckBox cbxUpdate;
	private Button btnUpdateOK,btnUpdateCancel;
	private TextView tvUpdateContent,tvApkVersionName,tvApkSize;
	private GetUpgradeDataBean updateBean;
	private boolean isGoneCheckbox;
	private long downloadId;
	private DownloadManager manager ;
	private boolean isApkDownloaded;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apk_update);
		manager =(DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		getIntentData();
		isApkDownloaded = checkApkIsDownloaded();
		initView();
		initData();
		initListener();

	}

	private boolean checkApkIsDownloaded() {
		String apkPath = PreferenceUtils.getPrefString(this, ConstantUtils.DOWNLOAD_APK_FILE_PATH,"");
		if(!TextUtils.isEmpty(apkPath)) {
			final File file = new File(apkPath);
			if (file.exists() && updateBean!=null && updateBean.getApkMD5().equals(Md5Util.getFileMD5(file))) {
				return true;
			}
		}
		return  false;
	}

	private void getIntentData() {
		 updateBean = (GetUpgradeDataBean) getIntent().getSerializableExtra(ConstantUtils.APK_UPDATE);
		isGoneCheckbox = getIntent().getBooleanExtra(ConstantUtils.IS_GONE_UPDATE_CHECKBOX,false);
		if(updateBean==null){
			ApkUpdateActivity.this.finish();
		}

	}



	public void initView() {
		cbxUpdate = (CheckBox) findViewById(R.id.cbxUpdate);
		btnUpdateOK = (Button) findViewById(R.id.btnUpdateOK);
		btnUpdateCancel = (Button) findViewById(R.id.btnUpdateCancel);
		tvUpdateContent = (TextView) findViewById(R.id.tvUpdateContent);
		tvApkVersionName = (TextView) findViewById(R.id.tvApkVersionName);
		tvApkSize = (TextView) findViewById(R.id.tvApkSize);

		if(updateBean!=null && updateBean.isForceUpgrade()){
			btnUpdateCancel.setVisibility(View.GONE);
			cbxUpdate.setVisibility(View.GONE);
			this.setFinishOnTouchOutside(false);
		}
	}

	public void initData() {
		if(isGoneCheckbox){
			cbxUpdate.setVisibility(View.GONE);
		}else {
			cbxUpdate.setChecked(PreferenceUtils.getPrefBoolean(this,ConstantUtils.APK_UPDATE_CHECKBOX_STATE,false));
		}
		if(isApkDownloaded){
			btnUpdateOK.setText(getResources().getString(R.string.quick_install_apk));
			tvApkSize.setText(getResources().getString(R.string.new_apk_downloaded_tips));

		}else {
			btnUpdateOK.setText(getResources().getString(R.string.quick_update_apk));
			tvApkSize.setText(String.format(this.getResources().getString(R.string.new_apk_size), SDUtils.getSize(updateBean.getApkSize())));
		}
		tvUpdateContent.setText(updateBean.isForceUpgrade()?updateBean.getStrForceUpgrade() : updateBean.getStrDescription());
		tvApkVersionName.setText(String.format(this.getResources().getString(R.string.apk_new_version),updateBean.getStrVersion()));

	}

	public void initListener() {
		btnUpdateOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//立即更新,下载apk

				PreferenceUtils.setPrefBoolean(ApkUpdateActivity.this, ConstantUtils.APK_UPDATE_DOWNLOADING,true);

				PreferenceUtils.setPrefString(ApkUpdateActivity.this,ConstantUtils.APK_FILE_MD5,updateBean.getApkMD5());
				if(checkApkIsDownloaded()){
					//install apk
					String apkPath = PreferenceUtils.getPrefString(ApkUpdateActivity.this,ConstantUtils.DOWNLOAD_APK_FILE_PATH,"");
					Utils.installAPK(apkPath,ApkUpdateActivity.this);
					PreferenceUtils.setPrefBoolean(ApkUpdateActivity.this, ConstantUtils.APK_UPDATE_DOWNLOADING,false);
				}else {
					if(!SystemUtils.checkAllNet(ApkUpdateActivity.this)){
						Toast.makeText(ApkUpdateActivity.this,ApkUpdateActivity.this.getResources().getString(R.string.net_error),Toast.LENGTH_SHORT).show();
						return;
					}
					downloadAPK(updateBean.getStrUrl());
					Toast.makeText(ApkUpdateActivity.this,ApkUpdateActivity.this.getResources().getString(R.string.apk_downloading),Toast.LENGTH_SHORT).show();
				}
					ApkUpdateActivity.this.finish();
			}
		});
		btnUpdateCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ApkUpdateActivity.this.finish();
			}
		});
		cbxUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				PreferenceUtils.setPrefBoolean(ApkUpdateActivity.this, ConstantUtils.APK_UPDATE_CHECKBOX_STATE,b);
			}
		});
	}
	private void downloadAPK(String apkUrl) {
		PreferenceUtils.setPrefInt(this,ConstantUtils.APK_CURRENT_VERSION_CODE,Utils.getAppVersionCode(this));
		DownloadManager.Query query = new DownloadManager.Query();
		query.setFilterById(downloadId);
		query.setFilterByStatus(DownloadManager.STATUS_RUNNING);//正在下载
		Cursor c = manager.query(query);
		if(c.moveToNext()){
			//正在下载中，不重新下载
		}else{
			DownloadManager.Request down=new DownloadManager.Request (Uri.parse(apkUrl));
			down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);
			//显示在下载界面，即下载后的文件在系统下载管理里显示
			down.setVisibleInDownloadsUi(true);
			down.setTitle("微洋洋收银");
			down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
			//设置下载后文件存放的位置，在SDCard/Android/data/你的应用的包名/files/目录下面
			String fileName = String.format("xuetangx_%s.apk",updateBean.getIntVersionCode());
			//down.setDestinationInExternalFilesDir(this, null,fileName);

			down.setDestinationInExternalPublicDir("xuetangx",fileName);
			downloadId = manager.enqueue(down);
        }

	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(updateBean!=null && updateBean.isForceUpgrade()){
				return false;
			}else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
