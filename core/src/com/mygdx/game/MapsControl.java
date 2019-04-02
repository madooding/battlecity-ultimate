package com.mygdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MapsControl {
	static public TiledMap currentMap;
	static public OrthogonalTiledMapRenderer currentMapRenderer;
	static public TiledMapTileLayer currentLayer;
	static public int mapIndex = 0;
	static public float stateTime;
	public static void set(){
		mapIndex = 0;
		currentMap = Assets.maps.first();
		currentLayer = (TiledMapTileLayer) currentMap.getLayers().get(0);
		currentMapRenderer = new OrthogonalTiledMapRenderer(currentMap);
	}
	public static void set(int index){
		mapIndex = index;
		currentMap = Assets.maps.get(index);
		currentLayer = (TiledMapTileLayer) currentMap.getLayers().get(0);
		currentMapRenderer = new OrthogonalTiledMapRenderer(currentMap);
	}
	
	public static void next(){
		MapsControl.set(mapIndex + 1);
	}
	
	public static boolean hasNext(){
		return mapIndex < Assets.maps.size - 1;
	}
}
