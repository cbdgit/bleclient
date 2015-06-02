package com.lowworker.android.views.adapters;

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
import com.lowworker.android.patternTools.model.ParcelableIBeacon;
import com.lowworker.android.utils.RoundedTransformation;
import com.lowworker.android.utils.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * Created by froger_mcs on 05.11.14.
 */
public class NearbyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final int VIEW_TYPE_DEFAULT = 1;


    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int avatarSize;
    private Context mContext;
    private int lastAnimatedPosition = -1;
    private boolean animateItems = false;
    private List<ParcelableIBeacon> mBeaconList = new ArrayList<>();



    private OnBeaconItemClickListener onBeaconItemClickListener;


    public NearbyAdapter(Context context) {
        this.mContext = context;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);
    }

    public NearbyAdapter(Context context, List<ParcelableIBeacon> BeaconList) {
        this.mContext = context;
        this.mBeaconList = BeaconList;
        avatarSize = context.getResources().getDimensionPixelSize(R.dimen.comment_avatar_size);

    }
    public void initAll( List<ParcelableIBeacon> BeaconList) {
        this.mBeaconList.clear();
        this.mBeaconList.addAll(BeaconList);
        notifyDataSetChanged();
    }
    public List<ParcelableIBeacon> getNearbyList() {

        return mBeaconList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.item_nearby, parent, false);
        final CellBeaconViewHolder cellBeaconViewHolder = new CellBeaconViewHolder(view);
        if (viewType == VIEW_TYPE_DEFAULT) {
            cellBeaconViewHolder.ll_beacon_item.setOnClickListener(this);
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
        ParcelableIBeacon BeaconItem = mBeaconList.get(position);


        holder.tvBeaconName.setText(BeaconItem.getName());
        holder.tvBeaconDescription.setText(BeaconItem.getDescription());
        Picasso.with(mContext)
                .load(BeaconItem.getImageUrl())
                .centerCrop()
                .resize(avatarSize, avatarSize)
                .transform(new RoundedTransformation())
                .into(holder.ivBeaconAvatar, new Callback() {
                    @Override
                    public void onSuccess() {

//                        mBeaconList.get(position).setBeaconReady(true);
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.tvBeaconDistance.setText(String.format(" %.2f m", BeaconItem.getDistance()));
        holder.ll_beacon_item.setTag(holder);

    }


    public void appendBeacons(List<ParcelableIBeacon> BeaconList) {

        mBeaconList.addAll(BeaconList);
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



    @Override
    public void onClick(View view) {
        final int viewId = view.getId();
        if (viewId == R.id.ll_beacon_item) {
            if (onBeaconItemClickListener != null) {
                onBeaconItemClickListener.onBeaconClick(view, (Integer) view.getTag());
            }
        }
    }




    public void setOnBeaconItemClickListener(OnBeaconItemClickListener onBeaconItemClickListener) {
        this.onBeaconItemClickListener = onBeaconItemClickListener;
    }


    public static class CellBeaconViewHolder extends RecyclerView.ViewHolder {



        @InjectView(R.id.ivBeaconAvatar)
        ImageView ivBeaconAvatar;
        @InjectView(R.id.tvBeaconName)
        TextView tvBeaconName;
        @InjectView(R.id.tvBeaconDescription)
        TextView tvBeaconDescription;
        @InjectView(R.id.tvBeaconDistance)
        TextView tvBeaconDistance;
        @InjectView(R.id.ll_beacon_item)
        LinearLayout ll_beacon_item;


        public CellBeaconViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }


    public interface OnBeaconItemClickListener {

        public void onBeaconClick(View v, int position);


    }
}
