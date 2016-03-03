package com.example.taksintroduction;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



class fall {
	public int x, y, rad;			// 좌표
	public  Bitmap imgSnow;			// 비트맵 이미지
	
	private int width, height;
	private int sx, sy; 
	private int range, dx, speed;
	private Random rnd;
	
	//-------------------------------------
	//  생성자
	//-------------------------------------
	public fall(int kind) {
		width = MyView.mScreenWidth;
		height = MyView.mScreenHeight;
		
		rnd = new Random();
		rad = rnd.nextInt(40) + 2; 				// 2~7 반지름
		x = rnd.nextInt(width);					// View 전체
		y = rnd.nextInt(height);
		sy = rnd.nextInt(4) + 2; 				// 2~5 속도
		sx = rnd.nextInt(3) + 1;				// 1~3
		range = rnd.nextInt(31) + 20;			// 20~50 좌우 이동 
		
		int k;									// 0~3 눈  종류
		if (kind == 1)
			k = rnd.nextInt(2);					// 전경
		else 
			k = rnd.nextInt(4);					// 배경
		
		
		imgSnow = BitmapFactory.decodeResource(MyView.mContext.getResources(), 
						R.drawable.fall_0 + k);
		imgSnow = Bitmap.createScaledBitmap(imgSnow, rad * 2 , rad * 2, true);
	}

	//-------------------------------------
	//  MoveSnow
	//-------------------------------------
	
	public void MoveSnow(int dir) {
		if (dir < 5) dir = 1;
		else if (dir < 10) dir = 2; 
		else if (dir > 200) dir = 0;
		
		switch (dir) {
		case 0 :				// 바람없음
			speed = sx;
			break;
		case 1 :				// 바람 →
			speed = Math.abs(sx) * 2;	
			break;
		case 2 :				// 바람 ←
			speed = -Math.abs(sx) * 2;
			break;
		default :				// 현재 바람 유지
			if (speed == 0) speed = sx;
		}

		x += speed;
		y += sy;
		if (x < -rad) x = width + rad; 		// 좌측을 벗어나면 우측에서 진입
		if (x > width + rad) x = -rad;		// 우측을 벗어나면 좌측에서 진입
		if (y > height + rad) y = -rad;		// 바닥을 벗어나면 천정에서 진입

		
		if (speed == sx) {					// Dir == 0 || 초기 상태
			dx += sx;						// 좌우로 이동한 거리 누적
			if (Math.abs(dx) > range) {		// 지정한 범위를 벗어나면 방향을 바꿈
				dx = 0;
				sx = -sx;
			}
		} // if
	} // move
}