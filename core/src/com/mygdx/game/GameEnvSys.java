package com.mygdx.game;

import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Array;

public class GameEnvSys {
	public static enum gameStatuses {credit, main, howToPlay, loadEnv, stageLevelShow, gamePlay, scoreResult, endOfGame};
	public gameStatuses gameStatus = gameStatuses.credit;
	public int gameStage = 1;
	public Tank yellowTank;
	public Tank greenTank;
	public Array<Bullet> bullets;
	public Array<CellXY> collisionObjects;
	public Array<ExplosionFx> explosion;
	public int greenTankResultScore = 0;
	public int yellowTankResultScore = 0;
	public int blueDiamondDestroyed = 0;
	public int redDiamondDestroyed = 0;
	public Crate crate;
	protected boolean wonTheStage = false;
	//public Movable test;
	private long startTime = System.currentTimeMillis();
	public Array<CrateText> crateTextArray;
	public Array<Turtle> turtleArray;
	public Diamond redDiamond;
	public Diamond blueDiamond;
	
	
	
	public void load(){
		if(gameStage > Assets.maps.size){
			gameStatus = gameStatuses.endOfGame;
			return;
		}
		MapsControl.set(gameStage - 1);
		wonTheStage = false;
		yellowTank = new Tank();
		greenTank = new Tank(600, 600, Tank.color.green);
		crate = new Crate();
		bullets = new Array(false, 0 , Bullet.class);
		collisionObjects = new Array();
		explosion = new Array();
		crateTextArray = new Array();
		crate.setTextContainer(crateTextArray);
		redDiamond = new Diamond(Tank.color.yellow);
		blueDiamond = new Diamond(Tank.color.green);
		turtleArray = new Array();
		
		for(int i = 0; i < MapsControl.currentLayer.getHeight(); i++)
			for(int j = 0; j < MapsControl.currentLayer.getWidth(); j++){
				if(MapsControl.currentLayer.getCell(i, j) != null){
					CellXY cell = new CellXY(MapsControl.currentLayer.getCell(i, j), i, j);
					collisionObjects.add(cell);
				}
					
			}
		this.gameStatus = GameEnvSys.gameStatuses.stageLevelShow;
	}
	
