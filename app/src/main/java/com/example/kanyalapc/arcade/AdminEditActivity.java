package com.example.kanyalapc.arcade;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class AdminEditActivity extends AppCompatActivity {
    Button GetImageFromGalleryButton, EditImageOnServerButton,ccancel,btndel;

    ImageView ShowSelectedImage;
    TextView txtInfo;
    EditText imageName,details,uid;


    Bitmap FixBitmap;

    String ImageTag = "image_tag" ;

    String ImageName = "image_data" ;

    ProgressDialog progressDialog ;

    ByteArrayOutputStream byteArrayOutputStream ;

    byte[] byteArray ;

    String ConvertImage ;

    String GetImageNameFromEditText;

    HttpURLConnection httpURLConnection ;

    URL url;

    OutputStream outputStream;

    BufferedWriter bufferedWriter ;

    int RC ;

    BufferedReader bufferedReader ;

    StringBuilder stringBuilder;

    boolean check = true;


    String adminname;
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_edit);

        GetImageFromGalleryButton = (Button)findViewById(R.id.buttonSelect);
        ccancel=(Button)findViewById(R.id.canncel);
        btndel=(Button)findViewById(R.id.btndel);
        EditImageOnServerButton = (Button)findViewById(R.id.buttonEdit);


       uid=(EditText)findViewById(R.id.uid);
       txtInfo = (TextView) findViewById(R.id.txtInfo);

        ShowSelectedImage = (ImageView)findViewById(R.id.imageView);

        imageName=(EditText)findViewById(R.id.imageName);
        details=(EditText)findViewById(R.id.details);



        uid.setText(""+getIntent().getSerializableExtra("uid"));
        adminname= ""+getIntent().getSerializableExtra("usrname");

        checkstatus();

        byteArrayOutputStream = new ByteArrayOutputStream();
        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(AdminEditActivity.this);
                alert.setTitle("Delete Game");
                alert.setMessage("Are you sure ?");
                alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                      deldb();

                        Intent intent = new Intent(AdminEditActivity.this, AdminListActivity.class);

                        intent.putExtra("usrname",(adminname));
                        startActivity(intent);


                    }
                });
                alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AdminEditActivity.this, "Cancel",Toast.LENGTH_SHORT).show();

                    }
                });
                alert.create().show();

            }
        });
        ccancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminEditActivity.this, AdminListActivity.class);
                intent.putExtra("usrname",(adminname));
                startActivity(intent);
            }
        });
        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showPictureDialog();


            }
        });


       EditImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(AdminEditActivity.this);
                alert.setTitle("Edit Game");
                alert.setMessage("Are you sure ?");
                alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        GetImageNameFromEditText = imageName.getText().toString();

                        Editgame();
                        Intent intent = new Intent(AdminEditActivity.this, AdminListActivity.class);
                        intent.putExtra("usrname",(adminname));
                        startActivity(intent);
                    }
                });
                alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AdminEditActivity.this, "Cancel",Toast.LENGTH_SHORT).show();

                    }
                });
                alert.create().show();




            }
        });

        if (ContextCompat.checkSelfPermission(AdminEditActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                        5);
            }
        }
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // String path = saveImage(bitmap);
                    //Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    ShowSelectedImage.setImageBitmap(FixBitmap);
                   EditImageOnServerButton.setVisibility(View.VISIBLE);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AdminEditActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            ShowSelectedImage.setImageBitmap(FixBitmap);
           EditImageOnServerButton.setVisibility(View.VISIBLE);
            //  saveImage(thumbnail);
            //Toast.makeText(ShadiRegistrationPart5.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }


    public void Editgame(){

        FixBitmap.compress(Bitmap.CompressFormat.JPEG, 40, byteArrayOutputStream);

        byteArray = byteArrayOutputStream.toByteArray();

        ConvertImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

        class AsyncTaskUploadClass extends AsyncTask<Void,Void,String> {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                progressDialog = ProgressDialog.show(AdminEditActivity.this,"Image is Uploading","Please Wait",false,false);
            }

            @Override
            protected void onPostExecute(String string1) {

                super.onPostExecute(string1);

                progressDialog.dismiss();

                Toast.makeText(AdminEditActivity.this,string1,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params) {

                AdminEditActivity.ImageProcessClass imageProcessClass = new AdminEditActivity.ImageProcessClass();

                HashMap<String,String> HashMapParams = new HashMap<String,String>();

                HashMapParams.put("UID", uid.getText().toString());

                HashMapParams.put(ImageTag, GetImageNameFromEditText);

                HashMapParams.put(ImageName, ConvertImage);

                HashMapParams.put("details",details.getText().toString());

                HashMapParams.put("adminname",adminname);




                String FinalData = imageProcessClass.ImageHttpRequest("http://"+ConfigIP.IP+"/edit.php", HashMapParams);

                return FinalData;
            }
        }
        AsyncTaskUploadClass AsyncTaskUploadClassOBJ = new AsyncTaskUploadClass();
        AsyncTaskUploadClassOBJ.execute();
    }
    public class ImageProcessClass{

        public String ImageHttpRequest(String requestURL,HashMap<String, String> PData) {

            StringBuilder stringBuilder = new StringBuilder();

            try {
                url = new URL(requestURL);

                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(20000);

                httpURLConnection.setConnectTimeout(20000);

                httpURLConnection.setRequestMethod("POST");

                httpURLConnection.setDoInput(true);

                httpURLConnection.setDoOutput(true);

                outputStream = httpURLConnection.getOutputStream();

                bufferedWriter = new BufferedWriter(

                        new OutputStreamWriter(outputStream, "UTF-8"));

                bufferedWriter.write(bufferedWriterDataFN(PData));

                bufferedWriter.flush();

                bufferedWriter.close();

                outputStream.close();

                RC = httpURLConnection.getResponseCode();

                if (RC == HttpsURLConnection.HTTP_OK) {

                    bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                    stringBuilder = new StringBuilder();

                    String RC2;

                    while ((RC2 = bufferedReader.readLine()) != null){

                        stringBuilder.append(RC2);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        private String bufferedWriterDataFN(HashMap<String, String> HashMapParams) throws UnsupportedEncodingException {

            stringBuilder = new StringBuilder();

            for (Map.Entry<String, String> KEY : HashMapParams.entrySet()) {
                if (check)
                    check = false;
                else
                    stringBuilder.append("&");

                stringBuilder.append(URLEncoder.encode(KEY.getKey(), "UTF-8"));

                stringBuilder.append("=");

                stringBuilder.append(URLEncoder.encode(KEY.getValue(), "UTF-8"));
            }

            return stringBuilder.toString();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera

            }
            else {

                Toast.makeText(AdminEditActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();

            }
        }
    }


   public void checkstatus() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http:/"+ConfigIP.IP+"/check.php").newBuilder();

            urlBuilder.addQueryParameter("UID", uid.getText().toString());


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
                                    imageName.setText(jsonObject.getString("name"));
                                    details.setText(jsonObject.getString("details"));


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

    public void deldb(){

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http:/"+ConfigIP.IP+"/del.php").newBuilder();
            urlBuilder.addQueryParameter("UID",uid.getText().toString());

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
