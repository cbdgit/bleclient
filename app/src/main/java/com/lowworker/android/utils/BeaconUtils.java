package com.lowworker.android.utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created by surecase on 16/07/14.
 */
public class BeaconUtils {

     public static final int REQUEST_ENABLE_BT = 6050;



    static public boolean checkBluetoothAvaliability() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                return true;
            } else {
                return false;
            }
        }
    }

    static public void turnOnBluetoothIfNotAvaliable(Activity activity) {
        if (!checkBluetoothAvaliability()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, BeaconUtils.REQUEST_ENABLE_BT);
        }
    }

}
