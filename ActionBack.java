package com.example.taksintroduction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ActionBack {
	
	
	int width, height;
	int x, y;
	
	long lastGen;
	
	float typeA,typeB,typeC,typeD;
	
	boolean dead = false;
	Bitmap back;
	
	
	public ActionBack(int kind){

		
		width = MyView.mScreenWidth;
		height = MyView.mScreenHeight;
		
		

		typeA = (float)(width * (1/4f));
		typeB = (float)(width * (2/4f));
		typeC = (float)(width * (3/4f));
		typeD = (float)(width * (4/4f));
		
		
		if(kind == 1) {
			x = (int)typeA - (int)typeA;
			back = createImageBlock(R.drawable.stick0);
		}
		
		
		if(kind == 2){
			x = (int)typeB - (int)typeA;
		back = createImageBlock(R.drawable.stick1);
		}
		
		if(kind == 3){
			x = (int)typeC - (int)typeA;
		back = createImageBlock(R.drawable.stick2);
		}
		if(kind == 4){
			x = (int)typeD - (int)typeA;
		back = createImageBlock(R.drawable.stick3);
		}
		
		y =0;
		
		
		
		
		
	}
	
	public void checkActionBack(long GameTime){
		
		
		if(System.currentTimeMillis() - GameTime >2200){
			
			dead = true;
			
			
		}
		
	}

	
	private Bitmap createImageBlock(int r){
		
			Bitmap imgPrevConv = BitmapFactory.decodeResource(MyView.mContext.getResources(), r);
			
			int w=1,h=1;
			
			w = width / 4;
			h = height;

			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
			
		}
}
