package com.android.metropicker;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class StationsListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    String [] stationsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stationsArray = getResources().getStringArray(R.array.stations);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.list_item, stationsArray);
        getListView().setAdapter(aa);
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        String res = ((TextView) view).getText().toString();
        intent.putExtra(MainActivity.STATION_NAME, res);
        setResult(RESULT_OK, intent);
        finish();
    }
}
