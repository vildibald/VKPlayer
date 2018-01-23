package sk.ics.upjs.VkSystemko.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import sk.ics.upjs.VkSystemko.database.tables.PlaylistTable;
import sk.ics.upjs.VkSystemko.database.tables.TrackTable;
import sk.ics.upjs.VkSystemko.database.tables.TracksNPlaylistsTable;

/**
 * Created by Viliam on 3.5.2014.
 */
public class PlayerDatabaseOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "vksystemko.db";
    public static final int DATABASE_VERSION = 1;

    public PlayerDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        PlaylistTable.onCreate(database);
        TrackTable.onCreate(database);
        TracksNPlaylistsTable.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        PlaylistTable.onUpgrade(database, oldVersion, newVersion);
        TrackTable.onUpgrade(database, oldVersion, newVersion);
        TracksNPlaylistsTable.onUpgrade(database, oldVersion, newVersion);
    }
}
