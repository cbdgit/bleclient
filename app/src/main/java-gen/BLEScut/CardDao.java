package BLEScut;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import BLEScut.Card;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CARD.
*/
public class CardDao extends AbstractDao<Card, Long> {

    public static final String TABLENAME = "CARD";

    /**
     * Properties of entity Card.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Card_id = new Property(1, String.class, "card_id", false, "CARD_ID");
        public final static Property Beacon_id = new Property(2, String.class, "beacon_id", false, "BEACON_ID");
        public final static Property Beacon_name = new Property(3, String.class, "beacon_name", false, "BEACON_NAME");
        public final static Property Beacon_avatar = new Property(4, String.class, "beacon_avatar", false, "BEACON_AVATAR");
        public final static Property Title = new Property(5, String.class, "title", false, "TITLE");
        public final static Property Slug = new Property(6, String.class, "slug", false, "SLUG");
        public final static Property Cover_url = new Property(7, String.class, "cover_url", false, "COVER_URL");
        public final static Property Content = new Property(8, String.class, "content", false, "CONTENT");
        public final static Property Favorites_count = new Property(9, String.class, "favorites_count", false, "FAVORITES_COUNT");
        public final static Property Comments_count = new Property(10, String.class, "comments_count", false, "COMMENTS_COUNT");
    };


    public CardDao(DaoConfig config) {
        super(config);
    }
    
    public CardDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CARD' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'CARD_ID' TEXT," + // 1: card_id
                "'BEACON_ID' TEXT," + // 2: beacon_id
                "'BEACON_NAME' TEXT," + // 3: beacon_name
                "'BEACON_AVATAR' TEXT," + // 4: beacon_avatar
                "'TITLE' TEXT," + // 5: title
                "'SLUG' TEXT," + // 6: slug
                "'COVER_URL' TEXT," + // 7: cover_url
                "'CONTENT' TEXT," + // 8: content
                "'FAVORITES_COUNT' TEXT," + // 9: favorites_count
                "'COMMENTS_COUNT' TEXT);"); // 10: comments_count
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CARD'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Card entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String card_id = entity.getCard_id();
        if (card_id != null) {
            stmt.bindString(2, card_id);
        }
 
        String beacon_id = entity.getBeacon_id();
        if (beacon_id != null) {
            stmt.bindString(3, beacon_id);
        }
 
        String beacon_name = entity.getBeacon_name();
        if (beacon_name != null) {
            stmt.bindString(4, beacon_name);
        }
 
        String beacon_avatar = entity.getBeacon_avatar();
        if (beacon_avatar != null) {
            stmt.bindString(5, beacon_avatar);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(6, title);
        }
 
        String slug = entity.getSlug();
        if (slug != null) {
            stmt.bindString(7, slug);
        }
 
        String cover_url = entity.getCover_url();
        if (cover_url != null) {
            stmt.bindString(8, cover_url);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(9, content);
        }
 
        String favorites_count = entity.getFavorites_count();
        if (favorites_count != null) {
            stmt.bindString(10, favorites_count);
        }
 
        String comments_count = entity.getComments_count();
        if (comments_count != null) {
            stmt.bindString(11, comments_count);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Card readEntity(Cursor cursor, int offset) {
        Card entity = new Card( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // card_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // beacon_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // beacon_name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // beacon_avatar
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // title
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // slug
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // cover_url
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // content
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // favorites_count
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // comments_count
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Card entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCard_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setBeacon_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBeacon_name(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setBeacon_avatar(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setTitle(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSlug(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCover_url(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setContent(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFavorites_count(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setComments_count(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Card entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Card entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
