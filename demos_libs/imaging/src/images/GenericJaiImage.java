/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.media.jai.PlanarImage;
import javax.media.jai.TiledImage;
import utils.BufferedImageUtils;
import utils.JAIUtils;


/**
 *
 * @author developer
 */
public class GenericJaiImage extends GenericImage {
  private TiledImage tImage;
  private TiledImage __tImage;


  public GenericJaiImage(TiledImage tiledImage){
    this.tImage = tiledImage;
  }

  public GenericJaiImage(int width, int height, int numLivelli) {
    this.tImage = JAIUtils.newTiledImage(width, height, numLivelli);
  }

  public GenericJaiImage(byte []bufferImageFile) {
    //this.tImage = JAIUtils.buildTiledImage(JAIUtils.getPlanarImage(bufferImageFile));
    this.tImage = new TiledImage(JAIUtils.getPlanarImage(bufferImageFile), true);
  }

  @Override
  public BufferedImage getBufferedImage() {
    return this.tImage.getAsBufferedImage();
  }

  public TiledImage getTiledImage() {
    return this.tImage;
  }

  @Override
  public int getWidth() {
    return this.tImage.getWidth();
  }

  @Override
  public int getHeight() {
    return this.tImage.getHeight();
  }

  @Override
  public int getNumLivelli() {
    return this.tImage.getNumBands();
  }

  @Override
  public long getRGB(int x, int y) {
    return JAIUtils.getRGB(this.tImage, x, y);
  }

  @Override
  public Graphics2D createGraphics() {
    return this.tImage.createGraphics();
  }

  @Override
  public RenderedImage getRenderedImage() {
    return this.tImage;
  }

  @Override
  public float[] getGrayOffsets() {
    float[] offset = new float[3];
    offset[0] = 10.0f; offset[2] = 10.0f; offset[1] = 10.0f;
    return offset;
  }

  @Override
  public float[] getCyanOffsets() {
    float[] offset = new float[3];
    //offset[0] = -50.0f; offset[1] = -20.0f; offset[2] = 0.0f;
    // offset[0] = -40.0f; offset[2] = -10.0f; offset[1] = 10.0f; //piu scuro
    offset[0] = -20.0f; offset[2] = 10.0f; offset[1] = 30.0f;
    return offset;
  }

  @Override
  public float[] getSeppiaOffsets() {
    float[] offset = new float[3];
    // offset[0] = 25.0f; offset[2] = 5.0f; offset[1] = -25.0f; piu scuro
    offset[0] = 35.0f; offset[2] = 15.0f; offset[1] = -15.0f;
    return offset;
  }

  @Override
  public void setData(Raster r) {
    this.tImage.setData(r);
  }

  @Override
  public Raster getData() {
    return this.tImage.getData();
  }

  @Override
  public void setRGBLevels(GenericImage srcRGB) {
    TiledImage transparencyImage = this.tImage.getSubImage(new int[] {3}, null);
    this.tImage = JAIUtils.addTransparencyMask(srcRGB.getTiledImage(), transparencyImage);
  }

  @Override
  public void saveAs(String filePath) {
    //BufferedImageUtils.saveImage(this.tImage,BufferedImageUtils.getDefaultMetadata(), filePath, 0.99F);
    this.saveAs(filePath,  0.99F);
  }

  @Override
  public void saveAs(String filePath, float quality) {
    BufferedImageUtils.saveImage(this.tImage,BufferedImageUtils.getDefaultMetadata(), filePath, quality);
  }

  @Override
  public byte[] saveToByteArray(Float quality, boolean addMetadata) {
    if(addMetadata){

      return JAIUtils.saveToByteArray(tImage, BufferedImageUtils.getDefaultMetadata(), quality);
    } else {
      return JAIUtils.saveToByteArray(this.tImage, quality);
    }

  }

  @Override
  public void saveAsWithoutIcc(String filePath) {
    //JAIUtils.save(tImage, filePath, quality);
    JAIUtils.save(this.tImage, filePath);
  }

  @Override
  public void saveAsWithoutIcc(String filePath, float quality) {
    JAIUtils.save(this.tImage, filePath, quality);
  }
  
  @Override
  public GenericImage clone() {
    GenericImage result;
    result = this.getSubimage(0, 0, this.tImage.getWidth(), this.tImage.getHeight());

    this.cloneGeneric(result);

    return result;
  }

  @Override
  public GenericImage grayScale() {
    this.__tImage = JAIUtils.Gray(this.tImage, false);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage grayScaleSingleLayer() {
    this.__tImage = JAIUtils.Gray(this.tImage, true);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage rotate(double angleRotation) {
    this.__tImage = JAIUtils.rotate(this.tImage, angleRotation);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage rotate(double angleRotation, Dimension size) {
    this.__tImage = JAIUtils.rotate(this.tImage, angleRotation, size);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage scale(double scaleFactor) {
    this.__tImage = JAIUtils.scale(this.tImage, scaleFactor);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage resizeImageStretched(int width, int height) {
    this.__tImage = JAIUtils.stretchImageLowQuality(this.tImage, width, height);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage addTrasparency() {
    this.__tImage = JAIUtils.addTransparency(this.tImage);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage addTrasparencyMask(GenericImage trasparencyMask) {
    this.__tImage = JAIUtils.addTransparencyMask(this.tImage, trasparencyMask.getTiledImage());
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage applyTrasparencyMask(GenericImage trasparencyMask) {
    this.__tImage = JAIUtils.applyTransparencyMask(this.tImage, trasparencyMask.getTiledImage());
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage getSubimage(int x, int y, int width, int height) {
    //this.__tImage = this.tImage.getSubImage(x, y, width, height);
    this.__tImage = JAIUtils.cropTiledImage(this.tImage, x, y, width, height, true);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage getShadowMask(int shadowLengthPixels, int transpValue) {
    int fattIngr = 6;
    /*/
    this.__tImage = JAIUtils.buildTiledImage(PlanarImage.wrapRenderedImage(BufferedImageUtils.createSmallShadowMask(this.getBufferedImage(), shadowLengthPixels, transpValue, fattIngr)));
    return new GenericJaiImage(this.__tImage);
    //*/
    return getShadowMask(shadowLengthPixels, transpValue, fattIngr);
  }

  //*****************************************************/
  @Override
  public GenericImage getShadowMask(int shadowLengthPixels, int transpValue, int fattoreRisparmioOmbra) {
    this.__tImage = JAIUtils.buildTiledImage(PlanarImage.wrapRenderedImage(BufferedImageUtils.createSmallShadowMask(this.getBufferedImage(), shadowLengthPixels, transpValue, fattoreRisparmioOmbra)));
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage getAlphaLaier() {
    this.__tImage = JAIUtils.alphaLayer(this.tImage);
    return new GenericJaiImage(this.__tImage);
  }

  @Override
  public GenericImage translate(Point translation) {
    this.__tImage = JAIUtils.translate(this.tImage, translation);
    return new GenericJaiImage(this.__tImage);
  }




}
