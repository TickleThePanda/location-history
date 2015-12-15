package com.ticklethepanda.lochistmap.cartograph;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import java.awt.Dimension;

public class HeatmapView extends JPanel {

  /**
   * 
   */
  private static final long serialVersionUID = -2412559105619917762L;
  private BufferedImage bi;

  public HeatmapView() {
    setPreferredSize(new Dimension(600, 600));
    setSize(new Dimension(600, 600));
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
