package com.lowworker;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "BLEScut");
        Entity beacon = schema.addEntity("Beacon");
        beacon.addIdProperty();
        beacon.addStringProperty("proximityUuid");
        beacon.addStringProperty("major");
        beacon.addStringProperty("minor");
        beacon.addStringProperty("longitude");
        beacon.addStringProperty("latitude");
        beacon.addStringProperty("uniqueId");
        beacon.addStringProperty("imageUrl");
        beacon.addStringProperty("name");
        beacon.addStringProperty("description");
        beacon.addStringProperty("content");
        beacon.addStringProperty("subscriptions_count");
        beacon.addBooleanProperty("isSubscripted").notNull();
        beacon.implementsSerializable();
        Entity card = schema.addEntity("Card");
        card.addIdProperty();
        card.addStringProperty("card_id");
        card.addStringProperty("beacon_id");
        card.addStringProperty("beacon_name");
        card.addStringProperty("beacon_avatar");
        card.addStringProperty("title");
        card.addStringProperty("slug");
        card.addStringProperty("cover_url");
        card.addStringProperty("content");
        card.addStringProperty("favorites_count");
        card.addStringProperty("comments_count");
        card.implementsSerializable();
        new DaoGenerator().generateAll(schema, args[0]);
    }
}
