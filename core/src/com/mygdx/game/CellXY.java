package com.mygdx.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;

public class CellXY{
	int x;
	int y;
	Cell cell;
	Rectangle rect;
	public CellXY(Cell cell, int x, int y){
		this.cell = cell;
		this.x = x;
		this.y = y;
		rect = new Rectangle(x * 25, y * 25, 25, 25);
	}
}
