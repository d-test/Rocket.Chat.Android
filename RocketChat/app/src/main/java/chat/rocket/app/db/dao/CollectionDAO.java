package chat.rocket.app.db.dao;

import android.content.ContentValues;

import java.util.Date;

import chat.rocket.app.db.DBManager;
import chat.rocket.app.db.util.TableBuilder;

/**
 * Created by julio on 22/11/15.
 */
public class CollectionDAO {
    public static final String TABLE_NAME = "collections";
    // android internal value, to be used with cursors
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DOCUMENT_ID = "document_id";
    public static final String COLUMN_COLLECTION_NAME = "collection_name";
    public static final String COLUMN_FIELDS = "collection_value";
    public static final String COLUMN_UPDATED_AT = "updated_at";

    public static String createTableString() throws Exception {
        TableBuilder tb = new TableBuilder(TABLE_NAME);
        tb.setPrimaryKey(COLUMN_ID, TableBuilder.INTEGER, true);
        tb.addColumn(COLUMN_DOCUMENT_ID, TableBuilder.TEXT, true);
        tb.addColumn(COLUMN_COLLECTION_NAME, TableBuilder.TEXT, true);
        tb.addColumn(COLUMN_FIELDS, TableBuilder.TEXT, false);
        tb.addColumn(COLUMN_UPDATED_AT, TableBuilder.TEXT, false);

        tb.makeUnique(COLUMN_DOCUMENT_ID, TableBuilder.ON_CONFLICT_REPLACE);
        return tb.toString();
    }

    protected String collectionName;
    protected String documentID;
    protected String newValuesJson;

    public CollectionDAO(String collectionName, String documentID, String newValuesJson) {
        this.collectionName = collectionName;
        this.documentID = documentID;
        this.newValuesJson = newValuesJson;
    }

    private ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOCUMENT_ID, documentID);
        values.put(COLUMN_COLLECTION_NAME, collectionName);
        values.put(COLUMN_UPDATED_AT, new Date().getTime() / 1000);
        values.put(COLUMN_FIELDS, newValuesJson);
        return values;
    }

    public void insert() {
        DBManager.getInstance().insert(TABLE_NAME, toContentValues());
    }

    public void update() {
        DBManager.getInstance().update(TABLE_NAME,
                toContentValues(),
                COLUMN_COLLECTION_NAME + "=? AND " + COLUMN_DOCUMENT_ID + "=?",
                new String[]{collectionName, documentID});
    }

    public void remove() {
        DBManager.getInstance().delete(TABLE_NAME, COLUMN_COLLECTION_NAME + "=? AND " + COLUMN_DOCUMENT_ID + "=?",
                new String[]{collectionName, documentID});
    }
}
