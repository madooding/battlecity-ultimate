package com.mygdx.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;

public class Assets {
	
	public static Texture tank_sheet;
	public static TextureRegion[] yellowTank;
	public static TextureRegion[] greenTank;
	public static TextureRegion[] turtle;
	public static TextureRegion[][] explosionFX;
	public static TextureRegion[] boxTexture;
	public static Texture blank = new Texture(Gdx.files.internal("blank.png"));
	public static Texture bg = new Texture(Gdx.files.internal("bg.png"));
	public static Texture blueDiamond = new Texture(Gdx.files.internal("blue_diamond.png"));
	public static Texture redDiamond = new Texture(Gdx.files.internal("red_diamond.png"));
	public static Texture splash = new Texture(Gdx.files.internal("splash.png"));
	public static Texture scoreResult = new Texture(Gdx.files.internal("score_result.png"));
	public static Texture howToPlayScreen = new Texture(Gdx.files.internal("how_to_play.png"));
	public static Texture members = new Texture(Gdx.files.internal("memberofgroup.png"));
	public static Animation yellowTankAnimation;
	public static Animation greenTankAnimation;
	public static Animation explosionFXAnimation;
	public static Animation turtleAnimation;
	public static TextureRegion yellowTankCurrentFrame;
	public static TextureRegion greenTankCurrentFrame;
	public static Sprite bullet;
	public static Array<TiledMap> maps;
	
	public static Sound shootSound;
	public static Sound explosionSound;
	public static Sound pickupSound;
	public static Sound hitSound;
	public static Sound blockedSound;
	public static Sound selectSound;
	public static Sound endOfGameSound;
	public static Sound shellSound;
	
	public static FreeTypeFontGenerator fontGenerator;
	public static BitmapFont font80;
	public static BitmapFont font50;
	
	@SuppressWarnings("deprecation")
	public static void load(){
		
		//Load Font 
		fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/gameFont.ttf"));
		font50 = fontGenerator.generateFont(55);
		font80 = fontGenerator.generateFont(80);
		
		fontGenerator.dispose();
		font80.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font80.setColor(Color.WHITE);
		font50.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font50.setColor(Color.WHITE);
		
		
		//Load texture		
		tank_sheet = new Texture(Gdx.files.internal("tank_animate.png"));
		
		//Load sprite
		bullet = new Sprite(new Texture(Gdx.files.internal("bullet.png")));
		
		TextureRegion[][] temp = TextureRegion.split(tank_sheet, 50, 50);
		yellowTank = new TextureRegion[2];
		greenTank = new TextureRegion[2];
		
		
		explosionFX = TextureRegion.split(new Texture(Gdx.files.internal("explosionFX.png")), 50, 50);
		turtle = TextureRegion.split(new Texture(Gdx.files.internal("turtle.png")), 50, 50)[0];
		
		for(int i = 0; i < 2; i++){
			yellowTank[i] = temp[0][i];
			greenTank[i] = temp[1][i];
		}
		temp = TextureRegion.split(new Texture(Gdx.files.internal("box_texture.png")), 50, 50);
		boxTexture = new TextureRegion[temp[0].length];
		
		for(int i = 0; i < temp[0].length; i++)
			boxTexture[i] = temp[0][i];
		
		//set animation
		yellowTankAnimation = new Animation(0.05f, yellowTank);
		greenTankAnimation = new Animation(0.05f, greenTank);
		explosionFXAnimation = new Animation(0.5f, explosionFX[0]);

		

		
		
		// Load Sound
		shootSound = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.wav"));
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
		pickupSound = Gdx.audio.newSound(Gdx.files.internal("sounds/pickup.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("sounds/hit.wav"));
		blockedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/blocked.wav"));
		selectSound = Gdx.audio.newSound(Gdx.files.internal("sounds/select.wav"));
		endOfGameSound = Gdx.audio.newSound(Gdx.files.internal("sounds/end_of_game.wav"));
		shellSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ShellSFX.wav"));
		
	}
	public static void loadMaps(){
		//Load maps
		maps = new Array();
		String mapsList[] = {"map1.tmx", "map2.tmx", "map3.tmx", "map4.tmx", "map5.tmx", "map6.tmx"};
		for(String mapName : mapsList){
			maps.add(new TmxMapLoader().load("maps/" + mapName));
		}
	}
	
	
}
