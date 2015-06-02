package com.lowworker.android.patternTools.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.lowworker.android.model.repository.BeaconRepository;
import com.lowworker.android.patternTools.model.ParcelableIBeacon;
import com.lowworker.android.patternTools.model.ParcelableIBeaconList;
import com.lowworker.android.patternTools.model.ParcelableRegion;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by surecase on 28/03/14.
 */
public class BeaconTrackService extends Service implements BeaconConsumer {



    public static final String ACTION_RESCHEDULE_MONITORING_FROM_DATABASE = "ibeacon.track.service" + "reschedule.monitoring.from.database";
    public static final String ACTION_STOP_MONITORING = "ibeacon.track.service" + "stop.monitoring";
    public static final String ACTION_RESCHEDULE_RANGING_FROM_DATABASE = "ibeacon.track.service" + "reschedule.ranging.from.database";
    public static final String ACTION_STOP_RANGING = "ibeacon.track.service" + "stop.ranging";
    public static final String ACTION_SET_FOREGROUND_SCAN_PERIOD = "ibeacon.track.service" + "set.foreground.scan.peroid";
    public static final String ACTION_SET_BACKGROUND_SCAN_PERIOD = "ibeacon.track.service" + "set.background.scan.peroid";
    public static final String CALLBACK_BEACON_SERVICE_CONNECTED = "ibeacon.track.service" + "beacon.service.connected";
    public static final String CALLBACK_MONITORING_ENTER = "ibeacon.track.service" + "monitoring.enter";
    public static final String CALLBACK_MONITORING_EXIT = "ibeacon.track.service" + "monitoring.exit";
    public static final String CALLBACK_MONITORING_DETERMINE_STATE = "ibeacon.track.service" + "monitoring.determine.state";
    public static final String CALLBACK_MONITORING_FROM_DATABASE_RESCHEDULE_ERROR = "ibeacon.track.service" + "monitoring.from.database.start.error";
    public static final String CALLBACK_MONITORING_RESCHEDULE_ERROR = "ibeacon.track.service" + "monitoring.start.error";
    public static final String CALLBACK_MONITORING_STOP_ERROR = "ibeacon.track.service" + "monitoring.stop.error";
    public static final String CALLBACK_RANGING_DID_RANGE = "ibeacon.track.service" + "ranging.did.range";
    public static final String CALLBACK_RANGING_FROM_DATABASE_RESCHEDULE_ERROR = "ibeacon.track.service" + "ranging.from.database.start.error";
    public static final String CALLBACK_RANGING_RESCHEDULE_ERROR = "ibeacon.track.service" + "ranging.start.error";
    public static final String CALLBACK_RANGING_STOP_ERROR = "ibeacon.track.service" + "ranging.stop.error";


    private BeaconManager iBeaconManager;
    private BroadcastReceiver trackServiceReceiver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        iBeaconManager = BeaconManager.getInstanceForApplication(this);
        iBeaconManager.getBeaconParsers().add(new BeaconParser()
                .setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
        if(BeaconTrackServiceSettings.shouldAlwaysWorkInBackground(getApplicationContext())){
            iBeaconManager.setBackgroundMode(true);
        }

        iBeaconManager.bind(this);
        registerReceiver();
    }

