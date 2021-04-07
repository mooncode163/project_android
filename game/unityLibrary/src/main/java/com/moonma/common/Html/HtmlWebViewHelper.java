package com.moonma.common;

import android.content.Context;
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

import android.graphics.Picture;
import android.os.Build;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.unity3d.player.UnityPlayer;

public class HtmlWebViewHelper extends Object {
    private final String TAG = this.getClass().toString();
    Context mContext;
    private WebView webView;
    int webProgress;

    FrameLayout layWeb;

    public HtmlWebViewHelper(Context context) {
        mContext = context;
        webView = new WebView(mContext);

        // FrameLayout
//        ViewGroup.LayoutParams framelayout_params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT);
//
//        layWeb = new FrameLayout(mContext);
//        layWeb.setLayoutParams(framelayout_params);
//
//        layWeb.addView(webView);
//
//        MyApplication.main().mainLayout.addView(layWeb);
    }

    public void setOffsetYInternal() {
        int screen_w = MyApplication.main().mainLayout.getWidth();
        int screen_h = MyApplication.main().mainLayout.getHeight();
        int y = screen_h ;

        // y = 128;
        // framelayoutAd.setX(x);
        layWeb.setY(y);

    }


    public void Load(String url) {
        Log.i(TAG, "Load url="+url);
        //        setContentView(webView);

        WebSettings webSettings = webView.getSettings();
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //https://appgallery1.huawei.com/#/app/C100270155
//                String url = "https://appgallery1.huawei.com/#/app/C100270155";
        //        url ="https://blog.csdn.net/qq_36332133/article/details/88071456";
        webView.loadUrl(url);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");


        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                webProgress = newProgress;
                Log.d(TAG, " newProgress=" + newProgress);
                if (newProgress >= 100) {
                    // GetAppVersion();
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {

                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished ="+url);
                GetAppVersion();
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.i(TAG, "onReceivedError");
                UnityPlayer.UnitySendMessage("Scene", "OnWebViewDidFail", " ");
            }
        });
        webView.setPictureListener(new WebView.PictureListener() {
            @Override
            public void onNewPicture(WebView view, Picture picture) {
                // Do Something

            //   if (webProgress >= 100)
                {
                    GetAppVersionInternal();

                }
            }
        });
    }

    public void SetObjectInfo(String name, String methodFinish, String methodFail) {


    }

    public void GetAppVersion() {

        Handler handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override

            public void run() {
                GetAppVersionInternal();
            }

        };
        handler.postDelayed(runnable, 2000);

    }

    void GetAppVersionInternal() {

//               webView.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
//                String js = "window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');";
        String js = "window.HTMLOUT.processHTML(document.getElementsByTagName('html')[0].innerHTML);";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("javascript:" + js, null);
            Log.i(TAG, "evaluateJavascript-javascript");
        } else {
            webView.loadUrl("javascript:" + js);
            Log.i(TAG, "loadUrl-javascript");
        }
    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        @SuppressWarnings("unused")
        public void processHTML(String html) {
            // 注意啦，此处就是执行了js以后 的网页源码


            Log.d(TAG, " html=" + html);

            int idx = html.indexOf("Version");
            if (idx >= 0) {
                Log.d(TAG, " Version=");

            }

            UnityPlayer.UnitySendMessage("Scene", "OnWebViewDidFinish", html);

        }
    }

}
