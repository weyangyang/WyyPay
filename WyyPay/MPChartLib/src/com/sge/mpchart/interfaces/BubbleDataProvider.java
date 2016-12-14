package com.sge.mpchart.interfaces;

import com.sge.mpchart.data.BubbleData;

public interface BubbleDataProvider extends BarLineScatterCandleDataProvider {

    public BubbleData getBubbleData();
    
}
