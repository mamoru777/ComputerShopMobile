package com.example.computershopmobile.Database.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.computershopmobile.Database.DBHelper;
import com.example.computershopmobile.Models.GoodId;

import java.util.ArrayList;

public class GoodIdStorage {
    DBHelper sqlHelper;
    SQLiteDatabase db;
    final String TABLE = "GoodId";
    final String COLUMN_ID = "id";
    final String GOOD_ID = "good_id";
    final String USER_ID = "user_id";
    public GoodIdStorage(Context context)
    {
        sqlHelper = new DBHelper(context);
        db = sqlHelper.getWritableDatabase();
    }
    public void close() {
        db.close();
    }
    public void create(GoodId goodId) {
        ContentValues content = new ContentValues();
        content.put(GOOD_ID, goodId.getGoodid());
        content.put(USER_ID, goodId.getUserid());
        db.insert(TABLE, null, content);
    }
    public ArrayList<GoodId> getFullList(String userId) {
        Cursor cursor = db.rawQuery("select * from " + TABLE + " where " + USER_ID + " = '" + userId + "'", null);
        ArrayList<GoodId> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }
        do {
            GoodId obj = new GoodId();
            obj.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            obj.setGoodid(cursor.getString((int) cursor.getColumnIndex(GOOD_ID)));
            obj.setUserid(cursor.getString((int) cursor.getColumnIndex(USER_ID)));
            list.add(obj);
        }
        while (cursor.moveToNext());
        cursor.close();
        return list;
    }

    public GoodId get(int id){
        Cursor cursor = db.rawQuery( "Select * from " + TABLE + " where " + COLUMN_ID + " = " + id , null);
        if (!cursor.moveToFirst())
        {
            return null;
        }
        do {
            GoodId model = new GoodId();
            model.setId(cursor.getInt((int) cursor.getColumnIndex(COLUMN_ID)));
            model.setGoodid(cursor.getString((int) cursor.getColumnIndex(GOOD_ID)));
            model.setGoodid(cursor.getString((int) cursor.getColumnIndex(USER_ID)));
            return model;
        } while (
                cursor.moveToNext()
        );
    }

    public void update(GoodId goodId) {
        ContentValues content = new ContentValues();
        content.put(COLUMN_ID, goodId.getId());
        content.put(GOOD_ID, goodId.getGoodid());
        content.put(USER_ID, goodId.getUserid());
        String where = COLUMN_ID + " = " + goodId.getId();
        db.update(TABLE, content, where, null);
    }

    public void delete(GoodId goodId) {
        String where = COLUMN_ID + " = " + goodId.getId();
        db.delete(TABLE, where, null);
    }
    public void clear() {
        db.delete(TABLE, null, null);
    }
}
