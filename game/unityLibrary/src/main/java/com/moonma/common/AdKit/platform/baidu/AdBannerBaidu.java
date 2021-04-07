package com.moonma.common;

import android.app.Activity;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.moonma.common.IAdBannerBase;
import com.moonma.common.IAdBannerBaseListener;
import com.moonma.common.AdConfigBaidu;


import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;
import com.baidu.mobads.AppActivity;


import org.json.JSONObject;


public class AdBannerBaidu implements IAdBannerBase {

    // 自定义单一平台广告视图对象
    private static String TAG = "AdBanner";

    FrameLayout framelayout;
    RelativeLayout framelayoutAd;
    Activity mainActivity;
    // AdView adView;
    IAdBannerBaseListener adBannerBaseListener;
    boolean isAdInit;
    private boolean sIsShow;
    private int bannerOffsety;
    private float bannerAlhpha;
    float heightRatioMin = 0.15f;
    static int heightAd;
    int widthAd;
    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
        sIsShow = false;
        bannerOffsety = 0;

    }

    public void setAd() {
        if (isAdInit == false) {
            isAdInit = true;
            String strAppId = AdConfigBaidu.main().appId;
            String strAppKey = AdConfigBaidu.main().appKeyBanner;

            RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            rllp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            // rllp.addRule(RelativeLayout.CENTER_HORIZONTAL);

            // FrameLayout
            ViewGroup.LayoutParams framelayout_params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
//            if (Device.isLandscape()) {
//                int screen_h = framelayout.getHeight();
//                // 横屏 显示面积过大 缩小一些
//                int h_adbanner = (int) (heightRatioMin * screen_h);
//                framelayout_params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, h_adbanner);
//            }
            framelayoutAd = new RelativeLayout(mainActivity);
            framelayoutAd.setLayoutParams(framelayout_params);

            framelayout.addView(framelayoutAd);

            Log.i(TAG, "banner id=" + strAppId + " key=" + strAppKey);
            bindBannerView(framelayoutAd, strAppKey, 20, 3);

        }
        show(true);
    }


    public void layoutSubView(int w, int h) {
        setOffsetY(bannerOffsety);

    }

    public void setAlpha(float alpha) {

        bannerAlhpha = alpha;
        if (mainActivity == null) {
            return;
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bannerAlhpha < 1.0) {
                    // bannerAlhpha =0;
                }
                framelayoutAd.setAlpha(bannerAlhpha);

            }
        });

    }

    private void doCloseBanner() {
        framelayoutAd.removeAllViews();
//        if (bv != null) {
//            bv.destroy();
//            bv = null;
//        }
    }

    public void show(boolean isShow) {
        sIsShow = isShow;

        if (mainActivity == null) {
            return;
        }

        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (framelayoutAd == null) {
                    return;
                }
                if (sIsShow) {

                   // bv.setVisibility(View.VISIBLE);
                    framelayoutAd.setVisibility(View.VISIBLE);

                } else {
                   // bv.setVisibility(View.INVISIBLE);
                    framelayoutAd.setVisibility(View.INVISIBLE);
                    // framelayoutAd.removeAllViews();
                    // doCloseBanner();
                }
            }
        });

    }



    public void setOffsetYInternal() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int screen_w = framelayout.getWidth();
                int screen_h = framelayout.getHeight();
                int h_adbanner = heightAd;
                Log.i(TAG, "setOffsetYInternal widthAd=" + widthAd + " heightAd=" + heightAd);
                int x = (screen_w - widthAd) / 2;
                int oft_bottom = com.moonma.common.ScreenUtil.getBottomNavigationBarHeight();
                if (Device.isLandscape()) {
                    // 华为全面屏 底部导航栏 横屏时候现在在右边
                    oft_bottom = 0;
                }
                int y = screen_h - h_adbanner - bannerOffsety
                        - oft_bottom;

                // y = 128;
                // framelayoutAd.setX(x);
                framelayoutAd.setY(y);


                if (adBannerBaseListener != null) {
                    adBannerBaseListener.onReceiveAd(widthAd, h_adbanner);
                }
            }
        });

    }

    public void setOffsetY(int y) {

        bannerOffsety = y;
        //
        //
        if ((framelayoutAd == null) || (framelayout == null) || (mainActivity == null)) {
            return;
        }
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (framelayoutAd == null) {
                    return;
                }
                if (framelayoutAd.getVisibility() != View.VISIBLE) {
                    return;
                }
                widthAd = framelayoutAd.getWidth();
                heightAd = framelayoutAd.getHeight();


                setOffsetYInternal();

            }

        });



    }

    public void setListener(IAdBannerBaseListener listener) {
        adBannerBaseListener = listener;
    }

    /**
     * 创建横幅广告的View，并添加至接界面布局中
     * 注意：只有将AdView添加到布局中后，才会有广告返回
     */
    private void bindBannerView(final RelativeLayout yourOriginalLayout,
                                String adPlaceId, int scaleWidth, int scaleHeight) {
        AdView adView = new AdView(mainActivity, adPlaceId);
        // 设置监听器
        adView.setListener(new AdViewListener() {
            @Override
            public void onAdSwitch() {
                Log.w(TAG, "onAdSwitch");
            }

            @Override
            public void onAdShow(JSONObject info) {
                // 广告已经渲染出来
                Log.w(TAG, "onAdShow " + info.toString());
                setOffsetY(bannerOffsety);
            }

            @Override
            public void onAdReady(AdView adView) {
                // 资源已经缓存完毕，还没有渲染出来
                Log.w(TAG, "onAdReady " + adView);
            }

            @Override
            public void onAdFailed(String reason) {
                Log.w("", "onAdFailed " + reason);
//                RelativeLayout.LayoutParams rllp = new RelativeLayout
//                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                rllp.addRule(RelativeLayout.CENTER_IN_PARENT);
//  yourOriginalLayout.addView(mNoDataView, rllp);
                if (adBannerBaseListener != null) {
                    adBannerBaseListener.onLoadAdFail();
                }
            }

            @Override
            public void onAdClick(JSONObject info) {
                // Log.w(TAG, "onAdClick " + info.toString());
            }

            @Override
            public void onAdClose(JSONObject arg0) {
                Log.w(TAG, "onAdClose");
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) mainActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        int winW = dm.widthPixels;
        int winH = dm.heightPixels;
        int width = Math.min(winW, winH);
        int height = width * scaleHeight / scaleWidth;
        // 将adView添加到父控件中(注：该父控件不一定为您的根控件，只要该控件能通过addView能添加广告视图即可)
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(width, height);
        rllp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        yourOriginalLayout.addView(adView, rllp);
    }


}
