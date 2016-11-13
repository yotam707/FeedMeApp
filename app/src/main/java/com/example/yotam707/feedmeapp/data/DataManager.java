package com.example.yotam707.feedmeapp.data;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.example.yotam707.feedmeapp.Category;
import com.example.yotam707.feedmeapp.Course;
import com.example.yotam707.feedmeapp.Ingredient;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.Steps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yotam707 on 9/13/2016.
 */
public class DataManager {

    private static DataManager mInstance = null;
    private List<Course> courses;
    private List<Course> allCourses;
    private List<Course> coursesToAdd;
    private Map<String, List<Course>> coursesCollection;
    List<String> groupList;
    List<Category> categoriesList;
    List<Steps> stepsList;
    List<Ingredient> ingredientList;
    Menu addedItemsMenu;
    SubMenu subMenu;

    public static DataManager getInstance(){
        if(mInstance == null){
            mInstance = new DataManager();
        }
        return mInstance;
    }

    private DataManager() {
        allCourses = new ArrayList<Course>();
        coursesToAdd = new ArrayList<Course>();
        createStepsList();
        createIngredientList();
        createCategoryList();
        createGroupList();
        createCollection();

    }

    public void createNavigationMenu(NavigationView navigationView){
        addedItemsMenu = navigationView.getMenu();
        subMenu = addedItemsMenu.addSubMenu("Added Items");

    }
    public void addCourse(Course c){
        coursesToAdd.add(c);
        if(subMenu != null){
            subMenu.add(c.getName());
        }
    }

    public List<Course> getAddedCourses(){
        return coursesToAdd;
    }

    public void removeCourse(Course c){
        int index = coursesToAdd.indexOf(c);
        coursesToAdd.remove(index);
        if(subMenu != null){
            subMenu.removeItem(index);
        }
    }
    public List<Course> getCourses() {
        return allCourses;
    }

    public Map<String,List<Course>> getCoursesCollection(){
        return coursesCollection;
    }
    public List<Category> getCategoriesList(){
        return categoriesList;
    }

    public List<String> getGroupList(){
        return groupList;
    }

    public  int getPositionByCourseId(int courseId){
       if(coursesToAdd.size() > 0 && courseId >= 0 ){
           for(int i =0; i< coursesToAdd.size(); i++){
               if(coursesToAdd.get(i).getId() == courseId)
                   return i;
           }
       }
        return -1;
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList.add("Firsts");
        groupList.add("Main");
        groupList.add("Deserts");
    }

    private void createCategoryList(){
        categoriesList = new ArrayList<Category>();
        categoriesList.add(new Category(0,"All"));
        categoriesList.add(new Category(1,"Category A"));
        categoriesList.add(new Category(2,"Category B"));
        categoriesList.add(new Category(3,"Category C"));
    }

    private void createStepsList(){
        stepsList = new ArrayList<Steps>();
        for (int i=1; i<8; i++){
            stepsList.add(new Steps(i,5000,"this is step " +i));
        }
    }

    private void createIngredientList(){
        ingredientList = new ArrayList<Ingredient>();
        for(int i=0;i<7; i++){
            ingredientList.add(new Ingredient(i+1,"Ingredient "+(i+1),"Ingredient "+(i+1)+" description"));
        }

    }

    private void createCollection() {
        coursesCollection = new HashMap<>();

        Course[] firstsCourse = {new Course(1,R.drawable.salad, "Salad",categoriesList.get(1), "Description 1", stepsList, ingredientList),
                new Course(2,R.drawable.eggplant, "EggPlant",categoriesList.get(1), "Description 2", stepsList, ingredientList),
                new Course(3,R.drawable.hasbrownies, "Hash Brown",categoriesList.get(1), "Description 3", stepsList, ingredientList)};



        Course[] mainCourses = { new Course(4,R.drawable.tomatopasta, "Tomato Pasta",categoriesList.get(2), "Description 4", stepsList, ingredientList),
                new Course(5,R.drawable.omlette, "Omelet",categoriesList.get(2), "Description 5", stepsList, ingredientList),
                new Course(6,R.drawable.fish, "Fish",categoriesList.get(2), "Description 6", stepsList, ingredientList)};



        Course[] desertCourses = {  new Course(7,R.drawable.chocolatecake, "Chocolate Cake",categoriesList.get(3), "Description 7", stepsList, ingredientList),
                new Course(8,R.drawable.bananaroti, "Banana Roti",categoriesList.get(3), "Description 8", stepsList, ingredientList)};




        for (String course : groupList) {
            int i=0;
            if (course.equals("Firsts")) {
                loadChild(firstsCourse);
            } else if (course.equals("Main"))
                loadChild(mainCourses);
            else
                loadChild(desertCourses);
            i++;
            Log.d("loop","loop"+i);
            coursesCollection.put(course, courses);
        }
    }

    private void loadChild(Course[] coursesLists) {
        courses = new ArrayList<Course>();
        for (Course c : coursesLists){
            courses.add(c);
            allCourses.add(c);
        }
    }
}
