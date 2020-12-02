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
        db.execSQL("create table mark(name text primary key, locx real, locy real, description text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists location");
    }

    public boolean addlocation(double locx, double locy, String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("locx", locx);
        contentValues.put("locy", locy);
        contentValues.put("description", description);
        long ins = db.insert("mark", null, contentValues);
        if (ins==-1) return false;
        else return true;
    }

    public location getlocation(double inx, double iny)
    {
        double locx = 0.0, locy = 0.0;
        //double[] out = {locx, locy};
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor yes = db.rawQuery("Select * from mark where locx>? and locx - 10<? or" +
                " locx<? and locx + 10>? or" +
                " locy>? and locy - 10<? or" +
                " locy<? and locy + 10>? Limit 1", new String[]{String.valueOf(inx), String.valueOf(iny)});
        location out = new location();
        if (yes.moveToFirst()) {
                out.name = yes.getString(yes.getColumnIndex("name"));
                out.locx = yes.getFloat(yes.getColumnIndex("locx"));

                out.locy = yes.getFloat(yes.getColumnIndex("locy"));
                out.description = yes.getString(yes.getColumnIndex(""));
        }
        return out;
    }



}

