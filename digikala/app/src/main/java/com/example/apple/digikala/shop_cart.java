package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.text.DecimalFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class shop_cart extends AppCompatActivity {

    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        requestQueue= Volley.newRequestQueue(this);
        int t_price=0;
        DecimalFormat price_format=new DecimalFormat("###,###");

        SharedPreferences shared_cart=getSharedPreferences("idigikal_cart", Context.MODE_PRIVATE);
        String k=shared_cart.getString("key","no");
        if(!k.equals("no"))
        {
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(Setting.pxfromdp(this,15),Setting.pxfromdp(this,10),Setting.pxfromdp(this,15),Setting.pxfromdp(this,10));
            LinearLayout cart_content=(LinearLayout)findViewById(R.id.cart_content);
            String keys[]=k.split(",");
            for (int i=0;i<keys.length;i++)
            {
                Log.i("key",keys[i]);
                View view= LayoutInflater.from(this).inflate(R.layout.cart_row,null);
                view.setLayoutParams(params);
                String name="digikala_product_"+keys[i];
                SharedPreferences shared_cart_product=getSharedPreferences(name,Context.MODE_PRIVATE);

                TextView product_title=(TextView)view.findViewById(R.id.product_title);
                product_title.setText(shared_cart_product.getString("product_title",null));

                TextView product_code=(TextView)view.findViewById(R.id.product_code);
                product_code.setText(shared_cart_product.getString("product_code",null));

                TextView color_name=(TextView)view.findViewById(R.id.color_name);
                color_name.setText(shared_cart_product.getString("color_name",null));


                int price=shared_cart_product.getInt("product_price",0)+shared_cart_product.getInt("discounts",0);
                String p1=price_format.format(price)+ " تومان";
                TextView price_text=(TextView)view.findViewById(R.id.price);
                price_text.setText(p1);

                String p2=price_format.format(shared_cart_product.getInt("discounts",0))+ " تومان";

                TextView discounts=(TextView)view.findViewById(R.id.discounts);
                discounts.setText(p2);


                String p3=price_format.format(shared_cart_product.getInt("product_price",0))+ " تومان";

                TextView final_price=(TextView)view.findViewById(R.id.final_price);
                final_price.setText(p3);

                t_price+=shared_cart_product.getInt("product_price",0);

                RelativeLayout view1=(RelativeLayout)view.findViewById(R.id.color_code_box);
                GradientDrawable gd=new GradientDrawable();
                gd.setCornerRadius(20);
                String c="#"+shared_cart_product.getString("color_code","FFFFFF");
                gd.setColor(Color.parseColor(c));
                if(shared_cart_product.getString("color_code","FFFFFF").equals("FFFFFF"))
                {
                    gd.setStroke(1,Color.BLACK);
                }
                gd.setSize(Setting.pxfromdp(this,20),Setting.pxfromdp(this,20));
                view1.setBackground(gd);


                TextView service_name=(TextView)view.findViewById(R.id.service_name);
                service_name.setText(shared_cart_product.getString("service_name",null));


                TextView product_number=(TextView)view.findViewById(R.id.product_number);
                product_number.setText(String.valueOf(shared_cart_product.getInt("product_number",1)));

                final ImageView cart_img=(ImageView)view.findViewById(R.id.cart_img);

                String url=Setting.url+"/upload/"+shared_cart_product.getString("product_image_url","");

                Log.i("url",url);

                ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response)
                    {
                        try
                        {
                            cart_img.setImageBitmap(response);
                        }
                        catch (Exception e)
                        {

                        }
                    }
                }, 0, 0, null, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }

                });

                requestQueue.add(imageRequest);
                cart_content.addView(view);
            }


            String p4=price_format.format(t_price)+ " تومان";

            TextView total_price=(TextView)findViewById(R.id.total_price);
            total_price.setText(p4);

            LinearLayout btn_addOrder=(LinearLayout)findViewById(R.id.btn_addOrder);
            btn_addOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent j=new Intent(shop_cart.this,order1.class);
                    startActivity(j);
                }
            });
        }





    }
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
