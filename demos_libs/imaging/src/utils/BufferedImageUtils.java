/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import exceptions.C_CMYKProfileException;
import exceptions.C_RuntimeException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.media.jai.RenderedOp;
import org.w3c.dom.Node;

/**
 *
 * @author Mario
 */
public class BufferedImageUtils {

  private static Object renderParameter = RenderingHints.VALUE_RENDER_SPEED;
    
  //**************************************************
  public static void setMaxQuality() {
    renderParameter = RenderingHints.VALUE_RENDER_QUALITY;
  }

  //**************************************************
  public static boolean isMaxQuality() {
    return renderParameter == RenderingHints.VALUE_RENDER_QUALITY;
  }

  //**************************************************
  public static void setMaxSpped() {
     renderParameter = RenderingHints.VALUE_RENDER_SPEED;
  }
  
  //**************************************************
  public static Object getRenderParamenter() {
     return renderParameter;
  }

  //**************************************************
  public static void setRenderParamenter(Object renderParam) {
     renderParameter = renderParam;
  }
  
    
  //*********************************************
  public static Dimension getImageSize(String imagePath) {
    Dimension imageSize = null;

    String imagePathLowerCase = imagePath.toLowerCase();

    if ((imagePathLowerCase.endsWith(".jpg") || imagePathLowerCase.endsWith(".jpeg"))) {
      try {
        ImageReader reader = ImageIO.getImageReadersByFormatName("JPEG").next();

        //reader.setInput(new JpegFilterInputStream(new FileInputStream(new File(imagePath)),markers));
        FileImageInputStream fIS = new FileImageInputStream(new File(imagePath));
        reader.setInput(fIS);

        //         IIOImage iimage = reader.readAll(0, new JPEGImageReadParam());
        imageSize = new Dimension(reader.getWidth(0), reader.getHeight(0));
        reader.dispose();
        fIS.close();
      } catch (Throwable ex) {
        System.out.println("Errore nel recupero della Dimension di " + imagePath + "dal suo header. La recupero aprendo l'immagine.");
        //GenericImage image = GenericImageFactory.readImage(imagePath);
        //imageSize = image.getDimension();
        imageSize = getImageSizeFromImage(imagePath);
      }

    } else if (imagePathLowerCase.endsWith(".tif") || imagePathLowerCase.endsWith(".tiff")){
      try {
        ImageReader reader = ImageIO.getImageReadersByFormatName("tif").next();

        //reader.setInput(new JpegFilterInputStream(new FileInputStream(new File(imagePath)),markers));
        FileImageInputStream fIS = new FileImageInputStream(new File(imagePath));
        reader.setInput(fIS);

        //         IIOImage iimage = reader.readAll(0, new JPEGImageReadParam());
        imageSize = new Dimension(reader.getWidth(0), reader.getHeight(0));
        reader.dispose();
        fIS.close();
      } catch (Throwable ex) {
        System.out.println("Errore nel recupero della Dimension di " + imagePath + "dal suo header. La recupero aprendo l'immagine.");
        imageSize = getImageSizeFromImage(imagePath);
      }
    } else if (imagePathLowerCase.endsWith(".png")){
      try {
        ImageReader reader = ImageIO.getImageReadersByFormatName("PNG").next();

        //reader.setInput(new JpegFilterInputStream(new FileInputStream(new File(imagePath)),markers));
        FileImageInputStream fIS = new FileImageInputStream(new File(imagePath));
        reader.setInput(fIS);

        //         IIOImage iimage = reader.readAll(0, new JPEGImageReadParam());
        imageSize = new Dimension(reader.getWidth(0), reader.getHeight(0));
        reader.dispose();
        fIS.close();
      } catch (Throwable ex) {
        System.out.println("Errore nel recupero della Dimension di " + imagePath + "dal suo header. La recupero aprendo l'immagine.");
        imageSize = getImageSizeFromImage(imagePath);
      }
    } else {
      throw new RuntimeException("Unknown file tipe");
    }

    return imageSize;
  }
 
