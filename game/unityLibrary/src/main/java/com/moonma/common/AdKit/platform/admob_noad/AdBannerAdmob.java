package com.moonma.common;

import android.app.Activity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.moonma.common.IAdBannerBase;
import com.moonma.common.IAdBannerBaseListener;
import com.moonma.common.AdConfigAdmob;



public class AdBannerAdmob implements IAdBannerBase {

    // 自定义单一平台广告视图对象

    private static String TAG = "AdBanner";


    IAdBannerBaseListener adBannerBaseListener;
    FrameLayout framelayout;
    Activity mainActivity;
    private   boolean sIsShow;
    private   int bannerOffsety;
    private   float bannerAlhpha;

    //  AdView adView;
    FrameLayout framelayoutAd;

    boolean isAdInit;
    public   void init(  Activity activity,FrameLayout layout)
    {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
        sIsShow = false;

    }
    public void setListener(IAdBannerBaseListener listener)
    {
        adBannerBaseListener = listener;

    }

    public void setAd()
    {
        if (adBannerBaseListener != null) {
            adBannerBaseListener.onLoadAdFail();
            return;
        }
    }



    public     void setAlpha(float alpha )
    {

        bannerAlhpha = alpha;
        if (mainActivity == null)
        {
            return;
        }
        mainActivity.runOnUiThread( new Runnable()
        {
            @Override
            public void run()
            {
                if(bannerAlhpha<1.0)
                {
                    //  bannerAlhpha =0;
                }
                framelayoutAd.setAlpha(bannerAlhpha);


            }
        } );

    }

    public     void show(boolean isShow )
    {
        sIsShow = isShow;

        if (mainActivity == null)
        {
            return;
        }



    }

    public void layoutSubView(int w,int h )
    {
        setOffsetY(bannerOffsety);

    }
    public void setOffsetY(int y )
    {

        bannerOffsety = y;

    }


}
