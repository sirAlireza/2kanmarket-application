package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class product_list extends AppCompatActivity {

    int cat1_id,cat2_id,cat3_id;
    protected int view_status=1;
    Search_Product search_product;
    String cat_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle=getIntent().getExtras();

        cat1_id=bundle.getInt("cat1_id",0);
        cat2_id=bundle.getInt("cat2_id",0);
        cat3_id=bundle.getInt("cat3_id",0);
        String filter=bundle.getString("filter","no");
        cat_name=bundle.getString("cat_name","");
        getSupportActionBar().setTitle(cat_name);

        RelativeLayout layout1=(RelativeLayout)findViewById(R.id.layout1);
        RelativeLayout layout2=(RelativeLayout)findViewById(R.id.layout2);
        int total_width=getResources().getDisplayMetrics().widthPixels;
        int w1=Setting.pxfromdp(this,52);
        float w=total_width-w1;
        w=w/2;
        int a=(int) w;

        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(a, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout1.setLayoutParams(params);
        layout2.setLayoutParams(params);



        search_product=new Search_Product(cat3_id,this,filter);



    }
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    public void show_filter_list(View view)
    {
        Intent j=new Intent(product_list.this,filter_list.class);
        j.putExtra("cat1_id",cat1_id);
        j.putExtra("cat2_id",cat2_id);
        j.putExtra("cat3_id",cat3_id);
        j.putExtra("cat_name",cat_name);
        startActivity(j);
        overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);
    }
    public void change_view_list(View view)
    {
        ImageView change_view_ic=(ImageView)findViewById(R.id.change_view_ic);
        if(view_status==1)
        {
            view_status=2;
            change_view_ic.setImageResource(R.drawable.ic_view_week);
            search_product.set_view2();
        }
        else
        {
            view_status=1;
            change_view_ic.setImageResource(R.drawable.ic_view_stream);
            search_product.set_view1();
        }
    }

}
