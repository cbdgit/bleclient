package com.lowworker.android.domain;


import com.lowworker.android.model.entities.AccessToken;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface GetAccessTokenUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onAccessTokenReceived(AccessToken response);

    /**
     * Request datasource the most popular movies
     */
    public void requestAccessToken(String username,String password);

    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendAccessTokenToPresenter(AccessToken response);

    public void unRegister();
}
