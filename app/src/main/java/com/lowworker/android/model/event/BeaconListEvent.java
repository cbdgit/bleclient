package com.lowworker.android.model.event;

import java.util.List;

import BLEScut.Beacon;

/**
 * Created by lowworker on 2015/5/14.
 */
public class BeaconListEvent {



        public final List<Beacon> beacons;

        public BeaconListEvent(List<Beacon> beacons) {
            this.beacons = beacons;
        }
    public List<Beacon> getData() {
        return beacons;
    }
}
