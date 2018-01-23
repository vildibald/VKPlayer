package sk.ics.upjs.VkSystemko;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import sk.ics.upjs.VkSystemko.constants.Constants;
import sk.ics.upjs.VkSystemko.database.PlayerDatabaseOpenHelper;
import sk.ics.upjs.VkSystemko.database.dao.PlayerDatabaseContentProvider;
import sk.ics.upjs.VkSystemko.database.tables.PlaylistTable;
import sk.ics.upjs.VkSystemko.database.tables.TrackTable;
import sk.ics.upjs.VkSystemko.entities.Track;
import sk.ics.upjs.VkSystemko.entities.dao.InternalStorageEntitiesDao;

import java.util.HashMap;


public class MainActivity extends ActionBarActivity implements TabListener, LoaderManager.LoaderCallbacks<Cursor> {


    public static final String STATE_SEEK_BAR = "seek_bar_state";
    public static final String TRACKLIST_LIST_FRAGMENT_KEY = "tlf";
    public static final String PLAYLIST_LIST_FRAGMENT_KEY = "pllf";
    public static final String PLAYER_LIST_FRAGMENT_KEY = "plf";

    private PlayerPagerAdapter playerPagerAdapter;

    private ViewPager viewPager;
    private ActionBar actionBar;


    private final HashMap<String, Track> tracksNPlaylists = new HashMap<String, Track>();
    private static final int SETTINGS_REQUEST_CODE = 1511;

    protected HashMap<String, Track> getTracksNPlaylists() {
        return tracksNPlaylists;
    }


    private String selectedPlaylist;
    private Track selectedTrack = null;

//    private Runnable seekBarThread = new Runnable() {
//        @Override
//        public void run() {
//            seekUpdation();
//        }
//    };

    protected Track getSelectedTrack() {
        return selectedTrack;
    }

    protected void setSelectedTrack(Track selectedTrack) {
        this.selectedTrack = selectedTrack;
    }

    protected void setSelectedPlaylist(String selectedPlaylist) {
        this.selectedPlaylist = selectedPlaylist;

    }

    public String getSelectedPlaylist() {
        return selectedPlaylist;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PlayerDatabaseOpenHelper databaseOpenHelper = new PlayerDatabaseOpenHelper(this);
        setContentView(R.layout.main_activity);


        actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setupViewPagerNAdapter();

        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        setupActionBarLayout();

        setupActionBarForTabs(actionBar);

        if (savedInstanceState != null) {

        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.i("MainActivity", "Restoring activity state");
        HashMap<String, Track> restoredTracksNPlaylists = InternalStorageEntitiesDao.getInstance().loadTracksNPlaylists(this);
        restoredTracksNPlaylists.putAll(restoredTracksNPlaylists);


        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if (tracksNPlaylists.size() != 0) {
            InternalStorageEntitiesDao.getInstance().saveTrackNPlaylists(this, tracksNPlaylists);
            Log.i("MainActivity", "Saving activity state");
        }
        super.onSaveInstanceState(outState);


//        FragmentManager fragmentManager = getSupportFragmentManager();
//        TrackListFragment trackListFragment = (TrackListFragment)fragmentManager.findFragmentById(R.layout.track_list_fragment);
//        PlaylistsFragment playlistsFragment = (PlaylistsFragment)fragmentManager.findFragmentById(R.layout.playlists_fragment);
//        PlayerFragment playerFragment = (PlayerFragment) fragmentManager.findFragmentById(R.layout.playlists_fragment);
//
//        fragmentManager.putFragment(outState, TRACKLIST_LIST_FRAGMENT_KEY, trackListFragment);
//        fragmentManager.putFragment(outState, PLAYLIST_LIST_FRAGMENT_KEY, playlistsFragment);
//        fragmentManager.putFragment(outState, PLAYER_LIST_FRAGMENT_KEY, playerFragment);
    }

    private void setupActionBarLayout() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(v);
    }


