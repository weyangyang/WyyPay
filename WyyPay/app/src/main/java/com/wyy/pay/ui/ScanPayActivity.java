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
import com.wyy.pay.R;
import com.wyy.pay.camera.CameraManager;
import com.wyy.pay.decoding.CaptureActivityHandler;
import com.wyy.pay.decoding.InactivityTimer;
import com.wyy.pay.utils.BitmapUtils;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

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
    private TextView tvSumOfMoney;
    private LinearLayout llPayLogoTips;//支付扫码时的logo提示
    private RelativeLayout rlProMessage;//商品扫码时显示的商品信息
    private int payType;//界面类型
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scan_pay);
        super.onCreate(savedInstanceState);
        CameraManager.init(getApplication());

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
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        // resetStatusView();
    }
    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
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
        String resultString = result.getText();
        Toast.makeText(ScanPayActivity.this, result.getText(), Toast.LENGTH_SHORT).show();
        if (resultString.equals("")) {
            Toast.makeText(ScanPayActivity.this, "Scan failed!", Toast.LENGTH_SHORT).show();
        }else {
            if(barcode!=null){
                if(ConstantUtils.PAY_TYPE_SCAN_PRO == payType){
                    rlProMessage.setVisibility(View.VISIBLE);
                    //设置商品数据
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
            restartPreviewAfterDelay(1000L);//重复扫码


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
        rlProMessage = (RelativeLayout) findViewById(R.id.rlProMessage);
        llPayLogoTips = (LinearLayout) findViewById(R.id.llPayLogoTips);
        tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
        tvNavRight.setText("作废");
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        ivPayLogo = (ImageView) findViewById(R.id.ivPayLogo);
        tvSumOfMoney = (TextView) findViewById(R.id.tvSumOfMoney);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        payType =  intent.getIntExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,10);
        switch (payType){
            case ConstantUtils.PAY_TYPE_SCAN_PRO:
                tvNavTitle.setText("商品扫码");
                llPayLogoTips.setVisibility(View.GONE);
                rlProMessage.setVisibility(View.GONE);

                break;
            case ConstantUtils.PAY_TYPE_ALIPAY:
                llPayLogoTips.setVisibility(View.VISIBLE);
                rlProMessage.setVisibility(View.GONE);
                tvNavTitle.setText("支付宝付款");
                ivPayLogo.setBackgroundResource(R.drawable.icon_bill_alipay);
                float sumOfMoney = intent.getFloatExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,0.00f);
                tvSumOfMoney.setText(String.format("¥\r\r%s",sumOfMoney));
                break;
            case ConstantUtils.PAY_TYPE_WEXIN:
                rlProMessage.setVisibility(View.GONE);
                llPayLogoTips.setVisibility(View.VISIBLE);
                ivPayLogo.setBackgroundResource(R.drawable.icon_bill_wechat);
                tvNavTitle.setText("微信付款");
                 sumOfMoney = intent.getFloatExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,0.00f);
                tvSumOfMoney.setText(String.format("¥\r\r%s",sumOfMoney));
                break;
        }


    }

    @Override
    public void initListener() {
        tvNavLeft.setOnClickListener(this);
        tvNavRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvNavLeft:
                this.finish();
                break;
            case R.id.tvNavRight:
                Toast.makeText(this,"不要了",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
