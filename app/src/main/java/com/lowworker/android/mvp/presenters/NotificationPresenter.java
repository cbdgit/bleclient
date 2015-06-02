package com.lowworker.android.mvp.presenters;

import com.lowworker.android.domain.GetNotificationsUsecaseController;
import com.lowworker.android.model.entities.NotificationsWrapper;
import com.lowworker.android.model.event.LoginErrorEvent;
import com.lowworker.android.model.rest.RestBLESource;
import com.lowworker.android.mvp.views.NotificationView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;

import retrofit.RetrofitError;


public class NotificationPresenter extends Presenter {

    private final NotificationView mNotificationView;
    private GetNotificationsUsecaseController mGetNotification;


    private boolean isLoading = false;
    private boolean   mRegistered = false;


    public NotificationPresenter(NotificationView notificationView,  String proximityUUid, String accessToken) {

        mNotificationView = notificationView;
        mGetNotification = new GetNotificationsUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance(),proximityUUid,accessToken);

    }




    @Subscribe
    public void onNotificationsReceive(NotificationsWrapper response) {

        isLoading = false;


        mNotificationView.postNotifications(response);

    }

    @Subscribe
    public void onErrorReceived(LoginErrorEvent errorEvent){
        mNotificationView.postLoginNotifications(errorEvent.toString());
    }
    @Subscribe

    public void onErrorReceived(RetrofitError error){


        if (error.getKind()== RetrofitError.Kind.NETWORK ) {
            mNotificationView.postErrorNotifications(error.toString());

        }else if(error.getKind()!= RetrofitError.Kind.NETWORK){
            if( error.getResponse().getStatus() == 400|| error.getResponse().getStatus() == 403)
            mNotificationView.postLoginNotifications(error.toString());
        }

    };



    @Override
    public void start() {

            BusProvider.getUIBusInstance().register(this);
            mRegistered = true;
        mGetNotification.execute();

    }

    public void retry() {

        mGetNotification.execute();

    }
    @Override
    public void stop() {
        mGetNotification.unRegister();
    }


}