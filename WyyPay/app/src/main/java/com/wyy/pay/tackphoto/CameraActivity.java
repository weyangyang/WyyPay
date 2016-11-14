package com.wyy.pay.tackphoto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.media.ExifInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.wyy.pay.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@SuppressWarnings({ "deprecation"})
public class CameraActivity extends Activity implements SurfaceHolder.Callback, OnClickListener {
    private RelativeLayout rlTackPhotoCancel, rlPhotoRotate,rlCaptruePicture;//返回,切换前后置摄像头,拍照
    private TextView tv_photo_rotate,tv_captrue;
    private ImageView iv_tack_photo_change,iv_captrue;
    private SurfaceView surface;
    private SurfaceHolder holder;
    private Camera mCamera;//声明相机
    private File headPhotoFile = null;
    private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头
    private MyOrientationListener mOrientationListener;
    /**
     * 最小
     */
    private static final int MIN = 0;
    /**
     * 中间
     */
    private static final int MIDDLE = 1;
    /**
     * 最大
     */
    private static final int MAX = 2;
    private int intPicSize = 2;
	private int mTempRotate = -1;
	private int mRotate = 0;
	private boolean flagCaptrue = false;
//	private boolean flagChange = false;
	private String picPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
        //设置手机屏幕朝向，一共有7种
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_verify_camera);
        picPath = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
        intPicSize = getIntent().getIntExtra(TakePhoto.DEFAULT_SIZE, 1);
        if (!TextUtils.isEmpty(picPath)) {
        	headPhotoFile = new File(picPath);
        }else {
    			try {
					headPhotoFile = new File(getFilesDir(), "pic.jpg");
					headPhotoFile.createNewFile();
					chmod(headPhotoFile.getAbsolutePath(), "777");
				} catch (Exception e) {
					e.printStackTrace();
				}
    		}
        initView();
        //设置监听
        rlTackPhotoCancel.setOnClickListener(this);
        rlPhotoRotate.setOnClickListener(this);
        rlCaptruePicture.setOnClickListener(this);
        mOrientationListener = new MyOrientationListener(this);
		mOrientationListener.enable();
    }
	/** 修改文件的权限,例如"777"等 */
	public  void chmod(String path, String mode) {
		try {
			String command = "chmod " + mode + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (Exception e) {
		}
	}
	private void initView() {
		tv_captrue = (TextView) findViewById(R.id.tv_captrue);
		iv_captrue = (ImageView) findViewById(R.id.iv_captrue);
		 rlTackPhotoCancel = (RelativeLayout) findViewById(R.id.rl_tack_phonto_cancel);
	        rlPhotoRotate = (RelativeLayout) findViewById(R.id.rl_photo_rotate);
	        rlCaptruePicture = (RelativeLayout) findViewById(R.id.rl_captrue_picture);
	        surface = (SurfaceView) findViewById(R.id.surface);
	        tv_photo_rotate = (TextView) findViewById(R.id.tv_photo_rotate);
	        iv_tack_photo_change = (ImageView) findViewById(R.id.iv_tack_photo_change);
	        holder = surface.getHolder();//获得句柄
	        holder.addCallback(this);//添加回调
	        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	       
	}

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    if (mOrientationListener != null) {
	        mOrientationListener.disable();
	    }
	    releaseCamera();
	}
	
	private static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return c;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	mCamera = getCameraInstance();
		if (mCamera == null) {
			Toast.makeText(this, "请检查手机摄像头是否正常", Toast.LENGTH_SHORT).show();
			this.finish();
			return;
		}
		try {
			Parameters params = mCamera.getParameters();
			params.setPictureFormat(PixelFormat.JPEG);
            //Camera.Size previewSize = getPreviewSize(params); //小米bug
           // params.setPreviewSize(previewSize.width, previewSize.height);// 预览图片大小

           // params.setPreviewSize(previewSize.width, previewSize.height);// 预览图片大小 小米bug.

//            Camera.Size pictureSize = getpictureSize(params); 小米bug
//            params.setPictureSize(pictureSize.width, pictureSize.height);// 保存图片大小 小米bug

			//
			setCameraSize(params); //为了修复小米bug.
			//


            mCamera.setParameters(params);//将参数设置到我的camera
            mCamera.setPreviewDisplay(holder);//通过surfaceview显示取景画面
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();//开始预览
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //当surfaceview关闭时，关闭预览并释放资源
    	releaseCamera(); 
        holder = null;
        surface = null;
    }
    //创建jpeg图片回调数据对象
    PictureCallback mPictureCallback = new PictureCallback() {
    	
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	mCamera.stopPreview();
        	if (data != null) {
        		try {
        			Options options = new Options();
					options.inPreferredConfig = Bitmap.Config.RGB_565 ;///////no alpha
					Bitmap  bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);

					bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                    if (cameraPosition == 0) {
                    	bitmap = reverseBitmap(bitmap, 0);
                    }
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(headPhotoFile));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
                    bos.flush();// 刷新此缓冲区的输出流
                    bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
                    if (mTempRotate >= 0 && mTempRotate < 360) {
    		            if (between(mTempRotate, 315, 360) || between(mTempRotate, 0, 45)) {
    		                mRotate = ExifInterface.ORIENTATION_ROTATE_90;
    		            } else if (between(mTempRotate, 45, 135)) {
    		                mRotate = ExifInterface.ORIENTATION_ROTATE_180;
    		            } else if (between(mTempRotate, 135, 225)) {
    		                mRotate = ExifInterface.ORIENTATION_ROTATE_270;
    		            } else {
    		                mRotate = 0;
    		            }
                    } else if (mTempRotate == -1) {
                    	mRotate = ExifInterface.ORIENTATION_ROTATE_90;
                    }
                    ExifInterface exifInterface = new ExifInterface(headPhotoFile.getAbsolutePath());
            		exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, mRotate + "");
            		exifInterface.saveAttributes();
            		rlCaptruePicture.setClickable(true);
                    rlPhotoRotate.setClickable(true);
                    tv_photo_rotate.setText("重拍");
                    iv_tack_photo_change.setBackgroundResource(R.drawable.ic_tack_photo);
                    tv_captrue.setText("确定");
                    iv_captrue.setBackgroundResource(R.drawable.ic_tack_photo_ok);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        	}
        }
    };

	private  void setCameraDisplayOrientation (Activity activity, int cameraId, Camera camera ) {
		CameraInfo info = new CameraInfo();
		Camera.getCameraInfo(cameraId, info);
		int rotation = activity.getWindowManager().getDefaultDisplay()
				.getRotation();
		int degrees = 0;
		switch (rotation) {
		case Surface.ROTATION_0:
			degrees = 0;
			break;
		case Surface.ROTATION_90:
			degrees = 90;
			break;
		case Surface.ROTATION_180:
			degrees = 180;
			break;
		case Surface.ROTATION_270:
			degrees = 270;
			break;
		}
		int result = 0;
		if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
			result = (info.orientation + degrees) % 360;
			result = (360 - result) % 360; // compensate the mirror
		} else { // back-facing
			result = ( info.orientation - degrees + 360 ) % 360 ;
		}
		camera.setDisplayOrientation(result);
	}

    /**
     * @Title: rotateImage
     * @Description: 旋转图片
     * @return Bitmap
     *
     */
