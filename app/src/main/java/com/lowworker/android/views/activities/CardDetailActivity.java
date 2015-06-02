package com.lowworker.android.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lowworker.android.R;
import com.lowworker.android.model.entities.CardDetailWrapper;
import com.lowworker.android.model.repository.CardRepository;
import com.lowworker.android.mvp.presenters.CardDetailPresenter;
import com.lowworker.android.mvp.views.CardDetailView;
import com.lowworker.android.utils.AppMsg;
import com.lowworker.android.utils.PicassoImageGetter;
import com.lowworker.android.views.custom_views.ObservableScrollView;
import com.lowworker.android.views.custom_views.ScrollViewListener;
import com.lowworker.android.views.custom_views.TextViewFixTouchConsume;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import BLEScut.Card;
import butterknife.InjectView;


public class CardDetailActivity extends BaseActivity implements CardDetailView, ScrollViewListener, Toolbar.OnMenuItemClickListener, View.OnClickListener {
    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIM_DURATION_TOOLBAR = 300;
    private static final int ANIM_DURATION_FAB = 400;
    private int mPrevScrollY;
    private int mScrollY;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;
    private GestureDetector productGestureDetector;
    private View.OnTouchListener productGestureListener;
    TextView tv_favorites_count;
    TextView tv_comments_count;
    ImageButton btn_like;
    private Boolean isLiked = false;

    @InjectView(R.id.iv_activity_card_detail_cover)
    ImageView iv_card_cover;
    @InjectView(R.id.tv_activity_card_detail_title)
    TextView tv_card_title;
    @InjectView(R.id.iv_activity_card_detail_beacon_image)
    ImageView iv_beacon_image;
    @InjectView(R.id.tv_activity_card_detail_beacon_name)
    TextView tv_beacon_name;
    @InjectView(R.id.tv_activity_card_detail_content)
    TextView tv_card_content;
    @InjectView(R.id.activity_movie_detail_scroll)
    ObservableScrollView sv_card;

    @InjectView(R.id.srl_card_detail)
    SwipeRefreshLayout srl_card_detail;


    private boolean pendingIntroAnimation;
    private CardDetailPresenter mCardDetailPresenter;
    private MenuItem miBack;
    private MenuItem miFavorite;
    private MenuItem miComment;
    private MenuItem miFavoriteNum;
    private MenuItem miCommentNum;
    private MenuItem miStar;
    private String card_id;
    private CardDetailWrapper cardDetailWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        getToolbar().setNavigationOnClickListener(this);
        getToolbar().setOnMenuItemClickListener(this);

        sv_card.setScrollViewListener(this);

