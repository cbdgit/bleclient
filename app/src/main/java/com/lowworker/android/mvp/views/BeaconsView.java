package com.lowworker.android.mvp.views;


import com.lowworker.android.model.entities.BeaconsWrapper;
import com.lowworker.android.model.entities.SubscriptionsWrapper;
import com.lowworker.android.model.entities.UserWrapper;

import java.util.List;

import BLEScut.Beacon;
import retrofit.RetrofitError;

public interface BeaconsView extends MVPView {

    void showBeacons(List<Beacon> beaconList);

    void showLoading();

    void hideLoading();


    void showError(String error);

    void hideError();


    void changeUserProfile(UserWrapper userWrapper);

    void saveBeacons(BeaconsWrapper beaconsWrapper);

    void saveSubscriptions(SubscriptionsWrapper subscriptionsWrapper);

    void appendBeacons(List<Beacon> beaconList);

    void showBeaconsFromDatabase(RetrofitError error);
}