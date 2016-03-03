package com.example.taksintroduction;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TouchEffect extends SpriteAnimation{
	
	int width, height;
	boolean dead = false;

	public TouchEffect(int x, int y) {
		super(AppManager.getInstance().getBitmap(R.drawable.ex1));

		width = MyView.mScreenWidth;
		height = MyView.mScreenHeight;
	
		m_x = x;
		m_y = y;
		
		m_bitmap = createImageBlock(R.drawable.ex1);
	

		m_x = x-m_bitmap.getWidth() /8;
		m_y = y- m_bitmap.getHeight() /2;
	
		
		this.InitSpriteDate(m_bitmap.getHeight(), m_bitmap.getWidth()/4, 15, 4);
		
		mbReply = false;
	}
	
	public void Update(long Gametime){
		super.Update(Gametime);
		
		if(this.getAnimationEnd()){
			dead = true;
		}
		
	}
	

	private Bitmap createImageBlock(int r){
		
			Bitmap imgPrevConv = BitmapFactory.decodeResource(MyView.mContext.getResources(), r);
			
			int w=1,h=1;
			
			w = width/8;
			h = height/8;

			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
			
		}
	

}
