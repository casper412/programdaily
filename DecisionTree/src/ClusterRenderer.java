import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class ClusterRenderer {
  private ArrayList<double[]> values = new ArrayList<double[]>();
  private ArrayList<double[]> centers = new ArrayList<double[]>();
  private ArrayList<Integer> centersCount = new ArrayList<Integer>();
  
  private int width   = 2048;
  private int height  = 2048;
  private int border  = 75;
  private int viewWidth   = 2048 - border * 2;
  private int viewHeight  = 2048 - border * 2;
  
  private double minX = 99999.;
  private double minY = 99999.;
  private double maxX = 0.;
  private double maxY = 0.;
  
  public void draw() {

    // Create a buffered image in which to draw
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);    
    // Create a graphics contents on the buffered image
    Graphics2D g2d = bufferedImage.createGraphics();
    g2d.setBackground(Color.white);
    g2d.clearRect(0, 0, width, height);
    
    drawGrid(g2d);
    
    for(double[] v : values) {
      draw(g2d, v, Color.black, false);
    }
    for(double[] v : centers) {
      draw(g2d, v, Color.red, true);
    }
    // Graphics context no longer needed so dispose it
    g2d.dispose();
     
    try {
      // Save as PNG
      File f = new File("newimage.png");
      ImageIO.write(bufferedImage, "png", f);
    } 
    catch(Throwable t)
    { 
    }
  }

  private void drawGrid(Graphics2D g2d) {
    g2d.setColor(Color.gray);
    g2d.setFont(new Font("Serif", 0, 28));
    DecimalFormat format = new DecimalFormat("00.00");
    int spacing = 125;
    for(int x = 0; x < viewWidth; x += spacing) {
      g2d.drawLine(x, border, x, viewHeight + border);
      double value = (float)x/viewWidth * (maxX - minX) + minX;
      String valueStr = format.format(value);
      g2d.drawString(valueStr, x, border);
    }
    spacing = 75;
    for(int y = 0; y < viewHeight; y += spacing) {
      g2d.drawLine(border, y, viewWidth + border, y);
      double value = (float)y/viewHeight * (maxY - minY) + minY;
      String valueStr = format.format(value);
      g2d.drawString(valueStr, 0, y);
    }
  }
  
  private void draw(Graphics2D g2d, double []v, Color c, boolean fill) {
    g2d.setColor(c);
    int x = (int)((v[0] - minX) / (maxX - minX) * viewWidth) + border;
    int y = (int)((v[1] - minY) / (maxY - minY) * viewHeight) + border;
    if(fill) {
      g2d.fillRect(x, y, 10, 10);
    } else {
      g2d.drawRect(x, y, 10, 10);
    }
  }
  
  public void add(double[] v) {
    double nv[] = new double[2];
    for(int i = 0; i < 2; i++) {
      nv[i] = v[i];
    }
    values.add(nv);
    
    if(nv[0] < minX) {
      minX = nv[0];
    }
    if(nv[0] > maxX) {
      maxX = nv[0];
    }
    if(nv[1] < minY) {
      minY = nv[1];
    }
    if(nv[1] > maxY) {
      maxY = nv[1];
    }
  }

  public void addCenter(double[] v, int count) {
    double nv[] = new double[2];
    for(int i = 0; i < 2; i++) {
      nv[i] = v[i];
    }
    centers.add(nv);
    centersCount.add(count);
  }
}
