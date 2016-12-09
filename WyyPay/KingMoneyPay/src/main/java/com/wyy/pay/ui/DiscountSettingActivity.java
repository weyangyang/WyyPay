package com.wyy.pay.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableDiscountNumBean;
import com.wyy.pay.ui.dialog.CustomDiscountDialog;
import com.wyy.pay.ui.dialog.CustomProgressDialog;
import com.wyy.pay.utils.RemoveZeroType;
import com.wyy.pay.utils.SubstringUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XTTagContainerLayout;

import java.util.ArrayList;
import java.util.List;

import db.utils.BaseDbBean;
import db.utils.TableDataListener;
import xtcore.utils.PreferenceUtils;

public class DiscountSettingActivity extends BaseActivity  {
	private CheckBox cbxRemoveZero;
	private LayoutInflater inflater;
	private XTTagContainerLayout modelTag,processTag;
	ArrayList<TableDiscountNumBean> zheList;
	ArrayList<TableDiscountNumBean> yuanList;
	private TableDataListener<TableDiscountNumBean> dataListener;
	private CustomProgressDialog initDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_discount_setting);
		inflater = LayoutInflater.from(this);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();

	}
	@Override
	public void initView() {
		modelTag = (XTTagContainerLayout) findViewById(R.id.modelTag);
		processTag = (XTTagContainerLayout) findViewById(R.id.processTag);
		cbxRemoveZero = (CheckBox) findViewById(R.id.cbxRemoveZero);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("优惠折扣设置");
	}

	@Override
	public void initData() {
		zheList = new ArrayList<>();
		yuanList = new ArrayList<>();
		getDataFromDB();
		updateTagView();
		boolean isChecked = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_DISCOUNT_SWITCH,false);
		cbxRemoveZero.setChecked(isChecked);

	}

	private void updateTagView() {
		processTag.setTags(yuanList);
		processTag.setOnTagClickListener(new XTTagContainerLayout.OnTagClickListener() {
			@Override
			public void onDeleteTagClick(int position, TableDiscountNumBean bean) {
				bean.rawDelete(TableCategoryBean.COLUMN_USER_ID+" =? AND " +TableDiscountNumBean.COLUMN_DISCOUNT_ID+" =?",new String[]{Utils.get6MD5WithString("18501053570"),bean.getDiscountId()});
				processTag.removeTag(position);
			}

			@Override
			public void onTagTextBgOnClick(int position, String text) {
				if("+".equals(text)){
					showAddDiscount(2);
				}
			}
		});
		modelTag.setTags(zheList);
		modelTag.setOnTagClickListener(new XTTagContainerLayout.OnTagClickListener() {
			@Override
			public void onDeleteTagClick(int position, TableDiscountNumBean bean) {
				bean.rawDelete(TableCategoryBean.COLUMN_USER_ID+" =? AND " +TableDiscountNumBean.COLUMN_DISCOUNT_ID+" =?",new String[]{Utils.get6MD5WithString("18501053570"),bean.getDiscountId()});
				modelTag.removeTag(position);
			}

			@Override
			public void onTagTextBgOnClick(int position, String text) {
				if("+".equals(text)){
					showAddDiscount(1);
				}
			}

		});

		boolean isChecked = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_DISCOUNT_SWITCH,false);
		modelTag.setSelectAllTagView(isChecked);
		processTag.setSelectAllTagView(isChecked);
	}

	private static final int TABLE_CATEGORY_CHANGED = 0;
	private static final int INIT_COMPLETE = 1;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what){
				case TABLE_CATEGORY_CHANGED:
					getDataFromDB();
					break;
				case INIT_COMPLETE:
					if(initDialog!=null){
						initDialog.dismiss();
					}
					updateTagView();
			}
		}
	};

	private void showAddDiscount(final int type) {
		CustomDiscountDialog discountDialog = new CustomDiscountDialog(DiscountSettingActivity.this,type, R.style.DefaultDialog, new CustomDiscountDialog.InfoCallback() {
            @Override
            public void btnOkOnClick(String text) {
				TableDiscountNumBean numTableBean = new TableDiscountNumBean();
				if(text.contains(".")){
					String temp = SubstringUtils.substringAfter(text,".");
					if("0".equals(temp)){
						text = SubstringUtils.substringBefore(text,".");
					}
				}
				String showText = "";
				if(type ==1){
					showText = String.format("%s折",text);
				}else  if(type==2){
					showText = String.format("减%s元",text);
				}
				numTableBean.setUserId(Utils.get6MD5WithString("18501053570"));
					numTableBean.setCreateTime(System.currentTimeMillis());
					String discountId = Utils.get6MD5WithString(showText);
					numTableBean.setDiscountId(discountId);
					numTableBean.setType(type);
					numTableBean.setShowText(showText);
					numTableBean.setDiscountNum(Float.parseFloat(text));
					numTableBean.insert(true,TableDiscountNumBean.COLUMN_DISCOUNT_ID,discountId);
            }
        });
		discountDialog.show();
	}

	private void getDataFromDB() {
		if (initDialog == null) {
			initDialog = CustomProgressDialog.createLoadingDialog(this);
		}
		if (!initDialog.isShowing()) {
			initDialog.show();
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				String orderBy = TableDiscountNumBean.COLUMN_CREATE_CATEGORY_TIEM+" DESC";
				ArrayList<TableDiscountNumBean> tableList = new TableDiscountNumBean().query(null,TableCategoryBean.COLUMN_USER_ID +"=?",new String[]{Utils.get6MD5WithString("18501053570")},null,null,orderBy);
				if(tableList==null||tableList.size()==0){
					int temp = 9;
					for (int i=1;i<13;i++){
						TableDiscountNumBean numTableBean = new TableDiscountNumBean();
						numTableBean.setUserId(Utils.get6MD5WithString("18501053570"));
						if(i<6){
							String showText = String.format("减%s元",i*5);
							numTableBean.setCreateTime(System.currentTimeMillis());
							String discountId = Utils.get6MD5WithString(showText);
							numTableBean.setDiscountId(discountId);
							numTableBean.setType(2);
							numTableBean.setShowText(showText);
							numTableBean.setDiscountNum(i*5);
							numTableBean.insert(true,TableDiscountNumBean.COLUMN_DISCOUNT_ID,discountId);
							yuanList.add(numTableBean);
						}else if(i==6){
							numTableBean.setCreateTime(System.currentTimeMillis());
							String discountId = Utils.get6MD5WithString(String.format("+%s",2));
							numTableBean.setDiscountId(discountId);
							numTableBean.setType(2);
							numTableBean.setDiscountNum(-200);
							numTableBean.setShowText("+");
							numTableBean.setCreateTime(2082733263);
							numTableBean.insert(true,TableDiscountNumBean.COLUMN_DISCOUNT_ID,discountId);
							yuanList.add(numTableBean);
						}
						else if(i>6&&i<12){
							numTableBean.setCreateTime(System.currentTimeMillis());
							String showText = String.format("%s折",temp);
							String discountId = Utils.get6MD5WithString(showText);
							numTableBean.setDiscountId(discountId);
							numTableBean.setType(1);
							numTableBean.setDiscountNum(temp);
							numTableBean.setShowText(showText);
							numTableBean.insert(true,TableDiscountNumBean.COLUMN_DISCOUNT_ID,discountId);
							zheList.add(numTableBean);
							temp --;
						}else if(i==12){
							numTableBean.setCreateTime(System.currentTimeMillis());
							String discountId = Utils.get6MD5WithString(String.format("+%s",1));
							numTableBean.setDiscountId(discountId);
							numTableBean.setType(1);
							numTableBean.setShowText("+");
							numTableBean.setDiscountNum(-100);
							numTableBean.setCreateTime(2082733261);
							numTableBean.insert(true,TableDiscountNumBean.COLUMN_DISCOUNT_ID,discountId);
							zheList.add(numTableBean);
						}

					}
				}else if(tableList.size()>0){
					zheList.clear();
					yuanList.clear();
					for(TableDiscountNumBean bean :tableList){
						if(bean.type ==1){
							zheList.add(bean);
						}else {
							yuanList.add(bean);
						}
					}
				}

				handler.sendEmptyMessage(INIT_COMPLETE);
			}
		}).start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		BaseDbBean.unregisterContentObserver(TableDiscountNumBean.TABLE_NAME, dataListener);
	}

	@Override
	public void initListener() {
		dataListener = new TableDataListener<TableDiscountNumBean>(handler) {
			@Override
			public void onDataChanged(int type, TableDiscountNumBean obj) {
				if (type == TableDataListener.TYPE_UPDATE) {
					handler.sendEmptyMessage(TABLE_CATEGORY_CHANGED);
				}
				if (type == TableDataListener.TYPE_ADD) {
					handler.sendEmptyMessage(TABLE_CATEGORY_CHANGED);
				}
				if (type == TableDataListener.TYPE_DELETE
						|| type == TableDataListener.TYPE_RAW_DELETE) {
					return;
				}
			}
		};
		TableDiscountNumBean.registerContentObserver(TableDiscountNumBean.TABLE_NAME,dataListener);
		cbxRemoveZero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceUtils.setPrefBoolean(DiscountSettingActivity.this,PreferenceUtils.SP_DISCOUNT_SWITCH,isChecked);
				modelTag.setSelectAllTagView(isChecked);
				processTag.setSelectAllTagView(isChecked);
			}
		});
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DiscountSettingActivity.this.finish();
			}
		});
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


}