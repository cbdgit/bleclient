package com.lowworker.android.model.repository;

import android.content.Context;
import android.util.Log;

import com.lowworker.android.BleScut;

import java.util.List;

import BLEScut.Beacon;
import BLEScut.BeaconDao;

/**
 * Created by lowworker on 2015/5/11.
 */
public class BeaconRepository {

    public static BeaconRepository INSTANCE;

    public  BeaconRepository getInstance() {

        if (INSTANCE == null)
            INSTANCE = new BeaconRepository();
        return INSTANCE;
    }
    public static void insertOrUpdate(Context context, Beacon Beacon) {
        getBeaconDao(context).insertOrReplace(Beacon);
    }


    public static void batchBeacons(Context context, List<Beacon> beacons) {
        getBeaconDao(context).insertOrReplaceInTx(beacons);
    }



    public static void clearBeacones(Context context) {
        getBeaconDao(context).deleteAll();
    }


    public static Beacon getBeaconByUuid(Context context, String uuid) {
        Beacon beacon = getBeaconDao(context).queryBuilder()
                .where(BeaconDao.Properties.ProximityUuid.eq(uuid)).list().get(0);
        return beacon;
    }

    public static boolean isBeaconExists(Context context, String uuid) {
      if(getBeaconDao(context).queryBuilder()
                .where(BeaconDao.Properties.ProximityUuid.eq(uuid)).list().size()>0)
      {
          return true;
      }else{
          return false;
      }

    }


    public static List<Beacon> getSubscriptedBeacon(Context context ) {

        List<Beacon> beacons = getBeaconDao(context).queryBuilder()
                .where(BeaconDao.Properties.IsSubscripted.eq(Boolean.TRUE)).list();
        Log.d("getSubscriptedBeacon",beacons.size()+"");
        return beacons;
    }

    public static void setBeaconSubscripted(Context context, String uuid,boolean isSubscripted ) {

        Beacon beacon =  getBeaconByUuid(context, uuid);
        beacon.setIsSubscripted(isSubscripted);
        getBeaconDao(context).update(beacon);

    }

    public static boolean isBeaconSubscriptedByUuid(Context context, String uuid) {
        Beacon beacon =  getBeaconByUuid(context, uuid);
        return beacon.getIsSubscripted();
    }

    public static void deleteBeaconByUuid(Context context, String uuid) {
        getBeaconDao(context).delete(getBeaconByUuid(context, uuid));
    }

    public static List<Beacon> getAllBeacons(Context context) {
        return getBeaconDao(context).loadAll();
    }

    private static BeaconDao getBeaconDao(Context c) {
        return ((BleScut) c.getApplicationContext()).getDaoSession().getBeaconDao();
    }
}
