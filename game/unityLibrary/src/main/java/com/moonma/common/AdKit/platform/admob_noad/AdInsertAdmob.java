package com.moonma.common;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener;
import com.moonma.common.AdConfigAdmob;


public class AdInsertAdmob implements IAdInsertBase  {

	//private AdView adView;

    private static String TAG = "AdInsert";


    boolean isAdInit;
   // FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

   // InterstitialAd interAd;

    private IAdInsertBaseListener adInsertBaseListener;

    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        //framelayout = layout;
        isAdInit = false;
    }


     public void setAd()
    {
 

    }
    
    public void show( )
    {
        if (adInsertBaseListener != null) {
            adInsertBaseListener.adInsertDidFail();
        }
    }

    public void setListener(IAdInsertBaseListener listener)
    {
        adInsertBaseListener = listener;
    }


    public void startSplashAd(final String strAppId, final String strAppKey) {


    }



}
