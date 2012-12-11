package com.ifancc.Utils;

import com.ifancc.braingame.R;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 自定义View中的的虚拟按钮类
 * @author liyang
 *
 */
public class VirtualButton {
	//button的位置
	private float x;
	private float y;
	
	private int height;
	private int width;
	private Bitmap downBmp;
	private Bitmap upBmp;
	private boolean isDown = false;
	
	public VirtualButton(Bitmap downBmp,Bitmap upBmp,float x,float y){
		this.downBmp = downBmp;
		this.upBmp = upBmp;
		this.height = downBmp.getHeight();
		this.width = downBmp.getWidth();
		this.x = x;
		this.y = y;
	}
	
	public void drawSelf(Canvas canvas,Paint paint){
		if(isDown){
			canvas.drawBitmap(downBmp, x, y, paint);
		}else{
			canvas.drawBitmap(upBmp, x, y, paint);
		}
	}
	
	public void pressDown(){
		isDown = true;
	}
	public void releaseUp(){
		isDown = false;
	}
	
	public boolean isActionOnButton(float pressX,float pressY){
		if((pressX > x)&&(pressX < x + width)&&(pressY > y)&&(pressY < y+height)){
			return true;
		}else{
			return false;
		}
	}
}