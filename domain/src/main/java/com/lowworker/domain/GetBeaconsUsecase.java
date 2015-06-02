package com.lowworker.domain;



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

    public void unRegister();
}
