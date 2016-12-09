package com.wyy.pay.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wyy.pay.R;
import com.wyy.pay.utils.RemoveZeroType;

import xtcore.utils.PreferenceUtils;

public class RemoveZeroSettingActivity extends BaseActivity implements View.OnClickListener {
	private CheckBox cbxRemoveZero;
	private RadioGroup rgRemoveZero;
	private RadioButton rbtnRemoveFen,rbtnRemoveJiao,rbtn5toJiao;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_remove_zero_setting);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
		setResult(RESULT_OK);
	}
	@Override
	public void initView() {
		rbtnRemoveFen = (RadioButton) findViewById(R.id.rbtnRemoveFen);
		rbtnRemoveJiao = (RadioButton) findViewById(R.id.rbtnRemoveJiao);
		rbtn5toJiao = (RadioButton) findViewById(R.id.rbtn5toJiao);
		cbxRemoveZero = (CheckBox) findViewById(R.id.cbxRemoveZero);
		rgRemoveZero = (RadioGroup) findViewById(R.id.rgRemoveZero);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("抹零设置");
	}

	@Override
	public void initData() {
		boolean isChecked = PreferenceUtils.getPrefBoolean(this,PreferenceUtils.SP_REMOVE_ZERO_SWITCH,false);
		cbxRemoveZero.setChecked(isChecked);
		updateBtnShowStatus(isChecked);
	}

	@Override
	public void initListener() {
		cbxRemoveZero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				PreferenceUtils.setPrefBoolean(RemoveZeroSettingActivity.this,PreferenceUtils.SP_REMOVE_ZERO_SWITCH,isChecked);
				updateBtnShowStatus(isChecked);
			}
		});
		rgRemoveZero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == rbtnRemoveJiao.getId()){
					PreferenceUtils.setPrefInt(RemoveZeroSettingActivity.this,PreferenceUtils.SP_REMOVE_ZERO_TYPE, RemoveZeroType.REMOVE_JIAO.ordinal());
				}else if(checkedId == rbtnRemoveFen.getId()){
					PreferenceUtils.setPrefInt(RemoveZeroSettingActivity.this,PreferenceUtils.SP_REMOVE_ZERO_TYPE, RemoveZeroType.REMOVE_FEN.ordinal());

				}else if(checkedId == rbtn5toJiao.getId()){
					PreferenceUtils.setPrefInt(RemoveZeroSettingActivity.this,PreferenceUtils.SP_REMOVE_ZERO_TYPE, RemoveZeroType.FILE_TO_JIAO.ordinal());
				}
			}
		});
		tvNavLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				RemoveZeroSettingActivity.this.finish();
			}
		});
		
	}

	private void updateBtnShowStatus(boolean isChecked) {
		if(isChecked){
            rbtn5toJiao.setEnabled(isChecked);
            rbtnRemoveFen.setEnabled(isChecked);
            rbtnRemoveJiao.setEnabled(isChecked);
            int type = PreferenceUtils.getPrefInt(RemoveZeroSettingActivity.this,PreferenceUtils.SP_REMOVE_ZERO_TYPE,-1);
            if(type == RemoveZeroType.FILE_TO_JIAO.ordinal()){
                rbtn5toJiao.setChecked(isChecked);
                //rgRemoveZero.check(rbtn5toJiao.getId());
            }else if(type == RemoveZeroType.REMOVE_FEN.ordinal()){
                rbtnRemoveFen.setChecked(isChecked);
            }else if(type == RemoveZeroType.REMOVE_JIAO.ordinal()){
                rbtnRemoveJiao.setChecked(isChecked);
            }else if(type ==-1){
                rbtnRemoveFen.setChecked(isChecked);
                PreferenceUtils.setPrefInt(RemoveZeroSettingActivity.this,PreferenceUtils.SP_REMOVE_ZERO_TYPE, RemoveZeroType.REMOVE_FEN.ordinal());
            }
        }else {
            rgRemoveZero.clearCheck();
            rbtn5toJiao.setEnabled(isChecked);
            rbtnRemoveFen.setEnabled(isChecked);
            rbtnRemoveJiao.setEnabled(isChecked);
        }
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


	@Override
	public void onClick(View v) {
		switch (v.getId()){

		}
	}
}