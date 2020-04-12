package com.android.metropicker;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class StationsListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    String [] stationsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stationsArray = getResources().getStringArray(R.array.stations);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.list_item, stationsArray);
        ListView listView = getListView();
        listView.setAdapter(aa);
        listView.setOnItemClickListener(this);
        registerForContextMenu(listView);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String res = ((TextView) view).getText().toString();
        sendStation(res);
    }

    private void sendStation(String value) {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.STATION_NAME, value);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_station_pick, menu);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        TextView tw = (TextView) info.targetView;
        menu.setHeaderTitle(tw.getText().toString());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_send:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                TextView tw = (TextView) info.targetView;
                sendStation(tw.getText().toString());
                return true;
            case R.id.item_exit:
                finish();
                return true;
        }
        return super.onContextItemSelected(item);
    }
}
