package com.yeohe.mylauncherapplication;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static HomeWatcherReceiver mHomeKeyReceiver = null;

    GridView appsGrid;
    TypeAdapter typeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appsGrid = (GridView) findViewById(R.id.apps_list);

        appsGrid.setOnItemClickListener(clickListener);

        registerHomeKeyReceiver(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        loadApps();
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


    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            startAPP(appPackages.get(i).packageName);
        }
    };


    List<Drawable> types = new ArrayList<Drawable>();
    List<String> strArr=new ArrayList<String>();
    List<PackageInfo> packages=new ArrayList<PackageInfo>();
    List<PackageInfo> appPackages=new ArrayList<PackageInfo>();
    //    private List<ResolveInfo> apps;
//    ResolveInfo info;
    private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        types.clear();
        strArr.clear();
        packages.clear();
        appPackages.clear();

//        apps = getPackageManager().queryIntentActivities(mainIntent, 0);

        packages= getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packages.size();i++) {
            PackageInfo packageInfo = packages.get(i);
            if((packageInfo.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==0)
            {
                appPackages.add(packages.get(i));
                //非系统应用
                strArr.add(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
                types.add( packageInfo.applicationInfo.loadIcon(getPackageManager()));
            }
        }

//        for (int i=0;i<apps.size();i++){
//            info= apps.get(i);
//            types.add(info.activityInfo.loadIcon(getPackageManager()));
//        }
        typeAdapter=new TypeAdapter(types,this,strArr,1);
        typeAdapter.notifyDataSetChanged();
        appsGrid.setAdapter(typeAdapter);
        appsGrid.setOnItemClickListener(clickListener);
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
