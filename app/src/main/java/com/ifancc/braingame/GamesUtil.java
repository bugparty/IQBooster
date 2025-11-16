package com.ifancc.braingame;

import com.ifancc.GameViews.BrainShiftView;
import com.ifancc.GameViews.ColorMatchView;
import com.ifancc.GameViews.GameView;
import com.ifancc.GameViews.MemoryMatchView;
import com.ifancc.GameViews.SpeedMatchView;
import com.ifancc.GameViews.TopChimpView;
import com.ifancc.braingame.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 初始化游戏和how to play介绍页面的工具类
 * @author liyang
 *
 */
public class GamesUtil {
	
	public static final int[][] trainGames = new int[][]{{0,1,2,3},{0,1,2,4},{1,2,0,3},{2,0,1,4},{1,0,2,3},{1,0,2,4}};
	
	public static Map loadGames(GameEngine context){
		Map<Integer,GameView> gameMap = new HashMap<Integer,GameView>();
		
		SpeedMatchView speedMatchView = new SpeedMatchView(context);
    	MemoryMatchView memoryMatchView = new MemoryMatchView(context);
    	TopChimpView topChimpView = new TopChimpView(context);
		ColorMatchView colorMatchView = new ColorMatchView(context);
    	BrainShiftView brainShiftView = new BrainShiftView(context);
    	gameMap.put(0, speedMatchView);
		gameMap.put(1, memoryMatchView);
		gameMap.put(2,topChimpView);
		gameMap.put(3, brainShiftView);
		gameMap.put(4, colorMatchView);
    	return gameMap;
	}
	public static List<Integer> loadIntroPages(){
		List<Integer> introPages = new ArrayList<Integer>();
		introPages.add(R.layout.activity_speed_match_intro);
		introPages.add(R.layout.activity_memory_match_intro);
		introPages.add(R.layout.activity_top_chimp_intro);
		introPages.add(R.layout.activity_brian_shift_intro);
		introPages.add(R.layout.activity_color_match_intro);
		return introPages;
	}
	
}