	public void process(float delta){
		Iterator<CellXY> iter = collisionObjects.iterator();
		Bullet[] b = bullets.toArray();
		
		//win condition
		if(wonTheStage){
			long nowTime = System.currentTimeMillis();
			if(Useful.elapsedTime(startTime, nowTime) > 5){
				this.gameStatus = GameEnvSys.gameStatuses.loadEnv;
				yellowTankResultScore += yellowTank.score;
				greenTankResultScore += greenTank.score;
			}
		}else if(yellowTank.life == 0 || greenTank.life == 0 || !redDiamond.alive || !blueDiamond.alive){
			wonTheStage = true;
			gameStage++;
			startTime = System.currentTimeMillis();
			if(!redDiamond.alive)
				redDiamondDestroyed += 1;
			if(!blueDiamond.alive)
				blueDiamondDestroyed += 1;
			Assets.endOfGameSound.play();
			return;
		}
		
		//Diamond
		Diamond diamond[] = {blueDiamond, redDiamond};
		for(Diamond item : diamond){
			greenTank.isCollision(item.rect);
			yellowTank.isCollision(item.rect);
		}
		

		
		//Crate
		crate.toDo();
		crate.overlaps(yellowTank);
		crate.overlaps(greenTank);
		
		//Tank
		yellowTank.cycle();
		greenTank.cycle();
		System.out.println("yellow Tank x : " + yellowTank.getX() + " y : " + yellowTank.getY());
		System.out.println("Green tank score : " + greenTank.score);
		System.out.println("Yellow tank score : " + yellowTank.score);
		if(yellowTank.secondaryWeaponStatus == Tank.secondaryWeapon.marioTurtle){
			System.out.println("mario turtleeeeeeee");
		}
		
		//Turtle
		Turtle[] turtleArray2 = turtleArray.toArray(Turtle.class);
		for(Turtle turtle : turtleArray){
			System.out.println("Turtle Cycle");
			for(Turtle turtle2 : turtleArray2){
				if(!turtle2.equals(turtle))
					turtle.overlaps(turtle2);
			}
			if(turtle.overlaps(yellowTank) && yellowTank.status != Tank.objStatus.immune){
				if(turtle.ownerColor == Tank.color.green)
					greenTank.score++;
				explosion.add(new ExplosionFx(yellowTank.getX(), yellowTank.getY()));
				yellowTank.die();			
			}
			if(turtle.overlaps(greenTank) && greenTank.status != Tank.objStatus.immune){
				if(turtle.ownerColor == Tank.color.yellow)
					yellowTank.score++;
				explosion.add(new ExplosionFx(greenTank.getX(), greenTank.getY()));
				greenTank.die();
			}
			turtle.cycle();
			if(!turtle.alive)
				turtleArray.removeValue(turtle, true);
			turtle.overlaps(crate);
		}
		
		//Bullet collision
		for(Bullet bullet : bullets){
			boolean temp = false;
			for(Bullet bullet2 : b){
				if(!bullet.equals(bullet2) && bullet.rect.overlaps(bullet2.rect)){
					bullets.removeValue(bullet2, true);
					temp = true;
					continue;
				}
			}
			for(Turtle turtle : turtleArray){
				if(turtle.overlaps(bullet))
					bullets.removeValue(bullet, true);
			}
			
			if(temp){
				bullets.removeValue(bullet, true);
				continue;
			}
			//Bullets collision with diamond
			if(redDiamond.overlaps(bullet)){
				bullets.removeValue(bullet, true);
				greenTank.score += 3;
				explosion.add(new ExplosionFx(redDiamond.getX(), redDiamond.getY()));
				redDiamond.destroy();
			}
			if(blueDiamond.overlaps(bullet)){
				bullets.removeValue(bullet, true);
				yellowTank.score += 3;
				explosion.add(new ExplosionFx(blueDiamond.getX(), blueDiamond.getY()));
				blueDiamond.destroy();
			}
			
			
			//Bullets collision with tank checking
			if(yellowTank.rect.overlaps(bullet.rect) && bullet.bulletOwner == Tank.color.green){
				bullets.removeValue(bullet, true);
				if(yellowTank.status != Tank.objStatus.immune){
					explosion.add(new ExplosionFx(yellowTank.getX(), yellowTank.getY())); greenTank.score++;
				}
				yellowTank.die();				
			}
			
			if(greenTank.rect.overlaps(bullet.rect) && bullet.bulletOwner == Tank.color.yellow){
				bullets.removeValue(bullet, true);
				if(greenTank.status != Tank.objStatus.immune){
					explosion.add(new ExplosionFx(greenTank.getX(), greenTank.getY())); yellowTank.score++;
				}
				greenTank.die();
			}
			
			if(crate.overlaps(bullet)){
				bullets.removeValue(bullet, true);
			}
		}

		
		
				while(iter.hasNext()){
					boolean temp = false;
					CellXY cell = iter.next();
					yellowTank.isCollision(cell.rect);
					greenTank.isCollision(cell.rect);
					for(Turtle turtle : turtleArray){
						turtle.overlaps(cell);
					}
					for(Bullet bullet : bullets){
						if(bullet.isCollision(cell.rect)){
							switch(bullet.bulletPower){
								case superBullet:
									if(!cell.cell.getTile().getProperties().containsKey("water")){
										MapsControl.currentLayer.setCell(cell.x, cell.y, null);
										iter.remove();
										bullets.removeValue(bullet, true);
										Assets.hitSound.play();
									}
									break;
								default : if(cell.cell.getTile().getProperties().containsKey("brick")){
									System.out.println("Brick shot");
									MapsControl.currentLayer.setCell(cell.x, cell.y, null);
									iter.remove();
									bullets.removeValue(bullet, true);
									Assets.hitSound.play();
								} else if(cell.cell.getTile().getProperties().containsKey("steel")){
									bullets.removeValue(bullet, true);
									Assets.hitSound.play();
								}
							}//End of switch
							
							
							
						} // End of checking bullets collision with tile
						
					}
					

				}
				
				Iterator<ExplosionFx> ex_iter = explosion.iterator();
				while(ex_iter.hasNext()){
					ExplosionFx ex = ex_iter.next();
					if(ex.isDone()){
						ex_iter.remove();
					}
				}
	}
	
	
	
	public void resetGame(){
		this.gameStage = 1;
		this.gameStatus = gameStatuses.main;
		//Reset map
		Assets.loadMaps();
	}
	

	
	
}
