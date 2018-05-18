package com.example.apple.digikala;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class show_product extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        final ImageView cart_ic=(ImageView)findViewById(R.id.cart_img);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);

        Drawable drawable= ContextCompat.getDrawable(this,R.drawable.ic_dots_vertical);
        toolbar.setOverflowIcon(drawable);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Bundle bundle=getIntent().getExtras();

        int product_id=bundle.getInt("product_id",0);
        final String product_title=bundle.getString("product_title");
        if(product_id>0)
        {
            new get_product_data(product_id,this);
        }

        final ScrollView scrollView=(ScrollView)findViewById(R.id.scrollView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {


                int a=scrollView.getScrollY()*2;
                int color= Color.argb(a,229,57,53);
                int color2= Color.argb(255,229,57,53);
                if(a>150)
                {
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_left2);
                    Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_dots_vertical2);
                    toolbar.setOverflowIcon(drawable);
                    cart_ic.setImageResource(R.drawable.ic_action_cart);
                    getSupportActionBar().setTitle(product_title);
                }
                else
                {
                    toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
                    Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_dots_vertical);
                    toolbar.setOverflowIcon(drawable);
                    cart_ic.setImageResource(R.drawable.ic_cart_outline2);
                    getSupportActionBar().setTitle("");
                }

                if(a>255)
                {
                    toolbar.setBackgroundColor(color2);

                }
                else
                {
                    toolbar.setBackgroundColor(color);
                }

            }
        });

    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product, menu);
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
        return super.onOptionsItemSelected(item);
    }
}
