package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.example.yotam707.feedmeapp.ApiActivity;
import com.example.yotam707.feedmeapp.CourseType;
import com.example.yotam707.feedmeapp.DetailActivity;
import com.example.yotam707.feedmeapp.GlideApp;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.data.DataManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ImageScrollRecyclerViewAdapter extends RecyclerView.Adapter<ImageScrollRecyclerViewAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();

    //vars
    private List<Recipe> recipeList;
    private Context mContext;
    private CourseType type;

    public ImageScrollRecyclerViewAdapter(List<Recipe> recipeList, CourseType type, Context mContext) {
        this.recipeList = recipeList;
        this.mContext = mContext;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.type_recipe_image_display, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Recipe currentRecipe = recipeList.get(position);
        final int recipeId = currentRecipe.getId();
        GlideApp
                .with(mContext)
                .load(FirestoreStorageManager.storageReference.child(currentRecipe.getImage()))
                .fitCenter()
                .into(holder.image);

        holder.name.setText(currentRecipe.getTitle());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("courseId", Integer.toString((recipeId)));
                intent.putExtra("type", type.name());
                mContext.startActivity(intent);
                Log.d(TAG, "OnClick: clicked on an image");
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Add Course");
                builder.setMessage(currentRecipe.getTitle() + "\n" +currentRecipe.getType());
                builder.setCancelable(false);
                builder.setPositiveButton("Add",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirestoreManager.addSelectedCourse(currentRecipe, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(mContext, "Recipe Added", Toast.LENGTH_LONG).show();
                                        DataManager.getInstance().addRecipe(currentRecipe);
                                        Log.d(TAG, "added successfully");
                                    }
                                }, new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, " error adding : " +e.getMessage());
                                    }
                                });
                            }
                        });
                builder.setNegativeButton("Back",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView name;
        ImageView add;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.name);
            add = itemView.findViewById(R.id.lw_add);
        }
    }
}
