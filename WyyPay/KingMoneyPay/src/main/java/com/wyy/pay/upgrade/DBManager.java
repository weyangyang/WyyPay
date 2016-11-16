package com.wyy.pay.upgrade;

import android.content.Context;

import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableGoodsDetailBean;

import db.utils.BaseDbBean;

public class DBManager {
	public DBManager() {
		//BaseDbBean.setDBConfig("kingmoney.db", 1);
	}

	public void initTables(Context mContext) {
		initTableNewVersion(mContext);
		//upgradeVersion1();
	}

	private void initTableNewVersion(Context mContext) {

		BaseDbBean.init(mContext, TableCategoryBean.class);
		BaseDbBean.init(mContext, TableGoodsDetailBean.class);

	}


//	private void upgradeVersion1() {
//		List<Object> listExecFromFive = new ArrayList<Object>(); // 2.4.1的更新
//
//		listExecFromFive.add("alter TABLE " + TableUser.TABLE_NAME + " ADD "
//				+ TableUser.COLUMN_SIGN_NUM + " INTEGER");
//		listExecFromFive.add("alter TABLE " + TableUser.TABLE_NAME + " ADD "
//				+ TableUser.COLUMN_SIGN_LAST + " LONG");
//		listExecFromFive.add("alter TABLE " + TableUser.TABLE_NAME + " ADD "
//				+ TableUser.COLUMN_SIGN_DESC + " TEXT");
//		listExecFromFive.add("alter TABLE " + TableUser.TABLE_NAME + " ADD "
//				+ TableUser.COLUMN_SIGN_MOBILE_URL + " TEXT");
//		listExecFromFive.add("alter TABLE " + TableUser.TABLE_NAME + " ADD "
//				+ TableUser.COLUMN_SIGN_MOBILE_DESC + " TEXT");
//		listExecFromFive.addAll(getVersion6Update());
//		BaseDbBean.setDBUpgrade(5, listExecFromFive);
//	}

}
