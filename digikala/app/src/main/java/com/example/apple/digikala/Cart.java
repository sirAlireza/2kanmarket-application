package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by apple on 3/8/18.
 */

public class Cart
{
    Context context;

    public Cart(Context c)
    {
        context=c;
    }
    public void add(String title,String code,String img_url,int price,int color_id,int service_id,int product_id,String color_code,String color_name,int discounts,String service_name)
    {
        String key=product_id+"_"+color_id+"_"+service_id+",";
        SharedPreferences shared_cart=context.getSharedPreferences("idigikal_cart",Context.MODE_PRIVATE);
        SharedPreferences.Editor edit=shared_cart.edit();
        String key_value=shared_cart.getString("key","");
        if(key_value.contains(key))
        {
            edit.putString("key",key_value);
        }
        else
        {
            key_value=key_value+key;
            edit.putString("key",key_value);
        }

        edit.commit();

        Log.i("key",shared_cart.getString("key","fdgh"));

        String name="digikala_product_"+product_id+"_"+color_id+"_"+service_id;

        SharedPreferences shared_cart_product=context.getSharedPreferences(name,Context.MODE_PRIVATE);
        SharedPreferences.Editor edit2=shared_cart_product.edit();
        if(shared_cart_product.getString("product_title","no").equals("no"))
        {
            Log.i("as",img_url);
            edit2.putString("product_title",title);
            edit2.putString("product_code",code);
            edit2.putString("product_image_url",img_url);
            edit2.putInt("product_price",price);
            edit2.putInt("discounts",discounts);
            edit2.putInt("product_number",1);
            edit2.putString("color_name",color_name);
            edit2.putString("color_code",color_code);
            edit2.putString("service_name",service_name);
            edit2.putInt("service_id",service_id);
        }
        else
        {
            int n=shared_cart_product.getInt("product_number",0)+1;
            edit2.putInt("product_number",n);
        }
        edit2.commit();

        Intent j=new Intent(context,shop_cart.class);
        context.startActivity(j);

    }
}
