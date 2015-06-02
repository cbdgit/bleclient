
package com.lowworker.android.model.entities;

import java.io.Serializable;
import java.util.List;

import BLEScut.Beacon;

public class BeaconsWrapper implements Serializable {


    private List<Beacon> data;
    private  List<Meta> meta;

    public List<Meta> getMeta() {
        return meta;
    }

    public void setMeta(List<Meta>  meta) {
        this.meta = meta;
    }



    public BeaconsWrapper(List<Beacon> data) {

        this.data = data;
    }

    public List<Beacon> getData() {
        return data;
    }

    public void setData(List<Beacon> data) {
        this.data = data;
    }




}
