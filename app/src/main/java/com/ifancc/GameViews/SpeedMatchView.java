package com.ifancc.GameViews;

import com.ifancc.Constants.SoundPlayer;
import com.ifancc.Constants.UserScores;
import com.ifancc.Utils.VirtualButton;
import com.ifancc.braingame.R;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.ifancc.braingame.GameEngine;

public class SpeedMatchView extends GameView{
	private static final String TAG = "SPEEDMATCHER2";
	
									
	private SurfaceHolder holder;  
	
	
	private Paint paint = new Paint();
	private Paint wordPaint = new Paint();
	
	private Canvas canvas;
	//获取屏幕的width的height
	private static int screenWidth;
	private static int screenHeight;
	//显示mentionTitle的时间
	private int timeMention = 40;
	//显示mentionTitle的分数
	private int score = 0;
	
	//屏幕中心的白色背景图片
	private Bitmap white;
	//不断变化判断是否match的图片套件
	private int gameImages[];
	//创建PhysicdUtils的对象
	private SpeedMatchPhysicsUtils p;
	//判断用户点击match和notMathch的时间，开始一次gameImage更新动画的时间
	private long timeMark;
	
	//判断用户是否点击了ready按钮
	private boolean isReady = false;
	//判断用户是否点击了match和notMatch按钮，即是否开始一次gameImage更新动画
	private boolean isShow = false;
	
	//下一个需要现实的gameImage
	private int nextNum = 0;
	//判断连续的两个gameImage是否match
	private boolean isMatch;

	private Random rand = new Random(System.currentTimeMillis());
	//当前显示的gameImage
	private int showNum = Math.abs(rand.nextInt())%3;
	//控制线程循环
	private boolean runFlag; 
	
	private Typeface typeface;
	
	private VirtualButton matchBtn;
	private VirtualButton notMatchBtn;
	
	private Bitmap star;
	private Bitmap clock;
	private Bitmap titleBar;
	private Bitmap matchDownBtn;
	private Bitmap matchUpBtn;
	private Bitmap notMatchDownBtn;
	private Bitmap notMatchUpBtn;
	
	private int width3;
	private int height3;
	private int width7;
	private int height7;
	private int width6;
	private int height6;
	private RectF titleBarRect;
	private RectF starRect;
	private RectF clockRect;
	private Paint numPaint = new Paint();

	
	private static  int notMatchBtnLocLeft ;
	private static  int matchBtnLocLeft ;
	private static  int btnTop ;
	
	private GameThread gameThread;
	private GameEngine gameEngine;
	private boolean isClockPlay = false;
	private ScoreAnimationThread sAnimationThread;
	
	public SpeedMatchView(GameEngine gameEngine){
		super(gameEngine);
		this.gameEngine = gameEngine;
		init(gameEngine);
	}
	/*
	 * 初始化成员变量的函数
	 */
	private void init(GameEngine gameEngine){
		
		holder = getHolder();
		holder.addCallback(this);
		runFlag = true;
		
		white  = ((BitmapDrawable)getResources().getDrawable(R.drawable.white)).getBitmap();
		gameImages = new int[]{R.drawable.image_one,R.drawable.image_two,R.drawable.image_three};
		
		typeface = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/PAPYRUS.TTF");
		initWordPaint();
		
		gameThread = new GameThread(this, holder);
	}
	private void initWordPaint(){
		wordPaint.setTypeface(typeface);
		wordPaint.setTextSize(27);
		wordPaint.setColor(Color.BLACK);
	}
	
	private Thread thread = new Thread(){
		public void run() {
				while(timeMention>=0){
					   try{
							 if(isReady){
									timeMention--;
									if((timeMention == 10) && (!isClockPlay)){
										SoundPlayer.playSound(R.raw.clock_sound);
										isClockPlay = true;
									}
								 Thread.sleep(1000); 
								}
							 }

						catch(InterruptedException e){
							       e.printStackTrace();
				             }
			       }
			}
			
	};

