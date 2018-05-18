package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaCas;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;

/**
 * Created by apple on 3/3/18.
 */

public class get_product_data
{
    RequestQueue requestQueue,requestScore;
    Context context;
    protected int product_id,price,discounts;
    String[] image_url,download_img,color_name,color_code,service_name;
    int[] id_slider_shape,color_id,id_view,service_id;
    protected int int_color,active_color_id,active_service_id;
    String title,code,tozihat;
    protected  int m_text=0;
    protected int[][] layout_id;
    double[] score_value;
    public get_product_data(int id, Context c)
    {

        layout_id=new int[6][5];
        set_key_layout_id();
        product_id=id;
        context=c;
        requestQueue= Volley.newRequestQueue(context);
        get_data();
    }
    public void get_data()
    {
        String url=Setting.url+"/api/get_android_product_data/"+product_id;
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    JSONObject data=response.getJSONObject("data");
                    JSONArray images=response.getJSONArray("img");
                    JSONArray colors=response.getJSONArray("colors");
                    JSONArray service=response.getJSONArray("service");

                    price=data.getInt("price");

                    image_url=new String[images.length()];
                    download_img=new String[images.length()];
                    color_id=new int[colors.length()];
                    color_name=new String[colors.length()];
                    color_code=new String[colors.length()];
                    service_id=new int[service.length()];
                    service_name=new String[service.length()];
                    for (int i=0;i<images.length();i++)
                    {
                        JSONObject jsonObject=images.getJSONObject(i);
                        image_url[i]=jsonObject.getString("url");
                    }

                    for (int i=0;i<colors.length();i++)
                    {
                        JSONObject jsonObject=colors.getJSONObject(i);
                        color_id[i]=jsonObject.getInt("id");
                        color_name[i]=jsonObject.getString("color_name");
                        color_code[i]=jsonObject.getString("color_code");
                    }
                    for(int i=0;i<service.length();i++)
                    {
                        JSONObject jsonObject=service.getJSONObject(i);
                        service_name[i]=jsonObject.getString("service_name");
                        service_id[i]=jsonObject.getInt("service_id");
                    }

                    title=data.getString("title");
                    code=data.getString("code");
                    tozihat=data.getString("tozihat");
                    discounts=data.getInt("discounts");
                    set_data();

                    final LinearLayout linearLayout=((Activity) context).findViewById(R.id.slider_shape_list);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(Setting.pxfromdp(context,10),Setting.pxfromdp(context,10));
                    layoutParams.setMargins(0,0,3,0);
                    id_slider_shape=new int[image_url.length];
                    for (int g=0;g<image_url.length;g++)
                    {
                        int id=View.generateViewId();
                        id_slider_shape[g]=id;
                        View view=new View(context);
                        view.setLayoutParams(layoutParams);
                        if(g==0)
                        {
                            view.setBackgroundResource(R.drawable.slider_shape1);
                        }
                        else
                        {
                            view.setBackgroundResource(R.drawable.slider_shape2);
                        }

                        view.setId(id);
                        linearLayout.addView(view);
                    }
                    Show_product_images show_product_images=new Show_product_images(context);
                    ViewPager viewPager=((Activity) context).findViewById(R.id.product_images);
                    viewPager.setAdapter(show_product_images);
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            for (int g=0;g<id_slider_shape.length;g++)
                            {
                                View shape_view=((Activity) context).findViewById(id_slider_shape[g]);
                                if(g==position)
                                {
                                    shape_view.setBackgroundResource(R.drawable.slider_shape1);
                                }
                                else
                                {
                                    shape_view.setBackgroundResource(R.drawable.slider_shape2);
                                }
                            }
                        }

