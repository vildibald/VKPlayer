package sk.ics.upjs.VkSystemko;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.*;
import sk.ics.upjs.VkSystemko.Utils.Utils;
import sk.ics.upjs.VkSystemko.entities.Track;
import sk.ics.upjs.VkSystemko.entities.dao.InternalStorageEntitiesDao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static sk.ics.upjs.VkSystemko.constants.Constants.*;

/**
 * Created by Viliam on 2.5.2014.
 */
public class PlaylistsFragment extends Fragment {
    protected static final int ADD_PLAYLIST_ADD_CODE = 1422;
    protected static final int ADD_PLAYLIST_EDIT_CODE = 1423;
    private ListView playlistListView;
    private View fragmentView;
    private MainActivity mainActivity;
    private ArrayAdapter<String> playlistListViewAdapter;
    private Button addPlaylistButton;

    private final ArrayList<String> playlists = new ArrayList<String>();

    protected List<String> getPlaylists() {
        return playlists;
    }
    // public static final String ARG_SECTION_NUMBER = "section_number_1";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.playlists_fragment, container, false);
        playlistListView = (ListView) fragmentView.findViewById(R.id.playlistListView);

        return fragmentView;
    }

    private void setupPlaylistListViewAdapter() {
        playlistListViewAdapter = new ArrayAdapter<String>(mainActivity, R.layout.playlist_list_item, R.id.playlistNameInListView, playlists);
        playlistListView = (ListView) fragmentView.findViewById(R.id.playlistListView);
        playlistListView.setAdapter(playlistListViewAdapter);
        registerForContextMenu(playlistListView);
    }

    private void setupPlaylistListViewOnItemClickListener() {
        playlistListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mainActivity.setSelectedPlaylist(playlistListViewAdapter.getItem(i));

            }
        });
//        trackListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                return false;
//            }
//        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        setupButtons();
        setupPlaylistListViewAdapter();
        setupPlaylistListViewOnItemClickListener();

    }

    private void setupButtons() {
        addPlaylistButton = (Button) fragmentView.findViewById(R.id.addPlaylistButton);
        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAddPlaylistActivityToAdd();
            }
        });

    }


    private void startAddPlaylistActivityToAdd() {
        Intent intent = new Intent(mainActivity, AddPlaylistActivity.class);
        startActivityForResult(intent, ADD_PLAYLIST_ADD_CODE);
    }

    private void startAddPlaylistActivityToEdit() {
        Intent intent = new Intent(mainActivity, AddPlaylistActivity.class);
        startActivityForResult(intent, ADD_PLAYLIST_EDIT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_PLAYLIST_ADD_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        String newPlaylist = data.getStringExtra(ADD_PLAYLIST_RESULT);
                        insertPlaylistIntoListView(newPlaylist);
                        return;
                    case Activity.RESULT_CANCELED:

                        return;
                }
                return;
            case ADD_PLAYLIST_EDIT_CODE:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        String newPlaylistName = data.getStringExtra("playlist");
                        editPlaylistIntoListViewAndDatabase(newPlaylistName);
                        return;
                    case Activity.RESULT_CANCELED:

                        return;
                }
                return;
        }
    }

    private void editPlaylistIntoListViewAndDatabase(String newPlaylistName) {
    }

    private void insertPlaylistIntoListView(String playlist) {

        playlists.add(playlist);
        playlistListViewAdapter.notifyDataSetChanged();
        savePlaylistsToStorage();

    }

    private void editPlaylistInListViewAndDatabase(int index, String newPlaylistName) {
    }

    private void removePlaylistFromListView(int index) {
        playlists.remove(index);
        playlistListViewAdapter.notifyDataSetChanged();
        savePlaylistsToStorage();
    }


    //    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//
//    }

    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(contextMenu, view, menuInfo);
        if (view.getId() == R.id.playlistListView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            contextMenu.setHeaderTitle(playlists.get(info.position));
            // contextMenu.add(Menu.NONE, R.id.action_playlist_edit, Menu.NONE, R.string.track_or_playlist_edit);
            contextMenu.add(Menu.NONE, R.id.action_playlist_delete, Menu.NONE, R.string.track_or_playlist_delete);

        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_playlist_delete:
                showDeleteAlertDialog(info.position);
                //removePlaylistFromListView(info.position);
                return true;
            case R.id.action_playlist_edit:
                //editPlaylistInListViewAndDatabase(info.position);
        }
        return super.onContextItemSelected(item);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.i("PlaylistListFragment", "Restoring fragment state");
        //if(savedInstanceState!=null&& savedInstanceState.getBoolean("Tracks")) {
        // ArrayList<String> restoredPlaylists = InternalStorageEntitiesDao.getInstance().loadPlaylists(mainActivity);
        loadFromStorage();

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(PLAYLIST_BACKGROUND_COLOR_STATE_CODE)) {
                int color = savedInstanceState.getInt(PLAYLIST_BACKGROUND_COLOR_STATE_CODE);
                RelativeLayout layout = (RelativeLayout) fragmentView.findViewById(R.id.playlistsLayout);
                layout.setBackgroundColor(color);
            }
        }

        //}

    }

    private void loadFromStorage() {

        InternalStorageEntitiesDao storageDao = InternalStorageEntitiesDao.getInstance();
        ArrayList<String> loadedPlaylists = storageDao.loadPlaylists(mainActivity);

        if (loadedPlaylists != null) {
            playlists.clear();
            playlists.addAll(loadedPlaylists);
        }

    }

    private void insertPlaylists(ArrayList<String> playlists) {
        playlists.addAll(playlists);
        playlistListViewAdapter.notifyDataSetChanged();
        savePlaylistsToStorage();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        savePlaylistsToStorage();
        RelativeLayout layout = (RelativeLayout) fragmentView.findViewById(R.id.playlistsLayout);
        int color = Utils.getRelativeLayoutColor(layout);
        outState.putInt(PLAYLIST_BACKGROUND_COLOR_STATE_CODE, color);
        super.onSaveInstanceState(outState);
    }

    protected void savePlaylistsToStorage() {
        //if (playlists.size() != 0) {
        InternalStorageEntitiesDao.getInstance().savePlaylists(mainActivity, playlists);
        Log.i("TrackListFragment", "Saving fragment state");

        //}

    }


    @Override
    public void onDestroy() {
        //    savePlaylistsToStorage();
        super.onDestroy();
    }

    private void showDeleteAlertDialog(final int position) {
        DialogFragment dialog = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mainActivity);
                dialogBuilder.setMessage(mainActivity.getString(R.string.track_remove))
                        .setPositiveButton(mainActivity.getString(R.string.answer_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                removePlaylistFromListView(position);
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
        };
        dialog.show(getFragmentManager(),"dialog");
    }


}