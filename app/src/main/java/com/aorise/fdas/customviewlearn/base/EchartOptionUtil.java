package com.aorise.fdas.customviewlearn.base;

import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/22.
 */
public class EchartOptionUtil {
    public static GsonOption getLineChartOptions(Object[] xAxis, Object[] yAxis) {
        GsonOption option = new GsonOption();
        option.title("折线图");
        option.legend("销量");
        option.tooltip().trigger(Trigger.axis);

        ValueAxis valueAxis = new ValueAxis();
        option.yAxis(valueAxis);

        CategoryAxis categorxAxis = new CategoryAxis();
        categorxAxis.axisLine().onZero(false);
        categorxAxis.boundaryGap(true);
        categorxAxis.data(xAxis);
        option.xAxis(categorxAxis);

        Line line = new Line();
        line.smooth(false).name("销量").data(yAxis).itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
        option.series(line);
        return option;
    }
    public static GsonOption getBarChartOptions(String[] xPlanxis, String[] yDatexis){
        GsonOption option = new GsonOption();
        option.title("项目统计");
       // option.legend("销量");
        option.tooltip().trigger(Trigger.axis);
        CategoryAxis categoryPlanNameAxis = new CategoryAxis();
        categoryPlanNameAxis.boundaryGap(true);
        categoryPlanNameAxis.data(xPlanxis);
        option.xAxis(categoryPlanNameAxis);


        CategoryAxis categoryDateAxis = new CategoryAxis();
        categoryDateAxis.boundaryGap(true);
        categoryDateAxis.data(yDatexis);
        option.yAxis(categoryDateAxis);

        Bar bar = new Bar();
        bar.data(yDatexis).itemStyle().normal().lineStyle();
        option.series(bar);
        return option;
    }
}
