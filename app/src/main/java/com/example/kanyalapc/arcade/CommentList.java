package com.example.kanyalapc.arcade;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class CommentList extends AppCompatActivity {


    class Teacher{
        private String name,description;
        private int id;


        public Teacher(String name, String description,int id) {
            this.name = name;
            this.description = description;

            this.id = id;

        }
        public int getID(){return id;}
        public String getName() {return name;}
        public String getDescription() { return description; }



    }
    /*
   Our custom adapter class
    */
    public class ListViewAdapter extends BaseAdapter {
        Context c;
        ArrayList<CommentList.Teacher> teachers;

        public ListViewAdapter(Context c, ArrayList<CommentList.Teacher> teachers) {
            this.c = c;
            this.teachers = teachers;
        }
        @Override
        public int getCount() {return teachers.size();}
        @Override
        public Object getItem(int i) {return teachers.get(i);}
        @Override
        public long getItemId(int i) {return i;}
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view= LayoutInflater.from(c).inflate(R.layout.row_comment,viewGroup,false);
            }

            TextView txtName = view.findViewById(R.id.nameTextView);
            TextView txtDescription = view.findViewById(R.id.descriptionTextView);
            ImageView teacherImageView = view.findViewById(R.id.teacherImageView);

            final CommentList.Teacher teacher= (CommentList.Teacher) this.getItem(i);

            final CommentList.Teacher id= (CommentList.Teacher) this.getItem(i);

            txtName.setText(teacher.getName());
            txtDescription.setText(teacher.getDescription());


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                }
            });

            return view;
        }
    }
    /*
    Our HTTP Client
     */
    public class DataRetriever {

        //YOU CAN USE EITHER YOUR IP ADDRESS OR  10.0.2.2 I depends on the Emulator
        //private static final String PHP_MYSQL_SITE_URL="http://10.0.2.2/php/spiritualteachers";
        //For genymotion you can use this
        //private static final String PHP_MYSQL_SITE_URL="http://10.0.3.2/php/spiritualteachers";
        //You can get your ip adrress by typing ipconfig/all in cmd
        private final String PHP_MYSQL_SITE_URL="http://"+ConfigIP.IP+"/readComment.php";

        //INSTANCE FIELDS
        private final Context c;
        private CommentList.ListViewAdapter adapter ;

        public DataRetriever(Context c) { this.c = c; }
        /*
        RETRIEVE/SELECT/REFRESH
         */
        public void retrieve(final ListView gv, final ProgressBar myProgressBar)
        {
            final ArrayList<CommentList.Teacher> teachers = new ArrayList<>();

            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);


            AndroidNetworking.get(PHP_MYSQL_SITE_URL)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            CommentList.Teacher teacher;
                            try
                            {

                                for(int i=0;i<response.length();i++)
                                {

                                    jo=response.getJSONObject(i);
                                    int id=jo.getInt("cid");
                                    String name=jo.getString("username");
                                    String description=jo.getString("message");
                                    String imageURL="";

                                    teacher=new CommentList.Teacher(name,description,id);
                                    teachers.add(teacher);
                                }

                                //SET TO SPINNER
                                adapter =new CommentList.ListViewAdapter(c,teachers);
                                gv.setAdapter(adapter);
                                myProgressBar.setVisibility(View.GONE);

                            }catch (JSONException e)
                            {
                                myProgressBar.setVisibility(View.GONE);
                                Toast.makeText(c, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIEVED. "+e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        //ERROR
                        @Override
                        public void onError(ANError anError) {
                            anError.printStackTrace();
                            myProgressBar.setVisibility(View.GONE);
                            Toast.makeText(c, "UNSUCCESSFUL :  ERROR IS : "+anError.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


        }
    }
    private Button btnback;
    private  TextView txtInfo;
    String username,cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        txtInfo=(TextView)findViewById(R.id.txtInfo);
        cid= ""+getIntent().getSerializableExtra("cid");
        username=  ""+getIntent().getSerializableExtra("usrname");

        ListView myGridView=findViewById(R.id.myListView);
        ProgressBar myDataLoaderProgressBar=findViewById(R.id.myDataLoaderProgressBar);

        new CommentList.DataRetriever(CommentList.this).retrieve(myGridView,myDataLoaderProgressBar);


        btnback = findViewById(R.id.btnback);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommentList.this,ShowCartoon.class);

                intent.putExtra("usrname",(username));
                intent.putExtra("cid",(""+cid));
                startActivity(intent);
            }
        });


    }



}
