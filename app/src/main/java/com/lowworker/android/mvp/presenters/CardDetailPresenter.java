package com.lowworker.android.mvp.presenters;

import android.util.Log;

import com.lowworker.android.domain.GetCardDetailUsecaseController;
import com.lowworker.android.model.entities.CardDetailWrapper;
import com.lowworker.android.model.rest.RestBLESource;
import com.lowworker.android.mvp.views.CardDetailView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


public class CardDetailPresenter extends Presenter {

    private final CardDetailView mCardDetailView;
    private GetCardDetailUsecaseController mCardDetail;


    private boolean isLoading = false;
    private boolean mRegistered;
    private CardDetailWrapper wrapper;

    public CardDetailPresenter(CardDetailView cardDetailView, String cardId ) {

        mCardDetailView = cardDetailView;
        mCardDetail = new GetCardDetailUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance(),cardId);

    }


    public CardDetailPresenter(CardDetailView cardDetailView, CardDetailWrapper wrapper ) {

        mCardDetailView = cardDetailView;
        mCardDetail = new GetCardDetailUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance());
        this.wrapper = wrapper;

    }



    @Override
    public void start() {

        BusProvider.getUIBusInstance().register(this);
        mRegistered = true;
        Log.d("card Detail Presenter","start");

        mCardDetailView.showLoading();
        if(wrapper ==null) {
            mCardDetail.execute();
        }else
        {
            onCardDetailReceived(wrapper);
        }
    }


    public void retry() {

        mCardDetailView.showLoading();
        mCardDetail.execute();

    }

    @Subscribe
    public void onCardDetailReceived(CardDetailWrapper response) {

        isLoading = false;
        mCardDetailView.hideLoading();
        mCardDetailView.showCardDetail(response);

    }





    @Subscribe

    public void onErrorReceived(RetrofitError error){

        mCardDetailView.showError("Network Error!");
        mCardDetailView.hideLoading();



    };



    @Override
    public void stop() {
        Log.d("CardDetail Presenter", "stop");

        BusProvider.getUIBusInstance().unregister(this);

    }

    public boolean isLoading() {

        return isLoading;
    }

    public void setLoading(boolean isLoading) {

        this.isLoading = isLoading;
    }
}