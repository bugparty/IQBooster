package com.ifancc.GameViews;

import com.ifancc.braingame.R;

import java.util.Random;

import android.util.Log;

public class MemoryMatchControl {
	
	private static final String TAG = "MemoryMatch";
	private Random rand;
	
	private boolean isReady;
	

	private boolean isMatch;
	private boolean isCorrect;
	private boolean isTop;
	
	private int leftImage;
	private int middleImage;
	private int rightImage;
	private int goImage;
	private int score;
	

	
	private int gameLevel;
	private int combos;
	private boolean lefIsVisible;
	private boolean midIsVisible;
	private boolean goIsVisible;
	
	private int scoreAdd = 0;
	

	public MemoryMatchControl() {
		// TODO Auto-generated method stub
		rand = new Random(System.currentTimeMillis());
		isReady = false;
		isMatch =false;
		isCorrect = false;
		isTop = false;
		
		score = 0;
		
		leftImage = 0;
		middleImage = 0;
		rightImage = 0;
		goImage = 0;
		
		gameLevel = 0;
		combos = 0;
		lefIsVisible = false;
		midIsVisible = false;
		goIsVisible = false;
		
	}
	
	
	
	private void updateCombos(){
		if(isCorrect && !isTop){
			if(combos <2){
				combos++;
			}else{
				combos = 1;
			}
		}else if(isCorrect && isTop){
			combos++;
		}else if(!isCorrect && isTop){
			combos = 0;
		}
	}
	
	private void updateGameLevel(){
		if(gameLevel < 3 && !isTop){
			if(combos == 2){
				gameLevel++;
				if(gameLevel == 3){
					isTop = true;
					combos = 0;
				}
			}
		}else if(isTop){
			if(isCorrect){
				if(gameLevel < 3){
					gameLevel++;
				}
			}
			else if(!isCorrect){
				if(gameLevel > 1){
					gameLevel--;
				}
			}
		}
	}
	
	private void updateScore(){
		
		if(isTop && (combos == 2)){
			score += 50;
			scoreAdd = 50;
		}else{
			score += 25;
			scoreAdd = 25;
		}
	}
	
	private void updateImage(){
		goImage = leftImage;
		leftImage = middleImage;
		middleImage = rightImage;
		rightImage = Math.abs(rand.nextInt()) % 3;
	}
	private void updateIsMatch(){
		if(leftImage == rightImage)
			isMatch =  true;
		else
			isMatch = false;
	}
	


	private void updateVisibility(){
		goIsVisible = lefIsVisible;
		if(gameLevel == 1){
			lefIsVisible = true;
			midIsVisible= true;
		}else if(gameLevel == 2){
			lefIsVisible = false;
			midIsVisible = true;
		}else if(gameLevel == 3){
			lefIsVisible = false;
			midIsVisible = false;
		}
	}
	
	
	public void start(){
		leftImage = Math.abs(rand.nextInt()) % 3;
		middleImage = Math.abs(rand.nextInt()) % 3;
		rightImage = Math.abs(rand.nextInt()) % 3;
		isTop = false;
		
		if(leftImage == rightImage)
			isMatch = true;
		else
			isMatch = false;
		
		gameLevel = 1;
		combos = 0;
		lefIsVisible =true;
		midIsVisible = true;
		
		Log.d(TAG,"leftImage---------->"+leftImage);
		Log.d(TAG,"middleImage---------->"+middleImage);
		Log.d(TAG,"RightImage---------->"+rightImage);
		Log.d(TAG,"score---------->"+ score);
		Log.d(TAG,"leftVisibility---------->" + lefIsVisible);
		Log.d(TAG,"midVisibility---------->" + midIsVisible);
		Log.d(TAG,"isMatch---------->" + isMatch);
		
	}
	
	
	public void next(){
		if(!isReady)
			isReady = true;
		if(isCorrect){
			updateScore();
		}
		updateCombos();
		updateGameLevel();
		updateVisibility();
		updateImage();
		updateIsMatch();
		Log.d(TAG,"gameLevel------>" + gameLevel);
		Log.d(TAG,"leftImage---------->"+leftImage);
		Log.d(TAG,"middleImage---------->"+middleImage);
		Log.d(TAG,"RightImage---------->"+rightImage);
		Log.d(TAG,"score---------->"+ score);
		Log.d(TAG,"leftVisibility---------->" + lefIsVisible);
		Log.d(TAG,"midVisibility---------->" + midIsVisible);
		Log.d(TAG,"isMatch---------->" + isMatch);
		Log.d(TAG,"combos---------->" + combos);
	}
	
	
	
	
	
	
	
	public int getScoreAdd(){
		return scoreAdd;
	}
	public boolean isReady() {
		return isReady;
	}

	public void setReady(boolean isReady) {
		this.isReady = isReady;
	}

	public boolean isMatch() {
		return isMatch;
	}

	public void setMatch(boolean isMatch) {
		this.isMatch = isMatch;
	}

	public int getLeftImage() {
		return leftImage;
	}

	public void setLeftImage(int leftImage) {
		this.leftImage = leftImage;
	}

	public int getMiddleImage() {
		return middleImage;
	}

	public void setMiddleImage(int middleImage) {
		this.middleImage = middleImage;
	}

	public int getRightImage() {
		return rightImage;
	}

	public void setRightImage(int rightImage) {
		this.rightImage = rightImage;
	}

	public int getGameLevel() {
		return gameLevel;
	}

	public void setGameLevel(int gameLevel) {
		this.gameLevel = gameLevel;
	}

	public boolean isLefIsVisible() {
		return lefIsVisible;
	}

	public void setLefIsVisible(boolean lefIsVisible) {
		this.lefIsVisible = lefIsVisible;
	}

	public boolean isMidIsVisible() {
		return midIsVisible;
	}

	public void setMidIsVisible(boolean midIsVisible) {
		this.midIsVisible = midIsVisible;
	}
	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	public int getScore() {
		return score;
	}



	public void setScore(int score) {
		this.score = score;
	}

	public int getGoImage() {
		return goImage;
	}



	public void setGoImage(int goImage) {
		this.goImage = goImage;
	}



	public boolean isGoIsVisible() {
		return goIsVisible;
	}



	public void setGoIsVisible(boolean goIsVisible) {
		this.goIsVisible = goIsVisible;
	}

}
