package com.lowworker.android.views.activities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.lowworker.android.BleScut;
import com.lowworker.android.R;
import com.lowworker.android.model.entities.BeaconsWrapper;
import com.lowworker.android.model.entities.NotificationsWrapper;
import com.lowworker.android.model.entities.SubscriptionsWrapper;
import com.lowworker.android.model.entities.User;
import com.lowworker.android.model.entities.UserWrapper;
import com.lowworker.android.model.repository.BeaconRepository;
import com.lowworker.android.mvp.presenters.BeaconsPresenter;
import com.lowworker.android.mvp.presenters.NotificationPresenter;
import com.lowworker.android.mvp.views.BeaconsView;
import com.lowworker.android.mvp.views.NotificationView;
import com.lowworker.android.patternTools.model.ParcelableRegion;
import com.lowworker.android.patternTools.service.BeaconTrackServiceHelper;
import com.lowworker.android.patternTools.service.BeaconTrackServiceSettings;
import com.lowworker.android.provider.ObscuredSharedPreferences;
import com.lowworker.android.utils.AppMsg;
import com.lowworker.android.utils.BeaconUtils;
import com.lowworker.android.utils.NotificationTools;
import com.lowworker.android.utils.Utils;
import com.lowworker.android.views.adapters.BeaconAdapter;
import com.lowworker.android.views.custom_views.AutofitRecyclerView;
import com.lowworker.android.views.custom_views.FeedContextMenu;
import com.lowworker.android.views.custom_views.FeedContextMenuManager;

import java.util.List;

import BLEScut.Beacon;
import butterknife.InjectView;
import retrofit.RetrofitError;


