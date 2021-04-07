package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
 
import com.unity3d.player.UnityPlayer;

import java.io.File;
 
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by jaykie on 16/5/24.
 */
public class Share  {
    public final static String SOURCE_WEIXIN = "weixin";
    public final static String SOURCE_WEIXINFRIEND = "weixinfriend";
    public final static String SOURCE_QQ = "qq";
    public final static String SOURCE_QQZONE = "qqzone";
    public final static String SOURCE_WEIBO = "weibo";
    public final static String SOURCE_EMAIL = "email";
    public final static String SOURCE_SMS = "sms";

    private static final String TAG = Share.class.getSimpleName();

    private static Activity sActivity = null;
    private static Share pthis;


    private static boolean sInited = false;

    private static String unityObjName;
    private static String unityObjMethod;

     String strSource;
     String strAppId;
     String strAppKey;
    String strTitle;
    String strDetail;
    String strUrl;

    String strShareResult;


    public void init(final Activity activity) {

        pthis = this;
        sActivity = activity; 
    }
 
    public static void ShareInit(final String source,final String appId,final String appKey)
    {
        pthis.strSource = source;
        pthis.strAppId = appId;
        pthis.strAppKey = appKey;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.ShareInit_nonstatic(pthis.strSource, pthis.strAppId,pthis.strAppKey);
            }
        });
    }
    public  void ShareInit_nonstatic(final String source,final String appId,final String appKey)
    {

    }

    public static void SetObjectInfo(final String objName) {
        pthis.unityObjName = objName;
    }

    public static void InitPlatform(final String source,final String appId,final String appKey)
    {
        pthis.strSource = source;
        pthis.strAppId = appId;
        pthis.strAppKey = appKey;
        pthis.InitPlatform_nonstatic(pthis.strSource, pthis.strAppId,pthis.strAppKey);
    }
    public  void InitPlatform_nonstatic(final String source,final String appId,final String appKey)
    { 
    }

    public static void ShareImage(final String source,final String pic,final String url)
    {

    }
    
    public static void ShareImageText(final String source,final String title,final String pic,final String url)
    {
        
    }

    public static void ShareWeb(final String source,final String title,final String detail,final String url)
    {
        pthis.strSource = source;
        pthis.strTitle = title;
        pthis.strDetail = detail;
        pthis.strUrl = url;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pthis.ShareWeb_nonstatic(pthis.strSource, pthis.strTitle,pthis.strDetail,pthis.strUrl);
            }
        });
    }

    public  void ShareWeb_nonstatic(final String source,final String title,final String detail,final String url)
    {
     
    }


    public void ShareDidFinish(String str) {
        Log.e(TAG,str);
        strShareResult = str;
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName, "ShareDidFinish", strShareResult);
            }
        });

    }

    public void ShareDidFail(String str) {
        strShareResult = str;
        Log.e(TAG,str);
        sActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                UnityPlayer.UnitySendMessage(unityObjName, "ShareDidFail", strShareResult);
            }
        });

    }

 



}
