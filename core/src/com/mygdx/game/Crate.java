package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Crate {
	public Rectangle rect = new Rectangle();
	boolean hide = true;
	private long startTime;
	private float duration = 8;
	private MyAnimation crateAnimation;
	private Array<CrateText> crateTextArray;
	
	
	public Crate(){
		rect.width = 50;
		rect.height = 50;
		this.hide();
		crateAnimation = new MyAnimation(0.05f, Assets.boxTexture);
		startTime = System.currentTimeMillis();
	}
	
	
	public void hide(){
		rect.x = 999;
		rect.y = 999;
		hide = true;
		duration = 8;
	}
	
	public TextureRegion getCurrentFrame(){
		return crateAnimation.getKeyFrame(true);
	}
	
	public void toDo(){
		long nowTime = System.currentTimeMillis();
		if((nowTime - startTime) / 1000 > duration){
			System.out.println("15 seconds elapsed !");
			if(hide)
				this.spawn();
			else
				this.hide();
			startTime = nowTime;
		}
	}
	
	public void spawn(){
		int x = 0;
		int y = 0;
		
		while(true){
			x = ((int)(Math.random()*100) % 25);
			y = ((int)(Math.random()*100) % 25);
			System.out.println("Test spawn at x : " + x + " y : " + y);
			if(MapsControl.currentLayer.getCell(x, y) == null && (x != 12 && y != 0) && (x != 13 && y != 0)
					&& (x != 12 && y != 1) && (x != 13 && y != 1) && (x != 12 && y != 24) && (x != 13 && y != 24
					&& (x != 12 && y != 25) && (x != 13 && y != 25))){
				rect.x = x * 25;
				rect.y = y * 25;
				hide = false;
				crateAnimation = new MyAnimation(0.05f, Assets.boxTexture);
				duration = 5;
				break;
			}
		}
	}
	
	public void overlaps(Tank tank){
		String text = "";
		if(!hide)
			if(rect.overlaps(tank.rect)){
				startTime = System.currentTimeMillis();
				Assets.pickupSound.play();
				if(tank.primaryWeaponLevel == Tank.primaryWeapon.superBullet || Math.random() > 0.4){
					if(Math.random() >= 0.7){
						tank.life ++;
						text = "+Life !";
					}else{
						System.out.println("You've got turtle");
						tank.secondaryWeaponStatus = Tank.secondaryWeapon.marioTurtle;
					text = "Turtle";
					}
				}else{
						switch(tank.primaryWeaponLevel){
							case oneShot:
								tank.primaryWeaponLevel = Tank.primaryWeapon.speed;
								break;
							case speed:
								tank.primaryWeaponLevel = Tank.primaryWeapon.twoShot;
								break;
							case twoShot:
								tank.primaryWeaponLevel = Tank.primaryWeapon.superBullet;
								break;
							default:
								break;
						} // End of switch
						text = "Upgrade!";
					
					
				}
				System.out.println("Overlaped box at x : " + rect.x);
				this.crateTextArray.add(new CrateText(text, rect.x, rect.y));				
				this.hide();

			}
	}
	
	public boolean overlaps(Bullet bullet){
		boolean isOverlap = rect.overlaps(bullet.rect);
		if(isOverlap)
			Assets.hitSound.play();
		return isOverlap;
	}
	
	public void setTextContainer(Array<CrateText> crateTextArray){
		this.crateTextArray = crateTextArray;
	}
	
}
