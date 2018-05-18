package com.example.apple.digikala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

public class get_order_product
{
    RequestQueue requestQueue,getProductImage;
    public List<product> productList;
    get_order_product.ProductAdapter productAdapter;
    protected String request_url;
    public get_order_product(Context context,int id,String url)
    {
        request_url=url;
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
                    for (int i=0;i<response.length();i++)
                    {
                        JSONObject jsonObject=response.getJSONObject(i);
                        product product=new product(jsonObject.get("title").toString(),jsonObject.get("price").toString(),jsonObject.get("img").toString(),jsonObject.getInt("id"));
                        productList.add(product);
                    }
                    productAdapter.notifyDataSetChanged();
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
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_index,null);
            view.setLayoutParams(layoutParams);
            return new get_order_product.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position)
        {
            final product product=productList.get(position);
            String t=product.getProduct_title().substring(0,30);
            if(product.getProduct_title().length()>30)
            {
                t=t+" ...";
            }
            holder.product_title.setText(t);
            holder.product_title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent j=new Intent(context,show_product.class);
                    j.putExtra("product_id",product.getProduct_id());
                    context.startActivity(j);
                    ((Activity)context).overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);

                }
            });
            DecimalFormat decimalFormat=new DecimalFormat("###,###");
            String p=decimalFormat.format(Integer.valueOf(product.getProduct_price()))+" تومان ";
            holder.product_price.setText(p);


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
        private String product_title,product_price,product_img;
        private int product_id;

        public String getProduct_title() {
            return product_title;
        }

        public void setProduct_title(String product_title) {
            this.product_title = product_title;
        }

        public String getProduct_price() {
            return product_price;
        }

        public void setProduct_price(String product_price) {
            this.product_price = product_price;
        }

        public String getProduct_img() {
            return product_img;
        }

        public void setProduct_img(String product_img) {
            this.product_img = product_img;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public product(String title, String price, String img, int id)
        {
           product_title=title;
           product_price=price;

           product_img=img;
           product_id=id;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView product_title,product_price;
        ImageView product_img;
        public MyViewHolder(View view)
        {
            super(view);

            product_title=(TextView)view.findViewById(R.id.product_title);
            product_price=(TextView)view.findViewById(R.id.product_price);
            product_img=(ImageView) view.findViewById(R.id.product_img);
        }
    }
}
