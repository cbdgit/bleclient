package com.lowworker.android.mvp.views;


import com.lowworker.android.model.entities.NotificationsWrapper;

public interface NotificationView extends MVPView {




    void postNotifications(NotificationsWrapper notificationsWrapper);

    void postErrorNotifications(String error);

    void postLoginNotifications(String error);
}
