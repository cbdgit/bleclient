package com.lowworker.android.domain;


import com.lowworker.android.model.entities.CommentsWrapper;

/**
 * Representation of an use case to get the most popular movies
 */
@SuppressWarnings("UnusedDeclaration")
public interface GetCommentsUsecase extends Usecase {

    /**
     * Callback used to be notified when the most popular Movies have been
     * received
     *
     * @param response the response containing a list with movies
     */
    public void onCommentsReceived(CommentsWrapper response);

    /**
     * Request datasource the most popular movies
     */
    public void requestComments();

    /**
     * Sends the PopularMoviesApiResponse thought the communication system
     * to be received by the presenter in another module
     *
     * @param response the response containing a list with movies
     */
    public void sendCommentsToPresenter(CommentsWrapper response);

    public void unRegister();
}
