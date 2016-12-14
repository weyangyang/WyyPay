package com.wyy.pay.ui;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.camera.CameraManager;
import com.wyy.pay.decoding.CaptureActivityHandler;
import com.wyy.pay.decoding.InactivityTimer;
import com.wyy.pay.engine.RequestEngine;
import com.wyy.pay.engine.XTAsyncTask;
import com.wyy.pay.ui.dialog.CustomDialog;
import com.wyy.pay.utils.BaseOptions;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.ViewfinderView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Vector;

import netutils.engine.NetReqCallBack;
import xtcore.utils.PreferenceUtils;
import xtcore.utils.SystemUtils;

/**
 * Created by liyusheng on 16/11/9.
 */
public class ScanPayActivity extends BaseActivity implements Callback, View.OnClickListener {

    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ImageView ivPayLogo;
    private TextView tvSumOfMoney;////￥100.00
    private LinearLayout llPayLogoTips;//支付扫码时的logo提示
    private RelativeLayout rlProMessage;//商品扫码时显示的商品信息
    private int payType;//界面类型
    private TextView tvMoneyTitle;//应收金额
    private TextView tvProAdd;//商品扫码时的添加按钮
    private TextView tvProNum;//商品编号
    private TextView tvCannel;//放弃
    private TextView tvScanTips;//扫描提示
    private TextView tvProName;//商品名称
    private TextView tvProPrice;//商品价格
    private TextView tvProSaveNum;//商品库存
    private ImageView ivProScanPic;//显示扫码后的商品图片
    private TableGoodsDetailBean goodsBean;
    private ArrayList<TableGoodsDetailBean> shopingCartList;