    private void registerReceiver() {
        trackServiceReceiver = createTrackBeaconReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_RESCHEDULE_MONITORING_FROM_DATABASE);
        filter.addAction(ACTION_STOP_MONITORING);
        filter.addAction(ACTION_RESCHEDULE_RANGING_FROM_DATABASE);
        filter.addAction(ACTION_STOP_RANGING);
        filter.addAction(ACTION_SET_BACKGROUND_SCAN_PERIOD);
        filter.addAction(ACTION_SET_FOREGROUND_SCAN_PERIOD);
        registerReceiver(trackServiceReceiver, filter);
    }

    private BroadcastReceiver createTrackBeaconReceiver() {
        return new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == null) {
                    return;

                } else if (intent.getAction().equals(ACTION_RESCHEDULE_MONITORING_FROM_DATABASE)) {
                    try {
                        rescheduleMonitoringFromDatabase(context);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_FROM_DATABASE_RESCHEDULE_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }

                } else if (intent.getAction().equals(ACTION_STOP_MONITORING)) {
                    try {
                        stopMonitoring();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_STOP_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }

                }  else if (intent.getAction().equals(ACTION_RESCHEDULE_RANGING_FROM_DATABASE)) {
                    try {
                        rescheduleRangingFromDatabase(context);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_RANGING_FROM_DATABASE_RESCHEDULE_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }

                }else if (intent.getAction().equals(ACTION_STOP_RANGING)) {
                    try {
                        stopRanging();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_RANGING_STOP_ERROR);
                        BeaconTrackService.this.sendBroadcast(callbackIntent);
                    }
                } else if (intent.getAction().equals(ACTION_SET_FOREGROUND_SCAN_PERIOD)) {
                    long milis = intent.getExtras().getLong("milis");
                    changeForegroundScanPeriod(milis);
                } else if (intent.getAction().equals(ACTION_SET_BACKGROUND_SCAN_PERIOD)) {
                    long milis = intent.getExtras().getLong("milis");
                    changeBackgroundScanPeriod(milis);
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        unregisterTrackBeaconReceiver();
        if (iBeaconManager.isBound(this)) {
            iBeaconManager.unbind(this);
        }
        super.onDestroy();
    }

    private void unregisterTrackBeaconReceiver() {
        unregisterReceiver(trackServiceReceiver);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        if (!BeaconTrackServiceSettings.shouldAlwaysWorkInBackground(this)) {
            if (iBeaconManager.isBound(this)) {
                iBeaconManager.unbind(this);
            }
            Intent intent = new Intent(this, BeaconTrackService.class);
            this.stopService(intent);
            return;
        }
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onBeaconServiceConnect() {
        Log.d("BeaconTrackService", "onBeaconServiceConnect");
        iBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {

                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getId2().toInt());
                parcelableRegion.setMinor(region.getId3().toInt());
                parcelableRegion.setProximityUuid(region.getId1().toUuidString());
                parcelableRegion.setUniqueId(region.getUniqueId());

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_ENTER);
                callbackIntent.putExtra("region", parcelableRegion);
                BeaconTrackService.this.sendBroadcast(callbackIntent);
            }

            @Override
            public void didExitRegion(Region region) {

                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getId2().toInt());
                parcelableRegion.setMinor(region.getId3().toInt());
                parcelableRegion.setProximityUuid(region.getId1().toUuidString());
                parcelableRegion.setUniqueId(region.getUniqueId());

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_EXIT);
                callbackIntent.putExtra("region", parcelableRegion);
                BeaconTrackService.this.sendBroadcast(callbackIntent);
            }

            @Override
            public void didDetermineStateForRegion(int i, Region region) {
                Log.d("BeaconTrackService", "didDetermineStateForRegion");
                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getId2().toInt());
                parcelableRegion.setMinor(region.getId3().toInt());
                parcelableRegion.setProximityUuid(region.getId1().toUuidString());
                parcelableRegion.setUniqueId(region.getUniqueId());

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_MONITORING_DETERMINE_STATE);
                callbackIntent.putExtra("region", parcelableRegion);
                BeaconTrackService.this.sendBroadcast(callbackIntent);

            }
        });

        iBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> iBeacons, Region region) {

                if (iBeacons.size() > 0) {
                    Log.i("setRangeNotifier", "The first beacon I see is about " + iBeacons.iterator().next().getDistance() + " meters away.");
                }

                ParcelableRegion parcelableRegion = new ParcelableRegion();
                parcelableRegion.setMajor(region.getId2().toInt());
                parcelableRegion.setMinor(region.getId3().toInt());
                parcelableRegion.setProximityUuid(region.getId1().toUuidString());
                parcelableRegion.setUniqueId(region.getUniqueId());

                List<ParcelableIBeacon> tempIBeaconList = new ArrayList<ParcelableIBeacon>();
                List<Beacon> IBeaconList = new ArrayList(iBeacons);
                for (int i = 0; i < iBeacons.size(); i++) {
                   BLEScut.Beacon beacon =  BeaconRepository.getBeaconByUuid(getApplicationContext(), IBeaconList.get(i).getId1().toUuidString());
                    ParcelableIBeacon tempIBeacon = new ParcelableIBeacon();

                    tempIBeacon.setProximityUuid(IBeaconList.get(i).getId1().toUuidString());
                    tempIBeacon.setDistance(IBeaconList.get(i).getDistance());
                    tempIBeacon.setBluetoothAddress(IBeaconList.get(i).getBluetoothAddress());
                    tempIBeacon.setMajor(IBeaconList.get(i).getId2().toInt());
                    tempIBeacon.setMinor(IBeaconList.get(i).getId3().toInt());
                    tempIBeacon.setRssi(IBeaconList.get(i).getRssi());
                    tempIBeacon.setTxPower(IBeaconList.get(i).getTxPower());
                    tempIBeacon.setImageUrl(beacon.getImageUrl());
                    tempIBeacon.setName(beacon.getName());
                    tempIBeacon.setDescription(beacon.getDescription());
                    tempIBeacon.setSubscriptions_count(beacon.getSubscriptions_count());
                    tempIBeacon.setLongitude(beacon.getLongitude());
                    tempIBeacon.setLatitude(beacon.getLatitude());

                    tempIBeaconList.add(tempIBeacon);
                }
                ParcelableIBeaconList parcelableIBeaconList = new ParcelableIBeaconList();
                parcelableIBeaconList.setParcelableIBeaconList(tempIBeaconList);

                Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_RANGING_DID_RANGE);
                Log.d("BTS beaconList", parcelableIBeaconList.toString());
                if(iBeacons!=null&& iBeacons.size() > 0){
                callbackIntent.putExtra("region", parcelableRegion);
                callbackIntent.putExtra("beaconList", parcelableIBeaconList);
                BeaconTrackService.this.sendBroadcast(callbackIntent);}
            }
        });

        Intent callbackIntent = new Intent(BeaconTrackService.CALLBACK_BEACON_SERVICE_CONNECTED);
        BeaconTrackService.this.sendBroadcast(callbackIntent);


    }





    private void rescheduleMonitoringFromDatabase(Context context) throws RemoteException {
       List<BLEScut.Beacon> mBeacons = BeaconRepository.getSubscriptedBeacon(context);
        Log.d("BeaconTrackService", "rescheduleMonitoringFromDatabase");

        for (int i=0; i<mBeacons.size(); i++) {

            BLEScut.Beacon beacon = mBeacons.get(i);
            String proximityUuid = beacon.getProximityUuid();
            String major = beacon.getMajor();
            String minor = beacon.getMinor();
            String uniqueId = beacon.getUniqueId();



            Region region = new Region(uniqueId, Identifier.parse(proximityUuid), Identifier.parse(major), Identifier.parse(minor));
            iBeaconManager.startMonitoringBeaconsInRegion(region);
        }
    }


    private void stopMonitoring() throws RemoteException {
        for (int i=0; i<iBeaconManager.getMonitoredRegions().size(); i++) {
            iBeaconManager.stopMonitoringBeaconsInRegion(new ArrayList<Region>(iBeaconManager.getMonitoredRegions()).get(i));
        }
    }


    private void rescheduleRangingFromDatabase(Context context) throws RemoteException {
        Log.d("BeaconTrackService","rescheduleRangingFromDatabase");
        List<BLEScut.Beacon> mBeacons = BeaconRepository.getAllBeacons(context);
        for (int i=0; i<mBeacons.size(); i++) {
            BLEScut.Beacon beacon = mBeacons.get(i);
            Log.d("beacon",beacon.getName());
            String proximityUuid = beacon.getProximityUuid();
            String major = beacon.getMajor();
            String minor = beacon.getMinor();
            String uniqueId = beacon.getUniqueId();
            Region region = new Region(uniqueId, Identifier.parse(proximityUuid), Identifier.parse(major), Identifier.parse(minor));
            iBeaconManager.startRangingBeaconsInRegion(region);
        }
    }

    private void stopRanging() throws RemoteException {
        for (int i=0; i<iBeaconManager.getMonitoredRegions().size(); i++) {

            iBeaconManager.stopRangingBeaconsInRegion(new ArrayList<Region>(iBeaconManager.getRangedRegions()).get(i));
        }
    }

    private void changeForegroundScanPeriod(long milis) {
        iBeaconManager.setForegroundScanPeriod(milis);
        try {
            iBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void changeBackgroundScanPeriod(long milis) {
        iBeaconManager.setBackgroundScanPeriod(milis);
        try {
            iBeaconManager.updateScanPeriods();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