//	private Bitmap rotateImage(Bitmap sBmp, int rotate) {
//	    if (rotate != 0) {
//            Matrix matrix = new Matrix();
//            matrix.reset();
//            matrix.setRotate(rotate);
//            Bitmap bmp = Bitmap.createBitmap(sBmp, 0, 0, sBmp.getWidth(), sBmp.getHeight(), matrix, true);
//            sBmp.recycle();
//            return bmp;
//	    } else {
//	        return sBmp;
//	    }
//    }

	/**
	 * @Title: reverseBitmap
	 * @Description: 反转图片 0：水平，1：垂直
	 * @return Bitmap
	 *
	 */
	private Bitmap reverseBitmap(Bitmap bmp, int flag) {
		float[] floats = null;
		switch (flag) {
		case 0: // 水平反转
			floats = new float[] { -1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f };
			break;
		case 1: // 垂直反转
			floats = new float[] { 1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f };
			break;
		}

		if (floats != null) {
			Matrix matrix = new Matrix();
			matrix.setValues(floats);
			return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
		}

		return null;
	}


	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.rl_tack_phonto_cancel:
            //返回
            CameraActivity.this.finish();
            break;

        case R.id.rl_photo_rotate:
			//切换前后摄像头
			changeCamera();
            break;

        case R.id.rl_captrue_picture:
        	// 拍照
        	captrue();
            break;
        }
	}

	/**
	 * @Title: captrue
	 * @Description: 拍照
	 * @return void
	 *
	 */
	private void captrue() {
		if (!flagCaptrue) {
			flagCaptrue = true;
			rlCaptruePicture.setClickable(false);
			rlPhotoRotate.setClickable(false);

		    //快门
		    mCamera.autoFocus(new AutoFocusCallback() {//自动对焦
		        @Override
		        public void onAutoFocus(boolean success, Camera camera) {
		        	//设置参数，并拍照
	                camera.takePicture(null, null, mPictureCallback);//将拍摄到的照片给自定义的对象
		        }
		    });
		} else {
			Intent intent = new Intent();
			intent.putExtra(TakePhoto.CAMERAHEAD, headPhotoFile.getAbsolutePath());
			setResult(Activity.RESULT_OK, intent);
			CameraActivity.this.finish();
		}
	}


	private void releaseCamera() {
		if (mCamera != null) {
			try {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @Title: changeCamera
	 * @Description: 切换前后摄像头
	 * @return void
	 *
	 */
	private void changeCamera() {
//		if (ivPic.getVisibility() == View.GONE) {
		if (!flagCaptrue) {
//			flagChange = true;
    		//切换前后摄像头
			int cameraCount = 0;
			CameraInfo cameraInfo = new CameraInfo();
			cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
			for (int i = 0; i < cameraCount; i++) {
				Camera.getCameraInfo(i, cameraInfo);// 得到每一个摄像头的信息
				if (cameraPosition == 1) {
					// 现在是后置，变更为前置
					// cameraInfo.facing代表摄像头的方位，CAMERA_FACING_FRONT前置
					// CAMERA_FACING_BACK后置
					if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
						mCamera.stopPreview();// 停掉原来摄像头的预览
						mCamera.release();// 释放资源
						mCamera = null;// 取消原来摄像头
						mCamera = Camera.open(i);// 打开当前选中的摄像头
						try {
							Parameters params = mCamera.getParameters();
//				            Camera.Size previewSize = getPreviewSize(params); //小米bug
//				            params.setPreviewSize(previewSize.width, previewSize.height);// 预览图片大小 //小米bug
//				            Camera.Size pictureSize = getpictureSize(params); //小米bug
//				            params.setPictureSize(pictureSize.width, pictureSize.height);// 保存图片大小 //小米bug
							setCameraSize(params); //修复小米bug.
				            mCamera.setParameters(params);//将参数设置到我的camera
				            setCameraDisplayOrientation(CameraActivity.this, i, mCamera);
				            mCamera.setPreviewDisplay(holder);// 通过surfaceview显示取景画面
						} catch (IOException e) {
						}
						mCamera.startPreview();// 开始预览
						cameraPosition = 0;
						break;
					}
				} else {
					// 现在是前置， 变更为后置
					// cameraInfo.facing代表摄像头的方位，CAMERA_FACING_FRONT前置
					// CAMERA_FACING_BACK后置
					if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
						mCamera.stopPreview();// 停掉原来摄像头的预览
						mCamera.release();// 释放资源
						mCamera = null;// 取消原来摄像头
						mCamera = Camera.open(i);// 打开当前选中的摄像头
						try {
							//切换摄像头后重新设置参数，原来没有设置.
							Parameters params = mCamera.getParameters();
							setCameraSize(params); //为了修复小米bug.
							mCamera.setParameters(params);
							//
							mCamera.setPreviewDisplay(holder);// 通过surfaceview显示取景画面
							mCamera.setDisplayOrientation(90);
							setCameraDisplayOrientation(CameraActivity.this, i, mCamera);
						} catch (IOException e) {
							e.printStackTrace();
						}
						mCamera.startPreview();// 开始预览
						cameraPosition = 1;
						break;
					}
				}
			}
    	} else {
//    		flagChange = false;
    		flagCaptrue = false;
    		rlCaptruePicture.setClickable(true);
    		rlPhotoRotate.setClickable(true);
    		// 重拍
    		tv_photo_rotate.setText("切换");
    		iv_tack_photo_change.setBackgroundResource(R.drawable.ic_tack_photo_change);
    		tv_captrue.setText("拍摄");
//    		ivPic.setVisibility(View.GONE);
    		iv_captrue.setBackgroundResource(R.drawable.ic_tack_photo);
    		mCamera.startPreview(); 
    	}
		
	}
	
	/**
	 * @Title: getpictureSize 
	 * @Description: 获取图片大小
	 * @return Camera.Size
	 *   
	 */
	private Camera.Size getpictureSize(Parameters params) {
		if (intPicSize == 0) {
			return getpictureSize(params, MIN);
		} else if (intPicSize == 1) {
			return getpictureSize(params, MIDDLE);
		} else if (intPicSize == 2) {
			return getpictureSize(params, MAX);
		}
//		return getpictureSize(params, intPicSize);//把图片设置成最高清就搞定了
		return null;
	}
	
	private Camera.Size getpictureSize(Parameters params, int type) {
		Camera.Size pictureSize = null;
		List<Camera.Size> pictureSizeList = params.getSupportedPictureSizes(); 
		Collections.sort(pictureSizeList, new ComparatorSizes());
		if (type == MIN) {
			pictureSize = pictureSizeList.get(0);
		} else if (type == MIDDLE) {
			pictureSize = pictureSizeList.get(pictureSizeList.size() / 2);
		} else if (type == MAX) {
			pictureSize = pictureSizeList.get(pictureSizeList.size() - 1);
		}
		return pictureSize;
	}

	/**
	 * @Title: getPreviewSize 
	 * @Description: 获取预览图片大小
	 * @return Camera.Size
	 *   
	 */
	private Camera.Size getPreviewSize(Parameters params) {
		if (intPicSize == 0) {
			return getPreviewSize(params, MIN);
		} else if (intPicSize == 1) {
			return getPreviewSize(params, MIDDLE);
		} else if (intPicSize == 2) {
			return getPreviewSize(params, MAX);
		}
		return null;
	}
	
	private Camera.Size getPreviewSize(Parameters params, int type) {
		Camera.Size previewSize = null;
		List<Camera.Size> previewSizeList = params.getSupportedPreviewSizes(); 
		Collections.sort(previewSizeList, new ComparatorSizes());
		if (type == MIN) {
			previewSize = previewSizeList.get(0);
		} else if (type == MIDDLE) {
			previewSize = previewSizeList.get(previewSizeList.size() / 2);
		} else if (type == MAX) {
			previewSize = previewSizeList.get(previewSizeList.size() - 1);
		}
		return previewSize;
	}

	/**
	 * @ClassName: ComparatorValues 
	 * @Description: 排序
	 *   
	 */
	public static final class ComparatorSizes implements Comparator<Camera.Size> {
		@Override
		public int compare(Camera.Size object1, Camera.Size object2) {
			int m1 = object1.height * object1.width;
			int m2 = object2.height * object2.width;
			if (m1 > m2) {
				return 1;
			} else if (m1 < m2) {
				return -1;
			} else {
				return 0;
			}
		}

	}
	
	/**
	 * @ClassName: MyOrientationListener 
	 * @Description: 横竖屏切换监听
	 */
	private final class MyOrientationListener extends OrientationEventListener {
        public MyOrientationListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
        	Log.i("liang", orientation+"");
            mTempRotate = orientation;
        }
	}
	
	private boolean between(int value, int start, int end) {
	    if (value >= start && value < end) {
	        return true;
	    }
	    return false;
	}
	
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, 
			double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}
	//-----以下方法为了修改部分手机预览尺寸和图片尺寸不支持的问题.

	private void setCameraSize(Parameters params) {
		//
		Camera.Size previewSize = getCurrentScreenSize(params.getSupportedPreviewSizes(), 1);
		if (previewSize == null) {
			previewSize = getPreviewSize(params);
		}
		params.setPreviewSize(previewSize.width, previewSize.height);
		List<Camera.Size> pictureSizes = params.getSupportedPictureSizes();
		Camera.Size pictureSize = getCurrentScreenSize(pictureSizes, 1);
		if (pictureSize == null) {
			pictureSize = getpictureSize(params);
		}
		params.setPictureSize(pictureSize.width, pictureSize.height);
	}

	/**
	 * 获取手机的宽度
	 *
	 * @param context
	 */
	private int getScreenMessage(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}
	/**
	 * 获得最接近频幕宽度的尺寸
	 *
	 * @param sizeList
	 * @param n
	 *            放大几倍 （>0)
	 * @return
	 */
	private Camera.Size getCurrentScreenSize(List<Camera.Size> sizeList, int n) {
		if (sizeList != null && sizeList.size() > 0) {
			int screenWidth = getScreenMessage(this) * n;
			int[] arry = new int[sizeList.size()];
			int temp = 0;
			for (Camera.Size size : sizeList) {
				arry[temp++] = Math.abs(size.width < size.height?size.width:size.height - screenWidth);
			}
			temp = 0;
			int index = 0;
			for (int i = 0; i < arry.length; i++) {
				if (i == 0) {
					temp = arry[i];
					index = 0;
				} else {
					if (arry[i] < temp) {
						index = i;
						temp = arry[i];
					}
				}
			}
			return sizeList.get(index);
		}
		return null;
	}
}