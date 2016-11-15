package com.wyy.pay.tackphoto;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;


import com.wyy.pay.R;

import java.io.File;


public class TakePhoto {
	private PhotoResult mPhotoResult = null;
	private Activity mActivity;
	private int outWidth = 660;
	private int outHeight = 390;
	public static final int REQUEST_CODE_CAMERA = 0x8620;
	public static final int REQUEST_CODE_CLIP = 0x8621;
	public static final int REQUEST_CODE_GALLERY = 0x8622;
	
	public static final String CAMERAHEAD = "cameraHead";
	public static final String DEFAULT_SIZE = "default_size";
	public static final String SP_TAKEPHOTOPATH = "takePhotoPath";
	private File mOutputPath;
	private Handler mHandler = new Handler();

	public TakePhoto(Activity activity, PhotoResult photoResult, File outputPath) {
		mActivity = activity;
		mPhotoResult = photoResult;
		mOutputPath = outputPath;
	}
	
	public void setOutWidth(int outWidth) {
		this.outWidth = outWidth;
	}
	
	public void setOutHeight(int outHeight) {
		this.outHeight = outHeight;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case REQUEST_CODE_CAMERA:
				String strCamera = data.getStringExtra(CAMERAHEAD);
				mOutputPath = new File(strCamera);
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						sendImageToClip(Uri.fromFile(mOutputPath), false);
					}
				}, 20);
				break;
				
			case REQUEST_CODE_GALLERY:
				sendImageToClip(data.getData(), true);
				break;
				
			case REQUEST_CODE_CLIP:
				mPhotoResult.onPhotoResult(mOutputPath);
				break;
			}
		}
	}

	private void pickImageFromCamera() {
//		Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Intent intent = new Intent(mActivity, CameraActivity.class);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutputPath.getAbsolutePath());
		intent.putExtra(DEFAULT_SIZE, 2);
		mActivity.startActivityForResult(intent, REQUEST_CODE_CAMERA);
//		mActivity.startActivityForResult(i, REQUEST_CODE_CAMERA);
	};

	private void pickImageFromGallaryAndClip() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		try {
			mActivity.startActivityForResult(intent, REQUEST_CODE_GALLERY);
		} catch (ActivityNotFoundException e) {
//			CommonUtils.toast(mActivity, "请确认手机是否有相册功能");
		}
	}

	private void sendImageToClip(Uri src, boolean fromAlbum) {
		Intent intent = new Intent(mActivity, ClipImageActivity.class);
        intent.putExtra(ClipImageActivity.PARAM_OUTPUT_FILE, mOutputPath);
        intent.putExtra(ClipImageActivity.PARAM_CLIP_WIDTH, outWidth);
        intent.putExtra(ClipImageActivity.PARAM_CLIP_HEIGHT, outHeight);
        intent.putExtra(ClipImageActivity.PARAM_FROM_ALBUM, fromAlbum);
        intent.setData(src);
        mActivity.startActivityForResult(intent, REQUEST_CODE_CLIP);
	}

	public void start() {
		if (mPhotoResult == null || mOutputPath == null
				|| mActivity == null
				|| mOutputPath.getParentFile() == null
				|| !mOutputPath.getParentFile().exists()
				|| TextUtils.isEmpty(mOutputPath.getName())) {
			return;
		}
		
		showMenuDialog();
	}

	private Dialog mMenuDialog;
	private void showMenuDialog() {
		if (mMenuDialog == null) {
			mMenuDialog = new Dialog(mActivity, R.style.Dialog_Fullscreen_Wrap);
			mMenuDialog.setContentView(R.layout.dialog_verify_camera);
			final Button head_camera = (Button) mMenuDialog.findViewById(R.id.head_camera);
			final Button head_picture = (Button) mMenuDialog.findViewById(R.id.head_picture);
			final Button btnCancle = (Button) mMenuDialog.findViewById(R.id.cancle);
			
			head_camera.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.setBackgroundResource(R.color.gray);
					head_picture.setBackgroundResource(R.color.white);
					btnCancle.setBackgroundResource(R.color.white);
					return false;
				}
			});
			head_camera.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pickImageFromCamera();
					mMenuDialog.dismiss();
				}
			});
			
			head_picture.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					v.setBackgroundResource(android.R.color.darker_gray);
					pickImageFromGallaryAndClip();
					mMenuDialog.dismiss();
				}
			});
			head_picture.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.setBackgroundResource(R.color.gray);
					head_camera.setBackgroundResource(R.color.white);
					btnCancle.setBackgroundResource(R.color.white);
					return false;
				}
			});
			btnCancle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mMenuDialog.dismiss();
				}
			});
			btnCancle.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.setBackgroundResource(R.color.gray);
					head_camera.setBackgroundResource(R.color.white);
					head_picture.setBackgroundResource(R.color.white);
					return false;
				}
			});
			mMenuDialog.show();
			setDialogDisplay(mMenuDialog);
		}
		mMenuDialog.show();
		setDialogDisplay(mMenuDialog);
	}
	public void setDialogDisplay(Dialog dialog){
		WindowManager windowManager = mActivity.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.width = (int)(display.getWidth()); //设置宽度
		dialog.getWindow().setAttributes(lp);
	}
	public interface PhotoResult {
		void onPhotoResult(File outputPath);
	}
}
