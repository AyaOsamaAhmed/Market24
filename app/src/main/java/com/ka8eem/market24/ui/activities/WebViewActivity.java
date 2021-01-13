package com.ka8eem.market24.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.ka8eem.market24.R;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = findViewById(R.id.web_view);
        intent = getIntent();
        if (intent != null) {
            String url = intent.getStringExtra("url");
            webView.loadUrl(url);
            finish();
        }
    }
}