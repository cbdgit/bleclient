package com.lowworker.android.views.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lowworker.android.R;
import com.lowworker.android.model.entities.AccessToken;
import com.lowworker.android.mvp.presenters.LoginPresenter;
import com.lowworker.android.mvp.views.LoginView;
import com.lowworker.android.provider.ObscuredSharedPreferences;
import com.lowworker.android.utils.Utils;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import butterknife.InjectView;


public class LoginActivity extends BaseActivity implements LoginView , View.OnClickListener{
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;


    @InjectView(R.id.tv_activity_login_title)
    TextView tvTitle;
    @InjectView(R.id.btn_activity_login_login)
    Button btnLogin;
    @InjectView(R.id.et_activity_login_username)
    EditText etUsername;
    @InjectView(R.id.et_activity_login_password)
    EditText etPassword;



    private boolean pendingIntroAnimation;
    private LoginPresenter mLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogin.setOnClickListener(this);
        pendingIntroAnimation = true;


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
    protected boolean shouldInstallDrawer() {
        return false;
    }
    @Override
    public void showLoading() {
        Snackbar loadingSnackBar = Snackbar.with(this)
                .text("Loging in!  ")
                .actionLabel("Cancel")
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .color(getResources().getColor(R.color.style_color_primary))
                .actionColor(getResources().getColor(R.color.style_color_accent));

        SnackbarManager.show(loadingSnackBar);
    }

    @Override
    public void hideLoading() {
        SnackbarManager.dismiss();
    }

    @Override
    public void showError(String error) {

    }

    @Override
    public void hideError() {

    }


    @Override
    public void saveAccessToken(AccessToken accessToken) {
        Log.d("token", accessToken.toString());
        final SharedPreferences prefs = new ObscuredSharedPreferences(
                this, this.getSharedPreferences("AccessToken", Context.MODE_PRIVATE) );
        prefs.edit().putBoolean("login", true).apply();
        prefs.edit().putString("access_token", accessToken.getAccess_token()).apply();
        prefs.edit().putString("refresh_token", accessToken.getRefresh_token()).apply();
        prefs.edit().putString("username", etUsername.getText().toString()).apply();
        prefs.edit().putString("password", etPassword.getText().toString()).apply();
    }

    @Override
    public void navigateToHome() {
        this.finish();
    }


    @Override
    public Context getContext() {
        return this;
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
//                .start();
    }


    @Override
    public void onClick(View v) {

        mLoginPresenter = new LoginPresenter(this,etUsername.getText().toString(),etPassword.getText().toString());
        mLoginPresenter.start();
    }
}