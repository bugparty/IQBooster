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

public class BrainShiftView  extends GameView{
	
	private Canvas canvas;
	
	private GameEngine gameEngine;
	private SurfaceHolder holder = null;
	
	private float x, y;
	private int timeMention; //显示title_bar的时间
	private int score; //显示title_bar的分数
	
	private boolean isYes;//判断是否正确
	private boolean isReady;//判断用户是否点击ready按钮
	
   
	//定义来两个虚拟按钮类的按钮 matchBtn 和 notMatchBtn
	private VirtualButton yesBtn;
    private VirtualButton noBtn;
    
	//屏幕中间的两个白色底座
	private Bitmap dizuo1;
	private Bitmap dizuo2;
	//屏幕上方的title bar
	private Bitmap title_bar;
	
	//定义按钮的位图
	private Bitmap background;
	private Bitmap yes;
	private Bitmap yes_pressed;
	private Bitmap no;
	private Bitmap no_pressed;
	private Bitmap star;
	private Bitmap clock;
	
	private RectF dizuo1Rect;
	private RectF dizuo2Rect;
	private RectF titleBarRect;	

	
	private RectF leftRect;
	private RectF rightRect;
	private RectF starRect;
	private RectF clockRect;
	private RectF backGroundRect;
	private int t = 0;
	
	
	
	//定义dizuo宽高width1,height1;
	int width1;
	int height1;
	
	//定义title_bar宽高width3,heigth3
	int width3;
	int height3;
	
	//定义match按钮的宽高
	int width4;
	int height4;
	int width5;
	int height5;
	int width6;
	int height6;
	int width7;
	int height7;
	
	//定义乱七八糟的画笔
    Paint mPaint = new Paint();
    Paint mPaint1 = new Paint();
    Paint mPaint2 = new Paint();
    Paint mPaint3 = new Paint();
    Paint mPaint4 = new Paint();
    Paint mPaint5 = new Paint();
    Paint mPaint6 = new Paint();
    Paint mPaint7 = new Paint();
    Paint mPaint8 = new Paint();
    FontMetrics fm,fm1,fm2,fm3,fm4,fm5,fm6,fm7;
    
    private Typeface typeface;
    private Typeface typeface1;
    private Typeface typeface2;
    
    //生成随机数和随机字母  
    Random r = new Random();
    Random v = new Random();
    Random l = new Random();
    Random j = new Random();
   
    int m = j.nextInt(2);
    int n = l.nextInt(2);
    int num = r.nextInt(9)+1;
    char ch = (char)(v.nextInt(26)+'A');
    
    //定义元音字符数组
    private char vowel[] = new char[5];
	
	
	private int width = 0;
	private int height = 0;
	
	private boolean isClockPlay = false;
	private GameThread gameThread = null;
	private ScoreAnimationThread sAnimationThread;

