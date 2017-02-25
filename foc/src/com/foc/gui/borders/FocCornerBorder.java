package com.foc.gui.borders;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class FocCornerBorder extends LineBorder {

	public FocCornerBorder(Color color, int thickness) {
		super(color, thickness);
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Color oldColor = g.getColor();
		int i;

		g.setColor(lineColor);
		for(i = 0; i < thickness; i++){
			int x1 = x;
			int y1 = y + i;
			int x2 = x + i;
			int y2 = y;
			
			g.drawLine(x1, y1, x2, y2);
		}
		g.setColor(oldColor);
	}

	/**
	 * Returns the insets of the border.
	 * 
	 * @param c
	 *          the component for which this border insets value applies
	 */
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, 0, 0);
	}

	/**
	 * Reinitialize the insets parameter with this Border's current Insets.
	 * 
	 * @param c
	 *          the component for which this border insets value applies
	 * @param insets
	 *          the object to be reinitialized
	 */
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.left = 0;
		insets.top = 0;
		insets.right = 0;
		insets.bottom = 0;
		return insets;
	}

}
	
