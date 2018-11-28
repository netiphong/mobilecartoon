package com.example.kanyalapc.arcade;

import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class LoginActivity extends AppCompatActivity {
    private EditText editName,editPassword;
    private Button Btn_insert,goRegister;
    private TextView txtInfo,status;

    String i,txtPID,txtPass,st;
    protected int _splashTime = 2000;
    public void checkstatus() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ConfigIP.IP+"/read.php").newBuilder();
            txtPID=(editName.getText().toString());
            urlBuilder.addQueryParameter("Name", txtPID);


            String url = urlBuilder.build().toString();

            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                //txtInfo.setText(response.body().string());

                                try {
                                    String data = response.body().string();

                                    JSONArray jsonArray = new JSONArray(data);
                                    JSONObject jsonObject;

                                    jsonObject = jsonArray.getJSONObject(0);
                                    txtPass=(jsonObject.getString("password"));
                                    status.setText(jsonObject.getString("status"));

                                    i=txtPass;
                                    st=status.getText().toString();
                                    if(i.equals(editPassword.getText().toString())){
                                        if((status.getText().toString()).equals("admin")){
                                            Intent intent = new Intent(LoginActivity.this, AdminListActivity.class);
                                            intent.putExtra("usrname",(editName.getText().toString()));
                                            startActivity(intent);

                                        }
                                        else {
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            intent.putExtra("usrname",(editName.getText().toString()));
                                            startActivity(intent);

                                        }
                                    }

                                } catch (JSONException e) {
                                    txtInfo.setText(e.getMessage());
                                }


                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }

                ;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editName = (EditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        goRegister=(Button)findViewById(R.id.btnSignUP);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        status = (TextView) findViewById(R.id.status);
        Btn_insert = (Button) findViewById(R.id.btnSignIn);



        Btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ConfigIP.IP+"/login.php").newBuilder();
                        urlBuilder.addQueryParameter("Name", editName.getText().toString());
                        urlBuilder.addQueryParameter("Password", editPassword.getText().toString());
                        String url = urlBuilder.build().toString();

                        okhttp3.Request request = new okhttp3.Request.Builder()
                                .url(url)
                                .build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, final okhttp3.Response response) throws IOException {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            txtInfo.setText(response.body().string());
                                            checkstatus();

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }

                            ;
                        });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        goRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
