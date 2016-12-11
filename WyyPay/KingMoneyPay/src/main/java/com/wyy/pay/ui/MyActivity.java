package com.wyy.pay.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.WyyCircleImageView;



public class MyActivity extends Activity implements View.OnClickListener {
    private WyyCircleImageView ivAvater;//头像
    private TextView tvShopName;//店铺名称
    private TextView tvNameVerify;//实名认证
    private TextView tvSign;//签到
    private TextView tvAccountSetting;//账号设置
    private TextView tvCashierManage;//收银员管理
//    private TextView tvCashierMateriel;//收银物料
//    private TextView tvTransactionCosts;//交易费率
    private TextView tvCashierSetting;//收银设置
    private TextView tvEvaluateUs;//评价我们
    private TextView tvFeedback;//意见反馈
    private TextView tvAboutUs;//关于我们
    private TextView tvUpdateVersition;//版本更新
    private TextView tvNewVersionHint;//有新版本（提示）
    private TextView tvVersionNum;//版本号
    private ImageView ivShowAuthPic;//认证图标
    ImageLoader mImageLoader = ImageLoader.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
      initView();
        initData();
        initListener();
    }

    private void initListener() {
        ivAvater.setOnClickListener(this);
        tvNameVerify.setOnClickListener(this);
        tvSign.setOnClickListener(this);
        tvAccountSetting.setOnClickListener(this);
        tvCashierManage.setOnClickListener(this);
//        tvCashierMateriel.setOnClickListener(this);
//        tvTransactionCosts.setOnClickListener(this);
        tvCashierSetting.setOnClickListener(this);
        tvEvaluateUs.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvAboutUs.setOnClickListener(this);
        tvUpdateVersition.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initView() {
        ivAvater = (WyyCircleImageView) findViewById(R.id.ivAvater);
        tvShopName  = (TextView) findViewById(R.id.tvShopName);
        tvNameVerify  = (TextView) findViewById(R.id.tvNameVerify);
        tvSign  = (TextView) findViewById(R.id.tvSign);
        tvAccountSetting  = (TextView) findViewById(R.id.tvAccountSetting);
        tvCashierManage  = (TextView) findViewById(R.id.tvCashierManage);
//        tvCashierMateriel  = (TextView) findViewById(R.id.tvCashierMateriel);
//        tvTransactionCosts  = (TextView) findViewById(R.id.tvTransactionCosts);
        tvCashierSetting  = (TextView) findViewById(R.id.tvCashierSetting);
        tvEvaluateUs  = (TextView) findViewById(R.id.tvEvaluateUs);
        tvFeedback  = (TextView) findViewById(R.id.tvFeedback);
        tvAboutUs  = (TextView) findViewById(R.id.tvAboutUs);
        tvUpdateVersition  = (TextView) findViewById(R.id.tvUpdateVersition);
        tvNewVersionHint  = (TextView) findViewById(R.id.tvNewVersionHint);
        tvVersionNum  = (TextView) findViewById(R.id.tvVersionNum);

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.ivAvater://点击头像
                break;
            case R.id.tvNameVerify://实名认证
                break;
            case R.id.tvSign://签到
                break;
            case R.id.tvAccountSetting://账号设置
                break;
            case R.id.tvCashierManage://收银员管理
                break;
//            case R.id.tvCashierMateriel://收银物料
//                break;
//            case R.id.tvTransactionCosts://交易费率
//                break;
            case R.id.tvCashierSetting://收银设置
                 intent = new Intent(this,CashierSettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tvEvaluateUs://评价我们
                String packageName = MyActivity.this.getApplication().getPackageName();
                try {
                    Uri uri = Uri.parse("market://details?id=" + packageName);
                    Intent grade = new Intent(Intent.ACTION_VIEW, uri);
                    MyActivity.this.startActivity(grade);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MyActivity.this, R.string.no_any_market,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.tvFeedback://意见反馈
                intent = new Intent(MyActivity.this,FeedbackActivity.class);
                MyActivity.this.startActivity(intent);
                break;
            case R.id.tvAboutUs://关于我们
                break;
            case R.id.tvUpdateVersition://版本更新
                if(!tvNewVersionHint.isShown()){
                    Toast.makeText(MyActivity.this,
                            R.string.new_version_checking, Toast.LENGTH_SHORT).show();
                }
                update(true);
                break;
        }
    }
    public void update(final boolean isShowUpdateDialog) {


//        Utils.checkApkUpdate(MyActivity.this, new AbsGetUpgradeData() {
//            @Override
//            public void getErrData(int errCode, String errMsg, String strUrl) {
//                super.getErrData(errCode, errMsg, strUrl);
//                toastUpdateApkErr(isShowUpdateDialog);
//            }
//
//            @Override
//            public void getParserErrData(int errCode, String errMsg, String strUrl) {
//                super.getParserErrData(errCode, errMsg, strUrl);
//                toastUpdateApkErr(isShowUpdateDialog);
//            }
//
//            @Override
//            public void getExceptionData(int errCode, String errMsg, String strUrl) {
//                super.getExceptionData(errCode, errMsg, strUrl);
//                toastUpdateApkErr(isShowUpdateDialog);
//            }
//
//            @Override
//            public void getSuccData(final GetUpgradeDataBean mGetUpgradeDataBean, String strUrl) {
//                MyActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mGetUpgradeDataBean!=null&&mGetUpgradeDataBean.getIntVersionCode()>  Utils.getAppVersionCode(MyActivity.this)
//                                &&Utils.getChannel(MyActivity.this).equals(mGetUpgradeDataBean.getStrChannel())
//                                &&!TextUtils.isEmpty(mGetUpgradeDataBean.getStrUrl())){//需要升级
//                            if(isShowUpdateDialog){
//                                if(isShowUpdateDialog && xtcore.utils.PreferenceUtils.getPrefBoolean(MyActivity.this, ConstantUtils.APK_UPDATE_DOWNLOADING,false)){
//                                    Toast.makeText(MyActivity.this,
//                                            MyActivity.this.getResources()
//                                                    .getString(R.string.apk_downloading),
//                                            Toast.LENGTH_SHORT).show();
//                                    return;
//                                }
//                                Intent intent = new Intent(MyActivity.this,ApkUpdateActivity.class);
//                                intent.putExtra(ConstantUtils.APK_UPDATE,mGetUpgradeDataBean);
//                                intent.putExtra(ConstantUtils.IS_GONE_UPDATE_CHECKBOX,true);
//                                MyActivity.this.startActivity(intent);
//                            }else {
//                                tvNewVersionHint.setVisibility(View.VISIBLE);
//                            }
//
//                        }else {
//                            if (isShowUpdateDialog) {
//                                Toast.makeText(MyActivity.this,
//                                        R.string.no_new_version, Toast.LENGTH_SHORT).show();
//                            }
//                            tvNewVersionHint.setVisibility(View.GONE);
//                        }
//                    }
//                });
//
//            }
//        });
    }

    private void toastUpdateApkErr(final boolean isShowUpdateDialog) {
        MyActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isShowUpdateDialog) {
                    Toast.makeText(MyActivity.this,
                            R.string.no_new_version, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
