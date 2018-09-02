package com.mazerunner.ui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.SwingUtilities;

import com.mazerunner.objects.GameMap;
import com.mazerunner.objects.Tile;
import com.mazerunner.objects.Walker;

public class GameCanvas extends Canvas implements Runnable, MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;

	private static final int FPS = 30;

	private boolean running = false;
	private boolean paused = true;

	private Graphics2D g2d;
	private Tile.Type selectionType;

	private ArrayList<Walker> walkers = new ArrayList<>();

	int mx, my;
	int anchorX, anchorY;

	private GameMap map;

	public GameCanvas(int width, int height, Window window) {
		setPreferredSize(new Dimension(width, height));
		setBackground(Color.WHITE);
		initMap();
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public void togglePause() {
		paused = !paused;
	}

	public void toggleWalkerTrail(boolean b) {
		for (int i = 0; i < walkers.size(); i++) {
			walkers.get(i).toggleTrail(b);
		}
	}

	public void setSelectionType(String str) {
		setSelectionType(Tile.Type.valueOf(str));
	}

	public void resetMap() {
		walkers.clear();
		map.setAllTiles(Tile.Type.PATH);
	}

	public void toggleGrid(boolean b) {
		map.toggleGrid(b);
	}

	private void initMap() {
		map = new GameMap(16, 16);
		map.setGameCanvas(this);
	}

	public void resizeMap(int cols, int rows) {
		removeWalkers();
		GameMap newMap = new GameMap(cols, rows);
		newMap.setGameCanvas(this);
		for (int c = 0; c < map.getCols(); c++) {
			for (int r = 0; r < map.getRows(); r++) {
				if (c < newMap.getCols() && r < newMap.getRows())
					newMap.setTile(c, r, map.getTile(c, r));
			}
		}
		map = newMap;
	}

	public GameMap getMap() {
		return map;
	}

	private void update() {
		for (Walker w : walkers)
			w.update();
	}

	private void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		g2d = (Graphics2D) bs.getDrawGraphics();
		g2d.clearRect(0, 0, getWidth(), getHeight());

		map.render(g2d);
		for (Walker w : walkers)
			w.render(g2d);
		map.renderGrid(g2d);
		drawMouseSelection();

		g2d.dispose();
		bs.show();
	}

	public void putWalker() {
		int x = mouseToCol();
		int y = mouseToRow();
		for (int i = 0; i < walkers.size(); i++) {
			Walker w = walkers.get(i);
			if (w.getX() == x && w.getY() == y) {
				return;
			}
		}
		walkers.add(new Walker(this, map, x, y));
	}

	public int mouseToCol() {
		Point p = mapPosition(mx, my);
		return p.x;
	}

	public int mouseToRow() {
		Point p = mapPosition(mx, my);
		return p.y;
	}

	public Point mapPosition(int x, int y) {
		int c = (int) (x * map.getCols() * 1.0f) / getWidth();
		int r = (int) (y * map.getRows() * 1.0f) / getHeight();
		return new Point(c, r);
	}

	private void drawMouseSelection() {
		float w = map.getTileWidth();
		float h = map.getTileHeight();
		g2d.setColor(Tile.getColorOfType(selectionType));
		g2d.draw(new Rectangle2D.Float(mouseToCol() * map.getTileWidth(), mouseToRow() * map.getTileHeight(), w, h));
	}

	public void setSelectionType(Tile.Type type) {
		this.selectionType = type;
	}

	@Override
	public void run() {
		running = true;
		long time = System.currentTimeMillis();
		while (running) {
			if (time + 1000 / FPS > System.currentTimeMillis())
				continue;
			time = System.currentTimeMillis();
			if (!paused)
				update();
			render();
		}
	}

	private void handleMousePress(Tile.Type type, boolean isShiftDown) {
		if (type == Tile.Type.WALKER)
			putWalker();
		else if (isShiftDown) {
			Point p = mapPosition(anchorX, anchorY);
			for (int c = p.x; c <= mouseToCol(); c++) {
				for (int r = p.y; r <= mouseToRow(); r++) {
					map.setTile(c, r, type);
				}
			}
		} else
			map.setTile(mouseToCol(), mouseToRow(), type);

	}

	public void removeWalkers() {
		walkers.clear();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Tile.Type t = SwingUtilities.isRightMouseButton(e) ? Tile.Type.PATH : selectionType;
		mx = e.getX();
		my = e.getY();
		boolean isShiftDown = e.isShiftDown();
		handleMousePress(t, isShiftDown);
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
		anchorX = e.getX();
		anchorY = e.getY();
		mx = e.getX();
		my = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		Tile.Type t = SwingUtilities.isRightMouseButton(e) ? Tile.Type.PATH : selectionType;
		mx = e.getX();
		my = e.getY();
		boolean isShiftDown = e.isShiftDown();
		handleMousePress(t, isShiftDown);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
	}
}
