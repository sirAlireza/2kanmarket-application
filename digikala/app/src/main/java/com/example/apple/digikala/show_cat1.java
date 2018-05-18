package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class show_cat1 extends AppCompatActivity {

    RequestQueue get_category;
    int[] cat_list_id;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cat1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        Bundle bundle=getIntent().getExtras();

        final int cat_id=bundle.getInt("cat_id",0);

        final  int cat_index=bundle.getInt("position",0);

        final TabLayout cat_tab=(TabLayout) findViewById(R.id.cat_tab);

        get_category= Volley.newRequestQueue(this);
        String url=Setting.url+"/api/get_android_cat_index";

        final Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/iranSansWeb.ttf");
        final JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    cat_list_id=new int[response.length()];
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        TextView textView=new TextView(show_cat1.this);
                        textView.setText(jsonObject.getString("cat_name"));
                        textView.setTextColor(Color.WHITE);
                        textView.setTypeface(typeface);
                        cat_tab.addTab(cat_tab.newTab().setCustomView(textView));
                        cat_list_id[i]=jsonObject.getInt("id");


                        cat_tab.post(new Runnable() {
                            @Override
                            public void run() {

                                int p=((ViewGroup) cat_tab.getChildAt(0)).getChildAt(cat_index).getLeft();
                                cat_tab.setScrollX(p);
                                cat_tab.getTabAt(cat_index).select();
                            }
                        });
                    }

                    get_child_cat1 get_child=new get_child_cat1(show_cat1.this,cat_list_id,response.length(),cat_tab,cat_index,cat_id);


                    RelativeLayout load_layout=(RelativeLayout)findViewById(R.id.load_layout);
                    load_layout.setVisibility(View.GONE);

                    RelativeLayout content_box=(RelativeLayout)findViewById(R.id.content_box);
                    content_box.setVisibility(View.VISIBLE);

                }
                catch (Exception e)
                {

                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });


        get_category.add(jsonArrayRequest);
    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
