package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipSession;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 3/1/18.
 */

public class get_cat_index
{
    RequestQueue requestQueue;
    public List<cat> catList;
    get_cat_index.CatAdapter catAdapter;
    Context cx;
    public get_cat_index(Context context)
    {
        cx=context;
        catList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(context);
        get_json_data();
        RecyclerView recyclerView=((Activity) context).findViewById(R.id.cat_index_list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        catAdapter=new CatAdapter(catList,context);
        recyclerView.setAdapter(catAdapter);
    }
    public void get_json_data()
    {
        String url=Setting.url+"/api/get_android_cat_index";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        cat cat=new cat(jsonObject.get("cat_name").toString(),jsonObject.getInt("id"));
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
    public class cat
    {
        private String cat_name;
        private int cat_id;
        public cat(String name,int id)
        {
            cat_name=name;
            cat_id=id;
        }
        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public int getCat_id() {
            return cat_id;
        }

        public void setCat_id(int cat_id) {
            this.cat_id = cat_id;
        }
    }


    public class CatAdapter extends RecyclerView.Adapter<MyViewHolder>
    {
        private List<cat> catList;
        Context context;
        public CatAdapter(List<cat> list,Context cx)
        {
            catList=list;
            context=cx;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10,0,0,0);
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_index,null);
            view.setLayoutParams(layoutParams);
            return new get_cat_index.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            final cat cat=catList.get(position);
            holder.cat_text.setText(cat.getCat_name());
            holder.cat_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent j=new Intent(context,show_cat1.class);
                    j.putExtra("cat_id",cat.getCat_id());
                    j.putExtra("position",position);
                    context.startActivity(j);
                    ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                }
            });
        }

        @Override
        public int getItemCount() {
            return catList.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView cat_text;
        public MyViewHolder(View view)
        {
            super(view);

            cat_text=(TextView)view.findViewById(R.id.cat_name);
        }
    }
}
