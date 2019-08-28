package com.aorise.fdas.customviewlearn;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.EventLogTags;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aorise.fdas.customviewlearn.base.Barrr;
import com.aorise.fdas.customviewlearn.base.EchartOptionUtil;
import com.aorise.fdas.customviewlearn.base.EchartsView;
import com.aorise.fdas.customviewlearn.base.MyCharView;
import com.github.abel533.echarts.json.GsonOption;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.style.ItemStyle;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ScaleGestureDetector.OnScaleGestureListener{
    private EchartsView lineChart;
    private MyCharView targetView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        HorizontalChartView horizontalScrollView = (HorizontalChartView)findViewById(R.id.id_one);
//        List<Barrr> barList = new ArrayList<>();
////        barList.add(new Barrr(18, "听力"));
////        barList.add(new Barrr(65, "语法"));
////        barList.add(new Barrr(37, "词法"));
////        barList.add(new Barrr(60, "阅读理解"));
//        Log.d("MainActivity",DateUtil.getDayAfterToday("2019-08-22",15)) ;
//        barList.add(new Barrr(10, "说"));
//        barList.add(new Barrr(16, "唱"));
//        barList.add(new Barrr(30, "跳"));
//        barList.add(new Barrr(45, "Rap"));
//        horizontalScrollView.setBarList(barList);

//        ChartView chartView = (ChartView)findViewById(R.id.id_two);
//        List<Bean> mBean = new ArrayList<>();
//        mBean.add(new Bean(70.1f,"2018-08-10","2018-08-22","计划AAAAsss2AAAAAA"));
//        mBean.add(new Bean(70.1f,"2018-08-19","2018-08-22","ssadddddddddd"));
//        mBean.add(new Bean(70.1f,"2018-08-10","2018-08-25","计划AAAAAdcwcadcwAAAAA"));
//        mBean.add(new Bean(70.1f,"2018-08-18","2018-09-22","计划AAAAaaaaAAAAAA"));
//        mBean.add(new Bean(70.1f,"2018-09-10","2018-10-22","计划AAAAbbbbbbbbAAAAAA"));
//        chartView.setList(mBean);
//        lineChart = (EchartsView)findViewById(R.id.echart_view);
//        lineChart.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                //最好在h5页面加载完毕后再加载数据，防止html的标签还未加载完成，不能正常显示
////                refreshLineChart();
//
//            }
//        });
//        Object[] x = new Object[]{
//                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
//        };
//        Object[] y = new Object[]{
//                820, 932, 901, 934, 1290, 1330, 1320
//        };

//        targetView = (MyCharView) findViewById(R.id.chart_view_one );
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
               // showPieChart();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        refreshBarChart();
    }

    private void refreshBarChart(){
        String[] planName = new String[]{
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        };
        String[] dateName = new String[]{
                "2019-08-01", "2019-09-01", "2019-10-01", "2019-11-01"
        };
        Log.d("MainActivity", " refreshEchartsWithOption   "+ EchartOptionUtil.getBarChartOptions(planName,dateName).toString());
       // lineChart.refreshEchartsWithOption(EchartOptionUtil.getBarChartOptions(planName,dateName));
    }

    private void refreshLineChart(){
        Object[] x = new Object[]{
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        };
        Object[] y = new Object[]{
                820, 932, 901, 934, 1290, 1330, 1320
        };

        lineChart.refreshEchartsWithOption(EchartOptionUtil.getLineChartOptions(x, y));
    }

    private void showPieChart() {
        lineChart.loadUrl("javascript:clear()");

        ItemStyle dataStyle = new ItemStyle();
        dataStyle.normal().label().show(true).formatter("{b}\n({d}%)");

        GsonOption option = new GsonOption();

        Pie pie = new Pie("访问来源");
        pie.clockWise(false).center("48%", "45%").radius("55", "80")
                .itemStyle(dataStyle)
                .data(
                        new Barrr(355,"直接访问"),
                        new Barrr(310,"邮件营销"),
                        new Barrr(274,"联盟广告"),
                        new Barrr(235,"视频广告"),
                        new Barrr(400,"搜索引擎")
                );

        option.series(pie);

        lineChart.loadUrl("javascript:setOption(" + option + ")");
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private float scale = 1;
    private float scaleTemp = 1;

    private boolean isFullGroup = false;
    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        scale = scaleTemp * detector.getScaleFactor();
        targetView.setScaleX(scale);
        targetView.setScaleY(scale);
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
