package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Box {
	int x = 0, y = 0, oldX, oldY;
	int width = 50, height = 50;
	private int speed = 150;
	Rectangle boxRect;
	int boxDirection = 2;
	boolean collisionTop = false, collisionBottom = false, collisionLeft = false, collisionRight = false;
	private float stateTime = 0f;
	public Box(){
	oldX = x;
	oldY = y;
	boxRect = new Rectangle();
	boxRect.set(x, y, width, height);

	}
	
	public Box(int x, int y){
		this.x = x;
		this.y = y;
		oldX = x;
		oldY = y;
		boxRect = new Rectangle();
		boxRect.set(x, y, width, height);
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}	
	
	public void moveDown(float delta){
		if(!collisionBottom){
			stateTime += delta;
			Assets.yellowTankCurrentFrame = Assets.yellowTankAnimation.getKeyFrame(stateTime, true);
			boxDirection = 4;
			y = (int) Math.round(y - speed * delta);
			if(y < 0) y = 0;
			boxRect.setY(y);
			collisionTop = false;
		}
	}
	
	public void moveUp(float delta){
		if(!collisionTop){
			stateTime += delta;
			Assets.yellowTankCurrentFrame = Assets.yellowTankAnimation.getKeyFrame(stateTime, true);
			boxDirection = 2;
			y = (int) Math.round(y + speed * delta);
			if(y > 600) y = 600;	
			boxRect.setY(y);
			collisionBottom = false;
		}
	}
	
	
	public void moveRight(float delta){
		if(!collisionRight){
			stateTime += delta;
			Assets.yellowTankCurrentFrame = Assets.yellowTankAnimation.getKeyFrame(stateTime, true);
			boxDirection = 3;
			x = (int) Math.round(x + speed * delta);
			if(x > 600) x = 600;
			boxRect.setX(x);
			collisionLeft = false;
		}
	}
	
	public void moveLeft(float delta){
		if(!collisionLeft){
			stateTime += delta;
			Assets.yellowTankCurrentFrame = Assets.yellowTankAnimation.getKeyFrame(stateTime, true);
			boxDirection = 1;
			x = (int) Math.round(x - speed * delta);
			if(x < 0) x = 0;
			boxRect.setX(x);
			collisionRight = false;
		}
	}
	
	public boolean isCollision(Rectangle rect){
		boolean collision = this.boxRect.overlaps(rect);
		if(!collision){
			collisionTop = false; collisionBottom = false; collisionLeft = false; collisionRight = false;

		}else System.out.println("its still overlapping");			
				collisionTop = collision && boxDirection == 2;
				if(collisionTop){
					y = (int)rect.y - (int)(boxRect.height);
					boxRect.setY(y);
				}			
			
			
				collisionBottom = collision && boxDirection == 4;
				if(collisionBottom){
					y = (int)rect.y + (int)rect.height;
					boxRect.setY(y);
				
			}
			

				collisionLeft = collision && boxDirection == 1;
				if(collisionLeft){
					x = (int)rect.x + (int)rect.width;
					boxRect.setX(x);
				}

		

				collisionRight = collision && boxDirection == 3;
				if(collisionRight){
					x = (int)rect.x - (int)(boxRect.width);
					boxRect.setX(x);
				}
			
		return collision;
	}
	
	public Sprite draw(SpriteBatch batch){
		
		//frame.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		Sprite mySprite = new Sprite();
		mySprite.setRegion(Assets.yellowTankCurrentFrame);
		mySprite.setOrigin(25, 25);
		switch(boxDirection){
			case 1:
				mySprite.setRotation(90f);
				break;
			case 2:
				mySprite.setRotation(0f);
				break;
			case 3:
				mySprite.setRotation(270f);
				break;
			case 4:
				mySprite.setRotation(180f);
				break;
		}
		//mySprite.setRotation(0f);
		
		mySprite.setPosition(boxRect.x, boxRect.y);
		mySprite.setSize(50, 50);
		System.out.println(stateTime);
		//mySprite.draw(batch);
		mySprite.draw(batch);
		return mySprite;
		//mySprite.
		
	}
	
	
}
