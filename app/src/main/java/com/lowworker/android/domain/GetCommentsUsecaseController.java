package com.lowworker.android.domain;


import com.lowworker.android.model.entities.CommentsWrapper;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetCommentsUsecaseController implements GetCommentsUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private int mCurrentPage = 1;
    private String cardId ;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetCommentsUsecaseController(RestDataSource dataSource, Bus uiBus, String cardId) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        this.cardId = cardId;


    }


    @Override
    public void requestComments() {

        mDataSource.getComments(cardId);
    }

    @Subscribe
    @Override
    public void onCommentsReceived(CommentsWrapper response) {


        sendCommentsToPresenter(response);
        BusProvider.getRestBusInstance().unregister(this);
        System.out.print(response.getData().toString());
    }

    @Override
    public void sendCommentsToPresenter (CommentsWrapper response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        BusProvider.getRestBusInstance().register(this);
        requestComments();

    }
}
