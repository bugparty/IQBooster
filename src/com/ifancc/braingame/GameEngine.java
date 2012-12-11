package com.ifancc.braingame;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.ifancc.braingame.IntroScrollLayout.OnViewChangeListener;

/**
 * Train和Play类继承此类，重写passMsg方法
 * 用于在Activity中笤俑不通的gameView
 * @author liyang
 *
 */
public class GameEngine extends Activity implements OnViewChangeListener, OnClickListener{
	public void passMsg(int i) {
	
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnViewChange(int view) {
		// TODO Auto-generated method stub
	}
}
