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

import com.moonma.common.IAdVideoBase;
import com.moonma.common.IAdVideoBaseListener;
import com.moonma.common.AdConfigBaidu;

import com.baidu.mobads.rewardvideo.RewardVideoAd;

public class AdVideoBaidu implements IAdVideoBase, RewardVideoAd.RewardVideoAdListener {

    private static String TAG = "AdVideo";

    String strAdAppId;
    boolean isAdInit;
    // FrameLayout framelayout;
    Activity mainActivity;
    private boolean sIsShow;
    private int bannerOffsety;
    private float bannerAlhpha;

    int adType;
    private IAdVideoBaseListener adVideoBaseListener;

    public RewardVideoAd mRewardVideoAd;

    public void init(Activity activity, FrameLayout layout) {
        mainActivity = activity;
        // framelayout = layout;
        isAdInit = false;
    }

    public void setType(int type) {
        adType = type;
    }

    public void setAd() {
        if (isAdInit == false) {
            isAdInit = true;
            String strAppId = AdConfigBaidu.main().appId;
            String strAppKey = AdConfigBaidu.main().appKeyVideo;
            Log.i(TAG, "video id="+strAppId+ " key="+strAppKey);
            mRewardVideoAd = new RewardVideoAd(mainActivity,
                    strAppKey, this, true);

        }

    }

    public void show() {
        mRewardVideoAd.load();

    }

    public void setListener(IAdVideoBaseListener listener) {
        adVideoBaseListener = listener;
    }


    @Override
    public void onVideoDownloadSuccess() {
        // 视频缓存成功
        // 说明：如果想一定走本地播放，那么收到该回调之后，可以调用show
        Log.i(TAG, "onVideoDownloadSuccess,isReady=" + mRewardVideoAd.isReady());

        boolean isReady = mRewardVideoAd != null && mRewardVideoAd.isReady();
        if (isReady) {
            mRewardVideoAd.show();
        }
    }

    @Override
    public void onVideoDownloadFailed() {
        // 视频缓存失败，如果想走本地播放，可以在这儿重新load下一条广告，最好限制load次数（4-5次即可）。
        Log.i(TAG, "onVideoDownloadFailed");

    }

    @Override
    public void playCompletion() {
        // 播放完成回调，媒体可以在这儿给用户奖励
        Log.i(TAG, "playCompletion");
        if (adVideoBaseListener != null) {
            adVideoBaseListener.adVideoDidFinish();
        }
    }

    @Override
    public void onAdShow() {
        // 视频开始播放时候的回调
        Log.i(TAG, "onAdShow");
        if (adVideoBaseListener != null) {
            adVideoBaseListener.adVideoDidStart();
        }
    }

    @Override
    public void onAdClick() {
        // 广告被点击的回调
        Log.i(TAG, "onAdClick");
    }

    @Override
    public void onAdClose(float playScale) {
        // 用户关闭了广告
        // 说明：关闭按钮在mssp上可以动态配置，媒体通过mssp配置，可以选择广告一开始就展示关闭按钮，还是播放结束展示关闭按钮
        // 建议：收到该回调之后，可以重新load下一条广告,最好限制load次数（4-5次即可）
        // playScale[0.0-1.0],1.0表示播放完成，媒体可以按照自己的设计给予奖励
        Log.i(TAG, "onAdClose" + playScale);
    }

    @Override
    public void onAdFailed(String arg0) {
        // 广告失败回调 原因：广告内容填充为空；网络原因请求广告超时
        // 建议：收到该回调之后，可以重新load下一条广告，最好限制load次数（4-5次即可）
        Log.i(TAG, "onAdFailed");
        if (adVideoBaseListener != null) {
            adVideoBaseListener.adVideoDidFail();
        }
    }


}
