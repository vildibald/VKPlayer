package sk.ics.upjs.VkSystemko;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Viliam on 30.5.2014.
 */
public class SeekFragment extends Fragment {
    private static final String TAG = SeekFragment.class.getSimpleName();
    private static final boolean DEBUG = true;

    private TaskCallbacks mCallbacks;
    private SeekTask mTask;
    private boolean mRunning;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    private class SeekTask {
    }

    private class TaskCallbacks {
    }
}
