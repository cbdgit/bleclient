package com.lowworker.android.mvp.views;


import java.util.List;

import BLEScut.Beacon;

public interface MapView extends MVPView {

    void showMarkers(List<Beacon> beaconList);

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String error);

    void hideError();

    void showLoadingLabel();

    void hideActionLabel();

    boolean isTheListEmpty();


}
