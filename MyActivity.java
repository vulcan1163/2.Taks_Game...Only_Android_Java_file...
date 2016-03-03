package com.example.taksintroduction;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {

	public MediaPlayer mediaPlayer;

	int i = 0;
	int backNumber = 1;

	boolean isPageOpen = false;

	Animation translateLeftAnim;
	Animation translateRightAnim;

	Animation frameAnimation_1, frameAnimation_2;

	LinearLayout slidingPage01;

	Button OKbtn, join, inputjoin, start, cencel;
	ImageView button1, title;
	/*
	 * group button
	 */
	RadioButton BG_OFF, BG_ON, BK_Spring, BK_Summer, BK_Fall, BK_Winter;
	LinearLayout testBack, mainlayout, joinlayout;

	EditText id, password, joinid, joinpw;
	TextView test;
	FrameLayout flayout;
	ImageView imgBack;

	MyThread mThread;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		AppManager.getInstance().setMyActivity(this);
		button1 = (ImageView) findViewById(R.id.button1);

		BG_OFF = (RadioButton) findViewById(R.id.RG_Music_Off);
		BG_ON = (RadioButton) findViewById(R.id.RG_Music_On);

		BK_Spring = (RadioButton) findViewById(R.id.RG_BACK_Spring);
		BK_Summer = (RadioButton) findViewById(R.id.RG_BACK_Summer);
		BK_Fall = (RadioButton) findViewById(R.id.RG_BACK_Fall);
		BK_Winter = (RadioButton) findViewById(R.id.RG_BACK_Winter);

		testBack = (LinearLayout) findViewById(R.id.back);
		mainlayout = (LinearLayout) findViewById(R.id.mainlayout);
		joinlayout = (LinearLayout) findViewById(R.id.joinlayout);

		OKbtn = (Button) findViewById(R.id.OK);

		cencel = (Button) findViewById(R.id.cencel);
		start = (Button) findViewById(R.id.start);
		join = (Button) findViewById(R.id.join);
		inputjoin = (Button) findViewById(R.id.inputJoin);

		id = (EditText) findViewById(R.id.id);
		password = (EditText) findViewById(R.id.password);
		joinid = (EditText) findViewById(R.id.joinID);
		joinpw = (EditText) findViewById(R.id.joinPW);


		slidingPage01 = (LinearLayout) findViewById(R.id.slidingPage01);

		translateLeftAnim = AnimationUtils.loadAnimation(this,
				R.anim.translate_left);
		translateRightAnim = AnimationUtils.loadAnimation(this,
				R.anim.translate_right);

		SlidingPageAnimationListener animListener = new SlidingPageAnimationListener();
		translateLeftAnim.setAnimationListener(animListener);
		translateRightAnim.setAnimationListener(animListener);
		
		
		mediaPlayer = MediaPlayer.create(this, R.raw.mspring);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		
		frameAnimation_1 = AnimationUtils.loadAnimation(this,
				R.anim.frame_animation_1);
		frameAnimation_2 = AnimationUtils.loadAnimation(this,
				R.anim.frame_animation_2);

		frameAnimation_1.setDuration(1000);
		frameAnimation_2.setDuration(1000);

		frameAnimation_1.setFillAfter(true);
		frameAnimation_2.setFillAfter(true);

		frameAnimation_1.setFillAfter(true);
		frameAnimation_2.setFillAfter(true);

		title = (ImageView) findViewById(R.id.title);

		flayout = (FrameLayout) findViewById(R.id.flayout_main);
		flayout.setBackgroundResource(R.drawable.spring1);

		final DBmanager dbManager = new DBmanager(getApplicationContext(),
				"ID.db", null, 1);

		start.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String ID = id.getText().toString();
				String PW = password.getText().toString();
				
				
				
				if(dbManager.checkID(ID, PW)){
				AppManager.getInstance().mMyThread.ThreadStop();

				Intent result = new Intent(getApplication(), ResultActivity.class);
					
				result.putExtra("back", backNumber);
				result.putExtra("id", ID);
				
				startActivityForResult(result, 1001);
				finish();
				}
				else Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 다릅니다 다시시도 해주세요.", Toast.LENGTH_SHORT).show();

			}
		});
		cencel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				mainlayout.setVisibility(View.VISIBLE);
				joinlayout.setVisibility(View.INVISIBLE);
			}
		});
		join.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				mainlayout.setVisibility(View.INVISIBLE);
				joinlayout.setVisibility(View.VISIBLE);
			}
		});
		inputjoin.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String id = joinid.getText().toString();
				String pw = joinpw.getText().toString();
				
				dbManager.insert("insert into ID_LIST values(null, '" +id  + "', '" + pw + "');");
				mainlayout.setVisibility(View.VISIBLE);
				joinlayout.setVisibility(View.INVISIBLE);
			}
		});

		h.sendEmptyMessage(0);

	}

	// ///////////////////////////////////////
	// 일시 중단
	// ///////////////////////////////////////
	protected void onPause() {

		if (mThread != null) {
			mThread.ThreadStop();
			mThread = null;
		}

		super.onPause();
	}

	Handler h = new Handler() {

		@Override
		public void handleMessage(android.os.Message msg) {

			if (i == 2) {
				i = 0;

				h.sendEmptyMessage(0);

			}

			else if (i == 0) {
				title.startAnimation(frameAnimation_1);
				h.sendEmptyMessageDelayed(0, 1000);
				i++;

			}

			else if (i == 1) {

				title.startAnimation(frameAnimation_2);
				h.sendEmptyMessageDelayed(0, 1000);
				i++;
			}
			// TODO: Implement this method
			super.handleMessage(msg);
		}

	};

	public void onButton1Clicked(View v) {

		if (isPageOpen) {
			slidingPage01.startAnimation(translateRightAnim);
			button1.setVisibility(View.VISIBLE);
		} else {
			slidingPage01.setVisibility(View.VISIBLE);
			slidingPage01.startAnimation(translateLeftAnim);
			button1.setVisibility(View.INVISIBLE);
		}
		
	}

	public void onButtonOKClicked(View v) {
		if (isPageOpen) {
			slidingPage01.startAnimation(translateRightAnim);
			button1.setVisibility(View.VISIBLE);
		} else {
			slidingPage01.setVisibility(View.VISIBLE);
			slidingPage01.startAnimation(translateLeftAnim);
			button1.setVisibility(View.INVISIBLE);
		}
	}

	public void MusicOnClick(View v) {

		mediaPlayer.setLooping(true);
		mediaPlayer.start();
	}

	public void MusicOFFClick(View v) {

		mediaPlayer.pause();

	}

	@Override
	protected void onDestroy() {
	
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

	public void SpringClick(View v) {

		killMediaPlayer();
		// flayout.setBackgroundResource(R.drawable.spring1);

		// MyView.imgBack =
		// BitmapFactory.decodeResource(MyView.mContext.getResources(),
		// R.drawable.spring1);

		MyView.imgBack = createImageAllScreen(R.drawable.spring1);
		mediaPlayer = MediaPlayer.create(this, R.raw.mspring);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		backNumber = 1;
		MyThread.backGround = 1;

	}

	private Bitmap createImageAllScreen(int r) {

		Resources res = getResources();
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
		int w = MyView.mScreenWidth;
		int h = MyView.mScreenHeight;

		return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
	}

	public void SummerClick(View v) {
		killMediaPlayer();

		// flayout.setBackgroundResource(R.drawable.summer1);

		MyView.imgBack = createImageAllScreen(R.drawable.summer1);

		mediaPlayer = MediaPlayer.create(this, R.raw.msummer);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		backNumber = 2;
		MyThread.backGround = 2;
	}

	public void FallClick(View v) {
		killMediaPlayer();

		// flayout.setBackgroundResource(R.drawable.fall);

		MyView.imgBack = createImageAllScreen(R.drawable.fall);
		mediaPlayer = MediaPlayer.create(this, R.raw.mfall);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();

		backNumber = 3;
		MyThread.backGround = 3;
	}

	public void WinterClick(View v) {

		killMediaPlayer();

		// flayout.setBackgroundResource(R.drawable.winter);

		// MyView.imgBack =
		// BitmapFactory.decodeResource(MyView.mContext.getResources(),
		// R.drawable.winter);
		MyView.imgBack = createImageAllScreen(R.drawable.winter);

		mediaPlayer = MediaPlayer.create(this, R.raw.mwinter);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();

		backNumber = 4;
		MyThread.backGround = 4;
	}

	public void onStartClick(View v) {

		AppManager.getInstance().mMyThread.ThreadStop();

		Intent result = new Intent(getApplication(), ResultActivity.class);

		result.putExtra("back", backNumber);
		startActivityForResult(result, 1001);
		finish();

	}


	private class SlidingPageAnimationListener implements Animation.AnimationListener {

		public void onAnimationEnd(Animation animation) {
			if (isPageOpen) {
				slidingPage01.setVisibility(View.INVISIBLE);

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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		// noinspection SimplifiableIfStatement

		return super.onOptionsItemSelected(item);
	}
	public void exit(View v){
		
		killMediaPlayer();
		finish();
	}

}
