package com.aorise.fdas.customviewlearn.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Tuliyuan.
 * Date: 2019/3/26.
 */
public class BasicView extends View {
    public BasicView(Context context) {
        super(context);
    }

    public BasicView(Context context, @NonNull AttributeSet attrs) {
        super(context, attrs);
    }

    public BasicView(Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BasicView(Context context, @NonNull AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Paint paint =new Paint();
//        paint.setColor(Color.RED);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(5f);
//        canvas.drawLine(190,200,290,430,paint);
        //画直线路径
//        Paint linePath = new Paint();
//        linePath.setColor(Color.RED);
//        linePath.setStrokeWidth(10f);
//        linePath.setStyle(Paint.Style.STROKE);
//        Path lineP = new Path();
//        lineP.moveTo(10,10);
//        lineP.lineTo(10,100);
//        lineP.lineTo(300,100);
//        lineP.close();
//        canvas.drawPath(lineP,linePath);
         //画弧线路径
//        Paint arcPath = new Paint();
//        arcPath.setColor(Color.RED);
//        arcPath.setStrokeWidth(10f);
//        arcPath.setStyle(Paint.Style.STROKE);
//        Path arcP = new Path();
//        arcP.moveTo(10,10);
//        RectF mRectF = new RectF(100,10,400,200);
//        canvas.drawRect(mRectF,arcPath);
//        arcP.arcTo(mRectF,0,90);
//        canvas.drawPath(arcP,arcPath);
        //截取画布区域比如椭圆和矩形重叠部分
        Paint mRegionPaint = new Paint();
        mRegionPaint.setColor(Color.RED);
        mRegionPaint.setStyle(Paint.Style.FILL);
        Path ovalPath = new Path();
        RectF rectF = new RectF(50,50,200,500);
        ovalPath.addOval(rectF,Path.Direction.CCW);
        Region rgn = new Region();
        rgn.setPath(ovalPath,new Region(50,50,200,200));

        drawRegion(canvas,rgn,mRegionPaint);

    }
    private void drawRegion(Canvas canvas,Region rgn ,Paint paint){
        RegionIterator iterator = new RegionIterator(rgn);
        Rect r = new Rect();
        while (iterator.next(r)){
            canvas.drawRect(r,paint);
        }
    }
}
