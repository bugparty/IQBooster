package com.ifancc.GameViews;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.ifancc.Constants.SoundPlayer;
import com.ifancc.Constants.UserScores;
import com.ifancc.Utils.VirtualButton;
import com.ifancc.braingame.GameEngine;
import com.ifancc.braingame.R;

public class ColorMatchView  extends GameView{
	
	private Canvas canvas;
	
	private SurfaceHolder holder = null;
	
	
	private int width = 0;
	private int height = 0;
	private int t = 0;
	
	
	private int x, y;
	private int j,k;//定义下面的数组变量
	
	private int timeMention;// 显示mentionTitle的时间
	private int score;// 显示mentionTitle的分数

	private boolean isMatch;//判断两边是否match
	private boolean isReady;//判断用户是否点击Ready按钮
	 
	//定义来两个虚拟按钮类的按钮 matchBtn 和 notMatchBtn
	private VirtualButton matchBtn;
	private VirtualButton notMatchBtn;
	
	private Bitmap dizuo;// 屏幕中间的白色底座
	private Bitmap title_bar;//屏幕上方的title_bar
	
	//定义按钮的位图
	private Bitmap match;
	private Bitmap match_pressed;
	private Bitmap not_match;
	private Bitmap not_match_pressed;
	private Bitmap background;
	private Bitmap star;
	private Bitmap clock;
	
	/*
	 * 定义矩形区域
	 */
	private RectF dizuoRect;
	private RectF titleBarRect;
	
	private RectF backGroundRect;
	private RectF starRect;
	private RectF clockRect;
	private RectF leftRect;
	private RectF rightRect;
	
	//底座的宽高
	int width2;
	int height2;
	
	//title_bar的宽高
	int width3;
	int height3;
	
	//定义按钮的宽高
	int width4;
	int height4;
	int width5;
	int height5;
	int width6;
	int height6;
	int width7;
	int height7;
	
	/*
	 * 定义乱七八糟的画笔
	 */
	Paint mPaint1 = new Paint();
    Paint mPaint2 = new Paint();
    Paint mPaint3 = new Paint();
    Paint mPaint4 = new Paint();
	FontMetrics fm,fm1,fm2,fm3,fm4;
	private Typeface typeface;
	private Typeface typeface1;
	private Typeface typeface2;
	
	
    //定义两个随机变量
    Random r = new Random();
    Random v = new Random();
 
  //定义显示在底座上的单词——字符串数组
    private String[] words=new String[7];
   
    //定义显示在底座字的画笔
    private Paint mPainter[] = new Paint[4];
    
    private boolean isClockPlay = false;
	private GameThread gameThread;
    private ScoreAnimationThread sAnimationThread;
    
    private GameEngine gameEngine;
	public ColorMatchView(GameEngine gameEngine) {
		// TODO Auto-generated constructor stub
		super(gameEngine);
		this.gameEngine = gameEngine;
		holder = getHolder();
		holder.addCallback(this);
		timeMention = 40;
		gameThread = new GameThread(this, holder);
		
		thread.start();
	}
	

	
	private Thread thread = new Thread(){
		public void run() {
				while(timeMention>0){
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
	public void run(){
		  
		while(timeMention>=0){
			   try{
					 if(isReady){
							timeMention--;
						 Thread.sleep(1000); 
						}
					 }

				catch(InterruptedException e){
					       e.printStackTrace();
		             }
	       }
	}
	
	
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if(matchBtn.isActionOnButton(x, y)){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				matchBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				matchBtn.releaseUp();
				setIsReady();	
		        if((getIsMatch()) && getTimeMention()>0){
		    		t++;	
		    		 updateScore();
		    		 //这里还要加correct动画效果和correct声音
		    		 }
		         else if((!getIsMatch()) && getTimeMention()>0){
		    		//这里还要加wrong动画效果和wrong声音
		        	 changeScore();
		    		}
		    	   	changedText();	
		    	   	updateIsMatch();
				break;
			}
		}else if(notMatchBtn.isActionOnButton(x,y)){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				notMatchBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				notMatchBtn.releaseUp();
				setIsReady();  
			    if((getIsMatch()) && getTimeMention()>0){
					  //这里还要加wrong动画效果和wrong声音	
			    	changeScore();
			  		  }
			  	 else if((!getIsMatch()) && getTimeMention()>0){
			  		t++;
			  		  updateScore();
			    	  //这里还要加correct动画效果和correct声音
			    	  }
				 	changedText();
				   	updateIsMatch();
				break;
			}
		}
		return super.onTouchEvent(event);
	}
	
