package com.example.yotam707.feedmeapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.yotam707.feedmeapp.Category;
import com.example.yotam707.feedmeapp.Course;
import com.example.yotam707.feedmeapp.CourseType;
import com.example.yotam707.feedmeapp.Ingredient;
import com.example.yotam707.feedmeapp.Steps;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yotam707 on 11/5/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 16;

    // Database Name
    private static final String DATABASE_NAME = "FeedMeDB";

    private static final String TABLE_COURSES = "coursesTable";
    private static final String TABLE_CATEGORIES = "categoryTable";
    private static final String TABLE_STEPS = "stepsTable";
    private static final String TABLE_INGREDIENTS = "ingredientsTable";


    private static final String KEY_COURSE_ID = "course_id";
    private static final String KEY_CATEGORY_ID = "category_id";
    private static final String KEY_INGREDIENT_ID = "ingredient_id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_STEPS_NUM = "steps_num";
    private static final String KEY_TIME_IN_SECONDS = "time_in_seconds";
    private static final String KEY_QUANTITY = "quantity";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.close();
        //db.remover

        String CREATE_COURSES_TABLE = "CREATE TABLE " + TABLE_COURSES + "("
        + KEY_COURSE_ID + " INTEGER PRIMARY KEY," + KEY_TYPE + " TEXT,"
        + KEY_CATEGORY + " TEXT," + KEY_IMAGE + " TEXT," + KEY_NAME + " TEXT," + KEY_DESCRIPTION + " TEXT" + ")";

        String CREATE_CATEGORIES_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "("
                + KEY_CATEGORY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT" + ")";

        String CREATE_STEPS_TABLE = "CREATE TABLE " + TABLE_STEPS + "("
                + KEY_STEPS_NUM + " INTEGER," + KEY_TIME_IN_SECONDS + " INTEGER,"
                + KEY_DESCRIPTION + " TEXT," + KEY_COURSE_ID + " INTEGER" + ")";

        String CREATE_INGREDIENTS_TABLE = "CREATE TABLE " + TABLE_INGREDIENTS + "("
                + KEY_INGREDIENT_ID + " INTEGER," + KEY_NAME + " TEXT," + KEY_QUANTITY + " INTEGER,"
                + KEY_COURSE_ID + " INTEGER" +  ")";

        db.execSQL(CREATE_COURSES_TABLE);
        db.execSQL(CREATE_CATEGORIES_TABLE);
        db.execSQL(CREATE_STEPS_TABLE);
        db.execSQL(CREATE_INGREDIENTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
// Creating tables again
        onCreate(db);
    }


    // Getting one shop
    public Course getCourse(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, new String[] {KEY_COURSE_ID,
                        KEY_TYPE, KEY_CATEGORY,KEY_IMAGE,KEY_NAME,KEY_DESCRIPTION }, KEY_COURSE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Uri image  = Uri.parse(cursor.getString(3));
        int imageId = getImageId(image);
        Course course = new Course(Integer.parseInt(cursor.getString(0)),
                CourseType.valueOf(cursor.getString(1)),imageId ,image,cursor.getString(5),
                getCategoryIdByName(cursor.getString(4)),cursor.getString(2),cursor.getString(5),
                new ArrayList<>(getSteps(Integer.parseInt(cursor.getString(0)))),getIngredients(Integer.parseInt(cursor.getString(0))));

        return course;
    }

    public void clearDb(){
        clearDatabase(TABLE_COURSES);
        clearDatabase(TABLE_CATEGORIES);
        clearDatabase(TABLE_STEPS);
        clearDatabase(TABLE_INGREDIENTS);

    }
    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase db = this.getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }
    public List<Course> getAllCourses() {
        List<Course> coursesList = new ArrayList<Course>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_COURSES;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                int courseId = Integer.parseInt(cursor.getString(0));
                CourseType type = CourseType.valueOf(cursor.getString(1));
                String categoryName  = cursor.getString(2);
                int categoryId = getCategoryIdByName(categoryName);
                Uri image = Uri.parse(cursor.getString(3));
                int imageId = getImageId(image);
                String name = cursor.getString(4);
                String description = cursor.getString(5);
                List<Steps> steps = getSteps(courseId);
                List<Ingredient> ingredients = getIngredients(courseId);
                Course course = new Course(courseId,type,imageId,image,name,categoryId,categoryName,description,new ArrayList<>(steps),ingredients);
// Adding contact to list
                coursesList.add(course);
            } while (cursor.moveToNext());
        }

// return contact list
        return coursesList;
    }
    // Adding new shop
    public void addCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, course.getName());
        values.put(KEY_TYPE, course.getCourseType().toString());
        values.put(KEY_IMAGE, course.getImage().toString());
        values.put(KEY_NAME, course.getName());
        values.put(KEY_DESCRIPTION, course.getDescription());
        values.put(KEY_CATEGORY, course.getCategoryName().toString());
        values.put(KEY_COURSE_ID, course.getId());
// Inserting Row
        db.insert(TABLE_COURSES, null, values);
        db.close(); // Closing database connection
    }

    public void addCategory(Category category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, category.getName());
        values.put(KEY_CATEGORY_ID, category.getId());
// Inserting Row
        db.insert(TABLE_CATEGORIES, null, values);
        db.close(); // Closing database connection
    }

    public void addStep(Steps step) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_ID, step.getCourseId());
        values.put(KEY_STEPS_NUM, step.getStepNum());
        values.put(KEY_TIME_IN_SECONDS, step.getTimeImSeconds());
        values.put(KEY_DESCRIPTION, step.getDescription());
