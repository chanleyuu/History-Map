package com.example.historymap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {
        super(context, "locations.db", null, 1 );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table user(email text primary key, password text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists user");
    }

    public boolean addlocation(double locx, double locy, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("location x", locx);
        contentValues.put("location y", locy);
        contentValues.put("name", name);
        long ins = db.insert("location", null, contentValues);
        if (ins==-1) return false;
        else return true;
    }

    public double[] getlocation(String name)
    {
        double locx = 0.0, locy = 0.0;
        double[] out = {locx, locy};
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor yes = db.rawQuery("Select * from location where name=? Limit 1", new String[]{name});
        if (yes.moveToFirst()) {

            String[] columnNames = yes.getColumnNames();

            out = new double[columnNames.length];

            for (int i = 0; i < columnNames.length; i++) {

                // Assume every column is int

                out[i] = yes.getDouble(yes.getColumnIndex(columnNames[i]));
            }

        }

        return out;
    }

}
