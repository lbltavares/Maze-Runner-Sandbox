package com.mazerunner.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class Tile {
	public static enum Type {
		WALL, PATH, END, WALKER
	}

	private Type type;

	public Tile(Tile.Type type) {
		setType(type);
	}

	public static boolean isSolid(Tile.Type type) {
		if (type == Tile.Type.WALL)
			return true;
		else if (type == Tile.Type.WALKER)
			return true;
		return false;
	}

	public void setType(Tile.Type type) {
		if (type != null)
			this.type = type;
	}

	public Tile.Type getType() {
		return type;
	}

	public static Color getColorOfType(Tile.Type type) {
		if (type == Type.WALL)
			return new Color(110, 100, 39);

		else if (type == Type.PATH)
			return new Color(30, 30, 30);

		else if (type == Type.END)
			return Color.GREEN;

		else if (type == Type.WALKER)
			return Color.BLUE;

		return Color.BLACK;
	}

	public void render(Graphics2D g2d, float x, float y, float w, float h) {
		g2d.setColor(getColorOfType(type));
		g2d.fill(new Rectangle2D.Float(x, y, w, h));
	}
}
