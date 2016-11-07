package com.example.yotam707.feedmeapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yotam707 on 11/5/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "feedMeManager";

    // Contacts table name
    private static final String TABLE_FEEDME = "feed_me";

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