    private void setupViewPagerNAdapter() {
        playerPagerAdapter = new PlayerPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(playerPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
    }


    private void setupActionBarForTabs(ActionBar actionBar) {

        Tab tab = actionBar
                .newTab()
                .setText(getString(R.string.player))
                .setTabListener(this)
                .setTag(PlayerFragment.class.getName());
        actionBar.addTab(tab);

        tab = actionBar
                .newTab()
                .setText(getString(R.string.all_tracks))
                .setTabListener(this)
                .setTag(TrackListFragment.class.getName());
        actionBar.addTab(tab);

        tab = actionBar
                .newTab()
                .setText(getString(R.string.playlists))
                .setTabListener(this)
                .setTag(PlaylistsFragment.class.getName());
        actionBar.addTab(tab);


    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    // @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        outState.putInt(STATE_SEEK_BAR,);
//
//        super.onSaveInstanceState(outState);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//// Inflate the menu; this adds items to the action bar if it is present.
//        //super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                startPreferenceActivity();
//                return true;
//
//            default:
//                startPreferenceActivity();
//                return true;
//                //return super.onOptionsItemSelected(item);
//        }
//    }

    private void startPreferenceActivity() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {

        viewPager.setCurrentItem(tab.getPosition());
    }

    //    private void startFileChooserActivity() {
//
//        Intent target = FileUtils.createGetContentIntent();
//
//        Intent intent = Intent.createChooser(
//                target, getString(R.string.filechooser_title));
//        try {
//            startActivityForResult(intent, FILECHOOSER_REQUEST_CODE);
//        } catch (ActivityNotFoundException e) {
//
//        }
//    }
//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case SETTINGS_REQUEST_CODE:

                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        int newBackgroundColor = data.getIntExtra(Constants.COLOR_CHOOSE_RESULT, Constants.DEFAULT_INTEGER_VALUE);
                        RelativeLayout layout = (RelativeLayout) findViewById(R.id.playerLayout);
                        layout.setBackgroundColor(newBackgroundColor);
//                        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.playlistsLayout);
//                        layout1.setBackgroundColor(newBackgroundColor);
//                        RelativeLayout layout2 = (RelativeLayout) findViewById(R.id.trackListLayout);
//                        layout2.setBackgroundColor(newBackgroundColor);

                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private String trackSuccessfulyAddedReport(Track track) {
        StringBuilder sb = new StringBuilder();
        sb.append("Track successfuly added, ");
        sb.append(track.getPath());
        sb.append(", ");
        sb.append(track.getName());
        sb.append(", ");
        sb.append(track.getArtist());
        sb.append(", ");
        sb.append(track.getAlbum());
        sb.append(", ");
        return sb.toString();
    }

    private void insertTrackIntoDatabase(Track track) {
        ContentValues values = new ContentValues();
        values.put(TrackTable.COLUMN_TITLE, track.getName());
        values.put(TrackTable.COLUMN_ALBUM, track.getAlbum());
        values.put(TrackTable.COLUMN_ALBUM, track.getAlbum());
        values.put(TrackTable.COLUMN_PATH, track.getPath());

        getContentResolver().insert(PlayerDatabaseContentProvider.TRACK_CONTENT_URI, values);

    }

    private void insertPlaylistIntoDatabase(String playlistName) {
        ContentValues values = new ContentValues();
        values.put(PlaylistTable.COLUMN_NAME, playlistName);
        getContentResolver().insert(PlayerDatabaseContentProvider.PLAYLIST_CONTENT_URI, values);
    }

    private void insertTrackNPlaylistIntoDatabase(Track track, String playlistName) {
        ContentValues values = new ContentValues();
        values.put(TrackTable.COLUMN_TITLE, playlistName);
        values.put(PlaylistTable.COLUMN_NAME, playlistName);

        getContentResolver().insert(PlayerDatabaseContentProvider.TRACK_N_PLAYLIST_CONTENT_URI, values);
    }

    //private Uri getInsertTrackUri()

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        String tag = (String) tab.getTag();
        Fragment selectedFragment = getSupportFragmentManager().findFragmentByTag(tag);
        if (selectedFragment != null) {
            ft.detach(selectedFragment);
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        PlaylistsFragment pf = (PlaylistsFragment) getSupportFragmentManager().findFragmentById(R.layout.playlists_fragment);
//        pf.savePlaylistsToStorage();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int position, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }


    public void onSettingsClick(View view) {
        startPreferenceActivity();

    }




}

