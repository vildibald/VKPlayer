package sk.ics.upjs.VkSystemko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import sk.ics.upjs.VkSystemko.entities.Track;

import java.util.List;

/**
 * Created by Viliam on 23.5.2014.
 */
//public class TrackListViewAdapter extends ArrayAdapter<Track> {
//
//   // private List<Track> items;
//
//    public TrackListViewAdapter(Context context, int resource) {
//        super(context, resource);
//
//    }
//
//    public TrackListViewAdapter(Context context, int resource, List<Track> items) {
//        super(context, resource, items);
//     //   this.items = items;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
//    }
//}

public class TrackListViewAdapter extends BaseAdapter {
    private static List<Track> tracks;
    private LayoutInflater layoutInflater;

    public TrackListViewAdapter(Context context, List<Track> tracks) {
        this.tracks = tracks;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public Track getItem(int position) {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.track_list_item,null);
            holder = new ViewHolder();
            holder.trackNameInListView = (TextView) convertView.findViewById(R.id.trackNameInListView);
            holder.trackArtistInListView = (TextView) convertView.findViewById(R.id.trackArtistInListView);
            holder.trackAlbumInListView = (TextView) convertView.findViewById(R.id.trackAlbumInListView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        Track track = tracks.get(position);
        holder.trackNameInListView.setText(track.getName());
        holder.trackArtistInListView.setText(track.getArtist());
        holder.trackAlbumInListView.setText(track.getAlbum());
        return convertView;
    }

    static class ViewHolder {

        TextView trackNameInListView;
        TextView trackArtistInListView;
        TextView trackAlbumInListView;
    }
}
