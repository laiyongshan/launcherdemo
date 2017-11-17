package com.yeohe.mylauncherapplication.gridePage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeohe.mylauncherapplication.HomeWatcherReceiver;
import com.yeohe.mylauncherapplication.R;
import com.yeohe.mylauncherapplication.TestActivity;
import com.yeohe.mylauncherapplication.swipe.NumKeyBortActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/17.
 */

public class HomeActivity extends Activity implements View.OnClickListener{

    private ViewPager viewPager;
    private LinearLayout group;//圆点指示器
    private ImageView[] ivPoints;//小圆点图片的集合
    private int totalPage; //总的页数
    private int mPageSize = 6; //每页显示的最大的数量
    private List<APPBean> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    //private int currentPage;//当前页

    private TextView swipe_card_tv,qr_code_tv,scan_tv;

    private static HomeWatcherReceiver mHomeKeyReceiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        registerHomeKeyReceiver(this);

        //初始化控件
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadApps();
        //添加业务逻辑
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterHomeKeyReceiver(this);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_MENU){
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_HOME){
            return true;
        }

        return false;
    }

    private void initView() {
        // TODO Auto-generated method stub
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        group = (LinearLayout)findViewById(R.id.points);
        listDatas = new ArrayList<APPBean>();
//        for(int i =0 ; i < proName.length; i++){
//            listDatas.add(new APPBean(proName[i], R.mipmap.ic_launcher));
//        }

        swipe_card_tv=(TextView)findViewById(R.id.swipe_card_tv);
        swipe_card_tv.setOnClickListener(this);
        qr_code_tv=(TextView)findViewById(R.id.qr_code_tv);
        qr_code_tv.setOnClickListener(this);
        scan_tv=(TextView) findViewById(R.id.scan_tv);
        scan_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.swipe_card_tv://刷卡收款
                startActivity(new Intent(HomeActivity.this, NumKeyBortActivity.class));
                break;

            case R.id.qr_code_tv://二维码收款
                startActivity(new Intent(HomeActivity.this, TestActivity.class));
                break;

            case R.id.scan_tv://扫一扫收款
                startActivity(new Intent(HomeActivity.this, TestActivity.class));
                break;
        }
    }


    private void initData() {
        // TODO Auto-generated method stub
        //总的页数向上取整
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<View>();
        for(int i = 0; i < totalPage; i++){
            //每个页面都是inflate出一个新实例
            final GridView gridView = (GridView)View.inflate(this, R.layout.item_gridview, null);
            gridView.setAdapter(new MyGridViewAdapter(this, listDatas, i, mPageSize));
            //添加item点击监听
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int position, long arg3) {
                    // TODO Auto-generated method stub
                    Object obj = gridView.getItemAtPosition(position);
                    if(obj != null && obj instanceof APPBean){
//                        System.out.println(obj);
//                        Toast.makeText(HomeActivity.this, ((APPBean)obj).getName(), Toast.LENGTH_SHORT).show();
                        startAPP(((APPBean)obj).packageName);
                    }
                }
            });
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));

        //添加小圆点
        group.removeAllViewsInLayout();
        ivPoints = new ImageView[totalPage];
        for(int i = 0; i < totalPage; i++){
            //循坏加入点点图片组
            ivPoints[i] = new ImageView(this);
            if(i==0){
                ivPoints[i].setImageResource(R.mipmap.white_dot);
            }else {
                ivPoints[i].setImageResource(R.mipmap.dark_dot);
            }
            ivPoints[i].setPadding(8, 8, 8, 8);
            group.addView(ivPoints[i]);
        }
        //设置ViewPager的滑动监听，主要是设置点点的背景颜色的改变
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                //currentPage = position;
                for(int i=0 ; i < totalPage; i++){
                    if(i == position){
                        ivPoints[i].setImageResource(R.mipmap.white_dot);
                    }else {
                        ivPoints[i].setImageResource(R.mipmap.dark_dot);
                    }
                }
            }
        });
    }


    private static void registerHomeKeyReceiver(Context context) {
//        Log.i(LOG_TAG, "registerHomeKeyReceiver");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);

        context.registerReceiver(mHomeKeyReceiver, homeFilter);
    }

    private static void unregisterHomeKeyReceiver(Context context) {
//        Log.i(LOG_TAG, "unregisterHomeKeyReceiver");
        if (null != mHomeKeyReceiver) {
            context.unregisterReceiver(mHomeKeyReceiver);
        }
    }


    List<PackageInfo> packages=new ArrayList<PackageInfo>();
    List<PackageInfo> appPackages=new ArrayList<PackageInfo>();
    //    private List<ResolveInfo> apps;
//    ResolveInfo info;
    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        packages.clear();
        appPackages.clear();
        listDatas.clear();
//        apps = getPackageManager().queryIntentActivities(mainIntent, 0);

        packages= getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);

            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0
                    &&!packageInfo.packageName.toString().equals("com.yeohe.mylauncherapplication"))
            {
                appPackages.add(packages.get(i));

                listDatas.add(new APPBean(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString()
                        ,packageInfo.applicationInfo.loadIcon(getPackageManager()) ,packages.get(i).packageName));

                //非系统应用
//                strArr.add(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
//                types.add( packageInfo.applicationInfo.loadIcon(getPackageManager()));
            }

            if(packageInfo.packageName.toString().trim().equals("com.android.settings")){
                listDatas.add(new APPBean(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString()
                        ,packageInfo.applicationInfo.loadIcon(getPackageManager()) ,packages.get(i).packageName));
            }

        }

//        for (int i=0;i<apps.size();i++){
//            info= apps.get(i);
//            types.add(info.activityInfo.loadIcon(getPackageManager()));
//        }
//        typeAdapter=new TypeAdapter(types,this,strArr,1);
//        typeAdapter.notifyDataSetChanged();
//        appsGrid.setAdapter(typeAdapter);
//        appsGrid.setOnItemClickListener(clickListener);
    }

    /*
* 启动一个app
*/
    public void startAPP(String appPackageName){
        try{
            Intent intent = this.getPackageManager().getLaunchIntentForPackage(appPackageName);
            startActivity(intent);
        }catch(Exception e){
            Toast.makeText(this, "没有安装"+appPackageName, Toast.LENGTH_LONG).show();
        }
    }



}
