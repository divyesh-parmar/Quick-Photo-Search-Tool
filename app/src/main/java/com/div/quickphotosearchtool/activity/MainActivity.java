package com.div.quickphotosearchtool.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.div.quickphotosearchtool.utilclass.AndroidMultipartEntity;
import com.div.quickphotosearchtool.R;
import com.div.quickphotosearchtool.utilclass.Utl;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    private static int CAMERA_REQUEST = 456;
    private Uri fileUri;

    public static String TEXT;
    ImageView btn_gallery, btn_camera, btn_create, btn_searchu;
    int L;
    int width, height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(1024);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String[] permision = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(permision, 100);
            } else {
//                start();
            }
        }

        btn_gallery = findViewById(R.id.btn_gallery);
        btn_camera = findViewById(R.id.btn_camera);
        btn_searchu = findViewById(R.id.btn_searchu);
        btn_create = findViewById(R.id.btn_create);

        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;

        Utl.SetUILinearTopMargin(this, btn_camera, 140, 140, 25);
        Utl.SetUILinearTopMargin(this, btn_gallery, 140, 140, 25);
        Utl.SetUILinearTopMargin(this, btn_searchu, 140, 140, 25);
        Utl.SetUILinearTopMargin(this, btn_create, 140, 140, 25);

        BTNS_VISIBILITY(8);

        findViewById(R.id.backy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_camera.getVisibility() == View.VISIBLE) {
                    BTNS_VISIBILITY(8);
                    btn_create.setImageResource(R.drawable.cancel_unpress);
                } else {
                    BTNS_VISIBILITY(0);
                    btn_create.setImageResource(R.drawable.cancel_press);
                }
            }
        });

        btn_searchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTNS_VISIBILITY(8);
                btn_create.setImageResource(R.drawable.cancel_unpress);

                final Dialog dialog1 = new Dialog(MainActivity.this);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog1.setContentView(R.layout.dialog_searchword);

                LinearLayout popbox = dialog1.findViewById(R.id.popbox);
                final EditText et_search = dialog1.findViewById(R.id.et_search);
                ImageView search_btns = (ImageView) dialog1.findViewById(R.id.search_btns);

                Utl.SetUILinear(MainActivity.this, popbox, 1000, 400);
                Utl.SetUILinear(MainActivity.this, et_search, 788, 100);
                Utl.SetUILinear(MainActivity.this, search_btns, 139, 100);

                search_btns.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isNetworkConnected()) {
                            TEXT = et_search.getText().toString();
                            if (TEXT.isEmpty()) {
                                Toast.makeText(MainActivity.this, "Please Enter Text", Toast.LENGTH_SHORT).show();
                            } else {
                                et_search.setText("");
                                dialog1.dismiss();
                                startActivity(new Intent(MainActivity.this, WebSearchActivity.class));
                            }
                        } else {
                            Toast.makeText(MainActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog1.show();
            }
        });

        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BTNS_VISIBILITY(8);
                L = 0;
                btn_create.setImageResource(R.drawable.cancel_unpress);
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BTNS_VISIBILITY(8);
                btn_create.setImageResource(R.drawable.cancel_unpress);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (Build.VERSION.SDK_INT > 19) {

                    L = 1;
                    if (intent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        if (photoFile != null) {

                            fileUri = FileProvider.getUriForFile(MainActivity.this,
                                    "com.div.quickphotosearchtool.provider",
                                    photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }

                    }

                } else {
                    L = 0;
                    fileUri = captureImage(MainActivity.this, CAMERA_REQUEST);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, FirstActivity.class));
        finish();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    void BTNS_VISIBILITY(int x) {
        btn_searchu.setVisibility(x);
        btn_gallery.setVisibility(x);
        btn_camera.setVisibility(x);
    }

    public void saveBitmap(Bitmap bitmap) {

        File myDir = new File(Environment.getExternalStorageDirectory()
                .toString()
                + "/"
                + getResources().getString(R.string.app_name));
        myDir.mkdirs();
        File file = new File(myDir, "Temp" + System.currentTimeMillis() + ".jpg");
        String filepath = file.getPath();
        resultUri = filepath;
        if (file.exists()) {
            file.delete();
        }
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);

            ostream.close();

        } catch (Exception e) {

        }
        Intent mediaScanIntent = new Intent(
                "android.intent.action.MEDIA_SCANNER_SCAN_FILE");
        mediaScanIntent.setData(Uri.fromFile(new File(filepath)));
        sendBroadcast(mediaScanIntent);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
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

    Bitmap bitmapx, mainBit;
    String FiPATH;

    public void CHU_YA_Dialog(Uri path) {

        final Dialog dialog = new Dialog(MainActivity.this, android.R.style.Theme_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_editimage);

        ImageView backy = dialog.findViewById(R.id.backy);
        final ImageView imgi = dialog.findViewById(R.id.img);
        final ImageView btn_crop = dialog.findViewById(R.id.btn_crop);
        final ImageView btn_horflip = dialog.findViewById(R.id.btn_horflip);
        final ImageView btn_vertflip = dialog.findViewById(R.id.btn_vertflip);
        final ImageView btn_rotate = dialog.findViewById(R.id.btn_rotate);
        ImageView btn_src = dialog.findViewById(R.id.btn_src);
        final ImageView btn_edit = dialog.findViewById(R.id.btn_edit);
        final ImageView btn_crt = dialog.findViewById(R.id.btn_crt);
        final ImageView btn_cam = dialog.findViewById(R.id.btn_cam);
        final ImageView btn_gal = dialog.findViewById(R.id.btn_gal);

        if (L == 1) {
            Glide.with(MainActivity.this)
                    .asBitmap()
                    .load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            mainBit = resource;
                            bitmapx = mainBit;
                            Glide.with(MainActivity.this).load(mainBit).diskCacheStrategy(DiskCacheStrategy.NONE).into(imgi);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        } else {
            Glide.with(MainActivity.this)
                    .asBitmap()
                    .load(getRealPathFromURI(path)).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            mainBit = resource;
                            bitmapx = mainBit;
                            Glide.with(MainActivity.this).load(mainBit).into(imgi);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }


        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_crop.getVisibility() == View.VISIBLE) {
                    btn_crop.setVisibility(View.GONE);
                    btn_horflip.setVisibility(View.GONE);
                    btn_vertflip.setVisibility(View.GONE);
                    btn_rotate.setVisibility(View.GONE);

                    btn_edit.setImageResource(R.drawable.edit_unpress);
                } else {
                    btn_crop.setVisibility(View.VISIBLE);
                    btn_horflip.setVisibility(View.VISIBLE);
                    btn_vertflip.setVisibility(View.VISIBLE);
                    btn_rotate.setVisibility(View.VISIBLE);

                    btn_edit.setImageResource(R.drawable.edit_press);
                }
            }
        });

        btn_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog1 = new Dialog(MainActivity.this, android.R.style.Theme_NoTitleBar_Fullscreen);
                dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog1.setCancelable(true);
                dialog1.setContentView(R.layout.dialog_crop);

                img = (CropImageView) dialog1.findViewById(R.id.img1);
                btncrop = (ImageView) dialog1.findViewById(R.id.btncrop);
                backy1 = (ImageView) dialog1.findViewById(R.id.backy1);
                img.setFixedAspectRatio(false);

