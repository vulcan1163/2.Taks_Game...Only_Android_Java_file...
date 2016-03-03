package com.example.taksintroduction;

import android.graphics.Rect;

public class CollisionManager {
	
public static boolean CheckBoxToBox(Rect _rt1, Rect _rt2){
	
		
		if(_rt1.top < _rt2.bottom && _rt1.bottom > _rt2.top && _rt1.right > _rt2.left && _rt1.left < _rt2.right)
			return true;
		
		return false;
	}


	public static boolean CheckThePlayer(Rect _rt1, Rect _rt2, Rect _rt3, Rect _rt4){
		
		if(_rt1.top < _rt4.bottom && _rt1.bottom > _rt4.top && _rt1.right > _rt4.left && _rt1.left < _rt4.right)
			return true;
			if(_rt2.top < _rt4.bottom && _rt2.bottom > _rt4.top && _rt2.right > _rt4.left && _rt2.left < _rt4.right)
				return true;
				if(_rt3.top < _rt4.bottom && _rt3.bottom > _rt4.top && _rt3.right > _rt4.left && _rt3.left < _rt4.right)
					return true;
		
				
		
		return false;
	}

}
