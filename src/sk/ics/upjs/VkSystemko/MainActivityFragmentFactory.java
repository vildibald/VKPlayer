package sk.ics.upjs.VkSystemko;

import android.support.v4.app.Fragment;

/**
 * Created by Viliam on 3.5.2014.
 */
public class MainActivityFragmentFactory {
    public static Fragment getFragment(int which){
        switch (which){
            case 0:
               return new PlayerFragment();

            case 1:
                return new PlaylistsFragment();

            case 2:
                return new TrackListFragment();

            default:
                return null;
        }
    }
}
