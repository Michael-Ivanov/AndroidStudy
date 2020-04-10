package com.android.metropicker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

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
        if (view != null) {
            String res = stationsArray[position];
            intent.putExtra(MainActivity.STATION_NAME, res);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            intent.putExtra(MainActivity.STATION_NAME, "Station not picked");
            setResult(RESULT_CANCELED, intent);
        }
    }
}
