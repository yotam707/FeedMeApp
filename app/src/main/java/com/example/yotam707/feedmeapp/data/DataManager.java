package com.example.yotam707.feedmeapp.data;

import android.content.Context;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.Menu;
import android.view.SubMenu;
import com.example.yotam707.feedmeapp.Category;
import com.example.yotam707.feedmeapp.Course;
import com.example.yotam707.feedmeapp.CourseType;
import com.example.yotam707.feedmeapp.DB.DatabaseHandler;
import com.example.yotam707.feedmeapp.GenQueue;
import com.example.yotam707.feedmeapp.Ingredient;
import com.example.yotam707.feedmeapp.R;
import com.example.yotam707.feedmeapp.Steps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yotam707 on 9/13/2016.
 */
public class DataManager {

    private static DataManager mInstance = null;
    private List<Course> courses;
    private List<Course> allCourses;
	DatabaseHandler dbHandler;
    Context ctx;
	List<CourseType> groupList;
    private GenQueue<Course> coursesToAdd;
    private List<Course> coursesToAddList;
    private Map<CourseType, List<Course>> coursesCollection;
    //private List<String> groupList;
    private List<Category> categoriesList;
    private GenQueue<Steps> stepsGenQueue;
    private List<Steps> stepsList;
    private List<Ingredient> ingredientList;
    private Menu addedItemsMenu;
    private SubMenu subMenu;
    private static final String DATABASE_NAME = "FeedMeDB";


    public static DataManager getInstance(Context ctx){
        if(mInstance == null){
            mInstance = new DataManager(ctx);
        }
        return mInstance;
    }

    private DataManager(Context ctx) {
        allCourses = new ArrayList<Course>();
        coursesToAddList = new ArrayList<Course>();
        coursesToAdd = new GenQueue<Course>();
        createStepsList();
        createIngredientList();
        createCategoryList(ctx);
        createGroupList();
        createCollection(ctx);
 		dbHandler = new DatabaseHandler(ctx);
        this.ctx = ctx;

    }

    public List<Steps> getStepsList(){
        if(this.stepsList.size() <= 0){
            createStepsList();
        }
        return this.stepsList;
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

    public Map<CourseType,List<Course>> getCoursesCollection(){
        return coursesCollection;
    }
    public List<Category> getCategoriesList(){
        return categoriesList;
    }

    public List<CourseType> getGroupList(){
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
        groupList = new ArrayList<CourseType>();
        groupList.add(CourseType.FIRST);
        groupList.add(CourseType.MAIN);
        groupList.add(CourseType.DESSERT);
    }

    private void createCategoryList(Context ctx){
        dbHandler = new DatabaseHandler(ctx);
        categoriesList = new ArrayList<>();
        categoriesList.add(new Category(0,"All"));
        categoriesList.add(new Category(1,"Category A"));
        categoriesList.add(new Category(2,"Category B"));
        categoriesList.add(new Category(3,"Category C"));

        for (int i = 0; i < categoriesList.size(); i++) {
            dbHandler.addCategory(categoriesList.get(i));
        }
    }

    public void createStepsList(){
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
            ingredientList.add(new Ingredient(i+1,"Ingredient " + i+1,i+3));
        }

    }

    @Override
    protected void finalize() throws Throwable {
        if(dbHandler != null) {
            dbHandler.close();
            ctx.deleteDatabase(DATABASE_NAME);
        }
        if(mInstance != null)
            mInstance = null;
        super.finalize();
    }

    private void createCollection(Context ctx) {
        dbHandler = new DatabaseHandler(ctx);
        //if (dbHandler.getAllCourses().size() <= 0){
        initDb(ctx);
        //}
        coursesCollection = new HashMap<>();
        List<Course> firstsCourse = dbHandler.getCoursesByType(CourseType.FIRST);
        List<Course> mainCourses = dbHandler.getCoursesByType(CourseType.MAIN);
        List<Course> desertCourses = dbHandler.getCoursesByType(CourseType.DESSERT);



        for (CourseType type : groupList) {
            int i=0;
            if (type == CourseType.FIRST) {
                loadChild(firstsCourse);

            } else if (type == CourseType.MAIN)
               loadChild(mainCourses);
            else
                loadChild(desertCourses);
            i++;
            Log.d("loop","loop"+i);
            coursesCollection.put(type, courses);
        }
    }

