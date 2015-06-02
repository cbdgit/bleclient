package com.lowworker.android.mvp.views;


import com.lowworker.android.model.event.CardListEvent;

public interface CardsView extends MVPView {

    void showCards(CardListEvent  cardList);

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String error);

    void hideError();

    void showLoadingLabel();

    void hideActionLabel();

    boolean isTheListEmpty();

    void appendCards(CardListEvent cardList);
}
