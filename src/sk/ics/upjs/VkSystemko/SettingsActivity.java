package sk.ics.upjs.VkSystemko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import sk.ics.upjs.VkSystemko.constants.Constants;
import yuku.ambilwarna.widget.AmbilWarnaPreference;

/**
 * Created by Viliam on 27.5.2014.
 */
public class SettingsActivity extends PreferenceActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    protected void onDestroy() {


        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        AmbilWarnaPreference colorPicker = (AmbilWarnaPreference) findPreference("colorPicker");
        int color = colorPicker.getValue();
        Intent returnIntent = new Intent();
        if(color!= Constants.DEFAULT_INTEGER_VALUE) {

            returnIntent.putExtra(Constants.COLOR_CHOOSE_RESULT, color);
            setResult(RESULT_OK, returnIntent);
        }
        else{
            setResult(RESULT_CANCELED,returnIntent);
        }
        finish();
    }
}