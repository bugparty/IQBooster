package com.ifancc.GameViews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.ifancc.braingame.Login;
import com.ifancc.braingame.R;



public class WelcomeView extends SurfaceView implements  SurfaceHolder.Callback{
	private Login login;
	private Bitmap bkground;
	private Bitmap[] loading = new Bitmap[3];
	
	private int drawing = 0;
	
	private int screenWidth;
	private int screenHeight;
	public WelcomeView(Login login) {
		// TODO Auto-generated constructor stub
		super(login);
		this.login = login;
		
		this.getHolder().addCallback(this);
		
		init();
	}
	
	private void init(){
		bkground = BitmapFactory.decodeResource(login.getResources(), R.drawable.welcome_bkground);
		loading[0] = BitmapFactory.decodeResource(login.getResources(), R.drawable.welcome_loading1);
		loading[1] = BitmapFactory.decodeResource(login.getResources(), R.drawable.welcome_loading2);
		loading[2] = BitmapFactory.decodeResource(login.getResources(), R.drawable.welcome_loading3);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawBitmap(bkground, null, new RectF(0,0,screenWidth,screenHeight), null);
		canvas.drawBitmap(loading[drawing],screenWidth/2 - loading[drawing].getWidth()/2,screenHeight/2,null);
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
		new Thread(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				for(int i = 0;i<3;i++){
					drawing = i;
					SurfaceHolder myHolder = WelcomeView.this.getHolder();
					 Canvas canvas = myHolder.lockCanvas();
					try{
						synchronized(myHolder){
							onDraw(canvas);//绘制
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					finally{
						if(canvas != null){
							myHolder.unlockCanvasAndPost(canvas);
						}
					}			
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				login.handler.sendEmptyMessage(0);
			}
		}.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
	}
	
}
