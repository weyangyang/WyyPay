
package com.sge.mpchart.charts;

import android.content.Context;
import android.util.AttributeSet;

import com.sge.mpchart.data.LineData;
import com.sge.mpchart.interfaces.LineDataProvider;
import com.sge.mpchart.renderer.LineChartRenderer;
import com.sge.mpchart.utils.FillFormatter;

/**
 * Chart that draws lines, surfaces, circles, ...
 * 
 * @author Philipp Jahoda
 */
public class LineChart extends BarLineChartBase<LineData> implements LineDataProvider {

    /** the width of the highlighning line */
    protected float mHighlightWidth = 3f;

    private FillFormatter mFillFormatter;

    public LineChart(Context context) {
        super(context);
    }

    public LineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();

        mRenderer = new LineChartRenderer(this, mAnimator, mViewPortHandler);
        
        mFillFormatter = new DefaultFillFormatter();
    }

    @Override
    protected void calcMinMax() {
        super.calcMinMax();

        // // if there is only one value in the chart
        // if (mOriginalData.getYValCount() == 1
        // || mOriginalData.getYValCount() <= mOriginalData.getDataSetCount()) {
        // mDeltaX = 1;
        // }

        if (mDeltaX == 0 && mData.getYValCount() > 0)
            mDeltaX = 1;
    }
    
    /**
     * set the width of the highlightning lines, default 3f
     * 
     * @param width
     */
    public void setHighlightLineWidth(float width) {
        mHighlightWidth = width;
    }

    /**
     * returns the width of the highlightning line, default 3f
     * 
     * @return
     */
    public float getHighlightLineWidth() {
        return mHighlightWidth;
    }

    @Override
    public void setFillFormatter(FillFormatter formatter) {

        if (formatter == null)
            formatter = new DefaultFillFormatter();
        else
            mFillFormatter = formatter;
    }

    @Override
    public FillFormatter getFillFormatter() {
        return mFillFormatter;
    }
    
    @Override
    public LineData getLineData() {
        return mData;
    }
}
