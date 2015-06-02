package com.lowworker.android.mvp.presenters;

import android.util.Log;

import com.lowworker.android.domain.GetBeaconDetailUsecaseController;
import com.lowworker.android.model.entities.BeaconDetailWrapper;
import com.lowworker.android.model.rest.RestBLESource;
import com.lowworker.android.mvp.views.BeaconDetailView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


public class BeaconDetailPresenter extends Presenter {

    private final BeaconDetailView mBeaconDetailView;
    private GetBeaconDetailUsecaseController mBeaconDetail;


    private boolean isLoading = false;
    private boolean mRegistered;
    private BeaconDetailWrapper wrapper;

    public BeaconDetailPresenter(BeaconDetailView BeaconDetailView, String beaconUuid ) {

        mBeaconDetailView = BeaconDetailView;
        mBeaconDetail = new GetBeaconDetailUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance(),beaconUuid);

    }


    public BeaconDetailPresenter(BeaconDetailView BeaconDetailView, BeaconDetailWrapper wrapper ) {

        mBeaconDetailView = BeaconDetailView;
        mBeaconDetail = new GetBeaconDetailUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance());
        this.wrapper = wrapper;

    }



    @Override
    public void start() {

        BusProvider.getUIBusInstance().register(this);
        mRegistered = true;
        Log.d("Beacon Detail Presenter","start");

        mBeaconDetailView.showLoading();
        if(wrapper ==null) {
            mBeaconDetail.execute();
        }else
        {
            onBeaconDetailReceived(wrapper);
        }
    }


    public void retry() {

        mBeaconDetailView.showLoading();
        mBeaconDetail.execute();

    }

    @Subscribe
    public void onBeaconDetailReceived(BeaconDetailWrapper response) {

        isLoading = false;
        mBeaconDetailView.hideLoading();
        mBeaconDetailView.showBeaconDetail(response);

    }





    @Subscribe

    public void onErrorReceived(RetrofitError error){

        mBeaconDetailView.showError("Network Error!");
        mBeaconDetailView.hideLoading();



    };



    @Override
    public void stop() {
        Log.d("BeaconDetail Presenter", "stop");

        BusProvider.getUIBusInstance().unregister(this);

    }

    public boolean isLoading() {

        return isLoading;
    }

    public void setLoading(boolean isLoading) {

        this.isLoading = isLoading;
    }
}