package com.ifancc.GameViews;

import com.ifancc.Constants.UserInfos;
import com.ifancc.Utils.VirtualButton;
import com.ifancc.braingame.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.ifancc.braingame.GameEngine;

public class StartView extends View{

	private GameEngine gameEngine;
	private VirtualButton playButton;
	private VirtualButton howButton;
	private Bitmap matchDownBtn;
	private Bitmap matchUpBtn;
	private Canvas canvas;
	public StartView(GameEngine gameEngine) {
		super(gameEngine);
		this.gameEngine = gameEngine;
		matchDownBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.match_down_btn)).getBitmap();
		matchUpBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.match_up_btn)).getBitmap();
		playButton = new VirtualButton(matchDownBtn, matchUpBtn, 100, 200);
		howButton = new VirtualButton(matchDownBtn, matchUpBtn, 300, 200);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		Paint paint = new Paint();
		this.canvas = canvas;
		super.onDraw(canvas);
		canvas.drawText(UserInfos.currentGame + "end!", 200, 200, paint);
		playButton.drawSelf(canvas, paint);
		howButton.drawSelf(canvas, paint);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(event.getAction() != MotionEvent.ACTION_DOWN){
			postInvalidate();
			return super.onTouchEvent(event);
		}
		if(playButton.isActionOnButton(event.getX(), event.getY())){
			gameEngine.passMsg(1);
		}else if(howButton.isActionOnButton(event.getX(), event.getY())){
			gameEngine.passMsg(4);
		}
		return true;
	}
}
