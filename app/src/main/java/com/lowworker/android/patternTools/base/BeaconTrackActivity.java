package com.lowworker.android.patternTools.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.lowworker.android.patternTools.model.ParcelableIBeaconList;
import com.lowworker.android.patternTools.model.ParcelableRegion;
import com.lowworker.android.patternTools.service.BeaconTrackService;
import com.lowworker.android.utils.NotificationTools;


/**
 * Created by surecase on 04/06/14.
 */
public class BeaconTrackActivity extends ActionBarActivity {

    private BroadcastReceiver trackServiceCallbackReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver();
    }

    private void registerReceiver() {
        Log.d("BeaconService","registerReceiver");
        trackServiceCallbackReceiver = createCallbackBeaconReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(BeaconTrackService.CALLBACK_BEACON_SERVICE_CONNECTED);
        filter.addAction(BeaconTrackService.CALLBACK_MONITORING_DETERMINE_STATE);
        filter.addAction(BeaconTrackService.CALLBACK_MONITORING_ENTER);
        filter.addAction(BeaconTrackService.CALLBACK_MONITORING_EXIT);
        filter.addAction(BeaconTrackService.CALLBACK_MONITORING_RESCHEDULE_ERROR);
        filter.addAction(BeaconTrackService.CALLBACK_MONITORING_FROM_DATABASE_RESCHEDULE_ERROR);
        filter.addAction(BeaconTrackService.CALLBACK_MONITORING_STOP_ERROR);
        filter.addAction(BeaconTrackService.CALLBACK_RANGING_DID_RANGE);
        filter.addAction(BeaconTrackService.CALLBACK_RANGING_RESCHEDULE_ERROR);
        filter.addAction(BeaconTrackService.CALLBACK_RANGING_STOP_ERROR);
        filter.addAction(NotificationTools.ACTION_RETRY);
        registerReceiver(trackServiceCallbackReceiver, filter);
    }

    private BroadcastReceiver createCallbackBeaconReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == null) {
                    return;

                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_BEACON_SERVICE_CONNECTED)) {
                    onBeaconServiceConnected();
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_MONITORING_DETERMINE_STATE)) {
                    ParcelableRegion region;
                    if (intent.getExtras() != null) {
                        region = (ParcelableRegion) intent.getExtras().get("region");
                        onBeaconDetermineState(region);
                    } else {
                        onBeaconDetermineState(null);
                    }
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_MONITORING_ENTER)) {
                    ParcelableRegion region;
                    if (intent.getExtras() != null) {
                        region = (ParcelableRegion) intent.getExtras().get("region");
                        onBeaconMonitoringEnter(region);
                    } else {
                        onBeaconMonitoringEnter(null);
                    }
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_MONITORING_EXIT)) {
                    ParcelableRegion region;
                    if (intent.getExtras() != null) {
                        region = (ParcelableRegion) intent.getExtras().get("region");
                        onBeaconMonitoringExit(region);
                    } else {
                        onBeaconMonitoringExit(null);
                    }
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_MONITORING_RESCHEDULE_ERROR)) {
                    onBeaconMotoringRescheduleError();
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_MONITORING_FROM_DATABASE_RESCHEDULE_ERROR)) {
                    onBeaconMotoringFromDatabaseRescheduleError();
                }
                else if (intent.getAction().equals(BeaconTrackService.CALLBACK_MONITORING_STOP_ERROR)) {
                    onBeaconMonitoringStopError();
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_RANGING_DID_RANGE)) {

                    ParcelableRegion region = null;
                    ParcelableIBeaconList parcelableIBeaconList = null;
                    if (intent.getExtras() != null && intent.getExtras().get("region") != null) {
                        region = (ParcelableRegion) intent.getExtras().get("region");
                    }
                    if (intent.getExtras() != null && intent.getExtras().get("beaconList") != null) {
                        parcelableIBeaconList = (ParcelableIBeaconList) intent.getExtras().get("beaconList");
                    }
                    onBeaconRanging(parcelableIBeaconList, region);
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_RANGING_RESCHEDULE_ERROR)) {
                    onBeaconRangingRescheduleError();
                } else if (intent.getAction().equals(BeaconTrackService.CALLBACK_RANGING_STOP_ERROR)) {
                    onBeaconRangingStopError();
                }else if (intent.getAction().equals(NotificationTools.ACTION_RETRY)) {
                    onActionRetryReceived();
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        unregisterTrackServiceCallbackReceiver();
        super.onDestroy();
    }

    private void unregisterTrackServiceCallbackReceiver() {
        unregisterReceiver(trackServiceCallbackReceiver);
    }

    public void onBeaconServiceConnected() {


    }

    public void onBeaconDetermineState(ParcelableRegion region) {
        Log.d("BeaconService", "onBeaconDetermineState");

    }

    public void onBeaconMonitoringEnter(ParcelableRegion region) {

    }

    public void onBeaconMonitoringExit(ParcelableRegion region) {

    }

    public void onBeaconMotoringRescheduleError() { }
    public void  onBeaconMotoringFromDatabaseRescheduleError() { }
    public void onBeaconMonitoringStopError() { }

    public void onBeaconRanging(ParcelableIBeaconList iBeaconList, ParcelableRegion region) {   Log.d("beaconList", iBeaconList.toString());}

    public void onBeaconRangingRescheduleError() { }

    public void onBeaconRangingStopError() { }

    public void onActionRetryReceived() {

    }


    public Context getContext() {
        return getApplicationContext();
    }
}
