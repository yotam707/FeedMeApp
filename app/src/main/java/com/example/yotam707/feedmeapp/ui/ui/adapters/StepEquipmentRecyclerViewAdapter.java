package com.example.yotam707.feedmeapp.ui.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.yotam707.feedmeapp.GlideApp;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreStorageManager;
import com.example.yotam707.feedmeapp.domain.Equipment;

import java.util.List;

public class StepEquipmentRecyclerViewAdapter extends RecyclerView.Adapter<StepEquipmentRecyclerViewAdapter.ViewHolder>  {

    private final String TAG = this.getClass().getSimpleName();
    private List<Equipment> equipmentList;
    private Context mContext;

    public StepEquipmentRecyclerViewAdapter(Context mContext){
        this.mContext = mContext;
    }
    public void SetEquipment(List<Equipment> equipmentList){
        this.equipmentList = equipmentList;
    }

    @NonNull
    @Override
    public StepEquipmentRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cooking_equip_item, parent, false);
        return new StepEquipmentRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepEquipmentRecyclerViewAdapter.ViewHolder holder, int position) {
        if(equipmentList != null) {
            final Equipment ing = equipmentList.get(position);
            GlideApp
                    .with(mContext)
                    .load(FirestoreStorageManager.storageReference.child(ing.getName()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(holder.image);

            holder.equipmentName.setText(ing.getName());

        }
    }

    @Override
    public int getItemCount() {
        if(equipmentList != null)
            return this.equipmentList.size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView equipmentName;
        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cooking_equipment_image);
            equipmentName = itemView.findViewById(R.id.cooking_equipment_title);
        }
    }
}
