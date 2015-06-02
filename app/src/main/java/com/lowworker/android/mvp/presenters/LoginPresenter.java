package com.lowworker.android.mvp.presenters;

import android.util.Log;

import com.lowworker.android.domain.GetAccessTokenUsecaseController;
import com.lowworker.android.domain.GetUserProfileUsecaseController;
import com.lowworker.android.domain.SyncSubscriptionsUsecaseController;
import com.lowworker.android.model.entities.AccessToken;
import com.lowworker.android.model.rest.RestBLESource;
import com.lowworker.android.mvp.views.LoginView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


public class LoginPresenter extends Presenter {

    private final LoginView mLoginView;
    private GetAccessTokenUsecaseController mLogin;
    private GetUserProfileUsecaseController mGetUserProfile;
private SyncSubscriptionsUsecaseController mSyncSubscriptions;

    private boolean isLoading = false;
    private boolean mRegistered;


    public LoginPresenter(LoginView loginView,String username,String password) {

        mLoginView = loginView;
        mLogin = new GetAccessTokenUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance(),username,password);


    }



    @Subscribe
    public void onAccessTokenReceived(AccessToken response) {

        isLoading = false;
        mLoginView.hideLoading();
        mLoginView.saveAccessToken(response);
        mLoginView.navigateToHome();

        mGetUserProfile = new GetUserProfileUsecaseController( RestBLESource.getInstance(), BusProvider.getUIBusInstance(),response.getAccess_token());
        mGetUserProfile.execute();
        mSyncSubscriptions = new SyncSubscriptionsUsecaseController(RestBLESource.getInstance(), BusProvider.getUIBusInstance(),response.getAccess_token());
        mSyncSubscriptions.execute();
    }



    @Subscribe
    public void onErrorReceived(RetrofitError error){

        mLoginView.showError(error.getKind().toString());
        mLoginView.hideLoading();

    };


    @Override
    public void start() {

            BusProvider.getUIBusInstance().register(this);
            mRegistered = true;
            Log.d("beacons Presenter","start");

            mLoginView.showLoading();
            mLogin.execute();

    }

    @Override
    public void stop() {
        Log.d("Login Presenter","stop");
        mLogin.unRegister();
    }

    public boolean isLoading() {

        return isLoading;
    }

    public void setLoading(boolean isLoading) {

        this.isLoading = isLoading;
    }
}