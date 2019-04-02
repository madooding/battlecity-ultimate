package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MyAnimation {
	private float duration;
	private TextureRegion[] textureRegion;
	private long startTime = 0;
	private int frameCount;
	private int frameAmount;
	public TextureRegion currentFrame;
	public MyAnimation(float duration, TextureRegion[] textureRegion){
		this.duration = duration;
		this.textureRegion = textureRegion;
		currentFrame = textureRegion[0];
		frameAmount = textureRegion.length;
		frameCount = 0;
		startTime = System.currentTimeMillis();
	}
	
	public TextureRegion getKeyFrame(boolean loop){
		long nowTime = System.currentTimeMillis();
		if((nowTime - startTime) / 1000.0 > duration){
			startTime = nowTime;
			frameCount++;
			if(this.isDone()){
				if(loop)
					frameCount = 0;
				else
					currentFrame = currentFrame;
			}else{
				currentFrame = textureRegion[frameCount];
			}			
		}
		System.out.println("Frame : " + frameCount);
		return currentFrame;
	}
	
	public boolean isDone(){
		if(frameCount < frameAmount)
			return false;
		return true;
	}
	
}
