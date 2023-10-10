package com.example.computershopmobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Kursach.db";
    private static final int SCHEMA = 1;
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE IF NOT EXISTS GoodId (\n" + "    id integer PRIMARY KEY AUTOINCREMENT,\n" + "     good_id TEXT,\n" + "     user_id TEXT);\n");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        onCreate(db);
    }

    public void delete(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
