package com.lowworker.android.domain;


import com.lowworker.android.model.entities.SubscriptionsWrapper;

import retrofit.RetrofitError;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface SyncSubscriptionsUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onSubscriptionsReceived(SubscriptionsWrapper response);
    public void onErrorReceived(RetrofitError error);
    public void sendErrorToPresenter(RetrofitError error);
    /**
     * Request datasource the most popular movies
     */
    public void requestSubscriptions(String accessToken);

    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendSubscriptionsToPresenter(SubscriptionsWrapper response);

    public void unRegister();
}
