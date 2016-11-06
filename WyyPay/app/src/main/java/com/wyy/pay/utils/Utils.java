package com.wyy.pay.utils;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class Utils {
	public static String getScreenOrientation(Context context) {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return "landscape";
		} else {
			return "portrait";
		}
	}


	public static String getPushKey(Context context) {
		ApplicationInfo appInfo;
		String key = null;
		try {
			appInfo = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);
			key = appInfo.metaData.getString("PUSH_KEY");
		} catch (NameNotFoundException e) {
			return "";
		}
		if (key != null) {
			return key;
		} else {
			return "";
		}
	}

	public static String getUserAgent(Context context) {
		String webUserAgent = null;
		if (context != null) {
			try {
				Class sysResCls = Class
						.forName("com.android.internal.R$string");
				Field webUserAgentField = sysResCls
						.getDeclaredField("web_user_agent");
				Integer resId = (Integer) webUserAgentField.get(null);
				webUserAgent = context.getString(resId);
			} catch (Throwable ignored) {
			}
		}
		if (TextUtils.isEmpty(webUserAgent)) {
			webUserAgent = "Mozilla/5.0 (Linux; U; Android %s) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 %sSafari/533.1";
		}

		Locale locale = Locale.getDefault();
		StringBuffer buffer = new StringBuffer();
		// Add version
		final String version = Build.VERSION.RELEASE;
		if (version.length() > 0) {
			buffer.append(version);
		} else {
			// default to "1.0"
			buffer.append("1.0");
		}
		buffer.append("; ");
		final String language = locale.getLanguage();
		if (language != null) {
			buffer.append(language.toLowerCase());
			final String country = locale.getCountry();
			if (country != null) {
				buffer.append("-");
				buffer.append(country.toLowerCase());
			}
		} else {
			// default to "en"
			buffer.append("en");
		}
		// add the model for the release build
		if ("REL".equals(Build.VERSION.CODENAME)) {
			final String model = Build.MODEL;
			if (model.length() > 0) {
				buffer.append("; ");
				buffer.append(model);
			}
		}
		final String id = Build.ID;
		if (id.length() > 0) {
			buffer.append(" Build/");
			buffer.append(id);
		}
		return String.format(webUserAgent, buffer, "Mobile ");
	}

	public static String getStrTimeStamp() {
		return String.valueOf(System.currentTimeMillis());
	}

	public static String getTimeZone() {
		return TimeZone.getDefault().getID();
	}

	/**
	 * 
	 * @param edtView
	 *            ：
	 * @param hasFocus
	 *            ：是否出在焦点状态
	 * @param defaultText
	 *            ：edittext中的默认字符
	 * 
	 */
	public static void setEdtFocusText(EditText edtView, boolean hasFocus,
			String defaultText) {
		if (!hasFocus) {

			if (edtView.getText().toString().trim().equals("")) {
				edtView.setText(defaultText);

			}
		} else {
			if (edtView.getText().toString().trim().equals(defaultText)) {
				edtView.setText("");
			}
		}

	}

	/**
	 * 
	 * @param edtView
	 *            ：
	 * @param hasFocus
	 *            ：是否出在焦点状态
	 * @param defaultText
	 *            ：edittext中的默认字符
	 * @param isHidePwd
	 *            :edittext 中显示的是可见字符还是星号
	 */
	public static void setEdtFocusPwd(EditText edtView, boolean hasFocus,
			String defaultText, boolean isHidePwd) {

		// edtView.setTransformationMethod(HideReturnsTransformationMethod
		// .getInstance());

		if (hasFocus) {
			if (edtView.getText().toString().trim().equals(defaultText)) {

				if (isHidePwd) {
					edtView.setTransformationMethod(PasswordTransformationMethod
							.getInstance());
				}
				edtView.setText("");

			}

		} else {

			if (edtView.getText().toString().trim().equals("")) {
				if (isHidePwd) {
					edtView.setTransformationMethod(HideReturnsTransformationMethod
							.getInstance());
				}
				edtView.setText(defaultText);

			}
		}
	}

	public static boolean isEmail(String email) {
		if (null == email || "".equals(email))
			return false;

		Pattern p = Pattern
				.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

	public static boolean isPhoneNumber(String mobiles) {

		if (mobiles == null || mobiles.equals(""))
			return false;
		Pattern pattern = Pattern.compile("^1\\d{10,10}$");
		Matcher matcher = pattern.matcher(mobiles);
		return matcher.matches();

	}

	public static boolean isPassword(String pwd) {

		if (pwd == null || pwd.equals(""))
			return false;

		Pattern pattern = Pattern.compile("^([0-9a-zA-Z]){6,18}");
		Matcher matcher = pattern.matcher(pwd);
		return matcher.matches();
	}

	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 获取手机系统允许旋转.
	 * 
	 * @param context
	 *            activity上下文
	 * @return true 系统允许.
	 */
	public static boolean isSystemRotate(Context context) {
		int flag = Settings.System.getInt(context.getContentResolver(),
				Settings.System.ACCELEROMETER_ROTATION, 0);
		boolean isSystemRotate = flag == 1 ? true : false;
		return isSystemRotate;
	}


	/**
	 * 判断一个String是否是完整的Http Url. 这个方法现在要尽可能的弱化，因为服务器已经保证字幕链接一定是完整的.
	 * 
	 *            要判断的String
	 * @return 是完整的Url则返回true.
	 */
	public static boolean isHttpUrl(String url) {
		try {

			if (url == null) {
				return false;
			}
			Pattern p = Pattern
					.compile("^((https?|ftp|news)://)?([a-z]([a-z0-9\\-]*[\\.。])+([a-z]{2}"
							+ "|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)"
							+ "|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])"
							+ ")(/[a-z0-9_\\-\\.~]+)*(/([a-z0-9_\\-\\.]*)(\\?[a-z0-9+_\\-\\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$");
			Matcher m = p.matcher(url.toLowerCase());
			if (m.matches() || url.contains("http")) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static void hideKeyBoard(Context context) {

		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(((Activity) context).getWindow()
				.getDecorView().getWindowToken(), 0);

	}

	/**
	 * 计算字符串的长度：中文占两位，非中文占1位
	 * 
	 * @param str
	 * @return
	 */
	public static int countCharacter(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		int count = 0;
		char[] chs = str.toCharArray();
		for (int i = 0; i < chs.length; i++) {
			count += (chs[i] > 0xff) ? 2 : 1;
		}
		return count;
	}

//	public static String checkUsername(Context context, String str) {
//		if (TextUtils.isEmpty(str)) {
//			return context.getString(R.string.register_username_empty);
//		}
//		if (countCharacter(str) > 16) {
//			return context.getString(R.string.register_username_more);
//		}
//		if (countCharacter(str) < 2) {
//			return context.getString(R.string.register_username_less);
//		}
//		Pattern p = Pattern.compile("(^[\u4e00-\u9fa5-_\\w]+)");
//		Matcher m = p.matcher(str);
//		if (m.matches()) {
//			return "";
//		} else {
//			return context.getString(R.string.register_username_invalid);
//		}
//	}

	public static String getSubString(String string, int count) {
		if (string == null || string.length() == 0)
			return "";

		int length = Utils.countCharacter(string);

		if (length <= count)
			return string;

		char[] chs = string.toCharArray();
		StringBuilder sBuilder = new StringBuilder();
		for (int i = 0; i < chs.length && count > 0; i++) {
			if (chs[i] > 0xff) {

				count = count - 2;

			} else {
				count = count - 1;
			}
			sBuilder.append(chs[i]);
		}

		return sBuilder.toString() + " ...";
	}

	public static String datetime2date(String datetime) {
		if (datetime == null || datetime.trim().equals(""))
			return "";
		String[] arr = datetime.split(" ");
		if (arr.length > 0) {
			return arr[0];
		}
		return "";
	}

	/**
	 * 判断一个时间点是否过去.
	 * 
	 * @param timestamp
	 *            时间戳.
	 * @param def
	 *            默认是否是已经过去.
	 * @return true该时间点
	 */
	public static boolean isTimePast(String timestamp, boolean def) {
		int offset = TimeZone.getDefault().getRawOffset();
		long client = System.currentTimeMillis() - offset;
		Date now = new Date(client);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		int status = 0;
		boolean past = def;
		Date date;
		try {
			date = sdf.parse(timestamp);
			past = date.before(now);
		} catch (ParseException e2) {

		} catch (Exception e) {

		}
		return past;
	}
	public static String convertUTC2Local(String strUtc) {
		if (TextUtils.isEmpty(strUtc)) {
			return "";
		}
		int offset = TimeZone.getDefault().getRawOffset();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		try {
			date = sdf.parse(strUtc);
			long timeStamp = date.getTime() + offset;
			Date localDate = new Date(timeStamp);
			String localString = sdf.format(localDate);
			if (!TextUtils.isEmpty(localString)) {
				return localString;
			}
		} catch (ParseException e2) {

		} catch (Exception e) {

		}
		return strUtc;
	}
	public static String date2StrWithLines(String strDate) {
		return strDate.replace(" ", "\n");
	}
	public static long date2timeStampNotISO8601(String strDate) {
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		format2.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
		Date date = null;
		try {
			date = format2.parse(strDate);
		} catch (Exception e) {

		}
		if (date == null) {
			return 0;
		} else {
			return date.getTime();
		}

	}

	/**
	 *
	 * date = date.replace("Z", " UTC");//注意是空格+UTC
	 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");//注意格式化的表达式
	 Date d = format.parse(date );
     */
	public static long date2timeStamp(String strDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(strDate);
		} catch (Exception e) {

		}
		if (date == null) {
			try {
				date = format2.parse(strDate);
			} catch (Exception e) {

			}
		}
		if (date == null) {
			return 0;
		} else {
			return date.getTime();
		}

	}

	/**
	 * 获取该时间是在几天之后.可能为负.
	 * 
	 * @param timeStamp
	 * @return
	 */
	public static int getDaysAfterNow(String timeStamp) {
		int offset = TimeZone.getDefault().getRawOffset();
		long client = System.currentTimeMillis() - offset;
		Date now = new Date(client);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date;
		int days = -1;
		try {
			date = sdf.parse(timeStamp);
			long time = date.getTime() - client;
			double tmp = time / (1000.0 * 3600 * 24);
			days = (int) Math.ceil(tmp);
		} catch (ParseException e2) {

		} catch (Exception e) {

		}
		return days;
	}

	public static String day2Month(int days) {
		if (days > 30) {
			return days / 30 + "月";
		} else {
			return days + "天";
		}
	}

	public static int getStatusHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int height = frame.top;
		if (height <= 0) {
			Class<?> c = null;
			Object obj = null;
			Field field = null;
			int x = 0, sbar = 0;
			try {
				c = Class.forName("com.android.internal.R$dimen");
				obj = c.newInstance();
				field = c.getField("status_bar_height");
				x = Integer.parseInt(field.get(obj).toString());
				sbar = activity.getResources().getDimensionPixelSize(x);
				height = sbar;

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return height;
	}


	public static void isEmulator() {
		if (Build.MODEL.equals("sdk") || Build.MODEL.equals("google_sdk")) {
			throw new RuntimeException("is emulator");
		}
	}

	public static boolean hasSDCard() {
		// 判断sd卡是否存在
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);

		return sdCardExist;
	}
	public static String ToDBC(String input) {  
		   char[] c = input.toCharArray();  
		   for (int i = 0; i< c.length; i++) {  
		       if (c[i] == 12288) {  
		         c[i] = (char) 32;  
		         continue;  
		       }if (c[i]> 65280&& c[i]< 65375)  
		          c[i] = (char) (c[i] - 65248);  
		       }  
		   return new String(c);  
	}
	private static long timeStamp = 0;
	public static void printTimeLog(String title) {
		long t = System.currentTimeMillis();
		//Log.i(title, t - timeStamp + "");
		timeStamp = t;
	}
	public static char[] chars = {'零','一','二','三','四','五','六','七','八','九'};
	public static char[] digits = {'十','百','千','万'};
	public static String num2Char(int number) {
		StringBuffer sb = new StringBuffer();
		int tmp = number;
		int digit = 0;
		while (digit < digits.length) {
			if (tmp % 10 == 0 && digit == 0 && tmp / 10 != 0) {
				//处理最后一位为0的特殊情况.
			}else {
				sb.insert(0, chars[tmp % 10]);
			}
			tmp = tmp / 10;
			if (tmp == 0) {
				break;
			}
			if (tmp % 10 != 0) {
				sb.insert(0, digits[digit]);
			}
			digit ++;
			if (tmp == 1 && digit == 1) {
				break;
			}
		}
		return sb.toString();
	}
	public static boolean isVerifyCourse(String strVerify) {
		if (!TextUtils.isEmpty(strVerify) && Boolean.parseBoolean(strVerify.toLowerCase())) {
			return true;
		}
		return false;
		
	}
	/** 
	 * 返回当前程序版本名 
	 */  
	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
	        if (versionName == null || versionName.length() <= 0) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
	        Log.e("VersionInfo", "Exception", e);  
	    }  
	    return versionName;  
	}

