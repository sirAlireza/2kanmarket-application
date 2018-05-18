package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class get_child_cat1
{
    Context context;
    int[] cat_id;
    CatAdapter catAdapter;
    CatPagerAdapter pagerAdapter;
    RequestQueue get_cat_img;
    int cat1_id;
    public get_child_cat1(Context c, int[] cat_list_id, int size, final TabLayout tab,int cat_index,int c_id1)
    {
        context=c;
        cat1_id=c_id1;
        pagerAdapter=new CatPagerAdapter(size,cat_list_id);
        final ViewPager viewPager=((Activity) context).findViewById(R.id.cat_viewpager);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(cat_index);
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                int p=tab.getPosition();
                Log.i("p",String.valueOf(p));
               viewPager.setCurrentItem(p);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {

                tab.post(new Runnable() {
                    @Override
                    public void run() {

                        int p=((ViewGroup) tab.getChildAt(0)).getChildAt(position).getLeft();
                        tab.setScrollX(p);
                        tab.setScrollPosition(position,0,true);
                    }
                });
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    class cat
    {
        String cat_name,cat_img_url;
        int id;

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getCat_img_url() {
            return cat_img_url;
        }

        public void setCat_img_url(String cat_img_url) {
            this.cat_img_url = cat_img_url;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public cat(String name, String url, int i)
        {
            cat_name=name;
            cat_img_url=url;
            id=i;
        }
    }
    class CatPagerAdapter extends PagerAdapter
    {
        int size;

        public CatPagerAdapter(int s,int[] cat_list_id)
        {
            size=s;
            cat_id=cat_list_id;
        }
        @Override
        public int getCount() {
            return size;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view==(RelativeLayout)object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.recycle_childcat1,null);
            get_cat get_cat=new get_cat(position,view);
            container.addView(view);
            return view;
        }
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((RelativeLayout)object);
        }
    }

    class get_cat
    {
        public List<cat> catList;
        int position,view_id;
        RequestQueue requestQueue;
        View view;
        public get_cat(int p,View v)
        {
            catList=new ArrayList<>();
            position=p;
            view=v;
            requestQueue= Volley.newRequestQueue(context);
            get_json_data();
        }
        public void get_json_data()
        {
            String url=Setting.url+"/api/get_android_child_cat1/"+cat_id[position];
            JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response)
                {
                    try
                    {
                        for (int i=0;i<response.length();i++)
                        {
                            JSONObject jsonObject=response.getJSONObject(i);
                            cat add_cat=new cat(jsonObject.getString("cat_name"),jsonObject.getString("img"),jsonObject.getInt("id"));
                            catList.add(add_cat);
                        }

                        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.RecyclerView);
                        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
                        catAdapter=new CatAdapter(catList,context);
                        recyclerView.setAdapter(catAdapter);

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

    public class CatAdapter extends RecyclerView.Adapter<MyViewHolder>
    {
        private List<cat> catList;
        Context context;
        public CatAdapter(List<cat> list, Context cx)
        {
            catList=list;
            context=cx;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(Setting.pxfromdp(context,20),Setting.pxfromdp(context,0),Setting.pxfromdp(context,20),Setting.pxfromdp(context,0));
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.child_cat1_row,null);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            final cat cat=catList.get(position);
            holder.cat_name.setText(cat.getCat_name());
            holder.cat_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent j=new Intent(context,show_cat2.class);
                    j.putExtra("cat1_id",cat1_id);
                    j.putExtra("cat2_id",cat.getId());
                    j.putExtra("cat_name",cat.getCat_name());
                    context.startActivity(j);
                    ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                }
            });
            holder.cat_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent j=new Intent(context,show_cat2.class);
                    j.putExtra("cat1_id",cat1_id);
                    j.putExtra("cat2_id",cat.getId());
                    j.putExtra("cat_name",cat.getCat_name());
                    context.startActivity(j);
                    ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                }
            });

            get_download_img(cat.getCat_img_url(),holder.cat_img);
        }

        @Override
        public int getItemCount() {
            return catList.size();
        }
    }
    public void get_download_img(String url, final ImageView imageView)
    {
        String img_url=Setting.url+"/upload/"+url;
        get_cat_img= Volley.newRequestQueue(context);
        ImageRequest imageRequest=new ImageRequest(img_url, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response)
            {
                try
                {
                    imageView.setImageBitmap(response);
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

        get_cat_img.add(imageRequest);
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView cat_name;
        ImageView cat_img;
        public MyViewHolder(View view)
        {
            super(view);
            cat_name=(TextView)view.findViewById(R.id.cat_name);
            cat_img=(ImageView) view.findViewById(R.id.cat_img);
        }
    }
}
