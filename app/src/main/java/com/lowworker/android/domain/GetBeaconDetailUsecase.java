package com.lowworker.android.domain;


import com.lowworker.android.model.entities.BeaconDetailWrapper;

import retrofit.RetrofitError;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface GetBeaconDetailUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onBeaconDetailReceived(BeaconDetailWrapper response);


    /**
     * Request datasource the most popular movies
     */
    public void requestBeaconDetail();


    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendBeaconDetailToPresenter(BeaconDetailWrapper response);

    public void onErrorReceived(RetrofitError error);
    public void sendErrorToPresenter(RetrofitError error);
    public void unRegister();
}
