package com.mazerunner;

import java.awt.EventQueue;

import com.mazerunner.ui.Window;

public class App {
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			Window window = new Window("Maze Runner");
			window.setVisible(true);

			Thread gameThread = new Thread(window.getGameCanvas());
			gameThread.start();
		});
	}
}
