package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Bullet extends GameObject {
	
	protected Sprite currentFrame;
	public Tank.color bulletOwner;
	public Tank.primaryWeapon bulletPower = Tank.primaryWeapon.oneShot;
	public boolean outOfMap = false;
	int speed = 200;
	
	public Bullet(){
		super();
		rect.width = 6;
		rect.height = 6;
	}
	
	public Bullet(Tank.color bulletOwner, int x, int y, ObjDirection direction){
		super(x, y);
		rect.width = 6;
		rect.height = 6;
		this.setDirection(direction);
		this.bulletOwner = bulletOwner;
	}
	
	public Bullet(Tank.color bulletOwner, int x, int y, int width, int height, ObjDirection direction){
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;
		this.setDirection(direction);
		this.bulletOwner = bulletOwner;
	}
	
	@Override
	public void moveDown(float delta) {
		// TODO Auto-generated method stub

			objDirection = ObjDirection.DOWN;
			rect.y = (int) Math.round(rect.y - speed * delta);
			if(rect.y < 0){ rect.y = 0;  outOfMap = true; Assets.hitSound.play();}

			
		
	}
	@Override
	public void moveUp(float delta) {


			objDirection = ObjDirection.UP;
			rect.y = (int) Math.round(rect.y + speed * delta);
			if(rect.y > 650 - rect.height){ rect.y = 650 - rect.height; outOfMap = true; Assets.hitSound.play();}


		
	}

	@Override
	public void moveLeft(float delta) {
		// TODO Auto-generated method stub


			objDirection = ObjDirection.LEFT;
			rect.x = (int) Math.round(rect.x - speed * delta);
			if(rect.x < 0){ rect.x = 0; outOfMap = true; Assets.hitSound.play();}

	}

	@Override
	public void moveRight(float delta) {
		// TODO Auto-generated method stub

			objDirection = ObjDirection.RIGHT;
			rect.x = (int) Math.round(rect.x + speed * delta);
			if(rect.x > 650 - rect.height){ rect.x = 650 - rect.height; outOfMap = true; Assets.hitSound.play();}
			
	}
	
	public void draw(SpriteBatch batch){
		currentFrame = Assets.bullet;
		currentFrame.setOriginCenter();
		switch(objDirection){
			case LEFT:
				currentFrame.setRotation(90f);
				break;
			case UP:
				currentFrame.setRotation(0f);
				break;
			case RIGHT:
				currentFrame.setRotation(270f);
				break;
			case DOWN:
				currentFrame.setRotation(180f);
				break;
		}
		currentFrame.setPosition(rect.x, rect.y);
		currentFrame.setSize(rect.height, rect.height);
		currentFrame.draw(batch);
	}
	
	public void doSomething(float delta){
		System.out.println(objDirection);
		switch(objDirection){
			case UP:
				this.moveUp(delta);
				break;
			case DOWN:
				this.moveDown(delta);
				break;
			case LEFT:
				this.moveLeft(delta);
				break;
			case RIGHT:
				this.moveRight(delta);
				break;
		}
		//System.out.println("Bullet axis x : " + rect.x + " y : " + rect.y + " width : " + rect.width);
		
	}
	
	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	@Override
	public boolean isCollision(Rectangle boxRect){
		return this.rect.overlaps(boxRect);
	}
}
