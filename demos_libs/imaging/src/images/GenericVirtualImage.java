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
import java.io.File;
import javax.media.jai.TiledImage;
import utils.BufferedImageUtils;


/**
 *
 * @author developer
 */
public class GenericVirtualImage extends GenericImage {
  private Dimension size;
  private int numLivelli;

  //*****************************************************/
  public GenericVirtualImage(int width, int height, int numLivelli) {
    this.size = new Dimension(width, height);
    this.numLivelli = numLivelli;
  }

  //*****************************************************/
  public GenericVirtualImage(GenericImage source) {
    this(source.getWidth(), source.getHeight(), source.getNumLivelli());
  }

  //*****************************************************/
  public GenericVirtualImage(String pathImage) {

    if( (new File(pathImage)).exists() ){
      this.size = BufferedImageUtils.getImageSize(pathImage);
    } else {
      this.size = new Dimension(9999, 9999);
      //new RuntimeException("ATTENZIONE: creo una virtualmage per un file INESISTENTE").printStackTrace();
    }

    if(this.size == null){
      throw new RuntimeException("impossibile ottenere la realSize dell'immagine: "+pathImage);
    }

    if(pathImage.toLowerCase().endsWith(".png") || pathImage.toLowerCase().endsWith(".xng")){
      this.numLivelli = 4;
    } else {
      this.numLivelli = 3;
    }

  }


  //*****************************************************/
  @Override
  public BufferedImage getBufferedImage() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //*****************************************************/
  @Override
  public TiledImage getTiledImage() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //*****************************************************/
  @Override
  public int getWidth() {
    return this.size.width;
  }

  //*****************************************************/
  @Override
  public int getHeight() {
    return this.size.height;
  }

  //*****************************************************/
  @Override
  public int getNumLivelli() {
    return this.numLivelli;
  }

  //*****************************************************/
  @Override
  public long getRGB(int x, int y) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //*****************************************************/
  @Override
  public Graphics2D createGraphics() {
    return new VirtualGraphics2D();
  }

  //*****************************************************/
  @Override
  public RenderedImage getRenderedImage() {
    return null;
  }

  //*****************************************************/
  @Override
  public float[] getGrayOffsets() {
    //sono quelli di JAI
    float[] offset = new float[3];
    offset[0] = 10.0f; offset[2] = 10.0f; offset[1] = 10.0f;
    return offset;
  }

  //*****************************************************/
  @Override
  public float[] getCyanOffsets() {
    //sono quelli di JAI
    float[] offset = new float[3];
    offset[0] = -20.0f; offset[2] = 10.0f; offset[1] = 30.0f;
    return offset;
  }

  //*****************************************************/
  @Override
  public float[] getSeppiaOffsets() {
    //sono quelli di JAI
    float[] offset = new float[3];
    offset[0] = 35.0f; offset[2] = 15.0f; offset[1] = -15.0f;
    return offset;
  }

  //*****************************************************/
  @Override
  public void setData(Raster r) {
    //non faccio niente
  }

  //*****************************************************/
  @Override
  public Raster getData() {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  //*****************************************************/
  @Override
  public void setRGBLevels(GenericImage srcRGB) {
    //non faccio niente
  }

  //*****************************************************/
  @Override
  public void saveAs(String filePath) {
    //non faccio niente
  }

  //*****************************************************/
  @Override
  public void saveAs(String filePath, float quality) {
    //non faccio niente
  }

  //*************************************************/
  @Override
  public byte[] saveToByteArray(Float quality, boolean addMetadata) {
    return null;
  }

  //*****************************************************/
  @Override
  public void saveAsWithoutIcc(String filePath) {
    //non faccio niente
  }

  //*****************************************************/
  @Override
  public void saveAsWithoutIcc(String filePath, float quality) {
    //non faccio niente
  }

  //*****************************************************/
  @Override
  public GenericImage clone() {
    GenericImage result;
    result = new GenericVirtualImage(this.size.width, this.size.height, numLivelli);

    this.cloneGeneric(result);

    return result;
  }

  //*****************************************************/
  @Override
  public GenericImage grayScale() {
    return new GenericVirtualImage(this.size.width, this.size.height, 3);
  }

  //*****************************************************/
  @Override
  public GenericImage grayScaleSingleLayer() {
    return new GenericVirtualImage(this.size.width, this.size.height, 1);
  }

  //*****************************************************/
  @Override
  public GenericImage rotate(double angleRotation) {
    Dimension dimTmp = BufferedImageUtils.findRotatedSize(this.getDimension(), angleRotation);
    return new GenericVirtualImage(dimTmp.width, dimTmp.height, this.numLivelli);
  }

  //*****************************************************/
  @Override
  public GenericImage rotate(double angleRotation, Dimension size) {
    if(size != null){
      return new GenericVirtualImage(size.width, size.height, this.numLivelli);
    } else {
      return rotate(angleRotation);
    }
  }

  //*****************************************************/
  @Override
  public GenericImage scale(double scaleFactor) {
    return new GenericVirtualImage((int)Math.round(this.size.width * scaleFactor), (int)Math.round(this.size.height * scaleFactor), this.numLivelli);
  }

  //*****************************************************/
  @Override
  public GenericImage resize(int width, int height) {
    return new GenericVirtualImage(width, height, this.numLivelli);
  }

  //*****************************************************/
  @Override
  public GenericImage resizeImageStretched(int width, int height) {
    return resize(width, height);
  }

  //*****************************************************/
  @Override
  public GenericImage addTrasparency() {
    if(this.numLivelli != 3){
      throw new RuntimeException("l'immagine non ha 3 livelli, impossibile effettuare l'operazione");
    }
    return new GenericVirtualImage(this.size.width, this.size.height, 4);
  }

  //*****************************************************/
  @Override
  public GenericImage addTrasparencyMask(GenericImage trasparencyMask) {
    if(this.numLivelli != 3){
      throw new RuntimeException("l'immagine non ha 3 livelli, impossibile effettuare l'operazione");
    }
    return new GenericVirtualImage(this.size.width, this.size.height, 4);
  }

  //*****************************************************/
  @Override
  public GenericImage applyTrasparencyMask(GenericImage trasparencyMask) {
    return this.clone();
  }

  //*****************************************************/
  @Override
  public GenericImage getSubimage(int x, int y, int width, int height) {
    return new GenericVirtualImage(width, height, this.numLivelli);
  }

  //*****************************************************/
  @Override
  public GenericImage getShadowMask(int shadowLengthPixels, int transpValue) {
    return new GenericVirtualImage(this.size.width + shadowLengthPixels, this.size.height + shadowLengthPixels, 1);
  }

  //*****************************************************/
  @Override
  public GenericImage getShadowMask(int shadowLengthPixels, int transpValue, int fattoreRisparmioOmbra) {
    return new GenericVirtualImage(this.size.width + shadowLengthPixels, this.size.height + shadowLengthPixels, 1);
  }

  //*****************************************************/
  @Override
  public GenericImage getAlphaLaier() {
    GenericVirtualImage result = (GenericVirtualImage) this.clone();
    result.numLivelli = 1;
    return result;
  }

  //*****************************************************/
  @Override
  public GenericImage translate(Point translation) {
    return this.clone();
  }


  //*************************************************/
  @Override //maschero un metodo implementato in GenericImage
  public GenericImage applyRGBOffset(float [] offset){
    GenericImage rgbImage = new GenericVirtualImage(this.getWidth(), this.getHeight(), 3);

    return rgbImage;
  }

  //*************************************************/
  @Override
  public boolean isDrawable(){
    return false;
  }



}
