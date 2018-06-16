package com.example.yotam707.feedmeapp.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.example.yotam707.feedmeapp.Utils.StringUtils;
import com.example.yotam707.feedmeapp.data.Firestore.FirestoreManager;
import com.example.yotam707.feedmeapp.domain.CategoryTypeEnum;
import com.example.yotam707.feedmeapp.domain.FullRecipe;
import com.example.yotam707.feedmeapp.domain.Recipe;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yotam707 on 9/13/2016.
 */
public class DataManager {

    private final String TAG = this.getClass().getSimpleName();
    private static DataManager mInstance = null;

    //Display courses
    private List<Recipe> appetizerCourses;
    private List<Recipe> mainCourses;
    private List<Recipe> desertCourses;
    private List<Recipe> sideDishCourses;
    private Map<CourseType, List<Recipe>> coursesCollection;
    private List<Recipe> recipeListToAdd;
    public Map<String, List<Recipe>> getCategoryCourseCollection() {
        return categoryCourseCollection;
    }

    public void setCategoryCourseCollection(Map<String, List<Recipe>> categoryCourseCollection) {
        this.categoryCourseCollection = categoryCourseCollection;
    }

    private Map<String, List<Recipe>> categoryCourseCollection;
    private List<String> categories;

    private List<Course> allCourses;
	DatabaseHandler dbHandler;
    Context ctx;
	List<CourseType> groupList;
    private GenQueue<Course> coursesToAdd;
     List<Course> coursesToAddList;

    private List<Category> categoriesList;
    private GenQueue<Steps> stepsGenQueue;
    private List<Steps> stepsList;
    private List<Ingredient> ingredientList;
    private Menu addedItemsMenu;
    private SubMenu subMenu;
    private static final String DATABASE_NAME = "FeedMeDB";
    public static boolean wasInit = false;


    public static DataManager getInstance(){
        if(mInstance == null){
            mInstance = new DataManager();
        }
        return mInstance;
    }

    public static void initInstance(){
        if(!wasInit){
            mInstance = new DataManager();
            wasInit = true;
        }

    }

    private DataManager() {
        allCourses = new ArrayList<>();
        coursesToAddList = new ArrayList<>();
        coursesToAdd = new GenQueue<Course>();
        createCategoryList();
        //createCategoryRecipe();
        recipeListToAdd = new ArrayList<>();
        //createCategoryList(ctx);
        createGroupList();
        //createCollection();

        //createCollection(ctx);
 		//dbHandler = new DatabaseHandler(ctx);
        //this.ctx = ctx;

    }

//    public List<Steps> getStepsList(){
//        if(this.stepsList.size() <= 0){
//            createStepsList();
//        }
//        return this.stepsList;
//    }

    public List<Steps> getStepsList(int courseId){
        List<Steps> tempList = new ArrayList<Steps>();
        if(this.stepsList.size() <= 0){
            tempList = createStepsList(courseId);
        }
        return tempList;
    }

    public void createNavigationMenu(NavigationView navigationView){
        addedItemsMenu = navigationView.getMenu();
        subMenu = addedItemsMenu.addSubMenu("Added Items");

    }

    public void setAddedCoursesToSubMenu(){
        for(Recipe r : recipeListToAdd){
            subMenu.add(r.getTitle());
        }
    }

    public void clearAddedCourse(){
        coursesToAdd.clear();
        coursesToAddList.clear();
        subMenu.clear();
    }
    public void addCourse(Course c){
        c.setStepsList(createStepsList(c.getId()));
        c.setIngredientList(createIngredientList(c.getId()));
        //Course addedC = new Course(c.id, c.imageId, c.name,c.category, c.description, c.stepsList,c.ingredientList);
        coursesToAddList.add(c);
        coursesToAdd.enqueue(c);
        if(subMenu != null){
            subMenu.add(c.getName());
        }
    }

    public void addRecipe(Recipe e){
        recipeListToAdd.add(e);
        if(subMenu != null){
            subMenu.add(e.getTitle());
        }
    }

    public GenQueue<Course> getQueueAddedCourses(){
        return coursesToAdd;
    }


    public List<Course> getListAddedCourses(){
        return coursesToAddList;
    }

    public List<Recipe> getListAddedRecipes(){
        return recipeListToAdd;
    }

    public void removeCourse(Course c){
        int index = coursesToAdd.indexOf(c);
        coursesToAddList.remove(index);
        coursesToAdd.remove(c);
        if(subMenu != null){
            subMenu.removeItem(index);
        }
    }

