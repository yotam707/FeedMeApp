package com.example.yotam707.feedmeapp;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.yotam707.feedmeapp.Utils.StringUtils;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreEnum;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.AnalyzedInstructions;
import com.example.yotam707.feedmeapp.domain.CategoryTypeEnum;
import com.example.yotam707.feedmeapp.domain.Equipment;
import com.example.yotam707.feedmeapp.domain.FullRecipe;
import com.example.yotam707.feedmeapp.domain.Ingredients;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.example.yotam707.feedmeapp.domain.Step;
import com.example.yotam707.feedmeapp.domain.UploadImage;
import com.example.yotam707.feedmeapp.domain.UploadImageParams;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.opencensus.internal.StringUtil;

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
    String urlInformation = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";
    String urlIngridient = "https://spoonacular.com/cdn/ingredients_100x100/";
    String urlEquipement = "https://spoonacular.com/cdn/equipment_100x100/";
    String[] categoryArr = new String[]{"African","American","Chinese", "Greek", "Indian","Italian", "Mexican" , "Middle Eastern", "Thai"};
    List<String> categoryList = new ArrayList<>();
    List<FullRecipe> fullRecipes;
    ArrayList<List<Recipe>> listObj;
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
        //mProgressDialog.setTitle("AsyncTask");
        // Progress dialog message
        //mProgressDialog.setMessage("Please wait, we are downloading your image file...");
        //categoryList = Arrays.asList(categoryArr);
        DataManager.getInstance(getApplicationContext());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CreateApiCallToRecipe();
                //GetAllRecipes();
                //GetAllFullRecipes();
                //CreateRecipeByCuisineType();
                //DataManager.getInstance(getApplicationContext());
            }
        });
        }





    public void GetAllFullRecipes(){
            FirestoreManager.getAllFullRecipes(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        fullRecipes = new ArrayList<>();
                        for(DocumentSnapshot document: task.getResult()){
                            fullRecipes.add(document.toObject(FullRecipe.class));

                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        CreateEquipmentAndIngredients(fullRecipes);
                    }
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
        public void CreateEquipmentAndIngredients(List<FullRecipe> fullRecipes){
            for(FullRecipe fullRecipe: fullRecipes){
//                if(fullRecipe.getExtendedIngredients().size() > 0){
//                    UploadIngredient(fullRecipe.getExtendedIngredients());
//                    UploadIngredientImage(fullRecipe.getExtendedIngredients());
//                }
                if(fullRecipe.getAnalyzedInstructions().size() > 0){
                   for(AnalyzedInstructions analyzedInstructions : fullRecipe.getAnalyzedInstructions()){
                       if(analyzedInstructions.getSteps().size() > 0) {
                           for (Step step : analyzedInstructions.getSteps()){
                               if(step.getEquipment().size() > 0) {
                                   UploadEquipmentAndCheck(step.getEquipment());
                               }
                           }
                       }
                   }
                }
            }
        }

        public void CreateRecipeByCuisineType(){
            final Gson gson = new GsonBuilder().create();

                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                mProgressDialog.show();
                final String cuisineType = CategoryTypeEnum.Thai.name();
                String urlToQuery = url + "&cuisine=" + changeSpaces(cuisineType);
                StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, urlToQuery, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        List<Recipe> recipeList;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String urlImage = jsonObject.getString("baseUri");
                            recipeList = Arrays.asList(gson.fromJson(jsonObject.getJSONArray("results").toString(), Recipe[].class));
                            String camelCaseCuisineType = StringUtils.convertToCamelCase(cuisineType);
                            camelCaseCuisineType = camelCaseCuisineType.replaceAll("\\s+","");



                            FirestoreManager.addNewRecipeByCuisine(camelCaseCuisineType, recipeList, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Log.d(TAG, "Recipes by Cuisine added to firestore");
                                    mProgressDialog.hide();
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Recipes by Cuisine Error: "+e.getMessage());
                                }
                            });

                            for ( Recipe recipe : recipeList) {
                                UploadImageParams params = new UploadImageParams();
                                params.setUrl(stringToURL(urlImage+"/"+recipe.getImage()));
                                params.setImageName(recipe.getImage());
                                mMyTask = new DownloadTask().execute(params);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.hide();
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


        public void UploadEquipmentAndCheck(List<Equipment> list){
            for(final Equipment equipment: list) {
                FirestoreManager.getEquipment(Integer.toString(equipment.getId()), new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (!task.isSuccessful()) {
                            UploadEquipment(equipment);
                            UploadEquipmentImage(equipment);
                        }
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "UploadEquipmentAndCheck Error: "+e.getMessage());
                    }
                });
            }
        }
        public void UploadIngredient(List<Ingredients> list){
            FirestoreManager.addNewIngredient(list, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d(TAG, "Ingredient added to firestore");
                    mProgressDialog.hide();
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Ingredient Error: "+e.getMessage());
                }
            });
        }

        public void UploadEquipment(Equipment list){
            FirestoreManager.addNewEquipment(list, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d(TAG, "Equipment added to firestore");
                    mProgressDialog.hide();
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Equipment Error: "+e.getMessage());
                }
            });
        }

        public void UploadIngredientImage(List<Ingredients> list){
            for(Ingredients ing : list){
                if(!ing.getImage().isEmpty()){
                    UploadImageParams params = new UploadImageParams();
                    params.setUrl(stringToURL(urlIngridient + ing.getImage()));
                    params.setImageName(ing.getName());
                    mMyTask = new DownloadTask().execute(params);
                }
            }

        }

        public void UploadEquipmentImage(Equipment equipment){
        if(!equipment.getImage().isEmpty()){
            UploadImageParams params = new UploadImageParams();
            params.setUrl(stringToURL(urlEquipement + equipment.getImage()));
            params.setImageName(equipment.getName());
            mMyTask = new DownloadTask().execute(params);
        }
    }

        public void CreateFullRecipes(List<FullRecipe> list){
            FirestoreManager.addNewFullRecipe(list, new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    Log.d(TAG, "Full Recipe added to firestore");
                    mProgressDialog.hide();
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Full Recipe Error: "+e.getMessage());
                }
            });
        }

        public void GetFullRecipe(final List<Recipe> list){
            final Gson gson = new GsonBuilder().create();
            fullRecipes = new ArrayList<>();
            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            final int listSize = list.size();
            mProgressDialog.show();
            for (Recipe recipe : list) {
                String urlToQuery = urlInformation + recipe.getId() + "/information";
                StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, urlToQuery, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        FullRecipe fullRecipe;
                        fullRecipe = gson.fromJson(response, FullRecipe.class);
                        fullRecipes.add(fullRecipe);
                        Log.d(TAG, fullRecipe.toString());
                        if(fullRecipes.size() == listSize){
                            CreateFullRecipes(fullRecipes);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
                {
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

        public void GetAllRecipes(){
            listObj = new ArrayList<>();
            FirestoreManager.getAllDessertRecipes(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        List<Recipe> list = new ArrayList<>();
                        for(DocumentSnapshot document: task.getResult()){
                            list.add(document.toObject(Recipe.class));

                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        GetFullRecipe(list);
                    }
                }
            }, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Error getting documents: "+ e.getMessage());
                }
            });
        }
        public void CreateApiCallToRecipe(){
            final Gson gson = new GsonBuilder().create();
            if (txt.getText() != null) {
                RequestQueue requestQueue = Volley.newRequestQueue(mContext);
                mProgressDialog.show();
                String urlToQuery = url + "&type=" + changeSpaces(txt.getText().toString());
                StringRequest jsonArrayRequest = new StringRequest(Request.Method.GET, urlToQuery, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        List<Recipe> recipeList;
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String urlImage = jsonObject.getString("baseUri");
                            recipeList = Arrays.asList(gson.fromJson(jsonObject.getJSONArray("results").toString(), Recipe[].class));
                            String recipeType = StringUtils.convertToCamelCase(txt.getText().toString());
                            recipeType = recipeType.replaceAll("\\s+","");



                            FirestoreManager.addNewRecipe(recipeType, recipeList, new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Log.d(TAG, "Recipes added to firestore");
                                    mProgressDialog.hide();
                                }
                            }, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "Recipes Error: "+e.getMessage());
                                }
                            });

                            for ( Recipe recipe : recipeList) {
                                UploadImageParams params = new UploadImageParams();
                                params.setUrl(stringToURL(urlImage+"/"+recipe.getImage()));
                                params.setImageName(recipe.getImage());
                                recipe.setType(recipeType);
                                mMyTask = new DownloadTask().execute(params);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mProgressDialog.hide();
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
        private class DownloadTask extends AsyncTask<UploadImageParams,Void,UploadImage> {
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
        protected void onPostExecute(final UploadImage result){
            // Hide the progress dialog
            mProgressDialog.dismiss();
            if(result!=null){
                // Save bitmap to internal storage
                saveImageToStorage(result);

            }else {
                // Notify user that an error occurred while downloading image
                Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    public String changeSpaces(String val){
       val =  val.replace(' ', '+');
       return  val.replace('_', '+');
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
    protected void saveImageToStorage(UploadImage uploadImage){
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


