/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.JPEGEncodeParam;
import exceptions.C_CMYKProfileException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.metadata.IIOMetadata;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationBilinear;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.RasterFactory;
import javax.media.jai.RenderedOp;
import javax.media.jai.TiledImage;

/**
 *
 * @author Mario
 */
public class JAIUtils {

    
  //*****************************************************
  public static TiledImage loadTiledImage(String imagePath){

    RenderedOp imgTmp = load(imagePath);
    TiledImage result = null;
    try{
      if(imagePath.toLowerCase().endsWith(".png") || imagePath.toLowerCase().endsWith(".xng")){
        result = JAIUtils.newTiledImage(imgTmp.getWidth(), imgTmp.getHeight(), 4);
        Graphics2D g2d = result.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY); //******mario
        g2d.drawRenderedImage(imgTmp, new AffineTransform());
        g2d.dispose();

      } else {
        result = buildTiledImage(imgTmp);
      }

      if(imagePath.toLowerCase().endsWith(".jpg") || imagePath.toLowerCase().endsWith(".jpeg")){
        if(result.getNumBands() == 4){
          throw new C_CMYKProfileException("Il file jpeg e' di un formato non valido, probabilmente e' in formato CMYK, corvertire l'immagine in RGB");
        }
      }


    }catch(Throwable ex){
      //System.out.println("\n******Errore nel caricamento dell'immagine "+imagePath);
      throw new RuntimeException("******Errore nel caricamento dell'immagine "+imagePath+" ************",ex);
    }
    return result;
  }
    
  //************************************************/
  public static TiledImage newTiledImage(int w, int h, int numBands) {

    Rectangle bounds = new Rectangle(0, 0, w, h);
    SampleModel sm = RasterFactory.createPixelInterleavedSampleModel(DataBuffer.TYPE_BYTE, 256, 256, numBands);


    ColorModel cm = PlanarImage.createColorModel(sm);
    TiledImage constantImage = new TiledImage(bounds.x, bounds.y, bounds.width, bounds.height, bounds.x, bounds.y, sm, cm);

    return constantImage;
  }

  
  //*****************************************************
  public static RenderedOp getPlanarImage(byte fileBuffer[]){
    RenderedOp result = null;

    try {
      result = getImageWidthEx(fileBuffer);
    } catch(IOException ex1) {
      ex1.printStackTrace();
    }

    return result;
  }


  //*****************************************************
  public static RenderedOp getImageWidthEx(byte fileBuffer[]) throws IOException {
    RenderedOp result = null;
    ByteArraySeekableStream fss = null;
    fss = new ByteArraySeekableStream(fileBuffer);
    result = JAI.create("stream", fss); //to load the image from a filestream

    return result;
  }
  
  
  //*****************************************************
  public static RenderedOp load(String imagePath){
    RenderedOp result = null;

    result = JAI.create("fileload", imagePath);


    return result;
  }

  //*****************************************************
  public static void save(RenderedOp image, String imagePath){
    save((PlanarImage)image, imagePath);
  }

  //*****************************************************
  public static void save(PlanarImage image, String imagePath){
    save(image, imagePath, 0F);
  }

  //*****************************************************
  public static void save(PlanarImage image, String imagePath, float quality){
    FileOutputStream os = null;
    String type = null;

    try {
      os = new FileOutputStream(imagePath);
    } catch(FileNotFoundException ex) {
      throw new RuntimeException(ex);
    }

    if(imagePath.toLowerCase().endsWith(".png")){
      type = "PNG";
    } else {
      type = "JPEG";
    }
    save(image, os, type, quality);
  }

  //*****************************************************
  public static void save(PlanarImage image, OutputStream os, String type, float quality){
    if(quality == 0){
      quality = 0.99F;
    }

    if(type == null){
      type = "JPEG";
    }


    if(type.toLowerCase().endsWith("png")){
      JAI.create("encode", image, os, "PNG", null); //encode and write the image
    } else {
      JPEGEncodeParam jpe = new JPEGEncodeParam();
      jpe.setQuality(quality); // set the image compression quality
      JAI.create("encode", image, os, "JPEG", jpe); //encode and write the image
    }
    try {
      os.close();
    } catch(IOException ex1) {
      //todo: manage it
    }
//      }
  }
  
  //*****************************************************
  public static byte[] saveToByteArray(PlanarImage image, float quality){
    return saveToByteArray(image, (String)null, quality);
  }

  //*****************************************************
  public static byte[] saveToByteArray(PlanarImage image, String type, float quality){
    ByteArrayOutputStream os = new ByteArrayOutputStream();


    save(image, os, type, quality);
    return os.toByteArray();
  }
  
  //*****************************************************
  public static byte[] saveToByteArray(PlanarImage image, IIOMetadata metadata){
    return BufferedImageUtils.saveImageToByteArray(image, metadata);
  }

  //*****************************************************
  public static byte[] saveToByteArray(PlanarImage image, IIOMetadata metadata,float quality){
    return BufferedImageUtils.saveImageToByteArray(image, metadata,quality);
  }
  
  
  //*****************************************************
  public static RenderedOp convertBufferedImage(BufferedImage biImage){
    ParameterBlockJAI pb = new ParameterBlockJAI("awtImage");
    pb.setParameter("awtImage", biImage);
    RenderedOp roImage = JAI.create("awtImage", pb);

    return roImage;
  }

  
  //************************************************/
  public static TiledImage buildTiledImage(PlanarImage source){
    return new TiledImage(source, true);
  }

  
  //*****************************************************
  public static BufferedImage resize(BufferedImage biImage, int maxW, int maxH){

    ParameterBlockJAI pb = new ParameterBlockJAI("awtImage");
    pb.setParameter("awtImage", biImage);
    RenderedOp roImage = JAI.create("awtImage", pb);
    roImage = resize(roImage, maxW, maxH);
    BufferedImage biResult = roImage.getAsBufferedImage();
    roImage.dispose();
    return biResult;
  }


  //*****************************************************
  public static BufferedImage resize(BufferedImage biImage,  double scaleFactor){

    ParameterBlockJAI pb = new ParameterBlockJAI("awtImage");
    pb.setParameter("awtImage", biImage);
    RenderedOp roImage = JAI.create("awtImage", pb);
    roImage = resize(roImage, scaleFactor);

    return roImage.getAsBufferedImage();
  }

  
  //*****************************************************
  public static RenderedOp resize(RenderedOp image, int maxW, int maxH){

    //float scaleFactor = (float)getScale(maxW, maxH, image);

    float scaleFactor = (float)Sizes.getScaleFactor(maxW, maxH, image.getWidth(), image.getHeight());

    return resize(image, scaleFactor);

  }


  //*****************************************************
  public static RenderedOp resize(RenderedOp image, double scaleFactor){
    RenderedOp result = null;

    ParameterBlock params = new ParameterBlock();
    params.addSource(image);
    params.add((float)scaleFactor); //x scale factor
    params.add((float)scaleFactor); //y scale factor
    params.add(0.0F); //x translate
    params.add(0.0F); //y translate
    params.add(new InterpolationBilinear()); //better quality interpolation
    //      params.add(new InterpolationNearest()); //better quality interpolation

    result = JAI.create("scale", params); //actually reduce the image

    return result;
  }
  
  public static long getRGB(TiledImage image, int x, int y) {
    int []iRGB;
    long result;

    int xIndex = image.XToTileX(x);
    int yIndex = image.YToTileY(y);
    WritableRaster tileRaster = image.getWritableTile(xIndex, yIndex);
    if (tileRaster != null) {
      iRGB = tileRaster.getPixel(x, y, (int[]) null);
      image.releaseWritableTile(xIndex, yIndex);
      if(iRGB.length >= 4){
        result = (new Color(iRGB[0], iRGB[1], iRGB[2], iRGB[3])).getRGB();
      } else {
        result = (new Color(iRGB[0], iRGB[1], iRGB[2])).getRGB();
      }
    } else {
      throw new RuntimeException("impossibile recuperare il pixel");
    }

    return result;
  }
 
  //************************************************/
  public static TiledImage newFilledTiledImage(int w, int h, int numBands, Color fillColor){
    TiledImage result = newTiledImage(w,h,numBands);

    Graphics2D g2DResult = result.createGraphics();
    g2DResult.setColor(fillColor);
    g2DResult.fillRect(0, 0, result.getWidth(), result.getHeight());
    g2DResult.dispose();

    return result;
  }

  //*********************************************
  public static TiledImage alphaLayer(TiledImage origineImage){
    TiledImage alphaImage = JAIUtils.newTiledImage(origineImage.getWidth(), origineImage.getHeight(), 1);

    alphaImage.setData(origineImage.getSubImage(new int[] {3}, null).getData());

    return  alphaImage;
  }

  //*********************************************
  public static TiledImage applyTransparencyMask(TiledImage origineImage, TiledImage maskImage){

    TiledImage alphaImage, alphaOriginalImage, rgbOriginalImage; //immagine nella quale si costruisce la maschera di trasparenza composta
    TiledImage imageTmp;   //immagine di appoggio in cui si inserisce la maschera di trasparenza e alla quale si applica lo stesso livello di trasparenza dell'immagine iniziale
    Graphics2D gr2DTmp, gr2DAlpha;
    AffineTransform a;
    int w,h;

    w = origineImage.getWidth();
    h = origineImage.getHeight();

    alphaImage = JAIUtils.newTiledImage(w, h, 1);
    imageTmp = JAIUtils.newTiledImage(w, h, 3);

    //alphaOriginalImage = origineImage.getSubImage(new int[] {3}, null);
    //imageTmp = JAIUtils.addTransparencyMask(alphaOriginalImage, maskImage);


    alphaOriginalImage = origineImage.getSubImage(new int[] {3}, null);
    //alphaOriginalImage = JAIUtils.selectBands(origineImage, new int[] {3});

    gr2DTmp = imageTmp.createGraphics();
    gr2DTmp.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.getRenderParamenter());


    a = new AffineTransform();
