
package com.moonma.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.SurfaceView;
 

public interface IPlayer {
	
	void setPlayerListener(IPlayListener listener);
	
	void initPlayerListeners();
	void Open(String url);
	//void prepareDisplay(SurfaceHolder surfaceHolder, int width, int height) throws IllegalStateException;
    void setContext(Context ctx);
    void setActivity(Activity ac);
    void setIsUseHWDecode(boolean isHW);
    void reset(); 
	void prepareAsync();
	void start();
	void startTo(int seekDuration);
	void pause();
	void resume();
	void seekTo(int seekDuration);
	void stop();
	long getDuration();
	long getPlayingDuration();  //秒为单位
	long getCurDuration();   //硬解(毫秒为单位);软解(秒为单位)
	boolean isPlaying();
	void release();

	int getVideoWidth();
	int getVideoHeight();
    int getVideoDispWidth();
    int getVideoDispHeight();
//	void setDisplay(SurfaceHolder surfaceHolder);
	void setDisplay(SurfaceView surfaceview);
	
	void Enable3d(boolean s3d);
	boolean S3DSupported();
	void SetS3dMode(boolean s3d);
	
	Bitmap CaptureCurImage();

	Bitmap snap(String filePath, int ms) throws UnsupportedOperationException;
	

}
