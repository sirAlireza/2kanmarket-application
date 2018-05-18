package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.os.Handler;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 3/1/18.
 */

public class get_amazing_product
{
    RequestQueue requestQueue,getProductImage;
    int[] amazing_time;
    public List<product> productList;
    get_amazing_product.ProductAdapter productAdapter;
    protected String request_url;
    protected Context cx;
    protected int time=0;
    public get_amazing_product(Context context, int id, String url)
    {
        request_url=url;
        cx=context;
        productList=new ArrayList<>();
        requestQueue= Volley.newRequestQueue(context);
        getProductImage= Volley.newRequestQueue(context);
        get_json_data();
        RecyclerView recyclerView=((Activity) context).findViewById(id);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        productAdapter=new ProductAdapter(productList,context);
        recyclerView.setAdapter(productAdapter);
    }

    public void get_json_data()
    {
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, request_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    amazing_time=new int[response.length()];
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        amazing_time[i]=jsonObject.getInt("time");
                        product product=new product(jsonObject.get("title").toString(),jsonObject.get("price1").toString(),jsonObject.get("img").toString(),jsonObject.getInt("id"),jsonObject.get("price2").toString(),jsonObject.get("time").toString(),jsonObject.getString("title2"));
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
                    sent_time();
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
    public class ProductAdapter extends RecyclerView.Adapter<MyViewHolder>
    {
        private List<product> productList;
        Context context;
        public ProductAdapter(List<product> list,Context cx)
        {
            productList=list;
            context=cx;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(Setting.pxfromdp(context,160), ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(15,0,0,0);
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.amazing_product,null);
            view.setLayoutParams(layoutParams);
            return new get_amazing_product.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            final product product=productList.get(position);
            holder.product_title.setText(product.getProduct_title());
            holder.product_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent j=new Intent(context,show_product.class);
                    j.putExtra("product_id",product.getProduct_id());
                    j.putExtra("product_title",product.getTitle2());
                    context.startActivity(j);
                    ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                }
            });


            if(Integer.valueOf(product.getProduct_time())>0)
            {
                DecimalFormat decimalFormat=new DecimalFormat("###,###");
                String p=decimalFormat.format(Integer.valueOf(product.getProduct_price1()))+" تومان ";
                holder.product_price.setText(p);
                holder.product_price.setPaintFlags(holder.product_price.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);

                DecimalFormat decimalFormat2=new DecimalFormat("###,###");
                String p2=decimalFormat2.format(Integer.valueOf(product.getProduct_price2()))+" تومان ";
                holder.product_price2.setText(p2);
            }
            else
            {
                holder.product_price2.setText("تمام شد");
                LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity=Gravity.RIGHT;
                layoutParams.setMargins(0,0,10,0);
                holder.product_price2.setGravity(Gravity.RIGHT);
                holder.product_price2.setLayoutParams(layoutParams);
            }


            String url=Setting.url+"/upload/"+product.getProduct_img();
            ImageRequest imageRequest=new ImageRequest(url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response)
                {
                    try
                    {
                        holder.product_img.setImageBitmap(response);
                        holder.product_img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                Intent j=new Intent(context,show_product.class);
                                j.putExtra("product_id",product.getProduct_id());
                                j.putExtra("product_title",product.getTitle2());
                                context.startActivity(j);
                                ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                            }
                        });
                    }
                    catch (Exception e)
                    {
                    }
                }
            }, 150, 0, null, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });

            getProductImage.add(imageRequest);

           // holder.product_img.s(product.getProduct_title());
        }

        @Override
        public int getItemCount() {
            return productList.size();
        }
    }

    public class product
    {
        private String product_title,product_price1,product_img,product_price2,product_time,title2;
        private int product_id;

        public String getTitle2() {
            return title2;
        }

        public void setTitle2(String title2) {
            this.title2 = title2;
        }

        public String getProduct_title() {
            return product_title;
        }

        public void setProduct_title(String product_title) {
            this.product_title = product_title;
        }

        public String getProduct_price1() {
            return product_price1;
        }

        public void setProduct_price1(String product_price1) {
            this.product_price1 = product_price1;
        }

        public String getProduct_img() {
            return product_img;
        }

        public void setProduct_img(String product_img) {
            this.product_img = product_img;
        }

        public String getProduct_price2() {
            return product_price2;
        }

        public void setProduct_price2(String product_price2) {
            this.product_price2 = product_price2;
        }

        public String getProduct_time() {
            return product_time;
        }

        public void setProduct_time(String product_time) {
            this.product_time = product_time;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public product(String title, String price1, String img, int id, String price2, String time,String p_title2)
        {
           product_title=title;
           product_price1=price1;
           product_price2=price2;
           product_time=time;
           product_img=img;
           product_id=id;
           title2=p_title2;

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView product_title,product_price,product_price2;
        ImageView product_img;
        public MyViewHolder(View view)
        {
            super(view);

            product_title=(TextView)view.findViewById(R.id.product_title);
            product_price=(TextView)view.findViewById(R.id.product_price1);
            product_price2=(TextView)view.findViewById(R.id.product_price2);
            product_img=(ImageView) view.findViewById(R.id.product_img);
        }
    }

    public void sent_time()
    {
        final TextView amazing_h=((Activity) cx).findViewById(R.id.amazing_h);
        final TextView amazing_m=((Activity) cx).findViewById(R.id.amazing_m);
        final TextView amazing_s=((Activity) cx).findViewById(R.id.amazing_s);
        for (int i=0;i<amazing_time.length;i++)
        {
            if(amazing_time[i]>time)
            {
                time=amazing_time[i];
            }
        }
        if(time>0)
        {
            final Handler handler=new Handler();

            final Thread thread=new Thread(new Runnable() {
                @Override
                public void run()
                {
                    while (time>0)
                    {
                        try
                        {
                            handler.post(new Runnable() {
                                @Override
                                public void run()
                                {
                                    float h1=time/3600;
                                    int h=(int)h1;

                                    float m1=(time-(h)*3600)/60;
                                    int m=(int)m1;

                                    int s=(time-((h)*3600)-(m*60));

                                    String h_final=String.valueOf(h);
                                    String m_final=String.valueOf(m);
                                    String s_final=String.valueOf(s);
                                    if (String.valueOf(h).length()==1)
                                    {
                                        h_final="0"+String.valueOf(h);
                                    }
                                    if (String.valueOf(m).length()==1)
                                    {
                                        m_final="0"+String.valueOf(m);
                                    }
                                    if (String.valueOf(s).length()==1)
                                    {
                                        s_final="0"+String.valueOf(s);
                                    }
                                    amazing_h.setText(h_final);
                                    amazing_m.setText(m_final);
                                    amazing_s.setText(s_final);
                                    time--;
                                }
                            });
                            Thread.sleep(1000);
                        }
                        catch (Exception e)
                        {

                        }

                    }
                }
            });
            thread.start();

        }
        else
        {
            RelativeLayout amazing_box=((Activity) cx).findViewById(R.id.amazing_box);
            amazing_box.setVisibility(View.GONE);
        }

    }
}
