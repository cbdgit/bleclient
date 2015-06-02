
package com.lowworker.android.model.entities;


import java.io.Serializable;
import java.util.List;

import BLEScut.Card;

public class CardDetailWrapper implements Serializable {
   private Card data;
    private List<Meta> meta;

    public List<Meta> getMeta() {
        return meta;
    }

    public void setMeta(List<Meta>  meta) {
        this.meta = meta;
    }
    public CardDetailWrapper(Card data) {
        this.data = data;
    }

    public Card getData() {
        return data;
    }
}