        setupRefresh();
        if(getIntent().getSerializableExtra("card_detail_wrapper")!=null){
            Log.d("CardDetailActivity", "!-null");
           final  CardDetailWrapper wrapper  = (CardDetailWrapper)getIntent().getSerializableExtra("card_detail_wrapper");
            Log.d("CardDetailActivity", wrapper.getData().getBeacon_name());
                    mCardDetailPresenter = new CardDetailPresenter(CardDetailActivity.this, wrapper); //execute the task

        }else if(getIntent().getStringExtra("card_id")!= null){
        card_id = getIntent().getStringExtra("card_id");
        mCardDetailPresenter = new CardDetailPresenter(this, card_id);
        }

//        mCardDetailPresenter = new CardDetailPresenter(this, 1+"");

    }


    @Override
    public void showCardDetail(final CardDetailWrapper cardDetailWrapper) {
        this.cardDetailWrapper = cardDetailWrapper;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                setToolBarIcon(cardDetailWrapper);
            }
        }, 800);

        setHead(cardDetailWrapper);
        String htmlText = "<div>" +cardDetailWrapper.getData().getContent()+"</div>";
        setText(htmlText);
    }

    @Override
    protected boolean shouldInstallDrawer() {
        return false;
    }

    @Override
    protected boolean shouldNavigateBack() {
        return true;
    }


    private void setupRefresh() {

        srl_card_detail.setProgressViewOffset(false, 0, 300);

        srl_card_detail.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        srl_card_detail.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCardDetailPresenter.retry();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_card_view, menu);

        miStar = menu.findItem(R.id.action_star);
        miFavorite = menu.findItem(R.id.action_favorite);
        miComment = menu.findItem(R.id.action_comment);
        miFavoriteNum = menu.findItem(R.id.action_favorite_num);
        miCommentNum = menu.findItem(R.id.action_comment_num);

        View vComment = LayoutInflater.from(this).inflate(R.layout.menu_item_comment_num, null);
        View vFavorite = LayoutInflater.from(this).inflate(R.layout.menu_item_favorite_num, null);
        miFavoriteNum.setActionView(vFavorite);
        miCommentNum.setActionView(vComment);

        tv_favorites_count = (TextView) miFavoriteNum.getActionView();
        tv_comments_count = (TextView) miCommentNum.getActionView();


        return true;
    }

    protected void setHead(CardDetailWrapper cardDetailWrapper) {
        Picasso.with(getContext()).load(cardDetailWrapper.getData().getCover_url()).into(iv_card_cover);
        Picasso.with(getContext()).load(cardDetailWrapper.getData().getBeacon_avatar()).into(iv_beacon_image);
        tv_beacon_name.setText(cardDetailWrapper.getData().getBeacon_name());
        tv_card_title.setText(cardDetailWrapper.getData().getTitle());


    }

    protected void setToolBarIcon(CardDetailWrapper cardDetailWrapper) {

        if (CardRepository.isCardSavedByCardId(getContext(), cardDetailWrapper.getData().getCard_id())) {
            miStar.setIcon(R.drawable.ic_star_white_36dp);
        } else {
            miStar.setIcon(R.drawable.ic_star_outline_white_36dp);

        }
        tv_comments_count.setText(cardDetailWrapper.getData().getComments_count());
        tv_favorites_count.setText(cardDetailWrapper.getData().getFavorites_count());

    }

    protected void setText(String text) {

        Spanned spanned = Html.fromHtml(text, new PicassoImageGetter(tv_card_content, getContext()), null);


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
        tv_card_content.setMovementMethod(
                TextViewFixTouchConsume.LocalLinkMovementMethod.getInstance()
        );
        tv_card_content.setText(spanned);
    }

    @Override
    protected void onStop() {

        super.onStop();
        mCardDetailPresenter.stop();
    }

    @Override
    protected void onStart() {

        super.onStart();
        mCardDetailPresenter.start();
    }


    @Override
    public void showLoading() {
//        pbCardDetail.setVisibility(View.VISIBLE);
        srl_card_detail.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
//        pbCardDetail.setVisibility(View.GONE);
        srl_card_detail.setRefreshing(false);
    }



    @Override
    public void showError(String error) {
        AppMsg.makeText(CardDetailActivity.this, error, AppMsg.STYLE_ALERT).show();
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy) {
        mScrollY = y;
        if (mPrevScrollY < y) {


        } else if (y < mPrevScrollY) {


        }
        mPrevScrollY = y;

    }



    @Override
    public void onClick(View v) {
        CardDetailActivity.this.finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.action_star:
                if (CardRepository.isCardSavedByCardId(getContext(), cardDetailWrapper.getData().getCard_id())) {
                    menuItem.setIcon(R.drawable.ic_star_outline_white_36dp);
                    removeCard(cardDetailWrapper);
                } else {
                    menuItem.setIcon(R.drawable.ic_star_white_36dp);
                    saveCard(cardDetailWrapper);
                }
                break;

            case R.id.action_favorite:
                menuItem.setIcon(R.drawable.ic_heart_red);
                break;

            case R.id.action_comment:
                final Intent intent = new Intent(CardDetailActivity.this, CommentsActivity.class);
                int[] startingLocation = new int[2];
                getToolbar().getLocationOnScreen(startingLocation);
                intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION, startingLocation[1]);

                intent.putExtra("card_id", card_id);
                startActivity(intent);
                overridePendingTransition(0, 0);

                break;

        }


        return true;
    }


    public void saveCard(CardDetailWrapper cardDetailWrapper) {


        CardRepository.getInstance().insertOrUpdate(getContext(), cardDetailWrapper.getData());
    }

    public void removeCard(CardDetailWrapper cardDetailWrapper) {

        Card restCard = cardDetailWrapper.getData();
        String cardId = restCard.getCard_id();
        CardRepository.getInstance().deleteCardById(getContext(), cardId);
    }

}