	public void changedText(){
		j=r.nextInt(4);
		k=v.nextInt(4);
	}
	public	void updateIsMatch(){
		 switch(mPainter[k].getColor()){
		  case Color.BLUE:
				if(j==3){
					isMatch = true;
				}else{
					isMatch=false;
				}
			  break;
		  case Color.BLACK:
				 if(j==0){
						isMatch = true;
					}else{
						isMatch=false;
					}
				  break;
		  case Color.YELLOW:
				 if(j==1){
						isMatch = true;
					}else{
						isMatch=false;
					}
				  break;
		  case Color.RED:
				 if(j==2){
						isMatch = true;
					}else{
						isMatch=false;
					}
				  break;
			 }
	   }


		public boolean getIsMatch() {
			// TODO Auto-generated method stub
			return isMatch;
		}



		public void updateScore() {
			// TODO Auto-generated method stub
			if(t<=5){
				score+=25;
				setScoreShow(true,25);
			}else if(t>5 && t<=10){
				score+=50;
				setScoreShow(true,50);
			}else if(t>10 && t<=15){
				score+=75;
				setScoreShow(true,75);
			}else if(t>15 && t<=15){
				score+=100;
				setScoreShow(true,100);
			}
			SoundPlayer.playSound(R.raw.right_sound);
	}
	public void changeScore() {
		t = 0;
		setScoreShow(true,0);
		SoundPlayer.playSound(R.raw.wrong_sound);
	}


		public void setIsReady() {
			// TODO Auto-generated method stub
			isReady = true;
		}

		public int getTimeMention() {
			// TODO Auto-generated method stub
			return timeMention;
		}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.canvas = canvas;
		canvas.drawBitmap(background, null, backGroundRect, null);
		
		
		  float x = width2 / 2;
		  float y = height2 / 2 - (fm.ascent + fm.descent) / 2;
		  float x3 = width3 / 2;
		  float y3 = height3 / 2 - (fm.ascent + fm.descent) /2;
			
			/*
			 * 插入位图的位置
			 */
			
		    canvas.drawBitmap(dizuo, null,dizuoRect , null);
			canvas.drawBitmap(star, null, starRect, null);
			canvas.drawBitmap(clock, null, clockRect, null);
			
			
			/*
			 * 屏幕上白色底座上显示的提示消息
			 */
			canvas.drawText("meaning",
		     		((width - width2) / 2f) + width2 / 4F,
		    		dizuoRect.top-y/5,
		    		mPaint1);

	        canvas.drawText("color",
		    		((width-width2) / 2f) + width2*3 / 4F,
		    		dizuoRect.top-y/5,
		    		mPaint1);
		    /*
			* title_bar上的文字提示
			 */
	        canvas.drawText("Time:",
			 		width/10f + width7*5/2.2f,
					titleBarRect.top + height3*2,
					mPaint2);
	        canvas.drawText("Score:",
	        		width/2f + width3/2.8f + width6*3.2f,
				    titleBarRect.top + height3*2,
				    mPaint2);
		 /*
		  * title_bar上显示的时间倒计时和分数
		  */
				    
	        canvas.drawText(timeMention+"",
	                width/10f + width7*5/2.2f + width3/4f,
	                titleBarRect.top + height3*2,
	  		        mPaint4);
	        canvas.drawText(score+"",
	        		width/2f + width3/2.8f + width6*2.5f + width3/3f ,
	  		       titleBarRect.top + height3*2,
	  		        mPaint4);	 

				 
			/*
			 * 白色底座上的字的变换
			 */

		  canvas.drawText(words[j],
					((width - width2) / 2f) + width2 / 4F,
					dizuoRect.top+y,
					mPainter[j]);

	      canvas.drawText(words[k],
				    ((width-width2) / 2f) + width2*3 / 4F,
				    dizuoRect.top+y,
				    mPainter[k]);
	      
	     drawButton();
	      
	     
	     drawScoreA(canvas);
		//游戏结束时的界面显示
	   
	    if(timeMention <= 0){
	    	thread.interrupt();
	    	 UserScores.flexibilityScore = score;
	    	//gameEngine.handler.sendEmptyMessage(0);
	    	 gameEngine.passMsg(0);
	      }
	}

	
	private void drawButton(){
		Paint paint = new Paint();
		matchBtn.drawSelf(canvas, paint);
	    notMatchBtn.drawSelf(canvas, paint);
		}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		height = getHeight();
		width= getWidth();
		
		
		init();
		gameThread.setFlag(true);
		gameThread.start();
		sAnimationThread = new ScoreAnimationThread(this);
		sAnimationThread.start();
		
	}

