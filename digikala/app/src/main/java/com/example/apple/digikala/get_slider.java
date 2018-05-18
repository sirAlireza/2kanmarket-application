package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by apple on 3/1/18.
 */

public class get_slider
{

    RequestQueue get_slider;
    protected String[] slider_img;
    protected int[] id_slider_shape;
    protected int h;
    private int x=0;
    Context cx;
    public get_slider(Context context)
    {
        cx=context;
        get_slider= Volley.newRequestQueue(context);
        get_slider_json();

    }

    public void get_slider_json()
    {
        final ViewPager viewPager=((Activity) cx).findViewById(R.id.slider);

        String url=Setting.url+"/api/get_android_slider";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    slider_img=new String[response.length()];
                    for (int i=0;i<slider_img.length;i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        slider_img[i]=jsonObject.getString("img");
                    }

                    final LinearLayout linearLayout=((Activity) cx).findViewById(R.id.slider_shape_list);
                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(Setting.pxfromdp(cx,10),Setting.pxfromdp(cx,10));
                    layoutParams.setMargins(0,0,3,0);
                    id_slider_shape=new int[slider_img.length];
                    for (int g=0;g<slider_img.length;g++)
                    {
                        int id=View.generateViewId();
                        id_slider_shape[g]=id;
                        View view=new View(cx);
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


                    get_slider.Slider slider=new Slider(cx,slider_img,id_slider_shape);
                    viewPager.setAdapter(slider);
                    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                            for (int g=0;g<id_slider_shape.length;g++)
                            {
                                View shape_view=((Activity) cx).findViewById(id_slider_shape[g]);
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

                    Setting.show_content(cx);

                    final Handler handler=new Handler();
                    final Thread thread=new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            h=0;
                            boolean x=true;
                            while (x)
                            {
                                try
                                {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run()
                                        {
                                            viewPager.setCurrentItem(h);

                                            for (int g=0;g<id_slider_shape.length;g++)
                                            {
                                                View view=((Activity) cx).findViewById(id_slider_shape[g]);
                                                if(g==h)
                                                {
                                                    view.setBackgroundResource(R.drawable.slider_shape1);
                                                }
                                                else
                                                {
                                                    view.setBackgroundResource(R.drawable.slider_shape2);
                                                }
                                            }
                                            h++;
                                        }
                                    });
                                    Thread.sleep(5000);

                                    if(h==slider_img.length)
                                    {
                                        h=0;
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                            }
                        }
                    });
                    thread.start();
                }
                catch (Exception e)
                {
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        get_slider.add(jsonArrayRequest);

    }
    public class Slider extends PagerAdapter
    {
        Context context;
        String[] img_url;
        RequestQueue requestQueue;
        ImageView imageView;
        int[] id_slider_shape;
        public Slider(Context cx, String[] img, int[] slider_shape)
        {
            img_url=img;
            id_slider_shape=slider_shape;
            context=cx;
        }

        @Override
        public int getCount() {
            return img_url.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==(RelativeLayout)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            View view= LayoutInflater.from(context).inflate(R.layout.slider,null);
            imageView=(ImageView)view.findViewById(R.id.slide_img);
            requestQueue= Volley.newRequestQueue(context);
            show_img(position,imageView);
            container.addView(view);
            return view;
        }
        public void show_img(int i, final ImageView img)
        {
            String url=Setting.url+"/upload/"+img_url[i];
            ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response)
                {
                    try
                    {
                        img.setImageBitmap(response);
                    }
                    catch (Exception e)
                    {
                    }
                }
            }, 0, 0, null, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            requestQueue.add(imageRequest);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //container.removeView((RelativeLayout)object);
        }
    }

}
