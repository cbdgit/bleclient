package com.lowworker.android;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.baidu.mapapi.SDKInitializer;

import org.altbeacon.beacon.startup.RegionBootstrap;

import BLEScut.DaoMaster;
import BLEScut.DaoSession;

/**
 * Created by saulmm on 15/02/15.
 */
public class BleScut extends Application  {
    private static int sMemoryClass;
    private static Application mContext;
    public DaoSession daoSession;
    private RegionBootstrap regionBootstrap;
    @Override
    public void onCreate() {

        super.onCreate();
        mContext = this;
        initAppConfig();
        setupDatabase();

        setupMapSdk();

    }


    private void initAppConfig() {
        final ActivityManager mgr = (ActivityManager) getApplicationContext().
                getSystemService(Activity.ACTIVITY_SERVICE);
        sMemoryClass = mgr.getMemoryClass();
    }
    private  void setupMapSdk(){
        SDKInitializer.initialize(mContext);
    }
    public static Application getInstance(){
        return mContext;
    }
    public static int getMemorySize(){
        return sMemoryClass;
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "BLEScut", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }




}
