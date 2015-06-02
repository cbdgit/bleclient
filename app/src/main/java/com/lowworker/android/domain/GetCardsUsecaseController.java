package com.lowworker.android.domain;


import android.content.Context;

import com.lowworker.android.model.event.CardListEvent;
import com.lowworker.android.model.repository.CardRepository;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;


/**
 * This class is an implementation of {@link GetCardsUsecase}
 */
public class GetCardsUsecaseController implements GetCardsUsecase {

   
    private final Bus mUiBus;
    private final CardRepository mCardRepository;
    private  Context mContext;

    public GetCardsUsecaseController(CardRepository cardRepository, Bus uiBus,Context context) {
        
        if (uiBus == null)
            throw new IllegalArgumentException("Bus cannot be null");
        
        mUiBus = uiBus;
        mCardRepository = cardRepository;
        mContext = context;
        BusProvider.getRestBusInstance().register(this);
    }


    @Override
    public void requestCards() {

        mCardRepository.getAllCardes(mContext);
    }

    @Subscribe
    @Override
    public void onCardsReceived(CardListEvent response) {

        sendCardsToPresenter(response);

    }


  
    @Override
    public void sendCardsToPresenter(CardListEvent response) {

        mUiBus.post(response);
    }

    @Override
    public void unRegister() {

        BusProvider.getRestBusInstance().unregister(this);
    }

    @Override
    public void execute() {
                requestCards();
    }

}
