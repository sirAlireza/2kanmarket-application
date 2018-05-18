package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 3/11/18.
 */

public class get_filter
{
    int cat1_id,cat2_id,cat3_id;
    RequestQueue requestQueue;
    Context context;
    List<FilterName> filterNames;
    int[] filter_name_id_list;
    protected  FilterNameAdapter filterNameAdapter;
    JSONObject dataresponse;
    int active_position=0;
    RecyclerView recyclerView;
    public get_filter(Context c,int cat1,int cat2,int cat3)
    {
        context=c;
        cat1_id=cat1;
        cat2_id=cat2;
        cat3_id=cat3;
        filterNames=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(context);
        get_json_data();
        recyclerView=((Activity) context).findViewById(R.id.filter_name);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

    }
    public void get_json_data()
    {
        String url=Setting.url+"/api/get_android_product_filter/"+cat1_id+"/"+cat2_id+"/"+cat3_id;
        JsonObjectRequest jsonArrayRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    dataresponse=response;
                    JSONArray filter=response.getJSONArray("filter");
                    filter_name_id_list=new int[filter.length()];
                    for (int i = 0; i<filter.length();i++)
                    {
                        FilterName filterName=new FilterName(filter.getString(i));
                        filterNames.add(filterName);
                        int id=View.generateViewId();
                        filter_name_id_list[i]=id;
                    }
                    filterNameAdapter=new FilterNameAdapter(context,filterNames);
                    recyclerView.setAdapter(filterNameAdapter);
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            for (int i=0;i<filter_name_id_list.length;i++)
                            {
                                TextView textView=((Activity) context).findViewById(filter_name_id_list[i]);
                                if(textView!=null && active_position!=i)
                                {
                                    textView.setBackgroundColor(Color.BLACK);
                                    textView.setTextColor(Color.WHITE);
                                }
                            }
                        }
                    });
                    child_filter child_filter=new child_filter(response,context,0);
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

    public class FilterNameAdapter extends RecyclerView.Adapter<MyViewHolder>
    {
        private List<FilterName> filterNames;
        Context context;
        public FilterNameAdapter(Context c,List<FilterName> list)
        {
            filterNames=list;
            context=c;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setMargins(0,5,0,5);
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_name,null);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0,7,0,7);

            FilterName filterName=filterNames.get(position);
            holder.filter_name.setText(filterName.getFilter_name());
            holder.filter_name.setId(filter_name_id_list[position]);

            if(position>0)
            {
                holder.filter_name.setLayoutParams(layoutParams);
            }

            if(position==0)
            {
                holder.filter_name.setBackgroundColor(Color.WHITE);
                holder.filter_name.setTextColor(Color.BLACK);
            }
            holder.filter_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    active_position=position;
                    for (int i=0;i<filter_name_id_list.length;i++)
                    {
                        TextView textView=((Activity) context).findViewById(filter_name_id_list[i]);
                        if(textView!=null)
                        {
                            textView.setBackgroundColor(Color.BLACK);
                            textView.setTextColor(Color.WHITE);
                            v.setBackgroundColor(Color.WHITE);
                            holder.filter_name.setTextColor(Color.BLACK);
                        }


                    }
                    child_filter child_filter=new child_filter(dataresponse,context,position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return filterNames.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView filter_name;
        public MyViewHolder(View view)
        {
            super(view);
            filter_name=(TextView)view.findViewById(R.id.filter_name_text);
        }
    }

    public class FilterName
    {
        String filter_name;
        public FilterName(String name)
        {
            filter_name=name;
        }

        public String getFilter_name() {
            return filter_name;
        }

        public void setFilter_name(String filter_name) {
            this.filter_name = filter_name;
        }
    }
}
