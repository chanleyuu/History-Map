package com.example.historymap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "markers.db", null, 2 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table mark(name text primary key, type text, locx real, locy real, description text, url text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists location");
    }

    public boolean addlocation(location l) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", l.name);
        contentValues.put("type", l.type);
        contentValues.put("locx", l.locx);
        contentValues.put("locy", l.locy);
        contentValues.put("description", l.description);
        contentValues.put("url", l.image);
        long ins = db.insert("mark", null, contentValues);
        if (ins==-1) return false;
        else return true;
    }

    public location getlocation(String type)
    {
        double locx = 0.0, locy = 0.0;
        //double[] out = {locx, locy};
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor yes = db.rawQuery("Select * from mark where type=? ", new String[]{type});
        location out = new location();
        if (yes.moveToFirst()) {
                out.type = yes.getString(yes.getColumnIndex("type"));
                out.name = yes.getString(yes.getColumnIndex("name"));
                out.locx = yes.getFloat(yes.getColumnIndex("locx"));

                out.locy = yes.getFloat(yes.getColumnIndex("locy"));
                out.description = yes.getString(yes.getColumnIndex("description"));
                out.image = yes.getString(yes.getColumnIndex("url"));
        }
        return out;
    }



}

