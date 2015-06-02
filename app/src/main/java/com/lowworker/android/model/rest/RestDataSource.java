package com.lowworker.android.model.rest;

import com.lowworker.android.model.MediaDataSource;

/**
 * Created by saulmm on 25/02/15.
 */
public interface RestDataSource extends MediaDataSource {

    public void getBeaconsByPage(int page);
}
