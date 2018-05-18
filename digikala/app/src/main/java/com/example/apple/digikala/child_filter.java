package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.DataTruncation;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 3/12/18.
 */

public class child_filter
{

    JSONObject jsonObject;
    Context context;
    int size;
    List<Filter> filterNames;
    ChildFilterAdapter childFilterAdapter;
    SharedPreferences sharedPreferences;
    int filed;
    public child_filter(final JSONObject jsonObject, final Context context, int j)
    {
        this.jsonObject = jsonObject;
        this.context = context;

        filterNames=new ArrayList<>();

        String n="child_filter_"+j;
        try {

            JSONArray filter=jsonObject.getJSONArray(n);
            this.size=filter.length();

            String f_id="0";
            for (int i=0;i<filter.length();i++)
            {
                JSONObject jsonObject1=filter.getJSONObject(i);
                if(jsonObject1.has("id"))
                {
                    f_id=jsonObject1.getString("id");
                }
                Filter filter1=new Filter(jsonObject1.getString("name"),f_id);

                filterNames.add(filter1);
                filed=jsonObject1.getInt("filed");
            }
            RelativeLayout price_layout=((Activity) context).findViewById(R.id.price_layout);

            if(filed==1)
            {
                price_layout.setVisibility(View.GONE);
                GridLayout color_layout=((Activity) context).findViewById(R.id.color_layout);
                color_layout.setVisibility(View.GONE);
                RecyclerView recyclerView=((Activity) context).findViewById(R.id.child_filter_list);
                recyclerView.setVisibility(View.VISIBLE);
                childFilterAdapter=new ChildFilterAdapter(filterNames);
                recyclerView.setAdapter(childFilterAdapter);
                RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(layoutManager);
                childFilterAdapter.notifyDataSetChanged();
            }
            else if(filed==2)
            {
                price_layout.setVisibility(View.GONE);
                RecyclerView recyclerView=((Activity) context).findViewById(R.id.child_filter_list);
                recyclerView.setVisibility(View.GONE);
                int total_with=context.getResources().getDisplayMetrics().widthPixels;
                int w1=Setting.pxfromdp(context,150);
                float w=total_with-w1;
                float e=Setting.pxfromdp(context,50);
                float r=w/e;
                int n1=(int )r;
                GridLayout color_layout=((Activity) context).findViewById(R.id.color_layout);
                color_layout.removeAllViews();
                color_layout.setVisibility(View.VISIBLE);
                color_layout.setColumnCount(n1);
                final Map<Integer,String> colors_id=new HashMap<Integer, String>();
                final Map<Integer,String> colors_code=new HashMap<Integer, String>();

                for (int x=0;x<filterNames.size();x++)
                {
                    View view=LayoutInflater.from(context).inflate(R.layout.child_filter_row2,null);
                    String[] c=filterNames.get(x).getChild_name().split("@");
                    if(c.length==2)
                    {
                        final TextView color_name=(TextView)view.findViewById(R.id.child_filter_name);
                        color_name.setText(c[0]);
                        int id=View.generateViewId();
                        View view1=(View)view.findViewById(R.id.filter_view);
                        view1.setId(id);

                        colors_id.put(id,filterNames.get(x).getFilter_id());
                        colors_code.put(id,c[1]);

                        GradientDrawable gd=new GradientDrawable();
                        gd.setCornerRadius(30);
                        String color="#"+c[1];
                        gd.setColor(Color.parseColor(color));
                        if(c[1].equals("FFFFFF"))
                        {
                            gd.setStroke(1,Color.BLACK);
                        }
                        gd.setSize(Setting.pxfromdp(context,30),Setting.pxfromdp(context,30));
                        view1.setBackground(gd);

                        view1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {

                                sharedPreferences=context.getSharedPreferences("digikala_filter_product", Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor=sharedPreferences.edit();
                                String filter_id=sharedPreferences.getString("filter_id","");

                                String f_id=colors_id.get(v.getId())+",";
                                if(filter_id.contains(f_id))
                                {
                                    GradientDrawable gd=new GradientDrawable();
                                    if(colors_code.get(v.getId()).equals("FFFFFF"))
                                    {
                                        gd.setStroke(1,Color.BLACK);
                                    }
                                    else
                                    {
                                        gd.setStroke(0,Color.WHITE);
                                    }

                                    gd.setCornerRadius(30);
                                    String color="#"+colors_code.get(v.getId());
                                    gd.setColor(Color.parseColor(color));
                                    v.setBackground(gd);

                                    String new_id=filter_id.replace(f_id,"");
                                    editor.putString("filter_id",new_id);
                                }
                                else
                                {
                                    GradientDrawable gd=new GradientDrawable();
                                    gd.setStroke(1,Color.BLUE);
                                    gd.setCornerRadius(30);
                                    String color="#"+colors_code.get(v.getId());
                                    gd.setColor(Color.parseColor(color));
                                    v.setBackground(gd);

                                    String new_id=filter_id+f_id;
                                    editor.putString("filter_id",new_id);
                                }
                                editor.commit();


                                String fgjhfh=sharedPreferences.getString("filter_id","a");

                               Log.i("filters_id",fgjhfh);
                            }
                        });
                        color_layout.addView(view);
                    }
                }

            }
            else if(filed==3)
            {
                RecyclerView recyclerView=((Activity) context).findViewById(R.id.child_filter_list);
                recyclerView.setVisibility(View.GONE);

                price_layout.removeAllViews();
                price_layout.setVisibility(View.VISIBLE);

                View view=LayoutInflater.from(context).inflate(R.layout.price_list,null);
                price_layout.addView(view);

                final CardView cardMinPrice=((Activity) context).findViewById(R.id.cardMinPrice);
                final CardView cardMaxPrice=((Activity) context).findViewById(R.id.cardMaxPrice);

                final TextView set_min_price=((Activity) context).findViewById(R.id.set_min_price);
                set_min_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cardMinPrice.setVisibility(View.VISIBLE);
                        cardMaxPrice.setVisibility(View.GONE);

                    }
                });

                final TextView set_max_price=((Activity) context).findViewById(R.id.set_max_price);
                set_max_price.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        cardMaxPrice.setVisibility(View.VISIBLE);
                        cardMinPrice.setVisibility(View.GONE);
                    }
                });
                final LinearLayout minprice=((Activity) context).findViewById(R.id.minprice);
                LinearLayout maxprice=((Activity) context).findViewById(R.id.maxprice);
                final Typeface typeface=Typeface.createFromAsset(context.getAssets(),"fonts/iranSansWeb.ttf");
                DecimalFormat format=new DecimalFormat("###,###");
                for (int z=0;z<filterNames.size();z++)
                {

                    final String a=format.format(Integer.valueOf(filterNames.get(z).getChild_name()))+ " تومان";
                    TextView p=new TextView(context);
                    p.setText(a);
                    p.setTextSize(13);
                    p.setTypeface(typeface);
                    p.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cardMinPrice.setVisibility(View.GONE);
                            set_min_price.setText(a);
                        }
                    });
                    minprice.addView(p);

                }

                for (int z=0;z<filterNames.size();z++)
                {
                    final String a=format.format(Integer.valueOf(filterNames.get(z).getChild_name()))+ " تومان";
                    TextView p=new TextView(context);
                    p.setText(a);
                    p.setTextSize(13);
                    p.setTypeface(typeface);
                    p.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cardMaxPrice.setVisibility(View.GONE);
                            set_max_price.setText(a);
                        }
                    });
                    maxprice.addView(p);
                }
            }


        }
        catch (Exception e)
        {

        }




    }

    class ChildFilterAdapter extends RecyclerView.Adapter<MyViewHolder>
    {
        List<Filter> filterList;
        public ChildFilterAdapter(List<Filter> list)
        {
            filterList=list;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //layoutParams.setMargins(0,5,0,5);
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.child_filter_row,null);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            final Filter filter=filterList.get(position);
            sharedPreferences=context.getSharedPreferences("digikala_filter_product", Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor=sharedPreferences.edit();
            String filter_id=sharedPreferences.getString("filter_id","");

            String id=filter.getFilter_id()+",";
            if(filter_id.contains(id))
            {
                holder.child_filter_checkBox.setChecked(true);
            }
            holder.child_filter_name.setText(filter.getChild_name());
            holder.child_filter_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                   String filter_id=sharedPreferences.getString("filter_id","");

                    if(holder.child_filter_checkBox.isChecked())
                    {
                        holder.child_filter_checkBox.setChecked(false);
                        String a=filter.getFilter_id()+",";
                        String new_id=filter_id.replace(a,"");
                        editor.putString("filter_id",new_id);
                    }
                    else
                    {
                        holder.child_filter_checkBox.setChecked(true);
                        String new_id=filter_id+filter.getFilter_id()+",";
                        editor.putString("filter_id",new_id);
                    }
                    editor.commit();


                }
            });
            holder.child_filter_checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    String filter_id=sharedPreferences.getString("filter_id","");

                    if(holder.child_filter_checkBox.isChecked())
                    {
                        String new_id=filter_id+filter.getFilter_id()+",";
                        editor.putString("filter_id",new_id);
                    }
                    else
                    {
                        String a=filter.getFilter_id()+",";
                        String new_id=filter_id.replace(a,"");
                        editor.putString("filter_id",new_id);
                    }
                    editor.commit();

                    String fgjhfh=sharedPreferences.getString("filter_id","a");

                    Log.i("filters_id",fgjhfh);
                }
            });
        }

        @Override
        public int getItemCount() {
            return size;
        }
    }
    class Filter
    {
        String child_name;
        String filter_id;
        public Filter(String child_name,String id) {
            this.child_name = child_name;
            filter_id=id;
        }

        public String getFilter_id() {
            return filter_id;
        }

        public void setFilter_id(String filter_id) {
            this.filter_id = filter_id;
        }

        public String getChild_name() {
            return child_name;
        }

        public void setChild_name(String child_name) {
            this.child_name = child_name;
        }
    }
    class  MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView child_filter_name;
        CheckBox child_filter_checkBox;
        View filter_view;
        public MyViewHolder(View view)
        {
            super(view);
            child_filter_name=(TextView)view.findViewById(R.id.child_filter_name);

            if(filed==1)
            {
                child_filter_checkBox=(CheckBox)view.findViewById(R.id.child_filter_checkbox);
            }
            else
            {
                filter_view=(View)view.findViewById(R.id.filter_view);
            }
        }
    }
}
