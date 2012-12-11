package com.ifancc.Constants;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;

import cn.appmedia.adshelf.ShelfView;

import com.ifancc.braingame.R;

public class Constant {
	
	public static final String PREFERENCESNAME = "userInfo";
	
	public static final String BASE_URL = "http://braingame.sinaapp.com/api";
	public static final String REGISTER_URL = "http://braingame.sinaapp.com/api/register";
	public static final String LOGIN_URL = "http://braingame.sinaapp.com/api/login";
	public static final String ENCODING 		= "UTF-8";
	
	public static ShelfView shelView;
	
	/*
	 * 记录actiity
	 */
	public static List al;
	/*
	 * 描述loginactivity的状态
	 */
	public static int loginStatus;
	public static int engineStatus;
	
	public static int SCREENWIDTH;
	public static int SCREENHEIGHT;
	
	public static Bitmap correct;
	public static Bitmap wrong ;
	
	/*
	 * 自定义字体
	 */
	public static int[][] introPages;

	/*
	 * 需要使用到的自定义字体
	 */
	public static Typeface t1;
	public static Typeface t2;
	public static Typeface t3;
	
	/*
	 * topChimp中关于等级的描述：
	 * 
	 * 本游戏一共分为三个等级，LEVEL1,LEVEL2,LEVEL3.
	 * 每个等级都由 nums即：屏幕中显示数字的个数 和 speed 即：屏幕中数字被覆盖物覆盖的数度决定，同一等级的nums与speed的和相等
	 * 每一个顶级又有若干中表述方式，即：nums与speed的不同组合
	 * 
	 */
	public static final int[][] LEVEL1 = {{4,2},{3,3},{5,1}};
	public static final int[][] LEVEL2 = {{5,2},{6,1},{4,3}};
	public static final int[][] LEVEL3 = {{7,1},{5,3},{4,4},{6,2}};
	public static final int[][][] LEVEL = {LEVEL1,LEVEL2,LEVEL3};
	public static final int[] SPEED = {600,400,200,100};
	
	
	
	public static void initConst(Context context,int screenWidth,int screenHeight){
		al = new ArrayList();
		loginStatus = 0;
		engineStatus =0;
		
		SCREENHEIGHT = screenWidth;
		SCREENWIDTH = screenHeight;
		
		correct = BitmapFactory.decodeResource(context.getResources(),R.drawable.correct);
		wrong = BitmapFactory.decodeResource(context.getResources(),R.drawable.wrong);
		
		introPages = new int[][]{
				{R.drawable.speed_match_intro1,R.drawable.speed_match_intro2,R.drawable.speed_match_intro3},
				{R.drawable.memory_match_intro1,R.drawable.memory_match_intro2},
				{R.drawable.top_chimp_intro1, R.drawable.top_chimp_intro2},
				{R.drawable.brain_shift_intro1,R.drawable.brain_shift_intro2,R.drawable.brain_shift_intro3},
				{R.drawable.color_match_intro1,R.drawable.color_match_intro2,R.drawable.color_match_intro3}};
		
		t1 = Typeface.createFromAsset(context.getAssets(),"fonts/CARTOON.ttf");
		t2 = Typeface.createFromAsset(context.getAssets(), "fonts/PAPYRUS.TTF");
		t3 = Typeface.createFromAsset(context.getAssets(), "fonts/GIDDYUPSTD.OTF");
	}
	
}
	
	
