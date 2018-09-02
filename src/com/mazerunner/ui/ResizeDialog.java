package com.mazerunner.ui;

import java.awt.Point;
import java.util.Hashtable;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

public class ResizeDialog extends JOptionPane {
	private static final long serialVersionUID = 1L;

	private int x, y;

	public ResizeDialog(int c, int r) {
		x = c;
		y = r;
		JSlider sliderWidth = getSlider(this, x);
		sliderWidth.addChangeListener((ChangeEvent changeEvent) -> {
			JSlider theSlider = (JSlider) changeEvent.getSource();
			if (!theSlider.getValueIsAdjusting()) {
				x = theSlider.getValue();
			}
		});

		JSlider sliderHeight = getSlider(this, y);
		sliderHeight.addChangeListener((ChangeEvent changeEvent) -> {
			JSlider theSlider = (JSlider) changeEvent.getSource();
			if (!theSlider.getValueIsAdjusting()) {
				y = theSlider.getValue();
			}
		});

		setMessage(new Object[] { "Width: ", sliderWidth, "Height", sliderHeight });
		setMessageType(JOptionPane.QUESTION_MESSAGE);
		setOptionType(JOptionPane.OK_CANCEL_OPTION);

		JDialog dialog = createDialog(this, "Resize Map");
		dialog.setVisible(true);
	}

	private JSlider getSlider(final JOptionPane optionPane, int v) {
		JSlider slider = new JSlider(8, 32, v);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		Hashtable<Integer, JLabel> labels = new Hashtable<>();
		labels.put(8, new JLabel("8"));
		labels.put(12, new JLabel("12"));
		labels.put(16, new JLabel("16"));
		labels.put(20, new JLabel("20"));
		labels.put(24, new JLabel("24"));
		labels.put(28, new JLabel("28"));
		labels.put(32, new JLabel("32"));
		slider.setLabelTable(labels);
		return slider;
	}

	public Point getValues() {
		return new Point(x, y);
	}
}
