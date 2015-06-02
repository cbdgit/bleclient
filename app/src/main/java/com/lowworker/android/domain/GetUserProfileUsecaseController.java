package com.lowworker.android.domain;


import com.lowworker.android.model.entities.UserWrapper;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetUserProfileUsecaseController implements GetUserProfileUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private String accessToken;

//    private int mCurrentPage = 1;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetUserProfileUsecaseController(RestDataSource dataSource, Bus uiBus, String accessToken) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        this.accessToken = accessToken;



    }


    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        BusProvider.getRestBusInstance().register(this);
        requestUserProfile( accessToken);

    }

    @Override
    public void requestUserProfile(String accessToken) {
        mDataSource.getUserProfile(accessToken);
    }

    @Subscribe
    @Override
    public void onUserProfileReceived(UserWrapper response) {

        sendUserProfileToPresenter(response);
    }


    @Override
    public void sendUserProfileToPresenter(UserWrapper response) {
        mUiBus.post(response);
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
}