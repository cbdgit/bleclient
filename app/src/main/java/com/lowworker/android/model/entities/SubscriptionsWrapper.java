
package com.lowworker.android.model.entities;

import java.io.Serializable;
import java.util.List;

import BLEScut.Beacon;

public class SubscriptionsWrapper implements Serializable {


    private List<Beacon> data;



    public SubscriptionsWrapper(List<Beacon> data) {

        this.data = data;
    }

    public List<Beacon> getData() {
        return data;
    }

    public void setData(List<Beacon> data) {
        this.data = data;
    }




}
