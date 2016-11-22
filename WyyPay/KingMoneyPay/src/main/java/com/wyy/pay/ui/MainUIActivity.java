package com.wyy.pay.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.utils.Utils;


public class MainUIActivity extends TabActivity implements View.OnClickListener {
    private TabHost mTabHost;
    private TabHost.TabSpec spec;
    public static final String TAB_ID_CASHIER = "cashier";//收银
    private static final String TAB_ID_ORDER = "order";//订单
    private static final String TAB_ID_PRO_MANAGE = "pro_manage";
    private static final String TAB_ID_MY = "my";
    private int widgetW = 0, currIndex = 0;
    private ImageView iv_cursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        initView();
        initData();
        mTabHost = getTabHost();
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                changedTabShow(tabId);
                int arg0 = 0;
                if (widgetW == 0) {
                    widgetW = mTabHost.getTabWidget().getWidth() / 4;
                }
                if (tabId.equals(TAB_ID_ORDER)) {
                    arg0 = 0;
                } else if (tabId.equals(TAB_ID_CASHIER)) {
                    arg0 = 1;
                } else if (tabId.equals(TAB_ID_PRO_MANAGE)) {
                    arg0 = 2;
                } else if (tabId.equals(TAB_ID_MY)) {
                    arg0 = 3;
                }
                Animation animation = new TranslateAnimation(widgetW
                        * currIndex, widgetW * arg0, 0, 0);
                currIndex = arg0;
                animation.setFillAfter(true);/* True:图片停在动画结束位置 */
                animation.setDuration(300);
                iv_cursor.startAnimation(animation);
                setImageViewWidth(widgetW);
            }
        });
        addBottomTab(ProOrderActivity.class,R.drawable.tab_activity_main_ui,R.string.text_order,TAB_ID_ORDER);
        addBottomTab(CashierActivity.class,R.drawable.tab_activity_main_ui,R.string.text_main_ui_pay,TAB_ID_CASHIER);
        addBottomTab(ProManageActivity.class,R.drawable.tab_activity_main_ui,R.string.text_main_ui_pro_manage,TAB_ID_PRO_MANAGE);
        addBottomTab(MyActivity.class,R.drawable.tab_activity_main_ui,R.string.text_main_ui_my,TAB_ID_MY);
        setDefaultTab(0);


    }

    private void initData() {
        TableCategoryBean bean = new TableCategoryBean();
        String categoryName = "默认分类";
        String categoryId = Utils.get6MD5WithString(categoryName);
        bean.setCategoryId(categoryId);
        bean.setCategoryName(categoryName);
        bean.setUserId(Utils.get6MD5WithString("18501053570"));
        long cTime = 1462381810;
        bean.setCreateTime(cTime);//2016/11/15 0:0:0
        bean.insert(true,TableCategoryBean.COLUMN_CATEGORY_ID,categoryId);

    }

    private void addBottomTab(Class<?> cls,int imgId,int strId,String tabId) {
        Intent intentCount = new Intent(this, cls);
        Drawable countDrawable = getResources().getDrawable(
                imgId);
        View viewCount = inittabwidget(countDrawable,
                getResources().getString(strId));
        spec = mTabHost.newTabSpec(tabId).setIndicator(viewCount)
                .setContent(intentCount);
        mTabHost.addTab(spec);
    }


    private View inittabwidget(Drawable drawable, String message) {
        LayoutInflater mInflater = LayoutInflater.from(this);
        View view = mInflater.inflate(R.layout.tab_widget, null);
        ImageView mImageView = (ImageView) view.findViewById(R.id.tab_image);
        mImageView.setImageDrawable(drawable);
        TextView mTextView = (TextView) view.findViewById(R.id.tab_text);
        mTextView.setText(message);
        return view;
    }
    /* 初始化动画 */
    private void InitImageView() {
        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        iv_cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     *  设置图片宽度
     * @param width
     */
    private void setImageViewWidth(int width) {
        if (width != iv_cursor.getWidth()) {
            android.widget.RelativeLayout.LayoutParams laParams = (android.widget.RelativeLayout.LayoutParams) iv_cursor
                    .getLayoutParams();
            laParams.width = width;
            iv_cursor.setLayoutParams(laParams);
        }
    }

    private void changedTabShow(String tabId) {
        if (TAB_ID_ORDER.equals(tabId)) {
            setIntTabId(1);
        } else if (TAB_ID_CASHIER.equals(tabId)) {
            setIntTabId(2);
        } else if (TAB_ID_PRO_MANAGE.equals(tabId)) {
            setIntTabId(3);
        } else if (TAB_ID_MY.equals(tabId)) {
            setIntTabId(4);
        }
    }

    private int intTabId;

    public void setIntTabId(int intTabId) {
        this.intTabId = intTabId;
    }

    public int getIntTabId() {
        return intTabId;
    }
    private void initView() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        iv_cursor = (ImageView) findViewById(R.id.iv_cursor);
        InitImageView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}
