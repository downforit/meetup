/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package images.text;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Mario
 */
public class FontsUtil {
    public static int calculateFontSize(int DPI, int heightMM) {

        int fontSize = (int)(((double)heightMM * 6) / ((double)200/DPI));
        return fontSize;

    }

    
    
    public static int calculateTextWidthBasedOnFont(String inputText,String fontName,int fontStyle, int fontSize){


        BufferedImage bi = new BufferedImage(100,100,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();

        Font currentFont = new Font(fontName, fontStyle, fontSize);
        g2d.setFont(currentFont);
        FontMetrics fm = g2d.getFontMetrics();

        int stringWidth = fm.stringWidth(inputText);


        g2d.dispose();
        bi = null;
        return stringWidth;
    }
    
}
