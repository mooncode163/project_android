package com.moonma.common;

import android.app.Activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.moonma.common.IAdInsertBase;
import com.moonma.common.IAdInsertBaseListener;
import com.moonma.common.AdConfigBaidu;


import com.baidu.mobads.AdSize;
import com.baidu.mobads.InterstitialAd;
import com.baidu.mobads.InterstitialAdListener;

public class AdInsertBaidu implements IAdInsertBase, InterstitialAdListener {

    // 自定义单一平台广告视图对象
    // private AdView adView;
    static boolean isFistRun = true;
    private static String TAG = "AdInsert";

    public boolean isUseActivity = false;// true
    boolean isAdInit;
    FrameLayout framelayout;
    Activity mainActivity;
    private boolean sIsShow;
    private int bannerOffsety;
    private float bannerAlhpha;
    String strAdAppId;
    // InterstitialAd interAd;

    private IAdInsertBaseListener adInsertBaseListener;

    private InterstitialAd mInterAd;            // 插屏广告实例，支持单例模式
    private String mAdType = "interAd";         // 插屏广告的类型，Demo使用，避免重复创建广告实例

    private boolean isAdForVideo = false;       // 视频插屏广告
    private RelativeLayout mVideoAdLayout;      // 展示视频插屏的布局
    private RelativeLayout.LayoutParams reLayoutParams;
    private boolean isQianTiePianAd = true;     // 前贴片广告 or 暂停页广告

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        framelayout = layout;
        isAdInit = false;
    }

    public void setAd() {
       
        if(isAdInit==false)
        {
            isAdInit = true;
            String strAppId = AdConfigBaidu.main().appId;
            String strAppKey = AdConfigBaidu.main().appKeyInsert;
//            strAppKey = "2403633";
            Log.i(TAG, "insert id="+strAppId+ " key="+strAppKey);
            createInterstitialAd(strAppKey);
            loadAd();
            if(isFistRun)
            {
                isFistRun = false;
                //            baid sdk bug 第一次需要延迟加载 不然load一直卡住不显示广告
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "postDelayed  loadAd");
                        loadAd();
                    }
                };
//        5000 3000
                handler.postDelayed(runnable, 4000);

            }
        }
    }

    public void show() {
//        if (mInterAd.isAdReady()) {
//            showAd();
//        } else {
////                    createInterstitialAd();
//            loadAd();
//        }

        Log.i(TAG, "show for loadAd");
        loadAd();


//        Intent intent=new Intent(mainActivity, com.moonma.common.InterstitialAdActivity.class);  //方法1
//        mainActivity.startActivity(intent);
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "postDelayed  loadAd");
                loadAd();
            }
        };
//        5000 3000
        handler.postDelayed(runnable, 1000);
    } 
 

    public void setListener(IAdInsertBaseListener listener) {
        adInsertBaseListener = listener;
    }


      /**
     * 创建广告实例，支持：插屏、前贴片、暂停页
     */
    private void createInterstitialAd(String adPlaceId) { 
        if (isAdForVideo) {
            if (isQianTiePianAd) {
                if (!"qianTiePian".equals(mAdType)) {
                    // 创建前贴片广告
                    mInterAd = new InterstitialAd(mainActivity, AdSize.InterstitialForVideoBeforePlay, adPlaceId);
                    mInterAd.setListener(this);
                    mAdType = "qianTiePian";
                }
            } else {
                // 创建暂停页广告
                if (!"zanTingYe".equals(mAdType)) {
                    mInterAd = new InterstitialAd(mainActivity, AdSize.InterstitialForVideoPausePlay, adPlaceId);
                    mInterAd.setListener(this);
                    mAdType = "zanTingYe";
                }
            }
        } else {
            // 创建插屏广告
            if (mInterAd == null || !"interAd".equals(mAdType)) {
                mInterAd = new InterstitialAd(mainActivity, adPlaceId);
                mInterAd.setListener(this);
                mAdType = "interAd";
            }
        }
    }

    /**
     * 加载广告
     */
    private void loadAd() {
        if (isAdForVideo) {
            int width = 0;//getValueById(R.id.edit_width);
            int height =0;// getValueById(R.id.edit_height);
            if (width <= 0 || height <= 0) {
                width = 600;
                height = 500;
            }
            reLayoutParams.width = width;
            reLayoutParams.height = height;
            mInterAd.loadAdForVideoApp(width, height);
        } else {
            mInterAd.loadAd();
        }
    }

    /**
     * 展现广告
     */
    private void showAd() {
        if (isAdForVideo) {
            mInterAd.showAdInParentForVideoApp(mainActivity, mVideoAdLayout);
        } else {
            mInterAd.showAd(mainActivity);
        }
    }

    @Override
    public void onAdClick(InterstitialAd arg0) {
        Log.i(TAG, "onAdClick");
    }

    @Override
    public void onAdDismissed() {
        Log.i(TAG, "onAdDismissed");
       // mButton.setText("加载插屏广告");
        if (!isAdForVideo) {
            loadAd();
        }

        if (adInsertBaseListener!=null){
            adInsertBaseListener.adInsertDidClose();

        }
    }

    @Override
    public void onAdFailed(String arg0) {
        Log.i(TAG, "onAdFailed arg0="+arg0);
        if (adInsertBaseListener != null) {
            adInsertBaseListener.adInsertDidFail();

        }
    }

    @Override
    public void onAdPresent() {
        Log.i(TAG, "onAdPresent");
        if (adInsertBaseListener!=null){
            adInsertBaseListener.adInsertWillShow();
        }
    }

    @Override
    public void onAdReady() {
        Log.i(TAG, "onAdReady");
      //  mButton.setText("展现插屏广告");
        showAd();
    }

    private void initAdControlView() {
      //  CheckBox mCheckVideo = findViewById(R.id.check_video_ad);
      //  RadioGroup videoType = findViewById(R.id.video_ad_type);
//        videoType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                isQianTiePianAd = (checkedId == R.id.qian_tie_pian);
//            }
//        });

//        final LinearLayout adSizeEditView = findViewById(R.id.ad_xy_size);
//
//        final RadioButton btn1 = findViewById(R.id.qian_tie_pian);
//        final RadioButton btn2 = findViewById(R.id.zan_ting_ye);
//        mCheckVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                isAdForVideo = isChecked;
//                mAdPlaceIdView.setText(isAdForVideo ? YOUR_VIDEO_AD_PLACE_ID : YOUR_AD_PLACE_ID);
//                btn1.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
//                btn2.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
//                adSizeEditView.setVisibility(isAdForVideo ? View.VISIBLE : View.GONE);
//            }
//        });
    }

    private int getValueById(int viewId) {
//        EditText editText = findViewById(viewId);
//        String value = editText.getText().toString();
//        if (value.length() > 0) {
//            try {
//                return Integer.valueOf(value);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        return 0;
    }
 
}
