package com.android.tvchannelsadapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChannelsAdapter extends RecyclerView.Adapter<ChannelsHolder> {

    public static final String [] FIELDS = {"_id", DbOpenHelper.COLUMN_NAME,
        DbOpenHelper.COLUMN_URL, DbOpenHelper.COLUMN_RATING};
    private HashSet<Integer> selection = new HashSet<>();
    SQLiteDatabase database;
    private Context context;
    private Cursor cursor;

    private ChannelsHolder.OnChannelClick mOnChannelClick;
    private ChannelsHolder.OnChannelLongClick mOnChannelLongClick;


    public ChannelsAdapter(Context context, Cursor cursor,
                           ChannelsHolder.OnChannelClick onChannelClick,
                           ChannelsHolder.OnChannelLongClick onChannelLongClick) {
        this.context = context;
        this.cursor = cursor;
        this.mOnChannelClick = onChannelClick;
        this.mOnChannelLongClick = onChannelLongClick;
    }

    @NonNull
    @Override
    public ChannelsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = null;
        if(viewType == 1)
            view = inflater.inflate(R.layout.channel_item_selected, parent, false);
        else if (viewType == 0)
            view = inflater.inflate(R.layout.channel_item, parent, false);


        assert view != null; // TODO: проверить как работает
        return  new ChannelsHolder(view, this, mOnChannelClick, mOnChannelLongClick);

    }

    @Override
    public void onBindViewHolder(@NonNull ChannelsHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DbOpenHelper.COLUMN_NAME));
        String url = cursor.getString(cursor.getColumnIndexOrThrow(DbOpenHelper.COLUMN_URL));
        float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DbOpenHelper.COLUMN_RATING));
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("_id"));

        holder.name.setText(name);
        holder.url.setText(url);
        holder.rating.setRating(rating);
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
         notifyDataSetChanged();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(isSelected(position))
            return 1;
        else
            return 0;
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
        notifyDataSetChanged();
    }
    public boolean hasSelected () {
        return !selection.isEmpty();
    }
    public void clearSelection() {
        selection.clear();
    }


    public HashSet<Integer> getSelection() {
        return selection;
    }

//    public List<Integer> getttSelection() {
//        List<Integer> array = new ArrayList<>();
//        array.addAll(selection);
//        return array;
//    }
    public void deleteSelection(int position) {
        selection.remove(position);
    }
}
