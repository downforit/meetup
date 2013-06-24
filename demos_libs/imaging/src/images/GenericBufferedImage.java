/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.TiledImage;
import utils.BufferedImageUtils;
import utils.JAIUtils;

/**
 *
 * @author developer
 */
public class GenericBufferedImage extends GenericImage {

  private BufferedImage bufferedImage;
  private BufferedImage _bufImageTmp;

  //*****************************************************/
  public GenericBufferedImage(BufferedImage bufImage) {
    this.bufferedImage = bufImage;
  }

  //*****************************************************/
  public GenericBufferedImage(int width, int height, int numLivelli) {
    int type = BufferedImage.TYPE_INT_RGB;
    if (numLivelli == 4) {
      type = BufferedImage.TYPE_INT_ARGB;
    } else if (numLivelli == 1) {
      type = BufferedImage.TYPE_BYTE_GRAY;
    }

    this.bufferedImage = new BufferedImage(width, height, type);
  }

  //*****************************************************/
  public BufferedImage getBufferedImage() {
    return bufferedImage;
  }

  //*****************************************************/
  public TiledImage getTiledImage() {
    return JAIUtils.buildTiledImage(JAIUtils.convertBufferedImage(this.bufferedImage));
  }

  //*****************************************************/
  public void setBufferedImage(BufferedImage bufferedImage) {
    this.bufferedImage = bufferedImage;
  }

  //*****************************************************/
  @Override
  public int getWidth() {
    return this.bufferedImage.getWidth();
  }

  //*****************************************************/
  @Override
  public int getHeight() {
    return this.bufferedImage.getHeight();
  }

  //*****************************************************/
  @Override
  public long getRGB(int x, int y) {
    return this.bufferedImage.getRGB(x, y);
  }

  //*****************************************************/
  @Override
  public int getNumLivelli() {

    int result = 0;
    result = GenericBufferedImage.getNumLivelliByType(this.bufferedImage.getType());


    return result;
  }

  //*****************************************************/
  public static int getNumLivelliByType(int type){
    int result = 0;

    if (type == BufferedImage.TYPE_4BYTE_ABGR || type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_ARGB_PRE) {
      result = 4;
    } else if (type == BufferedImage.TYPE_3BYTE_BGR || type == BufferedImage.TYPE_INT_RGB || type == BufferedImage.TYPE_INT_BGR) {
      result = 3;
    } else if (type == BufferedImage.TYPE_BYTE_GRAY || type == BufferedImage.TYPE_USHORT_GRAY) {
      result = 1;
    }

    return result;
  }


  //*****************************************************/
  @Override
  public Graphics2D createGraphics() {
    return this.bufferedImage.createGraphics();
  }

  //*****************************************************/
  @Override
  public float[] getGrayOffsets() {
    return null;
  }

  //*****************************************************/
  @Override
  public float[] getCyanOffsets() {
    float[] offset = new float[3];
    //offset[0] = -50.0f; offset[1] = -20.0f; offset[2] = 0.0f;
    offset[0] = -40.0f; offset[2] = 10.0f; offset[1] = -10.0f;
    //offset[0] = -20.0f; offset[2] = 10.0f; offset[1] = 30.0f;
    return offset;

  }

  //*****************************************************/
  @Override
  public float[] getSeppiaOffsets() {
    float [] offset = new float[3];
   // offset[0] = 25.0f; offset[1] = 5.0f; offset[2] = -25.0f;
    offset[0] = 20.0f; offset[1] = 0.0f; offset[2] = -30.0f;
   // offset[0] = 35.0f; offset[2] = 15.0f; offset[1] = -15.0f;
    return offset;

  }


  //*****************************************************/
  @Override
  public RenderedImage getRenderedImage() {
    return this.bufferedImage;
  }


  //*****************************************************/
  @Override
  public Raster getData() {
    return this.bufferedImage.getData();
  }

  //*****************************************************/
  @Override
  public void setData(Raster r) {
    this.bufferedImage.setData(r);
  }

  //*****************************************************/
  @Override
  public void setRGBLevels(GenericImage srcRGB) {
    WritableRaster wr = this.bufferedImage.getWritableTile(0, 0);
    WritableRaster wr3 = wr.createWritableChild(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight(), 0, 0, new int[]{0, 1, 2});

    wr3.setRect(srcRGB.getBufferedImage().getData());
    this.bufferedImage.releaseWritableTile(0, 0);
  }

  //*****************************************************/
  @Override
  public void saveAs(String filePath) {
    BufferedImageUtils.saveImage(this.bufferedImage, filePath);
  }

  //*****************************************************/
  @Override
  public void saveAs(String filePath, float quality) {
    BufferedImageUtils.saveImage(this.bufferedImage, filePath, quality);
  }

  //*****************************************************/
  @Override
  public byte[] saveToByteArray(Float quality, boolean addMetadata) {
    if(addMetadata){
      return BufferedImageUtils.saveImageToByteArray(bufferedImage, BufferedImageUtils.getDefaultMetadata(), quality);
    } else {
      return BufferedImageUtils.saveImageToByteArray(bufferedImage, quality);
    }
  }

  //*****************************************************/
  @Override
  public void saveAsWithoutIcc(String filePath) {
    //BufferedImageUtils.saveImage(bufferedImage, filePath, quality);
    BufferedImageUtils.saveImage(this.bufferedImage, filePath);
  }

  //*****************************************************/
  @Override
  public void saveAsWithoutIcc(String filePath, float quality) {
    BufferedImageUtils.saveImage(this.bufferedImage, filePath, quality);
  }

