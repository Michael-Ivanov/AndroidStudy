package com.android.tvchannelsadapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class MainActivity extends AppCompatActivity implements ChannelsHolder.OnChannelClick, ChannelsHolder.OnChannelLongClick {

    private static final int REQUEST = 562;
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_CHANNEL = "extra channel name";
    public static final String EXTRA_RATING = "extra rating";

    private RecyclerView recyclerView;
    private ChannelsAdapter adapter;

    DbOpenHelper helper = new DbOpenHelper(this);
    SQLiteDatabase database;
    private String orderBy;

    private static final String TAG = "dddd";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = helper.getWritableDatabase();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL));

        adapter = new ChannelsAdapter(this, getAllItems(), this, this);
        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem((long) viewHolder.itemView.getTag());
                adapter.swapCursor(getAllItems());
            }
        }).attachToRecyclerView(recyclerView);


    }
    private Cursor getAllItems() {
        return database.query(
               DbOpenHelper.DB_TABLE,
                null,null, null,
                null, null, orderBy
        );
    }


    private void showAddDialog() {
        View addDialog = getLayoutInflater().inflate(R.layout.add_dialog, null);
        final EditText addChannelName = addDialog.findViewById(R.id.add_channel_name);
        final EditText addChannelUrl = addDialog.findViewById(R.id.add_channel_url);
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b
                .setView(addDialog)
                .setTitle("Add new channel")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = addChannelName.getText().toString();
                        String url = addChannelUrl.getText().toString();
                        ContentValues values = new ContentValues();
                        values.put(DbOpenHelper.COLUMN_NAME, name);
                        values.put(DbOpenHelper.COLUMN_URL, url);
                        values.put(DbOpenHelper.COLUMN_RATING, 0);
                        database.insert(DbOpenHelper.DB_TABLE, null, values);

                    }
                })
                .create().show();
    }

    private void deleteAllItems() {
        database.execSQL(" delete from channels; ");
        database.execSQL(" delete from sqlite_sequence where name='channels' ;");

    }

    private void deleteItem(long id) {
            database.delete(DbOpenHelper.DB_TABLE, "_id = " + id, null);
    }

    private void deleteSelected() {

        List<Integer> items = new ArrayList<>();
        items.addAll(adapter.getSelection());
        Collections.sort(items, Collections.<Integer>reverseOrder());

        for(int i : items) {
            Cursor cursor = getAllItems();
            cursor.moveToPosition(i);
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            database.delete(DbOpenHelper.DB_TABLE, "_id = " + id, null);
            cursor.close();
        }
        adapter.clearSelection();
    }

    private void parseChannelXMLList () {
        InputSource channelsInput = new InputSource(getResources().openRawResource(R.raw.webtv_usr));
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/webtvs/webtv";
        NodeList nodes;

        ContentValues values = new ContentValues();
        database = helper.getWritableDatabase();
        try {
            nodes = (NodeList) xPath.evaluate(expression, channelsInput, XPathConstants.NODESET);
            if (nodes != null) {
                int numChannels = nodes.getLength();

                for (int i = 0; i < numChannels; i++) {
                    Node nodeChannel = nodes.item(i);

                    String ch = nodeChannel.getAttributes().getNamedItem("title").getTextContent();
                    String url = nodeChannel.getAttributes().getNamedItem("url").getTextContent();

                    values.put(DbOpenHelper.COLUMN_NAME, ch);
                    values.put(DbOpenHelper.COLUMN_URL, url);
                    values.put(DbOpenHelper.COLUMN_RATING, 0);

                    database.insert(DbOpenHelper.DB_TABLE, null, values);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.swapCursor(getAllItems());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                showRefreshDialog();
                return true;
            case R.id.delete_all:
                deleteAllItems();
                adapter.swapCursor(getAllItems());
                return true;
            case R.id.delete_one:
                deleteSelected();
                adapter.swapCursor(getAllItems());
                return true;
            case R.id.sort_by_name:
                orderBy = DbOpenHelper.COLUMN_NAME + " asc ";
                adapter.swapCursor(getAllItems());
                return true;
            case R.id.sort_by_id_desc:
                orderBy = " _id desc ";
                adapter.swapCursor(getAllItems());
                return true;
            case R.id.add_channel:
                showAddDialog();
                adapter.swapCursor(getAllItems());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void showRefreshDialog() {
        View dialog = getLayoutInflater().inflate(R.layout.dialog_refresh, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(dialog)
                .setPositiveButton("Refresh", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        parseChannelXMLList();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create()
                .show();

    }


        @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (REQUEST == requestCode && data != null) {
            int id = data.getIntExtra(EXTRA_ID, -1);
            float rating = data.getFloatExtra(EXTRA_RATING, 0);

            ContentValues values = new ContentValues();
            values.put(DbOpenHelper.COLUMN_RATING, rating);
//            устанавливаем мини-рейтинг бар
            database = helper.getWritableDatabase();
            database.update(
                    DbOpenHelper.DB_TABLE,
                    values,
                    "_id = " + id,
                    null
            );
            adapter.swapCursor(getAllItems());

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
    }

    @Override
    public void onBackPressed() {
        if (adapter.hasSelected()) {
            adapter.clearSelection();
            adapter.notifyDataSetChanged();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onChannelClick(int position) {
        if(adapter.hasSelected()) {
            toggleSelection(position);
        } else {
            Intent intent = new Intent(this, ChannelActivity.class);

            Cursor cursor = getAllItems();
            cursor.moveToPosition(position);
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DbOpenHelper.COLUMN_NAME));
            float rating = cursor.getFloat(cursor.getColumnIndexOrThrow(DbOpenHelper.COLUMN_RATING));

            intent.putExtra(EXTRA_ID, id);
            intent.putExtra(EXTRA_CHANNEL, name);
            intent.putExtra(EXTRA_RATING, rating);
            startActivityForResult(intent, REQUEST);

        }

    }

    @Override
    public void onChannelLongClick(int position) {
        toggleSelection(position);
    }
}