  //*********************************************
  public static BufferedImage readImage(String pathImage) {
    BufferedImage result = null;

    String pathImageLowerCase = pathImage.toLowerCase();

    if (pathImageLowerCase.endsWith(".png")) {
      File fileImage = new File(pathImage);
      try {
        result = ImageIO.read(fileImage);
      } catch (Exception ex) {
        throw new RuntimeException(ex.getMessage() + ":" + pathImage, ex);
      }
    } else if (pathImageLowerCase.endsWith(".tif") || pathImageLowerCase.endsWith(".tiff")) {
      result = JAIUtils.load(pathImage).getAsBufferedImage();
    } else {//jpg
      FileInputStream fis = null;
      try {
        fis = new FileInputStream(pathImage);
        com.sun.image.codec.jpeg.JPEGImageDecoder decoder = com.sun.image.codec.jpeg.JPEGCodec.createJPEGDecoder(fis);

        result = decoder.decodeAsBufferedImage();
        fis.close();
      } catch (Exception ex) {
        //ex.printStackTrace();
        throw new RuntimeException(ex.getMessage() + ":" + pathImage, ex);
      }

      if(getNumLivelliByType(result.getType()) == 4){
        throw new C_CMYKProfileException("Il file jpeg e' di un formato non valido, probabilmente e' in formato CMYK, corvertire l'immagine in RGB");
      }
    }

    if (result.getType() == 0 || result.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
      BufferedImage imgTmp = new BufferedImage(result.getWidth(), result.getHeight(), BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = imgTmp.createGraphics();
      g2d.drawImage(result, 0, 0, null);
      g2d.dispose();
      result = imgTmp;
    }

    return result;
  }
  

  /***********************************************************/
  public static Dimension getImageSizeFromImage(String imgFilePath) {
    Dimension result = new Dimension();

    RenderedOp img = JAIUtils.load(imgFilePath);

    result = new Dimension(img.getWidth(), img.getHeight());
    img.dispose();
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

  
  
  //*******************************************
  public static void saveImage(BufferedImage imageToSave, String imageName) {
    saveImage(imageToSave, imageName, 0);
  }

  //*******************************************
  public static void saveImage(BufferedImage imageToSave, String imageName, float quality) {
    saveImage(imageToSave, imageName, quality, null);
  }

  //*******************************************
  public static void saveImage(BufferedImage imageToSave, String imageName, float quality, String type) {
    if (quality == 0) {
      quality = 0.99F;
    }

    String extn = imageName.substring(imageName.lastIndexOf('.') + 1).toLowerCase();
    if (extn.equals("jpg") || extn.equals("jpeg") || (type != null && type.equals("jpg"))) {

      try {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
        JPEGEncodeParam p = encoder.getDefaultJPEGEncodeParam(imageToSave);
        // set JPEG quality to 50% with baseline optimization
        p.setQuality(quality, true);
        encoder.setJPEGEncodeParam(p);
        encoder.encode(imageToSave);

        File file = new File(imageName);
        file.delete();
        FileOutputStream fo = new FileOutputStream(file);
        out.writeTo(fo);
        fo.close();
        out.close();

      } catch (IOException ioe) {
        //TODO: gestire l'eccezione
        System.out.println(ioe);
        throw new RuntimeException(ioe);
          
      }
    } else if (extn.equals("xpg")) {
      byte buff[] = saveImageToByteArray(imageToSave, quality);
      buff = FileUtilities.cifraByteArray(buff);
      FileUtilities.saveFileFromBuffer(buff, imageName);

    } else if (extn.equals("png") || (type != null && type.equals("png"))) {
      try {
        ImageIO.write(imageToSave, "png", new File(imageName));
      } catch (IOException ex1) {
        throw new RuntimeException("impossibile salvare il file " + imageName, ex1);
      }

    } else {
    //TODO: gestire
    }

  }

  //*******************************************
  public static void saveImage(BufferedImage imageToSave, IIOMetadata imageMetaData, String imageName) {
    saveImage( imageToSave, imageMetaData, imageName, 0.985F);
  }

  //*******************************************
  public static void saveImage(BufferedImage imageToSave, IIOMetadata imageMetaData, String imageName, float quality) {

    if (imageMetaData == null) {
      saveImage(imageToSave, imageName, quality);
    } else {
      saveImage((RenderedImage) imageToSave, imageMetaData, null, imageName, quality);
    }

  }

  //*******************************************
  public static void saveImage(RenderedImage imageToSave, IIOMetadata imageMetaData, String imageName) {
    saveImage(imageToSave, imageMetaData, imageName, 0.985F);
  }

  //*******************************************
  public static void saveImage(RenderedImage imageToSave, IIOMetadata imageMetaData, String imageName, float quality) {
    saveImage(imageToSave, imageMetaData, null, imageName, quality);
  }

  //*******************************************
  public static void saveImage(RenderedImage imageToSave, IIOMetadata imageMetaData, ImageOutputStream fiosOutuputImage, float quality) {
    saveImage(imageToSave, imageMetaData, fiosOutuputImage, null, quality);
  }

  //*******************************************
  public static void saveImage(RenderedImage imageToSave, IIOMetadata imageMetaData, ImageOutputStream fiosOutuputImage) {
    saveImage(imageToSave, imageMetaData, fiosOutuputImage, null);
  }

  public static void saveImage(RenderedImage imageToSave, IIOMetadata imageMetaData, ImageOutputStream fiosOutuputImage, String imageName) {
    saveImage(imageToSave, imageMetaData, fiosOutuputImage, imageName, 0.985F);
  }

  //*******************************************
  public static void saveImage(RenderedImage imageToSave, IIOMetadata imageMetaData, ImageOutputStream fiosOutuputImage, String imageName, float quality) {

    ImageWriter writer = ImageIO.getImageWritersByFormatName("JPEG").next();
    IIOImage iimageFinal;
    JPEGImageWriteParam iwp;


    try {

      iwp = new JPEGImageWriteParam(Locale.getDefault()); //questo serve per correggere alcuni errori che si hanno quando la la buffered image è stata passata per JAI
      iwp.setOptimizeHuffmanTables(true);



      if (imageMetaData != null) {




        //questo funziona
        IIOMetadata metadataTmp = writer.getDefaultImageMetadata(new ImageTypeSpecifier(imageToSave), iwp);
        String[] formatNames = imageMetaData.getMetadataFormatNames();
        Node[] nodes = new Node[formatNames.length];
        for (int i = 0; i < formatNames.length; i++) {
          String filename = formatNames[i];
          nodes[i] = imageMetaData.getAsTree(filename);
        //xmlUtilities.writeXmlFile(nodes[i], "c:/tmp/header/xml/" + filename + ".xml", false);
        }

        Node node = metadataTmp.getAsTree(metadataTmp.getNativeMetadataFormatName());
        imageMetaData.mergeTree(metadataTmp.getNativeMetadataFormatName(), node);
        for (int i = 0; i < nodes.length - 1; i++) {
          imageMetaData.mergeTree(formatNames[i], nodes[i]);
        }

        if(quality > 0){
          iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
          iwp.setCompressionQuality(quality);
        }

      } else { //if(metadata != null)

        iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        if(quality > 0){
          iwp.setCompressionQuality(quality);
        } else {
          iwp.setCompressionQuality(0.99F);
        }
        iwp.setCompressionQuality(quality);

        IIOMetadata metadataTmp = writer.getDefaultImageMetadata(new ImageTypeSpecifier(imageToSave), iwp);

        imageMetaData = metadataTmp;

      }


      iimageFinal = new IIOImage(imageToSave, null, imageMetaData);
      if (fiosOutuputImage == null) {
        fiosOutuputImage = new FileImageOutputStream(new File(imageName));
      }
      writer.setOutput(fiosOutuputImage);

    } catch (IOException ex) {
      throw new RuntimeException(ex.getMessage() + ": impossibile salvare il file", ex);
    }


    try {
      writer.write(null, iimageFinal, iwp);
      fiosOutuputImage.close();
    } catch (IOException ex) {

      try {
        System.out.println("errore nel salvataggio, faccio un secondo tentativo.. error: " + ex.getMessage());
        SystemUtils.freeMemory();
        SystemUtils.freeMemory();

        writer.write(null, iimageFinal, iwp);
        fiosOutuputImage.close();
      } catch (IOException ex2) {
        writer.dispose();
        fiosOutuputImage = null;
        writer = null;
        System.gc();
        System.runFinalization();
        System.gc();
        if (imageName != null) {
          File fdTmp = new File(imageName);
          fdTmp.delete();
        }
        throw new RuntimeException(ex2.getMessage() + ": impossibile salvare il file", ex2);
      }

    }
    writer.dispose();

  }
  
  //*******************************************
  public static byte[] saveImageToByteArray(RenderedImage imageToSave, IIOMetadata imageMetaData, float quality) {
     byte[] result;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    OutputStream output = new BufferedOutputStream(baos);
    ImageOutputStream fiorOutuputImage;
    try {
      fiorOutuputImage = ImageIO.createImageOutputStream(output);
    } catch (IOException ex) {
      throw new C_RuntimeException("impossibile salvare il file ", ex);
    }

    saveImage(imageToSave, imageMetaData, fiorOutuputImage, null, quality);



    result = baos.toByteArray();
    try {
      //non faccio la close di fiorOutuputImage perchè va in errore
      output.close();
      baos.close();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }

    return result;
  }

  //*******************************************
  public static byte[] saveImageToByteArray(RenderedImage imageToSave, IIOMetadata imageMetaData) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    ImageOutputStream fiorOutuputImage;
    try {
      fiorOutuputImage = ImageIO.createImageOutputStream(output);
    } catch (IOException ex) {
      throw new C_RuntimeException("impossibile salvare il file ", ex);
    }

    saveImage(imageToSave, imageMetaData, fiorOutuputImage);

    return output.toByteArray();
  }
  
  //*******************************************
  public static byte[] saveImageToByteArray(BufferedImage imageToSave) {
    return saveImageToByteArray(imageToSave, (float) 0.9);
  }

  //*******************************************
  public static byte[] saveImageToByteArray(BufferedImage imageToSave, float quality) {
    byte[] result = null;
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
      JPEGEncodeParam p = encoder.getDefaultJPEGEncodeParam(imageToSave);
      // set JPEG quality to 50% with baseline optimization
      p.setQuality(quality, true);
      encoder.setJPEGEncodeParam(p);
      encoder.encode(imageToSave);
      result = out.toByteArray();
      out.close();

    } catch (IOException ioe) {
      //TODO: gestire l'eccezione
      System.out.println(ioe);
    }
    return result;
  }
  
  
  
  
  //*********************************************
  public static IIOMetadata getDefaultMetadata() {
    IIOMetadata metaData = null;

    //load the metadata for a file

    return metaData;
  }

  
  //****************************************************/
  public static BufferedImage grayScale(BufferedImage image) {
    return grayScale(image, false);
  }

