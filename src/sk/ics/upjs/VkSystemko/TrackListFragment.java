package sk.ics.upjs.VkSystemko;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.ipaulpro.afilechooser.utils.FileUtils;
import sk.ics.upjs.VkSystemko.Utils.Utils;
import sk.ics.upjs.VkSystemko.entities.Track;
import sk.ics.upjs.VkSystemko.entities.dao.InternalStorageEntitiesDao;

import java.util.*;

import static sk.ics.upjs.VkSystemko.constants.Constants.*;


/**
 * Created by Viliam on 2.5.2014.
 */
public class TrackListFragment extends Fragment {
    protected static final int FILECHOOSER_REQUEST_CODE = 1421;
    private static final String FILECHOOSER_TAG = "VK SYSTEMKO FileChooser activity ";

    // public static final String ARG_SECTION_NUMBER = "section_number_2";
    private ListView trackListView;
    private View fragmentView;
    private MainActivity mainActivity;
    private TrackListViewAdapter trackListViewAdapter;
    private Button addTrackButton;
    private Button addTrackToPlaylistButton;
    private ToggleButton allOrPlaylistTracksButton;


    private final ArrayList<Track> allTracks = new ArrayList<Track>();
    private final ArrayList<Track> playlistTracks = new ArrayList<Track>();
    private HashMap<String, Track> allTracksNPlaylists;

    protected void onNewPlaylistSelected() {
        refreshTracksListView();
    }

    private void refreshTracksListView() {
        playlistTracks.clear();

        if (allOrPlaylistTracksButton.isChecked()) {
            playlistTracks.addAll(allTracks);
        } else {
            loadTracksFromPlaylist();
        }
        trackListViewAdapter.notifyDataSetChanged();
    }

    private void loadTracksFromPlaylist() {
        String playlist = mainActivity.getSelectedPlaylist();
        for (Map.Entry<String, Track> entry : allTracksNPlaylists.entrySet()) {
            if (entry.getKey().equals(playlist)) {
                this.playlistTracks.add(entry.getValue());
            }
        }

    }

    protected List<Track> getAllTracks() {
        return allTracks;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.track_list_fragment, container, false);
        // setupTrackListViewAdapter(view);

