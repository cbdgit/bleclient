package com.lowworker.android.domain;


import com.lowworker.android.model.entities.AccessToken;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetAccessTokenUsecaseController implements GetAccessTokenUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private String username;
    private String password;
//    private int mCurrentPage = 1;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetAccessTokenUsecaseController(RestDataSource dataSource, Bus uiBus, String username, String password) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        this.username = username;
        this.password = password;

        BusProvider.getRestBusInstance().register(this);
    }


    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {

        requestAccessToken(username, password);

    }

    @Override
    public void requestAccessToken(String username,String password) {
        mDataSource.getAccessToken(username, password);
    }

    @Subscribe
    @Override
    public void onAccessTokenReceived(AccessToken response) {

        sendAccessTokenToPresenter(response);
    }


    @Override
    public void sendAccessTokenToPresenter(AccessToken response) {
        mUiBus.post(response);
    }
}