    public void removeRecipe(Recipe r){
        int index = recipeListToAdd.indexOf(r);
        recipeListToAdd.remove(index);
        if(subMenu != null){
            subMenu.removeItem(index);
        }
    }

    public List<Course> getCourses() {
        return allCourses;
    }

    public Map<CourseType,List<Recipe>> getCoursesCollection(){
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
        groupList = new ArrayList<>();
        for(CourseType cType: CourseType.values()){
            groupList.add(cType);
        }
//        groupList.add(CourseType.APPETIZER);
//        groupList.add(CourseType.MAIN_COURSE);
//        groupList.add(CourseType.SIDE_DISH);
//        groupList.add(CourseType.DESSERT);
    }

    private void createCategoryRecipe(){
        categoryCourseCollection = new HashMap<>();

        for(CategoryTypeEnum type : CategoryTypeEnum.values())
            switch (type) {
                case Thai:
                    FirestoreManager.getThaiCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.Thai.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Thai documents: " + e.getMessage());
                        }
                    });
                    continue;
                case Greek:
                    FirestoreManager.getGreekCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.Greek.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Greek documents: " + e.getMessage());
                        }
                    });
                    continue;
                case Indian:
                    FirestoreManager.getIndianCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.Indian.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Indian documents: " + e.getMessage());
                        }
                    });
                    continue;
                case African:
                    FirestoreManager.getAfricanCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.African.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting African documents: " + e.getMessage());
                        }
                    });
                    continue;
                case Chinese:
                    FirestoreManager.getChineseategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.Chinese.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Chinese documents: " + e.getMessage());
                        }
                    });
                    continue;
                case Italian:
                    FirestoreManager.getItalianCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.Italian.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Italian documents: " + e.getMessage());
                        }
                    });
                    continue;
                case Mexican:
                    FirestoreManager.getMexicanCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.Mexican.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Mexican documents: " + e.getMessage());
                        }
                    });
                    continue;
                case American:
                    FirestoreManager.getAmericanCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(CategoryTypeEnum.American.name(), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting American documents: " + e.getMessage());
                        }
                    });
                    continue;
                case Middle_Eastern:
                    FirestoreManager.getMiddleEastrenCategory(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<Recipe> list = new ArrayList<>();
                                for (DocumentSnapshot document : task.getResult()) {
                                    list.add(document.toObject(Recipe.class));

                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                }
                                categoryCourseCollection.put(StringUtils.changeSpaces(CategoryTypeEnum.Middle_Eastern.name()), list);
                            }
                        }
                    }, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "Error getting Greek documents: " + e.getMessage());
                        }
                    });
                    continue;
            }
    }
    private void createCategoryList(){
        categoriesList = new ArrayList<>();
        int counter = 0;
        categoriesList.add(new Category(counter,"All" ));
        for(CategoryTypeEnum type : CategoryTypeEnum.values()){
            counter++;
            categoriesList.add(new Category(counter, StringUtils.removeUnderscoreAndSPaces(type.name())));
        }
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

//    public List<Steps> createStepsList(){
//        //stepsGenQueue = new GenQueue<Steps>();
//        stepsList = new ArrayList<Steps>();
//        for (int i=1; i<3; i++){
//            //stepsGenQueue.enqueue(new Steps(i,5000,"this is step " +i));
//            stepsList.add(new Steps(i,5000,"this is step " +i));
//        }
//        return stepsList;
//    }

    public List<Steps> createStepsList(int courseId){
        //stepsGenQueue = new GenQueue<Steps>();
        List<Steps> tempList = new ArrayList<Steps>();
        for (int i=1; i<3; i++){
            //stepsGenQueue.enqueue(new Steps(courseId,i,5000,"this is step " +i));
            tempList.add(new Steps(courseId,i,5000,"this is step " +i));
        }
        return tempList;
    }

//    private void createIngredientList(){
//        ingredientList = new ArrayList<Ingredient>();
//        for(int i=0;i<7; i++){
//            ingredientList.add(new Ingredient(i+1,"Ingredient " + i+1,i+3));
//        }
//
//    }

    private List<Ingredient> createIngredientList(int courseId){
        List<Ingredient> tempList = new ArrayList<Ingredient>();
        for(int i=0;i<7; i++){
            tempList.add(new Ingredient(courseId, i+1,"Ingredient " + i+1,i+3));
        }
        return  tempList;

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

    public Course findCourseByImage(Uri image){
        for(Course c: allCourses){
            if(c.getImage().compareTo(image)==0){
                c.setStepsList(createStepsList(c.getId()));
                c.setIngredientList(createIngredientList(c.getId()));
                return c;
            }
        }
        return null;
    }

    private void createCollection(){
        coursesCollection = new HashMap<>();
        FirestoreManager.getAllAppetizerRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    appetizerCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        appetizerCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    coursesCollection.put(CourseType.APPETIZER, appetizerCourses);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getAllMainCourseRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    mainCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        mainCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    coursesCollection.put(CourseType.MAIN_COURSE, mainCourses);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getAllDessertRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    desertCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        desertCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    coursesCollection.put(CourseType.DESSERT, desertCourses);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });

        FirestoreManager.getAllSideDishRecipes(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    sideDishCourses = new ArrayList<>();
                    for(DocumentSnapshot document: task.getResult()){
                        sideDishCourses.add(document.toObject(Recipe.class));
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }

                    coursesCollection.put(CourseType.SIDE_DISH, sideDishCourses);
                }
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error getting documents: "+ e.getMessage());
            }
        });
    }

