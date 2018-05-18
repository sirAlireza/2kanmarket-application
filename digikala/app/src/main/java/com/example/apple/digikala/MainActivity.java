package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.MailTo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    SharedPreferences user_shared;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        check_login();




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawerLayout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        ImageView imageView=(ImageView)findViewById(R.id.app_cart);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
               Intent j=new Intent(MainActivity.this,shop_cart.class);
               startActivity(j);
            }
        });

        get_cat_index get_cat_index=new get_cat_index(MainActivity.this);

        get_amazing_product get_amazing_product=new get_amazing_product(this,R.id.amazing_product,Setting.url+"/api/get_android_amazing_product");

        get_order_product get_order_product=new get_order_product(this,R.id.order_product,Setting.url+"/api/get_android_order_product");

        get_order_product get_new_product=new get_order_product(this,R.id.new_product,Setting.url+"/api/get_android_new_product");

        get_order_product get_view_product=new get_order_product(this,R.id.view_product,Setting.url+"/api/get_android_view_product");

        get_slider get_slider=new get_slider(MainActivity.this);

    }
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void show_register(View view)
    {
        Intent j=new Intent(MainActivity.this,register.class);
        startActivity(j);
        overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

    }
    public void show_login(View view)
    {
        Intent j=new Intent(MainActivity.this,Login.class);
        startActivity(j);
        overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);
    }
    public void check_login()
    {
        SharedPreferences login=getSharedPreferences("digikal_userdata",MODE_PRIVATE);
        String token=login.getString("access_token","no");
        String email=login.getString("username","no");
        if(!token.equals("no"))
        {
            TextView register=(TextView)findViewById(R.id.register);
            TextView logins=(TextView)findViewById(R.id.login);
            TextView userpanel=(TextView)findViewById(R.id.userpanel);
            register.setVisibility(View.GONE);
            logins.setVisibility(View.GONE);
            userpanel.setText(email);
        }
    }

}