	long timeCircle = 0l;
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.canvas = canvas;
		if(timeMention > 0){
			canvas.drawColor(Color.GRAY);
			drawBack();
			drawScoreA(canvas);
			if(isReady){
				timeCircle = System.currentTimeMillis();
				if((timeMention == 10) && (!isClockPlay)){
					SoundPlayer.playSound(R.raw.clock_sound);
					isClockPlay = true;
				}
				drawData();
				if(isShow){
					animation(timeCircle);
				}
				else{
					drawShowImage();
				}
			}else{
//				canvas.drawText(timeMention + "", screenWidth/10f + width7*5/2.2f + width3/4f,titleBarRect.top + height3*2,
//		                numPaint);
//				canvas.drawText(score + "",screenWidth/12f + width7*5/2.2f + width3*4/2.8f +  width3/4.1f ,titleBarRect.top + height3*2,
//				        numPaint);
				drawShowImage();
			}
		}else{
		//处理时间消耗完的时候需要显示的内容
		canvas.drawColor(Color.GRAY);
		Log.d(TAG,"clearing!!");
		drawBack();
		drawShowImage();
		//向handler传值
		UserScores.speedScore = score;
		gameEngine.passMsg(0);
	}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if(matchBtn.isActionOnButton(x, y)){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				matchBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				isReady = !isReady?true:isReady;
				matchBtn.releaseUp();
				if(isMatch&&(timeMention > 0)){
					setScoreShow(true,25);
					updateScore();
					SoundPlayer.playSound(R.raw.right_sound);
				}else{
					setScoreShow(true, 0);
				}
				nextNum = Math.abs(rand.nextInt())%3;
				isShow = true;
				timeMark = System.currentTimeMillis();
				SoundPlayer.playSound(R.raw.wrong_sound);
				break;
			}
		}else if(notMatchBtn.isActionOnButton(x,y)){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				notMatchBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				isReady = !isReady?true:isReady;
				
				notMatchBtn.releaseUp();
				if(!isMatch&&(timeMention > 0)){
					setScoreShow(true,25);
					updateScore();
					SoundPlayer.playSound(R.raw.right_sound);
				}else{
					setScoreShow(true,0);
				}
				nextNum = Math.abs(rand.nextInt())%3;
				isShow = true;
				timeMark = System.currentTimeMillis();
				SoundPlayer.playSound(R.raw.wrong_sound);
				break;
			}
		}
		return super.onTouchEvent(event);
	}
	
	/*
	 * 画游戏中静态背景的函数
	 */
	private void drawBack(){
		initPaint();
		Bitmap back =((BitmapDrawable)getResources().getDrawable(R.drawable.speedmatch_bkground)).getBitmap();
		canvas.drawBitmap(back,null,new Rect(0,0,screenWidth,screenHeight),null);
		drawTitle();
		drawButton();
		drawWords();
		drawWhite();
	}
	
	
	private void drawWords(){
		//	canvas.drawText(mentionWords, mentionWordsLeft, mentionWordsTop, wordPaint);
		canvas.drawText("Time:",screenWidth/10f + width7*5/2.5f,titleBarRect.top + height3*2,wordPaint);
        canvas.drawText("Score:", screenWidth/2f + width3/2.8f + width6*2 ,titleBarRect.top + height3*2,wordPaint);
			
		}
	
	private void drawButton(){
		matchBtn.drawSelf(canvas, paint);
		notMatchBtn.drawSelf(canvas, paint);
	}
	private void initPaint(){
		wordPaint.setTypeface(typeface);
		wordPaint.setTextSize(27);
		wordPaint.setColor(Color.BLACK);
		
		numPaint.setColor(Color.rgb(103, 58, 121));
		numPaint.setTextSize(30);
	}
	private void drawData(){
		paint.reset();
		paint.setTypeface(typeface);
		paint.setTextSize(20);
		canvas.drawText(timeMention + "", 
		        screenWidth/10f + width7*5/2.2f + width3/4f,
                titleBarRect.top + height3*2,
                numPaint);
        canvas.drawText(score + "",
        		screenWidth/2f + width3/2.8f + width6*2 +  width3/3f ,
		        titleBarRect.top + height3*2,
		        numPaint);
	}
	
	private void drawTitle(){
		canvas.drawBitmap(star, null, starRect, null);
		canvas.drawBitmap(clock, null, clockRect, null);
	}
	/*
	 * 画在游戏屏幕中央现实的白色图片的函数
	 */
	private void drawWhite(){
		int width = white.getWidth();
		int height = white.getHeight();
		canvas.drawBitmap(white, screenWidth/2 - width/2, screenHeight/2 - height/2, null);
	}
	/*
	 * 在游戏屏幕中央画当前现实的gameImage
	 */
	private void drawShowImage (){
		Bitmap showImage = ((BitmapDrawable)getResources().getDrawable(gameImages[showNum])).getBitmap();
		int height = showImage.getHeight();
		int width = showImage.getWidth();
		canvas.drawBitmap(showImage, screenWidth/2 - width/2, screenHeight/2 - height/2, new Paint());
	}
	
	/*
	 * 实现gameImage更新动画的函数
	 */
	private void animation(long timeCircle){
		
		Bitmap gameImage1 = ((BitmapDrawable)getResources().getDrawable(gameImages[nextNum])).getBitmap();
		Bitmap gameImage2 = ((BitmapDrawable)getResources().getDrawable(gameImages[showNum])).getBitmap();
		Paint paint = new Paint();
		int width = gameImage1.getWidth();
		int height = gameImage1.getHeight();
		long time = timeCircle - timeMark;
		p = new SpeedMatchPhysicsUtils(time, screenHeight, height);
		if(!p.getIsMiddle()){
			Log.d(TAG,p.getY() + "");
			canvas.drawBitmap(gameImage1,screenWidth/2 - width/2,p.getY(),paint);
			canvas.drawBitmap(gameImage2,screenWidth/2 - width/2,p.getY() + screenHeight /2,paint);
		}else{                    //更新动画结束时候，更新isShow的值，判断isMtach的值，并更新当前showNum
			isShow = false;
			
			updateIsMatch();
			
			showNum = nextNum;
		}
	}
	private void updateIsMatch(){
		if(showNum == nextNum)
			isMatch = true;   
		else 
			isMatch =false;
	}
	
	/*
	 * 更新分数的函数
	 */
	public void updateScore(){
		score += 25;
	}
	
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		screenWidth = getWidth();
		screenHeight = getHeight();
		
		gameThread.start();
		sAnimationThread = new ScoreAnimationThread(this);
		sAnimationThread.start();
		thread.start();
		
		
		matchDownBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.match_down_btn)).getBitmap();
		matchUpBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.match_up_btn)).getBitmap();
		notMatchDownBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.not_match_down_btn)).getBitmap();
		notMatchUpBtn = ((BitmapDrawable)getResources().getDrawable(R.drawable.not_match_up_btn)).getBitmap();
		clock = ((BitmapDrawable)getResources().getDrawable(R.drawable.title_clock1)).getBitmap();
		star = ((BitmapDrawable)getResources().getDrawable(R.drawable.title_star1)).getBitmap();
		titleBar = ((BitmapDrawable)getResources().getDrawable(R.drawable.title_bar)).getBitmap(); 
		
		notMatchBtnLocLeft = 30;
		matchBtnLocLeft = screenWidth - 180;
		btnTop = screenHeight/2;
		
		width3 = titleBar.getWidth();
		height3 = titleBar.getHeight();
		
		width6 = star.getWidth();
		height6 = star.getHeight();
		
		width7 = clock.getWidth();
		height7 = clock.getHeight();
		
		titleBarRect = new RectF((screenWidth-width3)/3f,
			      0,
			      (screenWidth-width3)*2/3F+ width3,
			      screenHeight/12.5f);
      starRect = new RectF(screenWidth/2f + width3/2.8f,
                titleBarRect.top+ height3*5/4f,
                screenWidth/2f + width3/2.8f + width6,
               titleBarRect.top+ height3*5/4f + height6);
      clockRect = new RectF(screenWidth/8f,
               titleBarRect.top+ height3*7/7f,
               screenWidth/8f + width7,
               titleBarRect.top+ height3*7/7f + height7);
		
		
		matchBtn = new VirtualButton(matchDownBtn, matchUpBtn, matchBtnLocLeft, btnTop);
		notMatchBtn = new VirtualButton(notMatchDownBtn, notMatchUpBtn, notMatchBtnLocLeft, btnTop);
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		gameThread.setFlag(false);
		sAnimationThread.setFlag(false);
		SoundPlayer.stopSound();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}
}
