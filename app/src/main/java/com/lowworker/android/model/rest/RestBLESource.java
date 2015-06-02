package com.lowworker.android.model.rest;


import android.util.Log;

import com.lowworker.android.model.entities.AccessToken;
import com.lowworker.android.model.entities.BeaconDetailWrapper;
import com.lowworker.android.model.entities.BeaconsWrapper;
import com.lowworker.android.model.entities.CardDetailWrapper;
import com.lowworker.android.model.entities.CommentsWrapper;
import com.lowworker.android.model.entities.MetaError;
import com.lowworker.android.model.entities.NotificationsWrapper;
import com.lowworker.android.model.entities.SubscriptionsWrapper;
import com.lowworker.android.model.entities.UserWrapper;
import com.lowworker.android.model.event.LoginErrorEvent;
import com.lowworker.common.utils.BusProvider;
import com.lowworker.common.utils.Constants;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by saulmm on 31/01/15.
 */
public class RestBLESource implements RestDataSource {

    public static RestBLESource INSTANCE;
    private final BLEDatabaseAPI bleDBApi;

    private RestBLESource() {

        RestAdapter movieAPIRest = new RestAdapter.Builder()
            .setEndpoint(Constants.BLE_DB_HOST)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .build();

        bleDBApi = movieAPIRest.create(BLEDatabaseAPI.class);
    }



    public static RestBLESource getInstance() {

        if (INSTANCE == null)
            INSTANCE = new RestBLESource();

        return INSTANCE;
    }

    @Override
    public void getCards(String beaconId) {

//        bleDBApi.getCardsByBeaconId(beaconId,
//                retrofitCallback);
    }

    @Override
    public void getCardDetail(String id ) {

        bleDBApi.getCardDetail(id,
                retrofitCallback);
    }

    @Override
    public void getBeacons() {

        bleDBApi.getBeacons(
                retrofitCallback);
    }

    @Override
    public void getBeaconsByPage(int page) {

        bleDBApi.getBeaconsByPage(page,
                retrofitCallback);
    }

    @Override
    public void getBeaconDetail(String beaconUuid) {

       bleDBApi.getBeaconDetail(beaconUuid,
               retrofitCallback);
    }

    @Override
    public void getUserSubscriptions(String accessToken) {

        accessToken ="Bearer " + accessToken;
        bleDBApi.getUserSubscriptions(accessToken, retrofitCallback);
    }

    @Override
    public void getUserFavorite(String userId) {

    }

    @Override
    public void getUserProfile(String accessToken) {
        accessToken ="Bearer "+ accessToken;
        bleDBApi.getUserProfile(accessToken, retrofitCallback);

    }

    @Override
    public void getComments(String cardId) {
       bleDBApi.getComments(cardId,
               retrofitCallback);
    }

    @Override
    public void getAccessToken(String username, String password) {

        bleDBApi.getAccessToken(Constants.client_id, Constants.client_secret, username, password, Constants.grant_type, retrofitCallback);
    }

    @Override
    public void refreshAccessToken(String refreshToken){

        bleDBApi.refreshToken(Constants.client_id, Constants.client_secret, refreshToken, Constants.grant_type);

    }
    @Override
    public void getNotifications(String proximityUuid, String accessToken) {
        accessToken ="Bearer "+ accessToken;
        bleDBApi.getNotifications(proximityUuid, accessToken, retrofitCallback);

    }



    public Callback retrofitCallback = new Callback() {
        @Override
        public void success(Object o, Response response) {
            Log.d("rest ble source ", "retrofitCallback");

            if( response.getStatus() == 400|| response.getStatus() == 403)
                BusProvider.getRestBusInstance().post(new LoginErrorEvent(String.valueOf(response.getStatus())));

            if (o instanceof BeaconsWrapper) {

                BeaconsWrapper beaconsresponse = (BeaconsWrapper) o;

                BusProvider.getRestBusInstance().post(beaconsresponse);

            }
            else if (o instanceof NotificationsWrapper) {

                NotificationsWrapper notificationsResponse = (NotificationsWrapper) o;

                BusProvider.getRestBusInstance().post(
                        notificationsResponse);

            }
            else if (o instanceof CommentsWrapper) {

                CommentsWrapper commentsResponse = (CommentsWrapper) o;

                BusProvider.getRestBusInstance().post(
                        commentsResponse);

            }
            else if (o instanceof AccessToken) {

                AccessToken accessTokenResponse = (AccessToken) o;
                BusProvider.getRestBusInstance().post(
                        accessTokenResponse
                );

            }
            else if (o instanceof CardDetailWrapper) {

                CardDetailWrapper cardDetailWrapperResponse = (CardDetailWrapper) o;
                BusProvider.getRestBusInstance().post(
                        cardDetailWrapperResponse
                );

            }
            else if (o instanceof BeaconDetailWrapper) {

                BeaconDetailWrapper beaconDetailWrapper = (BeaconDetailWrapper) o;
                BusProvider.getRestBusInstance().post(
                        beaconDetailWrapper
                );

            }
            else if (o instanceof UserWrapper) {

                UserWrapper userWrapperResponse = (UserWrapper) o;
                BusProvider.getRestBusInstance().post(
                        userWrapperResponse
                );

            }
            else if (o instanceof SubscriptionsWrapper) {

                SubscriptionsWrapper subscriptionsWrapper = (SubscriptionsWrapper) o;
                BusProvider.getRestBusInstance().post(
                        subscriptionsWrapper
                );

            }
// else if (o instanceof ReviewsWrapper) {
//
//                ReviewsWrapper reviewsWrapper = (ReviewsWrapper) o;
//
//                BusProvider.getRestBusInstance().post(
//                    reviewsWrapper
//                );
//
//            } else if (o instanceof ImagesWrapper) {
//
//                ImagesWrapper imagesWrapper = (ImagesWrapper) o;
//
//                BusProvider.getRestBusInstance().post(
//                    imagesWrapper
//                );
            }


        @Override
        public void failure(RetrofitError error) {
            Log.d("rest ble source ", "retrofitCallback failure" + error.getMessage());

            MetaError body = (MetaError) error.getBodyAs(MetaError.class);

            BusProvider.getRestBusInstance().post(
                    error
            );
        }
    };






}
