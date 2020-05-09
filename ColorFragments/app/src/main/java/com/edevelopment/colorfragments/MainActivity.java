package com.edevelopment.colorfragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    String[] colorsArray = {"Red", "Blue", "Green", "Yellow", "Brown"};
    public static final int RED = 0x80FF0000;
    public static final int GREEN = 0x8000FF00;
    public static final int BLUE = 0x800000FF;
    public static final int YELLOW = 0x80FFFF00;
    public static final int BROWN = 0x80912C0C;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.list_view);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                colorsArray);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView text = view.findViewById(android.R.id.text1);
                String s = text.getText().toString();
                switch (s) {
                    case "Red":
                        createFragment(RED, s);
                        return;
                    case "Blue":
                        createFragment(BLUE, s);
                        return;
                    case "Green":
                        createFragment(GREEN, s);
                        return;
                    case "Yellow":
                        createFragment(YELLOW, s);
                        return;
                    case "Brown":
                        createFragment(BROWN, s);
                        return;
                }
            }
        });

    }

    private void createFragment(int color, String text) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.host, new MyColorfulFragment().newInstance(color, text));
        transaction.commit();
    }
}
