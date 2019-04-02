package com.mygdx.game;

import com.badlogic.gdx.math.Rectangle;

public class Diamond {
	public Rectangle rect;
	public Tank.color owner;
	public boolean alive = true;
	
	public Diamond(Tank.color owner){
		this.owner = owner;
		rect = new Rectangle();
		rect.width = 50;
		rect.height = 50;
		if(owner == Tank.color.yellow){
			rect.x = 300;
			rect.y = 0;
		}else if(owner == Tank.color.green){
			rect.x = 300;
			rect.y = 600;
		}
	}
	
	public boolean overlaps(Bullet bullet){
		return bullet.isCollision(rect);
	}
	
	
	public int getX(){
		return (int)rect.x;
	}
	
	public int getY(){
		return (int)rect.y;
	}
	
	public void destroy(){
		alive = false;
		rect.x = 888;
		rect.y = 888;
	}
}
