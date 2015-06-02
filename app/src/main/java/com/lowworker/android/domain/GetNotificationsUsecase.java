package com.lowworker.android.domain;


import com.lowworker.android.model.entities.NotificationsWrapper;

import retrofit.RetrofitError;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface GetNotificationsUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onNotificationsReceived(NotificationsWrapper response);

    /**
     * Request datasource the most popular movies
     */
    public void requestNotifications(String proximityUuid, String accessToken);

    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendNotificationsToPresenter(NotificationsWrapper response);

    public void onErrorReceived(RetrofitError error);
    public void sendErrorToPresenter(RetrofitError error);

    public void unRegister();
}