//    a.translate(posImage.x, posImage.y);
    gr2DTmp.drawRenderedImage(maskImage,a);
    gr2DTmp.dispose();
    imageTmp = JAIUtils.addTransparencyMask(imageTmp, alphaOriginalImage);



    gr2DAlpha = alphaImage.createGraphics();
    gr2DAlpha.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.getRenderParamenter());
//    gr2DAlpha.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    a = new AffineTransform();
    gr2DAlpha.drawRenderedImage(imageTmp, a);

    rgbOriginalImage = origineImage.getSubImage(new int[] {0,1,2}, null);
//    rgbOriginalImage = JAIUtils.selectBands(origineImage, new int[] {0,1,2});

    origineImage = JAIUtils.addTransparencyMask(rgbOriginalImage, alphaImage);

    gr2DAlpha.dispose();

    return origineImage;
  }
  
  
  //************************************************/
  public static TiledImage addTransparency(TiledImage image){
    //Add the calculated alpha layer back to the final image in preparation
    //for merging with a layer of white.

    TiledImage imageAlpha = JAIUtils.newFilledTiledImage(image.getWidth(), image.getHeight(), 1, Color.white);
    TiledImage imageResult = JAIUtils.addTransparencyMask(image, imageAlpha);

    return imageResult;
  }
  
  //************************************************/
  public static TiledImage addTransparencyMask(TiledImage image, TiledImage imageAlpha){
    //Add the calculated alpha layer back to the final image in preparation
    //for merging with a layer of white.

    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.addSource(imageAlpha);

    TiledImage imageResult = buildTiledImage(JAI.create("bandMerge", pb, null));


    return imageResult;
  }
  
  //**********************************************/
  public static TiledImage Gray(TiledImage src, boolean singleLayer) {
    TiledImage result = null;

    if(singleLayer){
      AffineTransform a;
      result = JAIUtils.newTiledImage(src.getWidth(), src.getHeight(), 1);
      Graphics2D gr2D = result.createGraphics();
      gr2D.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.getRenderParamenter());
      a = new AffineTransform();
      gr2D.drawRenderedImage(src, a);
      gr2D.dispose();
    } else {
      BufferedImage srcTmp = src.getAsBufferedImage();

      BufferedImage resultTmp = BufferedImageUtils.grayScale(srcTmp);

      Graphics2D gr2D = srcTmp.createGraphics();
      gr2D.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.getRenderParamenter());
      gr2D.drawImage(resultTmp, 0, 0, null);
      gr2D.dispose();
      result = JAIUtils.buildTiledImage(PlanarImage.wrapRenderedImage(srcTmp));
    }
    return result;
  }  
  
  //****************************************************
  public static TiledImage rotate(TiledImage src, double angle) {
    return rotate(src, angle, null);
  }

  //****************************************************
  public static TiledImage rotate(TiledImage src, double angle, Dimension expectedSize) {
    TiledImage result = null;
    Dimension sizeResult = null;

    if(angle == 0) {
      return src;
    }
    //todo: se angle e' = 0 si potrebbe evitare di fare la rotazione

    AffineTransform rotateTrans = new AffineTransform();
    rotateTrans.rotate(angle, src.getWidth() / 2.0, src.getHeight() / 2.0);
    rotateTrans.preConcatenate(BufferedImageUtils.findTranslation(rotateTrans, src.getWidth(), src.getHeight()));

    if (expectedSize == null) {
      sizeResult = BufferedImageUtils.findSize(rotateTrans,new Dimension(src.getWidth(), src.getHeight()));
    } else {
      sizeResult = expectedSize;
    }

    //result = new BufferedImage(sizeResult.width, sizeResult.height, src.getNumBands());
    result = JAIUtils.newTiledImage(sizeResult.width, sizeResult.height, src.getNumBands());

    Graphics2D g2d = result.createGraphics();
    //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); //non ha effetto
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//      g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);

//     g2d.drawImage(src, rotateTrans, null);
    g2d.drawRenderedImage(src, rotateTrans);
    g2d.dispose();

    return result;

  }

  
  //*****************************************************/
  public static PlanarImage scale(PlanarImage image, double scaleFactor) {
    return scale( (Object) image, scaleFactor, false);
  }

