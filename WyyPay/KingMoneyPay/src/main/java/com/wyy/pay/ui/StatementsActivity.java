package com.wyy.pay.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wyy.pay.R;
import com.wyy.pay.adapter.StatementsListAdapter;
import com.wyy.pay.bean.StatementsDiscountBean;
import com.wyy.pay.bean.TableCategoryBean;
import com.wyy.pay.bean.TableDiscountNumBean;
import com.wyy.pay.bean.TableGoodsDetailBean;
import com.wyy.pay.engine.RequestEngine;
import com.wyy.pay.engine.XTAsyncTask;
import com.wyy.pay.ui.dialog.CustomDialog;
import com.wyy.pay.utils.ConstantUtils;
import com.wyy.pay.utils.SubstringUtils;
import com.wyy.pay.utils.Utils;
import com.wyy.pay.view.XListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import netutils.engine.NetReqCallBack;
import xtcore.utils.PreferenceUtils;
import xtcore.utils.SystemUtils;

public class StatementsActivity extends BaseActivity implements View.OnClickListener, StatementDiscountPopWindow.DiscountPopWindowListener, Statements2PayPopWindow.Statements2PayPopWindowListener {
	private RelativeLayout rlDiscount;//用于显示优惠信息
	private TextView tvDiscount;//显示优惠信息
	private Button btnDelete;//删除优惠信息
	private Button tvOrderToPay;//收款
	private TextView tvDiscountTypeT,tvDiscountType,tvDiscountTM,tvDiscountM;
	private double tempPayTotal =0;
	private TextView tvCount;//显示总购物数量
	private TextView tvSum;//合计多少钱
	private TextView tvOrderTotalMoney;//应收多少钱
	private XListView statementsListView;//显示商品列表
	private ArrayList<TableGoodsDetailBean> shopingCartList;//购物车
	private StatementsListAdapter adapter;
	private  double toPayMoney =0;//实际支付的钱
	private CustomDialog mCustomDialog;
	private volatile static String orderNo = "";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_statements);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		statementsListView = (XListView) findViewById(R.id.statementsListView);
		statementsListView.setPullRefreshEnable(false);
		statementsListView.setPullLoadEnable(false);
		if(shopingCartList==null){
			shopingCartList = new ArrayList<>();
		}
		adapter = new StatementsListAdapter(this);
		statementsListView.setAdapter(adapter);
		tvOrderTotalMoney = (TextView) findViewById(R.id.tvOrderTotalMoney);
		tvSum = (TextView) findViewById(R.id.tvSum);
		tvCount = (TextView) findViewById(R.id.tvCount);
		tvDiscountM = (TextView) findViewById(R.id.tvDiscountM);
		tvDiscountTM = (TextView) findViewById(R.id.tvDiscountTM);
		tvDiscountType = (TextView) findViewById(R.id.tvDiscountType);
		tvDiscountTypeT = (TextView) findViewById(R.id.tvDiscountTypeT);
		tvDiscount = (TextView) findViewById(R.id.tvDiscount);
		rlDiscount  = (RelativeLayout) findViewById(R.id.rlDiscount);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		tvOrderToPay = (Button) findViewById(R.id.tvOrderToPay);

		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("结算");
		tvNavRight.setText("选择优惠");
	}

	@Override
	public void initData() {
		int totalCoount =0;
		Intent intent = getIntent();
		shopingCartList = (ArrayList<TableGoodsDetailBean>) intent.getSerializableExtra(ConstantUtils.INTENT_KEY_SHOPING_CART_LIST);
		if(shopingCartList!=null&&shopingCartList.size()>0){
			for(TableGoodsDetailBean bean :shopingCartList){
				tempPayTotal +=bean.getGoodsPrice() * bean.getAddGoodsCount();
				totalCoount += bean.getAddGoodsCount();
			}
			adapter.setGoodsListData(shopingCartList);
			adapter.notifyDataSetChanged();
		}
		setDiscountGone();
		tvCount.setText(String.valueOf(totalCoount));
		tvSum.setText(String.format("￥%.2f",tempPayTotal));
//		setDiscountShow(true,1,8.0f);

	}

	@Override
	public void initListener() {
		btnDelete.setOnClickListener(this);
		tvOrderToPay.setOnClickListener(this);
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				StatementsActivity.this.finish();
			}
		});
		tvNavRight.setOnClickListener(this);
		
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * 设置优惠信息显示
	 * @param isShow 是否显示
	 * @param type type=1 整单折扣，type =2整单优惠
	 * @param number 折扣或优惠数字
	 */
	private void setDiscountShow(boolean isShow,int type,double number){
		rlDiscount.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountTypeT.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountType.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountTM.setVisibility(isShow?View.VISIBLE:View.GONE);
		tvDiscountM.setVisibility(isShow?View.VISIBLE:View.GONE);
		if(!isShow){
			tvOrderTotalMoney.setText(String.format("￥%.2f",tempPayTotal));
			toPayMoney =tempPayTotal;
		}else {
			switch (type){
				case 1:
					tvDiscount.setText(String.format("%.1f折",number));
					tvDiscountType.setText(String.format("整单%.1f折",number));
					double tDiscount = tempPayTotal*((number/10));
					String tDiscountPrice = String.format("%.2f",tDiscount);
					tvDiscountM.setText(String.format("￥%.2f",(tempPayTotal - Double.parseDouble(tDiscountPrice))));
					tvOrderTotalMoney.setText(String.format("￥%s",tDiscountPrice));
					toPayMoney = Double.parseDouble(tDiscountPrice);
					break;
				case 2:
				case 3:
					tvDiscountM.setText(String.format("￥%.2f",number));
					tvDiscount.setText(String.format("减%.2f元",number));
					tvDiscountType.setText(String.format("整单减%.2f元",number));
					double d1 = tempPayTotal -number;
					tvOrderTotalMoney.setText(String.valueOf(d1));
					toPayMoney = d1;
					break;
			}


		}

	}
	public void setDiscountGone(){
		setDiscountShow(false,0,0);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btnDelete:
				setDiscountGone();
				break;
			case R.id.tvOrderToPay:
				if(toPayMoney>0){
					if(SystemUtils.checkAllNet(this)){
						if(Utils.checkUserIsLogin(StatementsActivity.this)){
							createOrder4Net(BaseApplication.getUserName(),BaseApplication.getWyyCode(),toPayMoney);
						}else {
							Intent intent = new Intent(StatementsActivity.this,LoginActivity.class);
							StatementsActivity.this.startActivity(intent);
						}

					}else {
						Toast.makeText(StatementsActivity.this,getString(R.string.text_net_error),Toast.LENGTH_SHORT).show();
					}
				}else {
					Toast.makeText(this,"待支付金额不能为0",Toast.LENGTH_SHORT).show();
				}
				break;
			case R.id.tvNavRight://选择优惠
				StatementDiscountPopWindow popWindow = new StatementDiscountPopWindow(this);
				ArrayList<StatementsDiscountBean> arrayListZhenZ = new ArrayList<>();
				ArrayList<StatementsDiscountBean> arrayListZhenJ = new ArrayList<>();
				boolean isDiscount = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_DISCOUNT_SWITCH,false);
				if(isDiscount){
					String orderBy = TableDiscountNumBean.COLUMN_CREATE_CATEGORY_TIEM+" DESC";
					ArrayList<TableDiscountNumBean> tableList = new TableDiscountNumBean().query(null, TableCategoryBean.COLUMN_USER_ID +"=?",new String[]{Utils.get6MD5WithString("18501053570")},null,null,orderBy);
					if(tableList!=null&&tableList.size()>0){
						for (TableDiscountNumBean bean:tableList){
							if(!"+".equals(bean.getShowText())){
								StatementsDiscountBean discountBean = new StatementsDiscountBean();
								discountBean.setType(bean.getType());
								discountBean.setNumber(bean.getDiscountNum());
								if(bean.getType()==1){
									arrayListZhenZ.add(discountBean);
								}else if(bean.getType()==2){
									arrayListZhenJ.add(discountBean);
								}
							}
						}
					}
				}
				popWindow.setDiscountListData(arrayListZhenZ,arrayListZhenJ);
				popWindow.showPopupWindow(tvNavTitle);
				popWindow.setDiscuntPopWListener(this);
				tvNavRight.setText("");
				tvNavRight.setBackgroundResource(R.drawable.ic_nav_back);
				break;
		}
	}

	private void createOrder4Net(final String userName, final String wyyCode, final double toPayMoney) {
		new XTAsyncTask() {
			@Override
			protected void onPreExectue() {
				if(mCustomDialog==null)
					mCustomDialog = CustomDialog.createLoadingDialog(StatementsActivity.this,
							"正在生成订单...", true);
			}

			@Override
			protected void doInbackgroud() {
				RequestEngine.getInstance().createOrder(userName, wyyCode, toPayMoney, new NetReqCallBack() {
					@Override
					public void getTimeOutMsg(int exceptionCode, String strMsg, String strUrl) {
						super.getTimeOutMsg(exceptionCode, strMsg, strUrl);
						Utils.sendAsyncToast(StatementsActivity.this,"网络异常,创建订单失败");
					}

					@Override
					public void getExceptionMsg(int exceptionCode, String strMsg, String strUrl) {
						super.getExceptionMsg(exceptionCode, strMsg, strUrl);
						Utils.sendAsyncToast(StatementsActivity.this,"创建订单失败");
					}

					@Override
					public void getErrData(int statusCode, String strJson, String strUrl) {
						super.getErrData(statusCode, strJson, strUrl);
						Utils.sendAsyncToast(StatementsActivity.this,"创建订单失败");
					}

					@Override
					public void getSuccData(int statusCode, final String strJson, String strUrl) {
						StatementsActivity.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								try {
									JSONObject mJSONObject = new JSONObject(strJson);
									int code = mJSONObject.optInt("Code");
									String message = mJSONObject.optString("Message");
									if(code==1){
										//创建订单成功
										JSONObject orderObj = 	mJSONObject.optJSONObject("Obj");
										if(orderObj!=null){
											String orderNo = orderObj.optString("orderno");
											StatementsActivity.orderNo = orderNo;
											StatementsActivity.this.runOnUiThread(new Runnable() {
												@Override
												public void run() {
													Statements2PayPopWindow payPopWindow = new Statements2PayPopWindow(StatementsActivity.this,toPayMoney);
													payPopWindow.showPopupWindow(tvOrderToPay);
													payPopWindow.set2PayWindowListener(StatementsActivity.this);
												}
											});
										}
									}else {
										if(!TextUtils.isEmpty(message))
										Utils.sendAsyncToast(StatementsActivity.this,message);
									}
								} catch (JSONException e) {
									Utils.sendAsyncToast(StatementsActivity.this,"创建订单失败");
								}
							}
						});

					}
				});
			}

			@Override
			protected void onPostExecute() {
				if(null!=StatementsActivity.this&& mCustomDialog.isShowing())
					mCustomDialog.dismiss();
			}
		}.execute();
	}

	@Override
	public void onItemSelected(int type, double number) {
		if(type==1){
			setDiscountShow(true,type,number);
			return;
		}
		if(tempPayTotal >number && type==2||type==3){
			setDiscountShow(true,type,number);
		}else {
			Toast.makeText(this,"整单减少的金额已大于商品总价！",Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onDiscountPopWindowDismiss() {
		tvNavRight.setText("选择优惠");
		tvNavRight.setBackground(null);
	}

	@Override
	public void on2PayMoney(int type, double payMoney) {
		switch (type){
			case 1: //微信支付
				toWeixinOrAlipay(ConstantUtils.PAY_TYPE_WEXIN,payMoney);
				break;
			case 2://支付宝支付
				toWeixinOrAlipay(ConstantUtils.PAY_TYPE_ALIPAY,payMoney);
				break;
			case 3://现金支付
				if(payMoney<=0){
					Toast.makeText(this,"付款金额要大于0!",Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(this,CashPayActivity.class);
				intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,payMoney);
				this.startActivity(intent);

				break;
		}
	}
	private void toWeixinOrAlipay(int payType,double money) {
		if(money<=0){
			Toast.makeText(this,"付款金额要大于0!",Toast.LENGTH_SHORT).show();
			return;
		}
		Intent intent = new Intent(this,ScanPayActivity.class);
		intent.putExtra(ConstantUtils.INTENT_KEY_PAY_TYPE,payType);
		intent.putExtra(ConstantUtils.INTENT_KEY_SUM_OF_MONEY,money);
		intent.putExtra(ConstantUtils.INTENT_KEY_ORDER_NO,StatementsActivity.orderNo);
		startActivity(intent);
	}
}