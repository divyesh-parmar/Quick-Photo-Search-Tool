package com.div.quickphotosearchtool.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.div.quickphotosearchtool.R;
import com.div.quickphotosearchtool.utilclass.Utl;

public class SplashActivity extends Activity {

    int[] imageArray = {R.drawable.dot1, R.drawable.dot2, R.drawable.dot3, R.drawable.dot4};
    ImageView proimg, logo1;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().addFlags(1024);

        proimg = findViewById(R.id.txtlogo);
        logo1 = findViewById(R.id.logo2);
        webView = findViewById(R.id.webView);

        Utl.SetUIRelative(this, proimg, 230, 30);
        Utl.SetUIRelativeVivo(this, webView, 1000, 1000);
        Utl.SetUIRelative(this, findViewById(R.id.logo2), 730, 140);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.loadUrl("file:///android_asset/mygif.gif");

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            int i = 0;

            public void run() {
                proimg.setImageResource(imageArray[i]);
                i++;
                if (i > imageArray.length - 1) {
                    i = 0;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, FirstActivity.class));
            }
        }, 4000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}