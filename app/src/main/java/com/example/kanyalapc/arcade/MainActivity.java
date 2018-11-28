package com.example.kanyalapc.arcade;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Debug;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
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

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {


    class Teacher{
        private String name,description,imageURL;
        private int id;


        public Teacher(String name, String description, String imageURL,int id) {
            this.name = name;
            this.description = description;
            this.imageURL = imageURL;
            this.id = id;

        }
        public int getID(){return id;}
        public String getName() {return name;}
        public String getDescription() { return description; }
        public String getImageURL() { return imageURL; }


    }
    /*
   Our custom adapter class
    */
    public class ListViewAdapter extends BaseAdapter {
        Context c;
        ArrayList<MainActivity.Teacher> teachers;

        public ListViewAdapter(Context c, ArrayList<MainActivity.Teacher> teachers) {
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
                view= LayoutInflater.from(c).inflate(R.layout.row_model,viewGroup,false);
            }

            TextView txtName = view.findViewById(R.id.nameTextView);
            TextView txtDescription = view.findViewById(R.id.descriptionTextView);
            ImageView teacherImageView = view.findViewById(R.id.teacherImageView);

            final MainActivity.Teacher teacher= (MainActivity.Teacher) this.getItem(i);

            final MainActivity.Teacher id= (MainActivity.Teacher) this.getItem(i);

            txtName.setText(teacher.getName());
            txtDescription.setText(teacher.getDescription());

            if(teacher.getImageURL() != null && teacher.getImageURL().length() > 0)
            {
                Picasso.get().load(teacher.getImageURL()).placeholder(R.drawable.placeholder).into(teacherImageView);
            }else {
                Toast.makeText(c, "Empty Image URL", Toast.LENGTH_LONG).show();
                Picasso.get().load(R.drawable.placeholder).into(teacherImageView);
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Toast.makeText(c, teacher.getName(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, ShowCartoon.class);
                    intent.putExtra("usrname",(username));
                    intent.putExtra("cid",(""+id.getID()));
                    startActivity(intent);

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
        private final String PHP_MYSQL_SITE_URL="http://"+ConfigIP.IP+"/userread.php";
        //INSTANCE FIELDS
        private final Context c;
        private MainActivity.ListViewAdapter adapter ;

        public DataRetriever(Context c) { this.c = c; }
        /*
        RETRIEVE/SELECT/REFRESH
         */
        public void retrieve(final ListView gv, final ProgressBar myProgressBar)
        {
            final ArrayList<MainActivity.Teacher> teachers = new ArrayList<>();

            myProgressBar.setIndeterminate(true);
            myProgressBar.setVisibility(View.VISIBLE);

            AndroidNetworking.get(PHP_MYSQL_SITE_URL)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jo;
                            MainActivity.Teacher teacher;
                            try
                            {
                                for(int i=0;i<response.length();i++)
                                {
                                    jo=response.getJSONObject(i);

                                    int id=jo.getInt("cid");
                                    String name=jo.getString("name");
                                    String description="";
                                    String imageURL=jo.getString("image_url");

                                    teacher=new MainActivity.Teacher(name,description,"http://"+ConfigIP.IP+""+imageURL,id);
                                    teachers.add(teacher);
                                }

                                //SET TO SPINNER
                                adapter =new MainActivity.ListViewAdapter(c,teachers);
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
    String username;
    String cid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username=  ""+getIntent().getSerializableExtra("usrname");
        cid=  ""+getIntent().getSerializableExtra("cid");
        ListView myGridView=findViewById(R.id.myListView);
        ProgressBar myDataLoaderProgressBar=findViewById(R.id.myDataLoaderProgressBar);

        new MainActivity.DataRetriever(MainActivity.this).retrieve(myGridView,myDataLoaderProgressBar);

    }
}
