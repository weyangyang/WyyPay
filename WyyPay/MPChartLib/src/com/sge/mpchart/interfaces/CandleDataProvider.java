package com.sge.mpchart.interfaces;

import com.sge.mpchart.data.CandleData;

public interface CandleDataProvider extends BarLineScatterCandleDataProvider {

    public CandleData getCandleData();
}
