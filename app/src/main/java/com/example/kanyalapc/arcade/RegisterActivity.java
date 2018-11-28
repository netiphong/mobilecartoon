package com.example.kanyalapc.arcade;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class RegisterActivity extends AppCompatActivity {
    private EditText editName,editPassword,editConPassword;
    private Button Btn_insert,btn_back;
    private TextView txtInfo,chk1,chk2,chk3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btn_back = (Button)findViewById(R.id.btnBack);
        editName = (EditText) findViewById(R.id.editName);
        editPassword = (EditText) findViewById(R.id.editPassword);
        editConPassword = (EditText) findViewById(R.id.editConPassword);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        Btn_insert = (Button) findViewById(R.id.btnRegister);

        final String userPattern = "[a-zA-Z0-9._-]+";
        final String passPattern = "[a-zA-Z0-9._-]+";

        chk1=(TextView)findViewById(R.id.chk1);
        chk2=(TextView)findViewById(R.id.chk2);

        chk3=(TextView)findViewById(R.id.chk3);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        Btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editPassword.getText().toString().matches(passPattern)){
                    chk2.setText("Correct");
                }
                else {
                    chk2.setText("Enter A-Z or a-z or number");
                }
                if(editName.getText().toString().matches(userPattern)){
                    chk1.setText("Correct");
                }
                else {
                    chk1.setText("Enter A-Z or a-z or number");
                }

                if(editPassword.getText().toString().matches(editConPassword.getText().toString())){

                    chk3.setText("Correct");
                }
                else {
                    chk3.setText("Password not match");
                }
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                    if( editPassword.getText().toString().matches(passPattern)&&editName.getText().toString().matches(userPattern)&& editPassword.getText().toString().matches(editConPassword.getText().toString())){
                        txtInfo.setText("Complete");
                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ConfigIP.IP+"/create.php").newBuilder();
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
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });
                            }

                            ;
                        });
                   finish();
                    }


                    else
                        {
                            txtInfo.setText("Please Check Infomation");
                           }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}