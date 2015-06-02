package com.lowworker.android.mvp.presenters;

import android.content.Context;
import android.util.Log;

import com.lowworker.android.domain.GetCardsUsecaseController;
import com.lowworker.android.model.event.CardListEvent;
import com.lowworker.android.model.repository.CardRepository;
import com.lowworker.android.mvp.views.CardsView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;


public class CardsPresenter extends Presenter {

    private final CardsView mCardsView;
    private GetCardsUsecaseController mGetCards;



    public CardsPresenter(CardsView CardsView,Context mContext) {

        mCardsView = CardsView;
        mGetCards = new GetCardsUsecaseController(
                CardRepository.getInstance(), BusProvider.getUIBusInstance(),mContext);

    }
   

    @Override
    public void start() {

        if (mCardsView.isTheListEmpty()) {

            BusProvider.getUIBusInstance().register(this);
            Log.d("Cards Presenter","start");
            mCardsView.showLoading();
            mGetCards.execute();
        }


    }

    @Override
    public void stop() {
        Log.d("Cards Presenter","stop");
        BusProvider.getUIBusInstance().unregister(this);
    }

    @Subscribe
    public void onCardsReceived(CardListEvent response) {
        mCardsView.hideLoading();
        mCardsView.showCards(response);

    }




}