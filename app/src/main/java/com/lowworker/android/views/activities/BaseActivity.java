package com.lowworker.android.views.activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.lowworker.android.R;
import com.lowworker.android.patternTools.base.BeaconTrackActivity;
import com.lowworker.android.utils.DrawerLayoutInstaller;
import com.lowworker.android.utils.Utils;
import com.lowworker.android.views.adapters.GlobalMenuAdapter;
import com.lowworker.android.views.custom_views.GlobalMenuView;
import com.lowworker.android.views.custom_views.LobsterTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;


/**
 * Created by Miroslaw Stanek on 19.01.15.
 */
public class BaseActivity extends BeaconTrackActivity implements GlobalMenuView.OnHeaderClickListener,GlobalMenuView.OnItemClickListener {

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Optional
    @InjectView(R.id.ltLogo)
    LobsterTextView ltLogo;


    private DrawerLayout drawerLayout;
    private GlobalMenuView menuView;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.inject(this);
        setupToolbar();
        if (shouldInstallDrawer()) {
            setupDrawer();
        }
    }


    protected void setupToolbar() {
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            if (shouldInstallDrawer()) {
                 toolbar.setNavigationIcon(R.drawable.ic_menu_white);
            }
            if (shouldNavigateBack()) {
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

            }
        }
    }

    protected boolean shouldInstallDrawer() {
        return true;
    }
    protected boolean shouldNavigateBack() {
        return false;
    }
    private void setupDrawer() {
        menuView = new GlobalMenuView(this);
        menuView.setOnHeaderClickListener(this);
        menuView.setOnItemClickListener(this);
        drawerLayout = DrawerLayoutInstaller.from(this)
                .drawerRoot(R.layout.drawer_root)
                .drawerLeftView(menuView)
                .drawerLeftWidth(Utils.dpToPx(300))
                .withNavigationIconToggler(getToolbar())
                .build();
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        inboxMenuItem = menu.findItem(R.id.action_inbox);
//        inboxMenuItem.setActionView(R.layout.menu_item_map);
//        inboxMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//           Toast.makeText(getContext(),"inboxMenuItem",Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public  GlobalMenuView getGlobalMenuView() {
        return menuView;
    }




    public LobsterTextView getLtLogo() {
        return ltLogo;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        GlobalMenuAdapter.GlobalMenuItem item = (GlobalMenuAdapter.GlobalMenuItem) parent.getAdapter().getItem(position);

        switch (item.iconResId) {

            case R.drawable.ic_global_menu_feed:
                Toast.makeText(parent.getContext(),"ic_global_menu_feed",Toast.LENGTH_LONG).show();
                break;
            case R.drawable.ic_global_menu_news:
                Toast.makeText(parent.getContext(),"ic_global_menu_news",Toast.LENGTH_LONG).show();
                break;
            case R.drawable.ic_global_menu_popular:
                Toast.makeText(parent.getContext(),"ic_global_menu_popular",Toast.LENGTH_LONG).show();
                break;
            case R.drawable.ic_global_menu_nearby:
                drawerLayout.closeDrawer(Gravity.START);
                Intent nearbyIntent = new Intent(this,NearbyActivity.class);
                startActivity(nearbyIntent);
                break;

            case R.drawable.ic_global_menu_likes:
                drawerLayout.closeDrawer(Gravity.START);
                Intent cardIntent = new Intent(this,CardActivity.class);
                startActivity(cardIntent);
                break;

            case GlobalMenuAdapter.ID_MENU_SETTING:
                drawerLayout.closeDrawer(Gravity.START);
                Intent intent = new Intent(this,SettingsActivity.class);
                startActivity(intent);
                break;
            case GlobalMenuAdapter.ID_MENU_EXIT_:
                drawerLayout.closeDrawer(Gravity.START);
               this.finish();
                break;


        }


    }


    @Override
    public void onGlobalMenuHeaderClick(final View v) {
        drawerLayout.closeDrawer(Gravity.START);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int[] startingLocation = new int[2];
//                v.getLocationOnScreen(startingLocation);
//                startingLocation[0] += v.getWidth() / 2;
//                UserProfileActivity.startUserProfileFromLocation(startingLocation, BaseActivity.this);
//                overridePendingTransition(0, 0);
//            }
//        }, 200);

        Intent intent = new Intent(this,LoginActivity.class);
intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT );
        startActivity(intent);
    }


}
