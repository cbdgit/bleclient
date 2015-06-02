package com.lowworker.android.views.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lowworker.android.BleScut;
import com.lowworker.android.R;
import com.lowworker.android.model.entities.BeaconDetailWrapper;
import com.lowworker.android.model.repository.BeaconRepository;
import com.lowworker.android.mvp.presenters.BeaconDetailPresenter;
import com.lowworker.android.mvp.views.BeaconDetailView;
import com.lowworker.android.patternTools.service.BeaconTrackServiceHelper;
import com.lowworker.android.utils.AppMsg;
import com.lowworker.android.utils.PicassoImageGetter;
import com.lowworker.android.views.custom_views.ObservableScrollView;
import com.lowworker.android.views.custom_views.ScrollViewListener;
import com.lowworker.android.views.custom_views.SubscriptBeaconButton;
import com.lowworker.android.views.custom_views.SubscriptBeaconCircleButton;
import com.lowworker.android.views.custom_views.TextViewFixTouchConsume;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import BLEScut.Beacon;
import butterknife.InjectView;

public class BeaconDetailActivity extends BaseActivity implements BeaconDetailView,
    ScrollViewListener {


    private BaiduMap mBaiduMap;
    private int mPrevScrollY;
    private int mScrollY;
    private BeaconDetailPresenter mBeaconDetailPresenter;

    @InjectView(R.id.btn_beacon_detail_button)
    SubscriptBeaconCircleButton btn_beacon_detail_button;

    @InjectView(R.id.iv_beacon_detail_cover)
    ImageView iv_beacon_detail_cover;
    @InjectView(R.id.tv_beacon_detail_name)
    TextView tv_beacon_detail_name;
    @InjectView(R.id.tv_beacon_detail_content)
    TextView tv_beacon_detail_content;

    @InjectView(R.id.osv_beacon_detail)
    ObservableScrollView sv_beacon_detail;


    @InjectView(R.id.srl_beacon_detail)
    SwipeRefreshLayout srl_beacon_detail;
    @InjectView(R.id.mv_beacon_detail)
    MapView mv_beacon_detail;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_detail);

        mBeaconDetailPresenter = new BeaconDetailPresenter(this, getIntent()
            .getStringExtra("beacon_uuid"));
        initBaiduMap();
    }


    public void initBaiduMap() {

        mBaiduMap = mv_beacon_detail.getMap();
        mBaiduMap.clear();
        mBaiduMap.setMyLocationEnabled(true);

        int count = mv_beacon_detail.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mv_beacon_detail.getChildAt(i);
            // 隐藏百度logo ZoomControl
            // if (child instanceof ImageView || child instanceof ZoomControls)
            // {
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }

    }
    public void addBeaconsOverlay(Beacon beacon) {

        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor mIconMaker = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_map_marker);

            // 位置

            latLng = new LatLng(Double.valueOf(beacon.getLatitude()), Double.valueOf(beacon.getLongitude()));
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mIconMaker).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("beacon", beacon);
            marker.setExtraInfo(bundle);
            Log.d("MapActivity", "addMarker");


        // 将地图移到到最后一个经纬度位置

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 18);

        mBaiduMap.setMapStatus(u);
    }
    @Override
    protected boolean shouldInstallDrawer() {
        return false;
    }

    @Override
    protected boolean shouldNavigateBack() {
        return true;
    }



    @Override
    protected void onStop() {

        super.onStop();
        mBeaconDetailPresenter.stop();
    }

    @Override
    protected void onStart() {

        super.onStart();
        mBeaconDetailPresenter.start();
    }



    @Override
    public Context getContext() {

        return this;
    }




    @Override
    public void showBeaconDetail(final BeaconDetailWrapper beaconDetailWrapper) {
        if   (BeaconRepository.isBeaconSubscriptedByUuid(getContext(), beaconDetailWrapper.getData().get(0).getProximityUuid())) {
            btn_beacon_detail_button.setCurrentState(SubscriptBeaconButton.STATE_DONE);
        }
        btn_beacon_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Beacon beacon = beaconDetailWrapper.getData().get(0);
                    btn_beacon_detail_button.refreshButtonStyle();
                    if (BeaconRepository.isBeaconSubscriptedByUuid(getContext(), beacon.getProximityUuid())) {
                        BeaconRepository.setBeaconSubscripted(getContext(),beacon.getProximityUuid(),false);
                    }else{
                        BeaconRepository.setBeaconSubscripted(getContext(),beacon.getProximityUuid(),true);
                    }

                    BeaconTrackServiceHelper.rescheduleMonitoringFromDatabase(BleScut.getInstance());
                }

        });
        addBeaconsOverlay(beaconDetailWrapper.getData().get(0));
        setHead(beaconDetailWrapper);
        setContent(beaconDetailWrapper.getData().get(0).getContent());
    }

    protected void setHead(BeaconDetailWrapper beaconDetailWrapper) {
        Picasso.with(getContext()).load(beaconDetailWrapper.getData().get(0).getImageUrl()).into(iv_beacon_detail_cover);
        tv_beacon_detail_name.setText(beaconDetailWrapper.getData().get(0).getName());

    }
    protected void setContent(String text) {

        Spanned spanned = Html.fromHtml(text, new PicassoImageGetter(tv_beacon_detail_content, getContext()), null);


        SpannableStringBuilder htmlSpannable;
        if (spanned instanceof SpannableStringBuilder) {
            htmlSpannable = (SpannableStringBuilder) spanned;
        } else {
            htmlSpannable = new SpannableStringBuilder(spanned);
        }

        ImageSpan[] spans = htmlSpannable.getSpans(0, htmlSpannable.length(), ImageSpan.class);
        final ArrayList<String> imageUrls = new ArrayList<String>();
        final ArrayList<String> imagePositions = new ArrayList<String>();
        for (ImageSpan currentSpan : spans) {
            final String imageUrl = currentSpan.getSource();
            final int start = htmlSpannable.getSpanStart(currentSpan);
            final int end = htmlSpannable.getSpanEnd(currentSpan);
            imagePositions.add(start + "," + end);
            imageUrls.add(imageUrl);
        }

        for (ImageSpan currentSpan : spans) {
            final int start = htmlSpannable.getSpanStart(currentSpan);
            final int end = htmlSpannable.getSpanEnd(currentSpan);

            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    PhotoViewActivity.launch(getContext(), imagePositions.indexOf(start + "," + end), imageUrls);
                }
            };

            ClickableSpan[] clickSpans = htmlSpannable.getSpans(start, end, ClickableSpan.class);
            if (clickSpans != null && clickSpans.length != 0) {

                for (ClickableSpan c_span : clickSpans) {
                    htmlSpannable.removeSpan(c_span);
                }
            }

            htmlSpannable.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tv_beacon_detail_content.setMovementMethod(
                TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance()
        );
        tv_beacon_detail_content.setText(spanned);
    }
    @Override
    public void showLoading() {
//        pbCardDetail.setVisibility(View.VISIBLE);
        srl_beacon_detail.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
//        pbCardDetail.setVisibility(View.GONE);
        srl_beacon_detail.setRefreshing(false);
    }



    @Override
    public void showError(String error) {
        AppMsg.makeText(BeaconDetailActivity.this, error, AppMsg.STYLE_ALERT).show();
    }

    boolean isTranslucent = false;
    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        mScrollY = y;
        if (mPrevScrollY < y) {
            showToolbar();

        } else if (y < mPrevScrollY) {
            hideToolbar();

        }
        mPrevScrollY = y;

    }

    private void showToolbar() {

        getToolbar().startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.translate_up_off));
    }

    private void hideToolbar() {

        getToolbar().startAnimation(AnimationUtils.loadAnimation(this,
                R.anim.translate_up_on));
    }

}
