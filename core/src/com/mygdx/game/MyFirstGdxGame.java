package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.Layout;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Align;


public class MyFirstGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera camera;
	Tank yellowTank;
	Tank greenTank;
	Array<Bullet> bullets;	
	Array<CellXY> collisionObjects;
	GameEnvSys gameEnv;
	GlyphLayout layout;
	float x = 0, y = -650;
	private long startTime = System.currentTimeMillis(), nowTime = 0;
	float delta;
	float stateTime;
	int pointer = 1;
	
	private long diff, start = System.currentTimeMillis();
	
	
	CrateText cText;


	@Override
	public void create () {
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 950, 650);
		camera.position.x -= 150;
		camera.update();
		

		//Load assets
		Assets.load();
		Assets.loadMaps();
		layout = new GlyphLayout();
		layout.setText(Assets.font80, "Hello");
		cText = new CrateText("Hello", 0 ,0);
		
		//Load and set map;
		MapsControl.set();
		

		
		//set and load envirionment
		gameEnv = new GameEnvSys();
		
		
		//State time
		stateTime = 0F;
		
		Assets.yellowTankCurrentFrame =  Assets.yellowTankAnimation.getKeyFrame(stateTime, true);
		Assets.greenTankCurrentFrame = Assets.greenTankAnimation.getKeyFrame(stateTime, true);
		
		batch = new SpriteBatch();		
		
	}

	@Override
	public void render () {
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		
		System.out.println(MapsControl.currentLayer.getHeight());
		//Timing
		delta = Gdx.graphics.getDeltaTime();
		stateTime += delta;
		MapsControl.stateTime = stateTime;
		camera.update();				
		batch.setProjectionMatrix(camera.combined);
		
		switch(gameEnv.gameStatus)
		{
		
			case credit:
				if(Useful.elapsedTime(startTime, System.currentTimeMillis()) > 5){
					gameEnv.gameStatus = GameEnvSys.gameStatuses.main;
				}else{
					batch.begin();
					batch.draw(Assets.members, 0, 0);
					batch.end();
				}
				break;
			case main:
				batch.begin();
				batch.draw(Assets.splash, 0, y);
				if(y < 0){
					y += 200 * Gdx.graphics.getDeltaTime();
				}else{
					//System.out.println("Yeah");
					layout.setText(Assets.font80, "Start game");
					Assets.font80.draw(batch, layout, 325 - layout.width / 2f, 200);
					layout.setText(Assets.font80, "How to play");
					Assets.font80.draw(batch, layout, 325 - layout.width / 2f, 160);
					switch(pointer){
						case 1:
							batch.draw(Assets.yellowTank[0], 140, 205, 0, 0, 35, 35, 1, 1, 270);
							break;
						case 2:
							batch.draw(Assets.yellowTank[0], 140, 165, 0, 0, 35, 35, 1, 1, 270);
							break;
						}
					if(Gdx.input.isKeyJustPressed(Keys.DOWN) || Gdx.input.isKeyJustPressed(Keys.UP)){
						Assets.selectSound.play();
						if(pointer == 1)
							pointer = 2;
						else
							pointer = 1;
					}
					if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
						Assets.selectSound.play();
						if(pointer == 1)
							gameEnv.gameStatus = GameEnvSys.gameStatuses.loadEnv;
						else gameEnv.gameStatus = GameEnvSys.gameStatuses.howToPlay;
					}
					//Assets.font80.
				}
				batch.end();
				break;
			case loadEnv:
				gameEnv.load();
				break;
				
			case stageLevelShow:
				batch.begin();
				layout.setText(Assets.font80, "Stage " + gameEnv.gameStage);
				Assets.font80.draw(batch, layout, 325 - layout.width / 2f, 325);				
				batch.end();
				if(Gdx.input.isKeyJustPressed(Keys.SPACE))
					gameEnv.gameStatus = GameEnvSys.gameStatuses.gamePlay;
				break;
			case gamePlay:
				if(MapsControl.currentLayer.getCell(0, 0) == null){
					System.out.println("null");
				}
				
				
				//Assets process
				yellowTank = gameEnv.yellowTank;
				greenTank = gameEnv.greenTank;
				
				//Keyboard detect
				yellowTank.checkMovable(greenTank.rect);
				
				if(Gdx.input.isKeyPressed(Keys.UP)){ 
					yellowTank.moveUp(delta);
				}else
				if(Gdx.input.isKeyPressed(Keys.DOWN)){
				
					yellowTank.moveDown(delta);
				}else
				if(Gdx.input.isKeyPressed(Keys.LEFT)){
					yellowTank.moveLeft(delta);
				}else
				if(Gdx.input.isKeyPressed(Keys.RIGHT)){
					yellowTank.moveRight(delta);
				}
				if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
					yellowTank.shoot(gameEnv.bullets);
				}
				if(Gdx.input.isKeyJustPressed(Keys.CONTROL_RIGHT)){
					yellowTank.shoot2(gameEnv.turtleArray);
				}
				greenTank.checkMovable(yellowTank.rect);
				if(Gdx.input.isKeyPressed(Keys.W)){
					greenTank.moveUp(delta);
				}else
				if(Gdx.input.isKeyPressed(Keys.S)){
					greenTank.moveDown(delta);
				}else
				if(Gdx.input.isKeyPressed(Keys.A)){
					greenTank.moveLeft(delta);
				}else
				if(Gdx.input.isKeyPressed(Keys.D)){
					greenTank.moveRight(delta);
				}
				if(Gdx.input.isKeyJustPressed(Keys.G))
					greenTank.shoot(gameEnv.bullets);
				if(Gdx.input.isKeyJustPressed(Keys.H))
					greenTank.shoot2(gameEnv.turtleArray);
				
				
				gameEnv.process(delta);						
			

				int[] a = {0};
				batch.begin();
				
				//Game play text render
				int plus = -20;
					//Player 1
				layout.setText(Assets.font50, "Player 1 : ");
				Assets.font50.draw(batch, layout, -130, 600 + plus);
				batch.draw(Assets.yellowTank[0], -130, 540 + plus, 0, 0, 25, 25, 1, 1, 0);
				layout.setText(Assets.font50, "x " + gameEnv.yellowTank.life);
				Assets.font50.draw(batch, layout, -95, 562 + plus);
				if(yellowTank.secondaryWeaponStatus == Tank.secondaryWeapon.marioTurtle){
					batch.draw(Assets.turtle[2], -130, 510 + plus, 0, 0, 25, 25, 1, 1, 0);
					layout.setText(Assets.font50, "x 1");
					Assets.font50.draw(batch, layout, -95, 532 + plus);
				}
				layout.setText(Assets.font50, "Score : " + (gameEnv.yellowTank.score));
				Assets.font50.draw(batch, layout, -130, 450 + plus);
				
				
					//Player 2
				layout.setText(Assets.font50, "Player 2 : ");
				Assets.font50.draw(batch, layout, 670, 600 + plus);
				batch.draw(Assets.greenTank[0], 670, 540 + plus, 0, 0, 25, 25, 1, 1, 0);
				layout.setText(Assets.font50, "x " + gameEnv.greenTank.life);
				Assets.font50.draw(batch, layout, 705, 562 + plus);
				if(greenTank.secondaryWeaponStatus == Tank.secondaryWeapon.marioTurtle){
					batch.draw(Assets.turtle[2], 705, 510 + plus, 0, 0, 25, 25, 1, 1, 0);
					layout.setText(Assets.font50, "x 1");
					Assets.font50.draw(batch, layout, 670, 532 + plus);
				}
				layout.setText(Assets.font50, "Score : " + (gameEnv.greenTank.score));
				Assets.font50.draw(batch, layout, 670, 450 + plus);
				
				
				
				batch.draw(Assets.bg, 0, 0);
				batch.end();
				MapsControl.currentMapRenderer.setView(camera);
				MapsControl.currentMapRenderer.render(a);				
				batch.begin();		
				Iterator<Bullet> bulletsIterator = gameEnv.bullets.iterator();
				while(bulletsIterator.hasNext()){
					Bullet b = bulletsIterator.next();
					b.doSomething(delta);
					b.draw(batch);
					if(b.outOfMap == true)
						bulletsIterator.remove();
				}
				
				//Crate render
				batch.draw(gameEnv.crate.getCurrentFrame(), gameEnv.crate.rect.x, gameEnv.crate.rect.y);
				
				//Diamond render
				batch.draw(Assets.blueDiamond, gameEnv.blueDiamond.getX(), gameEnv.blueDiamond.getY(), 50, 50, 0, 0, 50, 50, false, true);
				batch.draw(Assets.redDiamond, gameEnv.redDiamond.rect.x, gameEnv.redDiamond.rect.y);
				
				//Explosion effect render
				Iterator<ExplosionFx> ex_iter = gameEnv.explosion.iterator();
				while(ex_iter.hasNext()){
					ExplosionFx ex = ex_iter.next();
					batch.draw(ex.getCurrentFrame(), ex.getX(), ex.getY());
				}
				
				//Tank Render
				for(Turtle turtle : gameEnv.turtleArray){
					batch.draw(turtle.getCurrentFrame(), turtle.rect.x, turtle.rect.y);
				}
				yellowTank.draw(batch);
				greenTank.draw(batch);
				batch.end();
				
				a[0] = 1;
				MapsControl.currentMapRenderer.render(a);
				batch.begin();
				Iterator<CrateText> crateTiter = gameEnv.crateTextArray.iterator();
				while(crateTiter.hasNext()){
					CrateText crateText = crateTiter.next();
					if(!crateText.isDone()){
						layout.setText(Assets.font50, crateText.text);
						Assets.font50.draw(batch, layout, crateText.x - (layout.width / 2), crateText.y + (layout.height /2));
						System.out.println("Print text (" + crateText.text + ") at X : " + crateText.x);
						}
					else crateTiter.remove();
				}

				
				batch.end();
				break;
			case endOfGame:
				batch.begin();
				switch(pointer){
					case 4:
						layout.setText(Assets.font80, "" + ((gameEnv.greenTankResultScore - gameEnv.redDiamondDestroyed * 3) + gameEnv.redDiamondDestroyed * 3));
						Assets.font80.draw(batch, layout, 460 - layout.width / 2f, 200);
					case 3:
						layout.setText(Assets.font80, "" + ((gameEnv.yellowTankResultScore - gameEnv.blueDiamondDestroyed * 3) + gameEnv.blueDiamondDestroyed * 3));
						Assets.font80.draw(batch, layout, 160 - layout.width / 2f, 200);
						
				}
				if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
					Assets.selectSound.play();
					if(pointer < 3)
						pointer = 3;
					else if(pointer == 3)
						pointer = 4;
					else{
						gameEnv.resetGame();
						pointer = 1;
					}
				}
				plus = -70;
				
				batch.draw(Assets.scoreResult, 0, 650 - Assets.scoreResult.getHeight());
				//Player 1
				layout.setText(Assets.font80, "Player 1");
				Assets.font80.draw(batch, layout, 100, 450 + plus);
				batch.draw(Assets.greenTank[0], 120, 350 + plus, 0, 0, 25, 25, 1, 1, 0);
				layout.setText(Assets.font50, "x " + (gameEnv.yellowTankResultScore - gameEnv.blueDiamondDestroyed * 3));
				Assets.font50.draw(batch, layout, 165, 370 + plus);
				batch.draw(Assets.blueDiamond, 120, 310 + plus, 0, 0, 25, 25, 1, 1, 0, 0, 0, 50, 50, false, false);
				layout.setText(Assets.font50, "x " + gameEnv.blueDiamondDestroyed);
				Assets.font50.draw(batch, layout, 165, 335 + plus);
				
				//Player 2
				layout.setText(Assets.font80, "Player 2");
				Assets.font80.draw(batch, layout, 400, 450 + plus);
				batch.draw(Assets.yellowTank[0], 420, 350 + plus, 0, 0, 25, 25, 1, 1, 0);
				layout.setText(Assets.font50, "x " + (gameEnv.greenTankResultScore - gameEnv.redDiamondDestroyed * 3));
				Assets.font50.draw(batch, layout, 465, 370 + plus);
				batch.draw(Assets.redDiamond, 420, 310 + plus, 0, 0, 25, 25, 1, 1, 0, 0, 0, 50, 50, false, false);
				layout.setText(Assets.font50, "x " + gameEnv.redDiamondDestroyed);
				Assets.font50.draw(batch, layout, 465, 335 + plus);
					
				batch.end();
				break;
			case howToPlay:
				batch.begin();
				batch.draw(Assets.howToPlayScreen, 0, 0);
				if(Gdx.input.isKeyJustPressed(Keys.SPACE)){
					gameEnv.gameStatus = GameEnvSys.gameStatuses.main;
					Assets.selectSound.play();
				}
				batch.end();
	}
		sleep(60); 
	}
	
	private int randxy(){
		return 50 * ((int)(Math.random()*100) % 13);
	}
	
	public void sleep(int fps) {
	    if(fps>0){
	      diff = System.currentTimeMillis() - start;
	      long targetDelay = 1000/fps;
	      if (diff < targetDelay) {
	        try{
	            Thread.sleep(targetDelay - diff);
	          } catch (InterruptedException e) {}
	        }   
	      start = System.currentTimeMillis();
	    }
	}
}
