package com.ifancc.GameViews;

import com.ifancc.braingame.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.View;

public class DataView extends View {
	
	private int screenHeight = 0;
	private int screenWidth = 0;
	
	
	private static final int RHEIGHT = 30;
	private static final int RLEFT = 150;
	private static final int TLEFT = 20;
	
	private static final int LLEFT = 100;
	private static final int LINCREACE = 20;
	private static final int LTOP = 300;
	
	
	private int speed = 0;
	private int memory = 0;
	private int attention = 0;
	private int flexibility = 0;
	
	private int[] data;
	//private int[] history;
	
	private Canvas canvas = null;
	private Paint paint = new Paint();
	private Typeface typeface = null;
	
	private Bitmap bkground;
	private Handler handler = new Handler();
	
	public DataView(Context context,int[] data) {
		super(context);
		this.data = data;
		//this.history = history;
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/GIDDYUPSTD.OTF");
		bkground = ((BitmapDrawable)getResources().getDrawable(R.drawable.grid_view_bkground1)).getBitmap();
		new Thread(run).start();
	}

	
	Runnable run = new Runnable() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(speed < data[0]/5)
				speed+=10;
			if(memory < data[1]/5)
				memory+=10;
			if(attention < data[2]/5)
				attention += 10;
			if(flexibility < data[3]/5)
				flexibility +=10;
			if(n < 30){
				n++;
			}
			postInvalidate();
			handler.postDelayed(run, 10);
		}
	};
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		this.canvas = canvas;
		paint.reset();
		paint.setTypeface(typeface);
		paint.setColor(Color.rgb(70,70,70));
		paint.setTextSize(30);
		canvas.drawBitmap(bkground, null,new RectF(0,0,screenWidth,screenHeight), null);
		canvas.drawText("speed", 100, screenHeight - 50	, paint);
		canvas.drawText("memory", 300, screenHeight - 50	, paint);
		canvas.drawText("attention", 500, screenHeight - 50	, paint);
		canvas.drawText("flexibility", 700, screenHeight - 50	, paint);
		drawSpeed();
		drawMemory();
		drawAttention();
		drawFlexibility();
		drawDataLine();
	}
	
	private void drawSpeed(){
		drawT("Speed",50);
		drawR(30,speed);
		drawN(data[0],50);
	}
	
	private void drawMemory(){
		drawT("Memory",100);
		drawR(80,memory);
		drawN(data[1],100);
	}
	
	private void drawAttention(){
		drawT("Attention",150);
		drawR(130,attention);
		drawN(data[2],150);
	}
	
	private void drawFlexibility(){
		drawT("Flexibility",200);
		drawR(180,flexibility);
		drawN(data[3],200);
	}
	
	int n = 0;
	private void drawDataLine(){
		Paint paint = new Paint();
		paint.setColor(Color.rgb(70,70,70));
		paint.setTextSize(17);
		drawL();
	}
	
	
	private void drawT(String text,float y){
		paint.reset();
		paint.setTypeface(typeface);
		paint.setColor(Color.rgb(70,70,70));
		paint.setTextSize(30);
		canvas.drawText(text,TLEFT,y,paint);
	}
	
	private void drawR(float top,int x){
		paint.reset();
		paint.setColor(Color.rgb(30, 100, 123));
		canvas.drawRect(RLEFT,top,(RLEFT + x),(top + RHEIGHT),paint);
	}
	private void drawN(int x,float top){
		paint.reset();
		paint.setColor(Color.WHITE);
		paint.setTextSize(17);
		canvas.drawText(x+"",RLEFT+10,top,paint);
	}
	private void drawL(){
		paint.reset();
		paint.setStyle(Paint.Style.STROKE);
		Path path = new Path();
		path.moveTo(LLEFT,LTOP);
		path.lineTo(LLEFT + (n/10 < 1?(n % 10):10)* LINCREACE,
				LTOP - ((data[1] - data[0])) * (n/10 < 1?(n % 10):10)/100);
		if(n/10 >= 2){
			path.lineTo(LLEFT + ((n/10 < 2?(n % 10):10 )+ 10)* LINCREACE,
					LTOP - ((data[2] - data[1])) * (n/10 < 2?(n % 10):10)/100);
		}
		if(n/10 >= 3){
			path.lineTo(LLEFT + ((n/10 < 3?(n % 10):10) + 20)* LINCREACE,
					LTOP - ((data[3] - data[2])) * (n/10 < 3?(n % 10):10)/100);
		}
		canvas.drawPath(path, paint);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		screenHeight = h;
		screenWidth = w;
	}
}
