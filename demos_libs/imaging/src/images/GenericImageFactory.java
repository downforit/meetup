/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images;

import images.text.TextUtil;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import javax.media.jai.TiledImage;
import javax.swing.ImageIcon;
import utils.BufferedImageUtils;
import utils.JAIUtils;
import utils.LLJTUtils;
import utils.Sizes;


/**
 *
 * @author developer
 */
public class GenericImageFactory {
  static GenericImageFactory singletonInstance = null;
    
  private boolean flgUseJai = false;
  private boolean flgUseVirtualImages = false;

  public static GenericImageFactory getInstance(){
      if(singletonInstance == null){
          singletonInstance = newInstance(false);
      }
      
      return singletonInstance;
  }

  
  public static GenericImageFactory newInstance(boolean useJai){
      GenericImageFactory result = new GenericImageFactory();
      
      result.setFlgUseJai(useJai);
      
      return result;
  }

  public void setMaxQuality(){
    BufferedImageUtils.setMaxQuality();
    this.setFlgUseJai(true);
    this.setFlgUseVirtualImages(false);
  }


  public boolean isFlgUseJai() {
    return flgUseJai;
  }

  public void setFlgUseJai(boolean aFlgUseJai) {
    flgUseJai = aFlgUseJai;
  }

  private static boolean flgUseJaiOld = false;

  public void setFlgUseJaiTmp(boolean aFlgUseJai) {
      flgUseJaiOld = flgUseJai;
      flgUseJai = aFlgUseJai;
  }

  public void ripristinaFlgUseJaiOld(){
      flgUseJai = flgUseJaiOld;
  }

  /**
   * @return the flgUseVirtualImages
   */
  public boolean isFlgUseVirtualImages() {
    return flgUseVirtualImages;
  }

  /**
   * @param aFlgUseVirtualImages the flgUseVirtualImages to set
   */
  public void setFlgUseVirtualImages(boolean aFlgUseVirtualImages) {
    flgUseVirtualImages = aFlgUseVirtualImages;
  }

  //*****************************************************/
  private boolean isGenericBufferedImage(){
    return !isFlgUseJai();
  }


  //*****************************************************/
  /*
  public static GenericImage buildGenericImage(byte []bufferImageFile){
    if(bufferImageFile == null){
      return null;
    }

    GenericImage result = null;

    if(isFlgUseVirtualImages()){
      result = new GenericVirtualImage(bufImage.getWidth(), bufImage.getHeight(), GenericBufferedImage.getNumLivelliByType(bufImage.getType()) );
    } else if(flgUseJai){
      result = buildGenericImage(JAIUtils.buildTiledImage(JAIUtils.convertBufferedImage(bufImage)));
    } else {
      result = new GenericBufferedImage(bufImage);
    }

    return result;
  }
  */

  //*****************************************************/
  public GenericImage buildGenericImage(BufferedImage bufImage, boolean addTransparency){
    GenericImage result = buildGenericImage(bufImage);
    if(addTransparency){
      if(result != null && result.getNumLivelli() < 4){
        result = result.addTrasparency();
      }
    }

    return result;
  }


  //*****************************************************/
  public GenericImage buildGenericImage(BufferedImage bufImage){
    if(bufImage == null){
      return null;
    }

    GenericImage result = null;

    if(isFlgUseVirtualImages()){
      result = new GenericVirtualImage(bufImage.getWidth(), bufImage.getHeight(), GenericBufferedImage.getNumLivelliByType(bufImage.getType()) );
    } else if(flgUseJai){
      result = buildGenericImage(JAIUtils.buildTiledImage(JAIUtils.convertBufferedImage(bufImage)));
    } else {
      result = new GenericBufferedImage(bufImage);
    }

    return result;
  }

  //*****************************************************/
  public GenericImage buildGenericImage(TiledImage tiledImage){
    if(tiledImage == null){
      return null;
    }
    GenericImage result = null;
    if(isFlgUseVirtualImages()){
      result = new GenericVirtualImage(tiledImage.getWidth(), tiledImage.getHeight(), tiledImage.getNumBands());
    } else if(flgUseJai){
      result = new GenericJaiImage(tiledImage);
    } else {
      result = buildGenericImage(tiledImage.getAsBufferedImage());
    }

    return result;
  }

  //*****************************************************/
  public GenericImage buildGenericImage(int widthMM, int heightMM, int DPI, int numLivelli){

    return buildGenericImage(Sizes.mmTOpxByDPI(widthMM, DPI), Sizes.mmTOpxByDPI(heightMM, DPI), numLivelli, false);
  }


  //*****************************************************/
  public GenericImage buildGenericImage(int width, int height, int numLivelli){
    return buildGenericImage(width, height, numLivelli, false);
  }

