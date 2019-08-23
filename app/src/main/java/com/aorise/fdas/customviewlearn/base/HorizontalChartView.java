package com.aorise.fdas.customviewlearn.base;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/21.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import java.util.List;

/**
 * TODO 因为行高和间距都是固定的，所以当统计条数改变时，请在布局文件中设置好高度
 * 水平进度条
 * Created by cuihuihui on 2017/5/5.
 */

public class HorizontalChartView extends View {
    /**
     * 最大值
     */
    private float maxValue;
    /**
     * 统计项目
     */
    private List<Barrr> barList;

    /**
     * 线的宽度
     */
    private int lineStrokeWidth;
    /**
     * 统计条宽度
     */
    private int barWidth;

    /**
     * 两条统计图之间空间
     */
    private int barSpace;

    /**
     * 各画笔
     */
    private Paint barPaint, linePaint, textPaint, scoreTextPaint;

    /**
     * 矩形区域
     */
    private Rect barRect, topRect;

    private Path textPath;

    private int itemNameWidth;

    private int scoreTextHeight;

    /**
     * 项目名和条形图之间的距离
     */
    private int betweenMargin;


    public HorizontalChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化设置
     */
    private void init(Context context) {
        barPaint = new Paint();
        barPaint.setColor(Color.BLUE);

        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        lineStrokeWidth = DensityUtil.dip2px(context, 0.5f);
        linePaint.setStrokeWidth(lineStrokeWidth);

        textPaint = new Paint();
        textPaint.setColor(getResources().getColor(android.R.color.black));
        textPaint.setTextSize(DensityUtil.dip2px(context, 13));
        textPaint.setAntiAlias(true);

        scoreTextPaint = new Paint();
        scoreTextPaint.setTextSize(DensityUtil.dip2px(context, 13));
        scoreTextPaint.setColor(Color.WHITE);

        barRect = new Rect(0, 0, 0, 0);
        textPath = new Path();

        barWidth = DensityUtil.dip2px(context, 18);
        barSpace = DensityUtil.dip2px(context, 40);
        scoreTextHeight = DensityUtil.dip2px(context, 13);
        itemNameWidth = DensityUtil.dip2px(context, 86);
        betweenMargin = scoreTextHeight / 2;

    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int width = measureWidth(widthMeasureSpec);
//        int height = measureHeight(heightMeasureSpec);
//        //设置宽高
//        setMeasuredDimension(width, height);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float lineViewWidth = (float) ((this.getWidth() - itemNameWidth) * 0.8);//线的宽度占总宽度的0.8，剩余的部分显示分数
        float scoreWidth = lineViewWidth / 5;
        int scoreAdd = (int) (maxValue / 5);
        if (isInEditMode()) {
            return;
        }
        for (int i = 0; i < barList.size(); i++) {
            barRect.left = itemNameWidth;
            barRect.top = barSpace * (i + 2) + barWidth * i;
            barRect.right = (int) (lineViewWidth * (barList.get(i).getScore() / maxValue)) + itemNameWidth;
            barRect.bottom = barRect.top + barWidth;
            if ((barList.get(i).getScore() / maxValue) >= 0.6) {
                barPaint.setColor(Color.BLUE);
            } else {
                barPaint.setColor(Color.RED);
            }

            canvas.drawRect(barRect, barPaint);
            canvas.drawText(barList.get(i).getScore() + "分", barRect.right, barRect.bottom - (barWidth - scoreTextHeight), textPaint);
            canvas.drawText(barList.get(i).getItemName(), itemNameWidth - betweenMargin - textPaint.measureText(barList.get(i).getItemName()), barRect.bottom - (barWidth - scoreTextHeight), textPaint);
            canvas.drawText(String.valueOf(scoreAdd * (i + 1)), itemNameWidth + scoreWidth * (i + 1) - textPaint.measureText(String.valueOf(scoreAdd * (i + 1))) / 2, barSpace, textPaint);
        }
//        canvas.drawText(String.valueOf(maxValue), itemNameWidth + lineViewWidth - textPaint.measureText(String.valueOf(maxValue)) / 2, barSpace, textPaint);
        canvas.drawText("0(分)", itemNameWidth - betweenMargin - textPaint.measureText("0(分)"), barSpace, textPaint);
        canvas.drawLine(itemNameWidth, 0, itemNameWidth, this.getHeight(), linePaint);
    }


    /**
     * 设置统计项目列表
     *
     * @param barList
     */
    public void setBarList(List<Barrr> barList) {
        this.barList = barList;
        if (barList == null) {
            throw new RuntimeException("BarChartView.setItems(): the param items cannot be null.");
        }
        if (barList.size() == 0) {
            return;
        }
        maxValue = barList.get(0).getScore();
        for (Barrr bar : barList) {
            if (bar.getScore() > maxValue) {
                maxValue = bar.getScore();
            }
        }
        invalidate();

    }

//    public static class Barrr {
//        int score;
//        String itemName;
//
//        public Barrr(int score, String itemName) {
//            this.score = score;
//            this.itemName = itemName;
//        }
//
//        public int getScore() {
//            return score;
//        }
//
//        public void setScore(int score) {
//            this.score = score;
//        }
//
//        public String getItemName() {
//            return itemName;
//        }
//
//        public void setItemName(String itemName) {
//            this.itemName = itemName;
//        }
//    }

    //根据xml的设定获取宽度
    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {

        }
        Log.i("这个控件的宽度----------", "specMode=" + specMode + " specSize=" + specSize);

        return specSize;
    }

    //根据xml的设定获取高度
    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        //wrap_content
        if (specMode == MeasureSpec.AT_MOST) {

        }
        //fill_parent或者精确值
        else if (specMode == MeasureSpec.EXACTLY) {

        }
        Log.i("这个控件的高度----------", "specMode:" + specMode + " specSize:" + specSize);

        return specSize;
    }

}