package com.android.metropicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_CODE = 7595;
    public static final String STATION_NAME = "Station name";
    TextView textStation;
    Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStation = findViewById(R.id.text_field);

        mainButton = findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_button) {
            Intent intent = new Intent(this, StationsListActivity.class);
            startActivityForResult(intent, RESULT_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_CODE) {
            String s = null;
            if (data != null) {
                s = data.getStringExtra(STATION_NAME);
            }
            textStation.setText(s);
        } else {
            textStation.setText(R.string.no_station);
        }
    }
}
