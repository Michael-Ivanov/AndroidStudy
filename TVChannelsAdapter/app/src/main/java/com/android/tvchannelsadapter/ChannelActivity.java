package com.android.tvchannelsadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RatingBar;
import android.widget.TextView;

public class ChannelActivity extends AppCompatActivity {

    private TextView channelTitle;
    private RatingBar ratingBar;
    private WebView webView;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        channelTitle = findViewById(R.id.channel_title);
        ratingBar = findViewById(R.id.channel_rating);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        if (intent != null) {

                String channelName = intent.getStringExtra(MainActivity.EXTRA_CHANNEL);
                float channelRating = intent.getFloatExtra(MainActivity.EXTRA_RATING, 0);

                channelTitle.setText(channelName);
                channelName = channelName.replace(" ", "_");
                webView.loadUrl("https://ru.m.wikipedia.org/wiki/" + channelName);

                ratingBar.setRating(channelRating);
                if (intent.hasExtra(MainActivity.EXTRA_ID)) {
                    id = intent.getIntExtra(MainActivity.EXTRA_ID, -1);
                }

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXTRA_RATING, ratingBar.getRating());
        intent.putExtra(MainActivity.EXTRA_ID, id);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