private void init() {
		
		score = 0;
		isReady = false;
		typeface = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/PAPYRUS.TTF");
		typeface1 = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/GABRIOLA.TTF");
		typeface2 = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/MYRIADPRO-REGULAR.OTF");
		
		// 位图初始化索引
	
		background = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.color_match_bkground);
		dizuo = BitmapFactory.decodeResource(gameEngine.getResources(),
						R.drawable.color_match_white);
	    title_bar = BitmapFactory.decodeResource(gameEngine.getResources(),
						R.drawable.title_bar);
	    star = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.title_star1);
	    clock = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.title_clock1);
	    match = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.color_match_match_up_btn);
	    match_pressed = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.color_match_match_down_btn);
	    not_match = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.color_match_not_match_up_btn);
	    not_match_pressed = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.color_match_not_match_down_btn);


	    width2 = dizuo.getWidth();
		height2 = dizuo.getHeight();

		width3 = title_bar.getWidth();
		height3 = title_bar.getHeight();
		
		width4 = match.getWidth();
		height4 = match.getHeight();
		
		width5 = not_match.getWidth();
		height5 = not_match.getHeight();
		
		width6 = star.getWidth();
		height6 = star.getHeight();
		
		width7 = clock.getWidth();
		height7 = clock.getHeight();
		
		backGroundRect = new RectF(0,0,width,height);
		dizuoRect = new  RectF((width - width2) / 2f,
						1 / 2F * height,
						(width - width2) / 2f + width2,
						1 / 2F* height + height2);
		titleBarRect = new RectF((width-width3)/3f,
				       0,
				      (width-width3)*2/3F+ width3,
				       height/12.5f);
        starRect = new RectF(width/2f + width3/2.8f,
		              titleBarRect.top+ height3*5/4f,
		              width/2f + width3/2.8f + width6,
                      titleBarRect.top+ height3*5/4f + height6);
        clockRect = new RectF(width/10f,
                      titleBarRect.top+ height3*7/7.2f,
                      width/10f + width7,
                      titleBarRect.top+ height3*7/7.2f + height7);
	    leftRect = new RectF(dizuoRect.left - width5*0.3f-width5,
	  		          height*0.45f,
	  		          dizuoRect.left - width5*0.3f,
				      height*0.45f + height5);
        rightRect = new RectF(dizuoRect.right + width4*0.3f,
				      height*0.45f,
				      dizuoRect.right + width4*0.3f + width4,
				      height*0.45f+ height4);
	    
        matchBtn = new VirtualButton(match_pressed, match, dizuoRect.right + width4*0.2f,height*0.45f);
	    notMatchBtn = new VirtualButton(not_match_pressed, not_match, dizuoRect.left - width5*0.2f-width5,
		  		                              height*0.45f);
			  
	     /*
		  * 用到的画笔
		  */
	     mPaint1.setColor(Color.rgb(140, 33, 167));
		  mPaint1.setStyle(Style.FILL);
		  mPaint1.setTextSize(35);
		  fm1 = mPaint1.getFontMetrics();
		  mPaint1.setTypeface(typeface1);
		  mPaint1.setTextScaleX(width / height);
		  mPaint1.setTextAlign(Paint.Align.CENTER);

		  mPaint2.setColor(Color.DKGRAY);
		  mPaint2.setStyle(Style.FILL);
		  mPaint2.setTextSize(27);
		  fm2 = mPaint2.getFontMetrics();
		  mPaint2.setTypeface(typeface);
		  mPaint2.setTextScaleX(width / height);
		  mPaint2.setTextAlign(Paint.Align.CENTER);

		  mPaint3.setColor(Color.DKGRAY);
		  mPaint3.setStyle(Style.FILL);
		  mPaint3.setTextSize(115);
		  fm3 = mPaint3.getFontMetrics();
		  mPaint3.setTextScaleX(width / height);
		  mPaint3.setTextAlign(Paint.Align.CENTER);
		  
		  mPaint4.setColor(Color.rgb(12, 94, 155));
		  mPaint4.setStyle(Style.FILL);
		  mPaint4.setTextSize(30);
		  fm4 = mPaint4.getFontMetrics();
		  mPaint4.setTextScaleX(width/height);
		  mPaint4.setTextAlign(Paint.Align.CENTER);
      /*
       * 底座上显示的单词的初始化
       */
		
		  words[0]="black";
		  words[1]="yellow";
		  words[2]="red";
		  words[3]="blue";
		  
		/*
		 * 底座上显示的单词的各种颜色  
		 */
	 for(int i =0; i < 4; i++){
		mPainter[i] = new Paint();
		mPainter[i].setStyle(Style.FILL);
		mPainter[i].setTextSize(50);
		mPainter[i].setTypeface(typeface2);
		mPainter[i].setTextScaleX(width / height);
		mPainter[i].setTextAlign(Paint.Align.CENTER);
		}

	    mPainter[0].setColor(Color.BLUE);
	    mPainter[1].setColor(Color.RED);
	    mPainter[2].setColor(Color.BLACK);
	    mPainter[3].setColor(Color.YELLOW);

	    fm = mPainter[0].getFontMetrics();
			
	  }

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		boolean retry = true;
        gameThread.setFlag(false);
        while (retry) {
            try {
            	gameThread.join();
                retry = false;
            } 
            catch (InterruptedException e) {
            }
        }	
        sAnimationThread.setFlag(false);
        SoundPlayer.stopSound();
	}
}
