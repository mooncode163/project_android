package com.moonma.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.moonma.common.DBHelper;
/**
 */
public class DBInterfaceUnity extends Object {
    static   private final String TAG = "DBInterfaceUnity";

//    static DBHelper dbHelper = null;
    public DBInterfaceUnity( ){

    }

    static public DBHelper OpenDB(String dbfile) {
//        DBHelper  dbHelper.DATABASE_NAME =dbfile;
        Log.d(TAG, "DBInterfaceUnity OpenDB dbfile="+dbfile);
//        try {
//            CopySqliteFileFromRawToDatabases(dbfile);
//        } catch (Exception e) {
//
//        }
//        if(dbHelper==null)
        {
            DBHelper  dbHelper = new DBHelper(MyApplication.main().getMainActivity(),dbfile);
            dbHelper.Open();
            return dbHelper;
        }
    }

    static public void CopyFromAsset(String dbfile) {
        Log.d(TAG, "CopyFromAsset  dbfile="+dbfile);
                try {
            CopySqliteFileFromRawToDatabases(dbfile);
        } catch (Exception e) {

        }
    }
    static public void CloseDB() {

    }
    static public void ExecSQL(Object db,String strsql)
    {
        DBHelper  dbHelper = (DBHelper)db;
        Log.d(TAG, "ExecSQL strsql="+strsql);
        if (dbHelper != null) {
            dbHelper.ExecSQL(strsql);
        }
    }

    static  public Cursor Query(Object db,String strsql)
    {
        DBHelper  dbHelper = (DBHelper)db;
        Log.d(TAG, "DBInterfaceUnity Query strsql="+strsql);
        Cursor cr = null;
        if(dbHelper!=null){
            cr =  dbHelper.Query(strsql);
        }
        return  cr;
    }

    static public String QueryTest(String strsql)
    {
        Log.d(TAG, "DBInterfaceUnity QueryTest strsql="+strsql);
        return  "return QueryTest";
    }

      public String QueryTest2(String strsql)
    {
        Log.d(TAG, "DBInterfaceUnity QueryTest2 strsql="+strsql);
        return  "return QueryTest2";
    }

    static  public   void CopySqliteFileFromRawToDatabases(String SqliteFileName) throws IOException {

        Context mContext=  MyApplication.main().getMainActivity();
        // 第一次运行应用程序时，加载数据库到data/data/当前包的名称/database/<db_name>
        String dirRoot = com.moonma.common.CommonUtils.getSDCardPath()+"/AppData/"+mContext.getPackageName();
//              dirRoot = CommonUtils.getSDCardPath()+"/app";
        dirRoot ="data/data/" + mContext.getPackageName();
        Log.d(TAG,"CopySqliteFileFromRawToDatabases :dirRoot="+dirRoot+" SqliteFileName="+SqliteFileName);

//            File dir = new File("data/data/" + mContext.getPackageName() + "/databases");
        File dir = new File(dirRoot + "/databases");
        if (!dir.exists() || !dir.isDirectory()) {
            boolean ret =  dir.mkdir();
            if(!ret){
                Log.d(TAG,"CopySqliteFileFromRawToDatabases :mkdir fail");
            }
        }

        File file = new File(dir, SqliteFileName);
        InputStream inputStream = null;
        OutputStream outputStream = null;

        //通过IO流的方式，将assets目录下的数据库文件，写入到SD卡中。
        if (!file.exists()) {
            try {
                file.createNewFile();
                inputStream = mContext.getClass().getClassLoader().getResourceAsStream("assets/GameRes/" + SqliteFileName);
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }


            } catch (IOException e) {
                e.printStackTrace();

            } finally {

                if (outputStream != null) {

                    outputStream.flush();
                    outputStream.close();

                }
                if (inputStream != null) {
                    inputStream.close();
                }

            }

        }

//            return file.getPath();

    }

}
