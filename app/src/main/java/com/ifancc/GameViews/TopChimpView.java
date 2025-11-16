package com.ifancc.GameViews;

import com.ifancc.Constants.Constant;
import com.ifancc.Constants.SoundPlayer;
import com.ifancc.Constants.UserScores;
import com.ifancc.braingame.R;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.ifancc.braingame.GameEngine;

public class TopChimpView extends GameView {
	//获得screen的信息    
	private int screenWidth = 0;
	private int screenHeight = 0;
	//创建surfaceView的holder    创建更新画布的Thread
	private SurfaceHolder holder = null;
	//游戏信息
	private int currentLevel = 2;
	private int currentSpeed = 0;
	private int score = 0;
	private int correctNums = 0;
	//使用的自定义字体
	private static Typeface typeface = null;
	
	//每一次岁进现实的数字
	private int showNums[]= null;
	//将屏幕分成24块，代表24个location，数字在24块中随机显示    第25块的作用是在更新动画中画移动出界面的数字使用的
	private int[][] locations = new int[25][2];
	//表示每一块location的长和宽
	private int locWidth = 0;
	private int locHeight = 0;
	//存储随机出现的数字所画的位置
	private int[] numsLocations = null;
	//存储遮盖随机出现数字的圆，其中的元素与nums'Locations的元素一样，但要用队列来存储，以实现判断是否按照顺序点击数字
	private Queue<Integer> shadeQueue = new LinkedList<Integer>();
	//表示游戏转台，以更新canvas应该现实的内容
	private int status = 0;
	//一些用于控制游戏的boolean量   具体含义可由变量名得知
	//用户是否按照顺序点击数字
	private boolean isOrder = true;
	//一局游戏是否完成
	private boolean isComplete = false;
	//当isClick为true的时候，screen相应用户的点击，在onTouchEvent中执行相应的函数
	private boolean isClick = false;
	//需要计算线程刷新画布的次数   所以必须使用int型来定义isShow
	private int isShow = 0;  

	private int times = 0;
	private Bitmap circle;
	private GameThread gameThread;
	private GameEngine gameEngine;
	public TopChimpView(GameEngine gameEngine) {
		// TODO Auto-generated constructor stub
		super(gameEngine);
		this.gameEngine = gameEngine;
		typeface = Typeface.createFromAsset(gameEngine.getAssets(), "fonts/TRAJANPRO-BOLD.OTF");
		//currentLevel = level;
		circle = ((BitmapDrawable)getResources().getDrawable(R.drawable.top_chimp_circle)).getBitmap();
		
		holder = getHolder();
		holder.addCallback(this);
		gameThread = new GameThread(this, holder);
	}
	
	/*
	 * 初始化locations中的信息，需要在GameView类构造之后，与得到scren的信息之后执行
	 */
	private void initLocations(){
		
		int m = 0;
		for(int i = 0;i<6;i++){
			for(int j = 0;j<4;j++){
				locations[m][0] = i* locWidth + 3;
				locations[m][1] = j* locHeight;   //不断调整出来的结果
				m++;
			}
		}
		locations[24][0] = -100;
		locations[24][1] = -100;
	}
	/*
	 * 用于随机获取每一局得到的随机数字相对应的随机位置
	 */
	private void getShowLocations(){
		//清空numsLocations  shadeQueue
		numsLocations = new int[showNums.length];
		shadeQueue.clear();
		
		Random rand = new Random(System.currentTimeMillis()); 
		for(int m = 0;m<showNums.length;m++){
			int temp = Math.abs(rand.nextInt())%24;
			for(Iterator<Integer> iterator = shadeQueue.iterator();iterator.hasNext(); ){
				Integer x = iterator.next();
				if(temp == x){
					temp = Math.abs(rand.nextInt())%24;
					iterator = shadeQueue.iterator();
				}
			}
			numsLocations[m] = temp;
			shadeQueue.offer(temp);
		}
	}
	/*
	 * 根据currentLevel获取每一局随机显示的数字和速度
	 */
	public void getShowNums(){    
		Random rand = new Random(System.currentTimeMillis());
		int nums = 0;
		int [][]  level = Constant.LEVEL[currentLevel - 1];
		//随机显示此level中的某一个levelDetail
		int levelDetailNum = Math.abs(rand.nextInt())%(level.length);
		int[] levelDetail = level[levelDetailNum];
		//update nums
		nums = levelDetail[0];
		//update speed   speed在menuView中进行设置
		currentSpeed = levelDetail[1] - 1;
		showNums = new int[nums];
		for(int i = 0;i<nums;i++){
			int temp = Math.abs(rand.nextInt())%9 + 1;
			for(int j = 0;j<=i;j++){
				if(temp == showNums[j]){  //防止产生一样的随机数字
					j = -1;
					temp = Math.abs(rand.nextInt())%9 + 1;
				}
			}
			showNums[i] = temp;
		}
		//将产生的随机数列排序
		sortShowNum();
	}
	/*
	 * 将随机选出的数字按照从小到达的顺序排列
	 */
	private void sortShowNum(){
		
		int little = 0;
		for(int i = 0;i < showNums.length;i++){
			little = i;
			for(int j = i+1;j<showNums.length;j++){
				if(showNums[j] < showNums[little]){
					little = j;
				}
			}
			int temp = showNums[i];
			showNums[i] = showNums[little];
			showNums[little] = temp;
		}
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawBack(canvas);
		if(times <=10){
			if(status == 0){
				SoundPlayer.playSound(R.raw.touch_sound);
				updateData();
				drawNums(canvas);
				times++;
			}else if(status == 1){
				setSleepSpan(50);
				isClick = true;
				drawNums(canvas);
				if(isOrder){
					drawShade(canvas);
				}
			}else if(status == 2){
				if(times < 10){
					drawNums(canvas);
					drawAnimation();
				}else{
					times++;
				}
				
			}
		}else{
			UserScores.attentionScore = score;
			gameEngine.passMsg(0);
		}
	}
	/*
	 * 画canvas的背景
	 */
	private void drawBack(Canvas canvas) {
		canvas.drawColor(Color.rgb(0,0,0));
	}

