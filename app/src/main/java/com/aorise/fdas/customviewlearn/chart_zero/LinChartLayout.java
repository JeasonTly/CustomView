package com.aorise.fdas.customviewlearn.chart_zero;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class LinChartLayout extends LinearLayout {

    private static final String TAG = "LinChartLayout";
    /**
     * 列表的数据源
     */
    private List<LineChartData> mData;

    /**
     * 屏幕的宽
     */
    private int scrW;

    public LinChartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public LinChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // setView();
    }

    public LinChartLayout(Context context) {
        super(context);
        this.setOrientation(VERTICAL);
        // setView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        scrW = measureWidth(widthMeasureSpec);
        mData = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < 15; i++) {
            LineChartData data = new LineChartData();
            data.setName("产品计划计划计划计划加护996" + i);
            int complete = random.nextInt(100);

            data.setRecover_complete(complete);
            data.setRecover_uncomplete(100 - complete);
            Log.d(TAG," data is "+data.toString());
            mData.add(data);
        }
        Log.d(TAG, " onMeasure " + measureWidth(widthMeasureSpec));
        setView();
    }

    public void setView() {

        if (mData != null && !mData.isEmpty()) {
            int text_max_length = 0;
            int value_max = 0;
            LineChartData maxLengthData = new LineChartData();
            for (LineChartData data : mData) {
                // 获取最长文字的个数
                if (text_max_length <= data.getName().length()) {
                    text_max_length = data.getName().length();
                    maxLengthData = data;
                }
                // 获取数据值的大小
                int total = data.getRecover_complete() + data.getRecover_uncomplete();

                // 获取数据的值
                if (value_max <= total) {
                    value_max = total;
                }
                Log.d(TAG, " maxlength is " + text_max_length);
            }
            int[] wh = getTextWH();
            // 文字区域的宽
            //int textAreW = text_max_length * wh[0] + dip2px(getContext(), 10);
            int textAreW = 600;

            // 图形区域的宽
            int chartAreW = scrW - textAreW - 10;
            Log.d(TAG, " src W is " + scrW);

            LinearLayout.LayoutParams layoutParams = new LayoutParams(scrW - dip2px(getContext(), 10), Math.max(50,caculatMaxItemHeight(maxLengthData, textAreW)));
            // 设置居中
            layoutParams.gravity = Gravity.CENTER;
            // 设置Margin
            layoutParams.topMargin = dip2px(getContext(), 4);
            layoutParams.bottomMargin = dip2px(getContext(), 4);

            // 遍历添加LinChartView
            for (LineChartData data : mData) {
                LinChartView chartView = new LinChartView(getContext());
                chartView.setData(textAreW, chartAreW, value_max, data);
                this.addView(chartView, layoutParams);
            }
            //setMeasuredDimension(getWidth(), (caculatMaxItemHeight(maxLengthData, textAreW) + 5) * mData.size());
        }

    }

    private int caculatMaxItemHeight(LineChartData data, int textAreW) {
        int height = 0;
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(dip2px(getContext(), 16));
        // 设置文字右对齐
        textPaint.setTextAlign(Paint.Align.LEFT);
        StaticLayout staticLayout = StaticLayout.Builder.obtain(data.getName(), 0, data.getName().length(), textPaint, textAreW)
                .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                .setEllipsizedWidth(textAreW)
                .setIncludePad(true)
                .build();
        Log.d(TAG,  " height is " + staticLayout.getHeight());
        height = staticLayout.getHeight();

        return height;
    }

    /**
     * 获取单个字符的高和宽
     */
    private int[] getTextWH() {
        int[] wh = new int[2];
        // 一个矩形
        Rect rect = new Rect();
        String text = "我";
        Paint paint = new Paint();
        // 设置文字大小
        paint.setTextSize(dip2px(getContext(), 16));
        paint.getTextBounds(text, 0, text.length(), rect);
        //wh[0] = rect.width();
        wh[0] = rect.width();
        wh[1] = rect.height();
        Log.d(TAG, "getTextWH " + rect.width());
        return wh;
    }


    public void setData(List<LineChartData> d, int scrw) {
        this.mData = d;
        this.scrW = scrw;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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
        }

        Log.e(TAG, "---result = " + result);
        return result;
    }
}
