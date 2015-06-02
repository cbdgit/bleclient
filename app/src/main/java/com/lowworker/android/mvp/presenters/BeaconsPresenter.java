package com.lowworker.android.mvp.presenters;

import android.util.Log;

import com.lowworker.android.domain.GetBeaconsUsecaseController;
import com.lowworker.android.model.entities.BeaconsWrapper;
import com.lowworker.android.model.entities.SubscriptionsWrapper;
import com.lowworker.android.model.entities.UserWrapper;
import com.lowworker.android.model.rest.RestBLESource;
import com.lowworker.android.mvp.views.BeaconsView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


public class BeaconsPresenter extends Presenter {

    private final BeaconsView mBeaconsView;
    private GetBeaconsUsecaseController mGetBeacons;


    private boolean isLoading = false;
    private boolean mRegistered;



    public BeaconsPresenter(BeaconsView beaconsView) {

        mBeaconsView = beaconsView;


        mGetBeacons = new GetBeaconsUsecaseController(
            RestBLESource.getInstance(), BusProvider.getUIBusInstance());


    }

    public BeaconsPresenter(BeaconsView beaconsView, BeaconsWrapper beaconsWrapper) {

        mBeaconsView = beaconsView;

        mGetBeacons = new GetBeaconsUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance());

        onBeaconsReceived(beaconsWrapper);
    }

    @Subscribe
    public void onBeaconsReceived(BeaconsWrapper beaconsWrapper) {



            mBeaconsView.hideLoading();
            mBeaconsView.saveBeacons(beaconsWrapper);
            mBeaconsView.showBeacons(beaconsWrapper.getData());


        isLoading = false;
    }


    @Subscribe
    public void onErrorReceived(RetrofitError error){

        mBeaconsView.showError(error.getKind().toString());
        mBeaconsView.showBeaconsFromDatabase(error);
        mBeaconsView.hideLoading();

    };

    @Subscribe
    public void onUserProfileReceived(UserWrapper response) {

        isLoading = false;

        mBeaconsView.changeUserProfile(response);

    }

    @Subscribe
    public void onSubscriptionsReceived(SubscriptionsWrapper response) {

        mBeaconsView.saveSubscriptions(response);


    }
//    public void onEndListReached () {
//
//        mGetBeacons.execute();
//        mBeaconsView.showLoadingLabel();
//        isLoading = true;
//    }

    @Override
    public void start() {



            BusProvider.getUIBusInstance().register(this);
            mRegistered = true;
            Log.d("beacons Presenter","start");
            mBeaconsView.showLoading();
            mGetBeacons.execute();



    }




    public void retry() {

            mBeaconsView.showLoading();
            mGetBeacons.retry();

    }

    @Override
    public void stop() {
        Log.d("beacons Presenter","stop");
        mGetBeacons.unRegister();
    }

    public boolean isLoading() {

        return isLoading;
    }

    public void setLoading(boolean isLoading) {

        this.isLoading = isLoading;
    }
}