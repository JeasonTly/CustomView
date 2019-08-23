package com.aorise.fdas.customviewlearn.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/21.
 */
public class ArrowRulerView extends ViewGroup {
    private int mWidth;
    private int mHeight;

    //基线宽高
    private float mBaseLineWidth;
    private float mBaseLineHeight;

    //大刻度宽高
    private float mScalWidth;//这个是单个大刻度占view的像素值
    private float mScalLineWidth = 2;
    private float mScalLineHeight = 20;

    //小刻度宽高
    private float mScalLitleWidth;//这个是单个小刻度占view的像素值
    private float mScalLitleLineWidth = 2;
    private float mScalLitleLineHeight = 10;

    private Context mContext;
    private float DEFAULT_PADDING_LEFT = 10;
    private float DEFAULT_PADDING_RIGHT = 10;
    private float DEFAULT_PADDING_TOP = 10;
    private float DEFAULT_PADDING_BOTTOM = 10;

    private int mScale = 3; //等分值
    private int mLitleScale = 5;//小等分值
    private static final int DIRCTION_HORIZ = 1;//方向水平
    private static final int DIRCTION_VEC = 2;//方向垂直
    private int DEFAULT_DIRECTION = DIRCTION_HORIZ;


    private Paint mBaseLinePaint; //基线画笔
    private Paint mScalePaint; //刻度画笔
    private Paint mLitleScalePaint; //小刻度画笔

    public ArrowRulerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public ArrowRulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initPaint();
    }

    public ArrowRulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPaint();
    }

    public ArrowRulerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (DEFAULT_DIRECTION == DIRCTION_HORIZ) {
            float start_x = getPaddingLeft() + DEFAULT_PADDING_LEFT;
            float stop_x = mWidth - getPaddingRight() - DEFAULT_PADDING_RIGHT;
            canvas.drawLine(start_x, mHeight,
                    stop_x, mHeight, mBaseLinePaint);
            for (int i = 0; i < mScale; i++) {
                float top_x = getPaddingLeft() + DEFAULT_PADDING_LEFT + mScalWidth * i;
                canvas.drawRect(top_x - mScalLineWidth, mHeight - mScalLineHeight,
                        top_x + mScalLineWidth, mHeight, mScalePaint);
                for (int j = 1; j < mLitleScale; j++) {
                    float top_litle_x = top_x + mScalLitleWidth * j;
                    canvas.drawRect(top_litle_x - mScalLitleLineWidth, mHeight - mScalLitleLineHeight,
                            top_litle_x + mScalLitleLineWidth, mHeight, mLitleScalePaint);
                }
            }
            canvas.drawLine(stop_x, mHeight, stop_x - 30, mHeight - 30, mBaseLinePaint);
            //   canvas.drawLine(stop_x, mHeight, stop_x - 15, mHeight + 15, mBaseLinePaint);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = measureWidth(widthMeasureSpec);
        mHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private void initPaint() {
        mBaseLinePaint = new Paint();
        mBaseLinePaint.setStrokeWidth(5);
        mBaseLinePaint.setColor(Color.RED);
        mBaseLinePaint.setStyle(Paint.Style.FILL);
        mBaseLinePaint.setAntiAlias(true);

        mScalePaint = new Paint();
        mScalePaint.setStrokeWidth(8);
        mScalePaint.setColor(Color.BLUE);
        mScalePaint.setStyle(Paint.Style.FILL);
        mScalePaint.setAntiAlias(true);

        mLitleScalePaint = new Paint();
        mLitleScalePaint.setStrokeWidth(5);
        mLitleScalePaint.setColor(Color.BLACK);
        mLitleScalePaint.setStyle(Paint.Style.FILL);
        mLitleScalePaint.setAntiAlias(true);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e("ArrowViewWidth", "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = getPaddingLeft() + getPaddingRight();
                Log.e("ArrowViewWidth", "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                Log.e("ArrowViewWidth", "---speMode = EXACTLY");
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e("ArrowViewWidth", "---speMode = UNSPECIFIED");
                result = Math.max(result, specSize);
        }
        if (DEFAULT_DIRECTION == DIRCTION_HORIZ) {
            mBaseLineWidth = result - (getPaddingRight() + getPaddingLeft() + DEFAULT_PADDING_RIGHT + DEFAULT_PADDING_LEFT);
            mScalWidth = mBaseLineWidth / mScale;
            mScalLitleWidth = mScalWidth / mLitleScale;
        }
        Log.e("ArrowViewWidth", "---result = " + result);
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.e("ArrowViewHeight", "---speSize = " + specSize + "");

        switch (specMode) {
            case MeasureSpec.AT_MOST:
                result = getPaddingTop() + getPaddingBottom();
                Log.e("ArrowViewHeight", "---speMode = AT_MOST");
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                Log.e("ArrowViewHeight", "---speSize = EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                result = Math.max(result, specSize);
                Log.e("ArrowViewHeight", "---speSize = UNSPECIFIED");
                break;
        }
//        if(DEFAULT_DIRECTION == DIRCTION_VEC){
//            result = result - dip2px(DEFAULT_PADDING_TOP) - dip2px(DEFAULT_PADDING_BOTTOM);
//        }
        Log.e("ArrowViewHeight", "---result = " + result);
        return result;
    }

    public int getmScale() {
        return mScale;
    }

    public void setmScale(int mScale) {
        this.mScale = mScale;
    }

    public int getmLitleScale() {
        return mLitleScale;
    }

    public void setmLitleScale(int mLitleScale) {
        this.mLitleScale = mLitleScale;
    }

    public int dip2px(float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
