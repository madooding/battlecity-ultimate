package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.GameObject.objStatus;

public class GameObject implements Movable {
	Rectangle rect = new Rectangle();
	static public enum objStatus {alive, freeze, died};
	public objStatus status = objStatus.alive;
	public int speed = 150;
	boolean collisionTop = false, collisionBottom = false, collisionLeft = false, collisionRight = false;
	protected float stateTime = 0f;
	protected Sprite currentFrame = new Sprite();
	private int tempX = 0, tempY = 0;
	public ObjDirection objDirection = ObjDirection.UP;
	
	
	public GameObject(){
		rect.x = 0;
		rect.y = 0;
		rect.width = 50;
		rect.height = 50;
	}
	
	
	public GameObject(int x, int y){
		rect.x = x;
		rect.y = y;
		rect.width = 50;
		rect.height = 50;
		
	}
	
	public GameObject(int x, int y, int width, int height){
		rect.x = x;
		rect.y = y;
		rect.width = width;
		rect.height = height;		
	}
	
	@Override
	public void moveDown(float delta) {
		// TODO Auto-generated method stub
		objDirection = ObjDirection.DOWN;
		if(!collisionBottom){
			stateTime += delta;
			rect.y = (int) Math.round(rect.y - speed * delta);
			if(rect.y < 0) rect.y = 0;
			collisionTop = false;
			
		}
	}
	@Override
	public void moveUp(float delta) {
		objDirection = ObjDirection.UP;
		if(!collisionTop){
			stateTime += delta;
			rect.y = (int) Math.round(rect.y + speed * delta);
			if(rect.y > 650 - rect.height) rect.y = 650 - rect.height;
			collisionBottom = false;
		}
		
	}

	@Override
	public void moveLeft(float delta) {
		// TODO Auto-generated method stub
		objDirection = ObjDirection.LEFT;
		if(!collisionLeft){
			stateTime += delta;
			rect.x = (int) Math.round(rect.x - speed * delta);
			if(rect.x < 0) rect.x = 0;
			collisionRight = false;
		}
	}

	@Override
	public void moveRight(float delta) {
		// TODO Auto-generated method stub
		objDirection = ObjDirection.RIGHT;
		if(!collisionRight){
			stateTime += delta;
			rect.x = (int) Math.round(rect.x + speed * delta);
			if(rect.x > 650 - rect.height) rect.x = 650 - rect.height;
			collisionLeft = false;
			
		}
	}
	
	@Override
	public void shoot(){
		//Blank method
	}
	
	@Override
	public void shoot2(){
		//Blank method
	}
	
	public boolean isCollision(Rectangle boxRect){
		boolean collision = this.rect.overlaps(boxRect);
		if(!collision){
			collisionTop = false; collisionBottom = false; collisionLeft = false; collisionRight = false;
			
		}
		
		collisionTop = collision && objDirection == ObjDirection.UP;
		collisionBottom = collision && objDirection == ObjDirection.DOWN;
		collisionLeft = collision && objDirection == ObjDirection.LEFT;
		collisionRight = collision && objDirection == ObjDirection.RIGHT;
		
		if(collisionTop) rect.y = (int)boxRect.y - (int)rect.height;
		if(collisionBottom) rect.y = (int)boxRect.y + (int)boxRect.height;
		if(collisionLeft) rect.x = (int)boxRect.x + (int)boxRect.width;
		if(collisionRight) rect.x = (int)boxRect.x - (int)rect.width;
		
		return collision;
		}
	
	public boolean canMoveLeft(Rectangle r){
		tempX = (int)rect.x;
		rect.x = (int)Math.round(rect.x - speed * Gdx.graphics.getDeltaTime());
		boolean t = rect.overlaps(r);
		if(t)
			rect.x = r.x + rect.height;
		rect.x = tempX;
		return t;
	}
	
	public boolean canMoveDown(Rectangle r){
		tempY = (int)rect.y;
		rect.y = (int)Math.round(rect.y - speed * Gdx.graphics.getDeltaTime());
		boolean t = rect.overlaps(r);
		if(t)
			rect.y = r.y + rect.height;
		rect.y = tempY;
		return t;
	}
	
	public boolean canMoveRight(Rectangle r){
		tempX = (int)rect.x;
		rect.x = (int)Math.round(rect.x + speed * Gdx.graphics.getDeltaTime());
		boolean t = rect.overlaps(r);
		if(t)
			rect.x = r.x - rect.height;
		rect.x = tempX;
		return t;
	}
	
	public boolean canMoveUp(Rectangle r){
		tempY = (int)rect.y;
		rect.y = (int)Math.round(rect.y + speed * Gdx.graphics.getDeltaTime());
		boolean t = rect.overlaps(r);
		if(t)
			rect.y = r.y - rect.height;
		rect.y = tempY;
		return t;
	}
	
	public void cycle(){
		
	}
	
	public void setX(int x){
		rect.x = x;
	}
	
	public void setY(int y){
		rect.y = y;
	}
	
	public void setWidth(int width){
		rect.width = width;
	}
	
	public void setHeight(int height){
		rect.height = height;
	}
	
	public void setPosition(int x, int y){
		rect.setPosition(x, y);
	}

	public void setDirection(ObjDirection direction){
		objDirection = direction;
	}
	
	
	public int getX(){
		return (int)rect.x;
	}
	
	public int getY(){
		return (int)rect.y;
	}
	
	public int getWidth(){
		return (int)rect.width;		
	}
	
	public int getHeight(){
		return (int)rect.height;
	}


	public void shoot2(Array<Turtle> turtleArray) {
		// TODO Auto-generated method stub
		
	}
	


}
