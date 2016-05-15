package co.uk.ticklethepanda.lochistmap.cartograph;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class HeatmapView extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2412559105619917762L;
	private BufferedImage bi;

	public HeatmapView() {
		setPreferredSize(new Dimension(600, 600));
		setSize(new Dimension(600, 600));
		setBackground(Color.white);
		bi = null;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bi != null)
			g.drawImage(bi, 0, 0, null);
	}
	
	public void setHeatmap(BufferedImage map) {
		bi = map;
		this.repaint();
	}
}