public class BeaconActivity extends BaseActivity implements BeaconAdapter.OnBeaconItemClickListener,
        FeedContextMenu.OnFeedContextMenuItemClickListener, BeaconsView ,NotificationView {

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private MenuItem mapMenuItem;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private  boolean firstIn = true;

    @InjectView(R.id.rvBeacon)
    AutofitRecyclerView rvBeacon;


    @InjectView(R.id.srl_beacon)
    SwipeRefreshLayout srl_beacon;

    private BeaconAdapter beaconAdapter;
    private BeaconsWrapper beaconsWrapper;
    private boolean pendingIntroAnimation;
    private BeaconsPresenter mBeaconPresenter;
    private NotificationPresenter mNotificationPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon);
        initialize();
        setupBeacons();
        setupRefresh();


        if (savedInstanceState == null&& firstIn) {
            pendingIntroAnimation = true;
            mBeaconPresenter = new BeaconsPresenter(this);
            firstIn =false;
        } else {


            beaconsWrapper = (BeaconsWrapper) savedInstanceState.getSerializable("beacons_wrapper");
            mBeaconPresenter = new BeaconsPresenter(this, beaconsWrapper);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        if (beaconAdapter != null) {

            outState.putSerializable("beacons_wrapper",
                    new BeaconsWrapper(beaconAdapter.getBeaconList()));

//            outState.putFloat("background_translation", mBackgroundTranslation);
        }

    }

    @Override
    protected void onStop() {

        super.onStop();
        mBeaconPresenter.stop();
    }

    @Override
    protected void onStart() {

        super.onStart();

            mBeaconPresenter.start();


    }

    @Override
    public void showBeacons(List<Beacon> beaconList) {
        beaconAdapter.appendBeacons(beaconList);

    }

    @Override
    public void showLoading() {
        srl_beacon.setRefreshing(true);
//        pbBeacon.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        srl_beacon.setRefreshing(false);
//        pbBeacon.setVisibility(View.GONE);
    }



    @Override
    public void showError(String error) {
        AppMsg.makeText(BeaconActivity.this, error, AppMsg.STYLE_ALERT).show();
    }

    @Override
    public void hideError() {

    }





    @Override
    public void appendBeacons(List<Beacon> beaconList) {
        beaconAdapter.appendBeacons(beaconList);
    }

    @Override
    public void showBeaconsFromDatabase(RetrofitError error) {
        List<Beacon> beacons =  BeaconRepository.getAllBeacons(getContext());
        beaconAdapter.appendBeacons(beacons);
    }

    @Override
    public Context getContext() {
        return this;
    }


    private  void initialize(){
        BeaconTrackServiceHelper.restartBeaconTrackSerivce(BleScut.getInstance());
        if (!BeaconUtils.checkBluetoothAvaliability()) {
            BeaconUtils.turnOnBluetoothIfNotAvaliable((Activity) getContext());
        }


    }

    private void setupBeacons() {
        LinearLayoutManager linearLayoutManagerinearLayoutManager = new LinearLayoutManager(this) {

        };
        rvBeacon.setLayoutManager(linearLayoutManagerinearLayoutManager);
        beaconAdapter = new BeaconAdapter(this);
        beaconAdapter.setOnBeaconItemClickListener(this);
        rvBeacon.setAdapter(beaconAdapter);
        rvBeacon.setOnScrollListener(recyclerScrollListener);

    }



    private void setupRefresh() {
        srl_beacon.setProgressViewOffset(false, 0, 200);

        srl_beacon.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        srl_beacon.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mBeaconPresenter.retry();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mapMenuItem = menu.findItem(R.id.action_map);
        mapMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d("BeaconActivity", "onMenuItemClick");
                Intent mapIntent = new Intent(BeaconActivity.this, MapActivity.class);
                startActivity(mapIntent);
                return false;
            }
        });
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
    }

    @Override
    public void changeUserProfile(UserWrapper userWrapper) {
        Log.d("LoginActivity", "changeUserProfile");
        User user = userWrapper.getData();
        final SharedPreferences prefs = new ObscuredSharedPreferences(
                this, this.getSharedPreferences("UserProfile", Context.MODE_PRIVATE));
        prefs.edit().putString("username", user.getUsername()).apply();
        prefs.edit().putString("avatarUrl", user.getAvatarUrl()).apply();
        getGlobalMenuView().setUpUserHeader(user);

    }

    @Override
    public void saveBeacons(BeaconsWrapper beaconsWrapper) {

        Log.d("BeaconActivity", "saveBeacons");

        List<Beacon> bs = beaconsWrapper.getData();
        for (int i = 0; i < bs.size(); i++) {

          if(BeaconRepository.isBeaconExists(getContext(), bs.get(i).getProximityUuid()))
          {
//              bs.get(i).setContent("notLoading");
              bs.get(i).setIsSubscripted(BeaconRepository.isBeaconSubscriptedByUuid(getContext(), bs.get(i).getProximityUuid()));
          }
            // Create Note object to persist
            // Populate note from JSON Object
        }


        BeaconRepository.batchBeacons(getContext(), bs);

    }

    @Override
    public void saveSubscriptions(SubscriptionsWrapper subscriptionsWrapper) {

        Log.d("BeaconActivity", "saveSubscriptions");
        List<Beacon> bs = subscriptionsWrapper.getData();

        for (int i = 0; i < bs.size(); i++) {

            // Create Note object to persist
           BeaconRepository.setBeaconSubscripted(getContext(), bs.get(i).getProximityUuid(),true);
            // Populate note from JSON Object


        }
        BeaconTrackServiceHelper.rescheduleMonitoringFromDatabase(getContext());
        beaconAdapter.notifyDataSetChanged();

    }

    private void startIntroAnimation() {
//        btnCreate.setTranslationY(2 * getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize = Utils.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getLtLogo().setTranslationY(-actionbarSize);
//        getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getLtLogo().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
//        getInboxMenuItem().getActionView().animate()
//                .translationY(0)
//                .setDuration(ANIM_DURATION_TOOLBAR)
//                .setStartDelay(500)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        startContentAnimation();
//                    }
//                })
//                .start();
    }
