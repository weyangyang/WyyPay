package com.wyy.pay.view;

import android.content.Context;
import android.widget.TextView;

import com.sge.mpchart.components.MarkerView;
import com.sge.mpchart.data.CandleEntry;
import com.sge.mpchart.data.Entry;
import com.sge.mpchart.utils.Utils;
import com.wyy.pay.R;

/**
 */
public class MyMarkerView extends MarkerView  {

	private TextView tvContent;
	private Context mContext;
	private String str = "";

	public MyMarkerView(final Context context, int layoutResource) {
		super(context, layoutResource);
		this.mContext = context;
		tvContent = (TextView) findViewById(R.id.tvMarkerView);
	}

	// callbacks everytime the MarkerView is redrawn, can be used to update the
	// content (user-interface)
	@Override
	public void refreshContent(final Entry e, int dataSetIndex) {
		if (e instanceof CandleEntry) {
			CandleEntry ce = (CandleEntry) e;
			tvContent.setText("" + Utils.formatNumber(ce.getHigh(), 0, true));
		} else {
			tvContent.setText("" + Utils.formatNumber(e.getVal(), 0, true));
		}
		
	}
	@Override
	public int getXOffset() {
		// this will center the marker-view horizontally
		return -(getWidth() / 2);
	}

	@Override
	public int getYOffset() {
		// this will cause the marker-view to be above the selected value
		return -getHeight();
	}
}
