/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.Map;

/**
 *
 * @author developer
 */
public class VirtualGraphics2D extends Graphics2D {

  @Override
  public void draw(Shape s) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean drawImage(Image img, AffineTransform xform, ImageObserver obs) {
    ////throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public void drawImage(BufferedImage img, BufferedImageOp op, int x, int y) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawRenderedImage(RenderedImage img, AffineTransform xform) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawRenderableImage(RenderableImage img, AffineTransform xform) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawString(String str, int x, int y) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawString(String str, float x, float y) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawString(AttributedCharacterIterator iterator, int x, int y) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawString(AttributedCharacterIterator iterator, float x, float y) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawGlyphVector(GlyphVector g, float x, float y) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fill(Shape s) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean hit(Rectangle rect, Shape s, boolean onStroke) {
    ////throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public GraphicsConfiguration getDeviceConfiguration() {
    ////throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void setComposite(Composite comp) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setPaint(Paint paint) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setStroke(Stroke s) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setRenderingHint(Key hintKey, Object hintValue) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Object getRenderingHint(Key hintKey) {
    ////throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void setRenderingHints(Map<?, ?> hints) {
    ////throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void addRenderingHints(Map<?, ?> hints) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public RenderingHints getRenderingHints() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void translate(int x, int y) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void translate(double tx, double ty) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void rotate(double theta) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void rotate(double theta, double x, double y) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void scale(double sx, double sy) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void shear(double shx, double shy) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void transform(AffineTransform Tx) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setTransform(AffineTransform Tx) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public AffineTransform getTransform() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public Paint getPaint() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public Composite getComposite() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void setBackground(Color color) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Color getBackground() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public Stroke getStroke() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void clip(Shape s) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public FontRenderContext getFontRenderContext() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public Graphics create() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public Color getColor() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void setColor(Color c) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setPaintMode() {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setXORMode(Color c1) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Font getFont() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void setFont(Font font) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public FontMetrics getFontMetrics(Font f) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public Rectangle getClipBounds() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void clipRect(int x, int y, int width, int height) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void setClip(int x, int y, int width, int height) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public Shape getClip() {
    //throw new UnsupportedOperationException("Not supported yet.");
    return null;
  }

  @Override
  public void setClip(Shape clip) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void copyArea(int x, int y, int width, int height, int dx, int dy) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawLine(int x1, int y1, int x2, int y2) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fillRect(int x, int y, int width, int height) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void clearRect(int x, int y, int width, int height) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawOval(int x, int y, int width, int height) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fillOval(int x, int y, int width, int height) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

  @Override
  public boolean drawImage(Image img, int x, int y, ImageObserver observer) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, int width, int height, ImageObserver observer) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, Color bgcolor, ImageObserver observer) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public boolean drawImage(Image img, int x, int y, int width, int height, Color bgcolor, ImageObserver observer) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, ImageObserver observer) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, int sx1, int sy1, int sx2, int sy2, Color bgcolor, ImageObserver observer) {
    //throw new UnsupportedOperationException("Not supported yet.");
    return false;
  }

  @Override
  public void dispose() {
    //throw new UnsupportedOperationException("Not supported yet.");
  }

}
