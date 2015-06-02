package com.lowworker.android.model.repository;

import android.content.Context;

import com.lowworker.android.BleScut;
import com.lowworker.android.model.event.CardListEvent;
import com.lowworker.common.utils.BusProvider;

import java.util.ArrayList;
import java.util.List;

import BLEScut.Card;
import BLEScut.CardDao;

/**
 * Created by lowworker on 2015/5/11.
 */
public class CardRepository {
    public static CardRepository INSTANCE;

    public static CardRepository getInstance() {

        if (INSTANCE == null)
            INSTANCE = new CardRepository();

        return INSTANCE;
    }

    public static void batchCards(Context context, ArrayList<Card> Cards) {
        getCardDao(context).insertInTx(Cards);
    }

    public static void clearCardes(Context context) {
        getCardDao(context).deleteAll();
    }

    public void insertOrUpdate(Context context, Card Card) {
        getCardDao(context).insertOrReplace(Card);
    }

    public  void deleteCardById(Context context, String cardId) {
        getCardDao(context).delete(getCardByCardId(context, cardId));
    }



    public  Card getCardByCardId(Context context, String cardId) {
        Card card = getCardDao(context).queryBuilder()
                .where(CardDao.Properties.Card_id.eq(cardId)).list().get(0);
        return card;

    }
    public static  boolean isCardSavedByCardId(Context context, String cardId) {
        if (getCardDao(context).queryBuilder()
                .where(CardDao.Properties.Card_id.eq(cardId)).list()!=null&&getCardDao(context).queryBuilder()
                .where(CardDao.Properties.Card_id.eq(cardId)).list().size()>0) {
            return true;
        } else {
            return false;
        }

    }
    public  void getAllCardes(Context context) {
        List<Card> cards = getCardDao(context).loadAll();

        BusProvider.getRestBusInstance().post(new CardListEvent(cards));

    }

    private static CardDao getCardDao(Context c) {
        return ((BleScut) c.getApplicationContext()).getDaoSession().getCardDao();
    }
}
