package sk.ics.upjs.VkSystemko.entities.dao;

import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import sk.ics.upjs.VkSystemko.constants.Constants;
import sk.ics.upjs.VkSystemko.entities.Track;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import static sk.ics.upjs.VkSystemko.constants.Constants.FILE_STORAGE_PATH;

/**
 * Created by Viliam on 30.5.2014.
 */
public class InternalStorageEntitiesDao implements EntitiesDao {

    private InternalStorageEntitiesDao() {
    }

    private static class Holder{
        private static final InternalStorageEntitiesDao INSTANCE = new InternalStorageEntitiesDao();
    }

    public static InternalStorageEntitiesDao getInstance(){
        return Holder.INSTANCE;
    }

    private static final String PLAYLISTS_FILE = "playlists.json";
    private static final String TRACKS_FILE = "tracks.json";
    protected static final String TRACKS_N_PLAYLISTS_FILE = "relations.json";

    private Type getTrackListType() {
        return new TypeToken<ArrayList<Track>>() {
        }.getType();
    }

    private Type getPlaylistListType() {
        return new TypeToken<ArrayList<String>>() {
        }.getType();
    }

    private Type getTracksNPlaylistsMapType() {
        return new TypeToken<HashMap<String, Track>>() {
        }.getType();
    }

    @Override
    public void saveTracks(Context context, ArrayList<Track> tracks) {
        String jsonTracks = new Gson().toJson(tracks, getTrackListType());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(FILE_STORAGE_PATH+TRACKS_FILE,false);
            fos.write(jsonTracks.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("JSON write", "Cannot open file.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON write", "Cannot write file.");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON write", "Cannot close FileOutputStream.");
                }
            }
        }

    }

    @Override
    public ArrayList<Track> loadTracks(Context context) {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(TRACKS_FILE);
            int character;
            while((character = fis.read())!=-1){
                sb.append((char) character);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("JSON read", "Cannot open file.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON read", "Cannot read file.");
        } finally {

            if (fis != null) {
                try {
                    fis.close();
                    String jsonTracks = sb.toString();
                    return new Gson().fromJson(jsonTracks, getTrackListType());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON read", "Cannot close FileInputStream.");
                }
            }
            return null;
        }


    }

    @Override
    public void savePlaylists(Context context, ArrayList<String> playlists) {
        String jsonPlaylists = new Gson().toJson(playlists, getPlaylistListType());

        FileOutputStream fos = null;
        try {
            //fos = context.openFileOutput(PLAYLISTS_FILE, Context.MODE_PRIVATE);
            fos = new FileOutputStream(FILE_STORAGE_PATH+PLAYLISTS_FILE,false);

            fos.write(jsonPlaylists.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("JSON write", "Cannot open file.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON write", "Cannot write file.");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON write", "Cannot close FileOutputStream.");
                }
            }
        }
    }

    @Override
    public ArrayList<String> loadPlaylists(Context context) {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(PLAYLISTS_FILE);
            int character;
            while((character = fis.read())!=-1){
                sb.append((char) character);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("JSON read", "Cannot open file.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON read", "Cannot read file.");
        } finally {

            if (fis != null) {
                try {
                    fis.close();
                    String jsonPlaylists = sb.toString();
                    return new Gson().fromJson(jsonPlaylists, getPlaylistListType());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON read", "Cannot close FileInputStream.");
                }
            }
            return null;
        }
    }

    @Override
    public void saveTrackNPlaylists(Context context, HashMap<String, Track> tracksNPlaylists) {
        String jsonTracksNPlaylists = new Gson().toJson(tracksNPlaylists, getTracksNPlaylistsMapType());

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(FILE_STORAGE_PATH+TRACKS_N_PLAYLISTS_FILE,false);
            fos.write(jsonTracksNPlaylists.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("JSON write", "Cannot open file.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON write", "Cannot write file.");
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON write", "Cannot close FileOutputStream.");
                }
            }
        }
    }

    @Override
    public HashMap<String, Track> loadTracksNPlaylists(Context context) {
        FileInputStream fis = null;
        StringBuilder sb = new StringBuilder();
        try {
            fis = context.openFileInput(TRACKS_N_PLAYLISTS_FILE);
            int character;
            while((character = fis.read())!=-1){
                sb.append((char) character);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e("JSON read", "Cannot open file.");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON read", "Cannot read file.");
        } finally {

            if (fis != null) {
                try {
                    fis.close();
                    String jsonTracksNPlaylists = sb.toString();
                    return new Gson().fromJson(jsonTracksNPlaylists, getTracksNPlaylistsMapType());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("JSON read", "Cannot close FileInputStream.");
                }
            }
            return null;
        }
    }
}
