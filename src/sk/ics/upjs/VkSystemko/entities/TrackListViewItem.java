package sk.ics.upjs.VkSystemko.entities;

/**
 * Created by Viliam on 23.5.2014.
 */
public class TrackListViewItem {
    private String name;
    private String artist;
    private String album;

    public TrackListViewItem(String album, String artist, String name) {
        this.album = album;
        this.artist = artist;
        this.name = name;
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
}
