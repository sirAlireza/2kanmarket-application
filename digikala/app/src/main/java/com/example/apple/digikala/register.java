package com.example.apple.digikala;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class register extends AppCompatActivity {

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
        setContentView(R.layout.activity_register);
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


    public class Send_Data
    {
        RequestQueue requestQueue;
        public Send_Data()
        {
            if(send==0)
            {
                if(check_empty())
                {
                    if(check_username())
                    {
                        if(check_password())
                        {
                            send_data();
                        }

                    }
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
        public boolean check_username()
        {
            boolean a=true;
            String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(!username.getText().toString().trim().matches(emailPattern))
            {
                if(!check_mobile_number())
                {
                    a=false;
                    username.setError("ایمیل یا شماره موبایل وارد شده معتبر نمی باشد");
                }
            }

            return  a;
        }
        public boolean check_mobile_number()
        {
            String numberPattern="[0-9]+";
            boolean a=true;
            String v=username.getText().toString().trim();
            if(v.length()!=11)
            {
                Log.i("number_error","1");
                a=false;
            }
            else
            {
               if(!v.matches(numberPattern))
               {
                   Log.i("number_error","2");
                   a=false;
               }
               else
               {
                   String l1=v.substring(0,1);
                   String l2=v.substring(1,2);
                   if(!l1.equals("0") || !l2.equals("9"))
                   {
                       a=false;
                   }

               }
            }
            return  a;
        }
        public boolean check_password()
        {
            boolean a=true;
            if(password.getText().toString().trim().length()<6)
            {
                a=false;
                password.setError("کلمه عبور حداقل باید شامل 6 کاراکتر باشد");
            }
            return  a;
        }

        public void send_data()
        {
           final ProgressBar send_progress=(ProgressBar)findViewById(R.id.send_progress);
           send_progress.setVisibility(View.VISIBLE);
           btn_send.setVisibility(View.GONE);

           final TextView error_test=(TextView)findViewById(R.id.error_test);
           send=1;
           String url=Setting.url+"/api/android_user_register";
           requestQueue= Volley.newRequestQueue(register.this);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response)
                {
                    send_progress.setVisibility(View.GONE);
                    btn_send.setVisibility(View.VISIBLE);
                   if(response.equals("ok"))
                   {
                       Intent j=new Intent(register.this,Login.class);
                       startActivity(j);
                   }
                   else if (response.equals("error"))
                   {
                       send=0;
                       error_test.setText("خطا در ثبت اطلاعات -دوباره تلاش نمایید");
                   }
                   else
                   {
                       send=0;
                       error_test.setText(response);
                   }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params=new HashMap<String,String>();
                    params.put("username",username.getText().toString().trim());
                    params.put("password",password.getText().toString().trim());
                    return  params;
                }
            };

            requestQueue.add(stringRequest);

        }
    }
}
