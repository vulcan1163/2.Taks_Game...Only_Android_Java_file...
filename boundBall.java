package com.example.taksintroduction;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class boundBall {

	Rect m_boundBox = new Rect();

	int x,m_x,m_y, width, height;
	int ball_width, ball_height;
	float y;
	int speed = 12;
	Bitmap ball;

	public boundBall(){
		width = AppManager.getInstance().getMyView().mScreenWidth;
		height = AppManager.getInstance().getMyView().mScreenHeight;
		
		
		x = m_x;
		y = (float) (height * (3 / 4f));
	
		ball = createImageBlock(R.drawable.ball);
	
	}

	public void move(){
		x -=(int)AppManager.getInstance().getGameActivity().ACCY*speed; 
		
		if (m_y < 0) {
			m_y = 0;
		} else if(x > width-ball.getWidth()){
			x = width-ball.getWidth();
		}else if(x < 0){
			x=0;
		}
		
		m_boundBox.set(x,(int)y, x+ball.getWidth(),(int)y+  ball.getHeight());
	}

	private Bitmap createImageBlock(int r){
		
			Bitmap imgPrevConv = BitmapFactory.decodeResource(MyView.mContext.getResources(), r);
			
			int w=1,h=1;
			
			w = width / 4;
			h = height / 4;

			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
			
		}
	
	
}
