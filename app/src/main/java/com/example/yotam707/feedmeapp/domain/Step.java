package com.example.yotam707.feedmeapp.domain;

import java.util.List;

public class Step {
    private int number;
    private String step;
    private List<Ingredients> ingredients;
    private List<Equipment> equipment;
    private boolean isPassive;
    private Length length;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public boolean isPssive() {
        return isPassive;
    }

    public void setPssive(boolean passive) {
        isPassive = passive;
    }

    public Length getLength() {
        return length;
    }

    public void setLength(Length length) {
        this.length = length;
    }
}
