package com.edevelopment.colorfragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MyColorfulFragment extends Fragment {

    private int color;
    private String text;

    public static final String FRAGMENT_COLOR = "FRAGMENT_COLOR";
    public static final String FRAGMENT_TEXT = "FRAGMENT_TEXT";

//    public MyColorfulFragment(int color, String text) {
//        this.color = color;
//        this.text = text;
//    }

    public MyColorfulFragment newInstance(int color, String text) {
        MyColorfulFragment fragment = new MyColorfulFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_COLOR, color);
        bundle.putString(FRAGMENT_TEXT, text);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        if(bundle != null) {
            if (bundle.containsKey(FRAGMENT_COLOR)) {
                color = bundle.getInt(FRAGMENT_COLOR);
            }
            if (bundle.containsKey(FRAGMENT_TEXT)) {
                text = bundle.getString(FRAGMENT_TEXT);
            }
        }


        View view = inflater.inflate(R.layout.fragment_layout, container, false);

        RelativeLayout layout = view.findViewById(R.id.relative_layout);
        TextView textView = view.findViewById(R.id.text);

        layout.setBackgroundColor(color);
        textView.setText(text);

        return view;
    }
}
