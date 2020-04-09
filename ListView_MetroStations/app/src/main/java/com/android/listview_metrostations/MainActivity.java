package com.android.listview_metrostations;

import android.app.ListActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {
    String[] stationsArray;

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

        view.setBackgroundColor(Color.RED);
        Toast.makeText(this, ((TextView) view).getText(), Toast.LENGTH_SHORT).show();

    }
}
