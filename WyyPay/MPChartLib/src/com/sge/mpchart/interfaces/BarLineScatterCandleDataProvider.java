package com.sge.mpchart.interfaces;

import com.sge.mpchart.components.YAxis.AxisDependency;
import com.sge.mpchart.utils.Transformer;

public interface BarLineScatterCandleDataProvider extends ChartInterface {

    public Transformer getTransformer(AxisDependency axis);
    public int getMaxVisibleCount();
    public boolean isInverted(AxisDependency axis);
    
    public int getLowestVisibleXIndex();
    public int getHighestVisibleXIndex();
}
