package com.div.quickphotosearchtool.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.div.quickphotosearchtool.R;

import java.net.MalformedURLException;
import java.net.URL;

public class ImageSearchActivity extends Activity {

    private String imageName;
    private WebView webView;
    ProgressBar pbar;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_image_search);
        getWindow().addFlags(1024);

        imageName = getIntent().getStringExtra("image_name");
        pbar = findViewById(R.id.pbar);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new HelloWebViewClient());

        try {
            webView.loadUrl(new URL("http://images.google.com/searchbyimage?site=search&image_url=http://searchbyanimage.com/image-search-api/uploads/" + imageName).toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        findViewById(R.id.backy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private class HelloWebViewClient extends WebViewClient {
        private HelloWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            pbar.setVisibility(View.GONE);
            webView.loadUrl(str);
            return true;
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            super.onReceivedError(webView, i, str, str2);
        }
    }

    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
            startActivity(new Intent(ImageSearchActivity.this,FirstActivity.class));
            finish();
        }
    }
}