                        @Override
                        public void onPageSelected(int position) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });

                    add_color();
                    add_serice();


                    requestScore= Volley.newRequestQueue(context);
                    get_product_score();
                }
                catch (Exception e)
                {
                    Log.i("error","errorsss");
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
    public void set_data()
    {
        TextView product_title=((Activity) context).findViewById(R.id.product_title);
        product_title.setText(title);

        TextView product_code=((Activity) context).findViewById(R.id.product_code);
        product_code.setText(code);


        DecimalFormat price_format=new DecimalFormat("###,###");
        TextView product_price=((Activity) context).findViewById(R.id.product_price);
        product_price.setText(String.valueOf(price_format.format(price)));

        TextView product_price_type=((Activity) context).findViewById(R.id.product_price_type);
        product_price_type.setText("تومان");

        tozihat=tozihat.replace("<p>","");
        tozihat=tozihat.replace("</p>","");
        String[] t=tozihat.split("<!--more-->");
        final View more_text_line=((Activity) context).findViewById(R.id.more_text_line);
        if(t.length==2)
        {
            TextView tozihat1=((Activity) context).findViewById(R.id.tozihat1);
            tozihat1.setText( Html.fromHtml(t[0]));

            final TextView tozihat2=((Activity) context).findViewById(R.id.tozihat2);
            tozihat2.setText( Html.fromHtml(t[1]));

            final TextView more_text=((Activity) context).findViewById(R.id.more_text);
            more_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                   if(m_text==0)
                   {
                       m_text=1;
                       tozihat2.setVisibility(View.VISIBLE);
                       more_text.setText("کمتر");
                       more_text_line.setVisibility(View.GONE);
                   }
                   else
                   {
                       m_text=0;
                       more_text.setText("بیشتر");
                       tozihat2.setVisibility(View.GONE);
                       more_text_line.setVisibility(View.VISIBLE);
                   }

                }
            });
        }
        else
        {
            TextView more_text=((Activity) context).findViewById(R.id.more_text);
            more_text.setVisibility(View.GONE);

            more_text_line.setVisibility(View.GONE);

            TextView tozihat1=((Activity) context).findViewById(R.id.tozihat1);
            tozihat1.setText( Html.fromHtml(tozihat));

        }



    }
    public void add_color()
    {
        LinearLayout linearLayout=((Activity) context).findViewById(R.id.color_box);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,Setting.pxfromdp(context,10),0);
        id_view=new int[color_code.length];
        for(int i=0;i<color_code.length;i++)
        {
            int_color=i;
            int id=View.generateViewId();
            View view=LayoutInflater.from(context).inflate(R.layout.color_product,null);
            view.setLayoutParams(layoutParams);
            view.setId(id);
            id_view[i]=id;
            if(i==0)
            {
                active_color_id=color_id[0];
               view.setBackgroundResource(R.drawable.color_select_border);
            }
            else
            {
                view.setBackgroundResource(R.drawable.color_border);
            }
            TextView name=(TextView)view.findViewById(R.id.color_name);
            name.setText(color_name[i]);

            RelativeLayout view1=(RelativeLayout)view.findViewById(R.id.color_code_view);
            GradientDrawable gd=new GradientDrawable();
            gd.setCornerRadius(20);
            String c="#"+color_code[i];
            gd.setColor(Color.parseColor(c));
            if(color_code[i].equals("FFFFFF"))
            {
                gd.setStroke(1,Color.BLACK);
            }
            gd.setSize(Setting.pxfromdp(context,20),Setting.pxfromdp(context,20));
            view1.setBackground(gd);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int h=0;h<id_view.length;h++)
                    {
                        View view2=((Activity) context).findViewById(id_view[h]);
                        view2.setBackgroundResource(R.drawable.color_border);
                    }

                    v.setBackgroundResource(R.drawable.color_select_border);
                }
            });
            linearLayout.addView(view);
        }


    }

    public void add_serice()
    {
        String a="انتخاب گارانتی";
        if(service_name.length>0)
        {
            active_service_id=service_id[0];
            TextView textView=((Activity) context).findViewById(R.id.service_name);
            textView.setText(service_name[0]);

            TextView service_select_text1=((Activity) context).findViewById(R.id.service_select_text1);
            service_select_text1.setText(a);
            if(service_name.length>1)
            {
                TextView service_select_text2=((Activity) context).findViewById(R.id.service_select_text2);
                service_select_text2.setText("(تغییر گارانتی)");
            }

        }


        LinearLayout btn_cart=((Activity) context).findViewById(R.id.btn_cart);
        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Cart cart=new Cart(context);
                String url="";
                if(image_url.length>0)
                {

                    url=image_url[0];
                }
                String s_name="";
                if(service_name.length>0)
                {
                    s_name=service_name[0];
                }
                cart.add(title,code,url,price,active_color_id,active_service_id,product_id,color_code[0],color_name[0],discounts,s_name);
            }
        });

    }
    public class Show_product_images extends PagerAdapter
    {
        Context context;
        RequestQueue get_bitmap_img;
        private ImageView imageView;
        public Show_product_images(Context cx)
        {
            context=cx;
        }

        @Override
        public int getCount() {
            return image_url.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==(RelativeLayout)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view= LayoutInflater.from(context).inflate(R.layout.prodict_slider,null);
            imageView=(ImageView)view.findViewById(R.id.slide_img);
            get_bitmap_img= Volley.newRequestQueue(context);
            show_img(position,imageView);
            container.addView(view);
            return view;
        }
        public void show_img(final int i, final ImageView img)
        {
            if(download_img[i]==null)
            {
                String url=Setting.url+"/upload/"+image_url[i];
                download_img[i]=url;
                Log.i("url",url);
                ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response)
                    {
                        try
                        {
                            if(i==0)
                            {
                               RelativeLayout relativeLayout=((Activity) context).findViewById(R.id.progressBar_layout);
                               relativeLayout.removeAllViews();
                               relativeLayout.setVisibility(View.GONE);
                            }
                            img.setImageBitmap(response);

                        }
                        catch (Exception e)
                        {

                        }
                    }
                }, 600,600, null, null, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });

                get_bitmap_img.add(imageRequest);
            }

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

           // container.removeView((RelativeLayout)object);
        }
    }

    public void get_product_score()
    {
        String url=Setting.url+"/api/get_android_product_score/"+product_id;
        JsonObjectRequest jsonObject=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    score_value=new double[6];
                    score_value[0]=response.getDouble("1");
                    score_value[1]=response.getDouble("2");
                    score_value[2]=response.getDouble("3");
                    score_value[3]=response.getDouble("4");
                    score_value[4]=response.getDouble("5");
                    score_value[5]=response.getDouble("6");

                    float rating_value=Float.valueOf(response.getString("avg"));
                    RatingBar ratingBar=((Activity) context).findViewById(R.id.ratingBar);
                    ratingBar.setRating(rating_value);

                    TextView score_avg=((Activity) context).findViewById(R.id.score_avg);
                    score_avg.setText(response.getString("avg"));

                    TextView score_count=((Activity) context).findViewById(R.id.score_count);
                    score_count.setText(response.getString("number"));

                    int width=context.getResources().getDisplayMetrics().widthPixels;
                    int m1=Setting.pxfromdp(context,10);
                    int w=Setting.pxfromdp(context,150);
                    width=width-(4*m1)-w-25;
                    float a=width/5;
                    int b=(int)a;


                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(b,Setting.pxfromdp(context,10));
                    layoutParams.setMargins(5,0,0,0);

                    for (int i=0;i<layout_id.length;i++)
                    {
                        double v=score_value[i]-1;
                        for (int j=0;j<layout_id[i].length;j++)
                        {

                            RelativeLayout relativeLayout=((Activity) context).findViewById(layout_id[i][j]);
                            relativeLayout.setLayoutParams(layoutParams);
                            if(v>j)
                            {
                                View view1=new View(context);
                                view1.setLayoutParams(layoutParams);
                                view1.setBackgroundColor(Color.parseColor("#7CB342"));
                                relativeLayout.addView(view1);
                            }
                            else
                            {
                                double t=j-v;
                                t=1-t;
                                int g=(int) (b*t);
                                LinearLayout.LayoutParams layoutParams2=new LinearLayout.LayoutParams(g,Setting.pxfromdp(context,15));

                                View view1=new View(context);
                                view1.setLayoutParams(layoutParams2);
                                view1.setBackgroundColor(Color.parseColor("#7CB342"));
                                relativeLayout.addView(view1);
                            }
                        }
                    }


                    get_order_product get_new_product=new get_order_product(context,R.id.new_product,Setting.url+"/api/get_like_product/"+product_id);


                }
                catch (Exception e)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestScore.add(jsonObject);
    }
    public  void set_key_layout_id()
    {
        layout_id[0][0]=R.id.product_scrot_item1_1;
        layout_id[0][1]=R.id.product_scrot_item1_2;
        layout_id[0][2]=R.id.product_scrot_item1_3;
        layout_id[0][3]=R.id.product_scrot_item1_4;
        layout_id[0][4]=R.id.product_scrot_item1_5;


        layout_id[1][0]=R.id.product_scrot_item2_1;
        layout_id[1][1]=R.id.product_scrot_item2_2;
        layout_id[1][2]=R.id.product_scrot_item2_3;
        layout_id[1][3]=R.id.product_scrot_item2_4;
        layout_id[1][4]=R.id.product_scrot_item2_5;

        layout_id[2][0]=R.id.product_scrot_item3_1;
        layout_id[2][1]=R.id.product_scrot_item3_2;
        layout_id[2][2]=R.id.product_scrot_item3_3;
        layout_id[2][3]=R.id.product_scrot_item3_4;
        layout_id[2][4]=R.id.product_scrot_item3_5;

        layout_id[3][0]=R.id.product_scrot_item4_1;
        layout_id[3][1]=R.id.product_scrot_item4_2;
        layout_id[3][2]=R.id.product_scrot_item4_3;
        layout_id[3][3]=R.id.product_scrot_item4_4;
        layout_id[3][4]=R.id.product_scrot_item4_5;

        layout_id[4][0]=R.id.product_scrot_item5_1;
        layout_id[4][1]=R.id.product_scrot_item5_2;
        layout_id[4][2]=R.id.product_scrot_item5_3;
        layout_id[4][3]=R.id.product_scrot_item5_4;
        layout_id[4][4]=R.id.product_scrot_item5_5;

        layout_id[5][0]=R.id.product_scrot_item6_1;
        layout_id[5][1]=R.id.product_scrot_item6_2;
        layout_id[5][2]=R.id.product_scrot_item6_3;
        layout_id[5][3]=R.id.product_scrot_item6_4;
        layout_id[5][4]=R.id.product_scrot_item6_5;


    }
}
