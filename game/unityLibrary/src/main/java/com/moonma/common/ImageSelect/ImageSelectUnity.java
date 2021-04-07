package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


import com.donkingliang.imageselector.utils.ImageSelector;
import com.donkingliang.imageselector.utils.UriUtils;
import com.moonma.common.CommonUtils;
import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;



public class ImageSelectUnity {
    private  static  String TAG = "ImageSelectUnity";
    private static final int REQUEST_CODE = 0x00000011;
    private static final int PERMISSION_WRITE_EXTERNAL_REQUEST_CODE = 0x00000012;
    public  String unityObjName;
    public  String unityObjMethod;
//    private ArrayList<String> mImages;

    static private ImageSelectUnity _main;

    public static ImageSelectUnity Main() {
        if(_main==null)
        {
            _main = new ImageSelectUnity();
        }
        return _main;
    }

//unity 调用接口
    public static void UnityOpenImage( ) {
        Activity ac = Common.getMainActivity();
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageSelectUnity.Main().LoadOne();
            }
        });

    }

    public static void UnityOpenCamera( ) {
        Activity ac = Common.getMainActivity();
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageSelectUnity.Main().LoadCamera();
            }
        });

    }

    public static void UnitySetObjectInfo(String objName, String objMethod)
    {
        ImageSelectUnity.Main().SetObjectInfo(objName,objMethod);
    }

    //unity 调用接口 end

    public  boolean SaveImage(Bitmap bitmap, String filePath) {
        try {
            File mFile = new File(filePath);                        //将要保存的图片文件
            if (mFile.exists()) {
               // Toast.makeText(context, "该图片已存在!", Toast.LENGTH_SHORT).show();
//                return false;
            }

            FileOutputStream outputStream = new FileOutputStream(mFile);     //构建输出流
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);  //compress到输出outputStream
            Uri uri = Uri.fromFile(mFile);                                  //获得图片的uri
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)); //发送广播通知更新图库，这样系统图库可以找到这张图片
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public  void SetObjectInfo(String objName, String objMethod)
    {
        unityObjName = objName;
        unityObjMethod = objMethod;
    }
    public void OnImageFinish(final  String filePath) {
        Activity ac = Common.getMainActivity();
        final String name = this.unityObjName;
        final String method = this.unityObjMethod;
        ac.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                UnityPlayer.UnitySendMessage("Scene", "AdInsertWillShow", filePath);
                UnityPlayer.UnitySendMessage(name, method, filePath);
            }
        });
    }
    //unity 调用接口


    public void LoadOne() {
            Activity ac = Common.getMainActivity();
                //单选
                ImageSelector.builder()
                        .useCamera(true) // 设置是否使用拍照
                        .setSingle(true)  //设置是否单选
                        .canPreview(true) //是否点击放大图片查看,，默认为true
                        .start(ac, REQUEST_CODE); // 打开相册

    }

    public void LoadCamera() {
        Activity ac = Common.getMainActivity();
        //仅拍照
        ImageSelector.builder()
                .onlyTakePhoto(true)  // 仅拍照，不打开相册
                .start(ac, REQUEST_CODE);

    }

    protected void LoadImage( ArrayList<String> images ) {
        final String image = images.get(0);
        Activity ac = Common.getMainActivity();
        Glide.with(ac).asBitmap()
                .load(UriUtils.getImageContentUri(ac, image))
                .listener(new RequestListener<Bitmap>() {
                              @Override
                              public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
                                  return false;
                              }

                              @Override
                              public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
                                  Log.d(TAG,   " cover successful!");

                                  String filePath = CommonUtils.getCachePath()+"/imageselect_temp.jpg";
                                  ImageSelectUnity.Main().SaveImage(bitmap,filePath);
                                  ImageSelectUnity.Main().OnImageFinish(filePath);
                                   // this.OnImageFinish(filePath);
                                  return false;
                              }
                          }
                ).submit();

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            ArrayList<String> images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT);
            boolean isCameraImage = data.getBooleanExtra(ImageSelector.IS_CAMERA_IMAGE, false);
//            Log.d("ImageSelector", "是否是拍照图片：" + isCameraImage);
            this.LoadImage(images);
        }
    }


}
