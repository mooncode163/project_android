
package com.moonma.common;

public interface IPlayListener {
	
	public void onPrepared();
	public void onStarted();
	public void onPaused();
	public void onResumed();
	public void onSeeked(int seekedDuration);
	public void onVideoSizeChanged();
	public void onStopped();
	public void onFinished();
	public void onError(int errorCode);
	
	public void onMediaRealPaused();
	public void onMediaRealBuffingFinish();
}
