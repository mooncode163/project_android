
package com.moonma.common;

import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.media.ThumbnailUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import java.io.IOException;

public class SdkPlayer implements IPlayer {

	private MediaPlayer mediaPlayer;
	private IPlayListener listener; 
	private boolean isPlaying; // 只要打开了视频(包括暂停),知道关闭视频,就算正在播放
	private int pauseTime;

    @Override
    public void setIsUseHWDecode(boolean isHW)
    {

    }

    @Override
    public void setActivity(Activity ac)
    {

    }

    @Override
    public void setContext(Context ctx)
    {
        // TODO Auto-generated method stub

    }
	@Override
	public void setPlayerListener(IPlayListener listener) {
		this.listener = listener;
	}

	@Override
	public void initPlayerListeners() {
		if (mediaPlayer == null)
			mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {

				isPlaying = true; // 缓冲完成,开始播放

				listener.onPrepared();
			}

		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				isPlaying = false;
				listener.onFinished();
			}

		});
		mediaPlayer.setOnSeekCompleteListener(new OnSeekCompleteListener() {

			@Override
			public void onSeekComplete(MediaPlayer arg0) {
				listener.onSeeked(mediaPlayer.getCurrentPosition() / 1000);
			}

		});
		mediaPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				isPlaying = false;
				listener.onError(what);
				return true;
			}

		});

		mediaPlayer.setOnInfoListener(new OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				// TODO Auto-generated method stub
				int y = 0;
				y++;
				return false;
			}
		});

		mediaPlayer.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
				// TODO Auto-generated method stub
				//return false;
				int y = 0;
				y++;
			}
		});
		
		mediaPlayer.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

			@Override
			public void onBufferingUpdate(MediaPlayer mp, int percent) {
				// TODO Auto-generated method stub
				//return false;
				int y = 0;
				y++;
			}
		});
	}

	@Override
	public void start() {
		if (null != mediaPlayer) {
			mediaPlayer.start();
		}

		isPlaying = true;
		listener.onStarted();
	}

	@Override
	public void startTo(int seekDuration) {

		try {
			if (null != mediaPlayer) {
				mediaPlayer.seekTo(seekDuration * 1000);
				mediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.onError(0);
			return;
		}

		isPlaying = true;
		listener.onStarted();

	}

	@Override
	public void pause() {
		if (null != mediaPlayer) {
			mediaPlayer.pause();
			// pauseTime = mediaPlayer.getCurrentPosition();
		}

		listener.onPaused();
	}

	@Override
	public void resume() {

		if (null != mediaPlayer) {
			if (pauseTime > 0) {
				// mediaPlayer.seekTo(pauseTime);
				pauseTime = 0;
			}
			mediaPlayer.start(); // 可以从暂停的时候直接播放,而不是从头播放
		}

		listener.onResumed();
	}

	@Override
	public void seekTo(int seekDuration) {
		int p = seekDuration * 1000;
		if (pauseTime > 0)
			pauseTime = p;
		if (null != mediaPlayer) {
			mediaPlayer.seekTo(p);
		}
	}

	@Override
	public void stop() {
		if (null != mediaPlayer) {
			mediaPlayer.stop();
		}

		isPlaying = false;
		listener.onStopped();
	}

	@Override
	public long getDuration() {
		long nDuration = 0;
		if (null != mediaPlayer) {
			nDuration = mediaPlayer.getDuration() / 1000;
		}
		return nDuration;
	}

	// 毫米为单位
	@Override
	public long getCurDuration() {
		long ncurtime = 0;
		if (null != mediaPlayer) {
			ncurtime = mediaPlayer.getCurrentPosition();
		}

		return ncurtime;
	}

	@Override
	public long getPlayingDuration() {
		if (pauseTime > 0)
			return pauseTime / 1000;

		if (mediaPlayer == null)
			return 0;

		return mediaPlayer.getCurrentPosition() / 1000;
	}

	@Override
	public boolean isPlaying() {
		return isPlaying;
	}

	@Override
	public void release() {
		isPlaying = false;
		if (mediaPlayer == null)
			return;

		mediaPlayer.setAudioStreamType(AudioManager.STREAM_SYSTEM);
		mediaPlayer.stop();
		mediaPlayer.release();
		mediaPlayer = null;
	}

	@Override
	public int getVideoWidth() {
		int nVideoW = 0;
		if (null != mediaPlayer) {
			nVideoW = mediaPlayer.getVideoWidth();
		}
		return nVideoW;
	}

	@Override
	public int getVideoHeight() {
		int nVideoH = 0;
		if (null != mediaPlayer) {
			nVideoH = mediaPlayer.getVideoHeight();
		}
		return nVideoH;
	}

    public int getVideoDispWidth()
    {
        int ret =getVideoWidth();

        return ret;
    }

    public int getVideoDispHeight()
    {
        int ret =getVideoHeight();

        return ret;
	}
	/**
     * 打开raw目录下的音乐mp3文件 MediaPlayer.create(this, R.raw.raw_mp3);
     */
    public void OpenRaw(int rid) {
		 
        // mediaPlayer = MediaPlayer.create(this,rid);
        // //用prepare方法，会报错误java.lang.IllegalStateExceptio
        // //mediaPlayer1.prepare();
		// mediaPlayer1.start();
		prepareAsync();
		start();
    }

    /**
     * 打开assets下的音乐mp3文件
     */
	public void OpenAsset(String filepath) {
		//打开Asset目录 
		Activity ac = Common.getMainActivity();
        AssetManager assetManager = ac.getAssets();
		if (mediaPlayer == null) {
			mediaPlayer = new MediaPlayer();
		}
        try {
            //打开音乐文件shot.mp3
            AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filepath);
            mediaPlayer.reset();
            //设置媒体播放器的数据资源
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
    
        } catch (IOException e) {
            e.printStackTrace();
            // Log.d("GsonUtils", "IOException" + e.toString());
		}
		
		prepareAsync();
		start();
	}
	
	@Override
	public void Open(String url)
	{
		try {
			if (mediaPlayer == null) {
				mediaPlayer = new MediaPlayer();
			}
			mediaPlayer.setDataSource(url);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			//listener.onError(0);
		}

		prepareAsync();
		start();
	}

	// @Override
	// public void setDataSource(IVideo video) {
	// 	this.video = video;

	// 	try {
	// 		if (mediaPlayer == null) {
	// 			mediaPlayer = new MediaPlayer();
	// 		}
	// 		mediaPlayer.setDataSource(this.video.getPath());
	// 	} catch (Exception e) {
	// 		// TODO: handle exception
	// 		e.printStackTrace();
	// 		listener.onError(0);
	// 	}
	// }

	@Override
	public void setDisplay(SurfaceView surfaceView) {

		if (mediaPlayer == null)
			throw new IllegalStateException("Pls prepare engine first!");

		try {
			if (null != surfaceView) {
				surfaceView.getHolder().setType(
						SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				SurfaceHolder h = surfaceView.getHolder();
				mediaPlayer.setDisplay(h);
				mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			} else {
				mediaPlayer.setDisplay(null);
			}
		} catch (Exception e) {
			// TODO: handle exception
			listener.onError(0);
		}
	}

	@Override
	public void reset() {
		if (null != mediaPlayer) {
			mediaPlayer.reset();
		}
	}

	@Override
	public void prepareAsync() {
		try {
			if (null != mediaPlayer) {
				mediaPlayer.prepareAsync();
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.onError(0);
		}
	}

	@Override
	public Bitmap snap(String filePath, int ms)
			throws UnsupportedOperationException {
		return ThumbnailUtils.createVideoThumbnail(filePath, 0);
	}

	public void Enable3d(boolean s3d) {

	}

	public boolean S3DSupported() {
		return false;
	}

	public void SetS3dMode(boolean s3d) {

	}

	public Bitmap CaptureCurImage() {
		if (null == mediaPlayer) {
			return null;
		}

		long ntime = mediaPlayer.getCurrentPosition();

		Bitmap bitmap = null;

		// 使用反射机制,2.2也可以使用
		String className = "android.media.MediaMetadataRetriever";
		Object objectMediaMetadataRetriever = null;
		Method release = null;
		try {
			objectMediaMetadataRetriever = Class.forName(className)
					.newInstance();

			// 获取方法:setDataSource(String);
			Method setDataSourceMethod = Class.forName(className).getMethod(
					"setDataSource", String.class);
			String strPath = "";//video.getPath();
			setDataSourceMethod.invoke(objectMediaMetadataRetriever, strPath);

			// 获取方法getFrameAtTime(long,int);
			Method getFrameAtTimeMethod = Class.forName(className).getMethod(
					"getFrameAtTime", long.class, int.class);
			bitmap = (Bitmap) getFrameAtTimeMethod.invoke(
					objectMediaMetadataRetriever, ntime * 1000, 0x03);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != objectMediaMetadataRetriever) {
					// 获取方法release();
					release = Class.forName(className).getMethod("release");
				}

				if (release != null) {
					release.invoke(objectMediaMetadataRetriever);
					objectMediaMetadataRetriever = null;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// funClass.saveBitmap("/mnt/sdcard/", "testtest", bitmap);
		}

		// 2.3api下可用
		// MediaMetadataRetriever retriever = new MediaMetadataRetriever();
		// try {
		// String strPath = video.getPath();
		// retriever.setDataSource(strPath);// 资源路径
		//
		// //MediaMetadataRetriever.OPTION_CLOSEST,比较接近当前帧
		// bitmap =
		// retriever.getFrameAtTime(ntime*1000,MediaMetadataRetriever.OPTION_CLOSEST);//
		// 按当前播放位置选择帧
		// } catch (Exception ex) {
		//
		// } finally {
		// try {
		// retriever.release();
		// } catch (Exception ex) {
		//
		// }
		//
		// funClass.saveBitmap("/mnt/sdcard/", "testtest", bitmap);
		// }

		return bitmap;
	}
}
