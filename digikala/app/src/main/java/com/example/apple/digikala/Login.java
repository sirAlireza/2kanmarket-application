package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class Login extends AppCompatActivity {

    EditText username;
    EditText password;
    TextView btn_send;
    int send=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/iranSansWeb.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);

        CheckBox show_password=(CheckBox)findViewById(R.id.show_password);
        show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else
                {
                    password.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });


        btn_send=(TextView) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Send_Data send_data=new Send_Data();
            }
        });

    }

    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void show_register_activity(View view)
    {
        Intent j=new Intent(Login.this,register.class);
        startActivity(j);
        overridePendingTransition(R.anim.layout_enter,R.anim.layout_edit);
    }


    public class Send_Data
    {
        SharedPreferences sharedPreferences;
        RequestQueue requestQueue;
        public Send_Data()
        {
            if(send==0)
            {
                if(check_empty())
                {
                    send_data();
                }
            }

        }
        public boolean check_empty()
        {
            boolean a=true;
            if(username.getText().toString().trim().isEmpty())
            {
                a=false;
                username.setError("شماره موبایل یا ایمیل نمی تواند خالی باشد");
            }
            if(password.getText().toString().trim().isEmpty())
            {
                a=false;
                password.setError("کلمه عبور نمی تواند خالی باشد");
            }
            return a;
        }

        public void send_data()
        {
            final ProgressBar send_progress=(ProgressBar)findViewById(R.id.send_progress);
            send_progress.setVisibility(View.VISIBLE);
            btn_send.setVisibility(View.GONE);

            final TextView error_test=(TextView)findViewById(R.id.error_test);
            send=1;
            String url=Setting.url+"/oauth/token";
            requestQueue= Volley.newRequestQueue(Login.this);

            Map<String,String> params=new HashMap<String, String>();
            params.put("grant_type","password");
            params.put("client_id","6");
            params.put("client_secret","v3Bha4nEQztteaS3lkUJNkWbGA27zDOXbUMJncss");
            params.put("username",username.getText().toString());
            params.put("password",password.getText().toString());
            params.put("scope","");

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, url,new JSONObject(params),new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    send_progress.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                    try
                    {

                        Timestamp timestamp=new Timestamp(System.currentTimeMillis());

                        long token_time=timestamp.getTime();

                        token_time=token_time+(2*24*60*60*1000);

                        String access_token=response.getString("access_token");
                        sharedPreferences=getSharedPreferences("digikal_userdata",MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("access_token",access_token);
                        editor.putString("username",username.getText().toString());
                        editor.putLong("token_time",token_time);
                        editor.commit();
                        Intent j=new Intent(Login.this,MainActivity.class);
                        startActivity(j);
                    }
                    catch (Exception e)
                    {
                        send=0;
                        error_test.setText("اطلاعات ارسال شده اشتباه می باشد");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    send_progress.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                    send=0;
                    error_test.setText("اطلاعات ارسال شده اشتباه می باشد");
                }
            });

            requestQueue.add(jsonObjectRequest);

        }
    }
}
