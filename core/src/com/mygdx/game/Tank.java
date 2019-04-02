package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Tank extends GameObject{
	public static enum color { yellow, green };
	public static enum primaryWeapon { oneShot, speed, twoShot, superBullet};
	public static enum secondaryWeapon { none, marioTurtle };
	public static enum objStatus {alive, immune, freeze, died};
	public objStatus status = objStatus.immune;
	public color tankColor = color.yellow;
	public int life = 3;
	protected primaryWeapon primaryWeaponLevel = primaryWeapon.oneShot;
	protected secondaryWeapon secondaryWeaponStatus = secondaryWeapon.none;
	private int tempX = 0, tempY = 0;
	protected Sprite currentFrame;
	private long startTime = System.currentTimeMillis();
	private float durationTime = 3;
	public int score = 0;
	private int decisCounter = 0;
	private long millisCounter = System.currentTimeMillis();
	
	public Tank(){
		super();
		rect.width = 50;
		rect.height = 50;
	}
	public Tank(color tankColor){
		super();
		rect.width = 50;
		rect.height = 50;
		this.tankColor = tankColor;
	}
	
	public Tank(int x, int y){
		super(x, y);
		rect.width = 50;
		rect.height = 50;
	}
	
	public Tank(int x, int y, color tankColor){
		super(x, y);
		rect.width = 50;
		rect.height = 50;
		this.tankColor = tankColor;
		if(tankColor == color.green)
			objDirection = ObjDirection.DOWN;
	}
	
	public void moveDown(float delta){
		updateCurrentFrame();
		
		if(status != objStatus.died && status != objStatus.freeze){
			if(this.objDirection == ObjDirection.LEFT)
				rect.x = subDown((int)rect.x);
			else if(this.objDirection == ObjDirection.RIGHT)
				rect.x = plusUp((int)rect.x);
			super.moveDown(delta);
		}
	}
	
	public void moveUp(float delta){
		updateCurrentFrame();
		if(status != objStatus.died && status != objStatus.freeze){
			if(this.objDirection == ObjDirection.LEFT)
				rect.x = subDown((int)rect.x);
			else if(this.objDirection == ObjDirection.RIGHT)
				rect.x = plusUp((int)rect.x);
			super.moveUp(delta);
		}
	}
	
	public void moveLeft(float delta){
		updateCurrentFrame();
		if(status != objStatus.died && status != objStatus.freeze){
			if(this.objDirection == ObjDirection.UP)
				rect.y = this.plusUp((int)rect.y);
			else if(this.objDirection == ObjDirection.DOWN)
				rect.y = this.subDown((int)rect.y);
			super.moveLeft(delta);
		}
	}
	
	public void moveRight(float delta){
		updateCurrentFrame();
		if(status != objStatus.died && status != objStatus.freeze){
			if(this.objDirection == ObjDirection.UP)
				rect.y = plusUp((int)rect.y);
			else if(this.objDirection == ObjDirection.DOWN)
				rect.y = subDown((int)rect.y);
			super.moveRight(delta);
		}
	}
	
	
	public void draw(SpriteBatch batch){
		currentFrame = new Sprite();
		switch(tankColor){
			case yellow:
				currentFrame.setRegion(Assets.yellowTankCurrentFrame);
				break;
			case green:
				currentFrame.setRegion(Assets.greenTankCurrentFrame);
				break;
		}
		
		currentFrame.setOrigin(25, 25);
		switch(objDirection){
			case LEFT:
				currentFrame.setRotation(90f);
				break;
			case UP:
				currentFrame.setRotation(0);
				break;
			case RIGHT:
				currentFrame.setRotation(270f);
				break;
			case DOWN:
				currentFrame.setRotation(180f);
				break;
		}
		
		currentFrame.setPosition(rect.x, rect.y);
		currentFrame.setSize(rect.width, rect.height);
		if(this.status != objStatus.immune)
			currentFrame.draw(batch);
		else
			if(decisCounter % 5 == 0)
				currentFrame.draw(batch);
		
	}
	
	private void updateCurrentFrame(){
			switch(tankColor){
				case  yellow:
					Assets.yellowTankCurrentFrame = Assets.yellowTankAnimation.getKeyFrame(stateTime, true);
					break;
				case green:
					Assets.greenTankCurrentFrame = Assets.greenTankAnimation.getKeyFrame(stateTime, true);
					break;
			}

	}	
	
	public void shoot(Array<Bullet> bullets){
		if(this.status != objStatus.died && this.status != objStatus.freeze){
			int toFind = 1;
			int found = 0;
			int speedUp = 0;
			switch(primaryWeaponLevel){
				case superBullet:
				case twoShot:
					toFind = 2;
				case speed:
					speedUp = 80;
					break;
				case oneShot:
					toFind = 1;
					break;
					
			}
			for(Bullet bullet: bullets){
				if(bullet.bulletOwner == this.tankColor)
					found++;
			}
			
			if(found < toFind){
				bullets.add(new Bullet(this.tankColor, (int)rect.x + 23, (int)rect.y + 23, this.objDirection));
				bullets.peek().bulletPower = this.primaryWeaponLevel;
				bullets.peek().speed += speedUp;
				Assets.shootSound.play();
			}
		}
	}
	
	@Override
	public void shoot2(Array<Turtle> turtleArray){
		super.shoot2();
		if(this.status != objStatus.died && this.status != objStatus.freeze)
			switch(this.secondaryWeaponStatus){
				case marioTurtle:
					System.out.println("You've shot a turtle");
					turtleArray.add(new Turtle((int)rect.x, (int)rect.y, this.objDirection, this.tankColor));
					secondaryWeaponStatus = secondaryWeapon.none;
					Assets.shellSound.play();
					break;
			}
	}
	
	
	public void die(){
		if(this.status != Tank.objStatus.immune){
			this.status = objStatus.died;
			this.primaryWeaponLevel = primaryWeapon.oneShot;
			this.life--;
			setPosition(-999, -999);
			startTime = System.currentTimeMillis();
			durationTime = 3;
			this.secondaryWeaponStatus = secondaryWeapon.none;
			switch(this.tankColor){
				case yellow:
					objDirection = ObjDirection.UP;
					break;
				case green:
					objDirection = ObjDirection.DOWN;
					break;
			}
		}
	}
	
	public void beget(){
		if(life > 0){
			startTime = System.currentTimeMillis();
			durationTime = 3;
			this.status = objStatus.immune;
			if(tankColor == color.yellow)
				setPosition(0, 0);
			else
				setPosition(600, 600);
		}
	}
	
	public void freeze(){
		this.status = objStatus.freeze;
		startTime = System.currentTimeMillis();
		durationTime = 5;
	}
	
	@Override
	public void cycle(){
		timerCounter();
		long nowTime = System.currentTimeMillis();
		if(Useful.elapsedTime(startTime, nowTime) > durationTime){
			switch(status){
				case died:
					this.beget();
					durationTime = 3;
					break;
				case immune:
					status = objStatus.alive;
					break;
				case freeze:
					status = objStatus.alive;
					break;
			}
			startTime = nowTime;
		}
	}
	
	
	public void checkMovable(Rectangle r){
		collisionLeft = this.canMoveLeft(r);
		collisionBottom = this.canMoveDown(r);
		collisionRight = this.canMoveRight(r);
		collisionTop = this.canMoveUp(r);
	}
	
	private int plusUp(int i){
		int divided = i % 25;
		if(divided >= 16)
			return i + (25 - divided);
		else return i;
	}
	
	private int subDown(int i){
		int divided = i % 25;
		if(divided <= 9)
			return i - divided;
		else return i;
	}
	
	private void timerCounter(){
		long nowTime = System.currentTimeMillis();
		if((nowTime - millisCounter) / 100 >= 1){
			decisCounter += 1;
			decisCounter %= 600; 
			millisCounter = nowTime;
			System.out.println("Time counter : " + decisCounter / 10);
		}
		
		System.out.println("Time counter : " + decisCounter);
	}
	
}
