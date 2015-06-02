package com.lowworker.android.domain;


import com.lowworker.android.model.entities.BeaconsWrapper;

import retrofit.RetrofitError;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface GetBeaconsUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onBeaconsReceived(BeaconsWrapper response);


    /**
     * Request datasource the most popular movies
     */
    public void requestBeacons();


    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendBeaconsToPresenter(BeaconsWrapper response);

    public void onErrorReceived(RetrofitError error);
    public void sendErrorToPresenter(RetrofitError error);
    public void unRegister();
}