	/*
	 * 画canvas要显示的数字
	 */
	private void drawNums(Canvas canvas){
		Paint numPaint = new Paint();
		numPaint.setColor(Color.WHITE);
		numPaint.setTypeface(typeface);
		numPaint.setTextSize(80);
		//使用metrics设置字体的大小
		FontMetrics fm = numPaint.getFontMetrics();
		float x = locWidth/2;
		float y = locHeight/2 - (fm.ascent + fm.descent)/2;
		
		for(int i = 0;i<showNums.length;i++){
			canvas.drawText(showNums[i] + "", locations[numsLocations[i]][0] + x, locations[numsLocations[i]][1] + y, numPaint);
		}
		if(!isComplete){
			status = 1;
		}
	}
	/*
	 * 画遮盖数字的圆
	 */
	private void drawShade(Canvas canvas){
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		
		for(Iterator<Integer> iterator = shadeQueue.iterator();iterator.hasNext();){
			Integer i = iterator.next();
			int x = (2 * locations[i][0] + locWidth)/2 -18;
			int y = (2 * locations[i][1] + locHeight)/2 - 50;
			int radius = ((locHeight> locWidth)?locHeight/2:locWidth/2) - 10;
			canvas.drawBitmap(circle,null,
					new Rect(x,y,x + circle.getWidth(),y + circle.getHeight()),
					null);
		}
	}
	
	/*
	 * 游戏画面处于不通的状态，画布线程刷新的频率不同，此函数用设置画布刷新的频率
	 */
	private void setSleepSpan(int time){
		gameThread.setSleepSpan(time);
	}
	/*
	 * 画每相邻两局之间的更新动画
	 */
	private void drawAnimation(){
		if(isShow++ == 1){
			try {
				gameThread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setSleepSpan(50);
		}
		int i = 0;
		for( i = 0;i<numsLocations.length;i++){
			if((numsLocations[i] > 3)&&(numsLocations[i] != 24)){
				numsLocations[i] -= 4;
			}else if(numsLocations[i] <= 3){
				numsLocations[i] = 24;
			}
		}
		
		for(int j = 0;j<numsLocations.length;j++){
			if(numsLocations[j] != 24){
				break;
			}
			if(j == numsLocations.length - 1){
				status = 0;
			}
		}
	}
	/*
	 * 每两局之间需要更新游戏数据，此函数用来更新数据
	 */
	private void updateData(){
		score += correctNums * currentSpeed * 5;
		System.out.println(score + "");
		//得到要现实的数字
		getShowNums();
		//得到要显示的数字的位置
		getShowLocations();
		isComplete = false;
		isOrder = true;
		isClick = false;
		isShow =0;
		
		setSleepSpan(Constant.SPEED[currentSpeed]);
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if ( event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		if(isClick){
			float x = event.getX();
			float y = event.getY();
			//得到用户点击screen位置在locations中的索引值
			int n = (int)(x/locWidth) * 4 + (int)(y/locHeight);
			if(isContain(n)){
				
				if(n != shadeQueue.poll()){
					isOrder = false;
					isComplete = true;
					status = 2;
					SoundPlayer.playSound(R.raw.round_sound);
				}else{
					correctNums++;
					SoundPlayer.playSound(R.raw.touch_sound);
				}
				if(shadeQueue.isEmpty()){
					status = 2;
					isComplete = true;
					SoundPlayer.playSound(R.raw.round_sound2);
				}
			}
		}
		return true;
	}
	/*
	 * 判断用户点击的区域是否是随机数字出现的区域
	 */
	private boolean isContain(int n){
		for(Iterator<Integer> iterator = shadeQueue.iterator();iterator.hasNext();){
			Integer i = iterator.next();
			if(n == i){
				return true;
			}
		}
		return false;
	}
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		screenHeight = getHeight();
		screenWidth = getWidth();
		
		locWidth = (screenWidth - 8)/ 6;
		locHeight = (screenHeight-8)/4;
		initLocations();
		
		gameThread.setFlag(true);
		gameThread.start();
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
	}
	
	
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
	}
	
}
