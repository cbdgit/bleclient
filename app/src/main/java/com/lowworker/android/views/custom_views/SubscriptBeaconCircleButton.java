package com.lowworker.android.views.custom_views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ViewAnimator;

import com.lowworker.android.R;


/**
 * Created by froger_mcs on 01.12.14.
 */
public class SubscriptBeaconCircleButton extends ViewAnimator {
    public static final int STATE_Subscript = 0;
    public static final int STATE_DONE = 1;

    private static final long RESET_STATE_DELAY_MILLIS = 2000;

    private int currentState = STATE_Subscript;


    public SubscriptBeaconCircleButton(Context context) {
        super(context);
        init();
    }

    public SubscriptBeaconCircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_subscript_beacon_circle_button, this, true);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        currentState = STATE_SEND;
//        super.setOnClickListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {

        super.onDetachedFromWindow();
    }

    public void refreshButtonStyle() {

        if (currentState == STATE_DONE) {
            currentState = STATE_Subscript;

//            setEnabled(false);
            setBackgroundResource(R.drawable.btn_subscript_circle_beacon);
            setInAnimation(getContext(), R.anim.slide_in_done);
            setOutAnimation(getContext(), R.anim.slide_out_send);

        } else if (currentState == STATE_Subscript) {
            currentState = STATE_DONE;

//            setEnabled(true);
            setBackgroundResource(R.drawable.btn_subscripte_beacon_circle_done);
            setInAnimation(getContext(), R.anim.slide_in_send);
            setOutAnimation(getContext(), R.anim.slide_out_done);
        }
        showNext();
    }

    public int getCurrentState() {
        return currentState;
    }
    public void setCurrentState(int state) {
        if (state == currentState) {
           return;
       }

        if (currentState == STATE_DONE) {
            this.currentState =  state;
            setBackgroundResource(R.drawable.btn_subscript_circle_beacon);
            setInAnimation(getContext(), R.anim.slide_in_done);
            setOutAnimation(getContext(), R.anim.slide_out_send);
        } else if (currentState == STATE_Subscript) {
            this.currentState =  state;
            setBackgroundResource(R.drawable.btn_subscripte_beacon_circle_done);
            setInAnimation(getContext(), R.anim.slide_in_send);
            setOutAnimation(getContext(), R.anim.slide_out_done);
        }
        showNext();


        }

}