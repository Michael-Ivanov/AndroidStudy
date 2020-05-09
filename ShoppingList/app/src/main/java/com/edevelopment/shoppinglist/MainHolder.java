package com.edevelopment.shoppinglist;

import android.content.ContentValues;
import android.database.Cursor;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MainHolder extends RecyclerView.ViewHolder {
    TextView itemToBuy;
    CheckBox checkBox;
    RecyclerMainAdapter adapter;
    DbOpenHelper helper = new DbOpenHelper(itemView.getContext());

    public MainHolder(@NonNull View itemView, RecyclerMainAdapter adapter) {
        super(itemView);

        this.adapter = adapter;
        itemToBuy = itemView.findViewById(R.id.item_to_buy);
        checkBox = itemView.findViewById(R.id.checkbox);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                if (checkBox.isChecked()) {
                    values.put(DbOpenHelper.COLUMN_IS_CHECKED, 1);
                } else {
                    values.put(DbOpenHelper.COLUMN_IS_CHECKED, 0);
                }
                helper.getWritableDatabase().update(
                        DbOpenHelper.DB_TABLE,
                        values,
                        "_id = " + itemView.getTag(),
                        null
                );
                adapter.swapCursor(getCursor());
            }
        });
    }

    private Cursor getCursor() {
        return helper.getWritableDatabase().query(
                DbOpenHelper.DB_TABLE,
                null, null, null,
                null, null, null
        );
    }
}
