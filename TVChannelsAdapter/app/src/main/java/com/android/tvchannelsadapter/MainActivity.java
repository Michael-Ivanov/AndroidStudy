package com.android.tvchannelsadapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private static final int REQUEST = 562;
    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_CHANNEL = "extra channel name";
    private Channel channel;
    private List<Channel> channels = new ArrayList<>();
    private ListView list;
    private ChannelsAdapter adapter;

    public static final String PREFS = "prefs";
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.list_view);

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        int count = prefs.getInt("count", -1);
        for (int i = 0; i < count; i++) {
            String name = prefs.getString("name" + i, null);
            String url = prefs.getString("url" + i, null);
            float rating = prefs.getFloat("rating" + i, -1);
            Channel channel = new Channel(name, url, rating);
            channels.add(channel);
        }

        adapter = new ChannelsAdapter(this, R.layout.channel_item, channels);
        list.setAdapter(adapter);

        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);

    }
    private void parseChannelXMLList () {
        InputSource channelsInput = new InputSource(getResources().openRawResource(R.raw.webtv_usr));
        XPath xPath = XPathFactory.newInstance().newXPath();
        String expression = "/webtvs/webtv";
        NodeList nodes;
        if(channels.size() != 0) {
            channels.clear();
        }
        try {
            nodes = (NodeList) xPath.evaluate(expression, channelsInput, XPathConstants.NODESET);
            if (nodes != null) {
                int numChannels = nodes.getLength();
                for (int i = 0; i < numChannels; i++) {
                    Node nodeChannel = nodes.item(i);
                    String ch = nodeChannel.getAttributes().getNamedItem("title").getTextContent();
                    String url = nodeChannel.getAttributes().getNamedItem("url").getTextContent();
                    channel = new Channel(ch, url, 0);
                    channels.add(channel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(adapter.hasSelected()) {
            toggleSelection(position);
        } else {
            Intent intent = new Intent(this, ChannelActivity.class);
            Channel channel = channels.get(position);
            intent.putExtra(EXTRA_CHANNEL, (Parcelable) channel);
            intent.putExtra(EXTRA_ID, position);
            startActivityForResult(intent, REQUEST);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (REQUEST == requestCode && data != null) {
            int id = data.getIntExtra(EXTRA_ID, -1);
            Channel c = data.getParcelableExtra(EXTRA_CHANNEL);
            if(c != null) {
                channels.get(id).setRating(c.getRating());
            }
            adapter.notifyDataSetChanged();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onStop() {
        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = prefs.edit();
        int count = 0;
        for (Channel ch : channels) {
            String name = ch.getName();
            String url = ch.getUrl();
            float rating = ch.getRating();
            editor.putString("name" + count, name);
            editor.putString("url" + count, url);
            editor.putFloat("rating" + count, rating);
            count++;
        }
        editor.putInt("count", count);
        editor.apply();

        super.onStop();
    }
    private void toggleSelection(int position) {
        adapter.toggleSelection(position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        toggleSelection(position);
        return true;
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
}
