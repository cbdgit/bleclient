package com.lowworker.android.model;

/**
 * Created by saulmm on 31/01/15.
 */
public interface MediaDataSource {

    public void getCards(String beaconId);

    public void getCardDetail(String cardId);

    public void getBeacons();

    public void getBeaconDetail(String beaconUuid) ;

    public void getUserProfile(String accessToken);

    public void getUserSubscriptions(String userId);

    public void getUserFavorite(String userId);

    public void getComments(String cardId);

    public void getNotifications(String beaconId, String accessToken);

    public void getAccessToken(String username, String password);

    public void refreshAccessToken( String refreshToken);
}
