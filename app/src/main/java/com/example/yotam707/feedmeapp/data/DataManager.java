package com.example.yotam707.feedmeapp.data;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.example.yotam707.feedmeapp.Category;
import com.example.yotam707.feedmeapp.Course;
import com.example.yotam707.feedmeapp.GenQueue;
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
    private GenQueue<Course> coursesToAdd;
    private List<Course> coursesToAddList;
    private Map<String, List<Course>> coursesCollection;
    private List<String> groupList;
    private List<Category> categoriesList;
    private GenQueue<Steps> stepsGenQueue;
    private List<Steps> stepsList;
    private List<Ingredient> ingredientList;
    private Menu addedItemsMenu;
    private SubMenu subMenu;

    public static DataManager getInstance(){
        if(mInstance == null){
            mInstance = new DataManager();
        }
        return mInstance;
    }

    private DataManager() {
        allCourses = new ArrayList<Course>();
        coursesToAddList = new ArrayList<Course>();
        coursesToAdd = new GenQueue<Course>();
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
        //Course addedC = new Course(c.id, c.imageId, c.name,c.category, c.description, c.stepsList,c.ingredientList);
        coursesToAddList.add(c);
        coursesToAdd.enqueue(c);
        if(subMenu != null){
            subMenu.add(c.getName());
        }
    }

    public GenQueue<Course> getQueueAddedCourses(){
        return coursesToAdd;
    }

    public List<Course> getListAddedCourses(){
        return coursesToAddList;
    }

    public void removeCourse(Course c){
        int index = coursesToAdd.indexOf(c);
        coursesToAddList.remove(index);
        coursesToAdd.remove(c);
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
       if(coursesToAdd.getSize() > 0 && courseId >= 0 ){
           for(int i =0; i< coursesToAdd.getSize(); i++){
               if(coursesToAdd.getByIndex(i).getId() == courseId)
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
        stepsGenQueue = new GenQueue<Steps>();
        stepsList = new ArrayList<Steps>();
        for (int i=1; i<3; i++){
            stepsGenQueue.enqueue(new Steps(i,5000,"this is step " +i));
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

        Course[] firstsCourse = {new Course(1,R.drawable.rice, "Salad",categoriesList.get(1), "Description 1", new ArrayList<>(stepsList), ingredientList),
                new Course(2,R.drawable.rice, "EggPlant",categoriesList.get(1), "Description 2", new ArrayList<>(stepsList), ingredientList),
                new Course(3,R.drawable.rice, "Hash Brown",categoriesList.get(1), "Description 3", new ArrayList<>(stepsList), ingredientList)};



        Course[] mainCourses = { new Course(4,R.drawable.rice, "Tomato Pasta",categoriesList.get(2), "Description 4", new ArrayList<>(stepsList), ingredientList),
                new Course(5,R.drawable.rice, "Omelet",categoriesList.get(2), "Description 5", new ArrayList<>(stepsList), ingredientList),
                new Course(6,R.drawable.rice, "Fish",categoriesList.get(2), "Description 6", new ArrayList<>(stepsList), ingredientList)};



        Course[] desertCourses = {  new Course(7,R.drawable.rice, "Chocolate Cake",categoriesList.get(3), "Description 7", new ArrayList<>(stepsList), ingredientList),
                new Course(8,R.drawable.rice, "Banana Roti",categoriesList.get(3), "Description 8", new ArrayList<>(stepsList), ingredientList)};

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
