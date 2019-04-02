package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class Turtle extends GameObject{
	private MyAnimation turtleAnimation;
	public Tank.color ownerColor;
	public Sprite currentFrame;
	private long startTime = System.currentTimeMillis();
	private float durationTime = 4;
	private boolean immuneForOwner = true;
	public int speed = 300;
	public boolean alive = true;
	ObjDirection direction;
	
	public Turtle(){
		super();
		rect.width = 50;
		rect.height = 50;
	}
	
	public Turtle(int x, int y, ObjDirection direction, Tank.color ownerColor){
		super();
		rect.x = x;
		rect.y = y;
		rect.width = 50;
		rect.height = 50;
		this.ownerColor = ownerColor;
		this.direction = direction;
		turtleAnimation = new MyAnimation(0.05f, Assets.turtle);
		super.speed = 300;
	}
	
	public void cycle(){
		long nowTime = System.currentTimeMillis();
		float delta = Gdx.graphics.getDeltaTime();
		if(Useful.elapsedTime(startTime, nowTime) > durationTime)
			alive = false;
		switch(direction){
			case LEFT:
				moveLeft(delta);
				if(rect.x == 0)
					direction = ObjDirection.RIGHT;
				break;
			case RIGHT:
				moveRight(delta);
				if(rect.x == 650 - rect.height)
					direction = ObjDirection.LEFT;
				break;
			case UP:
				moveUp(delta);
				if(rect.y == 650 - rect.height)
					direction = ObjDirection.DOWN;
				break;
			case DOWN:
				moveDown(delta);
				if(rect.y == 0)
					direction = ObjDirection.UP;
				break;
		}
	}
	
	public TextureRegion getCurrentFrame(){
		return turtleAnimation.getKeyFrame(true);
	}
	
	public boolean overlaps(Bullet bullet){
		return bullet.isCollision(rect);
	}
	
	public boolean overlaps(Tank tank){
		boolean isOverlap = rect.overlaps(tank.rect);
		if(tank.tankColor == this.ownerColor){
			if(immuneForOwner){
				if(!isOverlap)
					immuneForOwner = false;
				else return false;
			}
		}
		return isOverlap;
	}
	
	public boolean overlaps(CellXY cell){
		boolean isOverlap = rect.overlaps(cell.rect);
		if(isOverlap){
			switch(direction){
				case LEFT:
					direction = ObjDirection.RIGHT;
					rect.x += 5;
					break;
				case RIGHT:
					direction = ObjDirection.LEFT;
					rect.x -= 5;
					break;
				case UP:
					direction = ObjDirection.DOWN;
					rect.y -= 5;
					break;
				case DOWN:
					direction = ObjDirection.UP;
					rect.y += 5;
					break;
			}
		}
		return isOverlap;
	}
	
	public boolean overlaps(Turtle turtle){
		boolean isOverlap = rect.overlaps(turtle.rect);
		if(isOverlap){
			switch(direction){
				case LEFT:
					direction = ObjDirection.RIGHT;
					rect.x += 12;
					break;
				case RIGHT:
					direction = ObjDirection.LEFT;
					rect.x -= 12;
					break;
				case UP:
					direction = ObjDirection.DOWN;
					rect.y -= 12;
					break;
				case DOWN:
					direction = ObjDirection.UP;
					rect.y += 12;
					break;
			}
		}
		return isOverlap;
	}
	
	public boolean overlaps(Crate crate){
		boolean isOverlap = rect.overlaps(crate.rect);
		if(isOverlap){
			switch(direction){
				case LEFT:
					direction = ObjDirection.RIGHT;
					rect.x += 12;
					break;
				case RIGHT:
					direction = ObjDirection.LEFT;
					rect.x -= 12;
					break;
				case UP:
					direction = ObjDirection.DOWN;
					rect.y -= 12;
					break;
				case DOWN:
					direction = ObjDirection.UP;
					rect.y += 12;
					break;
			}
		}
		return isOverlap;
	}
	
	public int getX(){
		return (int)rect.x;
	}
	
	public int getY(){
		return (int)rect.y;
	}
}
