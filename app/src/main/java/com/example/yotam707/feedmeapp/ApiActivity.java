package com.example.yotam707.feedmeapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yotam707.feedmeapp.data.Callback.IMyCallback;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.example.yotam707.feedmeapp.domain.RecipesResponse;
import com.example.yotam707.feedmeapp.domain.UploadImage;
import com.example.yotam707.feedmeapp.domain.UploadImageParams;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiActivity extends AppCompatActivity {

    private static final String TAG = ApiActivity.class.getSimpleName();

Button btn;
EditText txt;
Context mContext;
ImageView imgV;
    private AsyncTask mMyTask;
    private ProgressDialog mProgressDialog;
    private ConstraintLayout mCLayout;
    private Uri uri;
String url = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/search?query=*&number=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);
        btn = findViewById(R.id.button_2);
        txt = findViewById(R.id.edit_text);
        mContext = getApplicationContext();
        mCLayout = findViewById(R.id.constraint_layout);
        imgV = findViewById(R.id.image_view_res);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        // Progress dialog horizontal style
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        // Progress dialog title
        mProgressDialog.setTitle("AsyncTask");
        // Progress dialog message
        mProgressDialog.setMessage("Please wait, we are downloading your image file...");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Gson gson = new GsonBuilder().create();
                if (txt.getText() != null) {
                    RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                    String urlToQuery = url + "&type=" + txt.getText();
                    StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, urlToQuery, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, response);
                            List<Recipe> recipeList;
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String urlImage = jsonObject.getString("baseUri");
                                recipeList = Arrays.asList(gson.fromJson(jsonObject.getJSONArray("results").toString(), Recipe[].class));
                                UploadImageParams params = new UploadImageParams();
                                params.setUrl(stringToURL(
                                        urlImage+"/"+recipeList.get(0).getImage()));
                                params.setImageName(recipeList.get(0).getImage());
                               mMyTask = new DownloadTask().execute(params);
                                if(uri != null){
                                    recipeList.get(0).setImgUrl(uri.getPath());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG, error.getMessage());
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError  {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("X-Mashape-Key", "9HmtbD95PRmsht1DgeqSCh8N8KLTp17CGuwjsnaM0bgcQn5tbr");
                            headers.put("Accept", "application/json");
                            return headers;
                        }
                    };
                    requestQueue.add(jsonArrayRequest);
                }
            }
        });
        }
//todo: need to finish this method - figure out how to return Uri when finished downloading and uploading.
    private class DownloadTask extends AsyncTask<UploadImageParams,IMyCallback,UploadImage> {
        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            mProgressDialog.show();
        }

        // Do the task in background/non UI thread
        protected UploadImage doInBackground(UploadImageParams... urls) {
            UploadImage uploadImage = new UploadImage();
            URL url = urls[0].getUrl();
            uploadImage.setImageName(urls[0].getImageName());
            HttpURLConnection connection = null;

            try {
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();
                // Connect the http url connection
                connection.connect();
                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);
                uploadImage.setBitmap(bmp);
                // Return the downloaded bitmap
                return uploadImage;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(UploadImage result){
            // Hide the progress dialog
            mProgressDialog.dismiss();
            if(result!=null){
                // Save bitmap to internal storage

                saveImageToStorage(result, new IMyCallback() {
                    @Override
                    public void onCallback(Uri value) {
                        uri = value;
                    }
                });

            }else {
                // Notify user that an error occurred while downloading image
                Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }


    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected void saveImageToStorage(UploadImage uploadImage, final IMyCallback myCallback){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        StorageReference uploadImageReference = storageReference.child("images/"+uploadImage.getImageName());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        uploadImage.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = uploadImageReference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mProgressDialog.dismiss();
                Toast.makeText(ApiActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgressDialog.dismiss();

                Toast.makeText(ApiActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                myCallback.onCallback(taskSnapshot.getDownloadUrl());
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                mProgressDialog.setMessage("Uploaded "+(int)progress+"%");
            }
        });
    }
}


