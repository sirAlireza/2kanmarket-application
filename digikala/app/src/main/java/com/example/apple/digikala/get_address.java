package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 4/22/18.
 */

public class get_address
{
    Context context;
    RequestQueue requestQueue;
    SharedPreferences login;
    String token;
    public get_address(Context context) {
        this.context = context;


    }

    public void show_address()
    {

        Timestamp timestamp=new Timestamp(System.currentTimeMillis());

        long time=timestamp.getTime();

        login=context.getSharedPreferences("digikal_userdata",Context.MODE_PRIVATE);
        token=login.getString("access_token","no");
        String email=login.getString("username","no");
        long a=0;
        long token_time=login.getLong("token_time",a);
        if(!token.equals("no"))
        {
            if(token_time>time)
            {
                get_address();
            }
            else
            {
                  login.edit().clear().commit();
                  Intent j=new Intent(context,Login.class);
                  context.startActivity(j);
            }
        }
        else
        {
            login.edit().clear().commit();
            Intent j=new Intent(context,Login.class);
            context.startActivity(j);
        }
    }
    public void get_address()
    {
        requestQueue= Volley.newRequestQueue(context);
        String url=Setting.url+"/api/get_android_user_address";
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response)
            {
                try {

                   for (int i=0;i<response.length();i++)
                   {
                       JSONObject jsonObject=response.getJSONObject(i);
                       Log.i("name",jsonObject.getString("name"));
                   }
                }
                catch (Exception e)
                {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("error","ok");
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                String s="Bearer "+token;
                Map<String,String> headers=new HashMap<>();
                headers.put("accept","application/json");
                headers.put("authorization",s);
                return headers;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }
}
