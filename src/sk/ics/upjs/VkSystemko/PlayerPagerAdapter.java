package sk.ics.upjs.VkSystemko;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Viliam on 3.5.2014.
 */
public class PlayerPagerAdapter extends FragmentPagerAdapter {
    private static final int MAIN_ACTIVITY_FRAGMENT_COUNT =3;

    private FragmentManager fragmentManager;

    public PlayerPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragmentManager = fragmentManager;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new PlayerFragment();
            case 1:

                return new TrackListFragment();
            case 2:

                return new PlaylistsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return MAIN_ACTIVITY_FRAGMENT_COUNT;
    }


}
