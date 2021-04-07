//package com.czt.util;
package com.moonma.common;
import android.content.Context;

import com.moonma.common.ITongjiBase;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class TongjiUmeng implements ITongjiBase {
	 

         public void Init(final Context context, String appKey, String channel)
         {
                //        友盟sdk 小米android10 上第一次安装运行crash 先关闭
                UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
         }

    public  void onPause(Context context ) {
        //@moon
        MobclickAgent.onPause(context);
    }

    public void onResume(Context context) {
        //@moon
      MobclickAgent.onResume(context);
    }
}
