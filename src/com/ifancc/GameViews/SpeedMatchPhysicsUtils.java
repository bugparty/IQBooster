package com.ifancc.GameViews;

/**
 * @author liyang
 *PhysicsUtils类
 *此类模仿自由落体运动
 *用于计算相关数据
 */
public class SpeedMatchPhysicsUtils {
	private static final String TAG = "SPEEDMATCHER2";
	private static final float GRAVITY = 9.8f;
	
	private int mLine = 0;
	//判断物体是否完成运动
    private boolean isMiddle = false;
	//获得当前时间
	private long t = 0;
	//根据当前的时间来计算出下降的高度
	private long s = 0;
	
	public SpeedMatchPhysicsUtils(long time,int screenHeight,int height) {
		// TODO Auto-generated constructor stub
		this.t = time;
		
	 	mLine = screenHeight/2 - height/2;
	 	s = (long) ((GRAVITY * (t/15.0) * (t/15.0) )/2.0);
	 	
	 	isMiddle = (s >= mLine?true:false);
	 	
	}
	
	public float getY (){
		return s;
	}
	
	public boolean getIsMiddle(){
		return isMiddle;
	}
	
	public void setIsMiddle(boolean bool){
		isMiddle = bool;
	}
}