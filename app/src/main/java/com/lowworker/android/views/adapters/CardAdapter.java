package com.lowworker.android.views.adapters;

import android.animation.AnimatorSet;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lowworker.android.R;
import com.lowworker.android.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BLEScut.Card;
import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by froger_mcs on 05.11.14.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int VIEW_TYPE_DEFAULT = 1;


    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private boolean animateItems = false;
    private List<Card> mCardList;


    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();


    private OnCardItemClickListener onCardItemClickListener;


    public CardAdapter(Context context) {
        this.mContext = context;
    }

    public CardAdapter(Context context, List<Card> CardList) {
        this.mContext = context;
        this.mCardList = CardList;

    }

    public List<Card> getCardList() {

        return mCardList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        final CellBeaconViewHolder cellBeaconViewHolder = new CellBeaconViewHolder(view);
        if (viewType == VIEW_TYPE_DEFAULT) {
            cellBeaconViewHolder.ll_beacon.setOnClickListener(this);
            cellBeaconViewHolder.ll_card.setOnClickListener(this);
            cellBeaconViewHolder.ll_card_comment.setOnClickListener(this);
            cellBeaconViewHolder.ll_card_favorite.setOnClickListener(this);

        }

        return cellBeaconViewHolder;
    }

    private void runEnterAnimation(View view, int position) {
        if (!animateItems || position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            view.setTranslationY(Utils.getScreenHeight(mContext));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        runEnterAnimation(viewHolder.itemView, position);

        final CellBeaconViewHolder holder = (CellBeaconViewHolder) viewHolder;
        if (getItemViewType(position) == VIEW_TYPE_DEFAULT) {
            bindDefaultcardItem(position, holder);
        }
    }

    private void bindDefaultcardItem(final int position, CellBeaconViewHolder holder) {
        Card cardItem = mCardList.get(position);


        holder.tvBeaconName.setText(cardItem.getBeacon_name());
        holder.tvCardName.setText(cardItem.getTitle());
        holder.tvCardSlug.setText(cardItem.getSlug());
        Picasso.with(mContext)
                .load(cardItem.getBeacon_avatar())
                .fit().centerCrop()
                .into(holder.ivBeaconAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

//                        mCardList.get(position).setBeaconReady(true);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.tvCommentsNum.setText(cardItem.getComments_count());
        holder.tvFavoritesNum.setText(cardItem.getFavorites_count());
        holder.ll_beacon.setTag(holder);
        holder.ll_card.setTag(holder);
        holder.ll_card_comment.setTag(holder);
        holder.ll_card_favorite.setTag(holder);

    }


    public void appendCards(List<Card> cardList) {

        mCardList.addAll(cardList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return VIEW_TYPE_DEFAULT;

    }

    @Override
    public int getItemCount() {
        return mCardList.size();
    }



    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.ll_beacon) {
            if (onCardItemClickListener != null) {
                CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
                onCardItemClickListener.onBeaconClick(view, holder.getPosition());
            }
        } else if (viewId == R.id.ll_card) {
            if (onCardItemClickListener != null) {
                CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
                onCardItemClickListener.onCardClick(view, holder.getPosition());
            }
        } else if (viewId == R.id.ll_card_comment) {
            if (onCardItemClickListener != null) {
                CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
                onCardItemClickListener.onCommentsClick(view, holder.getPosition());
            }
        } else if (viewId == R.id.ll_card_favorite) {
            if (onCardItemClickListener != null) {
                CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
                onCardItemClickListener.onFavoritesClick(view,  holder.getPosition());
            }
        }
    }




    public void setOnCardItemClickListener(OnCardItemClickListener onCardItemClickListener) {
        this.onCardItemClickListener = onCardItemClickListener;
    }


    public static class CellBeaconViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.ll_beacon)
        LinearLayout ll_beacon;
        @InjectView(R.id.ll_card)
        LinearLayout ll_card;
        @InjectView(R.id.ll_card_comment)
        LinearLayout ll_card_comment;
        @InjectView(R.id.ll_card_favorite)
        LinearLayout ll_card_favorite;
        @InjectView(R.id.ivBeaconAvatar)
        ImageView ivBeaconAvatar;
        @InjectView(R.id.tvBeaconName)
        TextView tvBeaconName;
        @InjectView(R.id.tvCardName)
        TextView tvCardName;
        @InjectView(R.id.tvCardSlug)
        TextView tvCardSlug;
        @InjectView(R.id.tvCommentsNum)
        TextView tvCommentsNum;
        @InjectView(R.id.tvFavoritesNum)
        TextView tvFavoritesNum;


        public CellBeaconViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }


    public interface OnCardItemClickListener {


        public void onBeaconClick(View v, int position);
        public void onCardClick(View v, int position);
        public void onCommentsClick(View v, int position);
        public void onFavoritesClick(View v, int position);

    }
}
