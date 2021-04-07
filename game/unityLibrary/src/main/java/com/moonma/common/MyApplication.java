package com.moonma.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.moonma.common.AdConfig;
import com.moonma.common.AdConfigXiaomi;
import com.moonma.common.AdSplashXiaomi;
import com.moonma.common.AdConfigBaidu;
// import com.moonma.common.IAPXiaomi;
import com.moonma.common.Source;
//import com.moonma.common.AdConfigMobVista;
import com.moonma.common.Tongji;

import com.moonma.common.FileUtil;

import org.json.JSONObject;

public class MyApplication extends Application
{
	private static String TAG = "MyApplication";
	private static Context context;
	private Activity mainActivity;
	public FrameLayout mainLayout;
	static private MyApplication _main;

	public static MyApplication main() {
		return _main;
	}

	public static Context getAppContext() {
		return MyApplication.context;
	}

	public Activity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(Activity ac) {
		mainActivity = ac;
	}

	public boolean isBlankString(String str) {

		if (str == null) {
			//为null;
			return true;
		}
		if (str.length() == 0) {
			//为空字符串;
			return true;
		}
		if (str.equals("")) {
			//为空字符串;
			return true;
		}

		return false;
	}


	public String getUmengAppKey() {
		FileUtil fileUtil = new FileUtil();
		fileUtil.init(context);
		String appkey = "";
		String jsonFile = "ConfigData/config/config_android.json";
		String json = fileUtil.ReadStringAsset(jsonFile);
		try {
			JSONObject object = new JSONObject(json);
			appkey = object.getString("APPTONGJI_ID");
			Log.v(TAG, "json appkey tongji:" + appkey);
		} catch (Exception e) {
			e.printStackTrace();
			Log.v(TAG, "json appkey tongji error");
		}

		if (isBlankString(appkey)) {
			try {
				ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
				Bundle bundle = ai.metaData;
				appkey = bundle.getString("UMENG_APPKEY");
				Log.v(TAG, "xml appkey tongji:" + appkey);

			} catch (Exception e) {
				Log.v(TAG, "xml appkey tongji error");
				e.printStackTrace();
			}
		}

		Log.v(TAG, "last appkey tongji = " + appkey);

		return appkey;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		MyApplication.context = getApplicationContext();
		_main = this;


		String strPackage = getPackageName();
		String strTmp = strPackage.substring(4);//"com."
		String strTmp1 = strTmp.substring(strTmp.indexOf('.') + 1);
//        String channelName = ChannelUtil.getChannel(this);
//        if (isBlankString(channelName)) {
//            //默认设置为小米
//            channelName = "xiaomi";
//        }
//
//
//        String strChannel = strTmp1 + "_" + channelName;
		String strChannel = strTmp1;
		Tongji.Main().CreateTongji(context,Source.umeng,getUmengAppKey(),strChannel);

		//小米广告sdk初始化

		AdConfig adConfig = AdConfig.Main(this);
		{
			String appid = adConfig.GetAppId(Source.XIAOMI);
			// appid = "2882303761517411490"; //demo
			AdConfigXiaomi.main().initSdk(this,appid);

		}
		//xiaomi IAP
		//IAPXiaomi.initSDK(this);

		//mobvista sdk init
		// {
		// 	String  appid = adConfig.GetAppId(Source.MobVista);
		// 	String appkey = adConfig.GetAppId(Source.MobVista);
		//    AdConfigMobVista.main().init(this,appid,appkey);
		// }


		AdConfigBaidu.main().OnAppInit(this,"","");
	}

}
