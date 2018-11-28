package com.example.kanyalapc.arcade;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class ShowCartoon extends AppCompatActivity {
    private ImageView img;
    private TextView cname,details,txtInfo;
    String cid,imgurl,username;
    private Button back,readpost,comment;
    private EditText edtcomment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_cartoon);
        edtcomment=(EditText)findViewById(R.id.comment);
        back=(Button)findViewById(R.id.back);
        readpost=(Button)findViewById(R.id.readpost);
        comment=(Button)findViewById(R.id.submit);

        img=(ImageView)findViewById(R.id.img);
        cname=(TextView)findViewById(R.id.cname);
        details=(TextView)findViewById(R.id.cdetails);


        txtInfo=(TextView) findViewById(R.id.txtInfo);

        cid= ""+getIntent().getSerializableExtra("cid");
        username=  ""+getIntent().getSerializableExtra("usrname");

        checkstatus();
        readpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowCartoon.this, CommentList.class);
                intent.putExtra("usrname",(username));
                intent.putExtra("cid",(""+cid));

                startActivity(intent);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();
                        txtInfo.setText("Complete");
                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://"+ConfigIP.IP+"/comment.php").newBuilder();
                        urlBuilder.addQueryParameter("Name", username);
                        urlBuilder.addQueryParameter("Message", edtcomment.getText().toString());
                        urlBuilder.addQueryParameter("CID", cid);

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

                    Intent intent = new Intent(ShowCartoon.this, CommentList.class);
                    intent.putExtra("usrname",(username));
                    intent.putExtra("cid",(""+cid));
                  startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ShowCartoon.this, MainActivity.class);
                intent.putExtra("usrname",(username));
                intent.putExtra("cid",(""+cid));
                startActivity(intent);
            }
        });

    }

    public void checkstatus() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http:/" + ConfigIP.IP + "/checkcartoon.php").newBuilder();

            urlBuilder.addQueryParameter("UID", cid);


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
                                    cname.setText(jsonObject.getString("name"));
                                    details.setText(jsonObject.getString("details"));

                                    imgurl = (jsonObject.getString("image"));
                                    Picasso.get().load("http:/" + ConfigIP.IP + ""+imgurl).into(img);


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

}
