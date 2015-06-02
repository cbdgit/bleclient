package com.lowworker.domain;

import com.lowworker.common.utils.BusProvider;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

/**
 * This class is an implementation of {@link com.lowworker.domain.GetBeaconsUsecase}
 */
public class GetBeaconsUsecaseController implements GetBeaconsUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private int mCurrentPage = 1;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetBeaconsUsecaseController(RestDataSource dataSource, Bus uiBus) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;

        BusProvider.getRestBusInstance().register(this);
    }


    @Override
    public void requestBeacons() {

        mDataSource.getBeaconsByPage(mCurrentPage);
    }

    @Subscribe
    @Override
    public void onBeaconsReceived(BeaconsWrapper response) {
        System.out.print("onBeaconsReceived");
        sendBeaconsToPresenter(response);

        System.out.print(response.getData().toString());
    }

    @Override
    public void sendBeaconsToPresenter (BeaconsWrapper response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {

        requestBeacons();
        mCurrentPage++;
    }
}
