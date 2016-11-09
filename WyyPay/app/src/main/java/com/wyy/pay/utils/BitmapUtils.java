package com.wyy.pay.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;

public class BitmapUtils {
	private static final String TAG = "BitmapUtil";

	/**
	 * ͨ����Դidת����Bitmap
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * ����Bitmap����
	 * 
	 * @param bitmap
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int screenWidth,
			int screenHight) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scale = (float) screenWidth / w;
		float scale2 = (float) screenHight / h;
		// scale = scale < scale2 ? scale : scale2;
		matrix.postScale(scale, scale);
		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		if (bitmap != null && !bitmap.equals(bmp) && !bitmap.isRecycled()) {
			bitmap.recycle();
			bitmap = null;
		}
		return bmp;// Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/**
	 * �����߰�һ����С����ͼƬ
	 * */
	public static Bitmap scaleImage(byte[] buffer, float size) {
		// ��ȡԭͼ���
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length,
				options);
		// �������ű���
		float reSize = options.outWidth / size;
		if (options.outWidth < options.outHeight) {
			reSize = options.outHeight / size;
		}
		// �����Сͼ��Ŵ�
		if (reSize <= 1) {
			int newWidth = 0;
			int newHeight = 0;
			if (options.outWidth > options.outHeight) {
				newWidth = (int) size;
				newHeight = options.outHeight * (int) size / options.outWidth;
			} else {
				newHeight = (int) size;
				newWidth = options.outWidth * (int) size / options.outHeight;
			}
			bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
			bm = scaleImage(bm, newWidth, newHeight);
			if (bm == null) {
				Log.e(TAG, "convertToThumb, decode fail:" + null);
				return null;
			}
			return bm;
		}
		// ����
		options.inJustDecodeBounds = false;
		options.inSampleSize = (int) reSize;
		bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
		if (bm == null) {
			Log.e(TAG, "convertToThumb, decode fail:" + null);
			return null;
		}
		return bm;
	}

	/**
	 * ���ͼƬ�Ƿ񳬹�һ��ֵ��������С
	 * 
	 * @param view
	 * @param strFileName
	 */
	public static Bitmap convertToThumb(byte[] buffer, float size) {
		// ��ȡԭͼ���
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;
		options.inInputShareable = true;
		Bitmap bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length,
				options);
		// �������ű���
		float reSize = options.outWidth / size;
		if (options.outWidth > options.outHeight) {
			reSize = options.outHeight / size;
		}
		if (reSize <= 0) {
			reSize = 1;
		}
		Log.d(TAG, "convertToThumb, reSize:" + reSize);
		// ����
		options.inJustDecodeBounds = false;
		options.inSampleSize = (int) reSize;
		if (bm != null && !bm.isRecycled()) {
			bm.recycle();
			bm = null;
			Log.e(TAG, "convertToThumb, recyle");
		}
		bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length, options);
		if (bm == null) {
			Log.e(TAG, "convertToThumb, decode fail:" + null);
			return null;
		}
		return bm;
	}

	/**
	 * Bitmap --> byte[]
	 * 
	 * @param bmp
	 * @return
	 */
	private static byte[] readBitmap(Bitmap bmp) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		try {
			baos.flush();
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * Bitmap --> byte[]
	 * 
	 * @param bmp
	 * @return
	 */
	public static byte[] readBitmapFromBuffer(byte[] buffer, float size) {
		return readBitmap(convertToThumb(buffer, size));
	}

	/**
	 * ����Ļ���Ϊ��׼����ʾͼƬ
	 * 
	 * @param iv
	 * @param path
	 * @param screenW
	 * @return
	 */
	public static Bitmap decodeStream(Context context, Intent data, float size) {
		Bitmap image = null;
		try {
			Uri dataUri = data.getData();
			// ��ȡԭͼ���
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			options.inPurgeable = true;
			options.inInputShareable = true;
			BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(dataUri), null, options);
			// �������ű���
			float reSize = (int) (options.outWidth / size);
			if (reSize <= 0) {
				reSize = 1;
			}
			Log.d(TAG, "old-w:" + options.outWidth + ", llyt-w:" + size
					+ ", resize:" + reSize);
			// ����
			options.inJustDecodeBounds = false;
			options.inSampleSize = (int) reSize;
			image = BitmapFactory.decodeStream(context.getContentResolver()
					.openInputStream(dataUri), null, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * ���µĿ������ͼƬ
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
		if (bm == null) {
			return null;
		}
		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		if (bm != null & !bm.isRecycled()) {
			bm.recycle();
			bm = null;
		}
		return newbm;
	}

	/**
	 * fuction: ���ù̶��Ŀ�ȣ��߶���֮�仯��ʹͼƬ�������
	 * 
	 * @param target
	 *            ��Ҫת��bitmap����
	 * @param newWidth
	 *            �����µĿ��
	 * @return
	 */
	public static Bitmap fitBitmap(Bitmap target, int newWidth) {
		int width = target.getWidth();
		int height = target.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidth = ((float) newWidth) / width;
		// float scaleHeight = ((float)newHeight) / height;
		int newHeight = (int) (scaleWidth * height);
		matrix.postScale(scaleWidth, scaleWidth);
		// Bitmap result = Bitmap.createBitmap(target,0,0,width,height,
		// matrix,true);
		Bitmap bmp = Bitmap.createBitmap(target, 0, 0, width, height, matrix,
				true);
		if (target != null && !target.equals(bmp) && !target.isRecycled()) {
			target.recycle();
			target = null;
		}
		return bmp;// Bitmap.createBitmap(target, 0, 0, width, height, matrix,
		// true);
	}

	/**
	 * ����ָ���Ŀ��ƽ��ͼ��
	 * 
	 * @param width
	 * @param src
	 * @return
	 */
	public static Bitmap createRepeater(int width, Bitmap src) {
		int count = (width + src.getWidth() - 1) / src.getWidth();
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int idx = 0; idx < count; ++idx) {
			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		return bitmap;
	}

	/**
	 * ͼƬ������ѹ������
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
		int options = 100;
		while (baos.toByteArray().length / 1024 > 100) { // ѭ���ж����ѹ����ͼƬ�Ƿ����100kb,���ڼ���ѹ��
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// ����ѹ��options%����ѹ��������ݴ�ŵ�baos��
			options -= 10;// ÿ�ζ�����10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// ��ѹ���������baos��ŵ�ByteArrayInputStream��
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// ��ByteArrayInputStream��������ͼƬ
		if (baos != null) {
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (isBm != null) {
			try {
				isBm.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (image != null && !image.isRecycled()) {
			image.recycle();
			image = null;
		}
		return bitmap;
	}

	/**
	 * ͼƬ��������Сѹ������(����BitmapͼƬѹ��)
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap getImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		if (baos.toByteArray().length / 1024 > 1024) {// �ж����ͼƬ����1M,����ѹ������������ͼƬ��BitmapFactory.decodeStream��ʱ���
			baos.reset();// ����baos�����baos
			image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// ����ѹ��50%����ѹ��������ݴ�ŵ�baos��
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// ��ʼ����ͼƬ����ʱ��options.inJustDecodeBounds ���true��
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// ���������ֻ��Ƚ϶���800*480�ֱ��ʣ����ԸߺͿ���������Ϊ
		float hh = 800f;// �������ø߶�Ϊ800f
		float ww = 480f;// �������ÿ��Ϊ480f
		// ���űȡ������ǹ̶��������ţ�ֻ�ø߻��߿�����һ�����ݽ��м��㼴��
		int be = 1;// be=1��ʾ������
		if (w > h && w > ww) {// �����ȴ�Ļ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// ����߶ȸߵĻ����ݿ�ȹ̶���С����
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// �������ű���
		// ���¶���ͼƬ��ע���ʱ�Ѿ���options.inJustDecodeBounds ���false��
		isBm = new ByteArrayInputStream(baos.toByteArray());
		bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
		if (isBm != null) {
			try {
				isBm.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (image != null && !image.isRecycled()) {
			image.recycle();
			image = null;
		}
		return compressImage(bitmap);// ѹ���ñ�����С���ٽ�������ѹ��
	}

	/**
	 * ͨ����Դidת����Bitmap ȫ����ʾ
	 * 
	 * @param context
	 * @param drawableId
	 * @param screenWidth
	 * @param screenHight
	 * @return
	 */
	public static Bitmap ReadBitmapById(Context context, int drawableId,
			int screenWidth, int screenHight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Config.ARGB_8888;
		options.inInputShareable = true;
		options.inPurgeable = true;
		InputStream stream = context.getResources().openRawResource(drawableId);
		Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
		return getBitmap(bitmap, screenWidth, screenHight);
	}

}