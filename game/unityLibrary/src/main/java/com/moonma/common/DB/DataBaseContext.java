package com.moonma.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * Title:
 * Description:
 * <p>
 * Created by pei
 * Date: 2017/11/20
 */
public class DataBaseContext extends ContextWrapper {

    private Context mContext;

    public DataBaseContext(Context context){
        super(context);
        this.mContext=context;
    }

    /**重写数据库路径方法**/
    @Override
    public File getDatabasePath(String name) {
//        String path1= SDCardUtil.getDiskFilePath(name);
//        String dirPath=path1.replace(name,"");

        String dirPath="";//SDCardUtil.getInnerSDCardPath();

        String path=null;
        File parentFile=new File(dirPath);
        if(!parentFile.exists()){
            parentFile.mkdirs();
        }
        String parentPath=parentFile.getAbsolutePath();
        if(parentPath.lastIndexOf("\\/")!=-1){
            path=dirPath + File.separator + name;
        }else{
            path=dirPath+name;
        }
        File file = new File(path);

        return file;
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(),factory,errorHandler);
    }

}
