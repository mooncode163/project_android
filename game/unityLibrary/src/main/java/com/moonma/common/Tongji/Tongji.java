//package com.czt.util;
package com.moonma.common;
import com.moonma.common.ITongjiBase;
import com.moonma.common.TongjiUmeng;

import android.content.Context;
public class Tongji {
	
	ITongjiBase tongjiBase;
	private static String mChannel;
	
	private static Tongji _mian = null;  
    public static Tongji Main() {
        if(_mian==null){
            _mian = new Tongji(); 
            // _mian.Init(ctx);
        }
        return _mian;
    }

    public void CreateTongji(Context context,String source,String appKey,String channel) {
        if (source.equals(Source.umeng)) {
            tongjiBase = new TongjiUmeng();
        } else if (source.equals(Source.XIAOMI)) {
            
        }
        if (tongjiBase != null) {
            tongjiBase.Init(  context, appKey,channel);
        }
    }

    public  void onPause(Context context ) {
        if (tongjiBase != null) {
            tongjiBase.onPause(context);
        }
    }

    public void onResume(Context context) {
        if (tongjiBase != null) {
            tongjiBase.onResume(context);
        }
    }


}
