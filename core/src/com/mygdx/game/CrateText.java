package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CrateText {
	public float x, y;
	private long startTime = System.currentTimeMillis();
	private float duration = 2f;
	private float speed = 200f;
	private float originalY;
	String text;
	public CrateText(String text, float x, float y){
		this.text = text;
		this.x = ((x + 25));
		this.y = y;
		originalY = y + 25;
	}
	public boolean isDone(){
		y += speed * Gdx.graphics.getDeltaTime();
		if(y > originalY) y = originalY;
		long nowTime = System.currentTimeMillis();
		if(Useful.elapsedTime(startTime, nowTime) >= duration){
			startTime = nowTime;
			return true;
		}
		return false;
	}
	
}
