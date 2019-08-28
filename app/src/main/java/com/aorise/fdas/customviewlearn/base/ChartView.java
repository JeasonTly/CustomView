package com.aorise.fdas.customviewlearn.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.CollapsibleActionView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aorise.fdas.customviewlearn.BR;
import com.aorise.fdas.customviewlearn.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Tuliyuan.
 * 柱状统计图
 * Date: 2019/8/27.
 */
public class ChartView extends View {
    private static final String TAG = "ChartView";
    private Paint mAixsPaint;
    private TextPaint todayPaint;
    private Paint mBarPaint;
    private Paint mBarPercentPaint;
    private TextPaint mTodayPaint;
//    private TextPaint mYTextPaint;
//    private TextPaint mYBarDescriptionPaint;//Bar上的字符


    private int width;
    private int height;
    private int margin_left = 10;
    private int margin_top = 10;
    private int margin_bottom = 10;
    private int padding_xz_to_xtext = 10;
    private int Y_xWidth = 20; //Y轴上多出来的指向部分宽度
    private int mBarItemWidth = 180;// 为柱的宽度，也为X轴text的宽度
    private int mBarItemSpace = 100;// 为柱之间的间距
    private int mXTextPaintWidth = 120;//Y轴坐标文字描述宽度 日期描述


    private int startX; // 指的是X轴的起始位置，应该为mXTextPaintWidth+ Y_xWidth + 左边距
    private int startY; // 指的是Y轴的其实位置，应该为总高度- mYBarDescriptionPaint所占高度 - margin_top - margin_bottom - mXTextPaint的高度-padding_xz_to_xtext


    private List<Bean> mBeanList = new ArrayList<>();
    private String startDate = "2019-07-25";
    private String endDate = "2019-10-25";
    private int dengfen_YAix;//每天占的像素值