  //****************************************************/
  public static BufferedImage grayScale(BufferedImage image, boolean singleLayer) {
    BufferedImage result;

    if(singleLayer){
      BufferedImage bufTmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D g2 = bufTmp.createGraphics();
      g2.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);
      g2.drawImage(image, 0, 0, null);
      g2.dispose();

      result = bufTmp;
    } else {
      ColorSpace gray_space = ColorSpace.getInstance(ColorSpace.CS_GRAY);
      ColorConvertOp convertToGrayOp = new ColorConvertOp(gray_space, null);
      result = convertToGrayOp.filter(image, null);
      result.flush();
    }

    return result;
  }
  

  
  //****************************************************
  public static BufferedImage rotateImage(BufferedImage src, double angle) {
    return rotateImage(src, angle, null);
  }

  //****************************************************
  public static BufferedImage rotateImage(BufferedImage src, double angle, Dimension expectedSize) {
    BufferedImage result = null;
    Dimension sizeResult = null;

    if (angle == 0) {
      return src;
    }
    //todo: se angle e' = 0 si potrebbe evitare di fare la rotazione

    AffineTransform rotateTrans = new AffineTransform();
    rotateTrans.rotate(angle, src.getWidth() / 2.0, src.getHeight() / 2.0);
    rotateTrans.preConcatenate(findTranslation(rotateTrans, src.getWidth(), src.getHeight()));

    if (expectedSize == null) {
      sizeResult = findSize(rotateTrans, new Dimension(src.getWidth(), src.getHeight()));
    } else {
      sizeResult = expectedSize;
    }

    result = new BufferedImage(sizeResult.width, sizeResult.height, src.getType());

    Graphics2D g2d = result.createGraphics();
    //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON); //non ha effetto
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//      g2d.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_SPEED);

    g2d.drawImage(src, rotateTrans, null);
    g2d.dispose();

    return result;
  }
  

  //****************************************************
  public static Dimension findRotatedSize(Dimension srcSize, double angle) {
    Dimension result = null;

    AffineTransform rotateTrans = new AffineTransform();
    rotateTrans.rotate(angle, srcSize.width / 2.0, srcSize.height / 2.0);
    AffineTransform atTmp = findTranslation(rotateTrans, srcSize.width, srcSize.height);
    result = findSize(rotateTrans,  srcSize );

    result.setSize(result.width + atTmp.getTranslateX(), result.height + atTmp.getTranslateY());

    return result;
  }
  

  //****************************************************
  public static Dimension findSize(AffineTransform at, Dimension bi) {
    Point2D p2din = new Point2D.Double(0.0, 0.0), p2dout = new Point2D.Double(0.0, 0.0);
    double maxX = 0, maxY = 0;
    Dimension size;

    at.transform(p2din, p2dout);
    maxX = p2dout.getX();
    maxY = p2dout.getY();

    //p2din.setLocation(bi.getWidth() - 1, 0);
    p2din.setLocation(bi.getWidth(), 0);
    at.transform(p2din, p2dout);
    if (maxX < p2dout.getX()) {
      maxX = p2dout.getX();
    }
    if (maxY < p2dout.getY()) {
      maxY = p2dout.getY();
    }

    //p2din.setLocation(0, bi.getHeight() - 1);
    p2din.setLocation(0, bi.getHeight());
    at.transform(p2din, p2dout);
    if (maxX < p2dout.getX()) {
      maxX = p2dout.getX();
    }
    if (maxY < p2dout.getY()) {
      maxY = p2dout.getY();
    }

    //p2din.setLocation(bi.getWidth() - 1, bi.getHeight() - 1);
    p2din.setLocation(bi.getWidth(), bi.getHeight());
    at.transform(p2din, p2dout);
    if (maxX < p2dout.getX()) {
      maxX = p2dout.getX();
    }
    if (maxY < p2dout.getY()) {
      maxY = p2dout.getY();
    }

    //TODO: capire se questo +1 ci vuole oppure no..... dopo aver analizzato bene sono convinto che ci voglia però occore usare -1 prima del calcolo delle rotazioni prima degli "if" di sopra
    size = new Dimension( (int)Math.round(maxX), (int) Math.round(maxY) );

    return size;
  }

  
  //****************************************************
  public static AffineTransform findTranslation(AffineTransform at, int width, int height) {
    Point2D p2din = new Point2D.Double(0.0, 0.0), p2dout = new Point2D.Double(0.0, 0.0);
    double xtrans = 0, ytrans = 0;

    at.transform(p2din, p2dout);
    xtrans = p2dout.getX();
    ytrans = p2dout.getY();

    p2din.setLocation(width, 0);
    at.transform(p2din, p2dout);
    if (xtrans >= p2dout.getX()) {
      xtrans = p2dout.getX();
    }
    if (ytrans >= p2dout.getY()) {
      ytrans = p2dout.getY();
    }

    p2din.setLocation(0, height);
    at.transform(p2din, p2dout);
    if (xtrans >= p2dout.getX()) {
      xtrans = p2dout.getX();
    }
    if (ytrans >= p2dout.getY()) {
      ytrans = p2dout.getY();
    }

    p2din.setLocation(width, height);
    at.transform(p2din, p2dout);
    if (xtrans >= p2dout.getX()) {
      xtrans = p2dout.getX();
    }
    if (ytrans >= p2dout.getY()) {
      ytrans = p2dout.getY();
    }

    xtrans = Math.round(xtrans);
    ytrans = Math.round(ytrans);

    AffineTransform tat = new AffineTransform();
    tat.translate(-xtrans, -ytrans);
    return tat;
  }
  

  
  //*****************************************************
  public static BufferedImage scaleImage(BufferedImage inImage, int maxW, int maxH) {
    double scale = getScale(maxW, maxH, inImage);
    return scaleImage(inImage, scale);
  }


  //*****************************************************
  public static BufferedImage scaleImage(BufferedImage inImage, double scale) {
    int scaledW = (int) Math.round(scale * inImage.getWidth());
    int scaledH = (int) Math.round(scale * inImage.getHeight());

    // Create an image buffer in which to paint on.
    BufferedImage outImage;
    outImage = new BufferedImage(scaledW, scaledH, inImage.getType());
    // Set the scale.
    AffineTransform tx = new AffineTransform();
    tx.scale(scale, scale);

    // Paint image.
    Graphics2D g2d = outImage.createGraphics();

    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);


    if(BufferedImageUtils.renderParameter == RenderingHints.VALUE_RENDER_QUALITY ){
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
      g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
      g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
    }

    g2d.drawImage(inImage, tx, null);
    g2d.dispose();

    return outImage;

  }
  
  //*****************************************************
  public static Image resizeImage(Image imgToResize, int x, int y) {
    Image imgTmp;

    int imgH, imgW;
    imgW = imgToResize.getWidth(null);
    imgH = imgToResize.getHeight(null);
    double rapportoW, rapportoH;
    rapportoW = (double) imgW / x;
    rapportoH = (double) imgH / y;
    if ((rapportoW) > (rapportoH)) {
      imgTmp = imgToResize.getScaledInstance(x, -1, 0);
    } else {
      imgTmp = imgToResize.getScaledInstance(-1, y, 0);
    }


    return imgTmp;

  }

  
  //*****************************************************
  public static double getScale(double maxW, double maxH, BufferedImage image) {
    int imageWidth = image.getWidth();
    int imageHeight = image.getHeight();

    return getScale(maxW, maxH, imageWidth, imageHeight);
  }

  //*****************************************************
  public static double getScale(double maxW, double maxH, int imageWidth, int imageHeight) {

    if (((double) imageWidth / (double) imageHeight) <
            (maxW / maxH)) {
      return maxH / (double) imageHeight;
    }
    if (((double) imageHeight / (double) imageWidth) <
            (maxH / maxW)) {
      return maxW / (double) imageWidth;
    }
    if (imageWidth > (double) imageHeight) {
      return maxW / (double) imageWidth;
    } else {
      return maxH / (double) imageHeight;
    }
  }
  

  
  public static BufferedImage resizeImageStretched(String filePath, int width, int height) {

      BufferedImage originalBI = readImage(filePath);
      return resizeImageStretched(originalBI, width, height);
  }


  //*****************************************************
  public static BufferedImage resizeImageStretched(BufferedImage inImage, int width, int height) {
    double[] scales = new double[2];
    scales[0] = ((double) width) / inImage.getWidth();
    scales[1] = ((double) height) / inImage.getHeight();

    // Create an image buffer in which to paint on.
    BufferedImage outImage = new BufferedImage(width, height, inImage.getType());

    // Set the scale.
    AffineTransform tx = new AffineTransform();
    tx.scale(scales[0], scales[1]);

    // Paint image.
    Graphics2D g2d = outImage.createGraphics();

    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

    g2d.drawImage(inImage, tx, null);
    g2d.dispose();

    return outImage;
  }
  
  //*********************************************
  public static BufferedImage addTransparencyMask(BufferedImage origineImage, BufferedImage alphaImage) {
    int w, h;

    w = origineImage.getWidth();
    h = origineImage.getHeight();
    Graphics2D g2Result, g2Alpha;

    BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    BufferedImage alpha = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);

    g2Result = result.createGraphics();
    g2Result.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);
    g2Result.drawImage(origineImage, 0, 0, null);

    g2Alpha = alpha.createGraphics();
    g2Alpha.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);
    g2Alpha.drawImage(alphaImage, 0, 0, null);

    g2Alpha.dispose();
    g2Result.dispose();

    result.getAlphaRaster().setRect(alpha.getData());

    return result;
  }

  
  
  //*********************************************
  public static BufferedImage applyTransparencyMask(BufferedImage origineImage, BufferedImage maskImage) {

    WritableRaster wrOrigin, wrTmp;
    BufferedImage alphaImage; //immagine nella quale si costruisce la maschera di trasparenza composta
    BufferedImage imageTmp;   //immagine di appoggio in cui si inserisce la maschera di trasparenza e alla quale si applica lo stesso livello di trasparenza dell'immagine iniziale
    Graphics2D gr2DTmp, gr2DAlpha;
    int w, h;

    w = origineImage.getWidth();
    h = origineImage.getHeight();

    wrOrigin = origineImage.getAlphaRaster();
    alphaImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
    imageTmp = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    gr2DTmp = imageTmp.createGraphics();
    gr2DTmp.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);
