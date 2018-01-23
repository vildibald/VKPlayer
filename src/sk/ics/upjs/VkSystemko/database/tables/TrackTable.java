package sk.ics.upjs.VkSystemko.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Viliam on 3.5.2014.
 */
public class TrackTable {

    public static final String TABLE_NAME = "track";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PATH = "path";
    public static final String COLUMN_TITLE = "name";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_ALBUM = "album";

    private static final String DATABASE_CREATE =
            "create table "
                    + TABLE_NAME
                    + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_PATH + " text not null, "
                    + COLUMN_TITLE + " text, "
                    + COLUMN_ARTIST + " text, "
                    + COLUMN_ALBUM + " text"
                    + ");";

    public static void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(TrackTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
