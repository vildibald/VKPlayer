package sk.ics.upjs.VkSystemko.database.dao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import sk.ics.upjs.VkSystemko.database.PlayerDatabaseOpenHelper;
import sk.ics.upjs.VkSystemko.database.tables.PlaylistTable;
import sk.ics.upjs.VkSystemko.database.tables.TrackTable;
import sk.ics.upjs.VkSystemko.database.tables.TracksNPlaylistsTable;

import sk.ics.upjs.VkSystemko.entities.Track;

import static android.content.ContentResolver.SCHEME_CONTENT;

/**
 * Created by Viliam on 3.5.2014.
 */
public class PlayerDatabaseContentProvider extends ContentProvider {

    private PlayerDatabaseOpenHelper databaseOpenHelper;

    private static final String AUTHORITY = "sk.ics.upjs.VkSystemko.databaseOpenHelper.dao";

    //private static final String PATH_TRACKS = "tracks";
    //private static final String PATH_PLAYLISTS = "playlists";
    //private static final String PATH_TRACKS_N_PLAYLISTS = "tracknplaylists";

    //public static final Uri CONTENT_URI_TRACKS = Uri.parse(
    //        "content://" + AUTHORITY + "/" + PATH_TRACKS
    //);
//    public static final Uri CONTENT_URI_PLAYLISTS = Uri.parse(
//            "content://" + AUTHORITY + "/" + PATH_PLAYLISTS
//    );
//    public static final Uri CONTENT_URI_TRACKS_N_PLAYLISTS = Uri.parse(
//            "content://" + AUTHORITY + "/" + PATH_TRACKS_N_PLAYLISTS
//    );

    public static final Uri PLAYLIST_CONTENT_URI = new Uri.Builder().scheme(SCHEME_CONTENT).authority(AUTHORITY).appendPath(PlaylistTable.TABLE_NAME).build();
    public static final Uri TRACK_CONTENT_URI =new Uri.Builder().scheme(SCHEME_CONTENT).authority(AUTHORITY).appendPath(TrackTable.TABLE_NAME).build();
    public static final Uri TRACK_N_PLAYLIST_CONTENT_URI =new Uri.Builder().scheme(SCHEME_CONTENT).authority(AUTHORITY).appendPath(TracksNPlaylistsTable.TABLE_NAME).build();

