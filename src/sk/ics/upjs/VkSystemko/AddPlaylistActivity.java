package sk.ics.upjs.VkSystemko;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import sk.ics.upjs.VkSystemko.constants.Constants;

/**
 * Created by Viliam on 5.5.2014.
 */
public class AddPlaylistActivity extends Activity {
    private EditText newPlaylistNameEditText;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_playlist_activity);
        newPlaylistNameEditText = (EditText) findViewById(R.id.newPlaylistNameEditText);
    }

    public void onAddNewPlaylistButtonClick(View view) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.ADD_PLAYLIST_RESULT, newPlaylistNameEditText.getText().toString());
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public void onCancelNewPlaylistButtonClick(View view) {
        Intent returnIntent = new Intent();
        setResult(RESULT_CANCELED,returnIntent);
        finish();
    }

}