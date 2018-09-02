package com.mazerunner.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.mazerunner.objects.Tile;

public class Window extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;

	private GameCanvas game;
	private ButtonGroup tileBtnGroup;

	public Window(String title) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		initLookAndFeel();
		initComponents();
		setLocationRelativeTo(null);
	}

	public GameCanvas getGameCanvas() {
		return game;
	}

	private void initComponents() {
		initMenuBar();
		initToolBar();
		initGameCanvas();
		initTileButtons();
		pack();
	}

	private void initMenuBar() {
		JMenuBar bar = new JMenuBar();
		JMenu menu = new JMenu("Opções");

		JMenuItem item;
		item = new JMenuItem("Import map...");
		item.setActionCommand("import");
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem("Export map...");
		item.setActionCommand("export");
		item.addActionListener(this);
		menu.add(item);

		item = new JMenuItem("Resize map");
		item.setActionCommand("resize");
		item.addActionListener(this);
		menu.add(item);

		bar.add(menu);

		setJMenuBar(bar);
	}

	private void initToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

		JButton btn;
		JCheckBox checkBox;

		btn = new JButton("Run");
		btn.setPreferredSize(new Dimension(64, 28));
		btn.setBackground(Color.GREEN);
		btn.setActionCommand("run");
		btn.addActionListener(this);
		toolBar.add(btn);

		btn = new JButton("Reset");
		btn.setPreferredSize(new Dimension(64, 28));
		btn.setActionCommand("reset");
		btn.addActionListener(this);
		toolBar.add(btn);

		btn = new JButton("Clear Walkers");
		btn.setPreferredSize(new Dimension(92, 28));
		btn.setActionCommand("clearwalkers");
		btn.addActionListener(this);
		toolBar.add(btn);

		toolBar.add(Box.createHorizontalGlue());
		toolBar.addSeparator();

		checkBox = new JCheckBox("Grid");
		checkBox.setSelected(true);
		checkBox.addItemListener((ItemEvent e) -> {
			game.toggleGrid(((JCheckBox) e.getItem()).isSelected());
		});
		toolBar.add(checkBox);

		checkBox = new JCheckBox("Walker Trail");
		checkBox.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		checkBox.setSelected(false);
		checkBox.addItemListener((ItemEvent e) -> {
			game.toggleWalkerTrail(((JCheckBox) e.getItem()).isSelected());
		});
		toolBar.add(checkBox);

		getContentPane().add(toolBar, BorderLayout.NORTH);
	}

	private void initGameCanvas() {
		game = new GameCanvas(500, 500, this);
		getContentPane().add(game, BorderLayout.CENTER);
	}

	private void initTileButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		tileBtnGroup = new ButtonGroup();

		panel.add(makeTileButton("PATH", "Path", true));
		panel.add(makeTileButton("WALL", "Wall", false));
		panel.add(makeTileButton("END", "End", false));
		panel.add(makeTileButton("WALKER", "Walker", false));

		getContentPane().add(panel, BorderLayout.WEST);
	}

	private JToggleButton makeTileButton(String type, String tooltip, boolean selected) {
		JToggleButton btn;
		btn = new JToggleButton(tooltip);
		btn.setMinimumSize(new Dimension(65, 65));
		btn.setPreferredSize(new Dimension(65, 65));
		btn.setMaximumSize(new Dimension(65, 65));
		btn.setActionCommand(type);
		btn.addActionListener(this);
		btn.setBackground(Tile.getColorOfType(Tile.Type.valueOf(type)));
		if (selected) {
			btn.setSelected(true);
			game.setSelectionType(btn.getActionCommand());
		}
		tileBtnGroup.add(btn);
		return btn;
	}

	private void initLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					return;
				}
			}
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("initLookAndFeel() error: " + e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("clearwalkers".equals(e.getActionCommand())) {
			game.removeWalkers();

		} else if ("reset".equals(e.getActionCommand())) {
			game.resetMap();

		} else if ("import".equals(e.getActionCommand())) {

		} else if ("export".equals(e.getActionCommand())) {

		} else if ("resize".equals(e.getActionCommand())) {
			ResizeDialog dialog = new ResizeDialog(game.getMap().getCols(), game.getMap().getRows());
			dialog.setVisible(true);
			Point p = dialog.getValues();
			game.resizeMap(p.x, p.y);

		} else if ("run".equals(e.getActionCommand())) {
			game.togglePause();
			JButton btn = ((JButton) e.getSource());
			if ("Run".equals(btn.getText())) {
				btn.setText("Pause");
				btn.setBackground(Color.RED);
			} else {
				btn.setText("Run");
				btn.setBackground(Color.GREEN);
			}
		} else {
			game.setSelectionType(e.getActionCommand());
		}
	}
}