//    private void createCollection(Context ctx) {
//        dbHandler = new DatabaseHandler(ctx);
//        //if (dbHandler.getAllCourses().size() <= 0){
//        initDb(ctx);
//        //}
//        coursesCollection = new HashMap<>();
//        List<Recipe> appetizerCourses = dbHandler.getCoursesByType(CourseType.APPETIZER);
//        List<Recipe> mainCourses = dbHandler.getCoursesByType(CourseType.MAIN_COURSE);
//        List<Recipe> desertCourses = dbHandler.getCoursesByType(CourseType.DESSERT);
//
//
//
//        for (CourseType type : groupList) {
//            int i=0;
//            if (type == CourseType.FIRST) {
//                loadChild(firstsCourse);
//
//            } else if (type == CourseType.MAIN)
//               loadChild(mainCourses);
//            else
//                loadChild(desertCourses);
//            i++;
//            Log.d("loop","loop"+i);
//            coursesCollection.put(type, courses);
//        }
//    }

//    private void initDb(Context ctx){
//        dbHandler.clearDb();
//
//        for(int x = 1; x < categoriesList.size(); x++){
//            dbHandler.addCategory(categoriesList.get(x));
//        }
//
//        Course[] firstsCourse = {new Course(1,CourseType.FIRST,R.drawable.salad,dbHandler.getImageUri(R.drawable.salad), "Salad",1,categoriesList.get(1).getName(), "Description 1", new ArrayList<Steps>(), new ArrayList<Ingredient>()),
//                new Course(2,CourseType.FIRST,R.drawable.eggplant,dbHandler.getImageUri(R.drawable.eggplant), "EggPlant",1,categoriesList.get(1).getName(), "Description 2",new ArrayList<Steps>(), new ArrayList<Ingredient>()),
//                new Course(3,CourseType.FIRST,R.drawable.hasbrownies,dbHandler.getImageUri(R.drawable.hasbrownies), "Hash Brown",1,categoriesList.get(1).getName(), "Description 3",new ArrayList<Steps>(), new ArrayList<Ingredient>())};
//
////        Course[] firstsCourse = {new Course(1,CourseType.FIRST,R.drawable.salad,dbHandler.getImageUri(R.drawable.salad), "Salad",1,categoriesList.get(1).getName(), "Description 1", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
////                new Course(2,CourseType.FIRST,R.drawable.eggplant,dbHandler.getImageUri(R.drawable.eggplant), "EggPlant",1,categoriesList.get(1).getName(), "Description 2", new ArrayList<>(stepsList), new ArrayList<>(ingredientList)),
////                new Course(3,CourseType.FIRST,R.drawable.hasbrownies,dbHandler.getImageUri(R.drawable.hasbrownies), "Hash Brown",1,categoriesList.get(1).getName(), "Description 3", new ArrayList<>(stepsList), new ArrayList<>(ingredientList))};
//
//
//        for (int i = 0; i < firstsCourse.length ; i++) {
//                List<Steps> tempList = createStepsList(firstsCourse[i].getId());
//                firstsCourse[i].setStepsList(new ArrayList<Steps>(tempList));
//            for (int j = 0; j < firstsCourse[i].getStepsList().size(); j++) {
//                dbHandler.addStep(firstsCourse[i].getStepsList().get(j));
//            }
//            List<Ingredient> tempIngList = createIngredientList(firstsCourse[i].getId());
//            firstsCourse[i].setIngredientList(new ArrayList<Ingredient>(tempIngList));
//            for (int j = 0; j < firstsCourse[i].getIngredientList().size(); j++) {
//                dbHandler.addIngredient(firstsCourse[i].getIngredientList().get(j));
//            }
//            dbHandler.addCourse(firstsCourse[i]);
//        }
//
//
//
//        Course[] mainCourses = { new Course(4,CourseType.MAIN,R.drawable.tomatopasta,dbHandler.getImageUri(R.drawable.tomatopasta), "Tomato Pasta",2,categoriesList.get(2).getName(), "Description 4",new ArrayList<Steps>(), new ArrayList<Ingredient>()),
//                new Course(5,CourseType.MAIN,R.drawable.omlette,dbHandler.getImageUri(R.drawable.omlette), "Omelet",2,categoriesList.get(2).getName(), "Description 5", new ArrayList<Steps>(), new ArrayList<Ingredient>()),
//                new Course(6,CourseType.MAIN,R.drawable.fish,dbHandler.getImageUri(R.drawable.fish), "Fish",2,categoriesList.get(2).getName(), "Description 6", new ArrayList<Steps>(), new ArrayList<Ingredient>())};
//
//        for (int i = 0; i < mainCourses.length ; i++) {
//            List<Steps> tempList =  createStepsList(mainCourses[i].getId());
//            mainCourses[i].setStepsList(new ArrayList<Steps>(tempList));
//            for (int j = 0; j < mainCourses[i].getStepsList().size(); j++) {
//                dbHandler.addStep(mainCourses[i].getStepsList().get(j));
//            }
//            List<Ingredient> tempIngList = createIngredientList(mainCourses[i].getId());
//            mainCourses[i].setIngredientList(new ArrayList<Ingredient>(tempIngList));
//            for (int j = 0; j < mainCourses[i].getIngredientList().size(); j++) {
//                dbHandler.addIngredient(mainCourses[i].getIngredientList().get(j));
//            }
//            dbHandler.addCourse(mainCourses[i]);
//        }
//
//        Course[] desertCourses = {  new Course(7,CourseType.DESSERT,R.drawable.chocolatecake,dbHandler.getImageUri(R.drawable.chocolatecake), "Chocolate Cake",3,categoriesList.get(3).getName(), "Description 7",new ArrayList<Steps>(), new ArrayList<Ingredient>()),
//                new Course(8,CourseType.DESSERT,R.drawable.bananaroti,dbHandler.getImageUri(R.drawable.bananaroti), "Banana Roti",3,categoriesList.get(3).getName(), "Description 8", new ArrayList<Steps>(), new ArrayList<Ingredient>())};
//
//        for (int i = 0; i < desertCourses.length ; i++) {
//            List<Steps> tempList = createStepsList(desertCourses[i].getId());
//            desertCourses[i].setStepsList(new ArrayList<Steps>(tempList));
//            for (int j = 0; j < desertCourses[i].getStepsList().size(); j++) {
//                dbHandler.addStep(desertCourses[i].getStepsList().get(j));
//            }
//            List<Ingredient> tempIngList = createIngredientList(desertCourses[i].getId());
//            desertCourses[i].setIngredientList(new ArrayList<Ingredient>(tempIngList));
//            for (int j = 0; j < desertCourses[i].getIngredientList().size(); j++) {
//                dbHandler.addIngredient(desertCourses[i].getIngredientList().get(j));
//
//            }
//            dbHandler.addCourse(desertCourses[i]);
//        }
//    }

//    private void loadChild(List<Course> coursesLists) {
//        courses = new ArrayList<Course>();
//        for (int i= 0; i<coursesLists.size(); i++){
//            courses.add(coursesLists.get(i));
//            System.out.println(courses.get(i).getCategoryId());
//            allCourses.add(coursesLists.get(i));
//        }
//    }

    public List<Recipe> getAppetizerCourses() {
        return appetizerCourses;
    }

    public void setAppetizerCourses(List<Recipe> appetizerCourses) {
        this.appetizerCourses = appetizerCourses;
    }

    public List<Recipe> getMainCourses() {
        return mainCourses;
    }

    public void setMainCourses(List<Recipe> mainCourses) {
        this.mainCourses = mainCourses;
    }

    public List<Recipe> getDesertCourses() {
        return desertCourses;
    }

    public void setDesertCourses(List<Recipe> desertCourses) {
        this.desertCourses = desertCourses;
    }

    public List<Recipe> getSideDishCourses() {
        return sideDishCourses;
    }

    public void setSideDishCourses(List<Recipe> sideDishCourses) {
        this.sideDishCourses = sideDishCourses;
    }
}