    private String orderNo = "";//订单编号
    private double money = 0;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scan_pay);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }


    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();

    }
    public void setProLayoutShow(boolean isShow){
        if(isShow){
            rlProMessage.setVisibility(View.VISIBLE);
        }else {

            rlProMessage.setVisibility(View.GONE);
        }
    }
    public void restartPreviewAfterDelay(long delayMS) {
        if(payType == ConstantUtils.PAY_TYPE_SCAN_PRO){
            llPayLogoTips.setVisibility(View.VISIBLE);
            ivPayLogo.setVisibility(View.GONE);
            tvScanTips.setText(ScanPayActivity.this.getString(R.string.text_barcode_scan_tips));
        }
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        // resetStatusView();
    }
    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        CameraManager.clear();
        super.onDestroy();

    }

    /**
     * 处理扫描结果
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String resultString = result.getText().trim();
       // Toast.makeText(ScanPayActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
        if (TextUtils.isEmpty(resultString)) {
            Toast.makeText(ScanPayActivity.this, "扫码失败，请重试！", Toast.LENGTH_SHORT).show();
        }else {
            if(barcode!=null){
                if(ConstantUtils.PAY_TYPE_SCAN_PRO == payType){
                    llPayLogoTips.setVisibility(View.GONE);
                    rlProMessage.setVisibility(View.VISIBLE);
                    //设置商品数据
                    if(result.getText().contains("http")){
                        Toast.makeText(ScanPayActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
                    }else {
                        String goodsNo = result.getText().trim();
                        goodsBean = (TableGoodsDetailBean) new TableGoodsDetailBean().querySingle(null,TableGoodsDetailBean.COLUMN_USER_ID +"=? AND "+TableGoodsDetailBean.COLUMN_GOODS_BARCODE+" =?",new String[]{Utils.get6MD5WithString("18501053570"),goodsNo},null,null,null);
                      if(goodsBean!=null){
                          tvProName.setText(String.format("商品名称：%s",goodsBean.getGoodsName()));
                          tvProPrice.setText(String.format("商品价格：￥%s",goodsBean.getGoodsPrice()));
                          //ivProScanPic
                          String imgPath = goodsBean.getGoodsImgUrl();
                          if(TextUtils.isEmpty(imgPath)||imgPath.contains("http:")){
                              ImageLoader.getInstance().displayImage(imgPath,ivProScanPic, BaseOptions.getInstance().getProductClipImgOptions());
                          }else {
                              ImageLoader.getInstance().displayImage(String.format("file://%s",imgPath),ivProScanPic, BaseOptions.getInstance().getProductClipImgOptions());
                          }
                          tvProSaveNum.setText(String.format("商品库存：%s",goodsBean.getGoodsStockCount()));
                          tvProNum.setText(String.format("商品编号：%s",result.getText()));

                      }
                    }
                }else if(ConstantUtils.PAY_TYPE_SCAN_PRO_FOR_BARCODE ==payType){
                    Intent intent = new Intent();
                    intent.putExtra(ConstantUtils.INTENT_KEY_PRODUCT_NO,resultString);
                    ScanPayActivity.this.setResult(RESULT_OK,intent);
                    ScanPayActivity.this.finish();
                }else if(ConstantUtils.PAY_TYPE_WEXIN == payType){//微信支付
                    if(SystemUtils.checkAllNet(this)){
                        requestPay4Net(result.getText());
                    }else {
                        Toast.makeText(ScanPayActivity.this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                        ScanPayActivity.this.finish();
                    }

                }else if(ConstantUtils.PAY_TYPE_ALIPAY == payType){//支付宝支付
                    if(SystemUtils.checkAllNet(this)){
                        requestPay4Net(result.getText());
                    }else {
                        Toast.makeText(ScanPayActivity.this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
                        ScanPayActivity.this.finish();
                    }
                }
               // Bitmap bt = BitmapUtils.scaleImage(barcode, 200, 200);
               // ivPayLogo.setImageBitmap(bt);
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("result", resultString);
//                resultIntent.putExtra("bitmap", bt);
//                ScanPayActivity.this.setResult(RESULT_OK, resultIntent);
//                ScanPayActivity.this.finish();
//                return;
            }
            //在此处处理扫码成功后的代码逻辑



        }
    }
    private CustomDialog mCustomDialog;
    private void requestPay4Net(final String authToken) {
        if(Utils.checkUserIsLogin(this)){
            new XTAsyncTask() {
                @Override
                protected void onPreExectue() {

                    if(mCustomDialog==null)
                        mCustomDialog = CustomDialog.createLoadingDialog(ScanPayActivity.this,
                                "正在获取短信验证码", true); }

                @Override
                protected void doInbackgroud() {
                    RequestEngine.getInstance().requestPay(BaseApplication.getUserName(),BaseApplication.getWyyCode(),authToken,orderNo, new NetReqCallBack() {
                        @Override
                        public void getSuccData(int statusCode, String strJson, String strUrl) {
                            try {
                                JSONObject mJSONObject = new JSONObject(strJson);
                                int code = mJSONObject.optInt("Code");
                                String message = mJSONObject.optString("Message");
                                if(code==1){
                                    JSONObject orderObj = 	mJSONObject.optJSONObject("Obj");
                                    if(orderObj!=null) {
                                        final String orderNewNo = orderObj.optString("orderno");
                                        ScanPayActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(ScanPayActivity.this, "支付成功！", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent();
                                                intent.putExtra(ConstantUtils.INTENT_KEY_ORDER_NO,orderNewNo);
                                                ScanPayActivity.this.setResult(RESULT_OK, intent);
                                                ScanPayActivity.this.orderNo="";
                                                ScanPayActivity.this.finish();
                                            }
                                        });
                                    }
                                }else {
                                    if(TextUtils.isEmpty(message))
                                        Toast.makeText(ScanPayActivity.this,message,Toast.LENGTH_SHORT).show();
                                    ScanPayActivity.this.orderNo="";
                                    ScanPayActivity.this.finish();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(ScanPayActivity.this,"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
                                ScanPayActivity.this.orderNo="";
                                ScanPayActivity.this.finish();
                            }
                        }
                    });
                }

                @Override
                protected void onPostExecute() {
                    if(null!=ScanPayActivity.this && mCustomDialog.isShowing())
                        mCustomDialog.dismiss();
                }
            }.execute();
        }else {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
            // }
//	        break;
//	      case KeyEvent.KEYCODE_FOCUS:
//	      case KeyEvent.KEYCODE_CAMERA:
//	        // Handle these events so they don't launch the Camera app
//	        return true;
            // Use volume up/down to turn on light
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                CameraManager.get().setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                CameraManager.get().setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

    @Override
    public void initView() {
        tvProAdd =(TextView)findViewById(R.id.tvProAdd);
        tvProNum =(TextView)findViewById(R.id.tvProNum);
        tvProSaveNum =(TextView)findViewById(R.id.tvProSaveNum);
        tvProName =(TextView)findViewById(R.id.tvProName);
        tvProPrice =(TextView)findViewById(R.id.tvProPrice);
        ivProScanPic =(ImageView)findViewById(R.id.ivProScanPic);
        tvCannel =(TextView)findViewById(R.id.tvCannel);
        tvScanTips =(TextView)findViewById(R.id.tvScanTips);
        rlProMessage = (RelativeLayout) findViewById(R.id.rlProMessage);
        llPayLogoTips = (LinearLayout) findViewById(R.id.llPayLogoTips);
        tvMoneyTitle = (TextView)findViewById(R.id.tvMoneyTitle);
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        tvNavRight.setText("完成");
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        ivPayLogo = (ImageView) findViewById(R.id.ivPayLogo);
        tvSumOfMoney = (TextView) findViewById(R.id.tvSumOfMoney);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        payType =  intent.getIntExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,ConstantUtils.PAY_TYPE_SCAN_PRO_FOR_BARCODE);

        switch (payType){
            case ConstantUtils.PAY_TYPE_SCAN_PRO:
                CameraManager.init(getApplication(),40.0f,100.0f);
                shopingCartList = (ArrayList<TableGoodsDetailBean>) intent.getSerializableExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST);
                tvNavTitle.setText("商品扫码");
                llPayLogoTips.setVisibility(View.VISIBLE);
                ivPayLogo.setVisibility(View.GONE);
                rlProMessage.setVisibility(View.GONE);
                tvMoneyTitle.setVisibility(View.GONE);
                tvSumOfMoney.setVisibility(View.GONE);
                tvProAdd.setVisibility(View.VISIBLE);
                tvScanTips.setText(ScanPayActivity.this.getString(R.string.text_barcode_scan_tips));
                break;
            case ConstantUtils.PAY_TYPE_SCAN_PRO_FOR_BARCODE:
                CameraManager.init(getApplication());
                tvNavTitle.setText("商品扫码");
                llPayLogoTips.setVisibility(View.VISIBLE);
                ivPayLogo.setVisibility(View.GONE);
                rlProMessage.setVisibility(View.GONE);
                tvMoneyTitle.setVisibility(View.GONE);
                tvSumOfMoney.setVisibility(View.GONE);
                tvProAdd.setVisibility(View.GONE);
                tvScanTips.setText(ScanPayActivity.this.getString(R.string.text_barcode_scan_tips));
                break;
            case ConstantUtils.PAY_TYPE_ALIPAY:
                CameraManager.init(getApplication());
                llPayLogoTips.setVisibility(View.VISIBLE);
                rlProMessage.setVisibility(View.GONE);
                tvMoneyTitle.setVisibility(View.VISIBLE);
                tvSumOfMoney.setVisibility(View.VISIBLE);
                tvNavTitle.setText("支付宝付款");
                ivPayLogo.setBackgroundResource(R.drawable.icon_bill_alipay);
                double sumOfMoney = intent.getDoubleExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,0);
                orderNo = intent.getStringExtra(ConstantUtils.INTENT_KEY_ORDER_NO);
                money = sumOfMoney;
                tvSumOfMoney.setText(String.format("￥%.2f",sumOfMoney));
                break;
            case ConstantUtils.PAY_TYPE_WEXIN:
                CameraManager.init(getApplication());
                tvMoneyTitle.setVisibility(View.VISIBLE);
                rlProMessage.setVisibility(View.GONE);
                llPayLogoTips.setVisibility(View.VISIBLE);
                tvSumOfMoney.setVisibility(View.VISIBLE);
                ivPayLogo.setBackgroundResource(R.drawable.icon_bill_wechat);
                tvNavTitle.setText("微信付款");
                orderNo = intent.getStringExtra(ConstantUtils.INTENT_KEY_ORDER_NO);
                 sumOfMoney = intent.getDoubleExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,0);
                money = sumOfMoney;
                tvSumOfMoney.setText(String.format("￥%.2f",sumOfMoney));
                break;
        }


    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
        tvProAdd.setOnClickListener(this);
        tvCannel.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft:
                Intent intent = new Intent();
                if(shopingCartList!=null&&shopingCartList.size()>0){
                    intent.putExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST,(Serializable)shopingCartList);
                }
                setResult(RESULT_OK,intent);
                this.finish();
                break;
            case R.id.tvNavRight:
                 intent = new Intent();
                if(shopingCartList!=null&&shopingCartList.size()>0){
                    intent.putExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST,(Serializable)shopingCartList);
                }
                setResult(RESULT_OK,intent);
                //restartPreviewAfterDelay(100L);//重复扫码
                this.finish();
                break;
            case R.id.tvProAdd:
                if(shopingCartList==null){
                    shopingCartList = new ArrayList<>();
                }
                if(goodsBean!=null){
                    updateCartListData(goodsBean);
                }
                restartPreviewAfterDelay(100L);//重复扫码
                break;
            case R.id.tvCannel:
                goodsBean = null;
                goodsBean = new TableGoodsDetailBean();
                restartPreviewAfterDelay(100L);//重复扫码
                break;
        }
    }
    private void updateCartListData(TableGoodsDetailBean goodsDetailBean) {
        if(shopingCartList!=null&&shopingCartList.size()>0){
            if(shopingCartList!=null&&shopingCartList.size()>0){
                ArrayList<TableGoodsDetailBean> tempProList = new ArrayList<>();
                boolean isLock = false;
                for (TableGoodsDetailBean goodsBean:shopingCartList){
                    if(goodsBean.getGoodsId().equals(goodsDetailBean.getGoodsId())){
                        isLock = true;
                        int goodsCount = goodsBean.getAddGoodsCount();
                        goodsCount+=1;
                        goodsBean.setAddGoodsCount(goodsCount);
                        tempProList.add(goodsBean);
                    }else {
                        tempProList.add(goodsBean);
                    }
                }
                if(!isLock){
                    goodsDetailBean.setAddGoodsCount(1);
                    tempProList.add(goodsDetailBean);
                }
                shopingCartList.clear();
                shopingCartList.addAll(tempProList);
                tempProList.clear();
            }
        }else {
            shopingCartList = new ArrayList<>();
            goodsDetailBean.setAddGoodsCount(1);
            shopingCartList.add(goodsDetailBean);
        }
    }
}
