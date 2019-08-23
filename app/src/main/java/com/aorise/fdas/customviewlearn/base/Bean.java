package com.aorise.fdas.customviewlearn.base;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/22.
 */
public class Bean {
    private float percent;
    private String startTime;
    private String endTime;
    private String planName;

    public Bean(float percent, String startTime, String endTime, String planName) {
        this.percent = percent;
        this.startTime = startTime;
        this.endTime = endTime;
        this.planName = planName;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }
}
