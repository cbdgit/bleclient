package com.lowworker.android.model.rest;

import com.lowworker.android.model.entities.AccessToken;
import com.lowworker.android.model.entities.BeaconDetailWrapper;
import com.lowworker.android.model.entities.BeaconsWrapper;
import com.lowworker.android.model.entities.CardDetailWrapper;
import com.lowworker.android.model.entities.CommentsWrapper;
import com.lowworker.android.model.entities.NotificationsWrapper;
import com.lowworker.android.model.entities.SubscriptionsWrapper;
import com.lowworker.android.model.entities.UserWrapper;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Interface representing the MovieDatabaseAPI endpoints
 * used by retrofit
 */
public interface BLEDatabaseAPI {

    @GET("/beacons")
    void getBeacons(

            Callback<BeaconsWrapper> callback);

    @GET("/beacons/{uuid}")
    void getBeaconDetail(
            @Path("uuid") String beaconUuid,
            Callback<BeaconDetailWrapper> callback);


    @GET("/beacons")
    void getBeaconsByPage(

            @Query("page") int page,
            Callback<BeaconsWrapper> callback
    );




    @GET("/cards/{id}")
    void getCardDetail(
            @Path("id") String cardId,
            Callback<CardDetailWrapper> response
    );



    @FormUrlEncoded
    @POST("/oauth/access_token")
    void getAccessToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grant_type,
            Callback<AccessToken> callback
    );

    @FormUrlEncoded
    @POST("/oauth/access_token")
    AccessToken refreshToken(
            @Field("client_id") String client_id,
            @Field("client_secret") String client_secret,
            @Field("refresh_token") String refresh_token,
            @Field("grant_type") String grant_type
    );



    @GET("/cards/{id}/comments")
    void getComments(
            @Path("id") String cardId,
            Callback<CommentsWrapper> callback);


    @GET("/beacons/{proximityUuid}/notifications")
    void getNotifications(
            @Path("proximityUuid") String proximityUuid,
            @Header("Authorization") String access_token,
            Callback<NotificationsWrapper> callback
    );


    @GET("/users/self/profile")
    void getUserProfile(
            @Header("Authorization") String access_token,
            Callback<UserWrapper> callback);


    @GET("/users/self/subscriptions")
    void getUserSubscriptions(
            @Header("Authorization") String access_token,
            Callback<SubscriptionsWrapper> callback
    );

//
//    @GET("/beacons/{id}")
//    void getBeaconDetail(
//
//            @Path("id") String beaconId,
//            Callback<BeaconDetail> callback
//    );
//
//
//    @GET("/cards/{id}")
//    void getCardDetail(
//            @Query("id") String beaconId,
//            Callback<CardDetail> response
//    );
//
//    @GET("/beacons/{id}/cards")
//    void getCardsByBeaconId(
//            @Query("id") String beaconId,
//            Callback<CardsWrapper> response
//    );
//
//
//    @GET("/cards/{id}/comments")
//    void getCommentsByCardId(
//
//            @Path("id") String cardId,
//            Callback<CommentsWrapper> response
//    );
//
//    @GET("/users/{id}/subscriptions")
//    void getUserSubscriptions(
//
//            @Path("id") String userId,
//            Callback<SubscriptionsWrapper> response
//    );
//
//    @GET("/users/{id}/favorites")
//    void getUserFavorites(
//
//            @Path("id") String userId,
//            Callback<FavoritesWrapper> response
//    );
}
