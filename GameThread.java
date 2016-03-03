package com.example.taksintroduction;

import java.util.ArrayList;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.WindowManager;

public class GameThread extends Thread {

	final static int PROCESS = 1;
	final static int STAGE_CLEAR = 2;
	final static int GAME_OVER = 3;
	final static int ALL_CLEAR = 4;

	static int status = PROCESS;

	long LastRegen;
	long GameRegen;
	long BlockRegen;
	int score = 0;
	int speed = 2;
	int setBall_x;
	static int miss = 5;
	int touchPoint;

	ArrayList<Block> mBlock = new ArrayList<Block>();
	ArrayList<LongBlock> mLBlock = new ArrayList<LongBlock>();
	ArrayList<snow> mSnow1 = new ArrayList<snow>();
	ArrayList<snow> mSnow2 = new ArrayList<snow>();
	ArrayList<spring> mSpring1 = new ArrayList<spring>();
	ArrayList<spring> mSpring2 = new ArrayList<spring>();
	ArrayList<summer> mSummer1 = new ArrayList<summer>();
	ArrayList<summer> mSummer2 = new ArrayList<summer>();
	ArrayList<fall> mFall1 = new ArrayList<fall>();
	ArrayList<fall> mFall2 = new ArrayList<fall>();

	ArrayList<ActionBack> mActionBack = new ArrayList<ActionBack>();
	ArrayList<TouchEffect> mTouch = new ArrayList<TouchEffect>();
	ArrayList<SmallBall> sBall = new ArrayList<SmallBall>();

	int mWidth = 0;
	int mScreenWidth, mScreenHeight;
	float typeA, typeB, typeC, typeD;
	int sound_id_bubble, sound_id_gameover;
	SoundPool sound;
	Context mContext;
	Random rnd = new Random();
	static int backGround = 1;
	SurfaceHolder mHolder;

	Resources res;
	Bitmap title;
	Paint mPaint = new Paint();

	boundBall mball;

	boolean ThreadStart = true;
	boolean gameovercheck = true;

	public static Bitmap imgBack;

	public GameThread(SurfaceHolder holder, Context context) {
		mball = new boundBall();
		mHolder = holder;
		mContext = context;

		res = mContext.getResources();

		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 2;
		// imgBack = Bitmap.createScaledBitmap(imgBack, MyView.mScreenHeight,
		// MyView.mScreenWidth, true);
		LastRegen = System.currentTimeMillis();
		GameRegen = System.currentTimeMillis();
		BlockRegen = System.currentTimeMillis();
		initScreenSize();
		AppManager.getInstance().setGameThread(this);
		typeA = (float) (mScreenWidth * (1 / 4f));
		typeB = (float) (mScreenWidth * (2 / 4f));
		typeC = (float) (mScreenWidth * (3 / 4f));
		typeD = (float) (mScreenWidth * (4 / 4f));

		sound = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
		sound_id_gameover = sound.load(mContext, R.raw.gameover, 1);
		sound_id_bubble = sound.load(mContext, R.raw.bubble, 1);
		
	}

	// -------------------------------------
	// 작은 비눗방울 만들기
	// -------------------------------------
	private void MakeSmallBall(int kind) {
		Random rnd = new Random();
		int count = rnd.nextInt(9) + 7; // 7~15개
		for (int i = 1; i <= count; i++) {
			int ang = rnd.nextInt(360);
			sBall.add(new SmallBall(mContext, kind, ang, mScreenWidth,
					mScreenHeight));
		}
	}

	// ////////////////////////////////////
	// 전체사이즈
	// /////////////////////////////////////
	private Bitmap createImageAllScreen(int r) {

		Resources res = AppManager.getInstance().getResources();
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
		int w = mScreenWidth;
		int h = mScreenHeight;
		return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
	}

	// ////////////////////////////////////
	// 전체사이즈
	// /////////////////////////////////////
	private Bitmap createImage(int r) {

		Resources res = AppManager.getInstance().getResources();
		Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);

