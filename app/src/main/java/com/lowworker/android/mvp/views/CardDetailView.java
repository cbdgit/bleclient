package com.lowworker.android.mvp.views;


import com.lowworker.android.model.entities.CardDetailWrapper;

public interface CardDetailView extends MVPView {


    void showLoading();
    void showError(String error);

    void hideLoading();

    public void showCardDetail (CardDetailWrapper cardDetailWrapper);




}
