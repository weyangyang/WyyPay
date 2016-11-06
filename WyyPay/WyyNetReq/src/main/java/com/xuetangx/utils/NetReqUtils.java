package com.xuetangx.utils;

import android.text.TextUtils;

public class NetReqUtils {
	public static int parseTime(String strTime) {
		if (TextUtils.isEmpty(strTime)) {
			return -1;
		}
		String[] times = strTime.trim().split(":");
		int sum = 0;
		int rank = 1;
		for (int i = 0 ; i < times.length; i ++) {
			int pos = times.length - i - 1;
			try {
				int s = Integer.parseInt(times[pos]);
				sum += s * rank;
				rank *= 60;
			}catch(NumberFormatException e) {
				
			}
		}
		return sum;
	}

}
