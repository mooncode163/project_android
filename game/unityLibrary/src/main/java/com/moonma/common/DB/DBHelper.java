package com.moonma.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;


import com.moonma.common.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;



    public class DBHelper extends SQLiteOpenHelper {
    private final String TAG = this.getClass().toString();
//        public static  String DATABASE_NAME = "Idiom.db";//数据库名字
    private static final String TABLE_FACE = "TableIdiom";

    private static final int DATABASE_VERSION = 1;//数据库版本号
//    private static final String SQL_CREATE_TABLE_FACE = "create table " + TABLE_FACE + " ("
//            + "id text primary key,"//autoincrement
//            + "name text, "
//            + "time ong, "
//            + "pic text )";//数据库里的表 data blob

    public SQLiteDatabase dbOpen;
          Context mContext;

    public DBHelper(Context context,String dbfile) {
        this(context, dbfile, null, DATABASE_VERSION);
        mContext = context;
//        try {
//            CopySqliteFileFromRawToDatabases(dbfile);
//        } catch (Exception e) {
//
//        }
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);//调用到SQLiteOpenHelper中
        mContext = context;
        Log.d(TAG, "New CustomSQLiteOpenHelper");


    }

    public void Open() {
        if (dbOpen == null) {
            dbOpen = this.getWritableDatabase();//创建数据库
            //dbOpen.execSQL(SQL_CREATE_TABLE_FACE);
        }
    }

    public void Close() {
        this.close();
    }

    //在调getReadableDatabase或getWritableDatabase时，会判断指定的数据库是否存在，不存在则调SQLiteDatabase.create创建， onCreate只在数据库第一次创建时才执行
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
//        db.execSQL(SQL_CREATE_TABLE_FACE);
        //dbOpen = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

        public Cursor Query(String strsql)
        {
            Log.d(TAG, "Query strsql="+strsql);
            Cursor cr =null;
            if (dbOpen != null) {
                cr = dbOpen.rawQuery(strsql, null);
            }

            Log.d(TAG, "Query count="+cr.getCount());
            return cr;
        }

        public void ExecSQL(String strsql)
        {
            Log.d(TAG, "ExecSQL strsql="+strsql);
            if (dbOpen != null) {
                  dbOpen.execSQL(strsql);
            }
        }


    }
