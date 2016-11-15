package com.wyy.pay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wyy.pay.R;
import com.wyy.pay.utils.BaseOptions;

public class WyyCircleImageView extends ImageView {

	private Bitmap mSrc; // 图片
	private int mRadius = 0; // 圆角大小
	private int mWidth = 0;// 控件宽度
	private int mHeight = 0;// 控件高度
	private int mBorder = 0; // 控件边框粗细
	private int mBorderColor = Color.parseColor("#19000000");
//	private int mBorderColor = Color.parseColor("#00ffffff");

	private boolean isFirstVisit = true;
	private boolean isWidthBig = true;// 默认图片是宽大于高
	private boolean isShowShowdow = false;

	private int mShadowId = R.drawable.ic_launcher;

	/**
	 * @param context
	 */
	public WyyCircleImageView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public WyyCircleImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyleAttr
	 */
	public WyyCircleImageView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		//ImageLoader imageLoader = ImageLoader.getInstance();
		for (int i = 0; i < attrs.getAttributeCount(); i++) {

			if (attrs.getAttributeName(i).equals("src")) {

//				mSrc = imageLoader.loadImageSync("drawable://"
//						+ attrs.getAttributeResourceValue(i, 0), BaseOptions.getInstance().getAvaterOptions());

				 mSrc = BitmapFactory.decodeResource(getResources(),
				 attrs.getAttributeResourceValue(i, 0));

				if (mSrc.getHeight() > mSrc.getWidth()) {
					isWidthBig = false;
				}
			}
		}

		// -----------------------------------
		TypedArray myattrs = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.WyyCircleImageView, defStyleAttr, 0);

		for (int i = 0; i < myattrs.getIndexCount(); i++) {
			int index = myattrs.getIndex(i);
			switch (index) {
			case R.styleable.WyyCircleImageView_border:
				mBorder = myattrs.getDimensionPixelSize(index, 0);
				break;
			case R.styleable.WyyCircleImageView_radius:
				mRadius = myattrs.getDimensionPixelSize(index, 0);
				break;
			case R.styleable.WyyCircleImageView_borderColor:
				mBorderColor = myattrs.getColor(index, mBorderColor);
				break;

			case R.styleable.WyyCircleImageView_showShadow:
				isShowShowdow = myattrs.getBoolean(index, isShowShowdow);
				break;
			default:
				break;
			}
		}
		myattrs.recycle();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub

		if (mRadius != 0) {
			mWidth = 2 * (mRadius + mBorder);
			mHeight = 2 * (mRadius + mBorder);
			setMeasuredDimension(mWidth, mHeight);

		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub

		setImgMeasure();

		if (mBorder != 0 && mBorderColor != 0) {

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(mBorderColor);
			canvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, paint);
		}

		int[] dim = getDesirSize();

		// mSrc = compressImg(mSrc);
		if (isShowShowdow) {
			Bitmap shadowBMP = Bitmap.createScaledBitmap(
					BitmapFactory.decodeResource(getResources(), mShadowId),
					mWidth, mHeight, false);
			canvas.drawBitmap(shadowBMP, 0, 0, null);
		}
		mSrc = Bitmap.createScaledBitmap(mSrc, dim[0], dim[1], false);
		canvas.drawBitmap(createCircleImage(mSrc), mBorder, mBorder, null);

		// invalidate();

	}

	private void setImgMeasure() {
		if (mWidth == 0) {
			mWidth = getWidth();
		}
		if (mHeight == 0) {
			mHeight = getHeight();
		}

		mWidth = Math.min(mWidth, mHeight);
		mHeight = mWidth;

		if (mRadius == 0) {
			mRadius = (mWidth - 2 * mBorder) / 2;
		}
	}

	private int[] getDesirSize() {
		// 获得图片的宽高
		int ow = mSrc.getWidth();
		int oh = mSrc.getHeight();
		float ratio = (float) ow / (float) oh;

		int nh;
		int nw;
		int desirLen = 2 * mRadius;
		if (isWidthBig) {
			nh = desirLen;
			nw = (int) (desirLen * ratio);
		} else {
			nw = desirLen;
			nh = (int) ((float) desirLen / ratio);
		}

		return new int[] { nw, nh };

	}

	/**
	 * 根据原图和变长绘制圆形图片
	 * 
	 * @param source
	 * @return
	 */
	private Bitmap createCircleImage(Bitmap source) {

		final Paint paint = new Paint();
		paint.setAntiAlias(true);
		Bitmap bm = Bitmap.createBitmap(mRadius * 2, mRadius * 2,
				Config.ARGB_8888);

		Canvas canvas = new Canvas(bm);
		canvas.drawCircle(mRadius, mRadius, mRadius, paint); // 画一个圆形
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		int x = (source.getWidth() - mRadius * 2) / 2;
		int y = (source.getHeight() - mRadius * 2) / 2;

		canvas.drawBitmap(source, -x, -y, paint);

		return bm;
	}

	@Override
	public void setImageResource(int resId) {
		// TODO Auto-generated method stub
		super.setImageResource(resId);

		// mSrc = BitmapFactory.decodeResource(getResources(),
		// attrs.getAttributeResourceValue(i, 0));

		mSrc = BitmapFactory.decodeResource(getResources(), resId);
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		// TODO Auto-generated method stub
		super.setImageBitmap(bm);
		mSrc = bm;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		// TODO Auto-generated method stub
		super.setScaleType(scaleType);
	}
}