//*****************************************************/
  public static TiledImage scale(TiledImage image, int maxW,int maxH) {
    //double scale = getScale(maxW, maxH, image);
    double scale = (float)Sizes.getScaleFactor(maxW, maxH, image.getWidth(), image.getHeight());

    return scale(image,scale);
  }

  //*****************************************************/
  public static TiledImage scale(TiledImage image, double scaleFactor) {
    return scale(image, scaleFactor, false);
  }

  //*****************************************************/
  public static TiledImage scale(TiledImage image, double scaleFactor, boolean notCorrection) {
    return JAIUtils.buildTiledImage(scale((Object) image, scaleFactor, notCorrection));
  }
  
 //*****************************************************/
  private static PlanarImage scale(Object image, double scaleFactor, boolean notCorrection) {
    PlanarImage result = (PlanarImage)image;
    double limiteResize = 0.5;

    if(BufferedImageUtils.getRenderParamenter() == RenderingHints.VALUE_RENDER_QUALITY && scaleFactor<1){
      if(scaleFactor < limiteResize){
        result = scaleSubsample(result, scaleFactor);
      } else {
        result = scaleSimple(result, scaleFactor, notCorrection);
      }
          //result = scaleSimple(result, scaleFactor, true);

    } else {
      result = scaleSimple(result, scaleFactor, notCorrection);
      //result = scaleSubsample(result, scaleFactor);
    }

    return result;
  }
 
