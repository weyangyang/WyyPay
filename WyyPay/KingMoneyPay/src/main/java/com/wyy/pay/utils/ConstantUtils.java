package com.wyy.pay.utils;

/**
 * Created by liyusheng on 16/11/6.
 */

public class ConstantUtils {
    public static final String APK_UPDATE_CHECKBOX_STATE = "apk_update_checkbox_state";
    public static final String APK_UPDATE_DOWNLOADING = "apk_update_downloading";
    public static final String APK_FILE_MD5 = "apk_file_md5";
    public static final String APK_CURRENT_VERSION_CODE = "apk_current_version_code";
    public static final String DOWNLOAD_APK_FILE_PATH = "download_apk_file_path";
    public final static String APK_UPDATE = "apk_update";
    public final static String IS_GONE_UPDATE_CHECKBOX = "is_gone_update_checkbox";
    public static final String P_IMAGE = "wyypay/mobilev1/image/";
    public static final String INTENT_KEY_PAY_TYPE = "intent_key_pay_type";
    public static final String INTENT_KEY_PRODUCT_NO = "intent_key_product_no";//商品条码
    public static final String INTENT_KEY_PRODUCT_CATEGORY = "intent_key_product_category";//商品分类
    public static final String INTENT_KEY_PRODUCT_BEAN = "intent_key_product_bean";
    public static final String INTENT_KEY_SUM_OF_MONEY = "intent_key_sum_of_money";
    public static final int PAY_TYPE_ALIPAY = 10;//支付宝支付
    public static final int PAY_TYPE_WEXIN = 20;//微信支付
    public static final int PAY_TYPE_SCAN_PRO = 30;//扫描商品生成订单
    public static final int PAY_TYPE_SCAN_PRO_FOR_BARCODE = 40;//扫描商品获取条码信息
    public static final String INTENT_KEY_FROM_ACTIVITY_TYPE = "intent_key_from_activity_type";//界面类型
    public static final int FROM_PRODUCT_MANAGE_ACTIVITY_PRODUCT_LIST_ITEM = 60;//ProManageActivity
    public static final int FROM_POPUP_WINDOW_ADD_PRODUCT = 50;//popupwindow add button
    public static final int FROM_PRO_DETAIL_ACTIVITY = 70;//from pro detail activity
    public static final int FROM_POPUP_WINDOW_CATEGORY_MANAGE = 80;//from_popup_window_category_manage

}