//    gr2DTmp.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    wrTmp = imageTmp.getAlphaRaster();

    gr2DTmp.drawImage(maskImage, 0, 0, null);
    wrTmp.setRect(wrOrigin);

    gr2DAlpha = alphaImage.createGraphics();
    gr2DAlpha.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);
//    gr2DAlpha.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
    gr2DAlpha.drawImage(imageTmp, 0, 0, null);

    origineImage.getAlphaRaster().setRect(alphaImage.getData());

    origineImage.releaseWritableTile(0, 0);
    alphaImage.releaseWritableTile(0, 0);
    imageTmp.releaseWritableTile(0, 0);
    gr2DTmp.dispose();
    gr2DAlpha.dispose();

    return origineImage;
  }

  
  //****************************************************/
  public static BufferedImage alphaLaier(BufferedImage image) {
    WritableRaster wrTmp = image.getAlphaRaster();
    BufferedImage imageTmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
    imageTmp.getRaster().setRect(wrTmp);
    return imageTmp;
  }

  //****************************************************/
  public static BufferedImage createShadowMask(BufferedImage image, int shadowLength, int transpValue) {

    int width = image.getWidth();
    int height = image.getHeight();
    int extra2 = shadowLength / 2;

    BufferedImage result = new BufferedImage(width + shadowLength, height + shadowLength, BufferedImage.TYPE_BYTE_GRAY);
    Graphics2D g2 = result.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);

    g2.setColor(Color.BLACK);
    g2.fillRect(0, 0, result.getWidth(), result.getHeight());

    BufferedImage shadow = new BufferedImage(width + shadowLength, height + shadowLength, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = shadow.createGraphics();
    g.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);

    WritableRaster wrTmp, wrImage;
    BufferedImage imageTmp = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D gr2DTmp = imageTmp.createGraphics();
    gr2DTmp.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.renderParameter);


    gr2DTmp.setColor(new Color(transpValue, transpValue, transpValue));
    gr2DTmp.fillRoundRect(0, 0, imageTmp.getWidth(), imageTmp.getHeight(), (extra2 - 1) * 2, (extra2 - 1) * 2);

    wrTmp = imageTmp.getAlphaRaster();
    wrImage = image.getAlphaRaster();
    if(wrImage!=null){//MODIFICA*******************************
        wrTmp.setRect(wrImage);
    }

    g.drawImage(imageTmp, extra2 - 1, extra2 - 1, null);

    try {
      g2.drawImage(shadow, getBlurOp(extra2 - 1), 0, 0);
    } catch (Exception e) {
      g2.drawImage(shadow, getBlurOp(extra2 + 1), 0, 0);
    }

    g2.dispose();
    g.dispose();
    gr2DTmp.dispose();

    return result;
  }
  
  //****************************************************
  private static ConvolveOp getBlurOp(int size) {
    float[] data = new float[size * size];
    float value = 1 / (float) (size * size);
    for (int i = 0; i < data.length; i++) {
      data[i] = value;
    }
    return new ConvolveOp(new Kernel(size, size, data), ConvolveOp.EDGE_NO_OP, null);
  }

  
  //*****************************************************
  public static Dimension scaleDimension(Dimension currentDim, Dimension maxDim) {
    int newWidth = 0, newHeight = 0;
    double differenzaLarghezze = ((double) maxDim.width) / currentDim.width;
    double differenzaAltezze = ((double) maxDim.height) / currentDim.height;

    if (differenzaLarghezze > differenzaAltezze) {
      newHeight = maxDim.height;
      newWidth = (currentDim.width * maxDim.height) / currentDim.height;
    } else {
      newWidth = maxDim.width;
      newHeight = (currentDim.height * maxDim.width) / currentDim.width;
    }

    return new Dimension(newWidth, newHeight);
  }

  
  //*****************************************************
  public static BufferedImage scaleImageFromFile(String originalImageFilePath, int maxW, int maxH) {
    boolean successuful = false;

    BufferedImage originalBI = readImage(originalImageFilePath);
    int originalW = originalBI.getWidth();
    int originalH = originalBI.getHeight();

    float scale = Math.max((float) originalW / maxW, (float) originalH / maxH);
    int finalW = Math.round(originalW / scale);
    int finalH = Math.round(originalH / scale);

    BufferedImage newBi;
    if(originalImageFilePath.toLowerCase().endsWith(".png")){
      newBi = new BufferedImage(finalW, finalH, BufferedImage.TYPE_INT_ARGB);
    } else {
      newBi = new BufferedImage(finalW, finalH, BufferedImage.TYPE_INT_RGB);
    }
    Graphics newG = newBi.getGraphics();
    do {
      successuful = newG.drawImage(originalBI, 0, 0, finalW, finalH, null);
      if (!successuful) {
        System.out.println("successuful=" + String.valueOf(successuful));
      }
    } while (!successuful);


    newG.dispose();

    return newBi;
  }

  //****************************************************
  public static BufferedImage createSmallShadowMask(BufferedImage image, int shadowLength, int transpValue, double fattoreIngrandimento) {
    //BufferedImage smallImage = Resize.scaleImage(image, (double) 1 / fattoreIngrandimento);
    BufferedImage smallImage = JAIUtils.scaleSubsample(image, (double) 1 / fattoreIngrandimento);
    BufferedImage biShadow = createShadowMask(smallImage, (int) (((double) shadowLength / (double) fattoreIngrandimento) + 0.5), transpValue);
    biShadow = resizeImageStretched(biShadow, image.getWidth() + shadowLength, image.getHeight() + shadowLength);


    BufferedImage result = new BufferedImage(biShadow.getWidth(), biShadow.getHeight(), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = result.createGraphics();

    try {
      //g2.drawImage(biShadow, 0, 0, null);
      g2.drawImage(biShadow, getBlurOp((int) ((double) shadowLength / fattoreIngrandimento) - 1), 0, 0);
    } catch (Exception e) {
      g2.drawImage(biShadow, getBlurOp((int) ((double) shadowLength / fattoreIngrandimento) + 1), 0, 0);
    }
    g2.dispose();
    return result;
  }
  
}
