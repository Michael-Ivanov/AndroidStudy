package com.android.tvchannelsadapter;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelsHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public TextView name;
    public TextView url;
    public RatingBar rating;

    private ChannelsAdapter adapter;

    OnChannelClick onChannelClick;
    OnChannelLongClick onChannelLongClick;

    public ChannelsHolder(@NonNull View itemView, ChannelsAdapter adapter,
                          OnChannelClick onChannelClick, OnChannelLongClick onChannelLongClick) {
        super(itemView);
        this.adapter = adapter;
        this.onChannelClick = onChannelClick;
        this.onChannelLongClick = onChannelLongClick;

        name = itemView.findViewById(R.id.channel_name);
        url = itemView.findViewById(R.id.channel_url);
        rating = itemView.findViewById(R.id.channel_rating_mini);

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onChannelClick.onChannelClick(getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        onChannelLongClick.onChannelLongClick(getAdapterPosition());
        return true;
    }

    public interface OnChannelClick {
        void onChannelClick(int position);
    }

    public interface OnChannelLongClick {
        void onChannelLongClick(int position);
    }
}
