package com.lowworker.android.mvp.views;


import com.lowworker.android.model.entities.Comment;

import java.util.List;

public interface CommentsView extends MVPView {

    void showComments(List<Comment> commentList);

    void showLoading();

    void hideLoading();

    void showError(String error);

    void hideError();

    void showLoadingLabel();

    void hideActionLabel();

    boolean isTheListEmpty();

    void appendComments(List<Comment> commentList);
}
