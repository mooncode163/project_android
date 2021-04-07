package com.moonma.common;

import com.moonma.common.AdConfigBase;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.bun.supplier.IIdentifierListener;
import com.bun.miitmdid.core.JLibrary;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.supplier.IdSupplier;


import com.baidu.mobads.AdSettings;
import com.baidu.mobads.BaiduManager;
import com.moonma.common.DemoSPUtils;


import com.baidu.mobads.MobadsPermissionSettings;

/**
 * Created by jaykie on 16/5/24.
 */
public class AdConfigBaidu extends AdConfigBase {

    // SP的key, 读取设备信息权限
    private static final String KEY_PHONE_STATE = "key_phone_state";
    // SP的key, 读取定位权限
    private static final String KEY_LOCATION = "key_location";
    // SP的key, 读写外部存储权限（SD卡）
    private static final String KEY_STORAGE = "key_storage";
    // SP的key, 读取应用列表权限
    private static final String KEY_APP_LIST = "key_app_list";

    private static AdConfigBaidu _mian = null;

    public static AdConfigBaidu main() {
        if (_mian == null) {
            _mian = new AdConfigBaidu();
        }
        return _mian;
    }

    public void onInit(Context context, String appId, String appKey) {

    }

    public void OnAppInit(Context context, String appId, String appKey) {
        //集成信通院MSA http://msa-alliance.cn/
//        return;

        try {
            // 初始化MSA SDK
            JLibrary.InitEntry(context);
            //获取OAID
            int sdkState = MdidSdkHelper.InitSdk(context, true, new IIdentifierListener() {
                @Override
                public void OnSupport(boolean b, IdSupplier idSupplier) {
                    if (idSupplier != null) {
                        String oaid = idSupplier.getOAID();
                        Log.e("oaid", "oaid=" + oaid);
                    }
                }
            });
            Log.e("mdidsdk", "初始化" + sdkState);
        } catch (Throwable tr) {
            tr.printStackTrace();
        }
// 重要：适配安卓P，如果WebView使用多进程，添加如下代码
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(context);
            // 填入应用自己的包名
            if (!"com.baidu.mobads.demo.main".equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }


        //

        // 广告展现前先调用sdk初始化方法，可以有效缩短广告第一次展现所需时间
        BaiduManager.init(Common.getMainActivity());

        // 设置SDK可以使用的权限，包含：设备信息、定位、存储、APP LIST
        // 注意：建议授权SDK读取设备信息，SDK会在应用获得系统权限后自行获取IMEI等设备信息
        //      授权SDK获取设备信息会有助于提升ECPM
        initMobadsPermissions(Common.getMainActivity());


        // SDK默认使用https，如需改为http请求，调用如下方法
//        AdSettings.setSupportHttps(false);

    }

    private String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }

    /**
     * 初始化设置广告SDK的权限, 从SharedPreference中读取存储的权限状态
     */
    public void initMobadsPermissions(Context context) {
        MobadsPermissionSettings
                .setPermissionReadDeviceID(DemoSPUtils.getBoolean(context, KEY_PHONE_STATE, true));
        MobadsPermissionSettings
                .setPermissionLocation(DemoSPUtils.getBoolean(context, KEY_LOCATION, true));
        MobadsPermissionSettings
                .setPermissionStorage(DemoSPUtils.getBoolean(context, KEY_STORAGE, true));
        MobadsPermissionSettings
                .setPermissionAppList(DemoSPUtils.getBoolean(context, KEY_APP_LIST, true));
    }

    private void updatePermissions(Context context, String permission, boolean granted) {
        if (KEY_PHONE_STATE.equalsIgnoreCase(permission)) {
            MobadsPermissionSettings.setPermissionReadDeviceID(granted);
        } else if (KEY_LOCATION.equalsIgnoreCase(permission)) {
            MobadsPermissionSettings.setPermissionLocation(granted);
        } else if (KEY_STORAGE.equalsIgnoreCase(permission)) {
            MobadsPermissionSettings.setPermissionStorage(granted);
        } else if (KEY_APP_LIST.equalsIgnoreCase(permission)) {
            MobadsPermissionSettings.setPermissionAppList(granted);
        }
        DemoSPUtils.setBoolean(context, permission, granted);
    }

}
