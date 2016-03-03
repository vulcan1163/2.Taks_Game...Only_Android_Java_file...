package com.example.taksintroduction;

import java.util.Random;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class GameActivity extends Activity{

	final int REQ_CODE_SELECT_IMAGE = 100;
	Bitmap image_bitmap;
	public MediaPlayer mediaPlayer;
	int backnumber;
	int backButton;
	SoundPool sound;
	int gameover_y = -500;
	boolean intentCheck = false;
	
	

	private SensorEventListener accL; // 가속도
	private Sensor accSensor; // 가속도
	public float ACCX, ACCY;
	private SensorManager sm;
	
	
	
	int sound_id;
	int i =0;
	
	GameThread mThread;
	FrameLayout Flayout;
	Animation frameAnimation_1, frameAnimation_2;
	ImageView background;
	Typeface mFont;
	Context mContext;
	ImageView imgBack;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		setContentView( R.layout.game);
		
		
		AppManager.getInstance().setGameActivity(this);
		AppManager.getInstance().setResources(getResources());
		
		Intent intent = getIntent();

		backnumber = intent.getIntExtra("back", 1);

		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		accL = new Listener(); // 가속도 센서 리스너 인스턴스
		
	//	background = (ImageView)findViewById(R.id.background);
		//background.setBackgroundResource(R.drawable.pic);
		
		Flayout = (FrameLayout) findViewById(R.id.fram_resulta);

		frameAnimation_1 = AnimationUtils.loadAnimation(this,
				R.anim.background_alpha_1);
		frameAnimation_2 = AnimationUtils.loadAnimation(this,
				R.anim.background_alpha_2);

		switch (backnumber) {
		case 1:

			//Flayout.setBackgroundResource(R.drawable.spring1);
			GameThread.backGround = 1;
			// setBack.setImageResource(R.drawable.spring1);
			break;
		case 2:

			//Flayout.setBackgroundResource(R.drawable.summer_1);
			// setBack.setImageResource(R.drawable.summer1);
			GameThread.backGround = 2;
			break;

		case 3:

			//Flayout.setBackgroundResource(R.drawable.fall);
			// setBack.setImageResource(R.drawable.fall);
			GameThread.backGround = 3;
			break;

		case 4:

			//Flayout.setBackgroundResource(R.drawable.winter);
			// setBack.setImageResource(R.drawable.winter);
			GameThread.backGround = 4;
			break;
			
		
	}

		Random rnd = new Random();
		int kind = rnd.nextInt(5);
		
		switch(kind){
		case 0:
			mediaPlayer = MediaPlayer.create(this, R.raw.gameplaymusic1);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
			break;
		case 1:
			mediaPlayer = MediaPlayer.create(this, R.raw.gameplaymusic2);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
			break;
		case 2:
			mediaPlayer = MediaPlayer.create(this, R.raw.gameplaymusic3);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
			break;
		case 3:
			mediaPlayer = MediaPlayer.create(this, R.raw.gameplaymusic4);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
			break;
		case 4:
			mediaPlayer = MediaPlayer.create(this, R.raw.gameplaymusic5);
			mediaPlayer.setLooping(true);
			mediaPlayer.start();
			break;
		}
		
		mFont = Typeface.createFromAsset(getAssets(), "RixToyGray.ttf");
		
}
	/////////////////////////////////////////
	//	invisible 체크
	///////////////////////////////////////
	
	public void invisibleCheck(){
		
		if(intentCheck){
			imgBack.setVisibility(View.VISIBLE);
		}
		
	}
	

	@Override
	protected void onDestroy() {
		
		
		if (sm != null) {
			sm.unregisterListener(accL);
		}
		super.onDestroy();
	}
	public void killMediaPlayer() {
		
		if (mediaPlayer != null) {
			try {
				mediaPlayer.release();
				mediaPlayer = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/////////////////////////////////////////
	//				일시 중단 
	/////////////////////////////////////////
	protected void onPause() {
	
		if (sm != null) {
			sm.unregisterListener(accL);
		}
		
		super.onPause();
	}
	
	///////////////////////////////////////
	//		back 버튼 클릭
	/////////////////////////////////////
	public void back(){
		
		boolean flag = true;
		Intent in = new Intent(getApplication(), ResultActivity.class );
		in.putExtra("score", AppManager.getInstance().getGameThread().score);
		
		//AppManager.getInstance().getGameThread().gameovercheck = false;
		
		AppManager.getInstance().getGameThread().ThreadStop();

		in.putExtra("back", backnumber);
		
		/*
		while(flag){
		try{
			AppManager.getInstance().getGameThread().join();
			
			if(!AppManager.getInstance().getGameThread().isAlive()){
				flag = false;
			}
			
		}catch(InterruptedException e){
			
		}
		}
		*/
		
		
		if(intentCheck){
			
			startActivity(in);
		
		intentCheck = false;
	
		}
	//	AppManager.getInstance().mGameThread = null;

		finish();
		
	}

	///////////////////////////////////////////////
	//				Clear Method
	//////////////////////////////////////////////
	public void clear(){

		
		AppManager.getInstance().getGameThread().mActionBack.clear();
		AppManager.getInstance().getGameThread().mBlock.clear();
		AppManager.getInstance().getGameThread().sBall.clear();
		
		
		if(mThread.backGround == 1){

			AppManager.getInstance().getGameThread().mSpring1.clear();
			AppManager.getInstance().getGameThread().mSpring2.clear();
		}
		
		if(mThread.backGround == 2){

			AppManager.getInstance().getGameThread().mSummer1.clear();
			AppManager.getInstance().getGameThread().mSummer2.clear();
		}
		
		if(mThread.backGround == 3){

			AppManager.getInstance().getGameThread().mFall1.clear();
			AppManager.getInstance().getGameThread().mFall2.clear();
		}
		
		if(mThread.backGround == 4){

			AppManager.getInstance().getGameThread().mSnow1.clear();
			AppManager.getInstance().getGameThread().mSnow2.clear();
		}
		
		AppManager.getInstance().getGameThread().mTouch.clear();
		AppManager.getInstance().getGameThread().mLBlock.clear();

	}

	protected void onResume() {
		super.onResume();

		sm.registerListener(accL, accSensor, SensorManager.SENSOR_DELAY_FASTEST); // 가속도

	}

	

	
	///////////////////////////////////////////
	//			게임 종료 메서드
	///////////////////////////////////////////
	
	public void ExitGame(Canvas canvas){

		
		if(canvas != null){
			
			
			if(gameover_y < 0)
			
				gameover_y += 20;
			}
			
		canvas.drawBitmap(MyView.imgBack, 0, 0, null);
		canvas.drawBitmap(AppManager.getInstance().getGameView().gameover, 0, gameover_y, null);
		}
	
	
private class Listener implements SensorEventListener {

		
		public void onAccuracyChanged(Sensor sensor, int accuracy) {

		}

		
		public void onSensorChanged(SensorEvent event) {

			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
				
					ACCX=event.values[1];
					ACCY=event.values[0];
					
			}

		}

	}
	
	}



