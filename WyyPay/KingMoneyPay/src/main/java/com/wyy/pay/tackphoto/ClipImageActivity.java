package com.wyy.pay.tackphoto;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.wyy.pay.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("deprecation")
public class ClipImageActivity extends Activity implements OnClickListener,
        OnTouchListener {
    private ImageView screenShotImageView;
    private RelativeLayout rl_tack_capture_cancel, rl_capture_ok, rl_rotate_picture;
    private ClipView clipview;
    private Bitmap bitmap;
    private int screenWidth, screenHeight;
    private int statusBarHeight = 0;
    private int angleInt = 0; // 旋转次数
    private int n = 0;// angleInt % 4 的值，用于计算旋转后图片区域
    // These matrices will be used to move and zoom image
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private DisplayMetrics dm;
    private float minScaleR= 0.5f;// 最小缩放比例
    private static final float MAX_SCALE = 10f;// 最大缩放比例
    // We can be in one of these 3 states
    private static final int NONE = 0;// 初始状态
    private static final int DRAG = 1;// 拖动
    private static final int ZOOM = 2;// 缩放
    private int mode = NONE; // Remember some things for zooming
    private PointF prev = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private File outputFile;
    private int clipWidth;
    private int clipHeight;
    private boolean fromAlbum;
    public static final String PARAM_OUTPUT_FILE = "outputFile";
    public static final String PARAM_FROM_ALBUM = "fromAlbum";
    public static final String PARAM_CLIP_WIDTH = "clipWidth";
    public static final String PARAM_CLIP_HEIGHT = "clipHeight";
    private static final int DEFAULT_CLIP_SIZE = 300;
    private int initRotate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip_image);
        setUpViews();
        setUpListeners();
        Intent intent = getIntent();
        outputFile = (File) intent.getSerializableExtra(PARAM_OUTPUT_FILE);
        clipWidth = intent.getIntExtra(PARAM_CLIP_WIDTH, DEFAULT_CLIP_SIZE);
        clipHeight = intent.getIntExtra(PARAM_CLIP_HEIGHT, DEFAULT_CLIP_SIZE);
        fromAlbum = intent.getBooleanExtra(PARAM_FROM_ALBUM, false);
        if (clipWidth > screenWidth) {
            clipWidth = screenWidth;
            clipHeight = screenWidth;
        }
        clipview.setClipW(clipWidth);
        clipview.setClipH(clipHeight);
        final Uri uri = intent.getData();
        if (uri != null) {
            new Thread() {
            	public void run() {
            		bitmap = getBitmapFromUri(uri);
            		runOnUiThread(new Runnable() {
						@Override
						public void run() {
							afterBitmapLoaded();
						}
					});
            	};
            }.start();
        } else {
//            toastMsg("没有获取到图片源");
            finish();
            return;
        }
    }

	private void afterBitmapLoaded() {
		if (bitmap != null) {
            screenShotImageView.setImageBitmap(bitmap);
        } else {
//            toastMsg("没有获取到图片源");
            finish();
            return;
        }
		
		if (initRotate != 0) {
	        matrix.postRotate(initRotate, screenShotImageView.getWidth() ,
	                screenShotImageView.getHeight());
        }
        center();
        screenShotImageView.setImageMatrix(matrix);
	}

    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
    
    @SuppressLint("NewApi") public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            String imgPath = null;
            if (fromAlbum) {
            	imgPath = getPath(this, uri);
            } else {
                imgPath = uri.getPath();
            }
            
            ExifInterface exifInterface = new ExifInterface(imgPath);
            int result = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (result) {
            case ExifInterface.ORIENTATION_ROTATE_90:
            	initRotate = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
            	initRotate = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:   
            	initRotate = 270;
                break;
            }
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imgPath, options);
            if (options.outWidth < 0 || options.outHeight < 0) {
                return null;
            }  else {
            	if (1.0*screenWidth/screenHeight < 1.0*options.outWidth/options.outHeight) {
            		if (options.outWidth > screenWidth) {
            			options.inSampleSize = (int) (1.0 * options.outWidth / screenWidth + 0.5); 
            		}
            	} else {
            		if (options.outWidth > screenWidth) {
            			options.inSampleSize = (int) (1.0 * options.outHeight / screenHeight + 0.5); 
            		}
            	}
            }
               
            options.inJustDecodeBounds = false;
            Bitmap bmp = null;
            try {
            	bmp = BitmapFactory.decodeFile(imgPath, options);
            } catch (OutOfMemoryError e) {
            	options.inSampleSize *= 2;
            	bmp = BitmapFactory.decodeFile(imgPath, options);
            }
            return bmp;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setUpViews() {
        screenShotImageView = (ImageView) findViewById(R.id.imageView);
        rl_tack_capture_cancel = (RelativeLayout) findViewById(R.id.rl_tack_capture_cancel);
        rl_capture_ok = (RelativeLayout) findViewById(R.id.rl_capture_ok);
        rl_rotate_picture = (RelativeLayout) findViewById(R.id.rl_rotate_picture);
        clipview = (ClipView) findViewById(R.id.clipview);
        dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        screenShotImageView.setImageMatrix(matrix);
    }

    private void setUpListeners() {
    	rl_tack_capture_cancel.setOnClickListener(this);
    	rl_capture_ok.setOnClickListener(this);
        rl_rotate_picture.setOnClickListener(this);
        screenShotImageView.setOnTouchListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.rl_tack_capture_cancel:
            finish();
            break;
        case R.id.rl_capture_ok:
            Bitmap fianBitmap = getClipBitmap();
            try {
                saveMyBitmap(fianBitmap);
            } catch (IOException e) {
//                toastMsg("保存图片失败，请确认是否有足够内存空间");
                e.printStackTrace();
                finish();
                return;
            }
            setResult(RESULT_OK);
            finish();
            break;
        case R.id.rl_rotate_picture:
            n = ++angleInt % 4;
            // 图片旋转-90度
            matrix.postRotate(-90, screenShotImageView.getWidth() / 2,
                    screenShotImageView.getHeight() / 2);
            savedMatrix.postRotate(-90);
            screenShotImageView.setImageMatrix(matrix);
            break;
        }
    }

    private void saveMyBitmap(Bitmap bmp) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
        // 主点按下
        case MotionEvent.ACTION_DOWN:
            savedMatrix.set(matrix);
            // 设置初始点位置
            prev.set(event.getX(), event.getY());
            mode = DRAG;
            break;
        case MotionEvent.ACTION_POINTER_DOWN:
            oldDist = spacing(event);
            // 如果连续两点距离大于10，则判定为多点模式
            if (oldDist > 10f) {
                savedMatrix.set(matrix);
                midPoint(mid, event);
                mode = ZOOM;
            }
            break;
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_POINTER_UP:
            mode = NONE;
            break;
        case MotionEvent.ACTION_MOVE:
            if (mode == DRAG) {
                matrix.set(savedMatrix);
                matrix.postTranslate(event.getX() - prev.x, event.getY()
                        - prev.y);
            } else if (mode == ZOOM) {
                float newDist = spacing(event);
                if (newDist > 10f) {
                    matrix.set(savedMatrix);
                    float scale = newDist / oldDist;
                    matrix.postScale(scale, scale, mid.x, mid.y);
                }
            }
            break;
        }
        view.setImageMatrix(matrix);
        checkScaleRestrict();
        return true;
    }

    /** * 两点的距离 * */
    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);
    }

    /** * 两点的中点 * * */
    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** * 限制最大最小缩放比例 */
    private void checkScaleRestrict() {
        float p[] = new float[9];
        matrix.getValues(p);
        float scale = Math.max(Math.abs(p[0]), Math.abs(p[1]));
        if (mode == ZOOM) {
            if (scale < minScaleR) {
                matrix.setScale(minScaleR, minScaleR);
                center();
            }
            if (scale > MAX_SCALE) {
                matrix.set(savedMatrix);
            }
        }
    }

    private void center() {
        center(true, true);
    }

    /** * 横向、纵向居中 */
    protected void center(boolean horizontal, boolean vertical) {
        Matrix m = new Matrix();
        m.set(matrix);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        m.mapRect(rect);
        float height = rect.height();
        float width = rect.width();
        float deltaX = 0, deltaY = 0;
        if (vertical) {
            // 图片小于屏幕大小，则居中显示。大于屏幕，上方留空则往上移，下方留空则往下移
            int screenHeight = dm.heightPixels;
            if (height < screenHeight) {
                deltaY = (screenHeight - height - statusBarHeight) / 2
                        - rect.top;
            } else if (rect.top > 0) {
                deltaY = -rect.top;
            } else if (rect.bottom < screenHeight) {
                deltaY = screenShotImageView.getHeight() - rect.bottom;
            }
        }
        if (horizontal) {
            int screenWidth = dm.widthPixels;
            if (width < screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            } else if (rect.left > 0) {
                deltaX = -rect.left;
            } else if (rect.right > screenWidth) {
                deltaX = (screenWidth - width) / 2 - rect.left;
            }
        }
        matrix.postTranslate(deltaX, deltaY);
        if (n % 4 != 0) {
            matrix.postRotate(-90 * (n % 4),
                    screenShotImageView.getWidth() / 2,
                    screenShotImageView.getHeight() / 2);
        }
    }

    int titleBarHeight = 0;
    /* 获取矩形区域内的截图 */
    private Bitmap getClipBitmap() {
        getBarHeight();
        Bitmap screenShoot = takeScreenShot();
        Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, clipview.getClipLeft(),
                clipview.getClipTop() + statusBarHeight+titleBarHeight,
                clipview.getClipW(), clipview.getClipH());
        return finalBitmap;
    }

    private void getBarHeight() {
        // 获取状态栏高度
        Rect frame = new Rect();
        this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        int contenttop = this.getWindow()
                .findViewById(Window.ID_ANDROID_CONTENT).getTop();
        // statusBarHeight是上面所求的状态栏的高度
        titleBarHeight = contenttop - statusBarHeight;
    }

    // 获取Activity的截屏
    private Bitmap takeScreenShot() {
        View view = this.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
}