    private List<Rect> rectList = new ArrayList<>();
    private int selectedPostion = -1;
    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint(context);
        initData();
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint(context);
        initData();
    }

    private void initPaint(Context context) {
        mAixsPaint = new Paint();
        mAixsPaint.setColor(ContextCompat.getColor(context, R.color.color_274782));
        mAixsPaint.setAntiAlias(true);
        mAixsPaint.setStrokeWidth(2);


        todayPaint = new TextPaint();
        todayPaint.setColor(ContextCompat.getColor(context, R.color.line_chart_complete));
        todayPaint.setAntiAlias(true);
        todayPaint.setStrokeWidth(2);

        todayPaint = new TextPaint();
        todayPaint.setColor(ContextCompat.getColor(context, R.color.line_chart_complete));
        todayPaint.setTextSize(DensityUtil.dip2px(getContext(), 12));

        BlurMaskFilter PaintBGBlur = new BlurMaskFilter(
                1, BlurMaskFilter.Blur.INNER);
        mBarPaint = new Paint();
        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setStrokeWidth(4);
        mBarPaint.setMaskFilter(PaintBGBlur);


        mBarPercentPaint = new Paint();
        mBarPercentPaint.setStyle(Paint.Style.FILL);
        mBarPercentPaint.setStrokeWidth(4);
        mBarPercentPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_79d4d8));
        mBarPercentPaint.setMaskFilter(PaintBGBlur);

    }


    private void initData() {
        mBeanList = new ArrayList<>();
        mBeanList.add(new Bean(70.1f, "2019-08-10", "2019-10-27", "编码阶段"));
        mBeanList.add(new Bean(22.7f, "2019-08-11", "2019-08-22", "产品设计阶段"));
        mBeanList.add(new Bean(72.1f, "2019-08-11", "2019-08-25", "产品测试阶段"));
        mBeanList.add(new Bean(34.1f, "2019-08-12", "2019-09-22", "产品验收阶段"));
        mBeanList.add(new Bean(21.8f, "2019-09-13", "2019-10-20", "产品调研阶段"));
        mBeanList.add(new Bean(16.9f, "2019-09-13", "2019-10-20", "产品CC阶段"));
        mBeanList.add(new Bean(93.9f, "2019-09-13", "2019-10-20", "产品BBBB研阶段"));
        mBeanList.add(new Bean(26.9f, "2019-09-13", "2019-10-20", "产品ADDA研阶段"));
        mBeanList.add(new Bean(79.9f, "2019-09-13", "2019-10-20", "产品ADDA研阶段"));
        mBeanList.add(new Bean(18.9f, "2019-08-07", "2019-10-20", "产品ADDA研阶段"));
        mBeanList.add(new Bean(18.9f, "2019-08-16", "2019-10-21", "产品A222DDA2研阶段"));
        mBeanList.add(new Bean(18.9f, "2019-08-09", "2019-10-21", "产品A222DDA2研阶段"));
        mBeanList.add(new Bean(18.9f, "2019-08-29", "2019-10-21", "产品A222DDA2研阶段"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制X轴Y轴
        drawAxis(canvas);
        //绘制X轴上的日期
        drawYAxisIndicateAndDate(canvas);
        //绘制X轴上的项目计划
        drawXAxisText(canvas);
        //绘制柱状图以及柱状图上的起止日期
        drawBar(canvas);
        //绘制今天
        drawToday(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = measureHeight(heightMeasureSpec);
        width = mXTextPaintWidth + 2 * margin_left + mBeanList.size() * mBarItemWidth + (mBeanList.size() + 1) * mBarItemSpace;
        Log.e(TAG, " afterMeasue height is " + height + " width is " + width);
        measureParams(width, height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    /**
     * 绘制横轴和纵轴
     */
    private void drawAxis(Canvas canvas) {
        canvas.save();
        //绘制横轴
        canvas.drawLine(startX, startY, getWidth(), startY, mAixsPaint);
        //绘制纵轴
        canvas.drawLine(startX, startY, startX, margin_top + getTextWH()[1], mAixsPaint);
        canvas.restore();
    }

    /**
     * 绘制Y轴上的日期参数
     *
     * @param canvas
     */
    private void drawYAxisIndicateAndDate(Canvas canvas) {
        canvas.save();
        int totalMonth = DateUtil.getDiffMonth(startDate, endDate);
        int diffDay = DateUtil.getDiffDay(startDate, endDate);
        for (int i = 0; i < 4; i++) {

            Log.d(TAG, " Y轴等分每天所占的天数像素值为 " + dengfen_YAix);
//            canvas.drawText(DateUtil.getDayAfterToday(startDate, diffDay / 4 * i), margin_left, YTextPadding, paint_font2);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.GRAY);
            textPaint.setTextSize(DensityUtil.dip2px(getContext(), 16));
            // 设置文字右对齐
            textPaint.setTextAlign(Paint.Align.LEFT);
            String text = DateUtil.getDayAfterToday(startDate, (diffDay / 3) * i, endDate);
            StaticLayout staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, mXTextPaintWidth)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(mXTextPaintWidth)
                    .setIncludePad(true)
                    .build();
            canvas.save();
            canvas.translate(margin_left, startY - (diffDay / 3) * i * dengfen_YAix - staticLayout.getHeight() / 2);
            staticLayout.draw(canvas);
            canvas.restore();

            canvas.drawLine(startX - Y_xWidth, startY - (diffDay / 3) * i * dengfen_YAix, getWidth(), startY - (diffDay / 3) * i * dengfen_YAix, mAixsPaint);
        }

    }

    /**
     * 绘制X轴上的计划内容
     *
     * @param canvas
     */
    private void drawXAxisText(Canvas canvas) {

        for (int i = 0; i < mBeanList.size(); i++) {
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.GRAY);
            textPaint.setTextSize(DensityUtil.dip2px(getContext(), 16));
            // 设置文字右对齐
            textPaint.setTextAlign(Paint.Align.LEFT);

            StaticLayout staticLayout = StaticLayout.Builder.obtain(mBeanList.get(i).getPlanName(), 0, mBeanList.get(i).getPlanName().length(), textPaint, mBarItemWidth)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(mBarItemWidth)
                    .setIncludePad(true)
                    .build();
            canvas.save();
            canvas.translate(startX + mBarItemSpace * (i + 1) + mBarItemWidth * i, startY);
            staticLayout.draw(canvas);
            canvas.restore();

        }
    }

    /**
     * 绘制条形图
     *
     * @param canvas
     */
    private void drawBar(Canvas canvas) {
        for (int i = 0; i < mBeanList.size(); i++) {
            //绘制条形图
            Rect rect = new Rect();
            int left = startX + mBarItemSpace * (i + 1) + mBarItemWidth * i;
            int top = startY - DateUtil.getDiffDay(mBeanList.get(i).getEndTime(), startDate) * dengfen_YAix;
            int right = left + mBarItemWidth;
            int bottom = startY - DateUtil.getDiffDay(mBeanList.get(i).getStartTime(), startDate) * dengfen_YAix;
            rect.set(left, top, right, bottom);
            Log.e(TAG, "drawBar left " + left + " top " + top + " right " + right + " bottom " + bottom);
            mBarPaint.setColor(Color.GRAY);
            canvas.drawRect(rect, mBarPaint);
            rectList.add(rect);
            //绘制条形图上的日期描述 起始日期
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.GRAY);
            textPaint.setTextSize(DensityUtil.dip2px(getContext(), 12));
            StaticLayout staticLayout = StaticLayout.Builder.obtain(mBeanList.get(i).getStartTime(), 0, mBeanList.get(i).getStartTime().length(), textPaint, mBarItemWidth)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(mBarItemWidth)
                    .setIncludePad(true)
                    .build();
            canvas.save();
            canvas.translate(left, bottom);
            staticLayout.draw(canvas);
            canvas.restore();

            StaticLayout staticLayoutEnd = StaticLayout.Builder.obtain(mBeanList.get(i).getEndTime(), 0, mBeanList.get(i).getEndTime().length(), textPaint, mBarItemWidth)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(mBarItemWidth)
                    .setIncludePad(true)
                    .build();
            canvas.save();
            canvas.translate(left, top - staticLayoutEnd.getHeight());
            staticLayoutEnd.draw(canvas);
            canvas.restore();

            //绘制百分比
            Rect rectPercent = new Rect();
            float percentTop = top + (bottom - top) * (1 - mBeanList.get(i).getPercent() / 100);
            Log.d(TAG, "drawBar percentTop is " + percentTop + " mBeanList.get(i).getPercent() " + ((1 - mBeanList.get(i).getPercent() / 100)));
            rectPercent.set(left, (int) percentTop, right, bottom);
            canvas.drawRect(rectPercent, mBarPercentPaint);
        }
    }

    /**
     * 绘制今天
     * @param canvas
     */
    private void drawToday(Canvas canvas){
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = simpleDateFormat.format(date);
        int y = DateUtil.getDiffDay(today,startDate) * dengfen_YAix;
        canvas.drawLine(startX , startY - y , getWidth(), startY - y , todayPaint);

        StaticLayout staticLayout = StaticLayout.Builder.obtain(today, 0, today.length(), todayPaint, mBarItemSpace)
                .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                .setEllipsizedWidth(mBarItemSpace)
                .setIncludePad(true)
                .build();
        canvas.save();
        canvas.translate(startX, startY - y);
        staticLayout.draw(canvas);
        canvas.restore();
    }
    private void measureParams(int width, int height) {
        startX = mXTextPaintWidth + 2 * margin_left;
        startY = height - getTextWH()[1] - margin_bottom - caculatMaxItemHeight() - padding_xz_to_xtext;
        Log.d(TAG, "measureParams  startX is " + startX + " startY " + startY);
        int diffMonth = DateUtil.getDiffMonth(startDate, endDate);
        int diffDay = DateUtil.getDiffDay(startDate, endDate);
        int totalHeight = startY - margin_top;
        dengfen_YAix = totalHeight / diffDay;
        Log.d(TAG, "measureParams  diff month is " + diffMonth + " diffDay is " + diffDay);
    }


    private int caculatMaxItemHeight() {
        String text = "";
        int max_txt_lenght = 0;
        for (int i = 0; i < mBeanList.size(); i++) {
            if (max_txt_lenght <= mBeanList.get(i).getPlanName().length()) {
                max_txt_lenght = mBeanList.get(i).getPlanName().length();
                text = mBeanList.get(i).getPlanName();
            }
        }
        int height = 0;
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(DensityUtil.dip2px(getContext(), 16));
        // 设置文字右对齐
        textPaint.setTextAlign(Paint.Align.LEFT);
        StaticLayout staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, mBarItemWidth)
                .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                .setEllipsizedWidth(mBarItemWidth)
                .setIncludePad(true)
                .build();
        Log.d(TAG, "caculatMaxItemHeight  height is " + staticLayout.getHeight());
        height = staticLayout.getHeight();

        return height;
    }

    private int[] getTextWH() {
        int[] wh = new int[2];
        // 一个矩形
        Rect rect = new Rect();
        String text = "我";
        Paint paint = new Paint();
        // 设置文字大小
        paint.setTextSize(DensityUtil.dip2px(getContext(), 16));
        paint.getTextBounds(text, 0, text.length(), rect);
        //wh[0] = rect.width();
        wh[0] = rect.width();
        wh[1] = rect.height();
        Log.d(TAG, "getTextWH " + rect.width());
        return wh;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e(TAG, "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = getPaddingTop() + getPaddingBottom() + specSize;
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
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "touch event x" + x + " y is " + y);
                if(inRectArea(x, y)){

                    Toast toast = new Toast(getContext());
                    LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    ViewDataBinding viewDataBinding = DataBindingUtil.inflate(inflater,R.layout.toast_view,null,false);
                    viewDataBinding.setVariable(BR.planName,mBeanList.get(selectedPostion).getPlanName());
                    viewDataBinding.setVariable(BR.startTime, mBeanList.get(selectedPostion).getStartTime());
                    viewDataBinding.setVariable(BR.endTime, mBeanList.get(selectedPostion).getEndTime());
                    viewDataBinding.setVariable(BR.percentComplete, mBeanList.get(selectedPostion).getPercent() + "%");
//                    viewDataBinding.setVariable(BR.owner,mBuffer.toString());
                    toast.setView(viewDataBinding.getRoot());

                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                };
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "touch event ACTION_MOVE x" + x + " y is " + y);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    private synchronized boolean inRectArea(float x, float y) {
        for (int i = 0; i < rectList.size(); i++) {
            Rect rect = rectList.get(i);
            if (x <= rect.right && x >= rect.left) {
                if (y <= rect.bottom && y >= rect.top) {
                    Log.d(TAG, " inRectArea 在矩形内 " + i);
                    selectedPostion = i;
                    return true;
                }
            }
        }
        return false;
    }
}