//	public static void checkApkUpdate(final Activity activity,AbsGetUpgradeData mAbsGetUpgradeData) {
//
//		final String currentChannel = Utils.getChannel(activity);
//		final int currentVersionCode = Utils.getAppVersionCode(activity);
//		if(!SystemUtils.checkAllNet(activity)){
//			return;
//		}
//		if(mAbsGetUpgradeData==null){
//			ExternalFactory.getInstance().createGetUpgrade().getUpgrade(UserUtils.getDefaultHttpHeader(),currentChannel,currentVersionCode, new AbsGetUpgradeData() {
//				@Override
//				public void getSuccData(final GetUpgradeDataBean mGetUpgradeDataBean, String strUrl) {
//					activity.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							if(mGetUpgradeDataBean!=null&&mGetUpgradeDataBean.getIntVersionCode()> currentVersionCode
//									&&currentChannel.equals(mGetUpgradeDataBean.getStrChannel())
//									&&!TextUtils.isEmpty(mGetUpgradeDataBean.getStrUrl())){//需要升级
//								Intent intent = new Intent(activity,ApkUpdateActivity.class);
//								intent.putExtra(IntentKey.APK_UPDATE,mGetUpgradeDataBean);
//								activity.startActivity(intent);
//							}
//
//						}
//					});
//
//				}
//			});
//		}else {
//			ExternalFactory.getInstance().createGetUpgrade().getUpgrade(UserUtils.getDefaultHttpHeader(),currentChannel,currentVersionCode,mAbsGetUpgradeData);
//		}
//
//	}
	//安装APK
	public static void installAPK(String  filePath,Context context) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//广播里面操作需要加上这句，存在于一个独立的栈里
		intent.setDataAndType(Uri.fromFile(new File(filePath)),"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
	/**
	 * 获取 Channel
	 *
	 * @param context
	 * @return
	 */
	public static String getChannel(Context context) {

		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
					context.getPackageName(), PackageManager.GET_META_DATA);

			Bundle bundle = ai.metaData;
			if (bundle != null) {
				return bundle.getString("UMENG_CHANNEL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static int getAppVersionCode(Context context) {  
	    int versionCode = 0;  
	    try {  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionCode = pi.versionCode;
	    } catch (Exception e) {  
	        Log.e("VersionInfo", "Exception", e);  
	    }  
	    return versionCode;  
	}  
	public static boolean isToday(long timeStamp) {
		long day = timeStamp / (1000L * 3600 * 24);
		long now = System.currentTimeMillis() / (1000L * 3600 * 24);
		return day == now;
	}
	public static String formatDate(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dest = new SimpleDateFormat("yyyy.MM.dd");
		try {
			Date date = source.parse(strDate);
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	private static String formatDateISO4YMMDD(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dest = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = source.parse(strDate);
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static boolean isBackground(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(context.getPackageName())) {
				if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
					//Log.i("后台", appProcess.processName);
					return true;
				}else{
					//Log.i("前台", appProcess.processName);
					return false;
				}
			}
		}
		return false;
	}
	private static String formatDataISOGMT4YMMDD(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		source.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dest = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = new Date(source.parse(strDate).getTime());
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDateISO4YMMDD(strDate);
	}
	public static String formatDataISO86014YMMDD(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
		SimpleDateFormat dest = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = new Date(source.parse(strDate).getTime());
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(strDate);
		return formatDataISOGMT4YMMDD(strDate);
	}
	public static boolean isApkDebugable(Context context) {
		try {
			ApplicationInfo info= context.getApplicationInfo();
			return (info.flags&ApplicationInfo.FLAG_DEBUGGABLE)!=0;
		} catch (Exception e) {

		}
		return false;
	}

	public static String formatDataISO8601(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ");
		SimpleDateFormat dest = new SimpleDateFormat("yy/MM/dd");
		try {
			Date date = new Date(source.parse(strDate).getTime());
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(strDate);
		return formatDataISOGMT(strDate);
	}
	private static String formatDataISOGMT(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		source.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dest = new SimpleDateFormat("yy/MM/dd");
		try {
			Date date = new Date(source.parse(strDate).getTime());
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formatDateYYMMDD(strDate);
	}
	private static String formatDateYYMMDD(String strDate) {
		SimpleDateFormat source = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		source.setTimeZone(TimeZone.getTimeZone("GMT"));
		SimpleDateFormat dest = new SimpleDateFormat("yy/MM/dd");
		try {
			Date date = new Date(source.parse(strDate).getTime());
			return dest.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static Drawable getDrawableAround(Context mContext, int resId) {
		Drawable drawable = mContext.getResources().getDrawable(resId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		return drawable;
	}
	public static String getEncodingString(String strUrl) {
		try {
			URI uri = new URI(strUrl);
			return uri.toASCIIString().replaceAll(" ", "%20");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return strUrl;
	}
}
