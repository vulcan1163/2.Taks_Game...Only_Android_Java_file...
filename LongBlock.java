package com.example.taksintroduction;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LongBlock extends SpriteAnimation{
	int x, y, width, height;
	int sy;
	boolean dead = false;
	
	float typeA,typeB,typeC,typeD;
	
	Context mContext;
	Resources res;
	Bitmap block;
	
	public LongBlock(int kind){
		super(AppManager.getInstance().getBitmap(R.drawable.bublemotion));
		
		
		width = MyView.mScreenWidth;
		height = MyView.mScreenHeight;
	
		typeA = (float)(width * (1/4f));
		typeB = (float)(width * (2/4f));
		typeC = (float)(width * (3/4f));
		typeD = (float)(width * (4/4f));
		
		
		if(kind == 1) x = (int)typeA - (int)typeA;
		if(kind == 2) x = (int)typeB - (int)typeA;
		if(kind == 3) x = (int)typeC - (int)typeA;
		if(kind == 4) x = (int)typeD - (int)typeA;

		y=-height;
		sy = 10;
		
		m_x = x;
		m_y = y;
		
		m_bitmap = createImageLongBlock(R.drawable.bublemotion);
		
		this.InitSpriteDate(m_bitmap.getHeight(), m_bitmap.getWidth()/4, 30, 4);
		mbReply = true;
	}
	
	
	
	public void moveBlock(){
		m_y += sy;
		
		if(m_y > height) {
			dead = true;
			
			GameThread.miss--;
			if(GameThread.miss <= 0){
				
				//AppManager.getInstance().getGameThread().RemoveAll();
				//AppManager.getInstance().getGameActivity().clear();
				
				AppManager.getInstance().getGameActivity().killMediaPlayer();
				GameThread.status = GameThread.GAME_OVER;
				AppManager.getInstance().getGameActivity().intentCheck = true;
				
				String name = AppManager.getInstance().getMyActivity().id.getText().toString();
				AppManager.getInstance().getResultActivity().dbManager.insert("insert into Score_LIST values(null, '" +name  + "', " + ResultActivity.mScore + ");");
				
				AppManager.getInstance().getGameThread().sound.play(AppManager.getInstance().getGameThread().sound_id_gameover, 1, 1, 0, 0, 1);
			}
			
		
		}
	}
	
	
	
	private Bitmap createImageLongBlock(int r){
		
			Bitmap imgPrevConv = BitmapFactory.decodeResource(MyView.mContext.getResources(), r);
			
			int w=1,h=1;
			
			w = width;
			h = height / 4;

			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
			
		}
	
	

}
