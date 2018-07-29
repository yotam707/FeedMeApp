package com.example.yotam707.feedmeapp.domain;

import com.example.yotam707.feedmeapp.ui.ui.interfaces.DetailedListItem;

import java.util.List;

public class Step implements DetailedListItem{
    private int number;
    private String step;
    private boolean visited;
    private List<Ingredients> ingredients;
    private List<Equipment> equipment;
    private List<Integer> dependent;
    private boolean isPassive;
    private PassiveTime passiveTime;
    private Length length;
    private int timeLeftToFinishRecipe;
    private int groupId;
    private int priority;
    private boolean isServingStep;
    private String fullRecipeName;
    private boolean isHot;
    private boolean isPrep;
    private boolean isCooking;

    public Step(){

    }

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

    public boolean isPassive() {
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

    @Override
    public int getListItemType() {
        return DetailedListItem.TYPE_STEP;
    }

    public List<Integer> getDependent() {
        return dependent;
    }

    public void setDependent(List<Integer> dependent) {
        this.dependent = dependent;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isServingStep() {
        return isServingStep;
    }

    public void setServingStep(boolean servingStep) {
        isServingStep = servingStep;
    }

    public String getFullRecipeName() {
        return fullRecipeName;
    }

    public void setFullRecipeName(String fullRecipeName) {
        this.fullRecipeName = fullRecipeName;
    }

    public boolean isHot() {
        return isHot;
    }

    public void setHot(boolean hot) {
        isHot = hot;
    }

    public boolean isCooking() {
        return isCooking;
    }

    public void setCooking(boolean cooking) {
        isCooking = cooking;
    }

    public boolean isPrep() {
        return isPrep;
    }

    public void setPrep(boolean prep) {
        isPrep = prep;
    }

    public int getTimeLeftToFinishRecipe() {
        return timeLeftToFinishRecipe;
    }

    public void setTimeLeftToFinishRecipe(int timeLeftToFinishRecipe) {
        this.timeLeftToFinishRecipe = timeLeftToFinishRecipe;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public PassiveTime getPassiveTime() {
        return passiveTime;
    }

    public void setPassiveTime(PassiveTime passiveTime) {
        this.passiveTime = passiveTime;
    }
}
