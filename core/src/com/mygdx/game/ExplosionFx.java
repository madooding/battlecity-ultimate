package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ExplosionFx {
	private MyAnimation explosionAnimation;
	private int x, y;
	public ExplosionFx(int x, int y){
		this.x = x;
		this.y = y;
		explosionAnimation = new MyAnimation(0.05f, Assets.explosionFX[0]);
		Assets.explosionSound.play();
	}
	
	public TextureRegion getCurrentFrame(){

		return explosionAnimation.getKeyFrame(false);
	}
	
	public boolean isDone(){
		return this.explosionAnimation.isDone();
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
}
