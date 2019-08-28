package com.aorise.fdas.customviewlearn.chart_one;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


import com.aorise.fdas.customviewlearn.R;
import com.aorise.fdas.customviewlearn.base.Bean;
import com.aorise.fdas.customviewlearn.base.DensityUtil;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Tuliyuan.
 * Date: 2019/8/27.
 */
public class BarChartView extends View {
    private Context mContext;

    private Paint mPaintBar;
    private Paint mPaintLline;
    private Paint mPaintText;
    //柱状条对应的颜色数组
    private int[] colors;
    private int keduTextSpace = 10;//刻度与文字之间的间距
    private int keduWidth = 40; //坐标轴上横向标识线宽度
    private int keduSpace = 150; //每个Y轴上横轴线的 刻度之间的间距 px
    private int itemSpace = 60;//柱状条之间的间距
    private int itemWidth = 140;//柱状条的宽度
    //刻度递增的值
    private int valueSpace = 40;
    //绘制柱形图的坐标起点
    private int startX;
    private int startY;
    private int mTextSize = 25;
    private int mMaxTextWidth;
    private int mMaxTextHeight;
    private Rect mXMaxTextRect;
    private Rect mYMaxTextRect;
    //是否要展示柱状条对应的值
    private boolean isShowValueText = true;
    //数据值
    private List<Integer> mData = new ArrayList<>();
    private List<Integer> yAxisList = new ArrayList<>();
    private List<String> xAxisList = new ArrayList<>();
    private List<Bean> mListBean = new ArrayList<Bean>();

