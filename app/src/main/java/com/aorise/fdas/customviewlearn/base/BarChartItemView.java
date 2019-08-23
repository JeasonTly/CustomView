package com.aorise.fdas.customviewlearn.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/23.
 */
public class BarChartItemView extends View {
    private static final String TAG = "BarChartItemView";
    private int width;
    private int height;
    private Paint mRectPaint;
    private Paint mPecentRectPaint;
    private TextPaint paint_font2;

    private int DEFAULT_PADDING = 20;
    private Bean mBean;
    private List<Float> mTextFlotX;
    private List<Float> mTextFlotY;

    public BarChartItemView(Context context) {
        super(context);
    }

    public BarChartItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public BarChartItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
    }

    public BarChartItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initPaint(Context context) {

        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(8);
        mRectPaint.setColor(Color.GRAY);
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setAntiAlias(true);

        mPecentRectPaint = new Paint();
        mPecentRectPaint.setStrokeWidth(8);
        mPecentRectPaint.setColor(Color.rgb(61, 208, 120));
        mPecentRectPaint.setStyle(Paint.Style.FILL);
        mPecentRectPaint.setAntiAlias(true);

        paint_font2 = new TextPaint();
        paint_font2.setColor(Color.BLUE);
        paint_font2.setTextSize(DensityUtil.dip2px(context, 12));
        paint_font2.setAntiAlias(true);
        paint_font2.setTextAlign(Paint.Align.LEFT);

        mBean = new Bean(70.1f, "2018-08-10", "2018-08-22", "编码阶段");

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e(TAG, "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = getPaddingLeft() + getPaddingRight();
                Log.e(TAG, "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e(TAG, "---speMode = EXACTLY");
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e(TAG, "---speMode = UNSPECIFIED");
                result = Math.max(result, specSize);
                break;
        }

        Log.e(TAG, "---result = " + result);
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e(TAG, "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = getPaddingTop() + getPaddingBottom();
                Log.e(TAG, "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                Log.e(TAG, "---speSize = EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                result = Math.max(result, specSize);
                Log.e(TAG, "---speSize = UNSPECIFIED");
                break;
        }
        Log.e(TAG, "---result = " + result);
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initTextDescription(canvas);
    }

    private void initTextDescription(Canvas canvas) {
        canvas.drawText("计划名称:" + mBean.getPlanName(), getPaddingLeft() + DEFAULT_PADDING, height + getPaddingTop() + DEFAULT_PADDING, paint_font2);
        canvas.drawText("计划起止时间:" + mBean.getStartTime() + "----" + mBean.getEndTime(), getPaddingLeft() + DEFAULT_PADDING, height + getPaddingTop() + DEFAULT_PADDING, paint_font2);
        canvas.drawText("完成百分比:" + mBean.getPercent() + "%", getPaddingLeft() + DEFAULT_PADDING, height + getPaddingTop() + DEFAULT_PADDING, paint_font2);

    }
}
