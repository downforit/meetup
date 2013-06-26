/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.awt.Point;

/**
 *
 * @author Mario
 */
public class ImageStatus {
    Point position = new Point(0, 0);
    int opacity = 255;
    int sizePercentage = 100;    
    
    public void init(ImageStatus newStatus){
        this.position = newStatus.position;
        this.opacity = newStatus.opacity;
        this.sizePercentage = newStatus.sizePercentage;
    }

    public boolean equals(ImageStatus status){
        Boolean result = false;
        
        if(status != null){
            result = this.position == status.position
                   && this.opacity == status.opacity
                   && this.sizePercentage == status.sizePercentage;
        }
        
        return result;
    }
    
    
}
