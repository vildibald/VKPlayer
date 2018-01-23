package sk.ics.upjs.VkSystemko.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Viliam on 3.5.2014.
 */
public class TracksNPlaylistsTable {
    public static final String TABLE_NAME = "tracknplaylist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TRACK_TITLE = "_track__name";
    public static final String COLUMN_PLAYLIST_NAME = "_playlist_name";


    private static final String DATABASE_CREATE =
            "create table "
                    + TABLE_NAME
                    + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_TRACK_TITLE + " text not null, "
                    + COLUMN_PLAYLIST_NAME + " text not null, "
                    + "foreign key(" + COLUMN_TRACK_TITLE + ") references "
                        + TrackTable.TABLE_NAME + "(" + TrackTable.COLUMN_TITLE + ")"
                    + "foreign key(" + COLUMN_PLAYLIST_NAME + ") references "
                        + PlaylistTable.TABLE_NAME + "(" + PlaylistTable.COLUMN_NAME + ")"
                    + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Log.w(TrackTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
