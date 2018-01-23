package sk.ics.upjs.VkSystemko.entities.dao;

import android.content.Context;
import sk.ics.upjs.VkSystemko.entities.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Viliam on 30.5.2014.
 */
public interface EntitiesDao {

    public void saveTracks(Context context, ArrayList<Track> tracks);
    public ArrayList<Track> loadTracks(Context context);
    public void savePlaylists(Context context, ArrayList<String> playlists);
    public ArrayList<String> loadPlaylists(Context context);
    public void saveTrackNPlaylists(Context context, HashMap<String, Track> tracksNPlaylists);
    public HashMap<String, Track> loadTracksNPlaylists(Context context);
}
