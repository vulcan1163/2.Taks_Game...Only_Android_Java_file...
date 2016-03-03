package com.example.taksintroduction;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

public class GameView extends SurfaceView implements Callback {

	GameThread mThread;
	int mScreenWidth, mScreenHeight;

	
	static Context mContext;
	static Bitmap imgBack, imgPad,top1,top2,top3,top4,top5;
	SurfaceHolder holder;
	Bitmap title, gameover, start;
	
	Paint mPaint = new Paint();
	
	public GameView(Context context, AttributeSet attrs){
		super(context, attrs);
	
		holder = getHolder();
		
		getHolder().addCallback(this);
		mContext = context;
		mThread = new GameThread(holder, mContext);
		
		AppManager.getInstance().setGameView(this);
		initScreenSize();
		initImageData();
		setFocusable(true);
		
	}
	
	
	///////////////////////////////////////////////
	//	 		Thread null check
	///////////////////////////////////////////////
	public GameThread ThreadCheck(){
		
		if(mThread == null){
			mThread = new GameThread(holder, mContext);
		}
		
		return mThread;
	}
	
	
	//////////////////////////////
	// 전체 스크린 사이즈 가져오기 
	//////////////////////////////
	
	private void initScreenSize(){
		Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);
		
		mScreenWidth = point.x;
		mScreenHeight = point.y;
		
	}
	
	///////////////////////////////
	// 일반 이미지 만들기
	///////////////////////////////
	
	
	private Bitmap createImage(int r){
		
		Resources res = getResources();
		
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
		
		return imgPrevConv;
	}
	
	
	

	private Bitmap createPadImage(int r){
		
		Resources res = getResources();
		
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
		
		float a,b;
		int w,h;
		
		
		w = mScreenWidth;
		h = mScreenHeight / 4;
		
		return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
	}
	
	
	
	
	
	////////////////////////////////
	// 이미지 전체 이미지 만들기
	////////////////////////////////
	
	private Bitmap createImageAllScreen(int r){
		
		Resources res = getResources();
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
		int w = mScreenWidth;
		int h = mScreenHeight;
		return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
		
	}
	
	
	
	////////////////////////////
	// 이미지 블록 생성 메서드
	/////////////////////////////
	
	private Bitmap createImageBlock(int r){
		
	Resources res = getResources();
		
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
		
		int w,h;
		
		w = mScreenWidth / 4;
		h = mScreenHeight / 4;

		return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
		
	}


	//////////////////////////////////////
	// 실제 나타날 이미지 경로 지정
	//////////////////////////////////////
	
	private void initImageData(){
		
		imgBack = createImageAllScreen(R.drawable.spring1);
		imgPad = createPadImage(R.drawable.pad);
		title = createImageAllScreen(R.drawable.title);
		gameover = createImageAllScreen(R.drawable.gameover);
		start = createImageBlock(R.drawable.start);

		 top1 = BitmapFactory.decodeResource(getResources(), R.drawable.top_1);
		 top2 = BitmapFactory.decodeResource(getResources(), R.drawable.top_2);
		 top3 = BitmapFactory.decodeResource(getResources(), R.drawable.top_3);
		 top4 = BitmapFactory.decodeResource(getResources(), R.drawable.top_4);
		 top5 = BitmapFactory.decodeResource(getResources(), R.drawable.top_5);
		 
	
		
	}
	
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		
		
	}

	
	public void surfaceCreated(SurfaceHolder arg0) {
		
		mThread.start();
	}

	
	public void surfaceDestroyed(SurfaceHolder arg0) {
		
		boolean done = true;
		
		
		while (done){
			ThreadCheck().ThreadStop();
			if(!mThread.isAlive())
			done=false;
		}
		
	}
	
	
	public boolean onTouchEvent(MotionEvent e){
		
		if(e.getAction() == MotionEvent.ACTION_DOWN){
			
		int x = (int)e.getX();
		int y = (int)e.getY();
		
		
		mThread.createActionBack(x);
		mThread.makeEffect(x, y);
		mThread.TouchCollision(x, y);
		
		if(GameThread.status == GameThread.GAME_OVER){
			
			AppManager.getInstance().getMyActivity().mediaPlayer.setLooping(true);
			AppManager.getInstance().getMyActivity().mediaPlayer.start();
			AppManager.getInstance().getGameActivity().back();
			
			}
		
		}
		return true;
	}

}
