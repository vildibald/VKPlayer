package sk.ics.upjs.VkSystemko.database.tables;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Viliam on 3.5.2014.
 */
public class PlaylistTable {
    public static final String TABLE_NAME = "playlist";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";


    private static final String DATABASE_CREATE =
            "create table "
                    + TABLE_NAME
                    + "("
                    + COLUMN_ID + " integer primary key autoincrement, "
                    + COLUMN_NAME + " text, "
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