  //*****************************************************/
  //*****************************************************/
  //*****************************************************/
  @Override
  public GenericImage clone() {

    //_bufImageTmp = this.bufferedImage.getSubimage(0, 0, this.bufferedImage.getWidth(), this.bufferedImage.getHeight());
    //return new GenericBufferedImage(_bufImageTmp);

    GenericBufferedImage result = new GenericBufferedImage(this.getWidth(), this.getHeight(), this.getNumLivelli());
    result.DrawImage(this, 0, 0);

    this.cloneGeneric(result);

    return result;

  }

  //*****************************************************/
  @Override
  public GenericImage grayScale() {
    _bufImageTmp = BufferedImageUtils.grayScale(this.bufferedImage, false);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage grayScaleSingleLayer() {
    _bufImageTmp = BufferedImageUtils.grayScale(this.bufferedImage, true);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage rotate(double angleRotation) {
    if(angleRotation == 0){
      return this;
    }
    _bufImageTmp = BufferedImageUtils.rotateImage(this.bufferedImage, angleRotation);
    /*
    if(angleRotation != 0){
      Dimension dimTmp = BufferedImageUtils.findRotatedSize(this.bufferedImage, angleRotation);
      if(_bufImageTmp.getWidth() != dimTmp.width || _bufImageTmp.getHeight() != dimTmp.height){
        System.out.println("il calcolo delle misure non ha funzionato");
      }
    }
    */
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage rotate(double angleRotation, Dimension size) {
    _bufImageTmp = BufferedImageUtils.rotateImage(this.bufferedImage, angleRotation, size);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage scale(double scaleFactor) {
    _bufImageTmp = BufferedImageUtils.scaleImage(this.bufferedImage, scaleFactor);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage resizeImageStretched(int width, int height) {
    _bufImageTmp = BufferedImageUtils.resizeImageStretched(this.bufferedImage,width, height);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage addTrasparency() {
    GenericImage result = null;
    //GRAVE: in questo modo duplico l'immagine anche se non c'� bisogno di aggiungere la trasparenza
    result = this.clone();

    if (!(this.bufferedImage.getType() == BufferedImage.TYPE_4BYTE_ABGR || this.bufferedImage.getType() == BufferedImage.TYPE_INT_ARGB)) {
      result = new GenericBufferedImage(addTransparency(result.getBufferedImage()));
    }

    return result;
  }

  //****************************************************
  protected BufferedImage addTransparency(BufferedImage src) {
    if(src.getType() == BufferedImage.TYPE_BYTE_GRAY){
      BufferedImage buffTmp = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_BGR);
      Graphics2D grTmp = buffTmp.createGraphics();
      grTmp.drawImage(src, null, 0, 0);
      grTmp.dispose();
      src = buffTmp;
    }

    BufferedImage biAlpha = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    Graphics gr = biAlpha.getGraphics();
    gr.setColor(Color.white);
    gr.fillRect(0, 0, biAlpha.getWidth(), biAlpha.getHeight());

    int w = src.getWidth();
    int h = src.getHeight();

    BufferedImage biWithTransparency = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    WritableRaster wr = biWithTransparency.getWritableTile(0, 0);
    WritableRaster wr3 = wr.createWritableChild(0, 0, w, h, 0, 0, new int[]{0, 1, 2});
    WritableRaster wr1 = wr.createWritableChild(0, 0, w, h, 0, 0, new int[]{3});

    wr3.setRect(src.getData());
    wr1.setRect(biAlpha.getData());
    biWithTransparency.releaseWritableTile(0, 0);
//    result = new TiledImage(biWithTransparency, 50, 50);
//    result = BufferedImage.wrapRenderedImage(biWithTransparency);
//    result = BufferedImage.wrapRenderedImage(result.getAsBufferedImage(new Rectangle(0,0,result.getWidth(), result.getHeight()),null));

    return biWithTransparency;
  }

  //*****************************************************/
  @Override
  public GenericImage addTrasparencyMask(GenericImage trasparencyMask) {
    _bufImageTmp = BufferedImageUtils.addTransparencyMask(this.bufferedImage, trasparencyMask.getBufferedImage());
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage applyTrasparencyMask(GenericImage trasparencyMask) {
    if(this.getNumLivelli() == 3){
        (new RuntimeException("L'immagine non ha il quarto livello di strasparenza")).printStackTrace();
        this.bufferedImage = BufferedImageUtils.addTransparencyMask(this.bufferedImage, trasparencyMask.getBufferedImage());
    }
    _bufImageTmp = BufferedImageUtils.applyTransparencyMask(this.bufferedImage, trasparencyMask.getBufferedImage());
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage getSubimage(int x, int y, int width, int height) {
    if(this.getWidth() < width){
      width = this.getWidth();
    }
    if(this.getHeight() < height){
      height = this.getHeight();
    }
    try {
      _bufImageTmp = this.bufferedImage.getSubimage(x, y, width, height);
    } catch (RuntimeException e) {
      e.printStackTrace();
      throw e;
    }
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage getShadowMask(int shadowLengthPixels, int transpValue) {
    return getShadowMask(shadowLengthPixels, transpValue, 1);
  }
  //*****************************************************/
  @Override
  public GenericImage getShadowMask(int shadowLengthPixels, int transpValue, int fattoreRisparmioOmbra) {
    _bufImageTmp = BufferedImageUtils.createShadowMask(this.bufferedImage, shadowLengthPixels, transpValue);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage getAlphaLaier() {
    _bufImageTmp = BufferedImageUtils.alphaLaier(this.bufferedImage);
    return new GenericBufferedImage(_bufImageTmp);
  }

  //*****************************************************/
  @Override
  public GenericImage translate(Point translation) {
    //return this;
    throw new UnsupportedOperationException("Not supported yet.");
  }

}
