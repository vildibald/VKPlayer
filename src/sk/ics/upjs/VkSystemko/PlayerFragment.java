package sk.ics.upjs.VkSystemko;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import sk.ics.upjs.VkSystemko.Utils.Utils;
import sk.ics.upjs.VkSystemko.entities.Track;

import java.io.IOException;

import static sk.ics.upjs.VkSystemko.constants.Constants.*;

/**
 * Created by Viliam on 2.5.2014.
 */
public class PlayerFragment extends Fragment {

    // public static final String ARG_SECTION_NUMBER = "section_number";
    public static final int SEEK_BAR_DELAY_MILLIS = 500;
    public static final int NOTIFICATION_REQUEST_CODE = 1611;
    private static final String PLAY_TIME = "playTime";

    private SeekBar trackSeekBar;
    private final Handler seekHandler = new Handler();
    private MediaPlayer mediaPlayer;
    private View fragmentView;
    private MainActivity mainActivity;
    private NotificationManager notificationManager;
    protected static final int NOTIFICATION_ID = 100;
    private int numMesssages = 0;
    private ImageView trackImageView;
    private boolean isPaused = false;
    private int playerPos=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("PlayerFragment", "Creating fragment view");
        fragmentView = inflater.inflate(R.layout.player_fragment, container, false);
        // setRetainInstance(true);
        if (savedInstanceState != null && savedInstanceState.containsKey(PLAY_TIME)) {
            mediaPlayer.seekTo(savedInstanceState.getInt(PLAY_TIME));
        }


        return fragmentView;
    }

//    @Override
//    public void onViewStateRestored(Bundle savedInstanceState) {
//
//        if (savedInstanceState != null && savedInstanceState.containsKey(PLAY_TIME)) {
//            mediaPlayer.seekTo(savedInstanceState.getInt(PLAY_TIME));
//        }
//        super.onViewStateRestored(savedInstanceState);
//    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(PLAYER_BACKGROUND_COLOR_STATE_CODE)) {
                int color = savedInstanceState.getInt(PLAYER_BACKGROUND_COLOR_STATE_CODE, Color.TRANSPARENT);
                RelativeLayout layout = (RelativeLayout) fragmentView.findViewById(R.id.playerLayout);
                layout.setBackgroundColor(color);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.i("PlayerFragment", "Saving instance state");


        super.onSaveInstanceState(outState);
//        if (mediaPlayer!=null &&mediaPlayer.isPlaying()) {
//            outState.putInt(PLAY_TIME, mediaPlayer.getCurrentPosition());
//        }

        RelativeLayout layout = (RelativeLayout) fragmentView.findViewById(R.id.playerLayout);
        int color = Utils.getRelativeLayoutColor(layout);
        outState.putInt(PLAYER_BACKGROUND_COLOR_STATE_CODE, color);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("PlayerFragment", "Creating fragment");
        //mediaPlayer = savedInstanceState==null? new MediaPlayer(): mediaPlayer;
        mediaPlayer=new MediaPlayer();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        setupButtons();
        setupTrackSeekBar();
        trackImageView =(ImageView) fragmentView.findViewById(R.id.trackImageView);
    }

    private void setupTrackSeekBar() {
        trackSeekBar = (SeekBar) fragmentView.findViewById(R.id.trackSeekBar);
        trackSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                seekChange(view);
                return false;
            }
        });
    }

    private void seekChange(View view) {
        if (mediaPlayer.isPlaying()) {
            SeekBar sb = (SeekBar) view;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu, menu);
//    }

    private void setupButtons() {
        ImageButton playTrackButton = (ImageButton) fragmentView.findViewById(R.id.playTrackButton);
        ImageButton pauseTrackButton = (ImageButton) fragmentView.findViewById(R.id.pauseTrackButton);
        ImageButton stopTrackButton = (ImageButton) fragmentView.findViewById(R.id.stopTrackButton);
        ImageButton previousTrackButton = (ImageButton) fragmentView.findViewById(R.id.previousTrackButton);
        ImageButton nextTrackButton = (ImageButton) fragmentView.findViewById(R.id.nextTrackButton);

        playTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Track selectedTrack = mainActivity.getSelectedTrack();

                if(selectedTrack==null) return;

                Bitmap image = selectedTrack.getTrackImage();
                if(image != null){
                    trackImageView.setImageBitmap(image);
                }

                if(isPaused){

                    mediaPlayerAndSeekBarResume();
                    return;
                }

                if (selectedTrack != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();

                        }
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(selectedTrack.getPath());
                    } catch (IOException e) {
                        Log.e("Playing tag: ", "MediaPlayer unable to load file", e);
                        return;
                    }
                    try {
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        Log.e("Playing tag: ", "MediaPlayer unable to prepare file", e);
                        return;
                    }
                    mediaPlayerAndSeekBarStart();
                    displayNotification(mainActivity.getString(R.string.playing), selectedTrack.getName());
                }
            }
        });
        pauseTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayerAndSeekBarPause();
                }
            }
        });

        stopTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayerAndSeekBarStop();
                    cancelNotification();
                    trackImageView.setImageResource(R.drawable.ic_launcher);

                }
            }
        });

        previousTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        nextTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    public void startPlayProgressUpdater() {
        trackSeekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable seekThread = new Runnable() {
                @Override
                public void run() {
                    startPlayProgressUpdater();
                }

            };
            seekHandler.postDelayed(seekThread, SEEK_BAR_DELAY_MILLIS);
        } else {
            mediaPlayerAndSeekBarPause();
        }
    }


    protected void displayNotification(String title, String text) {
        Log.i("Start", "notification");

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mainActivity);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(text);
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher);

        notificationBuilder.setNumber(++numMesssages);

        Intent notificiationIntent = new Intent(mainActivity, NotificationView.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mainActivity);
        stackBuilder.addNextIntent(notificiationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(NOTIFICATION_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(notificationPendingIntent);

        notificationManager = (NotificationManager) mainActivity.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    protected void cancelNotification() {
        Log.i("Cancel", "notification");
        notificationManager.cancel(NOTIFICATION_ID);
    }


    @Override
    public void onStop() {
        //seekHandler.remo
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public void mediaPlayerAndSeekBarResume() {

            trackSeekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.seekTo(playerPos);
            mediaPlayer.start();
            startPlayProgressUpdater();

    }

    public void mediaPlayerAndSeekBarStart() {
        if (!mediaPlayer.isPlaying()) {
            trackSeekBar.setMax(mediaPlayer.getDuration());
            mediaPlayer.start();
            startPlayProgressUpdater();
        }
    }

    public void mediaPlayerAndSeekBarStop() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            trackSeekBar.setProgress(0);
        }
        //seekHandler.
    }

    public void mediaPlayerAndSeekBarPause() {
        mediaPlayer.pause();
        this.playerPos = mediaPlayer.getCurrentPosition();
        trackSeekBar.setProgress(playerPos);
        isPaused=true;
    }

    @Override
    public void onResume() {
        Log.i("PlayerFragment", "Resuming fragment");
        super.onResume();
    }


}