package com.wyy.pay.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.sge.mpchart.charts.LineChart;
import com.sge.mpchart.components.Legend;
import com.sge.mpchart.components.YAxis;
import com.sge.mpchart.data.Entry;
import com.sge.mpchart.data.LineData;
import com.sge.mpchart.data.LineDataSet;
import com.sge.mpchart.listener.OnChartValueSelectedListener;
import com.sge.mpchart.utils.Highlight;
import com.wyy.pay.R;
import com.wyy.pay.view.MyMarkerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import xtcore.utils.PreferenceUtils;

public class MyIncomeActivity extends BaseActivity implements OnChartValueSelectedListener {
	LineChart lineChart7day;//近7天趋势
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_income);
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initListener();
	}
	@Override
	public void initView() {
		lineChart7day = (LineChart) findViewById(R.id.lineChart7day);
		tvNavLeft.setBackgroundResource(R.drawable.ic_nav_back);
		tvNavTitle.setText("我的收入");
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
				MyIncomeActivity.this.finish();
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
	private void initLineChartData() {
		// add data
		setLineChatData();

		// // restrain the maximum scale-out factor
		// mChart.setScaleMinima(3f, 3f);
		//
		// // center the view to a specific position inside the chart
		// mChart.centerViewPort(10, 50);

		// get the legend (only possible after setting data)
		Legend l = lineChart7day.getLegend();

		// modify the legend ...
		l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);// 设置-心率值的位置
		l.setForm(Legend.LegendForm.LINE);
		lineChart7day.animateXY(1000, 1000);
		// dont forget to refresh the drawing
		lineChart7day.invalidate();
	}

	private void initLineChart() {
		lineChart7day.setOnChartValueSelectedListener(this);
		lineChart7day.setDrawGridBackground(false);

		// no description text
		lineChart7day.setDescription("");

		// enable value highlighting
		lineChart7day.setHighlightEnabled(true);

		// enable touch gestures
		lineChart7day.setTouchEnabled(true);

		// enable scaling and dragging
		// lcHeartHistoryList.setDragEnabled(true);
		lineChart7day.setScaleEnabled(true);

		// if disabled, scaling can be done on x- and y-axis separately
		lineChart7day.setPinchZoom(true);
		// set an alternative background color


		lineChart7day.setBackgroundColor(Color.rgb(56,58,64));
//		 lcHeartHistoryList.setBackgroundColor(Color.rgb(37, 38, 43));
		// create a custom MarkerView (extend MarkerView) and specify the layout
		// to use for it
		MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
		// set the marker to the chart
		lineChart7day.setMarkerView(mv);
		// lcHeartHistoryList.enableScroll();
		// enable/disable highlight indicators (the lines that indicate the
		// highlighted Entry)
		lineChart7day.setHighlightIndicatorEnabled(true);
		YAxis leftAxis = lineChart7day.getAxisLeft();
		leftAxis.setInverted(false);// 数值从大到小显示
		YAxis rightAxis = lineChart7day.getAxisRight();
		rightAxis.setEnabled(true);// 设置右边也显示数值


		/**
		 *
		 * // get the legend (only possible after setting data) Legend l =
		 * chart.getLegend(); // 设置标示，就是那个一组y的value的
		 *
		 * // modify the legend ... //
		 * l.setPosition(LegendPosition.LEFT_OF_CHART);
		 * l.setForm(LegendForm.CIRCLE);// 样式 l.setFormSize(6f);// 字体
		 * l.setTextColor(Color.WHITE);// 颜色 l.setTypeface(mTf);// 字体
		 *
		 * YLabels y = chart.getYLabels(); // y轴的标示 y.setTextColor(Color.WHITE);
		 * y.setTypeface(mTf); y.setLabelCount(4); // y轴上的标签的显示的个数
		 *
		 * XLabels x = chart.getXLabels(); // x轴显示的标签
		 * x.setTextColor(Color.WHITE); x.setTypeface(mTf);
		 * set1.setLineWidth(1.75f); // 线宽 set1.setCircleSize(3f);// 显示的圆形大小
		 * set1.setColor(Color.WHITE);// 显示颜色
		 * set1.setCircleColor(Color.WHITE);// 圆形的颜色
		 * set1.setHighLightColor(Color.WHITE); // 高亮的线的颜色
		 */
	}

	private void setLineChatData() {
		ArrayList<String> xVals = new ArrayList<String>();
		ArrayList<Entry> yVals = new ArrayList<Entry>();
//		if (dataList != null && dataList.size() > 0) {
//			for (int i = 0; i < dataList.size(); i++) {
//				TableHeartDataBean heartDataBean = dataList.get(i);
//				yVals.add(new Entry(heartDataBean.getIntHeartNum(), i));
//				long heartAfterTime = heartDataBean.getLongHeartAfterTime();
//				String strDate = refFormatNowDate(heartAfterTime);
//				// String strDate = new
//				// CBTimeUtil(heartAfterTime).getFormatTime(CBTimeUtil.MAPKEY_y_m_d_H_M);
//				xVals.add(strDate);
//			}
//		}
//		int intAverage = PreferenceUtils.getPrefInt("average", 0);
//		String strAverage = "";
//		if (intAverage != 0) {
//			strAverage = "\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + "平均值: " + intAverage;
//		}
		// create a dataset and give it a type
		LineDataSet set1 = new LineDataSet(yVals,String.valueOf(50));

		set1.setLineWidth(1.5f);
		set1.setCircleSize(3f);// 设置数据线圈大小
		set1.setColor(Color.rgb(29, 132, 227));// 显示颜色
//		set1.setColor(Color.rgb(220, 31, 83));// 显示颜色
		set1.setCircleColor(Color.GRAY);// 圆形的颜色
		set1.setValueTextColor(Color.WHITE);
		set1.setValueTextSize(8.0f);



//		 XLabels x = chart.getXLabels(); // x轴显示的标签
		// x.setTextColor(Color.WHITE);
		// x.setTypeface(mTf);
		// set1.setLineWidth(1.75f); // 线宽
		// set1.setCircleSize(3f);// 显示的圆形大小
		// set1.setColor(Color.WHITE);// 显示颜色
		// set1.setCircleColor(Color.WHITE);// 圆形的颜色
		// set1.setHighLightColor(Color.WHITE); // 高亮的线的颜色

		// create a data object with the datasets
		LineData data = new LineData(xVals, set1);
		// set data
		lineChart7day.setData(data);
	}

	public String refFormatNowDate(long millis) {
		Date nowTime = new Date(millis);
		SimpleDateFormat sdFormatter = new SimpleDateFormat("yy/MM/dd (HH:mm)");
		String retStrFormatNowDate = sdFormatter.format(nowTime);
		return retStrFormatNowDate;
	}

	@Override
	public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

	}

	@Override
	public void onNothingSelected() {

	}
}