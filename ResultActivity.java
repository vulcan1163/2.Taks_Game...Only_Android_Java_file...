package com.example.taksintroduction;

import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class ResultActivity extends Activity implements OnInitListener{

	final int REQ_CODE_SELECT_IMAGE = 100;
	
	String name;
	
	int backnumber;
	int score=0;
	int sound_id;
	static int mScore;
	int Level=0;
	
	boolean intentCheck;
	boolean isPageOpen = false;
	boolean anicheck = false;
	
	Animation frameAnimation_1, frameAnimation_2;
	SoundPool sound;
	LinearLayout Lpage1, Lpage2, Lpage3;
	ResultThread mThread;
	Bitmap image_bitmap;
	ImageView image;
	TextView NameEdit, ScoreEdit;
	Button reSet, Del;
	Block mBlock;
	DBmanager dbManager;
	RadioButton rb1, rb2, rb3;
	
	TextToSpeech myTTS;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.result);
		
		Intent intent = getIntent();
		backnumber = intent.getIntExtra("back", 1);
		
		
		switch (backnumber) {
		
		case 1:
			ResultThread.backGround = 1;
			break;
			
		case 2:
			ResultThread.backGround = 2;
			break;

		case 3:
			ResultThread.backGround = 3;
			break;

		case 4:
			ResultThread.backGround = 4;
			break;

		}

		
		ScoreEdit = (TextView)findViewById(R.id.score);
		//reSet = (Button)findViewById(R.id.reSet);
		//Del = (Button)findViewById(R.id.Del);
		
		AppManager.getInstance().setResultActivity(this);
		
		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		sound_id = sound.load(this, R.raw.bell, 1);

		dbManager = new DBmanager(getApplicationContext(), "Score.db", null, 1);
		

		Lpage1 = (LinearLayout) findViewById(R.id.Lpage1);
		Lpage2 = (LinearLayout) findViewById(R.id.Lpage2);
		Lpage3 = (LinearLayout) findViewById(R.id.Lpage3);
		
/*
		String test = "abc";
		dbManager.delete("delete from Score_LIST where name = '" + test + "';");

		String test1 = "zxc";
		dbManager.delete("delete from Score_LIST where name = '" + test1 + "';");
	*/
		ScoreEdit.setText(dbManager.PrintData());

		
		frameAnimation_1 = AnimationUtils.loadAnimation(this,
				R.anim.result_translate_up);
		frameAnimation_2 = AnimationUtils.loadAnimation(this,
				R.anim.result_translate_down);
		
		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		frameAnimation_1.setAnimationListener(animListener);
		frameAnimation_2.setAnimationListener(animListener);
		

		frameAnimation_1.setDuration(1000);
		frameAnimation_2.setDuration(1000);

		frameAnimation_1.setFillAfter(true);
		frameAnimation_2.setFillAfter(true);

		frameAnimation_1.setFillAfter(true);
		frameAnimation_2.setFillAfter(true);
		
/*
		reSet.setOnClickListener(new OnClickListener() {
			
			
			public void onClick(View v) {
				
				if(AppManager.getInstance().mGameThread != null){
					
					Intent intent = getIntent();
					score = intent.getIntExtra("score", 1);
				
			
				//ScoreEdit.setText(dbManager.PrintData());
				}
				else Toast.makeText(getApplicationContext(), "게임을 최소 1회 이상 하셔야 합니다", Toast.LENGTH_SHORT).show();
			}
		});
	
	*/
	/*
		Del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	*/
		
		intentCheck = true;
		
		myTTS = new TextToSpeech(getApplicationContext(),this);
		
		myTTS.setLanguage(Locale.US);
		String welcom = "Welcom Rhythm action game";
		 myTTS.speak(welcom, TextToSpeech.QUEUE_FLUSH, null);
		 
		  rb1 = (RadioButton)findViewById(R.id.easy);
		  rb2 = (RadioButton)findViewById(R.id.normal);
		  rb3 = (RadioButton)findViewById(R.id.hard);
		  
		  Typeface tf = Typeface.createFromAsset(getAssets(), "RixToyGray.ttf");
		  rb1.setTypeface(tf);
		  rb2.setTypeface(tf);
		  rb3.setTypeface(tf);
		  
		  ScoreEdit.setTypeface(tf);
		  ScoreEdit.setTextSize(22);
	}
	


	// ///////////////////////////////////////
	// 일시 중단
	// ///////////////////////////////////////
	protected void onPause() {

		if (mThread != null) {
			mThread.ThreadState(false);
			mThread = null;
		}

		super.onPause();
	}

	Button.OnClickListener myButtonClick = new Button.OnClickListener() {

		public void onClick(View v) {

			AppManager.getInstance().getResultThread().ThreadStart = false;

			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
			intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

		}

	};

	public void onDestroy() {

		AppManager.getInstance().getResultThread().ThreadStart = false;
		myTTS.shutdown();
		super.onDestroy();
	}

	public void information1(View v) {

		
			Lpage1.setVisibility(View.VISIBLE);
			Lpage2.setVisibility(v.INVISIBLE);
			Lpage3.setVisibility(v.INVISIBLE);
			
			String ranking = dbManager.Ranking();
			 myTTS.speak(ranking, TextToSpeech.QUEUE_FLUSH, null);
	}

	public void information2(View v) {

		sound.play(sound_id, 1, 1, 0, 0, 1);
		Lpage1.setVisibility(v.INVISIBLE);

		Lpage2.setVisibility(v.VISIBLE);

		Lpage3.setVisibility(v.INVISIBLE);
	}

	public void information3(View v) {
		sound.play(sound_id, 1, 1, 0, 0, 1);
		Lpage1.setVisibility(v.INVISIBLE);

		Lpage2.setVisibility(v.INVISIBLE);

		Lpage3.setVisibility(v.VISIBLE);
		
	}
	
	public void playGame(View v){

		AppManager.getInstance().getResultThread().ThreadStart = false;
		AppManager.getInstance().getGameThread().status = 1;
		AppManager.getInstance().getGameThread().miss = 5;
		AppManager.getInstance().getMyActivity().mediaPlayer.pause();
		mScore = 0;
		Intent result = new Intent(getApplication(), GameActivity.class);
		result.putExtra("back", backnumber);
		if(intentCheck){
		startActivity(result);
		
		intentCheck = false;
		}
		finish();
		
		
	}
	
	public void easy(View v){
		Level = 0;
	}
	
	public void normal(View v){
		Level = 1;
	}
	
	public void hard(View v ){
		Level = 2;
	}
	
	public void logout(View v){
		
		AppManager.getInstance().getResultThread().ThreadStart = false;
		AppManager.getInstance().getMyActivity().killMediaPlayer();
		Intent result = new Intent(getApplication(), MyActivity.class);
		result.putExtra("back", backnumber);
		if(intentCheck){
		startActivity(result);
		intentCheck = false;
		}
		finish();
	}
	
	public void exit(View v){
		AppManager.getInstance().getMyActivity().killMediaPlayer();
		finish();
	}

	private class SlidingPageAnimationListener implements Animation.AnimationListener {

		public void onAnimationEnd(Animation animation) {
			if (isPageOpen) {
				
				isPageOpen = false;
			} else {

				isPageOpen = true;
			}
		}

		public void onAnimationRepeat(Animation animation) {

		}

		public void onAnimationStart(Animation animation) {

		}

	}

	@Override
	public void onInit(int arg0) {
	
	}
}
