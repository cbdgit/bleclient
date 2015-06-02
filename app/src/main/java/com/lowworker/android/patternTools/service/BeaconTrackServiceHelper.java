package com.lowworker.android.patternTools.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * Created by surecase on 04/06/14.
 */
public class BeaconTrackServiceHelper {


    static public void rescheduleMonitoringFromDatabase(Context context) {
        Log.d("TrackServiceHelper","rescheduleMonitoringFromDatabase");
        Intent intent = new Intent(BeaconTrackService.ACTION_RESCHEDULE_MONITORING_FROM_DATABASE);
        context.sendBroadcast(intent);
    }

    static public void stopMonitoring(Context context) {
        Intent intent = new Intent(BeaconTrackService.ACTION_STOP_MONITORING);
        context.sendBroadcast(intent);
    }


    static public void rescheduleRangingFromDatabase(Context context) {
        Log.d("TrackServiceHelper","rescheduleRangingFromDatabase");
        Intent intent = new Intent(BeaconTrackService.ACTION_RESCHEDULE_RANGING_FROM_DATABASE);
        context.sendBroadcast(intent);
    }

    static public void stopRanging(Context context) {
        Intent intent = new Intent(BeaconTrackService.ACTION_STOP_RANGING);
        context.sendBroadcast(intent);
    }


    static public void restartBeaconTrackSerivce(Application application) {
        Log.d("TrackServiceHelper","restartBeaconTrackSerivce");
        Intent intent = new Intent(application, BeaconTrackService.class);
        application.stopService(intent);
        application.startService(intent);
    }

    static public void stopBeaconTrackSerivce(Application application) {
        Intent intent = new Intent(application, BeaconTrackService.class);
        application.stopService(intent);;
    }

    static public void setBackgroundScanPeriod(Context context, long milis) {
        Intent intent = new Intent(BeaconTrackService.ACTION_SET_BACKGROUND_SCAN_PERIOD);
        intent.putExtra("milis", milis);
        context.sendBroadcast(intent);
    }

    static public void setForegroundScanPeriod(Context context, long milis) {
        Intent intent = new Intent(BeaconTrackService.ACTION_SET_FOREGROUND_SCAN_PERIOD);
        intent.putExtra("milis", milis);
        context.sendBroadcast(intent);
    }

}
