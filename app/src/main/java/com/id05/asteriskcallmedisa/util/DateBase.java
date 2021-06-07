package com.id05.asteriskcallmedisa.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DateBase extends SQLiteOpenHelper {

    public DateBase(Context context) {
        super(context, "amiphone", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase amiphoneDB) {
        String SQL =
                "create table calls ("
                        + "id integer primary key autoincrement,"
                        + "name text,"
                        + "number text,"
                        + "datecall text" + ");";
        amiphoneDB.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase amiphoneDB, int i, int i1) {

    }
}
