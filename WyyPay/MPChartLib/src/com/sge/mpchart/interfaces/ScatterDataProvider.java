package com.sge.mpchart.interfaces;

import com.sge.mpchart.data.ScatterData;

public interface ScatterDataProvider extends BarLineScatterCandleDataProvider {

    public ScatterData getScatterData();
    
}
