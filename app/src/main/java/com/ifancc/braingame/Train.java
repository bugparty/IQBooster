package com.ifancc.braingame;


import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import com.ifancc.Constants.Constant;
import com.ifancc.Constants.UserInfos;
import com.ifancc.Constants.UserScores;
import com.ifancc.GameViews.DataView;
import com.ifancc.GameViews.GameEndView;
import com.ifancc.GameViews.GameView;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 用户开始今日训练时调用的Activity
 * 继承自GameEngine类
 * @author liyang
 *
 */
public class Train extends GameEngine  {

	private static Queue<Integer> gameList = new LinkedList<Integer>();
	private Map<Integer,GameView> gameMap = new HashMap<Integer, GameView>();
	private GameEndView gameEndView;
	private DataView dataView;
	
	private IntroScrollLayout introScroll;
	private ImageView[] imageViews;
	private int viewCount;
	private int curSel;
	private Button startButton;
	
	private List<Integer> introPages;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏模式
    	
        super.onCreate(savedInstanceState);
        
        //Constant.al.add(this);
        
        Constant.engineStatus = 1;
        
        initGameList();

    }
    
    protected void onResume() {
    	super.onResume();
    	//初始化游戏列表
        //initGameMap();
    	gameMap = GamesUtil.loadGames(this);
    	introPages = GamesUtil.loadIntroPages();
		
        gameEndView = new GameEndView(this);
        
        
        setContentView(introPages.get(UserInfos.currentGame));
        initIntroPage();
    }
    
    
    private void initGameList(){
    	if(gameList != null){
    		gameList.clear();
    	}
    	for(int i = 0;i<UserInfos.trainGames.length;i++){
    		gameList.add(UserInfos.trainGames[i]);
    	}
    	UserInfos.currentGame = gameList.peek();
    }
    
    @Override
    public void passMsg(int i) {
    	// TODO Auto-generated method stub
    	handler.sendEmptyMessage(i);
    }
    
    public Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		if(msg.what == 1){
    			if(UserInfos.currentGame != null){
    				setContentView(gameMap.get(UserInfos.currentGame));
    			}
    		}else if(msg.what == 0){   
    			if(gameList != null){
    				gameList.poll();
    			}
				UserInfos.currentGame = gameList.peek();
				if(UserInfos.currentGame == null){
					Date today = new Date();
					//记录最近一次完成训练的时间
					SharedPreferences preference = getSharedPreferences(UserInfos.userName + Constant.PREFERENCESNAME, MODE_PRIVATE);
					Editor editor = preference.edit();
					editor.putInt("lastTrainTime",today.getDate());
					 //将用户成绩记入本地文件
					editor.putInt("speedScore",UserScores.speedScore);
					editor.putInt("memoryScore",UserScores.memoryScore);
					editor.putInt("attentionScore",UserScores.attentionScore);
					editor.putInt("flexibilityScore",UserScores.flexibilityScore);
					editor.commit();
					UserInfos.isTrained = true;
					int[] a = new int[]{UserScores.speedScore,UserScores.memoryScore,UserScores.attentionScore,UserScores.flexibilityScore};
			        dataView = new DataView(Train.this,a);
			       
			        
			        System.out.println("after train"+ today.getDate());
			       
					setContentView(dataView);
				}else{
					setContentView(gameEndView);
				}
			}else if(msg.what == 4){
				setContentView(introPages.get(UserInfos.currentGame));
				initIntroPage();
			}
    	}
    };
    
    
	private void initIntroPage(){
    	startButton = (Button)findViewById(R.id.startBtn);
		introScroll = (IntroScrollLayout)findViewById(R.id.scrollLayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lLayout);
		viewCount =  introScroll.getChildCount();
		imageViews = new ImageView[viewCount];
		
		startButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
			}
		});
		for(int i = 0;i<viewCount;i++){
			imageViews[i] = (ImageView) linearLayout.getChildAt(i);
    		imageViews[i].setEnabled(true);
    		imageViews[i].setOnClickListener(this);
    		imageViews[i].setTag(i);
		}
		curSel = 0;
		imageViews[curSel].setEnabled(false);
		introScroll.SetOnViewChangeListener(this);
	}
	
	private void setCurPoint(int index){
		if(index < 0 || index >viewCount - 1||curSel == index){
			return;
		}
		imageViews[curSel].setEnabled(true);
    	imageViews[index].setEnabled(false);    	
    	curSel = index;
	}
	
	@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
		setCurPoint(view);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer)(v.getTag());
		setCurPoint(pos);
		introScroll.snapToScreen(pos);
	}
	
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_train, menu);
        return true;
    }
    
       @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	   //TODO Auto-generated method stub
    	if(keyCode == KeyEvent.KEYCODE_BACK){
    	}
		return super.onKeyDown(keyCode, event);
	}
}
