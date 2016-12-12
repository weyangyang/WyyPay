package com.wyy.pay.sms;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.wyy.pay.utils.ConstantUtils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsObserver extends ContentObserver{
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private Handler handler;
	private final int EXPIRES = 1000 * 60 * 30;
	private final String PRE = "学堂在线";
	private Context context;
	public SmsObserver(Context context, Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.handler = handler;
	}
	@Override
	public void onChange(boolean selfChange) {
		super.onChange(selfChange);
		String code = getSmsCode();
		Message message = handler.obtainMessage();
		message.obj = code;
		message.what = ConstantUtils.SMS_HANDLER_CODE;
		handler.sendMessage(message);
		
	}
	public String getSmsCode() {
		ContentResolver cr = context.getContentResolver();
		String[] projection = new String[] {"body"};
		String where = " date > " + (System.currentTimeMillis() - EXPIRES);
		Cursor cursor = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cursor) {
			return "";
		}
		String code = "";
		while (cursor.moveToNext()) {
			String body = cursor.getString(cursor.getColumnIndex("body"));
			//String address = cursor.getString(cursor.getColumnIndex("address"));
			if (body.contains(PRE)) {
				Pattern pattern = Pattern.compile("[a-zA-Z0-9]{6}");  
	            Matcher matcher = pattern.matcher(body); 
	            if (matcher.find()) {
	            	String res = matcher.group();
	            	if (!TextUtils.isEmpty(res)) {
	            		code = res;
	            		break;
	            	}
	            }
			}
		}
		cursor.close();
		return code;
	}

}
