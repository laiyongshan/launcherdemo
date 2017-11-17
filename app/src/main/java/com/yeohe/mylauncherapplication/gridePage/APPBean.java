package com.yeohe.mylauncherapplication.gridePage;

import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/11/17.
 */

public class APPBean {

    String name;
    Drawable drawable ;
    String packageName;

    public APPBean(String name,Drawable drawable,String packageName){
        this.name=name;
        this.drawable=drawable;
        this.packageName=packageName;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }
}