    private static final int TRACKS_CONTENT_CODE = 10;
    private static final int TRACKS_ID_CONTENT_CODE = 20;
    private static final int PLAYLISTS_CONTENT_CODE = 30;
    private static final int PLAYLISTS_ID_CONTENT_CODE = 40;
    private static final int TRACKS_N_PLAYLISTS_CONTENT_CODE = 50;
    private static final int TRACKS_N_PLAYLISTS_ID_CONTENT_CODE = 60;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, TrackTable.TABLE_NAME, TRACKS_CONTENT_CODE);
        URI_MATCHER.addURI(AUTHORITY, TrackTable.TABLE_NAME + "/#", TRACKS_ID_CONTENT_CODE);
        URI_MATCHER.addURI(AUTHORITY, PlaylistTable.TABLE_NAME, PLAYLISTS_CONTENT_CODE);
        URI_MATCHER.addURI(AUTHORITY, PlaylistTable.TABLE_NAME + "/#", PLAYLISTS_ID_CONTENT_CODE);
        URI_MATCHER.addURI(AUTHORITY, TracksNPlaylistsTable.TABLE_NAME, TRACKS_N_PLAYLISTS_CONTENT_CODE);
        URI_MATCHER.addURI(AUTHORITY, TracksNPlaylistsTable.TABLE_NAME + "/#", TRACKS_N_PLAYLISTS_ID_CONTENT_CODE);
    }

    @Override
    public boolean onCreate() {
        databaseOpenHelper = new PlayerDatabaseOpenHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();


        int uriType = URI_MATCHER.match(uri);
        switch (uriType) {
            case TRACKS_ID_CONTENT_CODE:
                queryBuilder.appendWhere(TrackTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            case TRACKS_CONTENT_CODE:
                queryBuilder.setTables(TrackTable.TABLE_NAME);
                break;
            case PLAYLISTS_ID_CONTENT_CODE:
                queryBuilder.appendWhere(PlaylistTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            case PLAYLISTS_CONTENT_CODE:
                queryBuilder.setTables(PlaylistTable.TABLE_NAME);
                break;
            case TRACKS_N_PLAYLISTS_ID_CONTENT_CODE:
                queryBuilder.appendWhere(TracksNPlaylistsTable.COLUMN_ID + "=" + uri.getLastPathSegment());
            case TRACKS_N_PLAYLISTS_CONTENT_CODE:
                queryBuilder.setTables(TracksNPlaylistsTable.TABLE_NAME);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = getDatabase();
        Cursor cursor =
                queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                //queryBuilder.


        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int uriType = URI_MATCHER.match(uri);


        switch (uriType) {
            case TRACKS_CONTENT_CODE:
                insertTrack(uri,contentValues);
                //if added successfully

                return uri;
            case PLAYLISTS_CONTENT_CODE:
                insertPlaylist(uri,contentValues);

                return uri;
            case TRACKS_N_PLAYLISTS_CONTENT_CODE:
                insertTrackIntoPlaylist(uri,contentValues);

                return uri;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

    }

    private void insertTrackIntoPlaylist(Uri uri,ContentValues contentValues) {
        getDatabase().insert(TracksNPlaylistsTable.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
    }

    private void insertPlaylist(Uri uri,ContentValues contentValues) {
       getDatabase().insert(PlaylistTable.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
    }

    private void insertTrack(Uri uri,ContentValues contentValues) {
       getDatabase().insert(TrackTable.TABLE_NAME, null, contentValues);
        getContext().getContentResolver().notifyChange(uri, null);
    }

    private SQLiteDatabase getDatabase() {
        return databaseOpenHelper.getWritableDatabase();
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = getDatabase();
        int rowsDeleted = 0;
        String id = null;
        switch (uriType) {
            case TRACKS_CONTENT_CODE:
                rowsDeleted = db.delete(TrackTable.TABLE_NAME,selection,selectionArgs);
                break;
            case TRACKS_ID_CONTENT_CODE:
                id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(
                            TrackTable.TABLE_NAME,
                            TrackTable.COLUMN_ID + "=" + id,
                            null);
                }else{
                    rowsDeleted = db.delete(
                            TrackTable.TABLE_NAME,
                            TrackTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            case PLAYLISTS_CONTENT_CODE:
                rowsDeleted = db.delete(PlaylistTable.TABLE_NAME,selection,selectionArgs);
                break;
            case PLAYLISTS_ID_CONTENT_CODE:
                id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(
                            PlaylistTable.TABLE_NAME,
                            PlaylistTable.COLUMN_ID + "=" + id,
                            null);
                }else{
                    rowsDeleted = db.delete(
                            PlaylistTable.TABLE_NAME,
                            PlaylistTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            case TRACKS_N_PLAYLISTS_CONTENT_CODE:
                rowsDeleted = db.delete(TracksNPlaylistsTable.TABLE_NAME,selection,selectionArgs);
                break;
            case TRACKS_N_PLAYLISTS_ID_CONTENT_CODE:
               id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    rowsDeleted = db.delete(
                            TracksNPlaylistsTable.TABLE_NAME,
                            TracksNPlaylistsTable.COLUMN_ID + "=" + id,
                            null);
                }else{
                    rowsDeleted = db.delete(
                            TracksNPlaylistsTable.TABLE_NAME,
                            TracksNPlaylistsTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int uriType = URI_MATCHER.match(uri);
        SQLiteDatabase db = getDatabase();
        int rowsUpdated = 0;
        String id = null;
        switch (uriType) {
            case TRACKS_CONTENT_CODE:
                rowsUpdated = db.update(TrackTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case TRACKS_ID_CONTENT_CODE:
                id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(
                            TrackTable.TABLE_NAME,
                            contentValues,
                            TrackTable.COLUMN_ID + "=" + id,
                            null);
                }else{
                    rowsUpdated = db.update(
                            TrackTable.TABLE_NAME,
                            contentValues,
                            TrackTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            case PLAYLISTS_CONTENT_CODE:
                rowsUpdated = db.update(PlaylistTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case PLAYLISTS_ID_CONTENT_CODE:
                id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(
                            PlaylistTable.TABLE_NAME,
                            contentValues,
                            PlaylistTable.COLUMN_ID + "=" + id,
                            null);
                }else{
                    rowsUpdated = db.update(
                            PlaylistTable.TABLE_NAME,
                            contentValues,
                            PlaylistTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            case TRACKS_N_PLAYLISTS_CONTENT_CODE:
                rowsUpdated = db.update(TracksNPlaylistsTable.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            case TRACKS_N_PLAYLISTS_ID_CONTENT_CODE:
                id = uri.getLastPathSegment();

                if (TextUtils.isEmpty(selection)){
                    rowsUpdated = db.update(
                            TracksNPlaylistsTable.TABLE_NAME,
                            contentValues,
                            TracksNPlaylistsTable.COLUMN_ID + "=" + id,
                            null);
                }else{
                    rowsUpdated = db.update(
                            TracksNPlaylistsTable.TABLE_NAME,
                            contentValues,
                            TracksNPlaylistsTable.COLUMN_ID + "=" + id + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return rowsUpdated;
    }
}
