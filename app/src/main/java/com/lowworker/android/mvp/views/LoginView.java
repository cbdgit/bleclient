package com.lowworker.android.mvp.views;


import com.lowworker.android.model.entities.AccessToken;

public interface LoginView extends MVPView {



    void showLoading();

    void hideLoading();

    void showError(String error);

    void hideError();

    void saveAccessToken(AccessToken accessToken);


    void navigateToHome();


}