	public BrainShiftView(GameEngine gameEngine) {
		super(gameEngine);
		this.gameEngine = gameEngine;
		holder = getHolder();
		holder.addCallback(this);
		timeMention = 40;
		gameThread = new GameThread(this, holder);
		
		thread.start();
	}

	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		if(yesBtn.isActionOnButton(x, y)){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				yesBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				yesBtn.releaseUp();
				setIsReady();
		    	setScoreShow(true, score);
		    	if((getIsYes()) && getTimeMention()>0){
		    		t++;
		    		updateScore();
		    		//这里还要加correct动画效果和correct声音
		    		
		    		}
		    	else if((!getIsYes()) && getTimeMention()>0){
		    		changeScore();
		    		//这里还要加wrong动画效果和wrong声音
		    		}
		    	   	changeText();
		    	   	updateIsYes();
				break;
			}
		}else if(noBtn.isActionOnButton(x,y)){
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				noBtn.pressDown();
				return true;
			case MotionEvent.ACTION_UP:
				noBtn.releaseUp();
				setIsReady();
				if((getIsYes()) && getTimeMention()>0){
					changeScore();
				  //这里还要加wrong动画效果和wrong声音	
		  		  }
		  		else if((!getIsYes()) && getTimeMention()>0){
		  			t++;
		  		  updateScore();
		    	  //这里还要加correct动画效果和correct声音
		    	  }
			 	changeText();
			   	updateIsYes();
				break;
			}
		}
		return super.onTouchEvent(event);
	}
	public void changeText() {
		
		 m = j.nextInt(2);
        n = l.nextInt(2);	
        num = r.nextInt(9)+1;
	     ch = (char)(v.nextInt(26)+'A');		
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
	
	protected void onDraw(Canvas canvas) {
		this.canvas = canvas;
		canvas.drawBitmap(background, null, backGroundRect, null);
		
		
		
		 x = width1 / 2;
		 y = height1 / 2 - (fm.ascent + fm.descent) / 2;
		
		/*
		 * 插入位图的位置
		 */
		    canvas.drawBitmap(dizuo1, null, dizuo1Rect, null);
			canvas.drawBitmap(dizuo2, null, dizuo2Rect, null);
			canvas.drawBitmap(star, null, starRect, null);
			canvas.drawBitmap(clock, null, clockRect, null);
			
		/*
		 * title_bar上的提示信息
		 */
			float x3 = width3 / 2;
			float y3 = height3 / 2 - (fm.ascent + fm.descent) / 2;
			
			canvas.drawText("Time:",
					((width - width3) /2f + width3/12f),
					titleBarRect.top+y3,
					mPaint3);
			canvas.drawText("Score:",
					((width - width3) /2f + width3*3/3.05f),
				    titleBarRect.top+y3,
				    mPaint3);
		
		/*
		 * title_bar上显示的倒计时和分数
		 */
			canvas.drawText(timeMention+"",
					((width - width3) /2f + width3/4.2f),
					titleBarRect.top+y3,
					mPaint8);
			canvas.drawText(score+"",
					((width - width3) /2f + width3*3/3.05f + width3/6f ),
					 titleBarRect.top+y3,
					mPaint8);
		
		/*
		 * 白色底座右侧的提示信息
		 */
		 
			canvas.drawText("Remember",
					dizuo1Rect.right +width3/2.7f,
					dizuo1Rect.top+y/3.5f,
					mPaint2);
			canvas.drawText("← Top Card",
					dizuo1Rect.right+width3/4.5f,
					dizuo1Rect.top+y*3/3.5f,
					mPaint4);
			canvas.drawText("Is the number",
					dizuo1Rect.right+width3/2.7f,
					dizuo1Rect.top+y*3/2.7f,
					mPaint5);
			canvas.drawText("even?",
					dizuo1Rect.right+width3/1.4f,
					dizuo1Rect.top+y*3/2.7f,
					mPaint6);
			canvas.drawText("← Bottom Card",
					dizuo2Rect.right+width3/3.3f,
					dizuo2Rect.top+y*3/3.5f,
					mPaint4);
			canvas.drawText("Is the letter a",
					dizuo2Rect.right+width3/2.7f,
					dizuo2Rect.top+y*3/2.7f,
					mPaint5);
			canvas.drawText("vowel?",
					dizuo2Rect.right+width3/1.4f,
					dizuo2Rect.top+y*3/2.7f,
					mPaint6);
		
		//在开始游戏前在底座上画点东西
	    drawButton();
		drawSomething();
	    updateIsYes();
	    
	    drawScoreA(canvas);
	    
	    //游戏结束时的界面显示
	    
	    if(timeMention <= 0){
			 thread.interrupt();
			 UserScores.flexibilityScore = score;
			 gameEngine.passMsg(0);
	    }		
	}
	private void drawButton(){
		Paint paint = new Paint();
		yesBtn.drawSelf(canvas, paint);
	    noBtn.drawSelf(canvas, paint);
		}
	public void drawSomething() {
		switch(m){
		case 0:
			if(n==0){
			canvas.drawText(num+""+ch+"", 
					((width - width1) /2f + width1/2f),
					 dizuo1Rect.top+y*3/2.5f, 
					mPaint);
		    }else if(n==1){
	          canvas.drawText(num+""+ch+"",
			  		((width - width1) /2f + width1/2f),
					dizuo2Rect.top+y*3/2.5f,
					mPaint);
	          }
            break;
		
		case 1:
	     if(n==0){
			canvas.drawText(ch+""+num+"", 
					((width - width1) /2f + width1/2f),
					 dizuo1Rect.top+y*3/2.5f, 
					mPaint);
		  }else if(n==1){
	        canvas.drawText(ch+""+num+"",
					((width - width1) /2f + width1/2f),
					dizuo2Rect.top+y*3/2.5f,
					mPaint);
	     }
	     break;
	 }
	}
	public void updateIsYes(){
		   
		//元音数组初始化
		    vowel[0] = 'A';
		    vowel[1] = 'E';
		    vowel[2] = 'I';
		    vowel[3] = 'O';
		    vowel[4] = 'U';
		    
		//实现isYes逻辑
     switch (n){	
		case 0:
			
				if(num % 2 == 0){
					isYes = true;
				}else{
					isYes = false;
				}
			  break;  
		case 1:
				if(ch!=vowel[0]){
					if(ch!=vowel[1]){
						if(ch!=vowel[2]){
							if(ch!=vowel[3]){
								if(ch!=vowel[4]){
									isYes = false;
								}else{
									isYes = true;
								}
							}else{
								isYes=true;
							}
						}else{
							isYes=true;
						}
					}else{
							isYes=true;
						}
				}else{
					isYes=true;	
					}
				break;
			}   		
	}
	
	public boolean getIsYes() {
		// TODO Auto-generated method stub
		return isYes;
	}


	public int getTimeMention() {
		// TODO Auto-generated method stub
		return timeMention;
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

	
	private void init(){
		score = 0;
		isReady = false;
		typeface = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/PAPYRUS.TTF");
		typeface1 = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/CORBELI.TTF");
		typeface2 = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/MYRIADPRO-REGULAR.OTF");
		
		//位图初始化索引
		background = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_bkground);
    	dizuo1 = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_white1);
		dizuo2 = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_white2);
		title_bar = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.title_bar);
		star = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.title_star);
		clock = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.title_clock);
	    yes = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_yes_up_btn);
	    yes_pressed = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_yes_down_btn);
	    no = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_no_up_btn);
	    no_pressed = BitmapFactory.decodeResource(gameEngine.getResources(),
				R.drawable.brain_shift_no_down_btn);
		
		
	    width1 = dizuo1.getWidth();
		 height1 = dizuo1.getHeight();
		 width3 = title_bar.getWidth();
		 height3 = title_bar.getHeight();
		 width4 = yes.getWidth();
		 height4 = yes.getHeight();
		 width5 = no.getWidth();
		 height5 = no.getHeight();
		 width6 = star.getWidth();
		 height6 = star.getHeight();
		 width7 = clock.getWidth();
		 height7 = clock.getHeight();
		 
		 backGroundRect = new RectF(0,0,width,height);
		
		 dizuo1Rect = new RectF((width - width1) / 2f,
				      ((height-height3)/2f)-height1+height3/0.7f,
				       (width - width1) / 2f +width1,
				       ((height-height3)/2f)+height3/0.7f);
		 dizuo2Rect = new RectF((width - width1) / 2f,
				      ((height-height3)/2f) +height3*4/3f,
				    		  (width-width1)/2f+width1,
				    		  ((height-height3)/2f+height3*4/3f+height1));
		 titleBarRect = new RectF((width - width3)/3f,
 		                  0,
 		                (width-width3)*2/3f+width3,
 		                height/12.5f);
         starRect = new RectF((width - width3)/2f + width3/1.3f,
	                    titleBarRect.top+ height3/3f,
	                    (width - width3)/2f + width3/1.3f + width6,
                        titleBarRect.top+ height3/3f + height6);
         clockRect = new RectF((width - width3)/2f - width3/6f,
  		               titleBarRect.top+ height3/8f,
 		              (width - width3)/2f - width3/6f + width7,
 		               titleBarRect.top+ height3/8f + height7);
		 leftRect = new RectF(dizuo2Rect.left - width5*0.9f-width5,
		  		       height*0.45f,
		  		       dizuo2Rect.left - width5*0.9f,
					   height*0.45f + height5);
	     rightRect = new RectF(dizuo2Rect.right + width4*0.9f,
					   height*0.45f,
					   dizuo2Rect.right + width4*0.9f + width4,
					   height*0.45f+ height4);
	     
	     
	     yesBtn = new VirtualButton(yes_pressed, yes, dizuo2Rect.right + width4*0.7f,height*0.45f);
		 noBtn = new VirtualButton(no_pressed, no, dizuo2Rect.left - width5*0.7f-width5,
			  		                              height*0.45f);
			  
	     /*
	      * 用到的画笔
	      */
		 mPaint.setColor(Color.rgb(1, 154, 255));
	     mPaint.setStyle(Style.FILL);
	     mPaint.setTextSize(100);
	     fm = mPaint.getFontMetrics();
	     mPaint.setTextScaleX(width/height);
	     mPaint.setTypeface(typeface2);
	     mPaint.setTextAlign(Paint.Align.CENTER);
	     
	     mPaint2.setColor(Color.BLACK);
	     mPaint2.setStyle(Style.FILL);
	     mPaint2.setTextSize(30);
	     fm = mPaint2.getFontMetrics();
	     mPaint2.setTypeface(typeface1);
	     mPaint2.setTextScaleX(width/height);
	     mPaint2.setTextAlign(Paint.Align.CENTER);
	     
	     mPaint3.setColor(Color.BLACK);
	     mPaint3.setStyle(Style.FILL);
	     mPaint3.setTextSize(20);
	     fm3 = mPaint3.getFontMetrics();
	     mPaint3.setTypeface(typeface);
	     mPaint3.setTextScaleX(width/height);
	     mPaint3.setTextAlign(Paint.Align.CENTER);
	     
	     mPaint4.setColor(Color.BLACK);
	     mPaint4.setStyle(Style.FILL);
	     mPaint4.setTextSize(24);
	     fm4 = mPaint4.getFontMetrics();
	     mPaint4.setTypeface(typeface1);
	     mPaint4.setTextScaleX(width/height);
	     mPaint4.setTextAlign(Paint.Align.CENTER);
	     
	     mPaint5.setColor(Color.BLACK);
	     mPaint5.setStyle(Style.FILL);
	     mPaint5.setTextSize(23);
	     fm5 = mPaint5.getFontMetrics();
	     mPaint5.setTypeface(typeface1);
	     mPaint5.setTextScaleX(width/height);
	     mPaint5.setTextAlign(Paint.Align.CENTER);
	     
	     mPaint6.setColor(Color.rgb(229, 0, 79));
	     mPaint6.setStyle(Style.FILL);
	     mPaint6.setTextSize(23);
	     fm6 = mPaint6.getFontMetrics();
	     mPaint6.setTypeface(typeface1);
	     mPaint6.setTextScaleX(width/height);
	     mPaint6.setTextAlign(Paint.Align.CENTER);
	     
	     mPaint7.setColor(Color.rgb(45, 165, 205));
	     mPaint7.setStyle(Style.FILL);
	     mPaint7.setTextSize(120);
	     fm7 = mPaint7.getFontMetrics();
	     mPaint7.setTextScaleX(width/height);
	     mPaint7.setTextAlign(Paint.Align.CENTER); 
	     
	     mPaint8.setColor(Color.rgb(229, 0, 79));
	     mPaint8.setTextSize(23);
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
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
