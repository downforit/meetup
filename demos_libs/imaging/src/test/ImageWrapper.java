/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import images.GenericImage;
import java.awt.Point;

/**
 *
 * @author Mario
 */
public class ImageWrapper {
    String name;
    
    GenericImage srcImage;
    GenericImage lastImage;
    
    ImageStatus status = new ImageStatus();
    ImageStatus lastStatus = new ImageStatus();
    

    public GenericImage getFinalImage(){
        return getFinalImage(false);
    }
    
    public GenericImage getFinalImage(boolean produceAlwaysACopy){
        GenericImage result;
        
        if(lastImage == null){
            this.lastImage = this.srcImage;
        }
        
        if(this.status.equals(this.lastStatus)){
            result = this.lastImage;
        } else {
            result = srcImage;

            if(status.opacity > 0 && status.opacity <= 254){
                if(result.getNumLivelli() == 4){
                    result = result.clone();
                }
                
                result = result.applyTrasparency(status.opacity);
            }

            if(status.sizePercentage > 0 && status.sizePercentage != 100){
                result = result.scale((float)status.sizePercentage / 100F);
            }
        }
        
        
        if(this.lastStatus == null){
            this.lastStatus = new ImageStatus();
        }
        this.lastStatus.init(this.status);
        this.lastImage = result;
        
        if(produceAlwaysACopy){
            result = result.clone();
        }

        
        return result;
    }
}
