package com.moonma.common;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

//import com.guo.android_extend.image.ImageConverter;

public class MediaPlayer   {
    private IPlayer player = null;
 

    public   void Open(String url)
    { 
        if(player==null) {
            player = new SdkPlayer();
        }

 
        // player.setContext(mContext);
        // player.setActivity(mActivity);

		// player.setPlayerListener(listener);
		player.Open(url);
	}
    public   void Close()
    {
        if(player==null) {
            player.release();
        }
    }

    public   void Play()
    {
        if(player==null) {
            player.resume();
        }
    }
    public   void Pause()
    {
        if(player==null) {
            player.pause();
        }
    }
}
