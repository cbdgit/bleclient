package com.lowworker.android.domain;


import android.util.Log;

import com.lowworker.android.model.entities.CardDetailWrapper;
import com.lowworker.android.model.rest.RestDataSource;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


/**
 * This class is an implementation of {@link GetBeaconsUsecase}
 */
public class GetCardDetailUsecaseController implements GetCardDetailUsecase {

    private final RestDataSource mDataSource;
    private final Bus mUiBus;
    private int mCurrentPage = 1;
    private String cardId ;
    private boolean isRegisterer = false;

    /**
     * Constructor of the class.
     *
     * @param uiBus The bus to communicate the domain module and the app module
     * @param dataSource The data source to retrieve the list of movies
     */
    public GetCardDetailUsecaseController(RestDataSource dataSource, Bus uiBus,String cardId) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        this.cardId = cardId;


    }

    public GetCardDetailUsecaseController(RestDataSource dataSource, Bus uiBus) {

        if (dataSource == null)
            throw new IllegalArgumentException("MediaDataSource cannot be null");

        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");

        mDataSource = dataSource;
        mUiBus = uiBus;
        BusProvider.getRestBusInstance().register(this);
        isRegisterer =true;
    }

    @Override
    public void requestCardDetail() {

        mDataSource.getCardDetail(cardId);
    }

    @Subscribe
    @Override
    public void onCardDetailReceived(CardDetailWrapper response) {

        Log.d("usecase", "onBeaconsReceive");
        sendCardDetailToPresenter(response);
        System.out.print(response.getData().toString());
    }

    @Subscribe
    @Override
    public void onErrorReceived(RetrofitError error) {
        sendErrorToPresenter(error);

    }


    @Override
    public void sendErrorToPresenter(RetrofitError error) {
        mUiBus.post(error);
    }

    @Override
    public void sendCardDetailToPresenter (CardDetailWrapper response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
        isRegisterer =true;
        BusProvider.getRestBusInstance().register(this);
        requestCardDetail();

    }
}
