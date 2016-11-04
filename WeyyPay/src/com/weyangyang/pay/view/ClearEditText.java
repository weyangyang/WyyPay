package com.weyangyang.pay.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.weyangyang.pay.R;
import com.weyangyang.pay.utils.Utils;


public class ClearEditText extends EditText implements OnFocusChangeListener,
		TextWatcher {
	/**
	 * 删除按钮的引用
	 */
	private Drawable mClearDrawable;
	private Drawable passwordDrawable;
	private Drawable passwordDrawableInVisible;
	/**
	 * 控件是否有焦点
	 */
	private boolean hasFoucs = false;
	private boolean isPassword = false;
	public boolean passVisible = false;
	private boolean isCenter = false;
	private Context context;
	private Paint paint;

	public ClearEditText(Context context) {
		this(context, null);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		// 这里构造方法也很重要，不加这个很多属性不能再XML里面定义
		this(context, attrs, android.R.attr.editTextStyle);
		this.context = context;
	}

	public ClearEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ClearEditText, defStyle, 0);
		isPassword = a.getBoolean(R.styleable.ClearEditText_isshowpassword,
				false);
		mClearDrawable = a
				.getDrawable(R.styleable.ClearEditText_deleteDrawable);
		passwordDrawable = a
				.getDrawable(R.styleable.ClearEditText_passwordDrawable);
		passwordDrawableInVisible = a
				.getDrawable(R.styleable.ClearEditText_passwordDrawableInvisible);
		isCenter = a.getBoolean(R.styleable.ClearEditText_isCenter, false);
		a.recycle();
		init(context);
	}

	private void init(Context context) {
		// 获取EditText的DrawableRight,假如没有设置我们就使用默认的图片
		this.context = context;
		if (mClearDrawable == null) {
			mClearDrawable = getResources().getDrawable(
					R.drawable.ic_mini_deletegray);
		}

		mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),
				mClearDrawable.getIntrinsicHeight());
		// 默认设置隐藏图标
		// setClearIconVisible(false);
		// 设置焦点改变的监听
		setOnFocusChangeListener(this);
		// 设置输入框里面内容发生改变的监听
		addTextChangedListener(this);
		if (isPassword && passwordDrawable == null) {
			passwordDrawable = getResources().getDrawable(
					R.drawable.ic_password_visible);

		}
		if (isPassword && passwordDrawableInVisible == null) {
			passwordDrawableInVisible = getResources().getDrawable(
					R.drawable.ic_password_invisible);
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (getText().length() == 0 || !hasFoucs) {
			return;
		}
		int width = this.getMeasuredWidth() - getTotalPaddingRight();
		int height = this.getMeasuredHeight();
		Rect rect = canvas.getClipBounds();
		if (isPassword) {
			float left = width - Utils.dip2px(context, 4)
					- passwordDrawable.getIntrinsicWidth() + rect.left;
			float top = height - Utils.dip2px(context, 16)
					- passwordDrawable.getIntrinsicHeight();
			float bottom = height - Utils.dip2px(context, 16);
			float right = width - Utils.dip2px(context, 4) + rect.left;
			if (isCenter) {
				top = (height - passwordDrawable.getIntrinsicHeight()) / 2;
				bottom = (height + passwordDrawable.getIntrinsicHeight()) / 2;
			}
			if (!passVisible) {
				passwordDrawable.setBounds((int) left, (int) top, (int) right,
						(int) bottom);
				passwordDrawable.draw(canvas);
			} else {
				passwordDrawableInVisible.setBounds((int) left, (int) top,
						(int) right, (int) bottom);
				passwordDrawableInVisible.draw(canvas);
			}
			left = left - Utils.dip2px(context, 17)
					- mClearDrawable.getIntrinsicWidth();
			right = left + mClearDrawable.getIntrinsicWidth();
			top = height - Utils.dip2px(context, 14)
					- mClearDrawable.getIntrinsicHeight();
			bottom = height - Utils.dip2px(context, 14);
			if (isCenter) {
				top = (height - mClearDrawable.getIntrinsicHeight()) / 2;
				bottom = (height + mClearDrawable.getIntrinsicHeight()) / 2;
			}
			mClearDrawable.setBounds((int) left, (int) top, (int) right,
					(int) bottom);
			mClearDrawable.draw(canvas);

		} else {
			float left = width - Utils.dip2px(context, 4)
					- mClearDrawable.getIntrinsicWidth() + rect.left;
			float top = height - Utils.dip2px(context, 14)
					- mClearDrawable.getIntrinsicHeight();
			float bottom = height - Utils.dip2px(context, 14);
			float right = width - Utils.dip2px(context, 4) + rect.left;
			if (isCenter) {
				top = (height - mClearDrawable.getIntrinsicHeight()) / 2;
				bottom = (height + mClearDrawable.getIntrinsicHeight()) / 2;
			}
			mClearDrawable.setBounds((int) left, (int) top, (int) right,
					(int) bottom);
			mClearDrawable.draw(canvas);
		}
	}

	private void setPasswordIconVisible(boolean visible) {
		passVisible = visible;
		/*
		 * if (visible && passwordIcon != null) {
		 * passwordIcon.setVisibility(View.VISIBLE); } else { if (!visible &&
		 * passwordIcon != null) { passwordIcon.setVisibility(View.GONE);
		 * passwordIcon .setImageResource(R.drawable.ic_mini_passwordvisible);
		 * passVisible = false; setInputType(InputType.TYPE_CLASS_TEXT |
		 * InputType.TYPE_TEXT_VARIATION_PASSWORD); } }
		 */
	}

	/**
	 * 因为我们不能直接给EditText设置点击事件，所以我们用记住我们按下的位置来模拟点击事件 当我们按下的位置 在 EditText的宽度 -
	 * 图标到控件右边的间距 - 图标的宽度 和 EditText的宽度 - 图标到控件右边的间距之间我们就算点击了图标，竖直方向就没有考虑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			boolean touchable = false;
			boolean password = false;
			int width = getMeasuredWidth() - getTotalPaddingRight();
			if (!isPassword) {
				float left = width - Utils.dip2px(context, 6)
						- mClearDrawable.getIntrinsicWidth();
				float right = width - Utils.dip2px(context, 6);
				touchable = event.getX() > left && event.getX() < right;
			} else {
				float left = width - Utils.dip2px(context, 6)
						- passwordDrawable.getIntrinsicWidth();
				float right = width - Utils.dip2px(context, 6);
				password = event.getX() > left && event.getX() < right;
				left = left - Utils.dip2px(context, 6)
						- mClearDrawable.getIntrinsicWidth();
				right = left + mClearDrawable.getIntrinsicWidth();
				touchable = event.getX() > left && event.getX() < right;
			}
			if (password) {
				passVisible = !passVisible;
				if (passVisible) {
					setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				} else {
					setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
			if (touchable) {
				this.setText("");
			}
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 当ClearEditText焦点发生变化的时候，判断里面字符串长度设置清除图标的显示与隐藏
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		invalidate();
		requestLayout();
		/*
		 * if (hasFocus) { setClearIconVisible(getText().length() > 0);
		 * setPasswordIconVisible(getText().length() > 0); } else {
		 * setClearIconVisible(false); setPasswordIconVisible(false); }
		 */
	}

	public void onInsideFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		invalidate();
		requestLayout();
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		if (hasFoucs) {
			invalidate();
			requestLayout();

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}
}
