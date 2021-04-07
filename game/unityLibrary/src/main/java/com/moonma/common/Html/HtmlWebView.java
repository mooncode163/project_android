package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper; 
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.moonma.common.HtmlWebViewHelper;
/**
 */
public class HtmlWebView extends Object {
    static   private final String TAG = "HtmlWebView";
    static private boolean isLoading;

    public HtmlWebView( ){
        isLoading = false;
    }
  
	static public  void Load(String url)
        {
            if(isLoading){
                return;
            }
            final  String str = url;
            final Activity ac = MyApplication.main().getMainActivity();
            ac.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isLoading = true;
                    HtmlWebViewHelper  p = new HtmlWebViewHelper(ac);
                    p.Load(str);
                }
            });

        }

        static	public  void SetObjectInfo(String name,String methodFinish,String methodFail)
		{ 
	 
				 
		}
    

}
