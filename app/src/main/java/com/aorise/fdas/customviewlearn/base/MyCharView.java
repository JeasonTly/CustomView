package com.aorise.fdas.customviewlearn.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/22.
 */
public class MyCharView extends View {
    private static final String TAG = "MyChartView";

    private int width;
    private int height;

    private Paint mXPaint;
    private Paint mYPaint;
    private Paint mYSignPaint;
    private TextPaint paint_font2;
    private Paint mRectPaint;
    private Paint mPecentRectPaint;

    private int XPadding = 20;
    private int YPadding = 10;
    private int YTextPadding = 50;
    private List<Bean> mBeanList;
    private List<Float> mTextFlotX;
    private List<Float> mTextFlotY;


    private float everyYLineWidth;
    private float everyDayWidthPx;
    private float planHeight;
    private float mRectHeigh;
    /**
     * 总共有多少天
     */
    private int totalDay;
    private int totalMonth;
    private String mProjectStartTime = "2018-08-01";
    private String mProjectEndTime = "2018-10-25";

    public MyCharView(Context context) {
        super(context);
    }


    public MyCharView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
    }

    public MyCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);

    }

    public MyCharView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = measureWidth(widthMeasureSpec);
        height = measureHeight(heightMeasureSpec);

        Log.d(TAG, "width is " + width + " height is " + height);
        totalDay = DateUtil.getDiffDay(mProjectStartTime, mProjectEndTime);
        totalMonth = DateUtil.getDiffMonth(mProjectStartTime, mProjectEndTime);
        Log.d(TAG, "totalDay is " + totalDay + " totalMonth is " + totalMonth);
        everyYLineWidth = width / (totalMonth + 1);
        mTextFlotX = new ArrayList<>();
        mTextFlotY = new ArrayList<>();
        everyDayWidthPx = (width - getPaddingRight() - getPaddingLeft() - 2 * XPadding) / totalDay;

        planHeight = (height - getPaddingTop() - getPaddingBottom() - 2 * YPadding - YTextPadding - 8) / (mBeanList.size());
        mRectHeigh = planHeight / 2;
        Log.d(TAG, " everyDayWidthPx is " + everyDayWidthPx + " planHeight is " + planHeight);
        // everyDayWidthPx = Math.max(everyDayWidthPx, 10);
        planHeight = Math.max(planHeight, 280);
        for (int i = 0; i < mBeanList.size(); i++) {
            Log.d(TAG, " .....添加文字的 坐标系 Y " + planHeight * i + "  X  " + DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), mProjectStartTime) * everyDayWidthPx);
            mTextFlotY.add(planHeight * i);
            mTextFlotX.add(DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), mProjectStartTime) * everyDayWidthPx + XPadding);
        }
        height = (int) (planHeight * (mTextFlotY.size()));
        setMeasuredDimension(width, height);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw " + totalMonth + " totalDay " + totalDay);
        //canvas.drawLine(XPadding, height - YTextPadding, width - getPaddingRight() - YPadding, height - YTextPadding, mXPaint);
        //绘制X轴
        canvas.drawLine(XPadding, YTextPadding + YPadding, width - getPaddingRight() - XPadding, YTextPadding + YPadding, mXPaint);
        canvas.drawLine(width - getPaddingRight() - XPadding, YTextPadding + YPadding, width - getPaddingRight() - XPadding - 20, YTextPadding / 2, mXPaint);
        //绘制Y轴
        canvas.drawLine(XPadding, YTextPadding + YPadding, XPadding, height, mYPaint);
        for (int i = 0; i < totalMonth + 1; i++) {
            if (i == 0) {
                canvas.drawText(mProjectStartTime, XPadding, YTextPadding, paint_font2);
                continue;
            }
            Log.d(TAG, " 标注时间线的 x轴宽度 " + everyYLineWidth);
            canvas.drawText(DateUtil.getDayAfterToday(mProjectStartTime, totalDay / 4 * i,mProjectEndTime), everyDayWidthPx * i * totalDay / 4 - XPadding * 2, YTextPadding, paint_font2);
            canvas.drawLine(XPadding + everyDayWidthPx * i * totalDay / 4, YTextPadding + YPadding, XPadding + everyDayWidthPx * i * totalDay / 4, height, mYSignPaint);
        }

        for (int i = 0; i < mTextFlotY.size(); i++) {
            Log.d(TAG, "1234 x is " + mTextFlotX.get(i) + " y is " + mTextFlotY.get(i));
            if (i == 0) {

                canvas.drawText(mBeanList.get(i).getPlanName(), 2 * XPadding, mTextFlotY.get(i) + 2 * YTextPadding + YPadding, paint_font2);
                canvas.drawText("起止时间" + mBeanList.get(i).getStartTime() + "----" + mBeanList.get(i).getEndTime(), 2 * XPadding, mTextFlotY.get(i) + 2 * YTextPadding + YPadding + 30, paint_font2);
                canvas.drawText("完成百分比" + mBeanList.get(i).getPercent() + "%", 2 * XPadding, mTextFlotY.get(i) + 2 * YTextPadding + YPadding + 60, paint_font2);
//                StaticLayout staticLayout = StaticLayout.Builder.obtain("产品计划ADBDKLASDJS",0,(mBeanList.get(i).getPlanName() + mBeanList.get(i).getStartTime()+ mBeanList.get(i).getStartTime()).length(),paint_font2,150).setEllipsize(TextUtils.TruncateAt.MARQUEE).build();
//                staticLayout.draw(canvas);
                RectF rectF = new RectF();
                rectF.set(mTextFlotX.get(i),
                        mTextFlotY.get(i) + 2 * YTextPadding + YPadding + 80,
                        mTextFlotX.get(i) + everyDayWidthPx * DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), mBeanList.get(i).getEndTime()),
                        mTextFlotY.get(i) + 2 * YTextPadding + YPadding + 140);
                RectF rectPercent = new RectF();
                rectPercent.set(mTextFlotX.get(i),
                        mTextFlotY.get(i) + 2 * YTextPadding + YPadding + 80,
                        (mTextFlotX.get(i) + everyDayWidthPx * DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), mBeanList.get(i).getEndTime())) * (mBeanList.get(i).getPercent() / 100),
                        mTextFlotY.get(i) + 2 * YTextPadding + YPadding + 140);
                canvas.drawRect(rectF, mRectPaint);
                canvas.drawRect(rectPercent, mPecentRectPaint);
                continue;
            }

            canvas.drawText(mBeanList.get(i).getPlanName(), 2 * XPadding, mTextFlotY.get(i), paint_font2);
            canvas.drawText("起止时间" + mBeanList.get(i).getStartTime() + "----" + mBeanList.get(i).getEndTime(), 2 * XPadding, mTextFlotY.get(i) + 30, paint_font2);
            canvas.drawText("完成百分比" + mBeanList.get(i).getPercent() + "%", 2 * XPadding, mTextFlotY.get(i) + 60, paint_font2);
