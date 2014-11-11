package com.himumsaiddad.niggle;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.himumsaiddad.niggle.intro.SplashScreen;

public abstract class VideoPlayer  implements
OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
OnVideoSizeChangedListener, SurfaceHolder.Callback{
	
	private String TAG = "wisdom play video";
	private MediaPlayer mMediaPlayer;
	private SurfaceView mPreview;
	private SurfaceHolder holder;
	private boolean mIsVideoSizeKnown = false;
	private boolean mIsVideoReadyToBePlayed = false;
	private int mVideoWidth;
	private int mVideoHeight;
	private boolean loop_video;
	private boolean has_stoped = false;
	private int rc_video;
	private Activity mActivity;

		
    /** Called when the activity is first created. */
    
    
   public VideoPlayer(Activity a, boolean loop_video,
    		SurfaceView view, int rc_video)
    {
  		mPreview = view;   
		holder = mPreview.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		this.loop_video = loop_video;
		this.rc_video = rc_video;
		this.mActivity = a;
    }
	
	public void playVideo() {
		doCleanUp();
		try
		{
			Uri video_file = Uri.parse("android.resource://" + mActivity.getPackageName() + "/" 
	        		+ rc_video); 
			
			// Create a new media player and set the listeners
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(mActivity.getApplicationContext(),video_file);
			mMediaPlayer.setDisplay(holder);
			mMediaPlayer.setLooping(loop_video);
			mMediaPlayer.prepare();
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			//mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			startVideoPlayback();
		} catch (Exception e) {
			Log.e(TAG, "error: " + e.getMessage(), e);
		}
	}

	public void onBufferingUpdate(MediaPlayer arg0, int percent) {
		Log.d(TAG, "onBufferingUpdate percent:" + percent);

	}

	public void onCompletion(MediaPlayer arg0) {
		Log.d(TAG, "onCompletion called");
		
		
		OnComplete();
		
		has_stoped = true;
	}

	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		Log.v(TAG, "onVideoSizeChanged called");
		if (width == 0 || height == 0) {
			Log.e(TAG, "invalid video width(" + width + ") or height(" + height
					+ ")");
			return;
		}
		mIsVideoSizeKnown = true;
		mVideoWidth = width;
		mVideoHeight = height;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void onPrepared(MediaPlayer mediaplayer) {
		Log.d(TAG, "onPrepared called");
		mIsVideoReadyToBePlayed = true;
		if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
			startVideoPlayback();
		}
	}

	public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
		Log.d(TAG, "surfaceChanged called");

	}

	public void surfaceDestroyed(SurfaceHolder surfaceholder) {
		Log.d(TAG, "surfaceDestroyed called");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated called");
			playVideo();

	}
/*
	@Override
	protected void onResume() {
		super.onResume();
			playVideo();
	}
	*/
	/*protected void onRestart() {
		super.onRestart();
		playVideo();
	}*/
	/*
	@Override
	protected void onStop() {
		super.onStop();
		releaseMediaPlayer();
		doCleanUp();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		releaseMediaPlayer();
		doCleanUp();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		releaseMediaPlayer();
		doCleanUp();
	}
*/
	public void releaseMediaPlayer() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void doCleanUp() {
		mVideoWidth = 0;
		mVideoHeight = 0;
		mIsVideoReadyToBePlayed = false;
		mIsVideoSizeKnown = false;
	}

	public void startVideoPlayback() {
		Log.v(TAG, "startVideoPlayback");
		holder.setFixedSize( mVideoWidth, mVideoHeight);
		if(has_stoped == false)
			mMediaPlayer.start();
	}
	
	public void stopVideoPlayback() {
		Log.v(TAG, "stopVideoPlayback");
	
		if(mMediaPlayer.isPlaying())
		{
			mMediaPlayer.stop();
			has_stoped = true;
		}
	}

	public void pauseVideoPlayback() {
		Log.v(TAG, "stopVideoPlayback");
	
		if(mMediaPlayer.isPlaying())
			mMediaPlayer.pause();
	}

	public boolean isPlaying() {
		Log.v(TAG, "isPlaying");
	
		return mMediaPlayer.isPlaying();
			
	}
	
	public boolean surfaceIsInitiated(){
		return mMediaPlayer != null; 
	}

	public abstract void OnComplete();
	
}
