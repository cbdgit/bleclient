
package com.lowworker.android.model.entities;

import java.io.Serializable;
import java.util.List;

public class CommentsWrapper implements Serializable {


    private List<Comment> data;



    public CommentsWrapper(List<Comment> data) {

        this.data = data;
    }

    public List<Comment> getData() {
        return data;
    }

    public void setData(List<Comment> data) {
        this.data = data;
    }




}
