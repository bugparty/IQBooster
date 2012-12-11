package com.ifancc.GameViews;
import android.graphics.Canvas;
import android.view.SurfaceHolder;


/**
 *  驱动每一个游戏surfaceView的线程
 * @author liyang
 *
 */
public class GameThread extends Thread{
	private int sleepSpan = 1;
	private boolean flag = true;
	private GameView gameView = null;
	SurfaceHolder holder = null;
	
	public GameThread(GameView gameView,SurfaceHolder holder) {
		// TODO Auto-generated constructor stub
		this.gameView = gameView;
		this.holder = holder;
		
	}
	
	public int getSleepSpan() {
		
		return sleepSpan;
	}

	public void setSleepSpan(int sleepSpan) {
		this.sleepSpan = sleepSpan;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		Canvas canvas = null;
		while(flag){
			canvas = holder.lockCanvas();
			synchronized (this.holder) {
		    	try{
		    		gameView.onDraw(canvas);
		    	}
		    	catch(Exception e){}finally {
				    if (canvas!= null) {
				        holder.unlockCanvasAndPost(canvas);
				    }
				}
		    	try{
					Thread.sleep(sleepSpan);
				}
				catch(Exception e){
					e.printStackTrace();
				}
		    }
		}
	}
	
	
	public void setFlag(boolean bool){
		this.flag = bool;
	}
}
