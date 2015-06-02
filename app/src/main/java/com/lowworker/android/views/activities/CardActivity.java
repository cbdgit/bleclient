package com.lowworker.android.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lowworker.android.R;
import com.lowworker.android.model.entities.CardDetailWrapper;
import com.lowworker.android.model.event.CardListEvent;
import com.lowworker.android.mvp.presenters.CardsPresenter;
import com.lowworker.android.mvp.views.CardsView;
import com.lowworker.android.utils.AppMsg;
import com.lowworker.android.utils.Utils;
import com.lowworker.android.views.adapters.CardAdapter;
import com.lowworker.android.views.custom_views.AutofitRecyclerView;
import com.lowworker.android.views.custom_views.FeedContextMenuManager;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;

import BLEScut.Card;
import butterknife.InjectView;


public class CardActivity extends BaseActivity implements CardAdapter.OnCardItemClickListener,
        CardsView{

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    @InjectView(R.id.rvCard)
    AutofitRecyclerView rvCard;
    @InjectView(R.id.pbCard)
    ProgressBar pbCard;
    @InjectView(R.id.tv_retry)
    TextView tv_retry;
    @InjectView(R.id.btn_retry)
    Button btn_retry;
    @InjectView(R.id.ll_retry)
    LinearLayout ll_retry;

    private CardAdapter CardAdapter;

    private boolean pendingIntroAnimation;
    private CardsPresenter mCardPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        setupCards();
        pendingIntroAnimation = true;
        mCardPresenter = new CardsPresenter(this,getContext());

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onStop() {

        super.onStop();
        mCardPresenter.stop();
    }

    @Override
    protected void onStart() {

        super.onStart();
        mCardPresenter.start();
    }

    @Override
    public void showCards(CardListEvent cardList) {

        CardAdapter = new CardAdapter(this,cardList.getData());
        CardAdapter.setOnCardItemClickListener(this);
        rvCard.setAdapter(CardAdapter);
        rvCard.setOnScrollListener(recyclerScrollListener);
    }

    @Override
    public void showLoading() {
        pbCard.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbCard.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {
       if(isTheListEmpty()) {
           ll_retry.setVisibility(View.VISIBLE);
       }
    }

    @Override
    public void hideRetry() {
        ll_retry.setVisibility(View.GONE);

    }

    @Override
    public void showError(String error) {
        AppMsg.makeText(CardActivity.this, error, AppMsg.STYLE_ALERT).show();
    }

    @Override
    public void hideError() {

    }

    @Override
    public void showLoadingLabel() {
        Snackbar loadingSnackBar = Snackbar.with(this)
                .text("Loading more Cards")
                .actionLabel("Cancel")
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .color(getResources().getColor(R.color.style_color_primary))
                .actionColor(getResources().getColor(R.color.style_color_accent));

        SnackbarManager.show(loadingSnackBar);
    }

    @Override
    public void hideActionLabel() {
        SnackbarManager.dismiss();
    }

    @Override
    public boolean isTheListEmpty() {
        return (CardAdapter == null) || CardAdapter.getCardList().isEmpty();
    }

    @Override
    public void appendCards(CardListEvent  cardList) {
        CardAdapter.appendCards(cardList.getData());
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void setupCards() {
        LinearLayoutManager linearLayoutManagerinearLayoutManager  = new LinearLayoutManager(this) {

        };
        rvCard.setLayoutManager(linearLayoutManagerinearLayoutManager);
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


    @Override
    protected boolean shouldInstallDrawer() {
        return false;
    }

    @Override
    protected boolean shouldNavigateBack() {
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

    }

    @Override
    public void onCardClick(View v, int position) {
        Intent intent = new Intent(CardActivity.this,CardDetailActivity.class);

        Card card = CardAdapter.getCardList().get(position);
        intent.putExtra("card_detail_wrapper",new CardDetailWrapper(card) );
//        intent.setFlags(FL)
//        intent.putExtra("card_id","1");
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT );
        startActivity(intent);

    }

    @Override
    public void onCommentsClick(View v, int position) {

    }

    @Override
    public void onFavoritesClick(View v, int position) {

    }

    private AutofitRecyclerView.OnScrollListener recyclerScrollListener = new AutofitRecyclerView.OnScrollListener() {
        public boolean flag;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);
            FeedContextMenuManager.getInstance().onScrolled(recyclerView, dx, dy);
            visibleItemCount = recyclerView.getLayoutManager().getChildCount();
            totalItemCount = recyclerView.getLayoutManager().getItemCount();

            pastVisiblesItems = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

//            if((visibleItemCount+pastVisiblesItems) >= totalItemCount && !mCardPresenter.isLoading()) {
//                mCardPresenter.onEndListReached();
//            }



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