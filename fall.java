package com.example.taksintroduction;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;



class fall {
	public int x, y, rad;			// ��ǥ
	public  Bitmap imgSnow;			// ��Ʈ�� �̹���
	
	private int width, height;
	private int sx, sy; 
	private int range, dx, speed;
	private Random rnd;
	
	//-------------------------------------
	//  ������
	//-------------------------------------
	public fall(int kind) {
		width = MyView.mScreenWidth;
		height = MyView.mScreenHeight;
		
		rnd = new Random();
		rad = rnd.nextInt(40) + 2; 				// 2~7 ������
		x = rnd.nextInt(width);					// View ��ü
		y = rnd.nextInt(height);
		sy = rnd.nextInt(4) + 2; 				// 2~5 �ӵ�
		sx = rnd.nextInt(3) + 1;				// 1~3
		range = rnd.nextInt(31) + 20;			// 20~50 �¿� �̵� 
		
		int k;									// 0~3 ��  ����
		if (kind == 1)
			k = rnd.nextInt(2);					// ����
		else 
			k = rnd.nextInt(4);					// ���
		
		
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
		case 0 :				// �ٶ�����
			speed = sx;
			break;
		case 1 :				// �ٶ� ��
			speed = Math.abs(sx) * 2;	
			break;
		case 2 :				// �ٶ� ��
			speed = -Math.abs(sx) * 2;
			break;
		default :				// ���� �ٶ� ����
			if (speed == 0) speed = sx;
		}

		x += speed;
		y += sy;
		if (x < -rad) x = width + rad; 		// ������ ����� �������� ����
		if (x > width + rad) x = -rad;		// ������ ����� �������� ����
		if (y > height + rad) y = -rad;		// �ٴ��� ����� õ������ ����

		
		if (speed == sx) {					// Dir == 0 || �ʱ� ����
			dx += sx;						// �¿�� �̵��� �Ÿ� ����
			if (Math.abs(dx) > range) {		// ������ ������ ����� ������ �ٲ�
				dx = 0;
				sx = -sx;
			}
		} // if
	} // move
}