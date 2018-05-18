package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class filter_list extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_list);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle bundle=getIntent().getExtras();

        final int cat1_id=bundle.getInt("cat1_id",0);
        final int cat2_id=bundle.getInt("cat2_id",0);
        final int cat3_id=bundle.getInt("cat3_id",0);
        final String cat_name=bundle.getString("cat_name","");


        int total_with=getResources().getDisplayMetrics().widthPixels;
        int w1=Setting.pxfromdp(this,150);
        float w=total_with-w1;
        w=w/2;
        int a=(int) w;

        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(a, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView remover_filter=(TextView) findViewById(R.id.remover_filter);
        remover_filter.setLayoutParams(layoutParams);

        LinearLayout product_status_box=(LinearLayout) findViewById(R.id.product_status_box);
        product_status_box.setLayoutParams(layoutParams);

        get_filter filter=new get_filter(this,cat1_id,cat2_id,cat3_id);

        Button send_filter=(Button)findViewById(R.id.send_filter);
        send_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent j=new Intent(filter_list.this,product_list.class);
                j.putExtra("filter","ok");
                j.putExtra("cat1_id",cat1_id);
                j.putExtra("cat2_id",cat2_id);
                j.putExtra("cat3_id",cat3_id);
                j.putExtra("cat_name",cat_name);
                startActivity(j);
            }
        });

    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
