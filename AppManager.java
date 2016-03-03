package  com.example.taksintroduction;

import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class AppManager {
	
	
	public Resources m_resources;
	
	public MyActivity mMyActivity;
	public MyThread mMyThread;
	public MyView mMyView;
	
	public ResultActivity mResultActivity;
	public ResultThread mResultThread;
	public ResultView mResultView;
	
	public GameActivity mGameActivity;
	public GameThread mGameThread;
	public GameView m_gameview;

	
	public Bitmap getBitmap(int r){
		return BitmapFactory.decodeResource(m_resources, r);
	}
	
	
	public void setMyThread(MyThread tmp){
		mMyThread = tmp;
	}
	public void setMyView(MyView tmp){
		mMyView = tmp;
	}

	public void setMyActivity(MyActivity tmp){
		mMyActivity = tmp;
	}
	public void setGameView(GameView gameview){
		m_gameview = gameview;
		
	}

	public void setGameActivity(GameActivity tmp){
		mGameActivity = tmp;
	}
	
	public void setGameThread(GameThread tmp){
		mGameThread = tmp;
	}
	
	public void setResultView(ResultView gameview){
		mResultView = gameview;
		
	}

	public void setResultThread(ResultThread tmp){
		mResultThread = tmp;
	}
	
	public void setResultActivity(ResultActivity tmp){
		mResultActivity = tmp;
	}
	
	
	public void setResources(Resources resources){
		m_resources = resources;
	}
	
	public GameView getGameView(){
		return m_gameview;
	}
	
	public GameActivity getGameActivity()	{
		return mGameActivity;
	}
	
	public GameThread getGameThread(){
		return mGameThread;
	}
	
	
	public Resources getResources(){
		return m_resources;
	}
	
	public MyThread getMyThread(){
		return mMyThread;
	}
	
	public MyActivity getMyActivity(){
		return mMyActivity;
	}
	
	public MyView getMyView(){
		return mMyView;
	}
	
	public ResultActivity getResultActivity(){
		return mResultActivity;
	}
	public ResultView getResultView(){
		return mResultView;
	}
	public ResultThread getResultThread(){
		return mResultThread;
	}
	
	
	private static AppManager m_instance;
	
	public static AppManager getInstance(){
		
		if(m_instance == null){
			m_instance = new AppManager();
		}
		
		return m_instance;
	}

}
