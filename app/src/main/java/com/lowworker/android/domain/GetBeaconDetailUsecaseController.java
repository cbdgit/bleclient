package com.lowworker.android.domain;


import com.lowworker.android.model.entities.BeaconDetailWrapper;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetBeaconDetailUsecaseController implements GetBeaconDetailUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private String beaconUuid ;
    private boolean isRegisterer = false;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetBeaconDetailUsecaseController(RestDataSource dataSource, Bus uiBus,String beaconUuid) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        this.beaconUuid = beaconUuid;


    }

    public GetBeaconDetailUsecaseController(RestDataSource dataSource, Bus uiBus) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        BusProvider.getRestBusInstance().register(this);
        isRegisterer =true;
    }

    @Override
    public void requestBeaconDetail() {

        mDataSource.getBeaconDetail(beaconUuid);
    }

    @Subscribe
    @Override
    public void onBeaconDetailReceived(BeaconDetailWrapper response) {

        sendBeaconDetailToPresenter(response);
        System.out.print(response.getData().toString());
    }

    @Subscribe
    @Override
    public void onErrorReceived(RetrofitError error) {
        sendErrorToPresenter(error);

    }


    @Override
    public void sendErrorToPresenter(RetrofitError error) {
        mUiBus.post(error);
    }

    @Override
    public void sendBeaconDetailToPresenter (BeaconDetailWrapper response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        isRegisterer =true;
        BusProvider.getRestBusInstance().register(this);
        requestBeaconDetail();

    }
}
