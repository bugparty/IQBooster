package com.ifancc.GameViews;

import com.ifancc.Constants.Constant;
import com.ifancc.Constants.SoundPlayer;
import com.ifancc.Constants.UserScores;
import com.ifancc.Utils.VirtualButton;
import com.ifancc.braingame.GameEngine;
import com.ifancc.braingame.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class MemoryMatchView extends GameView {
	private static final String TAG = "MemoryMatch";
	private int offset;
	
	private SurfaceHolder holder = null;
	
	public MemoryMatchControl gameControl;
	
	private static int screenHeight;	
	private static int screenWidth;
	private  int whiteWidth;
	private  int whiteHeight;
		
	private int timeMention;
	private Bitmap white;
	private int gameImages[];
	
	
	private boolean isShow;
	private boolean isTimeup;
	
	private  Canvas canvas;
	
	
	private VirtualButton matchBtn;
	private VirtualButton notMatchBtn;
	
	private Bitmap bkground;
	private Bitmap matchDownBtn;
	private Bitmap matchUpBtn;
	private Bitmap notMatchDownBtn;
	private Bitmap notMatchUpBtn;
	private Bitmap star;
	private Bitmap clock;
	private Bitmap titleBar;
	
	private RectF titleBarRect;
	private RectF starRect;
	private RectF clockRect;
	
	private Paint numPaint = new Paint();
	
	
	private static  int notMatchBtnLocLeft ;
	private static  int matchBtnLocLeft ;
	private static  int btnTop ;
	
	private GameEngine gameEngine;
	
	private boolean isClockPlay = false;
	
	private GameThread gameThread;
	private ScoreAnimationThread sAnimationThread;
	public MemoryMatchView(GameEngine gameEngine) {
		// TODO Auto-generated constructor stub
		super(gameEngine);
		this.gameEngine = gameEngine;
		init();
	}
		
	private void init(){
		timeMention = 40;
		white = ((BitmapDrawable)getResources().getDrawable(R.drawable.white)).getBitmap();
			
		gameImages = new int[]{R.drawable.image_one,R.drawable.image_two,R.drawable.image_three };
		whiteWidth = white.getWidth();
		whiteHeight = white.getHeight();
		
		gameControl = new MemoryMatchControl();
		gameControl.start();
		
		offset = whiteWidth;
		isShow =false;
		isTimeup = false;
		
		numPaint.setColor(Color.rgb(26, 95, 101));
		numPaint.setTextSize(30);
		
		holder = getHolder();
		holder.addCallback(this);
		gameThread = new GameThread(this, holder);
		
	}
		
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 * 更新timeMention的线程
	 */
	
	
	private Thread timeThread = new Thread(){
		public void run() {
			while(timeMention > 0){
				try{
					Thread.currentThread().sleep(1000);
				}catch(InterruptedException e){
					Thread.currentThread().interrupt();
				}
				timeMention--;
				if((timeMention == 10) && (!isClockPlay)){
					SoundPlayer.playSound(R.raw.clock_sound);
					isClockPlay = true;
				}
			}
			if(timeMention == 0){
				isTimeup = true;
				Thread.currentThread().interrupt();
				UserScores.memoryScore = gameControl.getScore();
				gameEngine.passMsg(0);
			}
		}
	};
	
		@Override
		protected void onDraw(Canvas canvas) {
			// TODO Auto-generated method stub
			this.canvas = canvas;
			super.onDraw(canvas);
			drawBack();
			if(!isTimeup){
				if(!gameControl.isReady()){
					drawBegin();
				}
				if(isShow){
					if(offset >=  0){
						offset -=40;
					}else{
						isShow = false;
						offset = whiteWidth;
					}
					drawAnimation();
				}else{
					drawImage();
				}
			}else{
				drawImage();
			}
			drawScoreA(canvas);
		}
		
		
		private void drawBack(){
			canvas.drawBitmap(bkground, null, new RectF(0,0,screenWidth,screenHeight), new Paint());
			drawWhite();
			drawTitle();
			drawButton();
			drawTimeMention();
			drawScore();
		};
		private void drawTitle(){
			Paint textPaint = new Paint();
			textPaint.setTypeface(Constant.t2);
			textPaint.setTextSize(27);
			textPaint.setColor(Color.BLACK);
			
			canvas.drawBitmap(star, null, starRect, null);
			canvas.drawBitmap(clock, null, clockRect, null);
			
			canvas.drawText("Time:",screenWidth/10f + clock.getWidth()*5/2.5f,titleBarRect.top + titleBar.getHeight()*2,textPaint);
	        canvas.drawText("Score:",screenWidth/2f + titleBar.getWidth()/2.8f + star.getWidth()*2 ,titleBarRect.top + titleBar.getHeight()*2,textPaint);
	       
		}
		
		private Bitmap getImage(int resource){
			return ((BitmapDrawable)getResources().getDrawable(resource)).getBitmap();
		}
		
		/*
		 * 
		 */
		private void drawBegin(){
			Bitmap image = null;
			Paint paint = new Paint();
			image= getImage(gameImages[gameControl.getLeftImage()]);
			canvas.drawBitmap(image,screenWidth/2 - whiteWidth - image.getWidth()/2,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			image= getImage(gameImages[gameControl.getMiddleImage()]);
			canvas.drawBitmap(image,screenWidth/2 - image.getWidth()/2,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			image= getImage(gameImages[gameControl.getRightImage()]);
			canvas.drawBitmap(image,screenWidth/2 + whiteWidth - image.getWidth()/2,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			
		}
		
		private void drawImage(){
			Bitmap image = null;
			Paint paint = new Paint();
			image= getImage(gameImages[gameControl.getLeftImage()]);
			setImageAlpha(paint,gameControl.isLefIsVisible());
			canvas.drawBitmap(image,screenWidth/2 - whiteWidth - image.getWidth()/2,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			image= getImage(gameImages[gameControl.getMiddleImage()]);
			setImageAlpha(paint,gameControl.isMidIsVisible());
			canvas.drawBitmap(image,screenWidth/2 - image.getWidth()/2,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			image= getImage(gameImages[gameControl.getRightImage()]);
			paint.setAlpha(0xFF);
			canvas.drawBitmap(image,screenWidth/2 + whiteWidth - image.getWidth()/2,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
		}
		
		
		
		
		private void drawWhite(){
			Paint paint = new Paint();
			canvas.drawBitmap(white,screenWidth/2 - whiteWidth/2 - whiteWidth,screenHeight/5,paint);
			canvas.drawBitmap(white,screenWidth/2 + whiteWidth/2,screenHeight/5,paint);
		}
		
		private void drawButton(){
			Paint paint = new Paint();
			matchBtn.drawSelf(canvas, paint);
			notMatchBtn.drawSelf(canvas, paint);
		}
		
		private void drawTimeMention(){
			numPaint.setColor(Color.rgb(26, 95, 101));
			numPaint.setTextSize(30);
			canvas.drawText(timeMention + "", 
			        screenWidth/10f + clock.getWidth()*5/2.2f + titleBar.getWidth()/4f,
	                titleBarRect.top + titleBar.getHeight()*2,
	                numPaint);
		}
		private void drawScore(){
			numPaint.setColor(Color.rgb(26, 95, 101));
			numPaint.setTextSize(30);
			canvas.drawText(gameControl.getScore()+"",screenWidth/2f + titleBar.getWidth()/2.8f + star.getWidth()*2 + titleBar.getWidth()/3f ,
			        titleBarRect.top + titleBar.getHeight()*2,
			        numPaint);
		}
		
		private void drawAnimation(){
			Bitmap image = null;
			Paint paint = new Paint();
			image = getImage(gameImages[gameControl.getGoImage()]); //取得图像
			setImageAlpha(paint,gameControl.isGoIsVisible());  //设置图像的透明度
			canvas.drawBitmap(image,(screenWidth/2 - whiteWidth - image.getWidth()/2 - whiteWidth) + offset,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			
			
			image= getImage(gameImages[gameControl.getLeftImage()]);
			setImageAlpha(paint,gameControl.isLefIsVisible());
			canvas.drawBitmap(image,(screenWidth/2 - whiteWidth - image.getWidth()/2) + offset,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			image= getImage(gameImages[gameControl.getMiddleImage()]);
			setImageAlpha(paint,gameControl.isMidIsVisible());
			canvas.drawBitmap(image,(screenWidth/2 - image.getWidth()/2) + offset,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
			image= getImage(gameImages[gameControl.getRightImage()]);
			paint.setAlpha(0xFF);
			canvas.drawBitmap(image,(screenWidth/2 + whiteWidth - image.getWidth()/2) + offset,
					screenHeight/5 + whiteHeight/2 - image.getHeight()/2,paint);
		}
		
		private void setImageAlpha(Paint paint,boolean bool){
			if(bool){
				paint.setAlpha(0x80);//半透明
			}else{
				paint.setAlpha(0x00);//透明
			}
		}
		
		
		public void doNext(){
			if(!gameControl.isReady()){
				timeThread.start();
			}
			if(!isTimeup){
				gameControl.next();
				isShow = true;
			}
		}
		
		
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			float x = event.getX();
			float y = event.getY();
			if(matchBtn.isActionOnButton(x, y)){
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					matchBtn.pressDown();
					return true;
				case MotionEvent.ACTION_UP:
					matchBtn.releaseUp();
					if(!isTimeup()){
		    			if(gameControl.isMatch()){
		        			gameControl.setCorrect(true);
		        			doNext();
		        			setScoreShow(true,gameControl.getScoreAdd());
		        			SoundPlayer.playSound(R.raw.right_sound);
		        		}else{
		        			gameControl.setCorrect(false);
		        			doNext();
		        			setScoreShow(true,0);
		        			SoundPlayer.playSound(R.raw.wrong_sound);
		        		}
		    		}
					break;
				}
			}else if(notMatchBtn.isActionOnButton(x,y)){
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN:
					notMatchBtn.pressDown();
					return true;
				case MotionEvent.ACTION_UP:
					notMatchBtn.releaseUp();
					if(!isTimeup()){
		    			if(gameControl.isMatch()){
		        			gameControl.setCorrect(false);
		        			doNext();
		        			setScoreShow(true,0);
		        			SoundPlayer.playSound(R.raw.wrong_sound);
		        		}else{
		        			gameControl.setCorrect(true);
		        			doNext();
		        			setScoreShow(true,gameControl.getScoreAdd());
		        			SoundPlayer.playSound(R.raw.right_sound);
		        		}
		    		}
					break;
				}
			}
			
			return super.onTouchEvent(event);
		}
		
		
		public boolean isTimeup(){
			return isTimeup;
		}
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			screenHeight = getHeight();
			screenWidth = getWidth();
			
			matchDownBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.memory_match_match_down_btn)).getBitmap();
			matchUpBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.memory_match_match_up_btn)).getBitmap();
			notMatchDownBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.memory_match_not_match_down_btn)).getBitmap();
			notMatchUpBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.memory_match_not_match_up_btn)).getBitmap();
			bkground = ((BitmapDrawable)getResources().getDrawable(R.drawable.memory_match_bkground)).getBitmap();
			star = ((BitmapDrawable)getResources().getDrawable(R.drawable.title_star1)).getBitmap();
			clock = ((BitmapDrawable)getResources().getDrawable(R.drawable.title_clock1)).getBitmap();
			titleBar = ((BitmapDrawable)getResources().getDrawable(R.drawable.title_bar)).getBitmap(); 
			

			titleBarRect = new RectF((screenWidth-titleBar.getWidth())/3f,0,(screenWidth-titleBar.getWidth())*2/3F+titleBar.getWidth(),
				      screenHeight/12.5f);
            starRect = new RectF(screenWidth/2f +titleBar.getWidth()/2.8f,titleBarRect.top+  titleBar.getHeight()*5/4f,
	                  screenWidth/2f +  titleBar.getWidth()/2.8f + star.getWidth(),titleBarRect.top+ titleBar.getHeight()*5/4f + star.getHeight());
            clockRect = new RectF(screenWidth/8f,titleBarRect.top+titleBar.getHeight()*7/7f,screenWidth/8f + clock.getWidth(),
                     titleBarRect.top+ titleBar.getHeight()*7/7f +clock.getHeight());
	       
			
			notMatchBtnLocLeft = 30;
			matchBtnLocLeft = screenWidth - 180;
			btnTop = screenHeight/2 + 100;
			matchBtn = new VirtualButton(matchDownBtn, matchUpBtn, matchBtnLocLeft, btnTop);
			notMatchBtn = new VirtualButton(notMatchDownBtn, notMatchUpBtn, notMatchBtnLocLeft, btnTop);
			
			gameThread.start();
			sAnimationThread = new ScoreAnimationThread(this);
			sAnimationThread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			gameThread.setFlag(false);
			sAnimationThread.setFlag(false);
			SoundPlayer.stopSound();
		}
		

}
