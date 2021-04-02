package com.div.quickphotosearchtool.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.div.quickphotosearchtool.R;
import com.div.quickphotosearchtool.utilclass.Utl;

import java.io.File;
import java.lang.reflect.Method;

public class FirstActivity extends Activity {

    ImageView sharebtn, ratebtn, privacybtn, startbtn;

//    Button btn_story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        getWindow().addFlags(1024);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] permision = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permision, 100);
            } else {
//                start();
            }
        }

        sharebtn = findViewById(R.id.sharebtn);
        ratebtn = findViewById(R.id.ratebtn);
        privacybtn = findViewById(R.id.privacybtn);
        startbtn = findViewById(R.id.btn_start);

//        btn_story = findViewById(R.id.btn_story);

        Utl.SetUILinearVivo(this, findViewById(R.id.logo), 980, 860);
        Utl.SetUILinear(this, findViewById(R.id.logo2), 720, 130);
        Utl.SetUILinear(this, startbtn, 350, 330);

        Utl.SetUILinearVivo(this, sharebtn, 150, 150);
        Utl.SetUILinearVivo(this, ratebtn, 150, 150);
        Utl.SetUILinearVivo(this, privacybtn, 150, 150);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create_appfolder(FirstActivity.this);
                startActivity(new Intent(FirstActivity.this, MainActivity.class));
            }
        });


//        btn_story.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                create_appfolder(FirstActivity.this);
//                startActivity(new Intent(FirstActivity.this, FbStoryActivity.class));
//            }
//        });

        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareBody = "Hey I Have Download This Wonderful App.\n\nYou can also Download it From Below link\n\nhttps://play.google.com/store/apps/details?id="
                        + getPackageName();
                Intent sharingIntent = new Intent(
                        Intent.ACTION_SEND);

                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(Intent.EXTRA_TEXT,
                        shareBody);
                startActivity(Intent.createChooser(sharingIntent,
                        "Share using"));
            }
        });

        ratebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id="
                                + getPackageName())));
            }
        });

        privacybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }

    public static File create_appfolder(Context con) {
        File myDir = null;
        try {
            myDir = new File(Environment.getExternalStorageDirectory()
                    .toString() + "/" + getAppName(con));
            if (!myDir.exists())
                myDir.mkdirs();
        } catch (Exception e) {
            Toast.makeText(con, "Error", Toast.LENGTH_SHORT).show();
        }
        return myDir;
    }

    public static String getAppName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    context.getApplicationInfo().packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
        }
        return (String) (applicationInfo != null ? packageManager
                .getApplicationLabel(applicationInfo) : "Unknown");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            int flag = 0;
            for (int i = 0; i < grantResults.length; i++) {
                Log.e("Pr", permissions[i] + "  " + grantResults[i]);
                if (grantResults[i] != 0) {
                    flag = 1;
                    finish();
                    break;
                }
            }
        }
    }

    ImageView im1, im2, im3, im4, im5;

    @Override
    public void onBackPressed() {
        final Dialog dialog1 = new Dialog(FirstActivity.this, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.setContentView(R.layout.dialog_exit);

        ImageView yes = (ImageView) dialog1.findViewById(R.id.yes);
        ImageView no = (ImageView) dialog1.findViewById(R.id.no);
        LinearLayout mypop = (LinearLayout) dialog1.findViewById(R.id.mypop);
        im1 = (ImageView) dialog1.findViewById(R.id.im1);
        im2 = (ImageView) dialog1.findViewById(R.id.im2);
        im3 = (ImageView) dialog1.findViewById(R.id.im3);
        im4 = (ImageView) dialog1.findViewById(R.id.im4);
        im5 = (ImageView) dialog1.findViewById(R.id.im5);

        Utl.SetUIRelative(FirstActivity.this, no, 340, 140);
        Utl.SetUIRelative(FirstActivity.this, yes, 340, 140);
        Utl.SetUILinearVivo(FirstActivity.this, mypop, 1060, 1170);

        im1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPress(im1, R.drawable.terrible_press);
            }
        });

        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPress(im2, R.drawable.bad_press);
            }
        });

        im3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPress(im3, R.drawable.okay_upress);
            }
        });

        im4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPress(im4, R.drawable.good_press);
            }
        });

        im5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonPress(im5, R.drawable.greatpress);
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finishAffinity();
                dialog1.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    public void ButtonPress(ImageView btn, int img1) {
        im1.setImageResource(R.drawable.terrible_unpress);
        im2.setImageResource(R.drawable.bad_unpress);
        im3.setImageResource(R.drawable.okay_unpress);
        im4.setImageResource(R.drawable.good_unpress);
        im5.setImageResource(R.drawable.great_unpress);

        btn.setImageResource(img1);

        startActivity(new Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/details?id="
                        + getPackageName())));
    }
}
