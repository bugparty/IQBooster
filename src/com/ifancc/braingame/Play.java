package com.ifancc.braingame;

import java.util.List;
import java.util.Map;

import com.ifancc.Constants.Constant;
import com.ifancc.GameViews.GameEndView;
import com.ifancc.GameViews.GameView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 *实现单个游戏的游戏引擎
 *继承自GameEngine类
 * @author liyang
 *
 */
public class Play extends GameEngine{
	private Map<Integer,GameView> gameMap;
	private List<Integer> introPages;
	private GameEndView gameEndView;
	private int gameId = 0;
	
	private IntroScrollLayout introScroll;
	private ImageView[] imageViews;
	private int viewCount;
	private int curSel;
	private Button startButton;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏模式
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        Bundle a = intent.getExtras();
        gameId = (Integer) a.get("gameId");
        System.out.println(gameId + "");
        
        Constant.engineStatus = 2;
        
        gameMap = GamesUtil.loadGames(Play.this);
        introPages = GamesUtil.loadIntroPages();
        gameEndView = new GameEndView(this);
        setContentView(introPages.get(gameId));
        initIntroPage();
    }
    
    @Override
    public void passMsg(int i) {
    	// TODO Auto-generated method stub
    	super.passMsg(i);
    	handler.sendEmptyMessage(i);
    }
    
    
    private Handler handler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch(msg.what){
    		case 0:
    			setContentView(gameEndView);
    			break;
    		case 1:
    			setContentView(gameMap.get(gameId));
    			break;
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
    
}
