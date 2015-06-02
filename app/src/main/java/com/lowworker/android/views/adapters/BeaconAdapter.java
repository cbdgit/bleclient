package com.lowworker.android.views.adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.lowworker.android.BleScut;
import com.lowworker.android.R;
import com.lowworker.android.model.repository.BeaconRepository;
import com.lowworker.android.patternTools.service.BeaconTrackServiceHelper;
import com.lowworker.android.utils.Utils;
import com.lowworker.android.views.custom_views.SubscriptBeaconButton;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import BLEScut.Beacon;
import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by froger_mcs on 05.11.14.
 */
public class BeaconAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int VIEW_TYPE_DEFAULT = 1;


    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIMATED_ITEMS_COUNT = 2;

    private Context mContext;
    private int lastAnimatedPosition = -1;
    private int itemsCount = 0;
    private boolean animateItems = false;
    private List<Beacon> mBeaconList;

    private final Map<Integer, Integer> likesCount = new HashMap<>();
    private final Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimations = new HashMap<>();
    private final ArrayList<Integer> likedPositions = new ArrayList<>();

    private OnBeaconItemClickListener onBeaconItemClickListener;


    public BeaconAdapter(Context context) {
        this.mContext = context;
        mBeaconList = new ArrayList<>();
    }

    public BeaconAdapter(Context context, List<Beacon> BeaconList) {
        this.mContext = context;
        this.mBeaconList = BeaconList;

    }

    public List<Beacon> getBeaconList() {

        return mBeaconList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_beacon, parent, false);
        final CellBeaconViewHolder cellBeaconViewHolder = new CellBeaconViewHolder(view);
        if (viewType == VIEW_TYPE_DEFAULT) {
            cellBeaconViewHolder.btnComments.setOnClickListener(this);
            cellBeaconViewHolder.btnMore.setOnClickListener(this);
            cellBeaconViewHolder.ivBeaconCenter.setOnClickListener(this);
            cellBeaconViewHolder.btnLike.setOnClickListener(this);
            cellBeaconViewHolder.tvBeaconName.setOnClickListener(this);
            cellBeaconViewHolder.btnSubscript.setOnClickListener(this);
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
            bindDefaultBeaconItem(position, holder);
        }
    }

    private void bindDefaultBeaconItem(final int position, CellBeaconViewHolder holder) {
        Beacon beaconItem = mBeaconList.get(position);
//        updateLikesCounter(holder, false);
//        updateHeartButton(holder, false);

        holder.tvBeaconBottom.setText(beaconItem.getDescription());
        holder.tvBeaconName.setText(beaconItem.getName());
        String currentSubscriptionsCount = beaconItem.getSubscriptions_count();
        holder.tvSubscriptionsCounter.setCurrentText(currentSubscriptionsCount);
        Picasso.with(mContext)
                .load(beaconItem.getImageUrl())
                .fit().centerCrop()
                .into(holder.ivBeaconCenter, new Callback() {
                    @Override
                    public void onSuccess() {

//                        mBeaconList.get(position).setBeaconReady(true);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.tvBeaconName.setTag(position);
        holder.btnComments.setTag(position);
        holder.btnMore.setTag(position);
        holder.ivBeaconCenter.setTag(holder);
        holder.btnLike.setTag(holder);
        if (BeaconRepository.isBeaconSubscriptedByUuid(mContext, beaconItem.getProximityUuid())) {
            holder.btnSubscript.setCurrentState(SubscriptBeaconButton.STATE_DONE);
        }
        holder.btnSubscript.setTag(holder);


        if (likeAnimations.containsKey(holder)) {
            likeAnimations.get(holder).cancel();
        }
        resetLikeAnimationState(holder);
    }


    public void appendBeacons(List<Beacon> beaconList) {
        mBeaconList.clear();
        mBeaconList.addAll(beaconList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {

        return VIEW_TYPE_DEFAULT;

    }

    @Override
    public int getItemCount() {

            return mBeaconList.size();

    }

    private void updateSubscriptionsCounter(CellBeaconViewHolder holder, boolean animated) {
        Beacon beaconItem = mBeaconList.get(holder.getPosition());
        String currentSubscriptionsCount = beaconItem.getSubscriptions_count();
        int currentSubscriptionsNum = Integer.parseInt(currentSubscriptionsCount);
        int subscriptionsCountNum = currentSubscriptionsNum + 1;

        String subscriptionsCount = String.valueOf(subscriptionsCountNum);
        if (holder.btnSubscript.getCurrentState() == SubscriptBeaconButton.STATE_DONE) {
            if (animated) {
                holder.tvSubscriptionsCounter.setText(currentSubscriptionsCount);
            } else {
                holder.tvSubscriptionsCounter.setCurrentText(currentSubscriptionsCount);
            }
        } else {
            if (animated) {
                holder.tvSubscriptionsCounter.setText(subscriptionsCount);
            } else {
                holder.tvSubscriptionsCounter.setCurrentText(subscriptionsCount);
            }
        }
    }

    private void updateHeartButton(final CellBeaconViewHolder holder, boolean animated) {
        if (animated) {
            if (!likeAnimations.containsKey(holder)) {
                AnimatorSet animatorSet = new AnimatorSet();
                likeAnimations.put(holder, animatorSet);

                ObjectAnimator rotationAnim = ObjectAnimator.ofFloat(holder.btnLike, "rotation", 0f, 360f);
                rotationAnim.setDuration(300);
                rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

                ObjectAnimator bounceAnimX = ObjectAnimator.ofFloat(holder.btnLike, "scaleX", 0.2f, 1f);
                bounceAnimX.setDuration(300);
                bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

                ObjectAnimator bounceAnimY = ObjectAnimator.ofFloat(holder.btnLike, "scaleY", 0.2f, 1f);
                bounceAnimY.setDuration(300);
                bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);
                bounceAnimY.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        holder.btnLike.setImageResource(R.drawable.ic_heart_red);
                    }
                });

                animatorSet.play(rotationAnim);
                animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);

                animatorSet.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resetLikeAnimationState(holder);
                    }
                });

                animatorSet.start();
            }
        } else {
            if (likedPositions.contains(holder.getPosition())) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            } else {
                holder.btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            }
        }
    }

    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.btnComments) {
            if (onBeaconItemClickListener != null) {
                onBeaconItemClickListener.onCommentsClick(view, (Integer) view.getTag());
            }
        } else if (viewId == R.id.btnMore) {
            if (onBeaconItemClickListener != null) {
                onBeaconItemClickListener.onMoreClick(view, (Integer) view.getTag());
            }
        } else if (viewId == R.id.btnLike) {
            CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
            if (!likedPositions.contains(holder.getPosition())) {
                likedPositions.add(holder.getPosition());

                updateHeartButton(holder, true);
            }
        } else if (viewId == R.id.ivBeaconCenter) {
            CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
            if (!likedPositions.contains(holder.getPosition())) {
//                likedPositions.add(holder.getPosition());
//                updateLikesCounter(holder, true);
//                animatePhotoLike(holder);
//                updateHeartButton(holder, false);
            }
        } else if (viewId == R.id.tvBeaconName) {

            if (onBeaconItemClickListener != null) {
                onBeaconItemClickListener.onBeaconNameClick(view);
            }
        } else if (viewId == R.id.btnSubscript) {
            CellBeaconViewHolder holder = (CellBeaconViewHolder) view.getTag();
            if (onBeaconItemClickListener != null) {
                Beacon beacon = mBeaconList.get(holder.getPosition());
                holder.btnSubscript.refreshButtonStyle();
                onBeaconItemClickListener.onSubscripteClick(view, holder.getPosition());
                if (BeaconRepository.isBeaconSubscriptedByUuid(mContext, beacon.getProximityUuid())) {
                    BeaconRepository.setBeaconSubscripted(mContext,beacon.getProximityUuid(),false);
                }else{
                    BeaconRepository.setBeaconSubscripted(mContext,beacon.getProximityUuid(),true);
                }

                BeaconTrackServiceHelper.rescheduleMonitoringFromDatabase(BleScut.getInstance());


            }
        }
    }

    private void animatePhotoLike(final CellBeaconViewHolder holder) {
        if (!likeAnimations.containsKey(holder)) {
            holder.vBgLike.setVisibility(View.VISIBLE);
            holder.ivLike.setVisibility(View.VISIBLE);

            holder.vBgLike.setScaleY(0.1f);
            holder.vBgLike.setScaleX(0.1f);
            holder.vBgLike.setAlpha(1f);
            holder.ivLike.setScaleY(0.1f);
            holder.ivLike.setScaleX(0.1f);

            AnimatorSet animatorSet = new AnimatorSet();
            likeAnimations.put(holder, animatorSet);

            ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleY", 0.1f, 1f);
            bgScaleYAnim.setDuration(200);
            bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleX", 0.1f, 1f);
            bgScaleXAnim.setDuration(200);
            bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.vBgLike, "alpha", 1f, 0f);
            bgAlphaAnim.setDuration(200);
            bgAlphaAnim.setStartDelay(150);
            bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleY", 0.1f, 1f);
            imgScaleUpYAnim.setDuration(300);
            imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleX", 0.1f, 1f);
            imgScaleUpXAnim.setDuration(300);
            imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

            ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleY", 1f, 0f);
            imgScaleDownYAnim.setDuration(300);
            imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
            ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.ivLike, "scaleX", 1f, 0f);
            imgScaleDownXAnim.setDuration(300);
            imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

            animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim, imgScaleUpXAnim);
            animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    resetLikeAnimationState(holder);
                }
            });
            animatorSet.start();
        }
    }

    private void resetLikeAnimationState(CellBeaconViewHolder holder) {
        likeAnimations.remove(holder);
        holder.vBgLike.setVisibility(View.GONE);
        holder.ivLike.setVisibility(View.GONE);
    }


    public void setOnBeaconItemClickListener(OnBeaconItemClickListener onBeaconItemClickListener) {
        this.onBeaconItemClickListener = onBeaconItemClickListener;
    }


    public static class CellBeaconViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.ivBeaconCenter)
        ImageView ivBeaconCenter;
        @InjectView(R.id.tvBeaconBottom)
        TextView tvBeaconBottom;
        @InjectView(R.id.btnComments)
        ImageButton btnComments;
        @InjectView(R.id.btnLike)
        ImageButton btnLike;
        @InjectView(R.id.btnMore)
        ImageButton btnMore;
        @InjectView(R.id.vBgLike)
        View vBgLike;
        @InjectView(R.id.ivLike)
        ImageView ivLike;
        @InjectView(R.id.tvSubscriptionsCounter)
        TextSwitcher tvSubscriptionsCounter;
        @InjectView(R.id.tvBeaconName)
        TextView tvBeaconName;
        @InjectView(R.id.vImageRoot)
        FrameLayout vImageRoot;
        @InjectView(R.id.btnSubscript)
        SubscriptBeaconButton btnSubscript;
        @InjectView(R.id.tv_subscriptions_fix)
        TextView tvSubscriptionsFix;

        public CellBeaconViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }


    public interface OnBeaconItemClickListener {
        public void onCommentsClick(View v, int position);

        public void onMoreClick(View v, int position);

        public void onBeaconNameClick(View v);

        public void onSubscripteClick(View v, int position);
    }
}
