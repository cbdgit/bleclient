package com.lowworker.android.domain;


import android.util.Log;

import com.lowworker.android.model.entities.NotificationsWrapper;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetNotificationsUsecaseController implements GetNotificationsUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private String proximityUuid;
    private String accessToken;
//    private int mCurrentPage = 1;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetNotificationsUsecaseController(RestDataSource dataSource, Bus uiBus,String proximityUuid,String accessToken) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        this.proximityUuid = proximityUuid;
        this.accessToken = accessToken;

    }


    @Override
    public void requestNotifications(String proximityUuid, String accessToken) {

        mDataSource.getNotifications(proximityUuid,accessToken);
    }

    @Subscribe
    @Override
    public void onNotificationsReceived (NotificationsWrapper response) {

        Log.d("usecase", "onNotificationsReceived");
        sendNotificationsToPresenter(response);

        System.out.print(response.getData().toString());
    }
    @Subscribe
    @Override
    public void onErrorReceived(RetrofitError error){
        sendErrorToPresenter(error);
    };

    @Override
    public void sendErrorToPresenter(RetrofitError error){
        mUiBus.post(error);
    };

    @Override
    public void sendNotificationsToPresenter (NotificationsWrapper response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        BusProvider.getRestBusInstance().register(this);
        requestNotifications(proximityUuid,accessToken);

    }
}
