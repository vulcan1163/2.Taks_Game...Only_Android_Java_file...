package com.example.taksintroduction;



import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class MyView extends SurfaceView implements android.view.SurfaceHolder.Callback {
		
	
		public static int mScreenWidth, mScreenHeight;
		
		public MyThread mThread;
		
		static Context mContext;
		
		static Bitmap imgBack;
		
		Paint mPaint = new Paint();
		
		public MyView (Context context, AttributeSet attrs){
			super(context, attrs);
			
			
			SurfaceHolder holder = getHolder();
			
			getHolder().addCallback(this);
			mContext = context;
			mThread = new MyThread(getHolder(), context);
			AppManager.getInstance().setMyView(this);
			initScreenSize();
			initImageData();
			
		}
		
		private void initScreenSize(){
			Display display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			Point point = new Point();
			display.getSize(point);
			
			mScreenWidth = point.x;
			mScreenHeight = point.y;
			
		}
		
		private Bitmap createImage(int r){
			
			Resources res = getResources();
			
			Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
			
			int w=0,h=0;
			

			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
		}
		
		private Bitmap createImageAllScreen(int r){
			
			Resources res = getResources();
			Bitmap imgPrevConv = BitmapFactory.decodeResource(res, r);
			int w = mScreenWidth;
			int h = mScreenHeight;
			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
		}
		
		private void initImageData(){
			
			imgBack = createImageAllScreen(R.drawable.spring1);
			
		}
		

		
		public void surfaceCreated(SurfaceHolder holder){
			mThread.start();
		}

		@Override
		public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder arg0) {
			// TODO Auto-generated method stub
			boolean done = true;
			
			
					mThread.ThreadStop();
			
			
			
		}
		

}
