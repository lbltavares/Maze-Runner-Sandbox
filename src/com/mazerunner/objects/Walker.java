package com.mazerunner.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Walker {

	public static boolean showTrail = false;

	private GameMap map;

	private Stack<Point> steps;
	private boolean[][] stepsMap;
	private int mapWidth, mapHeight;
	private Point pos;

	public Walker(GameMap map, int x, int y) {
		this.map = map;
		steps = new Stack<>();
		pos = new Point(x, y);
		steps.add(getPos());
		initStepsMap();
	}

	private void initStepsMap() {
		mapWidth = map.getCols();
		mapHeight = map.getRows();
		stepsMap = new boolean[mapWidth][mapHeight];
		setAllStepsMap(false);
		stepsMap[getX()][getY()] = true;
	}

	public boolean canMove(int x, int y) {
		if (x < 0 || x >= mapWidth || y < 0 || y >= mapHeight)
			return false;
		else if (stepsMap[x][y])
			return false;
		else if (Tile.isSolid(map.getTileTypeAt(x, y)))
			return false;

		return true;
	}

	public int getX() {
		return pos.x;
	}

	public int getY() {
		return pos.y;
	}

	public Point getPos() {
		return (Point) pos.clone();
	}

	public void setPos(int x, int y) {
		if (x >= 0 && x < mapWidth && y >= 0 && y < mapHeight) {
			pos.x = x;
			pos.y = y;
		}
	}

	public void update() {
		if (checkWin()) {
			steps.clear();
			steps.add(getPos());
			return;
		}

		if (!steps.isEmpty()) {
			int c = steps.lastElement().x;
			int r = steps.lastElement().y;
			setPos(c, r);

			ArrayList<Point> possibleMoves = new ArrayList<>();

			if (canMove(c + 1, r))
				possibleMoves.add(new Point(c + 1, r));
			if (canMove(c - 1, r))
				possibleMoves.add(new Point(c - 1, r));
			if (canMove(c, r + 1))
				possibleMoves.add(new Point(c, r + 1));
			if (canMove(c, r - 1))
				possibleMoves.add(new Point(c, r - 1));

			if (!possibleMoves.isEmpty()) {
				Random rand = new Random();
				Point p = possibleMoves.get(rand.nextInt(possibleMoves.size()));
				steps.add(new Point(p.x, p.y));
				stepsMap[p.x][p.y] = true;
				setPos(p.x, p.y);
			} else {
				Point rm = steps.pop();
				if (!steps.isEmpty()) {
					int x = steps.lastElement().x;
					int y = steps.lastElement().y;
					if (Tile.isSolid(map.getTileTypeAt(x, y))) {
						steps.clear();
						steps.add(rm);
						stepsMap[rm.x][rm.y] = true;
						setPos(rm.x, rm.y);
					}
				}
			}
		} else {
			setAllStepsMap(false);
			steps.add(getPos());
			stepsMap[getX()][getY()] = true;
		}

	}

	private void setAllStepsMap(boolean b) {
		for (int c = 0; c < mapWidth; c++) {
			for (int r = 0; r < mapHeight; r++) {
				stepsMap[c][r] = b;
			}
		}
	}

	public void toggleTrail(boolean b) {
		showTrail = b;
	}

	private boolean checkWin() {
		int x = getX();
		int y = getY();
		if (map.getTileTypeAt(x, y) == Tile.Type.END) {
			return true;
		}
		return false;
	}

	public void render(Graphics2D g2d) {
		float w = map.getTileWidth();
		float h = map.getTileHeight();
		int x = getX();
		int y = getY();

		if (showTrail) {
			for (int i = 0; i < steps.size(); i++) {
				int sx = steps.get(i).x;
				int sy = steps.get(i).y;
				g2d.setColor(Color.WHITE);
				g2d.fill(new Rectangle2D.Float(sx * w, sy * h, w, h));
			}
		}

		g2d.setColor(Tile.getColorOfType(Tile.Type.WALKER));
		g2d.fill(new Rectangle2D.Float(x * w, y * h, w, h));
	}
}
