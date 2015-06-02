package com.lowworker.android.mvp.views;


import com.lowworker.android.model.entities.BeaconDetailWrapper;

public interface BeaconDetailView extends MVPView {


    void showLoading();
    void showError(String error);

    void hideLoading();


    public void showBeaconDetail (BeaconDetailWrapper beaconDetailWrapper);




}