    private String projectStartDate = "2019-08-01";
    private String projectEndDate = "2019-10-22";
    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public BarChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        colors = new int[]{ContextCompat.getColor(context, R.color.color_07f2ab),
                ContextCompat.getColor(context, R.color.color_79d4d8),
                ContextCompat.getColor(context, R.color.color_4388bc),
                ContextCompat.getColor(context, R.color.color_07f2ab),
                ContextCompat.getColor(context, R.color.color_4388bc)};
        init(context, false);
    }

    private void init(Context context, boolean isUpdate) {
        if (!isUpdate) {
            initData();
        }
        //设置边缘特殊效果
        BlurMaskFilter PaintBGBlur = new BlurMaskFilter(
                1, BlurMaskFilter.Blur.INNER);
        //绘制柱状图的画笔
        mPaintBar = new Paint();
        mPaintBar.setStyle(Paint.Style.FILL);
        mPaintBar.setStrokeWidth(4);
        mPaintBar.setMaskFilter(PaintBGBlur);
        //绘制直线的画笔
        mPaintLline = new Paint();
        mPaintLline.setColor(ContextCompat.getColor(context, R.color.color_274782));
        mPaintLline.setAntiAlias(true);
        mPaintLline.setStrokeWidth(2);

        //绘制文字的画笔
        mPaintText = new Paint();
        mPaintText.setTextSize(mTextSize);
        mPaintText.setColor(ContextCompat.getColor(context, R.color.color_a9c6d6));
        mPaintText.setAntiAlias(true);
        mPaintText.setStrokeWidth(1);

        mYMaxTextRect = new Rect();
        mXMaxTextRect = new Rect();
        mPaintText.getTextBounds(Integer.toString(yAxisList.get(yAxisList.size() - 1)), 0, Integer.toString(yAxisList.get(yAxisList.size() - 1)).length(), mYMaxTextRect);
        mPaintText.getTextBounds(xAxisList.get(xAxisList.size() - 1), 0, xAxisList.get(xAxisList.size() - 1).length(), mXMaxTextRect);
        //绘制的刻度文字的最大值所占的宽高
//        mMaxTextWidth = mYMaxTextRect.width() > mXMaxTextRect.width() ? mYMaxTextRect.width() : mXMaxTextRect.width();
//        mMaxTextHeight = mYMaxTextRect.height() > mXMaxTextRect.height() ? mYMaxTextRect.height() : mXMaxTextRect.height();
        int text_max_length = 0;
        String maxTextString = "";
        for (int i = 0; i < xAxisList.size(); i++) {
            if (text_max_length <= xAxisList.get(i).length()) {
                text_max_length = xAxisList.get(i).length();
                maxTextString = xAxisList.get(i);
            }

        }
        mMaxTextWidth = itemWidth;
        mMaxTextHeight = caculatMaxItemHeight(maxTextString,itemWidth);

        if (yAxisList.size() >= 2) {
            valueSpace = yAxisList.get(1) - yAxisList.get(0);
        }
        //文字+刻度宽度+文字与刻度之间间距
        startX = mMaxTextWidth + keduWidth + keduTextSpace;
        //坐标原点 y轴起点
        startY = keduSpace * (yAxisList.size() - 1) + mMaxTextHeight + (isShowValueText ? keduTextSpace : 0);

    }

    private int caculatMaxItemHeight(String text, int textAreW) {
        int height = 0;
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(mTextSize);
        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_a9c6d6));
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(1);
        StaticLayout staticLayout = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, textAreW)
                .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                .setEllipsizedWidth(textAreW)
                .setIncludePad(true)
                .build();
        Log.e("TAG",  " height is " + staticLayout.getHeight());
        height = staticLayout.getHeight() + DensityUtil.dip2px(getContext(),10);

        return height;
    }

    /**
     * 初始化数据
     */
    private void initData() {
        int[] data = {80, 160, 30, 40, 100,400};
        for (int i = 0; i < data.length; i++) {
            mData.add(data[i]);
            yAxisList.add(0 + i * valueSpace);
        }
        String[] xAxis = {"计划111111月", "计划12222月", "计划3333333月", "计划111444444月", "计划5555555555月5","计划5月6弍但是 是是啊啊啊啊带我去"};
        for (int i = 0; i < mData.size(); i++) {
            xAxisList.add(xAxis[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e("TAG", "onMeasure()");
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.e("TAG", " 000 heightSize= " + heightSize + " widthSize=" + widthSize);
        if (heightMode == MeasureSpec.AT_MOST) {
            if (keduWidth > mMaxTextHeight + keduTextSpace) {
                Log.e("TAG"," height size is111 "+heightSize);
                heightSize = (yAxisList.size() - 1) * keduSpace + keduWidth + mMaxTextHeight;
            } else {
                Log.e("TAG"," height size is222 "+heightSize);
                heightSize = (yAxisList.size() - 1) * keduSpace + (mMaxTextHeight + keduTextSpace) + mMaxTextHeight;
            }
            Log.e("TAG"," height size is333 "+heightSize);
            heightSize = heightSize + keduTextSpace + (isShowValueText ? keduTextSpace : 0);//x轴刻度对应的文字距离底部的padding:keduTextSpace
        }
//        if (widthMode == MeasureSpec.AT_MOST) {
//            Log.e("TAG"," width mode atmost ");
//            widthSize = startX + mData.size() * itemWidth + (mData.size() + 1) * itemSpace;
//        }
        widthSize = startX + mData.size() * itemWidth + (mData.size() + 1) * itemSpace;
        Log.e("TAG", " 111  heightSize= " + heightSize + " widthSize=" + widthSize);
        //保存测量结果
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("TAG", "onDraw()");

        //从下往上绘制Y 轴
        canvas.drawLine(startX, startY + keduWidth, startX, startY - (yAxisList.size() - 1) * keduSpace, mPaintLline);

        for (int i = 0; i < yAxisList.size(); i++) {

            //绘制Y轴的文字
            Rect textRect = new Rect();
            mPaintText.getTextBounds(Integer.toString(yAxisList.get(i)), 0, Integer.toString(yAxisList.get(i)).length(), textRect);
            canvas.drawText(Integer.toString(yAxisList.get(i)), (startX - keduWidth) - textRect.width() - keduTextSpace, startY - (i + 1) * keduSpace + keduSpace, mPaintText);

            //画X轴及上方横向的刻度线
            canvas.drawLine(startX - keduWidth, startY - keduSpace * i, startX + mData.size() * itemWidth + itemSpace * (mData.size() + 1), startY - keduSpace * i, mPaintLline);

        }

        for (int j = 0; j < xAxisList.size(); j++) {

            TextPaint textPaint = new TextPaint();
            textPaint.setTextSize(mTextSize);
            textPaint.setColor(ContextCompat.getColor(getContext(), R.color.color_a9c6d6));
            textPaint.setAntiAlias(true);
            textPaint.setStrokeWidth(1);
            textPaint.setTextAlign(Paint.Align.LEFT);
            StaticLayout staticLayout = StaticLayout.Builder.obtain(xAxisList.get(j), 0, xAxisList.get(j).length(), textPaint, itemWidth)
                    .setEllipsize(TextUtils.TruncateAt.MARQUEE)
                    .setEllipsizedWidth(itemWidth)
                    .setIncludePad(true)
                    .build();
            canvas.save();
            canvas.translate(startX + itemSpace * (j + 1) + itemWidth * j ,startY + keduTextSpace);
            staticLayout.draw(canvas);
            canvas.restore();
            if (isShowValueText) {
                Rect rectText = new Rect();
                mPaintText.getTextBounds(mData.get(j) + "", 0, (mData.get(j) + "").length(), rectText);
                //绘制柱状条上的值
                canvas.drawText(mData.get(j) + "", startX + itemSpace * (j + 1) + itemWidth * j + itemWidth / 2 - rectText.width() / 2, (float) (startY - keduTextSpace - (mData.get(j) * (keduSpace * 1.0 / valueSpace))), mPaintText);
            }
            //绘制柱状条
            mPaintBar.setColor(colors[0]);
            //(mData.get(j) * (keduSpace * 1.0 / valueSpace))：为每个柱状条所占的高度值px
            int initx = startX + itemSpace * (j + 1) + j * itemWidth;
            canvas.drawRect(initx, (float) (startY - (mData.get(j) * (keduSpace * 1.0 / valueSpace))), initx + itemWidth, startY, mPaintBar);

        }
    }

    /**
     * 根据真实的数据刷新界面
     *
     * @param datas
     * @param xList
     * @param yList
     */
    public void updateValueData(List<Integer> datas,  List<String> xList, List<Integer> yList) {
        this.mData = datas;
        this.xAxisList = xList;
        this.yAxisList = yList;
        init(mContext, true);
        invalidate();
    }

    public void updateValueData(List<Bean> beans){
        this.mListBean = beans;
        init(mContext, true);
        invalidate();
    }
}
