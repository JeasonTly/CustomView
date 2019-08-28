package com.aorise.fdas.customviewlearn.chart_zero;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class LineChartData {
    @Override
    public String toString() {
        return "LineChartData{" +
                "recover_complete=" + recover_complete +
                ", name='" + name + '\'' +
                ", recover_uncomplete=" + recover_uncomplete +
                '}';
    }

    private int recover_complete;
    private String name;
    private int recover_uncomplete;

    public int getRecover_complete() {
        return recover_complete;
    }

    public void setRecover_complete(int recover_complete) {
        this.recover_complete = recover_complete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecover_uncomplete() {
        return recover_uncomplete;
    }

    public void setRecover_uncomplete(int recover_uncomplete) {
        this.recover_uncomplete = recover_uncomplete;
    }
}
