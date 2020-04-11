package com.android.metropicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_CODE = 7595;
    public static final String STATION_NAME = "Station name";
    public static final String ACTION_PICK = "com.example.metropicker.intent.action.PICK_METRO_STATION";
    TextView textStation;
    Button mainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textStation = findViewById(R.id.text_field);

        mainButton = findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();
        Toast.makeText(this, action, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_button) {
//            для явного вызова
//            Intent intent = new Intent(this, StationsListActivity.class);
//            startActivityForResult(intent, RESULT_CODE);

//            для неявного вызова
            Intent intent = new Intent(ACTION_PICK);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, RESULT_CODE);
            } else {
                Toast.makeText(this, "No application available",
                        Toast.LENGTH_SHORT).show();
            }

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
