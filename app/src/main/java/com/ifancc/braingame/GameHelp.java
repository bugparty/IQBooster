package com.ifancc.braingame;

import com.ifancc.braingame.IntroScrollLayout.OnViewChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
/**
 * 显示帮助信息的页面
 * @author liyang
 *
 */
public class GameHelp extends Activity implements OnViewChangeListener, OnClickListener{

	private IntroScrollLayout introScroll;
	private ImageView[] imageViews;
	private int viewCount;
	private int curSel;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //设置为全屏模式
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_help);
        init();
    }

    private void init(){
		introScroll = (IntroScrollLayout)findViewById(R.id.scrollLayout);
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lLayout);
		viewCount =  introScroll.getChildCount();
		imageViews = new ImageView[viewCount];
		
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
