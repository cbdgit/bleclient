package com.lowworker.android.mvp.presenters;

import android.util.Log;

import com.lowworker.android.domain.GetCommentsUsecaseController;
import com.lowworker.android.model.entities.CommentsWrapper;
import com.lowworker.android.model.rest.RestBLESource;
import com.lowworker.android.mvp.views.CommentsView;
import com.lowworker.common.utils.BusProvider;
import com.squareup.otto.Subscribe;


public class CommentsPresenter extends Presenter {

    private final CommentsView mCommentsView;
    private GetCommentsUsecaseController mGetComments;


    private boolean isLoading = false;
    private boolean mRegistered;


    public CommentsPresenter(CommentsView commentsView,String cardId) {

        mCommentsView = commentsView;

        mGetComments = new GetCommentsUsecaseController(
            RestBLESource.getInstance(), BusProvider.getUIBusInstance(),cardId);


    }

    public CommentsPresenter(CommentsView CommentsView, CommentsWrapper commentsWrapper,String cardId) {

        mCommentsView = CommentsView;

        mGetComments = new GetCommentsUsecaseController(
                RestBLESource.getInstance(), BusProvider.getUIBusInstance(),cardId);

        onCommentsReceived(commentsWrapper);
    }

    @Subscribe
    public void onCommentsReceived(CommentsWrapper commentsWrapper) {
        Log.d("Comments Presenter","onBeaconReceived");
        if (mCommentsView.isTheListEmpty()) {

            mCommentsView.hideLoading();

            mCommentsView.showComments(commentsWrapper.getData());

        } else {

            mCommentsView.hideActionLabel();
          mCommentsView.appendComments(commentsWrapper.getData());
        }

        isLoading = false;
    }



    public void onEndListReached () {

        mGetComments.execute();
        mCommentsView.showLoadingLabel ();
        isLoading = true;
    }

    @Override
    public void start() {

        if (mCommentsView.isTheListEmpty()) {

            BusProvider.getUIBusInstance().register(this);
            mRegistered = true;
            Log.d("Comments Presenter","start");
            mCommentsView.showLoading();
            mGetComments.execute();
        }
    }

    @Override
    public void stop() {
        Log.d("Comments Presenter","stop");
        mGetComments.unRegister();
    }

    public boolean isLoading() {

        return isLoading;
    }

    public void setLoading(boolean isLoading) {

        this.isLoading = isLoading;
    }
}