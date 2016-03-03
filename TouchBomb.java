package com.example.taksintroduction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TouchBomb extends SpriteAnimation{

	int width, height;

	boolean dead = false;
	
	float typeA,typeB,typeC,typeD;
	
	public TouchBomb(int kind) {
		super(AppManager.getInstance().getBitmap(R.drawable.bublemotion));


		width = MyView.mScreenWidth;
		height = MyView.mScreenHeight;

		typeA = (float)(width * (1/4f));
		typeB = (float)(width * (2/4f));
		typeC = (float)(width * (3/4f));
		typeD = (float)(width * (4/4f));
		
		

		if(kind == 1) {
			m_x = (int)typeA - (int)typeA;
			
		}
		if(kind == 2){
			m_x = (int)typeB - (int)typeA;
		
		}
		
		if(kind == 3){
			m_x = (int)typeC - (int)typeA;
		
		}
		if(kind == 4){
			m_x = (int)typeD - (int)typeA;
		
		}
		
		m_y = (int) ((float)(height * (3/4f)));
		
		m_bitmap = createImageBlock(R.drawable.bublemotion);
		
		this.InitSpriteDate(m_bitmap.getHeight(), m_bitmap.getWidth()/8, 20, 8);
		
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
			
			w = width;
			h = height/4;

			return Bitmap.createScaledBitmap(imgPrevConv, width*2, height/4, true);
			
		}
	
}
