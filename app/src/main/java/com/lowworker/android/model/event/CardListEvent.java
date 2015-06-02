package com.lowworker.android.model.event;

import java.util.List;

import BLEScut.Card;

/**
 * Created by lowworker on 2015/5/14.
 */
public class CardListEvent {



        public final List<Card> cards;

        public CardListEvent(List<Card> cards) {
            this.cards = cards;
        }
    public List<Card> getData() {
        return cards;
    }
}
