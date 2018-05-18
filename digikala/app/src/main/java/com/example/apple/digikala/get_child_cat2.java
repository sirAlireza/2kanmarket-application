package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 3/11/18.
 */

public class get_child_cat2
{
    RequestQueue requestQueue;
    public List<Cat> catList;
    CatAdapter catAdapter;
    int cat2_id,cat1_id;
    public get_child_cat2(Context context,int c_id1,int c_id2)
    {
        cat1_id=c_id1;
        cat2_id=c_id2;
        catList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(context);
        get_json_data();
        catAdapter=new CatAdapter(catList,context);
        RecyclerView recyclerView=((Activity) context).findViewById(R.id.cat_child_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(context,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(catAdapter);
    }
    public void get_json_data()
    {
        String url=Setting.url+"/api/get_android_child_cat2/"+cat2_id;
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        Cat cat=new Cat(jsonObject.get("cat_name").toString(),jsonObject.getString("cat_child"),jsonObject.getInt("id"));
                        catList.add(cat);
                    }
                    catAdapter.notifyDataSetChanged();
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
    class  CatAdapter extends RecyclerView.Adapter<MyViewHolder>
    {
        private List<Cat> catList;
        Context context;
        public CatAdapter(List<Cat> list,Context cx)
        {
            catList=list;
            context=cx;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setMargins(10,0,0,0);
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.child_cat2_row,null);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position)
        {
            final Cat cat=catList.get(position);
            holder.cat_name.setText(cat.getCat_name());
            if(cat.getCat_child().equals("no"))
            {
                holder.cat_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {

                        SharedPreferences sharedPreferences=context.getSharedPreferences("digikala_filter_product", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();
                        Intent j=new Intent(context,product_list.class);
                        j.putExtra("cat1_id",cat1_id);
                        j.putExtra("cat2_id",cat2_id);
                        j.putExtra("cat3_id",cat.getCat_id());
                        j.putExtra("cat_name",cat.getCat_name());
                        context.startActivity(j);
                        ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                    }
                });
                holder.cat_child.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return catList.size();
        }
    }
    class Cat
    {
        String cat_name,cat_child;
        int cat_id;
        public Cat(String name,String child,int id)
        {
            cat_name=name;
            cat_child=child;
            cat_id=id;
        }

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getCat_child() {
            return cat_child;
        }

        public void setCat_child(String cat_child) {
            this.cat_child = cat_child;
        }

        public int getCat_id() {
            return cat_id;
        }

        public void setCat_id(int cat_id) {
            this.cat_id = cat_id;
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView cat_name;
        ImageView cat_child;
        public MyViewHolder(View view)
        {
            super(view);

            cat_name=(TextView)view.findViewById(R.id.cat_name);
            cat_child=(ImageView) view.findViewById(R.id.cat_child);
        }
    }
}
