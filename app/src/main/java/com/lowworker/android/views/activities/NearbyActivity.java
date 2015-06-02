package com.lowworker.android.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.lowworker.android.BleScut;
import com.lowworker.android.R;
import com.lowworker.android.patternTools.model.ParcelableIBeacon;
import com.lowworker.android.patternTools.model.ParcelableIBeaconList;
import com.lowworker.android.patternTools.model.ParcelableRegion;
import com.lowworker.android.patternTools.service.BeaconTrackServiceHelper;
import com.lowworker.android.utils.Utils;
import com.lowworker.android.views.adapters.NearbyAdapter;
import com.lowworker.android.views.custom_views.AutofitRecyclerView;
import com.lowworker.android.views.custom_views.FeedContextMenuManager;

import butterknife.InjectView;


public class NearbyActivity extends BaseActivity implements NearbyAdapter.OnBeaconItemClickListener {

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private NearbyAdapter nearbyAdapter;
    @InjectView(R.id.rvNearby)
    AutofitRecyclerView rvNearby;



    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby);
        setupNearby();

        pendingIntroAnimation = false;
        nearbyAdapter = new NearbyAdapter(this);
        nearbyAdapter.setOnBeaconItemClickListener(this);
        rvNearby.setAdapter(nearbyAdapter);
        rvNearby.setOnScrollListener(recyclerScrollListener);
//        BeaconTrackServiceHelper.restartBeaconTrackSerivce(getApplication());
        BeaconTrackServiceHelper.rescheduleRangingFromDatabase(BleScut.getInstance());
    }


    @Override
    public void onBeaconRanging(ParcelableIBeaconList iBeaconList, ParcelableRegion region) {

        Log.d("NearbyActivity", "onBeaconRanging");

        nearbyAdapter.initAll(iBeaconList.getParcelableIBeaconList());

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStop() {

        super.onStop();

    }

    @Override
    protected void onStart() {

        super.onStart();

    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected boolean shouldInstallDrawer() {
        return false;
    }

    @Override
    protected boolean shouldNavigateBack() {
        return true;
    }

    private void setupNearby() {
        LinearLayoutManager linearLayoutManagerinearLayoutManager = new LinearLayoutManager(this) {

        };
        rvNearby.setLayoutManager(linearLayoutManagerinearLayoutManager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if (pendingIntroAnimation) {
            pendingIntroAnimation = false;
            startIntroAnimation();
        }
        return true;
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

    @Override
    public void onBeaconClick(View v, int position) {

       ParcelableIBeacon beacon = nearbyAdapter.getNearbyList().get(position);
        Intent beaconDetailIntent = new Intent(this,BeaconDetailActivity.class);
        beaconDetailIntent.putExtra("beacon_uuid", beacon.getProximityUuid());
        startActivity(beaconDetailIntent);


    }




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
}