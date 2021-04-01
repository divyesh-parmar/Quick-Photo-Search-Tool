package com.div.quickphotosearchtool.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.div.quickphotosearchtool.R;

public class WebSearchActivity extends Activity {

    WebView webView1;
    ProgressBar pbar;

    @SuppressLint({"SetJavaScriptEnabled"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        setContentView(R.layout.activity_image_search);

        webView1 = findViewById(R.id.webView);
        pbar = findViewById(R.id.pbar);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.setWebViewClient(new a(this, this));
        String mUrl = "http://images.google.com/images?q=" + MainActivity.TEXT;
        webView1.loadUrl(mUrl);

        pbar.setVisibility(View.GONE);
        findViewById(R.id.backy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WebSearchActivity.this,FirstActivity.class));
        finish();
    }

    public class a extends WebViewClient {
        public final Activity a;

        public a(WebSearchActivity webSearchActivity, Activity activity) {
            a = activity;
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            Toast.makeText(a, str, Toast.LENGTH_LONG).show();
        }

        @TargetApi(23)
        public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
            webResourceError.getErrorCode();
            String charSequence = webResourceError.getDescription().toString();
            webResourceRequest.getUrl().toString();
            Toast.makeText(a, charSequence, Toast.LENGTH_LONG).show();
        }
    }

}
