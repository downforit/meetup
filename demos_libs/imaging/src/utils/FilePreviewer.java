/*
 * FilePreviewer.java
 *
 * Created on 31 luglio 2007, 13.47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package utils;
import javax.swing.JComponent;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import javax.swing.ImageIcon;
import java.io.File;
import java.beans.PropertyChangeEvent;
import javax.swing.JFileChooser;
import java.awt.Dimension;
import java.awt.Graphics;

public class FilePreviewer extends JComponent implements PropertyChangeListener {
    ImageIcon thumbnail = null;

    public FilePreviewer(JFileChooser fc) {
        setPreferredSize(new Dimension(100, 100));
        fc.addPropertyChangeListener(this);
    }

    public void loadImage(File f) {
        if (f == null) {
            thumbnail = null;
        } else {
            ImageIcon tmpIcon = new ImageIcon(f.getPath());
            if(tmpIcon.getIconWidth() > 90) {
                thumbnail = new ImageIcon(
                    tmpIcon.getImage().getScaledInstance(90, -1, Image.SCALE_FAST));
            } else {
                thumbnail = tmpIcon;
            }
        }
    }

    public void propertyChange(PropertyChangeEvent e) {
        String prop = e.getPropertyName();
        if(prop == JFileChooser.SELECTED_FILE_CHANGED_PROPERTY) {
            if(isShowing()) {
                loadImage((File) e.getNewValue());
                repaint();
            }
        }
    }

    public void paint(Graphics g) {
        if(thumbnail != null) {
            int x = getWidth()/2 - thumbnail.getIconWidth()/2;
            int y = getHeight()/2 - thumbnail.getIconHeight()/2;
            if(y < 0) {
                y = 0;
            }

            if(x < 5) {
                x = 5;
            }
            thumbnail.paintIcon(this, g, x, y);
        }
    }
}

