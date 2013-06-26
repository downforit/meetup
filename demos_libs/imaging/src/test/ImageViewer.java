/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import images.GenericImage;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;
import utils.Sizes;

/**
 *
 * @author Mario
 */
public class ImageViewer extends JPanel {
    private GenericImage srcImage = null;
    private GenericImage imageToPaint = null;
    
    private double scaleFactor;

    
    
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        
        if(this.srcImage != null){
            if( Math.abs(this.imageToPaint.getWidth() - this.getSize().width) > 1 && Math.abs(this.imageToPaint.getHeight() - this.getSize().height) > 1){
                this.imageToPaint = this.srcImage.scale(this.getSize().width, this.getSize().height, false);
            }
        }
        
        
        if(this.imageToPaint != null){
            Graphics2D g2d = (Graphics2D) g;

            AffineTransform a = new AffineTransform();
            a.translate((this.getSize().width - this.imageToPaint.getWidth()) / 2, (this.getSize().height - this.imageToPaint.getHeight()) / 2);
            g2d.drawRenderedImage(this.imageToPaint.getRenderedImage(), a);            
            
        }
        
    }

    /**
     * @return the imageToPaint
     */
    public GenericImage getImageToPaint() {
        return imageToPaint;
    }

    /**
     * @param imageToPaint the imageToPaint to set
     */
    public void setImageToPaint(GenericImage imageToPaint) {
        this.srcImage = imageToPaint;
        
        if(this.srcImage == null){
            this.imageToPaint = null;
        } else {
            this.scaleFactor = Sizes.getScaleFactor(this.getSize().width, this.getSize().height, this.srcImage.getWidth(), this.srcImage.getHeight());
            this.imageToPaint = this.srcImage.scale(this.scaleFactor);
        }
        
        this.invalidate();
        this.repaint();
    }

    /**
     * @return the scaleFactor
     */
    public double getScaleFactor() {
        return scaleFactor;
    }

    
    
}
