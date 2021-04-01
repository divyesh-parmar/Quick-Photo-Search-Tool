package com.div.quickphotosearchtool.utilclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Utl {

    public static void SetUIRelative(Context cn, View mview, int WIDTH, int HEIGHT) {
        RelativeLayout.LayoutParams layoutParamsi = new RelativeLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().heightPixels * HEIGHT / 1920);

        mview.setLayoutParams(layoutParamsi);
    }

    public static void SetUILinear(Context cn, View mview, int WIDTH, int HEIGHT) {
        LinearLayout.LayoutParams layoutParamsi = new LinearLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().heightPixels * HEIGHT / 1920);

        mview.setLayoutParams(layoutParamsi);
    }

    public static void SetUILinearVivo(Context cn, View mview, int WIDTH,int HEIGHT) {
        LinearLayout.LayoutParams layoutParamsi = new LinearLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().widthPixels * HEIGHT / 1080);

        mview.setLayoutParams(layoutParamsi);
    }

    public static void SetUIRelativeVivo(Context cn, View mview, int WIDTH,int HEIGHT) {
        RelativeLayout.LayoutParams layoutParamsi = new RelativeLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().widthPixels * HEIGHT / 1080);

        mview.setLayoutParams(layoutParamsi);
    }

    public static void SetUILinearTopMargin(Context cn, View mview, int WIDTH, int HEIGHT, int TOP) {
        LinearLayout.LayoutParams layoutParamsi = new LinearLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().heightPixels * HEIGHT / 1920);
        layoutParamsi.topMargin = cn.getResources().getDisplayMetrics().widthPixels * TOP / 1920;
        mview.setLayoutParams(layoutParamsi);
    }

    public static void SetUIRelativeTopMargin(Context cn, View mview, int WIDTH, int HEIGHT, int TOP) {
        RelativeLayout.LayoutParams layoutParamsi = new RelativeLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().heightPixels * HEIGHT / 1920);
        layoutParamsi.topMargin = cn.getResources().getDisplayMetrics().widthPixels * TOP / 1920;
        mview.setLayoutParams(layoutParamsi);
    }

    public static void SetUIRelativeMargin(Context cn, View mview, int WIDTH, int HEIGHT, int TOP) {
        RelativeLayout.LayoutParams layoutParamsi = new RelativeLayout.LayoutParams(
                cn.getResources().getDisplayMetrics().widthPixels * WIDTH / 1080,
                cn.getResources().getDisplayMetrics().heightPixels * HEIGHT / 1920);
        layoutParamsi.setMargins(cn.getResources().getDisplayMetrics().widthPixels * TOP / 1080,cn.getResources().getDisplayMetrics().widthPixels * TOP / 1080,cn.getResources().getDisplayMetrics().widthPixels * TOP / 1080,cn.getResources().getDisplayMetrics().widthPixels * TOP / 1080);
        mview.setLayoutParams(layoutParamsi);
    }
}