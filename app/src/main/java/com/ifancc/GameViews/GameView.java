package com.ifancc.GameViews;
import com.ifancc.Constants.Constant;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 所有游戏surfaceView的父类
 * @author liyang
 *
 */
public class GameView  extends SurfaceView implements SurfaceHolder.Callback{
	public Score scoreA = new Score(Constant.SCREENWIDTH/2, Constant.SCREENHEIGHT/2);
	
	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public void drawScoreA(Canvas canvas){
		scoreA.drawScore(canvas);
	}

	public void setScoreShow(boolean b,int score){
		scoreA.setShow(b);
		scoreA.setScore(score);
	}
	public void setScoreLoc(){
		scoreA.setLoc(Constant.SCREENWIDTH/2, Constant.SCREENHEIGHT/2);
	}
	public void setScoreY(){
		scoreA.setScoreY();
	}
	public int getScoreY(){
		return  scoreA.getScoreY();
	}
	public boolean isScoreShow(){
		return scoreA.isShow();
	}
	public void setSocreShow(boolean b){
		scoreA.setShow(b);
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
