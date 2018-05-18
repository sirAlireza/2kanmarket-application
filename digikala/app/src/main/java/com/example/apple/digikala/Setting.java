package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

/**
 * Created by apple on 3/1/18.
 */

public class Setting
{

    protected static String url="http://192.168.43.210/laravel-digikala/public";
    public static int pxfromdp(Context context,int dp)
    {
        float a=dp*context.getResources().getDisplayMetrics().density;
        return (int)a;
    }
    public static int dpfrompx(Context context,int dp)
    {
        float a=dp/context.getResources().getDisplayMetrics().density;
        return (int)a;
    }
    public static void show_content(Context context)
    {
        RelativeLayout relativeLayout=((Activity) context).findViewById(R.id.load_layout);
        relativeLayout.setVisibility(View.GONE);

        ScrollView scrollView=((Activity) context).findViewById(R.id.content_box);
        scrollView.setVisibility(View.VISIBLE);

    }
}
