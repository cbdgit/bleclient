package com.lowworker.android.domain;


import com.lowworker.android.model.entities.BeaconsWrapper;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetBeaconsUsecaseController implements GetBeaconsUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private int mCurrentPage = 1;
    private final int caseNormal = 1;
    private final int caseRetry = 2;


    /**
     * Constructor of the class.
     *
     *
     */
    public GetBeaconsUsecaseController(RestDataSource dataSource, Bus uiBus ) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;



    }


    @Override
    public void requestBeacons() {

        mDataSource.getBeacons();
    }



    @Subscribe
    @Override
    public void onBeaconsReceived(BeaconsWrapper response) {


        sendBeaconsToPresenter(response);

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
    public void sendBeaconsToPresenter(BeaconsWrapper response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        BusProvider.getRestBusInstance().register(this);
                requestBeacons();


    }


    public void retry() {

        requestBeacons();
    }
}
