package com.example.yotam707.feedmeapp.ui.ui.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.domain.Ingredients;
import com.example.yotam707.feedmeapp.ui.ui.adapters.RecipeIngredientsDialogRecyclerView;

import java.util.List;

public class IngredientsFragmentDialog extends DialogFragment {
    RecyclerView rv;
    RecipeIngredientsDialogRecyclerView adapter;
    List<Ingredients> extendedIngredients;

    public void setExtendedIngredients(List<Ingredients> extendedIngredients) {
        this.extendedIngredients = extendedIngredients;
    }

    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredients_dialog, container, false);
        (rootView.findViewById(R.id.button_close_dialog)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        rootView.findViewById(R.id.button_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                getActivity().findViewById(R.id.btn_start).callOnClick();
            }
        });
        rv = rootView.findViewById(R.id.ingredient_cooking_rv);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rv.setNestedScrollingEnabled(true);
        rv.setHasFixedSize(true);
        //ADAPTER
        adapter = new RecipeIngredientsDialogRecyclerView(extendedIngredients, this.getActivity());
        rv.setAdapter(adapter);
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}