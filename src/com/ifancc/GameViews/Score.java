package com.ifancc.GameViews;

import com.ifancc.Constants.Constant;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class Score{
	private int scoreY;
	private int scoreX;
	private int score = 0;
	private boolean isShow = false;
	
	private Bitmap correct;
	private Bitmap wrong;
	
	public Score(int scoreX,int scoreY){
		this.scoreX = scoreX;
		this.scoreY = scoreY;
	}
	public void setScore(int score){
		this.score = score;
	}
	public void setScoreY(){
		scoreY -= 20;
	}
	public boolean isShow(){
		return isShow;
	}
	public void setShow(boolean isShow){
		this.isShow = isShow;
	}
	public void drawScore(Canvas canvas){
		if(isShow && score > 0){
			Paint paint = new Paint();
			paint.setTextSize(80);
			paint.setTypeface(Constant.t3);
			paint.setColor(Color.rgb(128, 11, 28));
			canvas.drawText("+"+score + "",scoreX - 40,scoreY + 80,paint);
			canvas.drawBitmap(Constant.correct, Constant.SCREENWIDTH/2 - 40, Constant.SCREENHEIGHT/2 + 80, null);
		}else if(isShow && score == 0){
			canvas.drawBitmap(Constant.wrong,Constant.SCREENWIDTH/2 - 40, Constant.SCREENHEIGHT/2 + 80,null);
		}
	}
	public void setLoc(int scoreX,int scoreY){
		this.scoreX = scoreX;
		this.scoreY = scoreY;
	}
	public int getScoreY() {
		// TODO Auto-generated method stub
		return scoreY;
	}
}
