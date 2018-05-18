package com.example.apple.digikala;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Search_Product
{
    RequestQueue requestQueue,getProductImage;
    String[] title,code,price;
    int[] id;
    int cat_id;
    Context context;
    ImageView[] p_img;
    String[] img_url,data_title,data_code,data_price;
    String is_filter;
    public Search_Product(int cat_id, Context context,String f)
    {
        this.cat_id = cat_id;
        this.context = context;
        is_filter=f;
        if(f.equals("no"))
        {
            get_product_data();
        }
        else
        {
            get_filter_product_data();
        }

    }
    public void get_product_data()
    {
        String url=Setting.url+"/api/get_android_product_cat/"+cat_id;
        requestQueue= Volley.newRequestQueue(context);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    p_img=new ImageView[response.length()];
                    img_url=new String[response.length()];
                    data_title=new String[response.length()];
                    data_code=new String[response.length()];
                    data_price=new String[response.length()];
                    int margin=Setting.pxfromdp(context,30);
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        img_url[i]=jsonObject.getString("img");
                        data_title[i]=jsonObject.getString("title");
                        data_code[i]=jsonObject.getString("code");
                        data_price[i]=jsonObject.getString("price");
                    }
                    set_view1();

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
        requestQueue.add(jsonArrayRequest);
    }
    public void set_product_img(final int img_key)
    {

            getProductImage= Volley.newRequestQueue(context);
            String url=Setting.url+"/upload/"+img_url[img_key];
            ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response)
                {
                    try
                    {
                        p_img[img_key].setImageBitmap(response);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }, Setting.pxfromdp(context,100), 0, null, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            getProductImage.add(imageRequest);

    }

    public void set_view1()
    {

        GridLayout product_list=((Activity) context).findViewById(R.id.product_list);
        product_list.removeAllViews();
        product_list.setColumnCount(1);
        DecimalFormat decimalFormat=new DecimalFormat("###,###");
        Setting.show_content(context);

        for (int i=0;i<data_title.length;i++)
        {
            View view=LayoutInflater.from(context).inflate(R.layout.search_product_layout,null);
            TextView title=(TextView)view.findViewById(R.id.search_product_title);
            title.setText(data_title[i]);

            ImageView img=(ImageView)view.findViewById(R.id.search_product_img);
            p_img[i]=img;

            TextView code=(TextView)view.findViewById(R.id.search_product_code);
            String c=data_code[i];
            if(c.length()>30)
            {
                c=c.substring(0,30)+"...";
            }
            code.setText(c);


            String p=decimalFormat.format(Integer.valueOf(data_price[i]))+" تومان ";
            TextView search_product_price=(TextView)view.findViewById(R.id.search_product_price);
            search_product_price.setText(p);
            product_list.addView(view);
            set_product_img(i);
        }


    }
    public void set_view2()
    {
        int total_width=context.getResources().getDisplayMetrics().widthPixels;
        total_width=total_width-Setting.pxfromdp(context,16);
        int r=Setting.pxfromdp(context,165);
        double count=total_width/r;
        int n=(int)count;

        GridLayout product_list=((Activity) context).findViewById(R.id.product_list);
        product_list.removeAllViews();
        product_list.setColumnCount(n);
        DecimalFormat decimalFormat=new DecimalFormat("###,###");
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(Setting.pxfromdp(context,165), ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i=0;i<data_title.length;i++)
        {
            View view=LayoutInflater.from(context).inflate(R.layout.product_index2,null);
            view.setLayoutParams(layoutParams);
            TextView title=(TextView)view.findViewById(R.id.product_title);
            title.setText(data_title[i]);
            String p=decimalFormat.format(Integer.valueOf(data_price[i]))+" تومان ";
            TextView search_product_price=(TextView)view.findViewById(R.id.product_price);
            search_product_price.setText(p);

            ImageView img=(ImageView)view.findViewById(R.id.product_img);
            p_img[i]=img;

            product_list.addView(view);
            set_product_img(i);
        }
    }

    public void get_filter_product_data()
    {
        requestQueue= Volley.newRequestQueue(context);
        String url=Setting.url+"/api/get_android_filter_product";


        SharedPreferences sharedPreferences=context.getSharedPreferences("digikala_filter_product", Context.MODE_PRIVATE);
        String filter_id=sharedPreferences.getString("filter_id","");


        Map<String,String> params=new HashMap<String, String>();
        params.put("filter_product",filter_id);
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
              try
              {
                  JSONArray data=response.getJSONArray("data");
                  p_img=new ImageView[data.length()];
                  img_url=new String[data.length()];
                  data_title=new String[data.length()];
                  data_code=new String[data.length()];
                  data_price=new String[data.length()];
                  int margin=Setting.pxfromdp(context,30);
                  for (int i=0;i<data.length();i++)
                  {
                      JSONObject product=data.getJSONObject(i);
                      data_title[i]=product.getString("title");
                      data_code[i]=product.getString("code");
                      data_price[i]=product.getString("price");

                      JSONObject img=product.getJSONObject("get_img");
                      img_url[i]=img.getString("url");
                  }
                  set_view1();

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
        requestQueue.add(jsonArrayRequest);
    }
}
