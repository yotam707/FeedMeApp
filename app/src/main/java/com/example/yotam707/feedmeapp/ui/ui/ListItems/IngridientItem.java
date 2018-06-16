package com.example.yotam707.feedmeapp.ui.ui.ListItems;

import com.example.yotam707.feedmeapp.ui.ui.interfaces.DetailedListItem;

public class IngridientItem implements DetailedListItem {
    private String quantity;
    private String ingredient;

    public IngridientItem(String quantity, String ingredient){
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    @Override
    public int getListItemType() {
        return DetailedListItem.TYPE_INGRIDIENT;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getIngridient() {
        return ingredient;
    }

    public void setIngridient(String ingridient) {
        this.ingredient = ingridient;
    }
}
