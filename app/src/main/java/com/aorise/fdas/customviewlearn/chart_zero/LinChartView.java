package com.aorise.fdas.customviewlearn.chart_zero;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.aorise.fdas.customviewlearn.R;

import static android.text.Layout.Alignment.ALIGN_CENTER;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/26.
 */
public class LinChartView extends View {
    private static final String TAG = "LinChartView";
    private LineChartData mData;
    private int mTextW, mChartH, mMaxV;

    private Paint arcPaint = null;

    public LinChartView(Context context) {
        super(context);
    }

    public LinChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null) {
            Log.d(TAG, "数据为空!");
            return;
        }
        // 画文字
        drawText(canvas, mData.getName());

        //画图形
        drawLine(canvas);

        drawBoardLine(canvas);
    }

    /**
     * 绘制图形
     *
     * @param canvas
     */
    private void drawLine(Canvas canvas) {
        double chart_length = (getWidth() - mTextW) / (double) mMaxV;
        int start_complete_left = mTextW + 10,
                start_complete_top = 4,
                start_complete_right = start_complete_left + (int) (chart_length * mData.getRecover_complete()),
                start_uncomplete_right = start_complete_left + (int) (chart_length * (mData.getRecover_complete() + mData.getRecover_uncomplete())),
                start_complete_bottom = mChartH;

        Log.d(TAG, start_complete_left + "..." + start_complete_top + ",,," + start_complete_right + "ds"
                + start_uncomplete_right);
        if (mChartH > 200) {
            start_complete_top = getHeight() / 5;
            start_complete_bottom = start_complete_top * 4;
        }
        this.arcPaint = new Paint();
        this.arcPaint.setColor(Color.GRAY);
        this.arcPaint.setAntiAlias(true);// 去除锯齿
        // 绘制未完成的，

        canvas.drawRect(start_complete_left, start_complete_top, start_uncomplete_right, start_complete_bottom, arcPaint);

        // 绘制完成的
        this.arcPaint.setColor(getResources().getColor(R.color.line_chart_complete));
        canvas.drawRect(start_complete_left, start_complete_top, start_complete_right, start_complete_bottom, arcPaint);
    }

    /**
     * 绘制文字说明  右对齐
     *
     * @param canvas
     * @param text
     */
    private void drawText(Canvas canvas, String text) {
        canvas.save();
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(dip2px(getContext(), 16));
        // 设置文字右对齐
        textPaint.setTextAlign(Paint.Align.LEFT);
        StaticLayout staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, mTextW)
                .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                .setEllipsizedWidth(mTextW)
                .setAlignment(ALIGN_CENTER)
                .setIncludePad(true)
                .build();

        staticLayout.draw(canvas);
        Log.d(TAG, " height is " + staticLayout.getHeight());
        canvas.restore();
    }

    private void drawBoardLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawLine(0, 5, mTextW, 5, paint);
        canvas.drawLine(0, getHeight(), mTextW, getHeight(), paint);


        int start_complete_top = 4;
        int start_complete_bottom = mChartH;
        if (mChartH > 200) {
            start_complete_top = getHeight() / 5;
            start_complete_bottom = start_complete_top * 4;
        }
        canvas.drawLine(mTextW, 5, mTextW, mChartH, paint);
        canvas.drawLine(mTextW, getHeight() / 2 + 5, mTextW + 10, getHeight() / 2 , paint);
    }

    /**
     * @return 返回指定笔和指定字符串的长度
     */
    public static float getFontlength(Paint paint, String str) {
        return paint.measureText(str);
    }

    /**
     * @return 返回指定笔的文字高度
     */
    public static float getFontHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.descent - fm.ascent;
    }

    /**
     * @return 返回指定笔离文字顶部的基准距离
     */
    public static float getFontLeading(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return fm.leading - fm.ascent;
    }

    public void setData(int textW, int chartW, int max_valur, LineChartData data) {
        Log.d(TAG, "  textW " + textW + " chartW " + chartW + " max_value" + max_valur);
        this.mTextW = textW;
        this.mChartH = chartW;
        this.mMaxV = max_valur;
        this.mData = data;
        this.postInvalidate();
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Toast.makeText(getContext(), mData.getName(), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    @Override
    public void setOnClickListener(View.OnClickListener l) {
        super.setOnClickListener(l);
    }
}
