package com.android.tvchannelsadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.List;

public class ChannelsAdapter extends ArrayAdapter {

    private List<Channel> channels;
    private HashSet<Integer> selection = new HashSet<>();

    ChannelsAdapter(@NonNull Context context, int resource, List<Channel> channels) {
        super(context, resource);
        this.channels = channels;
    }

    @Override
    public int getCount() {
        return channels.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public int getItemViewType(int position) {
        if(isSelected(position))
            return 1;
        else
            return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Channel channel = channels.get(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if(isSelected(position)) {
                convertView = inflater.inflate(R.layout.channel_item_selected, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.channel_item, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.name = convertView.findViewById(R.id.channel_name);
            viewHolder.url = convertView.findViewById(R.id.channel_url);
            viewHolder.ratingBar = convertView.findViewById(R.id.channel_rating_mini);
            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.name.setText(channel.getName());
        viewHolder.url.setText(channel.getUrl());
        viewHolder.ratingBar.setRating(channel.getRating());

        return convertView;
    }
    private static class ViewHolder{
        TextView name;
        TextView url;
        RatingBar ratingBar;
    }
//    ----------------------------- методы для selection -------------------------------
    public boolean isSelected (int position) {
        return selection.contains(position);
    }

    public void toggleSelection (int position) {
        if(isSelected(position)) {
            selection.remove(position);
        } else {
            selection.add(position);
        }
//        Log.d("debug", "Selection HashSet: " + selection.toString());
        notifyDataSetChanged();
    }
    public boolean hasSelected () {
        return !selection.isEmpty();
    }
    public void clearSelection() {
        selection.clear();
    }
}
