package com.sge.mpchart.interfaces;

import com.sge.mpchart.data.BarData;

public interface BarDataProvider extends BarLineScatterCandleDataProvider {

    public BarData getBarData();
    public boolean isDrawBarShadowEnabled();
    public boolean isDrawValueAboveBarEnabled();
    public boolean isDrawHighlightArrowEnabled();
    public boolean isDrawValuesForWholeStackEnabled();
}