//                Glide.with(MainActivity.this).load(bitmapx).into(img);

                Glide.with(MainActivity.this)
                        .asBitmap()
                        .load(bitmapx).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                img.setImageBitmap(bitmapResize(resource, width * 1080 / 1080, height * 1720 / 1920));

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });

                backy1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog1.dismiss();
                    }
                });

                btncrop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog1.dismiss();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Bitmap mBit = img.getCroppedImage();
                                bitmapx = mBit;
                                Glide.with(MainActivity.this).load(bitmapx).into(imgi);
                            }
                        });

                    }
                });

                dialog1.show();
            }
        });

        btn_crt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_cam.getVisibility() == View.VISIBLE) {
                    btn_crt.setImageResource(R.drawable.cancel_unpress);
                    btn_cam.setVisibility(View.GONE);
                    btn_gal.setVisibility(View.GONE);
                } else {
                    btn_crt.setImageResource(R.drawable.cancel_press);
                    btn_cam.setVisibility(View.VISIBLE);
                    btn_gal.setVisibility(View.VISIBLE);
                }
            }
        });

        btn_cam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (Build.VERSION.SDK_INT > 19) {

                    L = 1;
                    if (intent.resolveActivity(getPackageManager()) != null) {

                        File photoFile = null;
                        try {
                            photoFile = createImageFile();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                        if (photoFile != null) {

                            fileUri = FileProvider.getUriForFile(MainActivity.this,
                                    "com.div.quickphotosearchtool.provider",
                                    photoFile);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(intent, CAMERA_REQUEST);
                        }

                    }

                } else {
                    L = 0;
                    fileUri = captureImage(MainActivity.this, CAMERA_REQUEST);
                }

            }
        });

        btn_gal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                L = 0;
                startActivityForResult(new Intent("android.intent.action.PICK", MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
            }
        });

        btn_horflip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bitmapx = flipBitmapHorizontally(bitmapx);
                Glide.with(MainActivity.this).load(bitmapx).into(imgi);

            }
        });

        btn_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapx = rotateBitmap(bitmapx, 90);
                Glide.with(MainActivity.this).load(bitmapx).into(imgi);
            }
        });

        btn_vertflip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmapx = flipBitmapVertically(bitmapx);
                Glide.with(MainActivity.this).load(bitmapx).into(imgi);
            }
        });

        btn_src.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBitmap(bitmapx);

                new UploadFileToServer2().execute(new Void[0]);
            }
        });

        backy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
    }

    public Bitmap flipBitmapHorizontally(Bitmap bInput) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        return Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(),
                bInput.getHeight(), matrix, true);
    }

    public Bitmap flipBitmapVertically(Bitmap bInput) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        return Bitmap.createBitmap(bInput, 0, 0, bInput.getWidth(),
                bInput.getHeight(), matrix, true);
    }

    public static Bitmap bitmapResize(Bitmap bitmap, int i, int i2) {
        int width2 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        if (width2 >= height2) {
            int i3 = (height2 * i) / width2;
            if (i3 > i2) {
                i = (i * i2) / i3;
            } else {
                i2 = i3;
            }
        } else {
            int i4 = (width2 * i2) / height2;
            if (i4 > i) {
                i2 = (i2 * i) / i4;
            } else {
                i = i4;
            }
        }
        return Bitmap.createScaledBitmap(bitmap, i, i2, true);
    }

    public Bitmap getBitmapFromUri(Uri u) {
        Bitmap myBitmap = null;
        try {
            myBitmap = MediaStore.Images.Media.getBitmap(
                    getContentResolver(), u);
        } catch (Exception e) {
        }
        return myBitmap;
    }

    public static Uri captureImage(Context mContext, int REQUEST_PHOTO) {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        File img_path = Environment.getExternalStoragePublicDirectory(Environment
                .DIRECTORY_PICTURES);
        File mediaStorageDir = new File(img_path, "abc");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("aa", "Oops! Failed create " + "abc" + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss", Locale.getDefault())
                .format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" +
                timeStamp + ".jpg");
        Uri imageUri = Uri.fromFile(mediaFile);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        ((Activity) mContext).startActivityForResult(intent, REQUEST_PHOTO);
        return imageUri;

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        String currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    CropImageView img;
    ImageView btncrop, backy1;

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == CAMERA_REQUEST) {

            Uri selectedImage = fileUri;

            CHU_YA_Dialog(selectedImage);

//            final Dialog dialog1 = new Dialog(MainActivity.this, android.R.style.Theme_NoTitleBar_Fullscreen);
//            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog1.setCancelable(true);
//            dialog1.setContentView(R.layout.dialog_crop);
//
//            img = (CropImageView) dialog1.findViewById(R.id.img1);
//            btncrop = (ImageView) dialog1.findViewById(R.id.btncrop);
//            backy1 = (ImageView) dialog1.findViewById(R.id.backy1);
//            img.setFixedAspectRatio(false);
//            img.setImageUriAsync(selectedImage);
//
//            backy1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    dialog1.dismiss();
//                }
//            });
//
//            btncrop.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO Auto-generated method stub
//                    dialog1.dismiss();
//
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Bitmap mBit = img.getCroppedImage();
//                            saveBitmap(mBit);
//
//                            new UploadFileToServer2().execute(new Void[0]);
//                        }
//                    });
//
//                }
//            });
//
//            dialog1.show();

        } else if (i == 4) {

            Uri selectedImage = intent.getData();

            CHU_YA_Dialog(selectedImage);


        } else if (i == 7) {
            Toast.makeText(this, "Background Set", Toast.LENGTH_SHORT).show();
        }
    }

    public static final String FILE_UPLOAD_URL = "http://searchbyanimage.com/image-search-api/fileUpload.php";
    String resultUri;
    float totalSize = 0.0f;

    private class UploadFileToServer2 extends AsyncTask<Void, Integer, String> {

        ProgressDialog progressDialog;
        String inputLine = null;
        HttpEntity r_entity;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Image Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(FILE_UPLOAD_URL);

            try {
                AndroidMultipartEntity entity = new AndroidMultipartEntity(
                        new AndroidMultipartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {
                                publishProgress((int) ((num / (float) totalSize) * 100));
                            }
                        });

                //Recreates the source file to be sent
                String path = resultUri;
                File sourceFile = new File(path);

                // Adding file data to http body
                entity.addPart("image", new FileBody(sourceFile));

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response

                    if (r_entity != null) {
                        inputLine = EntityUtils.toString(r_entity);
                    }

                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("XXX", "Response from server: " + result);
            //Dismiss the progress dialong when successful

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog.dismiss();
                    Intent intent = new Intent(MainActivity.this, ImageSearchActivity.class);
                    intent.putExtra("image_name", inputLine.trim());
                    startActivity(intent);
                }
            });

            super.onPostExecute(result);
        }
    }

}