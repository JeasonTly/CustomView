package com.aorise.fdas.customviewlearn.base;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Tuliyuan.
 * Date: 2019/8/22.
 */
public class ChartView extends View {
    /**
     * 间隔线画笔
     */
    private Paint paint;

    /**
     * 线的颜色
     */
    private int color_line = Color.rgb(230, 230, 230);

    /**
     * 字的颜色
     */
    private int color_font = Color.rgb(51, 51, 51);

    /**
     * 比例图颜色
     */
    private int color_plan = Color.rgb(22, 85, 164);

    /**
     * 比例图画笔
     */
    private Paint paint_plan;

    /**
     * 比例图高度
     */
    private int plan_height;

    /**
     * 初始化比例
     */
    private Float[] ratio = {0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};

    /**
     * 数据列表
     */
    private List<Bean> mList;

    /**
     * 总共有多少天
     */
    private int totalDate;

    private String mProjectStartTime = "2018-08-01";
    private String mProjectEndTime = "2018-10-25";

    /**
     * 文字画笔1
     */
    private Paint paint_font;

    /**
     * 文字画笔2
     */
    private Paint paint_font2;

    /**
     * 线的条数
     */
    private int line_num = 11;

    /**
     * 比例数
     */
    private String ratio_num = "0";

    /**
     * 月份
     */
    private String month_num = "1月";


    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    public void init(Context context, AttributeSet attrs) {

        paint = new Paint();
        paint.setColor(color_line);

        paint_plan = new Paint();
        paint_plan.setColor(color_plan);

        paint_font = new Paint();
        paint_font.setColor(color_font);
        paint_font.setTextSize(DensityUtil.dip2px(context, 12));
        paint_font.setAntiAlias(true);
        paint_font.setTextAlign(Paint.Align.CENTER);

        paint_font2 = new Paint();
        paint_font2.setColor(color_font);
        paint_font2.setTextSize(DensityUtil.dip2px(context, 12));
        paint_font2.setAntiAlias(true);
        paint_font2.setTextAlign(Paint.Align.RIGHT);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int lift_width = (int) (width * 0.15);
        int line_width = (int) (width * 0.78);
        //获取底部文字信息
        Paint.FontMetrics fm = paint_font.getFontMetrics();

        int line_length = (int) (height - (fm.bottom - fm.top) - 4);

        plan_height = (int) (line_length / 12 * 0.3);
        // 横轴为月份
        for (int i = 0; i < totalDate / 5; i++) {
            canvas.save();
            String date = "";
            if (i == 0) {
                date = mProjectStartTime;
            } else {
                date = DateUtil.getDayAfterToday(mProjectStartTime, (totalDate / 5) * i);
            }
            //底部百分比数字
            canvas.drawText(date, lift_width + i * line_width / 10, height - 10, paint_font);
            //网络线
            canvas.drawLine(lift_width + i * line_width / 10, 0, lift_width + i * line_width / 10, line_length, paint);
            canvas.restore();
        }
        //纵轴表示项目计划
        Paint.FontMetrics fm1 = paint_font2.getFontMetrics();
        for (int n = 0; n < mList.size(); n++) {
            canvas.save();
//            month_num = n + "月";
            String planName = mList.get(n).getPlanName();
            //左侧计划列表
            Log.d("CharView", "planName is " + planName);
            canvas.drawText(planName, lift_width / 4 * 3, ((line_length / mList.size())) * n - line_length / mList.size() * 2 + (fm1.bottom - fm1.top) / 2, paint_font2);

            //比例图
            canvas.drawRect(lift_width, line_length / mList.size()  + (line_length / mList.size() * 2 + plan_height / 2) + fm1.bottom,
                    line_width * (mList.get(n).getPercent() / 100) + lift_width, line_length / mList.size() * (mList.size() + 1 - n) - (line_length / mList.size() * 2 + plan_height / 2) + plan_height + fm1.bottom, paint_plan);

            canvas.restore();
        }
    }

    /**
     * 传入比例信息
     *
     * @param ratio
     */
    public void setRatio(Float[] ratio) {
        this.ratio = ratio;
    }

    public void setList(List<Bean> list) {
        this.mList = list;
        invalidate();
    }

    public void setTotalDate(int totalDate) {
        this.totalDate = totalDate;
    }

    public void setmProjectStartTime(String mProjectStartTime) {
        this.mProjectStartTime = mProjectStartTime;
    }

    public void setmProjectEndTime(String mProjectEndTime) {
        this.mProjectEndTime = mProjectEndTime;
    }
}