		return imgPrevConv;
	}

	// /////////////////////////////////////////////
	// 전체 스크린 사이즈 가져오기
	// /////////////////////////////////////////////

	private void initScreenSize() {
		Display display = ((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		Point point = new Point();
		display.getSize(point);

		mScreenWidth = point.x;
		mScreenHeight = point.y;

	}

	// ///////////////////////////////////////////////
	// 터치시에 나타나는 막대
	// //////////////////////////////////////////////

	public void createActionBack(int x) {

		touchPoint = x;

		if (x < typeA) {
			if (mActionBack.size() < 4)
				mActionBack.add(new ActionBack(1));
		}

		if (x > typeA && x < typeB) {
			if (mActionBack.size() < 4)
				mActionBack.add(new ActionBack(2));
		}

		if (x > typeB && x < typeC) {
			if (mActionBack.size() < 4)
				mActionBack.add(new ActionBack(3));
		}

		if (x > typeC && x < typeD) {
			if (mActionBack.size() < 4)
				mActionBack.add(new ActionBack(4));
		}
	}

	// ///////////////////////////////////////////////
	// 블럭 움직임 (Thread 등록)
	// ///////////////////////////////////////////////

	public void AllMoveBlock() {

		mball.move();

		for (int i = mTouch.size() - 1; i >= 0; i--) { // 긴블럭 움직임 및 삭제

			if (mTouch.get(i).dead == true) {
				mTouch.remove(i);

			}
		}

		for (int i = mActionBack.size() - 1; i >= 0; i--) { // 긴블럭 움직임 및 삭제

			// mActionBack.get(i).checkActionBack(LastRegen);

			if (mActionBack.get(i).dead == true) {
				mActionBack.remove(i);

			}
		}

		for (int i = mLBlock.size() - 1; i >= 0; i--) { // 긴블럭 움직임 및 삭제
			mLBlock.get(i).moveBlock();

			if (mLBlock.get(i).dead == true) {
				mLBlock.remove(i);

			}
		}

		for (int i = mBlock.size() - 1; i >= 0; i--) { // 작은 블럭 움직임 및 삭제
			mBlock.get(i).moveBlock();

			if (mBlock.get(i).dead == true) {

				mBlock.remove(i);

			}
		}

		// 작은 비눗방울 이동
		for (int i = sBall.size() - 1; i >= 0; i--) {
			sBall.get(i).MoveBall();
			if (sBall.get(i).dead == true)
				sBall.remove(i);
		}

		ActionBackCheck();

	}

	// //////////////////////////////////////////
	// 액션바 시간 체크
	// //////////////////////////////////////////
	public void ActionBackCheck() {

		if (System.currentTimeMillis() - LastRegen >= 800) {
			for (int i = mActionBack.size() - 1; i >= 0; i--) {
				mActionBack.get(i).dead = true;
				LastRegen = System.currentTimeMillis();
			}
		}
	}

	// /////////////////////////////////////////////////
	// 게임 난이도 설정
	// /////////////////////////////////////////////////
	public void GameState() {
		if (System.currentTimeMillis() - GameRegen >= 15000) {
			for (int i = mBlock.size() - 1; i >= 0; i--) {

				switch (mBlock.get(i).Level) {

				case 0:
					mBlock.get(i).sy = 20;
					break;

				case 1:
					mBlock.get(i).sy = 40;
					break;

				case 2:
					mBlock.get(i).sy = 60;
					break;
				}
			}
		}
		if (System.currentTimeMillis() - GameRegen >= 30000) {
			for (int i = mBlock.size() - 1; i >= 0; i--) {

				switch (mBlock.get(i).Level) {

				case 0:
					mBlock.get(i).sy = 30;
					break;

				case 1:
					mBlock.get(i).sy = 50;
					break;

				case 2:
					mBlock.get(i).sy = 70;
					break;
				}

			}
		}
		if (System.currentTimeMillis() - GameRegen >= 45000) {
			for (int i = mBlock.size() - 1; i >= 0; i--) {

				switch (mBlock.get(i).Level) {

				case 0:
					mBlock.get(i).sy = 40;
					break;

				case 1:
					mBlock.get(i).sy = 60;
					break;

				case 2:
					mBlock.get(i).sy = 80;
					break;
				}

			}

		}

		if (System.currentTimeMillis() - GameRegen >= 60000) {
			for (int i = mBlock.size() - 1; i >= 0; i--) {
				switch (mBlock.get(i).Level) {

				case 0:
					mBlock.get(i).sy = 50;
					break;

				case 1:
					mBlock.get(i).sy = 70;
					break;

				case 2:
					mBlock.get(i).sy = 90;
					break;
				}

			}

		}

		if (System.currentTimeMillis() - GameRegen >= 75000) {
			for (int i = mBlock.size() - 1; i >= 0; i--) {
				switch (mBlock.get(i).Level) {

				case 0:
					mBlock.get(i).sy = 60;
					break;

				case 1:
					mBlock.get(i).sy = 80;
					break;

				case 2:
					mBlock.get(i).sy = 100;
					break;
				}

			}

		}
		if (System.currentTimeMillis() - GameRegen >= 90000) {
			for (int i = mBlock.size() - 1; i >= 0; i--) {
				switch (mBlock.get(i).Level) {

				case 0:
					mBlock.get(i).sy = 70;
					break;

				case 1:
					mBlock.get(i).sy = 90;
					break;

				case 2:
					mBlock.get(i).sy = 110;
					break;
				}

			}

		}
	}

	// //////////////////////////////////
	// 블럭 생성
	// //////////////////////////////////

	public void makeBlock() {

		if (mBlock.size() > 8)
			return;

		Random rnd1 = new Random();// 블럭 종류 선택하는 난수 발생
		int n = rnd1.nextInt(4) + 1;

		if (System.currentTimeMillis() - BlockRegen >= 450) { // 1초 간격으로 생성 하도록

			Random rnd2 = new Random(); // 긴 막대 발생 빈도 설정
			int n2 = rnd2.nextInt(4);

			if (n2 == 0){
				if(mLBlock.size() < 3)
				mLBlock.add(new LongBlock(n));
			}
			else
				mBlock.add(new Block(n));

			BlockRegen = System.currentTimeMillis(); // 다시 1초 시작
		}
	}

	// /////////////////////////////////////////////
	// 터치 이펙트 발생
	// ////////////////////////////////////////////
	public void makeEffect(int x, int y) {
		boolean flag = true;

		if (flag) {
			mTouch.add(new TouchEffect(x, y));
			flag = false;
		}
	}

	// /////////////////////////////////////////////
	// 눈 움직임
	// /////////////////////////////////////////////

	public void MoveSnow() {

		synchronized (mHolder) {
			if (mSnow1.size() < 25)
				mSnow1.add(new snow(2));
			if (mSnow1.size() < 25)
				mSnow1.add(new snow(1));
		}

		int n = rnd.nextInt(300);

		for (snow tmp : mSnow1)
			tmp.MoveSnow(n);
		for (snow tmp : mSnow2)
			tmp.MoveSnow(n);
		AppManager.getInstance().m_gameview.postInvalidate();
	}

	// /////////////////////////////////////////////
	// 눈 움직임
	// /////////////////////////////////////////////
	public void MoveSummer() {

		synchronized (mHolder) {
			if (mSummer1.size() < 25)
				mSummer1.add(new summer(2));
			if (mSummer1.size() < 25)
				mSummer1.add(new summer(1));
		}

		int n = rnd.nextInt(300);

		for (summer tmp : mSummer1)
			tmp.MoveSnow(n);
		for (summer tmp : mSummer2)
			tmp.MoveSnow(n);
		AppManager.getInstance().m_gameview.postInvalidate();
	}

	// /////////////////////////////////////////////
	// 눈 움직임
	// /////////////////////////////////////////////
	public void MoveFall() {

		synchronized (mHolder) {
			if (mFall1.size() < 25)
				mFall1.add(new fall(2));
			if (mFall1.size() < 25)
				mFall1.add(new fall(1));
		}

		int n = rnd.nextInt(300);

		for (fall tmp : mFall1)
			tmp.MoveSnow(n);
		for (fall tmp : mFall2)
			tmp.MoveSnow(n);
		AppManager.getInstance().m_gameview.postInvalidate();
	}

	// /////////////////////////////////////////////
	// 눈 움직임
	// /////////////////////////////////////////////

	public void MoveSpring() {

		synchronized (mHolder) {
			if (mSpring1.size() < 25)
				mSpring1.add(new spring(2));
			if (mSpring1.size() < 25)
				mSpring1.add(new spring(1));
		}

		int n = rnd.nextInt(300);

		for (spring tmp : mSpring1)
			tmp.MoveSnow(n);
		for (spring tmp : mSpring2)
			tmp.MoveSnow(n);

		AppManager.getInstance().m_gameview.postInvalidate();

	}

	// /////////////////////////////////////////////
	// 모든 이미지 삭제
	// /////////////////////////////////////////////
	public void RemoveAll() {

		mBlock.removeAll(mBlock);
		mLBlock.removeAll(mLBlock);
		mActionBack.removeAll(mActionBack);
		mTouch.removeAll(mTouch);

		if (backGround == 1) {

			mSpring1.removeAll(mSpring1);
			mSpring2.removeAll(mSpring2);
		}

		if (backGround == 2) {

			mSummer1.removeAll(mSummer1);
			mSummer2.removeAll(mSummer2);
		}

		if (backGround == 3) {

			mFall1.removeAll(mFall1);
			mFall2.removeAll(mFall2);
		}

		if (backGround == 4) {

			mSnow1.removeAll(mSnow1);
			mSnow2.removeAll(mSnow2);
		}
	}

	// ///////////////////////////////////////////////////
	// 모든 이미지를 그림(Thread 등록)
	// ///////////////////////////////////////////////////

	public void DrawAll(Canvas canvas) {

		Paint p = new Paint();
		p.setColor(Color.WHITE);
		p.setTextSize(30);

		long Gametime = System.currentTimeMillis();

		canvas.drawBitmap(MyView.imgBack, 0, 0, mPaint);

		canvas.drawBitmap(GameView.imgPad, 0,
				(float) (mScreenHeight * (3 / 4f)), mPaint);

		canvas.drawBitmap(mball.ball, mball.x, mball.y, mPaint);
		switch (miss) {
		case 5:
			canvas.drawBitmap(GameView.top1, 0, 0, mPaint);
			break;

		case 4:
			canvas.drawBitmap(GameView.top2, 0, 0, mPaint);
			break;

		case 3:
			canvas.drawBitmap(GameView.top3, 0, 0, mPaint);
			break;

		case 2:
			canvas.drawBitmap(GameView.top4, 0, 0, mPaint);
			break;

		case 1:
			canvas.drawBitmap(GameView.top5, 0, 0, mPaint);
			break;
		}
		if (backGround == 4) {
			for (snow tmp : mSnow1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 4) {
			for (snow tmp : mSnow2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 3) {
			for (fall tmp : mFall1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 3) {
			for (fall tmp : mFall2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 1) {
			for (spring tmp : mSpring1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 1) {
			for (spring tmp : mSpring2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 2) {
			for (summer tmp : mSummer1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		if (backGround == 2) {
			for (summer tmp : mSummer2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}

		for (int i = mBlock.size() - 1; i >= 0; i--) {
			canvas.drawBitmap(mBlock.get(i).block, mBlock.get(i).x,
					mBlock.get(i).y, null);

		}

		for (int i = mLBlock.size() - 1; i >= 0; i--) {

			mLBlock.get(i).Update(Gametime);
			mLBlock.get(i).Draw(canvas);
			// canvas.drawBitmap(mLBlock.get(i).block, mLBlock.get(i).x,
			// mLBlock.get(i).y,null);

		}

		for (int i = mActionBack.size() - 1; i >= 0; i--) {
			canvas.drawBitmap(mActionBack.get(i).back, mActionBack.get(i).x,
					mActionBack.get(i).y, null);
		}

		for (int i = mTouch.size() - 1; i >= 0; i--) {
			mTouch.get(i).Update(Gametime);
			mTouch.get(i).Draw(canvas);
		}

		for (int i = sBall.size() - 1; i >= 0; i--) {
			canvas.drawBitmap(sBall.get(i).imgBall,
					sBall.get(i).x - sBall.get(i).rad,
					sBall.get(i).y - sBall.get(i).rad, null);
		}

		p.setColor(Color.BLACK);

		if (AppManager.getInstance().getResultActivity().mScore > 3000)
			p.setColor(Color.GREEN);

		if (AppManager.getInstance().getResultActivity().mScore > 6000)
			p.setColor(Color.BLUE);

		if (AppManager.getInstance().getResultActivity().mScore > 10000)
			p.setColor(Color.RED);

		if (AppManager.getInstance().getResultActivity().mScore > 15000)
			p.setColor(Color.YELLOW);

		p.setTextSize(150);
		p.setTypeface(AppManager.getInstance().getGameActivity().mFont);

		canvas.drawText(" score :  "
				+ AppManager.getInstance().getResultActivity().mScore, 50, 400,
				p);

		

	}

	// ///////////////////////////////////
	// 충돌 체크
	// //////////////////////////////////
	public void Collision() {

		for (int i = mBlock.size() - 1; i >= 0; i--) {
			if (CollisionManager.CheckBoxToBox(mball.m_boundBox,
					mBlock.get(i).m_boundBox)) {

				MakeSmallBall(mBlock.get(i).x + mScreenWidth/8);
				sound.play(sound_id_bubble, 1, 1, 0, 0, 1);
				switch (AppManager.getInstance().getResultActivity().Level) {
				case 0:
					AppManager.getInstance().getResultActivity().mScore += 100;
					break;

				case 1:
					AppManager.getInstance().getResultActivity().mScore += 200;
					break;

				case 2:
					AppManager.getInstance().getResultActivity().mScore += 300;
					break;
				}

				mBlock.remove(i);
			}

		}
	}

	// //////////////////////////////////
	// 충돌 판정
	// //////////////////////////////////

	public void TouchCollision(int x, int y) {

		for (int i = mLBlock.size() - 1; i >= 0; i--) {

			if (mLBlock.get(i).m_x < x
					&& mLBlock.get(i).m_x + mLBlock.get(i).typeA > x
					&& (float) (mScreenWidth * (3 / 4f)) < y
					&& mScreenHeight > y) {
				if (mLBlock.get(i).m_y > (float) (mScreenWidth * (3 / 4f))) {


					if (touchPoint < typeA) {
						// mBomb.add(new TouchBomb(1));
						MakeSmallBall(mLBlock.get(i).m_x + mScreenWidth/8);

						sound.play(sound_id_bubble, 1, 1, 0, 0, 1);
						switch (AppManager.getInstance().getResultActivity().Level) {
						case 0:
							AppManager.getInstance().getResultActivity().mScore += 100;
							break;

						case 1:
							AppManager.getInstance().getResultActivity().mScore += 200;
							break;

						case 2:
							AppManager.getInstance().getResultActivity().mScore += 300;
							break;
						}
					}

					if (touchPoint > typeA && touchPoint < typeB) {
						// mBomb.add(new TouchBomb(2));
						MakeSmallBall(mLBlock.get(i).m_x + mScreenWidth/8);
						sound.play(sound_id_bubble, 1, 1, 0, 0, 1);
						switch (AppManager.getInstance().getResultActivity().Level) {
						case 0:
							AppManager.getInstance().getResultActivity().mScore += 100;
							break;

						case 1:
							AppManager.getInstance().getResultActivity().mScore += 200;
							break;

						case 2:
							AppManager.getInstance().getResultActivity().mScore += 300;
							break;
						}
					}

					if (touchPoint > typeB && touchPoint < typeC) {
						// mBomb.add(new TouchBomb(3));
						MakeSmallBall(mLBlock.get(i).m_x + mScreenWidth/8);
						sound.play(sound_id_bubble, 1, 1, 0, 0, 1);
						switch (AppManager.getInstance().getResultActivity().Level) {
						case 0:
							AppManager.getInstance().getResultActivity().mScore += 100;
							break;

						case 1:
							AppManager.getInstance().getResultActivity().mScore += 200;
							break;

						case 2:
							AppManager.getInstance().getResultActivity().mScore += 300;
							break;
						}
					}

					if (touchPoint > typeC && touchPoint < typeD) {
						// mBomb.add(new TouchBomb(4));
						MakeSmallBall(mLBlock.get(i).m_x + mScreenWidth/8);
						sound.play(sound_id_bubble, 1, 1, 0, 0, 1);
						switch (AppManager.getInstance().getResultActivity().Level) {
						case 0:
							AppManager.getInstance().getResultActivity().mScore += 100;
							break;

						case 1:
							AppManager.getInstance().getResultActivity().mScore += 200;
							break;

						case 2:
							AppManager.getInstance().getResultActivity().mScore += 300;
							break;
						}
					}

					mLBlock.remove(i);

				}
			}
		}
/*
		for (int i = mLBlock.size() - 1; i >= 0; i--) {

			if (mLBlock.get(i).x < x
					&& mLBlock.get(i).x + mLBlock.get(i).typeA > x
					&& (float) (mScreenWidth * (3 / 4f)) < y
					&& mScreenHeight > y) {
				if (mLBlock.get(i).y > (float) (mScreenWidth * (3 / 4f)))
					mLBlock.remove(i);
				// miss--;
			}
		}
*/
	}

	// //////////////////////////////////
	// 스레드 상태
	// //////////////////////////////////
	public void ThreadStop() {

		ThreadStart = false;

		synchronized (this) {
			this.notify();
		}

	}

	// ////////////////////////////////////
	// 스레드 동작
	// ///////////////////////////////////

	public void run() {
		Canvas canvas = null;

		while (ThreadStart) {

			canvas = mHolder.lockCanvas();

			try {

				synchronized (mHolder) {

					switch (status) {

					case PROCESS:

						if (backGround == 1)
							MoveSpring();
						if (backGround == 2)
							MoveSummer();
						if (backGround == 3)
							MoveFall();
						if (backGround == 4)
							MoveSnow();
						Collision();
						AllMoveBlock();
						DrawAll(canvas);
						makeBlock();
						GameState();

						break;

					case GAME_OVER:

						AppManager.getInstance().getGameActivity()
								.ExitGame(canvas);

						break;
					}
				}
			} finally {
				if (canvas != null)
					mHolder.unlockCanvasAndPost(canvas);

			}
		}

	}

}