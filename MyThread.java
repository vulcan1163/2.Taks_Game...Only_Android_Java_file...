package com.example.taksintroduction;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;

public class MyThread extends Thread{
	
	ArrayList<snow> mSnow1 = new ArrayList<snow>();
	ArrayList<snow> mSnow2 = new ArrayList<snow>();
	ArrayList<spring> mSpring1 = new ArrayList<spring>();
	ArrayList<spring> mSpring2 = new ArrayList<spring>();
	ArrayList<summer> mSummer1 = new ArrayList<summer>();
	ArrayList<summer> mSummer2 = new ArrayList<summer>();
	ArrayList<fall> mFall1 = new ArrayList<fall>();
	ArrayList<fall> mFall2 = new ArrayList<fall>();
	
	Random rnd = new Random();
	static int backGround = 1;
	SurfaceHolder mHolder;
	Paint mPaint = new Paint();
	
 boolean ThreadStart = true;
	
	public static Bitmap imgBack;
	
	
	public MyThread(SurfaceHolder holder, Context context){
		mHolder = holder;
		
	//	BitmapFactory.Options options = new BitmapFactory.Options();
	//	options.inSampleSize = 2;
		
		

	//	imgBack = Bitmap.createScaledBitmap(imgBack, MyView.mScreenHeight, MyView.mScreenWidth, true);
	AppManager.getInstance().setMyThread(this);
	}
	


	public void MoveSnow(){
		
		synchronized (mHolder) {
			if(mSnow1.size() < 100) mSnow1.add(new snow(2));
			if(mSnow1.size() < 50) mSnow1.add(new snow(1));
		}
		
		int n = rnd.nextInt(300);
		
		for(snow tmp : mSnow1) tmp.MoveSnow(n);
		for(snow tmp : mSnow2) tmp.MoveSnow(n);
		AppManager.getInstance().mMyView.postInvalidate();
	}
	


	public void MoveSummer(){
		
		synchronized (mHolder) {
			if(mSummer1.size() < 100) mSummer1.add(new summer(2));
			if(mSummer1.size() < 50) mSummer1.add(new summer(1));
		}
		
		int n = rnd.nextInt(300);
		
		for(summer tmp : mSummer1) tmp.MoveSnow(n);
		for(summer tmp : mSummer2) tmp.MoveSnow(n);
		AppManager.getInstance().mMyView.postInvalidate();
	}
	


	public void MoveFall(){
		
		synchronized (mHolder) {
			if(mFall1.size() < 100) mFall1.add(new fall(2));
			if(mFall1.size() < 50) mFall1.add(new fall(1));
		}
		
		int n = rnd.nextInt(300);
		
		for(fall tmp : mFall1) tmp.MoveSnow(n);
		for(fall tmp : mFall2) tmp.MoveSnow(n);
		AppManager.getInstance().mMyView.postInvalidate();
	}
	


	public void MoveSpring(){
		
		synchronized (mHolder) {
			if(mSpring1.size() < 100) mSpring1.add(new spring(2));
			if(mSpring1.size() < 50) mSpring1.add(new spring(1));
		}
		
		int n = rnd.nextInt(300);
		
		for(spring tmp : mSpring1) tmp.MoveSnow(n);
		for(spring tmp : mSpring2) tmp.MoveSnow(n);
		AppManager.getInstance().mMyView.postInvalidate();
	}
	
	public void DrawAll(Canvas canvas){
		if(canvas != null){
		canvas.drawBitmap(MyView.imgBack, 0, 0, mPaint);
		
		
		if(backGround == 4){
			for(snow tmp : mSnow1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		
		if(backGround == 4){
			for(snow tmp : mSnow2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		
		

		if(backGround == 3){
			for(fall tmp : mFall1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		
		if(backGround == 3){
			for(fall tmp : mFall2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		

		if(backGround == 1){
			for(spring tmp : mSpring1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		
		if(backGround == 1){
			for(spring tmp : mSpring2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		

		if(backGround == 2){
			for(summer tmp : mSummer1)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		
		if(backGround == 2){
			for(summer tmp : mSummer2)
				canvas.drawBitmap(tmp.imgSnow, tmp.x, tmp.y - tmp.rad, null);
		}
		
		}
		
	}
	

	public void ThreadStop(){
			ThreadStart = false;
		
	}
	
	public void run(){
		Canvas canvas = null;
		
		while(ThreadStart){
			
			canvas = mHolder.lockCanvas();
			
			try{
				
				synchronized (mHolder) {
					
					if(backGround == 1)MoveSpring();
					if(backGround == 2)MoveSummer();
					if(backGround == 3)MoveFall();
					if(backGround == 4)MoveSnow();
					
					
					
					DrawAll(canvas);
					
				}
			}finally {
				if(canvas != null)
					mHolder.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	

}