// Inserting Row
        db.insert(TABLE_STEPS, null, values);
        db.close(); // Closing database connection
    }

    public void addIngredient(Ingredient ingredient) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_INGREDIENT_ID, ingredient.getId());
        values.put(KEY_NAME, ingredient.getName());
        values.put(KEY_QUANTITY, ingredient.getQuantity());
        values.put(KEY_COURSE_ID, ingredient.getCourseId());
// Inserting Row
        db.insert(TABLE_INGREDIENTS, null, values);
        db.close(); // Closing database connection
    }

    public List<Steps> getSteps(int id) {
        List<Steps> steps = new ArrayList<Steps>();

        String selectQuery = "SELECT  * FROM " + TABLE_STEPS + " WHERE " + KEY_COURSE_ID + " = " +
                id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Steps step = new Steps(c.getInt(c.getColumnIndex(KEY_COURSE_ID)), c.getInt(c.getColumnIndex(KEY_STEPS_NUM)),
                        c.getInt(c.getColumnIndex(KEY_TIME_IN_SECONDS)),c.getString(c.getColumnIndex(KEY_DESCRIPTION)));
                steps.add(step);
            } while (c.moveToNext());
        }
        return steps;
    }

    public List<Ingredient> getIngredients(int id) {
        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENTS + " WHERE " + KEY_COURSE_ID + " = " +
                id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(c.getInt(c.getColumnIndex(KEY_COURSE_ID)), c.getInt(c.getColumnIndex(KEY_INGREDIENT_ID)),
                        c.getString(c.getColumnIndex(KEY_NAME)),c.getInt(c.getColumnIndex(KEY_QUANTITY)));
                ingredients.add(ingredient);
            } while (c.moveToNext());
        }
        return ingredients;
    }


    public Category getCategory(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COURSES, new String[] {KEY_CATEGORY_ID,
                        KEY_NAME }, KEY_CATEGORY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Category category = new Category(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
        return category;
    }

    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        int categoryId = 0;

          Cursor  cursor = db.rawQuery("SELECT * FROM " + TABLE_CATEGORIES + " WHERE " +  KEY_NAME + "=?", new String[] {categoryName + ""});

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                categoryId = Integer.valueOf(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY_ID)));
                System.out.println("Category Id = " + categoryId);
            }
        return categoryId;

    }

    public List<Course> getCoursesByType(CourseType type) {
        List<Course> typeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + KEY_TYPE + "=?", new String[]{type.toString() + ""});

            if (cursor.moveToFirst()) {
                do {
                    int courseId = Integer.parseInt(cursor.getString(0));
                    CourseType course_type = CourseType.valueOf(cursor.getString(1));
                    String categoryName = cursor.getString(2);
                    int categoryId = getCategoryIdByName(categoryName);
                    Uri image = Uri.parse(cursor.getString(3));
                    int imageId = getImageId(image);
                    String name = cursor.getString(4);
                    String description = cursor.getString(5);
                    List<Steps> steps = getSteps(courseId);
                    List<Ingredient> ingredients = getIngredients(courseId);
                    Course course = new Course(courseId, course_type,imageId, image, name, categoryId, categoryName, description, new ArrayList<>(steps), ingredients);
                    typeList.add(course);
                } while (cursor.moveToNext());
            }
            return typeList;
    }

    public Course getCourseByImage(Uri imagePath) {
        Course course = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + KEY_IMAGE + "=?", new String[]{imagePath.toString() + ""});

        if (cursor.moveToFirst()) {
            do {
                int courseId = Integer.parseInt(cursor.getString(0));
                CourseType course_type = CourseType.valueOf(cursor.getString(1));
                String categoryName = cursor.getString(2);
                int categoryId = getCategoryIdByName(categoryName);
                Uri image = Uri.parse(cursor.getString(3));
                int imageId = getImageId(image);
                String name = cursor.getString(4);
                String description = cursor.getString(5);
                List<Steps> steps = getSteps(courseId);
                List<Ingredient> ingredients = getIngredients(courseId);
                course = new Course(courseId, course_type, imageId,image, name, categoryId, categoryName, description, new ArrayList<>(steps), ingredients);
            } while (cursor.moveToNext());
        }
        return course;
    }

    public Course getCourseByName(String courseName) {
        Course course = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_COURSES + " WHERE " + KEY_NAME + "=?", new String[]{courseName.toString() + ""});

        if (cursor.moveToFirst()) {
            do {
                int courseId = Integer.parseInt(cursor.getString(0));
                CourseType course_type = CourseType.valueOf(cursor.getString(1));
                String categoryName = cursor.getString(2);
                int categoryId = getCategoryIdByName(categoryName);
                Uri image = Uri.parse(cursor.getString(3));
                int imageId = getImageId(image);
                String name = cursor.getString(4);
                String description = cursor.getString(5);
                List<Steps> steps = getSteps(courseId);
                List<Ingredient> ingredients = getIngredients(courseId);
                course = new Course(courseId, course_type,imageId, image, name, categoryId, categoryName, description, new ArrayList<>(steps), ingredients);
            } while (cursor.moveToNext());
        }
        return course;
    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }


    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static Bitmap getBitmap(int drawableRes, Context context) {
        Drawable drawable = context.getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public Uri getImageUri(int drawable_id) {
        Uri path = Uri.parse("android.resource://com.example.yotam707.feedmeapp/" + drawable_id);
        return path;
    }

   public int getImageId(Uri image_uri) {
       String path = image_uri.toString();
       String imageId = path.substring(path.lastIndexOf("/")+1);
      return Integer.valueOf(imageId);

    }

}