//*****************************************************/
  public static PlanarImage scaleSimple(TiledImage image, int maxW,int maxH) {
    //double scale = getScale(maxW, maxH, image);
    double scale = (float)Sizes.getScaleFactor(maxW, maxH, image.getWidth(), image.getHeight());

    return scaleSimple(image,scale, false);
  }

  //*****************************************************/
  public static PlanarImage scaleSimple(Object image, double scaleFactor, boolean notCorrection) {
    RenderingHints qualityHints = null;


    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add((float)scaleFactor);
    pb.add((float)scaleFactor);
    pb.add(0F);
    pb.add(0F);
    if(!notCorrection){
      pb.add(Interpolation.getInstance(Interpolation.INTERP_BILINEAR));
    }
    return JAI.create("Scale", pb, qualityHints);
  }

//*****************************************************/
  public static PlanarImage scaleSubsample(TiledImage image, int maxW,int maxH) {
    //double scale = getScale(maxW, maxH, image);
    double scale = (float)Sizes.getScaleFactor(maxW, maxH, image.getWidth(), image.getHeight());

    return scaleSubsample(image,scale);
  }

  //*****************************************************/
  //*/
  public static PlanarImage scaleSubsample(Object image, double scaleFactor) {

    RenderingHints qualityHints = null;

    if(BufferedImageUtils.getRenderParamenter() == RenderingHints.VALUE_RENDER_QUALITY ){
      qualityHints = new RenderingHints(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);
      qualityHints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    ParameterBlock pb = new ParameterBlock();
    pb.addSource(image);
    pb.add(scaleFactor);
    pb.add(scaleFactor);
    return JAI.create("SubsampleAverage", pb, qualityHints);

   }

   //*****************************************************
  public static BufferedImage scaleSubsample(BufferedImage biImage,  double scaleFactor){

    ParameterBlockJAI pb = new ParameterBlockJAI("awtImage");
    pb.setParameter("awtImage", biImage);
    RenderedOp roImage = JAI.create("awtImage", pb);
    roImage = (RenderedOp) scaleSubsample(roImage, scaleFactor);

    return roImage.getAsBufferedImage();
  }
 
  //*****************************************************/
  public static TiledImage stretchImageLowQuality(TiledImage inImage, int width, int height) {
    double[] scales = new double[2];
    scales[0] = ( (double) width) / inImage.getWidth();
    scales[1] = ( (double) height) / inImage.getHeight();

    // Create an image buffer in which to paint on.
    TiledImage outImage = JAIUtils.newTiledImage(width, height, inImage.getNumBands());

    // Set the scale.
    AffineTransform tx = new AffineTransform();
    tx.scale(scales[0], scales[1]);

    // Paint image.
    Graphics2D g2d = outImage.createGraphics();


    //********************************
    //lascio il render speed perchè viene usato solo da due effetti e se non metto questo le maschere di trasparenza non funzionano bene
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    //********************************

    g2d.drawRenderedImage(inImage, tx);
    g2d.dispose();

    return outImage;
  }

  
  //*****************************************************
  public static TiledImage cropTiledImage(TiledImage image,int x,int y,int w,int h){
    return cropTiledImage(image, x, y, w, h, false);
  }

    //*****************************************************
  public static TiledImage cropTiledImage(TiledImage image,int x,int y,int w,int h, boolean clone){

    if(x==0 && y==0 && w==image.getWidth() && h==image.getHeight() && !clone){
      return image;
    }


    TiledImage subImage = image.getSubImage(x,y,w,h);

    TiledImage result = newTiledImage(subImage.getWidth(),subImage.getHeight(),subImage.getNumBands());
    Graphics2D g2D = result.createGraphics();
    g2D.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.getRenderParamenter()); //******mario


    AffineTransform at = new AffineTransform();
    at.translate(-x,-y);
    g2D.drawRenderedImage(subImage,at);
    g2D.dispose();

    return result;
  }

  
  //**********************************************/
  public static TiledImage translate(TiledImage src, Point translation) {
    ParameterBlock pb = new ParameterBlock();
    pb.addSource(src);
    pb.add(((float)translation.x));
    pb.add(((float)translation.y));

    src = JAIUtils.buildTiledImage(JAI.create("translate",pb,null));

    return src;
  }

  
  
}
