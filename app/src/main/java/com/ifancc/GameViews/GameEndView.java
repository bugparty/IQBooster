package com.ifancc.GameViews;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.ifancc.Constants.Constant;
import com.ifancc.Utils.VirtualButton;
import com.ifancc.braingame.GameEngine;
import com.ifancc.braingame.GamesGridView;
import com.ifancc.braingame.R;


public class GameEndView extends View{
	private GameEngine gameEngine;
	private VirtualButton returnBtn;
	
	private Bitmap returnBtnUp;
	private Bitmap returnBtnDown;
	private Bitmap gameEnd;
	public GameEndView(GameEngine gameEngine) {
		// TODO Auto-generated constructor stub
		super(gameEngine);
		this.gameEngine = gameEngine;
		returnBtnUp = ((BitmapDrawable)getResources().getDrawable(R.drawable.return_btn_up)).getBitmap(); 
		returnBtnDown = ((BitmapDrawable)getResources().getDrawable(R.drawable.return_btn_down)).getBitmap(); 
		returnBtn = new VirtualButton(returnBtnDown,returnBtnUp,Constant.SCREENWIDTH - 150,Constant.SCREENHEIGHT - 100);
		gameEnd = ((BitmapDrawable)getResources().getDrawable(R.drawable.game_end)).getBitmap(); 
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(gameEnd,null,new RectF(0,0,Constant.SCREENWIDTH,Constant.SCREENHEIGHT),null);
		canvas.drawText("fd", 200,200, new Paint());
		returnBtn.drawSelf(canvas, new Paint());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(returnBtn.isActionOnButton(event.getX(), event.getY())){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				returnBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				returnBtn.releaseUp();
				if(Constant.engineStatus == 1){
					gameEngine.passMsg(4);
				}else if(Constant.engineStatus == 2){
					Intent intent = new Intent(gameEngine,GamesGridView.class);
					gameEngine.startActivity(intent);
				}
				break;
			}
		}
		return super.onTouchEvent(event);
	}
}