//
//    private void startContentAnimation() {
//        btnCreate.animate()
//                .translationY(0)
//                .setInterpolator(new OvershootInterpolator(1.f))
//                .setStartDelay(300)
//                .setDuration(ANIM_DURATION_FAB)
//                .start();
////        beaconAdapter.updateItems(true);
//    }

    @Override
    public void onCommentsClick(View v, int position) {
//        final Intent intent = new Intent(this, CommentsActivity.class);
//        int[] startingLocation = new int[2];
//        v.getLocationOnScreen(startingLocation);
//        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);
//        startActivity(intent);
//        overridePendingTransition(0, 0);
    }

    @Override
    public void onMoreClick(View v, int itemPosition) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(v, itemPosition, this);
    }

    @Override
    public void onBeaconNameClick(View v) {
        Intent beaconDetailIntent = new Intent(this,BeaconDetailActivity.class);
         Beacon beacon = beaconAdapter.getBeaconList().get((int)v.getTag());
        beaconDetailIntent.putExtra("beacon_uuid", beacon.getProximityUuid());
        startActivity(beaconDetailIntent);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSubscripteClick(View v, int position) {


    }

//    @OnClick(R.id.btnCreate)
//    public void onTakePhotoClick() {
////        int[] startingLocation = new int[2];
////        btnCreate.getLocationOnScreen(startingLocation);
////        startingLocation[0] += btnCreate.getWidth() / 2;
////        TakePhotoActivity.startCameraFromLocation(startingLocation, this);
////        overridePndingTransition(0, 0);
//    }


    private AutofitRecyclerView.OnScrollListener recyclerScrollListener = new AutofitRecyclerView.OnScrollListener() {
        public boolean flag;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);
            FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            // Is scrolling up
            if (dy > 10) {

                if (!flag) {

                    showToolbar();
                    flag = true;
                }

                // Is scrolling down
            } else if (dy < -10) {

                if (flag) {

                    hideToolbar();
                    flag = false;
                }
            }

        }
    };


    private void showToolbar() {

        getToolbar().startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.translate_up_off));
    }

    private void hideToolbar() {

        getToolbar().startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.translate_up_on));
    }

    public void onBeaconServiceConnected() {
        Log.d("BeaconActivity", "onBeaconServiceConnected");
        BeaconTrackServiceHelper.setBackgroundScanPeriod(this, 3000);
        BeaconTrackServiceHelper.setForegroundScanPeriod(this, 3000);

        BeaconTrackServiceHelper.rescheduleMonitoringFromDatabase(BleScut.getInstance());
//        BeaconTrackServiceHelper.rescheduleRangingFromDatabase(BleScut.getInstance());
    }

    public void onBeaconMonitoringEnter(ParcelableRegion region) {
        Log.d("BeaconService", "onBeaconMonitoringEnter");
        final SharedPreferences prefs = new ObscuredSharedPreferences(
                this, this.getSharedPreferences("AccessToken", Context.MODE_PRIVATE) );
        String access_token = prefs.getString("access_token",null);
        mNotificationPresenter = new NotificationPresenter(this,region.getProximityUuid(),access_token);
        mNotificationPresenter.start();
    }
    public void onActionRetryReceived() {
        Log.d("onActionRetryReceived","true");
        mNotificationPresenter.retry();
    }


    @Override
    public void postNotifications(NotificationsWrapper notificationsWrapper) {
        if (BeaconTrackServiceSettings.shouldSendNotification(getContext())){
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder bd = new Notification.Builder(getApplicationContext());
        NotificationTools.postNotificationWrapper(notificationsWrapper, getContext(), nm, bd);}
    }

    @Override
    public void postErrorNotifications(String error) {
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder bd = new Notification.Builder(getApplicationContext());
        NotificationTools.postErrorNotification(error, getContext(), nm, bd);

    }

    @Override
    public void postLoginNotifications(String error) {
        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder bd = new Notification.Builder(getApplicationContext());
        NotificationTools.postLoginNotification(error, getContext(), nm, bd);

    }

}