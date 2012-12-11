package com.ifancc.GameViews;


/**
 * 驱动游戏中正确，错误和分数动画的线程
 * @author liyang
 *
 */
public class ScoreAnimationThread extends Thread{
	private int sleepSpan = 30;
	private GameView gameView;
	private boolean flag;
	public ScoreAnimationThread(GameView gameView) {
		// TODO Auto-generated constructor stub
		this.gameView = gameView;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		flag = true;
		while(flag){
			if(gameView.getScoreY() < 0){
				gameView.setScoreLoc();
				gameView.setScoreShow(false,0);
			}
			if(gameView.isScoreShow()){
				gameView.setScoreY();
			}
			try{
				Thread.sleep(sleepSpan);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void setFlag(boolean b){
		this.flag = b;
	}
}
