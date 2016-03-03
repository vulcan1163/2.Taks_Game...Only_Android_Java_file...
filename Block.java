package com.example.taksintroduction;

import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Block {
	
	int x, y, width, height;
	int sy;
	boolean dead = false;
	
	float typeA,typeB,typeC,typeD;
	
	Context mContext;
	Resources res;
	Bitmap block;
	GameActivity mGameActivity;
	Rect m_boundBox = new Rect();
	
	int Level;
	public Block(){
		
	}
	public Block(int kind){
		
		
		//mGameActivity = new GameActivity();
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
		
		
		Random rnd = new Random();
		int k = rnd.nextInt(6);
		block = createImageBlock(R.drawable.block0+k);
		Level = AppManager.getInstance().getResultActivity().Level;
		
		switch(Level){
		case 0:
			sy = 10;
			break;
			
		case 1:
			sy = 30;
			break;
			
		case 2:
			sy = 50;
			break;
			
			
		}

	}
	
	
	
	public void moveBlock(){
		
		y += sy;
		
		if(y > height) {
			
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
		
		AppManager.getInstance().m_gameview.postInvalidate();
		
		m_boundBox.set(x,y, x+block.getWidth(), y+block.getHeight());
	}
	
	public GameActivity check(){
		
		if(mGameActivity == null){
			mGameActivity = new GameActivity();
		}
		
		return mGameActivity;
	}
	
	private Bitmap createImageBlock(int r){
		
			Bitmap imgPrevConv = BitmapFactory.decodeResource(MyView.mContext.getResources(), r);
			
			int w=1,h=1;
			
			w = width / 4;
			h = height / 4;

			return Bitmap.createScaledBitmap(imgPrevConv, w, h, true);
			
		}
	
	
	
}

class SmallBall {
	public int x, y, rad;			// ��ǥ, ������
	public  boolean dead = false; 	// �Ͷ߸� ����
	public  Bitmap imgBall;			// ��Ʈ�� �̹���
	
	private int width, height;		// View�� ũ��
	private int cx, cy;				// ���� �߽���
	private int cr;					// ���� ������
	private double r;				// �̵� ���� (radian)
	private int speed;				// �̵� �ӵ�
	private int num;				// �̹��� ��ȣ
	private int life;				// ���� �ֱ�
	float typeA,typeB,typeC,typeD;
	
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public SmallBall(Context context, int kind, int ang, int _width, int _height) {
		
		width = _width;
		height = _height;
		r = ang * Math.PI / 180;		// ���� radian

		typeA = (float)(width * (1/4f));
		typeB = (float)(width * (2/4f));
		typeC = (float)(width * (3/4f));
		typeD = (float)(width * (4/4f));
		
	cx = kind;
		
/*
		if(kind == 1) {
			cx = (int)typeA - (int)typeA/2;
			
		}
		if(kind == 2){
			cx = (int)typeB - (int)typeA/2;
		
		}
		
		if(kind == 3){
			cx = (int)typeC - (int)typeA/2;
		
		}
		if(kind == 4){
			cx = (int)typeD - (int)typeA/2;
		
		}
		
*/
		cy = (int) ((float)(height * (3/4f)));
		
		Random rnd = new Random();
		speed = rnd.nextInt(5) + 2;		// �ӵ�     : 2~6
		rad = rnd.nextInt(50) + 5;		// ������   : 5~14
		num = rnd.nextInt(6);			// �̹���  : 0~5
		life = rnd.nextInt(31) + 20;	// 20~50
		
		imgBall = BitmapFactory.decodeResource(context.getResources(), R.drawable.b0 + num);
		imgBall = Bitmap.createScaledBitmap(imgBall, rad * 2, rad * 2, false);
		cr = 10;						// ���� �ʱ� ������
		MoveBall();
	}

	//-------------------------------------
	//  MoveBall
	//-------------------------------------
	public void MoveBall() {
		life--;
		cr += speed;
		x = (int) (cx + Math.cos(r) * cr); 
		y = (int) (cy - Math.sin(r) * cr); 
		if (x < -rad || x > width + rad ||
				y < -rad || y > height + rad || life <= 0)
			dead = true;
	}
}
