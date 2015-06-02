package com.lowworker.android.views.custom_views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lowworker.android.R;
import com.lowworker.android.model.entities.User;
import com.lowworker.android.provider.ObscuredSharedPreferences;
import com.lowworker.android.utils.CircleTransformation;
import com.lowworker.android.views.adapters.GlobalMenuAdapter;
import com.squareup.picasso.Picasso;



/**
 * Created by Miroslaw Stanek on 30.01.15.
 */
public class GlobalMenuView extends ListView  implements View.OnClickListener{

    private OnHeaderClickListener   onHeaderClickListener;
    private GlobalMenuAdapter globalMenuAdapter;
    private ImageView ivUserProfilePhoto;
    private TextView  tvUserProfileName;
    private int avatarSize;
    private String profilePhoto;
    private  Context context;

    public GlobalMenuView(Context context) {
        super(context);
        this.context=context;
        init();
    }

    private void init() {
        setChoiceMode(CHOICE_MODE_SINGLE);
        setDivider(getResources().getDrawable(android.R.color.transparent));
        setDividerHeight(0);
        setBackgroundColor(Color.WHITE);

        setupHeader();
        setupAdapter();
    }

    private void setupAdapter() {
        globalMenuAdapter = new GlobalMenuAdapter(getContext());
        setAdapter(globalMenuAdapter);
    }

    private void setupHeader() {
        this.avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
        final SharedPreferences prefs = new ObscuredSharedPreferences(
                context, context.getSharedPreferences("UserProfile", Context.MODE_PRIVATE) );

        setHeaderDividersEnabled(true);
        View vHeader = LayoutInflater.from(getContext()).inflate(R.layout.view_global_menu_header, null);
        tvUserProfileName = (TextView) vHeader.findViewById(R.id.tvUserProfileName);
        ivUserProfilePhoto = (ImageView) vHeader.findViewById(R.id.ivUserProfilePhoto);
        if(prefs.getString("username",null)!=null){
            Log.d("GlobalMenuView","setText");
            tvUserProfileName.setText(prefs.getString("username",null));
            this.profilePhoto = prefs.getString("avatarUrl",null);
        }else{
            this.profilePhoto = getResources().getString(R.string.user_profile_photo);
        }
        Picasso.with(getContext())
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivUserProfilePhoto);
        addHeaderView(vHeader);
        vHeader.setOnClickListener(this);
    }

    public void setUpUserHeader(User user) {
        Log.d("GlobalMenuView","setUpUserHeader");
        profilePhoto = user.getAvatarUrl();
        tvUserProfileName.setText(user.getUsername());
        globalMenuAdapter.notifyDataSetChanged();
        invalidate();
    }


    @Override
    public void onClick(View v) {
        if (onHeaderClickListener  != null){
            onHeaderClickListener.onGlobalMenuHeaderClick(v);
        }


    }



    public interface OnHeaderClickListener  {
        public void onGlobalMenuHeaderClick(View v);

    }

    public void setOnHeaderClickListener(OnHeaderClickListener  onHeaderClickListener) {
        this.onHeaderClickListener  = onHeaderClickListener;
    }


}