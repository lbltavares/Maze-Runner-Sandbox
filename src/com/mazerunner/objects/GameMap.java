package com.mazerunner.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import com.mazerunner.ui.GameCanvas;

public class GameMap {

	private int cols, rows;
	private Tile[][] map;

	private boolean showGrid = true;

	private GameCanvas game;

	public GameMap(int cols, int rows) {
		this.cols = cols;
		this.rows = rows;
		map = new Tile[cols][rows];
		setAllTiles(Tile.Type.PATH);
	}

	public Tile[][] getMap() {
		return map.clone();
	}

	public void toggleGrid(boolean b) {
		showGrid = b;
	}

	public void setGameCanvas(GameCanvas game) {
		this.game = game;
	}

	public Tile.Type getTileTypeAt(int x, int y) {
		return getTile(x, y).getType();
	}

	public void setAllTiles(Tile.Type type) {
		if (type != null) {
			for (int c = 0; c < cols; c++) {
				for (int r = 0; r < rows; r++) {
					map[c][r] = new Tile(type);
				}
			}
		}
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public Tile getTile(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
			return map[x][y];
		return null;
	}

	public void setTile(int x, int y, Tile t) {
		if (x >= 0 && x < cols && y >= 0 && y < rows && t != null)
			map[x][y] = t;
	}

	public void setTile(int x, int y, Tile.Type type) {
		if (x >= 0 && x < cols && y >= 0 && y < rows && type != null)
			map[x][y] = new Tile(type);
	}

	public float getTileWidth() {
		return game.getWidth() / (cols * 1.0f);
	}

	public float getTileHeight() {
		return game.getHeight() / (rows * 1.0f);
	}

	public void render(Graphics2D g2d) {
		float tileWidth = getTileWidth();
		float tileHeight = getTileHeight();
		for (int c = 0; c < cols; c++) {
			for (int r = 0; r < rows; r++) {
				getTile(c, r).render(g2d, c * tileWidth, r * tileHeight, tileWidth, tileHeight);
			}
		}
	}

	public void renderGrid(Graphics2D g2d) {
		if (!showGrid)
			return;
		float w = getTileWidth();
		float h = getTileHeight();
		g2d.setColor(new Color(0, 0, 0, 40));
		for (int c = 0; c < cols; c++) {
			g2d.draw(new Line2D.Float(c * w, 0, c * w, h * rows));
		}
		for (int r = 0; r < rows; r++) {
			g2d.draw(new Line2D.Float(0, r * h, cols * w, r * h));
		}

	}

}
