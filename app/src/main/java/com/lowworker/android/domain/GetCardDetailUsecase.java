package com.lowworker.android.domain;


import com.lowworker.android.model.entities.CardDetailWrapper;

import retrofit.RetrofitError;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface GetCardDetailUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onCardDetailReceived(CardDetailWrapper response);
    public void onErrorReceived(RetrofitError error);
    public void sendErrorToPresenter(RetrofitError error);
    /**
     * Request datasource the most popular movies
     */
    public void requestCardDetail();

    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendCardDetailToPresenter(CardDetailWrapper response);

    public void unRegister();
}