        return fragmentView;
    }

    private void setupTrackListViewAdapter() {

        trackListViewAdapter = new TrackListViewAdapter(mainActivity, playlistTracks);
        trackListView = (ListView) fragmentView.findViewById(R.id.trackListView);
        trackListView.setAdapter(trackListViewAdapter);
        registerForContextMenu(trackListView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        setupButtons();

        setupTrackListViewAdapter();
        setupTrackListViewOnItemClickListener();
        allTracksNPlaylists = mainActivity.getTracksNPlaylists();
//        if(savedInstanceState!=null&& savedInstanceState.getBoolean("Tracks")){
//            ArrayList<Track> restoredTracks = StateToInternalStorage.getInstance().loadTracks(mainActivity);
//            allTracks.addAll(restoredTracks);
//            trackListViewAdapter.notifyDataSetChanged();
//        }


    }

    private void loadFromStorage() {

        InternalStorageEntitiesDao storageDao = InternalStorageEntitiesDao.getInstance();
        ArrayList<Track> loadedTracks = storageDao.loadTracks(mainActivity);
        HashMap<String, Track> loadedTNP = storageDao.loadTracksNPlaylists(mainActivity);

        if(loadedTracks!=null){
            allTracks.clear();
            allTracks.addAll(loadedTracks);
        }
        if(loadedTNP!=null){
            allTracksNPlaylists.clear();
            allTracksNPlaylists.putAll(loadedTNP);
        }
    }

    private void saveToStorage() {
        InternalStorageEntitiesDao storageDao = InternalStorageEntitiesDao.getInstance();

        if (allTracks != null && allTracks.size() > 0) {
            storageDao.saveTracks(mainActivity, allTracks);
        }
        if (allTracksNPlaylists != null && allTracksNPlaylists.size() > 0) {
            storageDao.saveTrackNPlaylists(mainActivity, allTracksNPlaylists);
        }
    }

    private void setupButtons() {
        addTrackButton = (Button) fragmentView.findViewById(R.id.addTrackButton);
        addTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFileChooserActivity();
            }
        });
        allOrPlaylistTracksButton = (ToggleButton) fragmentView.findViewById(R.id.allOrPlaylistTracksButton);
        allOrPlaylistTracksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshTracksListView();
            }
        });
        addTrackToPlaylistButton = (Button) fragmentView.findViewById(R.id.addTrackToPlaylistButton);
        addTrackToPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allOrPlaylistTracksButton.isChecked()) {
                    insertTrackIntoPlaylist(mainActivity.getSelectedPlaylist(), mainActivity.getSelectedTrack());
                    trackListViewAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        Log.i("TrackListFragment", "Restoring fragment state");
        //if(savedInstanceState!=null&& savedInstanceState.getBoolean("Tracks")) {
//        ArrayList<Track> restoredTracks = InternalStorageEntitiesDao.getInstance().loadTracks(mainActivity);
//        if (restoredTracks != null) {
//            allTracks.clear();
//            insertTracks(restoredTracks);
//            mainActivity.getTracksNPlaylists().clear();
//
//        }



        loadFromStorage();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(TRACKLIST_BACKGROUND_COLOR_STATE_CODE)) {
                int color = savedInstanceState.getInt(TRACKLIST_BACKGROUND_COLOR_STATE_CODE, Color.TRANSPARENT);
                RelativeLayout layout = (RelativeLayout) fragmentView.findViewById(R.id.trackListLayout);
                layout.setBackgroundColor(color);
            }
        }

        //}
        super.onViewStateRestored(savedInstanceState);
    }



    private void insertTrackIntoPlaylist(String playlist, Track track) {
        if (playlist != null || track != null) {
            allTracksNPlaylists.put(playlist, track);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveToStorage();
        Log.i("TrackListFragment", "Saving fragment state");
        //outState.putBoolean("Tracks", true);

        RelativeLayout layout = (RelativeLayout) fragmentView.findViewById(R.id.trackListLayout);
        int color = Utils.getRelativeLayoutColor(layout);
        outState.putInt(TRACKLIST_BACKGROUND_COLOR_STATE_CODE, color);

        super.onSaveInstanceState(outState);
    }

    private void startFileChooserActivity() {

        Intent target = FileUtils.createGetContentIntent();

        Intent intent = Intent.createChooser(
                target, getString(R.string.filechooser_title));
        try {
            startActivityForResult(intent, FILECHOOSER_REQUEST_CODE);
        } catch (ActivityNotFoundException e) {

        }
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("file/*");
//        startActivityForResult(intent,FILECHOOSER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILECHOOSER_REQUEST_CODE:
                // If the file selection was successful
                if (resultCode == MainActivity.RESULT_OK) {
                    if (data != null) {
                        // Get the URI of the selected file
                        final Uri uri = data.getData();
                        Log.i(FILECHOOSER_TAG, "Uri = " + uri.toString());
                        try {
                            // Get the file path from the URI
                            String filePath = FileUtils.getPath(getActivity(), uri);
                            // insert into database
                            insertTrack(filePath);

                            Toast.makeText(getActivity(),
                                    "File Selected: " + filePath, Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.e("FileSelectorTestActivity", "File select error", e);
                        }
                    }
                }
                break;
        }
        // super.onActivityResult(requestCode, resultCode, data);
    }


    private void insertTrack(String filePath) {
        Track track = new Track(filePath);
        insertTrack(track);

    }

    private void insertTrack(Track track) {
        allTracks.add(track);
        if (allOrPlaylistTracksButton.isChecked()) {
            trackListViewAdapter.notifyDataSetChanged();
        }
    }

    private void removeTrackFromListView(int index) {
//        if (allOrPlaylistTracksButton.isChecked()) {
//            allTracks.remove(index);
//            //playlistTracks.remove(index);
//        } else {
            //Track track  = playlistTracks.get(index);
            playlistTracks.remove(index);
            //allTracks.remove(track);
        //}

        trackListViewAdapter.notifyDataSetChanged();
    }

    private void setupTrackListViewOnItemClickListener() {
        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainActivity.setSelectedTrack(trackListViewAdapter.getItem(i));
            }
        });
//        trackListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                return false;
//            }
//        });
    }
    //    @Override
//    public void onListItemClick(ListView listView, View view, int position, long id) {
//
//    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        if (view.getId() == R.id.trackListView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            contextMenu.setHeaderTitle(allTracks.get(info.position).getName());
            contextMenu.add(Menu.NONE, R.id.action_tracklist_delete, Menu.NONE, R.string.track_or_playlist_delete);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_tracklist_delete:
                removeTrackFromListView(info.position);

                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void showDeleteAlertDialog(final int position) {
        new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
                dialogBuilder.setMessage(mainActivity.getString(R.string.track_remove))
                        .setPositiveButton(mainActivity.getString(R.string.answer_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removeTrackFromListView(position);
                            }
                        })
                        .setNegativeButton(mainActivity.getString(R.string.answer_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //User cancelled the dialog
                            }
                        });
                return dialogBuilder.create();
            }
        }.show(getFragmentManager(), "Track delete");
    }


}