//            StaticLayout staticLayout = StaticLayout.Builder.obtain(mBeanList.get(i).getPlanName() + mBeanList.get(i).getStartTime()+ mBeanList.get(i).getStartTime(),0,(mBeanList.get(i).getPlanName() + mBeanList.get(i).getStartTime()+ mBeanList.get(i).getStartTime()).length(),paint_font2,150).setEllipsize(TextUtils.TruncateAt.MARQUEE).build();
//                staticLayout.draw(canvas);
            RectF rectF = new RectF();
            rectF.set(mTextFlotX.get(i) + XPadding,
                    mTextFlotY.get(i) + 100,
                    mTextFlotX.get(i) + XPadding + everyDayWidthPx * DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), mBeanList.get(i).getEndTime()),
                    mTextFlotY.get(i) + 160);
            RectF rectPercent = new RectF();
            rectPercent.set(mTextFlotX.get(i) + XPadding,
                    mTextFlotY.get(i) + 100,
                    (mTextFlotX.get(i) + XPadding + everyDayWidthPx * DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), mBeanList.get(i).getEndTime())) * (mBeanList.get(i).getPercent() / 100),
                    mTextFlotY.get(i) + 160);
            canvas.drawRect(rectF, mRectPaint);
            //           canvas.drawRect(rectPercent, mPecentRectPaint);
        }

    }

    private void initPaint(Context context) {
        mXPaint = new Paint();
        mXPaint.setStrokeWidth(4);
        mXPaint.setColor(Color.GRAY);
        mXPaint.setStyle(Paint.Style.FILL);
        mXPaint.setAntiAlias(true);

        mYPaint = new Paint();
        mYPaint.setStrokeWidth(4);
        mYPaint.setColor(Color.GRAY);
        mYPaint.setStyle(Paint.Style.FILL);
        mYPaint.setAntiAlias(true);

        mYSignPaint = new Paint();
        mYSignPaint.setStrokeWidth(1);
        mYSignPaint.setColor(Color.BLUE);
        mYSignPaint.setStyle(Paint.Style.FILL);
        mYSignPaint.setAntiAlias(true);

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

        mBeanList = new ArrayList<>();
        mBeanList.add(new Bean(70.1f, "2018-08-10", "2018-08-22", "编码阶段"));
        mBeanList.add(new Bean(22.7f, "2018-08-11", "2018-08-22", "产品设计阶段"));
        mBeanList.add(new Bean(72.1f, "2018-08-11", "2018-08-25", "产品测试阶段"));
        mBeanList.add(new Bean(34.1f, "2018-08-12", "2018-09-22", "产品验收阶段"));
        mBeanList.add(new Bean(21.8f, "2018-09-13", "2018-10-22", "产品调研阶段"));
        mBeanList.add(new Bean(16.9f, "2018-09-13", "2018-10-22", "产品CC阶段"));
        mBeanList.add(new Bean(93.9f, "2018-09-13", "2018-10-22", "产品BBBB研阶段"));
        mBeanList.add(new Bean(26.9f, "2018-09-13", "2018-10-22", "产品ADDA研阶段"));
        mBeanList.add(new Bean(79.9f, "2018-09-13", "2018-10-22", "产品ADDA研阶段"));
        mBeanList.add(new Bean(18.9f, "2018-08-07", "2018-10-22", "产品ADDA研阶段"));
        mBeanList.add(new Bean(18.9f, "2018-08-16", "2018-10-22", "产品A222DDA2研阶段"));
        mBeanList.add(new Bean(18.9f, "2018-08-09", "2018-10-22", "产品A222DDA2研阶段"));
        mBeanList.add(new Bean(18.9f, "2018-08-29", "2018-10-22", "产品A222DDA2研阶段"));
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


    public void setBeanList(List<Bean> beanList) {
        this.mBeanList = beanList;
        invalidate();
    }


}