  //*****************************************************/
  public GenericImage buildGenericImage(int width, int height, int numLivelli, boolean forceToUseBufferedImage){
    GenericImage result = null;

    if(isFlgUseVirtualImages()){
      result = new GenericVirtualImage(width, height, numLivelli);
    } else if(isGenericBufferedImage() || forceToUseBufferedImage){
      result = new GenericBufferedImage(width, height, numLivelli);
    } else {
      result = new GenericJaiImage(width, height, numLivelli);
    }

    return result;
  }

  //*****************************************************/
  public GenericImage readImage(String pathImage){
    return readImage(pathImage, false, false);
  }


  //*****************************************************/
  public GenericImage readImage(String pathImage, boolean addTransparency, boolean forceToUseBufferedImage){
    GenericImage result = null;

    if(isFlgUseVirtualImages()){
      result = new GenericVirtualImage(pathImage);
    } else if(isGenericBufferedImage() || forceToUseBufferedImage){
      result = new GenericBufferedImage(BufferedImageUtils.readImage(pathImage));
    } else {
      result = new GenericJaiImage(JAIUtils.loadTiledImage(pathImage));
    }

    if(result.getNumLivelli() == 1){
      GenericImage imgTmp;
      if(addTransparency){
        imgTmp = buildGenericImage(result.getWidth(), result.getHeight(), 4);
      } else {
        imgTmp = buildGenericImage(result.getWidth(), result.getHeight(), 3);
      }
      imgTmp.DrawImage(result, 0, 0);
      result = imgTmp;
    }

    if(addTransparency && result.getNumLivelli() == 3){
      result = result.addTrasparency();
    }

    return result;
  }


  //****************************************************
  public GenericImage readThumbnail(String imagePath, int maxWidth, int maxHeight, boolean forceToUseBufferedImage){
    GenericImage result;

    ImageIcon iconTmp = LLJTUtils.getThumbnailImageIcon(imagePath);
    if (iconTmp != null) {
      //int moltiplicatore = 300 / iconTmp.getIconWidth();
      result = buildGenericImage(iconTmp.getIconWidth(), iconTmp.getIconHeight(), 4, forceToUseBufferedImage);
      iconTmp.paintIcon(null, result.createGraphics(), 0, 0);

      Dimension originalSize = BufferedImageUtils.getImageSize(imagePath);
      double or_rapporto = (double)originalSize.width/originalSize.height;
      double th_rapporto = (double)result.getWidth()/result.getHeight();

      if(Math.abs(or_rapporto - th_rapporto) > 0.05 ){
        Dimension dimThumbnail = BufferedImageUtils.scaleDimension(originalSize, result.getDimension());

        result = result.getSubimage((result.getWidth()-dimThumbnail.width)/2, (result.getHeight()-dimThumbnail.height)/2, dimThumbnail.width, dimThumbnail.height, true);
      }
    } else {
      result = readImage(imagePath, false, forceToUseBufferedImage);
    }

    result = result.scale(maxWidth, maxHeight);

    return result;
  }

  //****************************************************
  public GenericImage buildTextImage(String richTextDocument, Dimension areaReferredSize, int alignment) {
    return buildTextImage(richTextDocument, areaReferredSize, alignment, (float) 1);
  }

  //****************************************************
  public GenericImage buildTextImage(String richTextDocument, Dimension areaReferredSize, int alignment, float scaleFactor) {
    GenericImage result = null;

    if(isFlgUseVirtualImages()){
      result = buildGenericImage(TextUtil.buildMinimumBufferedImageFromDocument(TextUtil.RichTextToStyledDocument(richTextDocument), alignment, areaReferredSize.width, areaReferredSize.height, scaleFactor));
      //SystemUtils.printLog("ATTENZIONE: creo un'immagine virtuale per il testo.... verificare l'istruzione");
      result = new GenericVirtualImage(result.getWidth(), result.getHeight(), result.getNumLivelli());
    } else if(isGenericBufferedImage()){
      result = buildGenericImage(TextUtil.buildMinimumBufferedImageFromDocument(TextUtil.RichTextToStyledDocument(richTextDocument), alignment, areaReferredSize.width, areaReferredSize.height, scaleFactor));
    } else {
      result = buildGenericImage(TextUtil.buildMinimumTiledImageJAIFromDocument(TextUtil.RichTextToStyledDocument(richTextDocument), alignment, areaReferredSize.width, areaReferredSize.height, scaleFactor));
    }

    return result;

  }



  //*****************************************************/
  public GenericImage scaleImageFromFile(String pathImage, int maxW, int maxH){
    GenericImage result = null;

    if(isFlgUseVirtualImages()){
      result = new GenericVirtualImage(pathImage);
      double scaleFactor = Sizes.getScaleFactor(maxW, maxH, result.getWidth(), result.getHeight());
      result = result.scale(scaleFactor);
    } else if(isGenericBufferedImage()){
      result = new GenericBufferedImage(BufferedImageUtils.scaleImageFromFile(pathImage, maxW, maxH));
    } else {
      result = readImage(pathImage);
      result = result.scale(maxW, maxH);
    }

    return result;
  }


}