    private void initDb(Context ctx){
        dbHandler.clearDb();

        for(int x = 1; x < categoriesList.size(); x++){
            dbHandler.addCategory(categoriesList.get(x));
        }

        Course[] firstsCourse = {new Course(1,CourseType.FIRST,R.drawable.salad,dbHandler.getImageUri(R.drawable.salad), "Salad",1,categoriesList.get(1).getName(), "Description 1", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
                new Course(2,CourseType.FIRST,R.drawable.eggplant,dbHandler.getImageUri(R.drawable.eggplant), "EggPlant",1,categoriesList.get(1).getName(), "Description 2", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
                new Course(3,CourseType.FIRST,R.drawable.hasbrownies,dbHandler.getImageUri(R.drawable.hasbrownies), "Hash Brown",1,categoriesList.get(1).getName(), "Description 3", new ArrayList<>(stepsList), new ArrayList<>(ingredientList))};

//        Course[] firstsCourse = {new Course(1,R.drawable.rice, "Salad",categoriesList.get(1), "Description 1", new ArrayList<>(stepsList), ingredientList),
//                new Course(2,R.drawable.rice, "EggPlant",categoriesList.get(1), "Description 2", new ArrayList<>(stepsList), ingredientList),
//                new Course(3,R.drawable.rice, "Hash Brown",categoriesList.get(1), "Description 3", new ArrayList<>(stepsList), ingredientList)};

        for (int i = 0; i < firstsCourse.length ; i++) {

            dbHandler.addCourse(firstsCourse[i]);
            for (int j = 0; j < firstsCourse[i].getStepsList().size(); j++) {
                firstsCourse[i].getStepsList().get(j).setCourseId(firstsCourse[i].getId());
                dbHandler.addStep(firstsCourse[i].getStepsList().get(j));
            }
            for (int j = 0; j < firstsCourse[i].getIngredientList().size(); j++) {
                firstsCourse[i].getIngredientList().get(j).setCourseId(firstsCourse[i].getId());
                dbHandler.addIngredient(firstsCourse[i].getIngredientList().get(j));
            }
        }



//        Course[] mainCourses = { new Course(4,R.drawable.rice, "Tomato Pasta",categoriesList.get(2), "Description 4", new ArrayList<>(stepsList), ingredientList),
//                new Course(5,R.drawable.rice, "Omelet",categoriesList.get(2), "Description 5", new ArrayList<>(stepsList), ingredientList),
//                new Course(6,R.drawable.rice, "Fish",categoriesList.get(2), "Description 6", new ArrayList<>(stepsList), ingredientList)};

        Course[] mainCourses = { new Course(4,CourseType.MAIN,R.drawable.tomatopasta,dbHandler.getImageUri(R.drawable.tomatopasta), "Tomato Pasta",2,categoriesList.get(2).getName(), "Description 4", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
                new Course(5,CourseType.MAIN,R.drawable.omlette,dbHandler.getImageUri(R.drawable.omlette), "Omelet",2,categoriesList.get(2).getName(), "Description 5", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
                new Course(6,CourseType.MAIN,R.drawable.fish,dbHandler.getImageUri(R.drawable.fish), "Fish",2,categoriesList.get(2).getName(), "Description 6", new ArrayList<>(stepsList), new ArrayList<>(ingredientList))};

        for (int i = 0; i < mainCourses.length ; i++) {
            dbHandler.addCourse(mainCourses[i]);
            for (int j = 0; j < mainCourses[i].getStepsList().size(); j++) {
                mainCourses[i].getStepsList().get(j).setCourseId(mainCourses[i].getId());
                dbHandler.addStep(mainCourses[i].getStepsList().get(j));
            }
            for (int j = 0; j < mainCourses[i].getIngredientList().size(); j++) {
                mainCourses[i].getIngredientList().get(j).setCourseId(mainCourses[i].getId());
                dbHandler.addIngredient(mainCourses[i].getIngredientList().get(j));
            }
        }
//
//        Course[] desertCourses = {  new Course(7,R.drawable.rice, "Chocolate Cake",categoriesList.get(3), "Description 7", new ArrayList<>(stepsList), ingredientList),
//                new Course(8,R.drawable.rice, "Banana Roti",categoriesList.get(3), "Description 8", new ArrayList<>(stepsList), ingredientList)};
//

        Course[] desertCourses = {  new Course(7,CourseType.DESSERT,R.drawable.chocolatecake,dbHandler.getImageUri(R.drawable.chocolatecake), "Chocolate Cake",3,categoriesList.get(3).getName(), "Description 7", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
                new Course(8,CourseType.DESSERT,R.drawable.bananaroti,dbHandler.getImageUri(R.drawable.bananaroti), "Banana Roti",3,categoriesList.get(3).getName(), "Description 8", new ArrayList<>(stepsList), new ArrayList<>(ingredientList))};

        for (int i = 0; i < desertCourses.length ; i++) {
            dbHandler.addCourse(desertCourses[i]);
            for (int j = 0; j < desertCourses[i].getStepsList().size(); j++) {
                desertCourses[i].getStepsList().get(j).setCourseId(desertCourses[i].getId());
                dbHandler.addStep(desertCourses[i].getStepsList().get(j));
            }
            for (int j = 0; j < desertCourses[i].getIngredientList().size(); j++) {
                desertCourses[i].getIngredientList().get(j).setCourseId(desertCourses[i].getId());
                dbHandler.addIngredient(desertCourses[i].getIngredientList().get(j));

            }
        }
    }

    private void loadChild(List<Course> coursesLists) {
        courses = new ArrayList<Course>();
        for (int i= 0; i<coursesLists.size(); i++){
            courses.add(coursesLists.get(i));
            System.out.println(courses.get(i).getCategoryId());
            allCourses.add(coursesLists.get(i));
        }
    }
}
