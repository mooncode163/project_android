package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import com.moonma.common.IAdBannerBaseListener;

/**
 * Created by jaykie on 16/5/27.
 */
public interface ITongjiBase {

    public void Init(final Context context, String appKey, String channel);
    public  void onPause(Context context ) ;
    public void onResume(Context context) ;
}
