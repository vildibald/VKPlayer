package sk.ics.upjs.VkSystemko.entities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import android.os.AsyncTask;
import android.os.Parcel;
import android.util.Log;
import android.widget.ImageView;
import org.cmc.music.common.ID3ReadException;
import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.MusicMetadata;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.*;
import wseemann.media.FFmpegMediaMetadataRetriever;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

/**
 * Created by Viliam on 27.4.2014.
 */
public class Track implements Serializable {
    //private Long id;
    private String name;
    private String artist;
    private String album;
    private String path;


    public Track(String name, String artist, String album, String path) {
        this.album = album;
        this.path = path;
        this.name = name;
        this.artist = artist;
    }

    public Track(String path) {
        this.path = path;
        FFmpegMediaMetadataRetriever metadataRetriever = new FFmpegMediaMetadataRetriever();
        metadataRetriever.setDataSource(this.path);
        try {
            this.album = metadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
            this.name = metadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_TITLE);
            this.artist = metadataRetriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
        } catch (Exception e) {
            this.album = "";
            this.name = "";
            this.artist = "";
            Log.e("Track", "Failed to retrieve metadata from: " + path, e);
        }

    }

//    public Bitmap setTrackImage() {
//        try {
//
//            FFmpegMediaMetadataRetriever metadataRetriever = new FFmpegMediaMetadataRetriever();
//            metadataRetriever.setDataSource(this.path);
////            byte[] imageRawData = metadataRetriever.getEmbeddedPicture();
////            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageRawData, 0, imageRawData.length);
////            return imageBitmap;
//            metadataRetriever.
//        } catch (Exception e) {
//            Log.e("ID3", "Cannot find image in ID3 tag");
//            return null;
//        }
//
//    }

    public Bitmap getTrackImage() {
        try {
            FFmpegMediaMetadataRetriever metadataRetriever = new FFmpegMediaMetadataRetriever();
            metadataRetriever.setDataSource(this.path);
            byte[] imageRawData = metadataRetriever.getEmbeddedPicture();
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageRawData, 0, imageRawData.length);
            return imageBitmap;
        } catch (Exception e) {
            Log.e("ID3", "Cannot find image in ID3 tag");
            return null;
        }

    }
    // private int year;

//    public Long getId() {
//        return id;
//    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    // public int getYear() {
    //     return year;
    //  }

//    public void setYear(int year) {
//        this.year = year;
//    }

//    public static Track getTrackFromPath(String path){
//
//        AsyncTask prepMusicMetadataSet =new AsyncTask<String, Void, MusicMetadataSet>() {
//            @Override
//            protected MusicMetadataSet doInBackground(String... strings) {
//                MusicMetadataSet metadataSet=null;
//                try {
//                    metadataSet = new MyID3().read(new File(strings[0]));
//                } catch (IOException e) {
//                    Log.e("IOException","Unable to read file: " +strings[0]);
//                } catch (ID3ReadException e) {
//                    Log.e("ID3ReadException","Unable to read metadata: " +strings[0]);
//                }
//                return metadataSet;
//            }
//
//        };
//
//        prepMusicMetadataSet.execute(path);
//
//        MusicMetadataSet musicMetadataSet = null;
//        IMusicMetadata metadata = null;
//        try {
//            musicMetadataSet = (MusicMetadataSet)prepMusicMetadataSet.get();
//            metadata = musicMetadataSet.getSimplified();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//        String title = metadata.getArtist();
//        String artist = metadata.getArtist();
//        String album = metadata.getAlbum();
//
//        return new Track(path,title, artist,album);
//   }
}
