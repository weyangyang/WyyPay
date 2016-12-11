package com.wyy.pay.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;


import com.wyy.pay.R;
import com.wyy.pay.utils.NetUtils;
import com.wyy.pay.utils.RunningWithNet;
import com.wyy.pay.utils.UserUtils;
import com.wyy.pay.view.ClearEditText;


import netutils.http.HttpHeader;

public class FeedbackActivity extends BaseActivity {
	private ClearEditText edtContact;
	private ClearEditText edtContent;
	private Button btnSubmit;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_feedback);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		edtContact = (ClearEditText) findViewById(R.id.activity_feedback_contact);
		edtContent = (ClearEditText) findViewById(R.id.activity_feedback_content);
		btnSubmit = (Button) findViewById(R.id.activity_feedback_submit);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("意见反馈");
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initListener() {
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackActivity.this.finish();
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
	/**
	 * 提交反馈和联系方式.
	 * @param view
	 */
	public void submit(View view) {
		final String content = edtContent.getText().toString().trim();
		final String contact = edtContact.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, R.string.feedback_content_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(contact)) {
			Toast.makeText(this, R.string.feedback_contact_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		NetUtils.isNetForRunning(new RunningWithNet() {
			@Override
			public void netOK() {
				feedBackToNet(content, contact);
			}
		});
	}
	private void feedBackToNet(String content, String contact) {
		HttpHeader header = null;
		header = UserUtils.getDefaultHttpHeader();
//		ExternalFactory.getInstance().createFeedback().feedback(header, this, true, content, contact, new AbsFeedbackData(){
//
//			@Override
//			public void getSuccData(String strUrl) {
//				showMessage(R.string.feedback_submit_suc, true);
//			}
//
//			@Override
//			public void getErrData(int errCode, String errMsg,String strUrl) {
//				showMessage(R.string.feedback_submit_fail, false);
//			}
//
//			@Override
//			public void getParserErrData(int errCode, String errMsg,String strUrl) {
//				showMessage(R.string.feedback_submit_fail, false);
//			}
//			@Override
//			public void getExceptionData(int errCode, String errMsg,String strUrl) {
//				showMessage(R.string.feedback_submit_fail, false);
//			}
//		});
	}
	public void showMessage(final int resId, final boolean finish) {
		runOnUiThread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Toast.makeText(FeedbackActivity.this, resId, Toast.LENGTH_SHORT).show();
				if (finish) {
					FeedbackActivity.this.finish();
				}
			}
			
		});
	}

}