package com.lowworker.android.views.activities;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lowworker.android.BleScut;
import com.lowworker.android.R;
import com.lowworker.android.model.repository.BeaconRepository;
import com.lowworker.android.patternTools.service.BeaconTrackServiceHelper;
import com.lowworker.android.utils.MyOrientationListener;
import com.lowworker.android.utils.Utils;
import com.lowworker.android.views.custom_views.SubscriptBeaconButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import BLEScut.Beacon;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MapActivity extends BaseActivity {

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private BaiduMap mBaiduMap;
    @InjectView(R.id.mv_beacon)
    MapView mv_beacon;
    @InjectView(R.id.ibMyLocation)
    ImageButton ibMyLocation;

    private LocationClient mLocClient;
    private MyOrientationListener myOrientationListener;
    private MyLocationConfiguration.LocationMode mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
    private int mXDirection;
    private boolean pendingIntroAnimation;
    private double mCurrentLantitude = 0.0;
    private double mCurrentLongitude = 0.0;
    boolean isFirstLoc = true;
    private float mCurrentAccracy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initBaiduMap();
        initOritationListener();
        addBeaconsOverlay();
        SetupLocation();
        setUpMarker();
        pendingIntroAnimation = false;


    }

    public void initBaiduMap() {

        mBaiduMap = mv_beacon.getMap();
        mBaiduMap.clear();
        mBaiduMap.setMyLocationEnabled(true);


    }

    private void initOritationListener() {
        myOrientationListener = new MyOrientationListener(
                getApplicationContext());
        myOrientationListener
                .setOnOrientationListener(new MyOrientationListener.OnOrientationListener() {
                    @Override
                    public void onOrientationChanged(float x) {
                        mXDirection = (int) x;
                        MyLocationData locData = new MyLocationData.Builder()
                                .accuracy(mCurrentAccracy)
                                        // 此处设置开发者获取到的方向信息，顺时针0-360
                                .direction(mXDirection)
                                .latitude(mCurrentLantitude)
                                .longitude(mCurrentLongitude).build();
                        // 设置定位数据
                        mBaiduMap.setMyLocationData(locData);

                        // 设置自定义图标
                        BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory
                                .fromResource(R.drawable.ic_map_navi);


                        MyLocationConfiguration config = new MyLocationConfiguration(
                                mCurrentMode, true, mCurrentMarker);
                        mBaiduMap.setMyLocationConfigeration(config);

                    }
                });
    }

    public void SetupLocation() {


        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null || mv_beacon == null)
                    return;


                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                                // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(mXDirection).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                mBaiduMap.setMyLocationData(locData);
                mCurrentAccracy = bdLocation.getRadius();
                mCurrentLantitude = bdLocation.getLatitude();
                mCurrentLongitude = bdLocation.getLongitude();
                if (isFirstLoc) {
                    isFirstLoc = false;
                    LatLng ll = new LatLng(bdLocation.getLatitude(),
                            bdLocation.getLongitude());
                    MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                    mBaiduMap.animateMapStatus(u);
                }
            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

    }

    public void addBeaconsOverlay() {

        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        BitmapDescriptor mIconMaker = BitmapDescriptorFactory
                .fromResource(R.drawable.ic_map_marker);
        List<Beacon> beacons = BeaconRepository.getAllBeacons(getContext());
        for (Beacon beacon : beacons) {
            // 位置
            Log.d("ProximityUuid", beacon.getProximityUuid());
            Log.d("Latitude", beacon.getLatitude());
            Log.d("Longitude", beacon.getLongitude());
            latLng = new LatLng(Double.valueOf(beacon.getLatitude()), Double.valueOf(beacon.getLongitude()));
            // 图标
            overlayOptions = new MarkerOptions().position(latLng)
                    .icon(mIconMaker).zIndex(5);
            marker = (Marker) (mBaiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("beacon", beacon);
            marker.setExtraInfo(bundle);
            Log.d("MapActivity", "addMarker");

        }
        // 将地图移到到最后一个经纬度位置

        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(latLng, 18);

        mBaiduMap.setMapStatus(u);
    }


    public void setUpMarker() {

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                //获得marker中的数据
                final Beacon beacon = (Beacon) marker.getExtraInfo().get("beacon");

                InfoWindow mInfoWindow;
                //生成一个TextView用户在地图中显示InfoWindow
                final View mView = LayoutInflater.from(getContext()).inflate(R.layout.item_popup, null);

                TextView tvBeaconBottom = ButterKnife.findById(mView, R.id.tvBeaconBottom);
                TextView tvBeaconProfile = ButterKnife.findById(mView, R.id.tvBeaconProfile);
                ImageView ivBeaconMap = ButterKnife.findById(mView, R.id.ivBeaconMap);
               final SubscriptBeaconButton btnSubscript = ButterKnife.findById(mView, R.id.btnSubscript);
                tvBeaconBottom.setText(beacon.getDescription());
                tvBeaconProfile.setText(beacon.getName());
                if (BeaconRepository.isBeaconSubscriptedByUuid(getContext(), beacon.getProximityUuid())) {
                    btnSubscript.setCurrentState(SubscriptBeaconButton.STATE_DONE);
                }
                Picasso.with(getContext()).load(beacon.getImageUrl()).into(ivBeaconMap);

                btnSubscript.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (BeaconRepository.isBeaconSubscriptedByUuid(getContext(), beacon.getProximityUuid())) {
                            BeaconRepository.setBeaconSubscripted(getContext(), beacon.getProximityUuid(), false);
                            btnSubscript.setCurrentState(SubscriptBeaconButton.STATE_DONE);
                        } else {
                            BeaconRepository.setBeaconSubscripted(getContext(), beacon.getProximityUuid(), true);
                            btnSubscript.setCurrentState(SubscriptBeaconButton.STATE_Subscript);
                        }
                        BeaconTrackServiceHelper.rescheduleMonitoringFromDatabase(BleScut.getInstance());
                    }
                });

                //将marker所在的经纬度的信息转化成屏幕上的坐标
                final LatLng ll = marker.getPosition();
                Point p = mBaiduMap.getProjection().toScreenLocation(ll);

                p.y -= 47;
                LatLng llInfo = mBaiduMap.getProjection().fromScreenLocation(p);
                //为弹出的InfoWindow添加点击事件

                mInfoWindow = new InfoWindow(mView, llInfo,
                        -47);
                //显示InfoWindow
                mBaiduMap.showInfoWindow(mInfoWindow);
                //设置详细信息布局为可见


                return true;
            }
        });

    }

    @OnClick(R.id.ibMyLocation)
    public void goToMyLocation(View view) {
        if (mCurrentLantitude != 0 && mCurrentLongitude != 0) {
            LatLng ll = new LatLng(mCurrentLantitude, mCurrentLongitude);
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(u);
        }
    }


    @Override
    protected void onStart() {
        // 开启图层定位
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocClient.isStarted()) {
            mLocClient.start();
        }
        // 开启方向传感器
        myOrientationListener.start();
        super.onStart();
    }

    @Override
    protected void onStop() {
        // 关闭图层定位
        mBaiduMap.setMyLocationEnabled(false);
        mLocClient.stop();

        // 关闭方向传感器
        myOrientationListener.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mv_beacon.onDestroy();
        mv_beacon = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mv_beacon.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mv_beacon.onPause();
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


}