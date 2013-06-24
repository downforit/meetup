package utils;



import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.Iterator;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import javax.swing.*;

import mediautil.gen.directio.*;
import mediautil.image.*;
import mediautil.image.jpeg.*;
import mediautil.image.jpeg.Entry;


public class LLJTUtils {

  public static void main(String[] args) {

    String folder = "/LEO/tmp";
    File dir =new File(folder);
    File images[] = dir.listFiles();
    long t1 = System.currentTimeMillis();
    for(int i=0;i<images.length;i++){

        String imgName = images[i].getName().toLowerCase();
        if(images[i].isDirectory() || !( imgName.endsWith("jpg") || imgName.endsWith("jpeg") ) ){
            continue;
        }
        IIOMetadata metadata;
        try {
          System.out.println("___" + i+" ->"+images[i].getName());
          //setNewJpegHeader(images[i].getAbsolutePath(),images[i].getAbsolutePath()+".jpg");
          //metadata = getMetadata(images[i]);
          long tt1 = System.currentTimeMillis();
          ImageIcon icon = getThumbnailImageIcon(images[i].getAbsolutePath(),100,100);
          long tt2 = System.currentTimeMillis();
          ImageIcon icon2 = getThumbnailImageIcon(images[i].getAbsolutePath());
          long tt3 = System.currentTimeMillis();
          System.out.println("WITH RESIZE: Elapsed time for "+imgName + " = "+(tt2-tt1));
          System.out.println("NO RESIZE: Elapsed time for "+imgName + " = "+(tt3-tt2));
          //resetThumbnail(images[i]);
          //Entry iccEntry = getIccProfile(images[i]);
        } catch (Exception ex1) {
            ex1.printStackTrace();
        }

    }
    long t2 = System.currentTimeMillis();
    System.out.println("Elapsed time = "+(t2-t1) + " ms per " +(images.length)+" immagini" );


    /*
    String originalFilePath="/prove nikon 1041.jpg";
    String scaledFilePath="/scaled.jpg";
    File originalFile = new File(originalFilePath);
    String scaledImagePathNoHeader ="/c2.JPG";
    String scaledImagePathWithHeader="/c2h.JPG";
    String croppedFile="/ccropped.JPG";

    try {
      byte fileBuffer[] = FileUtilities.getByteArrayFromFile(originalFilePath);


      RenderedOp img = JAIUtils.getPlanarImage(fileBuffer);
      ImageInputStream is = new FileImageInputStream(originalFile);

      Iterator iterator = ImageIO.getImageReaders(is);
      ImageTypeSpecifier imageTypeSpecifier= new ImageTypeSpecifier(img);

      ImageWriter writer = getCoreJpegWriter();
      IIOMetadata defaultMetadata = writer.getDefaultImageMetadata(imageTypeSpecifier,writer.getDefaultWriteParam());
      J2KImageReaderSpi readerSpi = new J2KImageReaderSpi();

      J2KImageReader reader = new J2KImageReader(readerSpi);
      //ImageReader reader = getCoreJpegReader();
      reader.setInput(new FileImageInputStream(new File(originalFilePath)));

      IIOMetadata metadata;
      try{
        metadata = reader.getImageMetadata(0);
      }catch(Exception ex){
        metadata = reader.getImageMetadata(1);
      }

      RenderedOp piOriginal = JAIUtils.getPlanarImage(fileBuffer);
      RenderedOp scaledImage = JAIUtils.resize(piOriginal,1024,768);


      Sanselan san = new Sanselan();
      org.w3c.dom.Node defaultMetadataNode = defaultMetadata.getAsTree(defaultMetadata.getNativeMetadataFormatName());

      org.w3c.dom.Node node = san.getMetadata(originalFile, defaultMetadataNode.getNodeName());
      byte icc_profile_bytes[] = san.getIccProfile(fileBuffer);
      ICC_Profile icc_profile = ICC_Profile.getInstance(icc_profile_bytes);

      IIOImage iioImage = new IIOImage(piOriginal,null,metadata);

      writer.setOutput(new FileImageOutputStream(new File(scaledFilePath)));
      writer.write(iioImage);
      writer.dispose();


      //BufferedImageUtils.saveImage(scaledImage.getAsBufferedImage(),metadata,"/xx.jpg");

      //cropImageFile(originalFile,croppedFile,25,60,450,600);

    } catch(Exception ex) {
      ex.printStackTrace();
    }*/

  }

  /***********************************************************/
  public static void resetThumbnail(File originalFile) throws
            FileNotFoundException, IOException, LLJTranException {

      //BufferedImage biOriginal = BufferedImageUtils.readImage(originalFile.getAbsolutePath());
      //BufferedImage biThumb = BufferedImageUtils.scaleImage(biOriginal,120,120);
      //String thumbPath = "/tmp/_"+originalFile.getName();
      //BufferedImageUtils.saveImage(biThumb,thumbPath);
      //byte[] thumbByteArray = FileUtilities.getByteArrayFromFile(thumbPath);

      String strTmp = originalFile.getName().toLowerCase();
      if( strTmp.endsWith(".tif") || strTmp.endsWith("tiff")){
        return;
      }


      LLJTran llj = new LLJTran(originalFile);
      llj.read(LLJTran.READ_ALL, true);
      if(llj.getImageInfo() instanceof Exif)
      {
          // Read the image in llj and get a Thumbnail Image from it.
          //
          // In the regular usage you can save the image in llj to an
          // OutputStream.
          //
          // However since llj implements an IterativeWriter the image can be
          // directly read
          InStreamFromIterativeWriter iwip = new InStreamFromIterativeWriter();
          IterativeWriter iWriter = llj.initWrite(iwip.getWriterOutputStream(),
                                      LLJTran.NONE, LLJTran.OPT_WRITE_ALL,
                                      null, 0, false);
          iwip.setIterativeWriter(iWriter);
          byte newThumbnail[] = getThumbnailImage(iwip);
          llj.wrapupIterativeWrite(iWriter);

          // Set the new Thumbnail
          if(llj.setThumbnail(newThumbnail, 0, newThumbnail.length,ImageResources.EXT_JPG))
              System.out.println("Successfully Set New Thumbnail");
          else
              System.out.println("Error Setting New Thumbnail");
      }
      else
          System.out.println("Cannot Set Thumbnail Since There is no EXIF Header");

      // 4. Save the Image with the new Thumbnail
     /* String originalName = originalFile.getName();
      String newName = originalName.substring(0,originalName.lastIndexOf("."));
      String ext = originalName.substring(originalName.lastIndexOf("."));
      newName = newName + "_x_" +ext;
      File newOriginalFile = new File(originalFile.getParentFile(),newName);*/


      OutputStream out = new BufferedOutputStream(new FileOutputStream(originalFile));
      llj.save(out, LLJTran.OPT_WRITE_ALL);
      out.close();

      // Cleanup
      llj.freeMemory();
  }

  /***********************************************************/
  public static byte [] resetThumbnailInByteArray(byte []baFileIn) {
    return resetThumbnailInByteArray(baFileIn, null);
  }

  /***********************************************************/
  public static byte [] resetThumbnailInByteArray(byte []baFileIn, byte newThumbnail[]) {

    LLJTran llj = null;
    ByteArrayOutputStream baos = null;
    try {
      //BufferedImage biOriginal = BufferedImageUtils.readImage(originalFile.getAbsolutePath());
      //BufferedImage biThumb = BufferedImageUtils.scaleImage(biOriginal,120,120);
      //String thumbPath = "/tmp/_"+originalFile.getName();
      //BufferedImageUtils.saveImage(biThumb,thumbPath);
      //byte[] thumbByteArray = FileUtilities.getByteArrayFromFile(thumbPath);
      ByteArrayInputStream bais = new ByteArrayInputStream(baFileIn);
      llj = new LLJTran(bais);
      llj.read(LLJTran.READ_ALL, true);
      if (llj.getImageInfo() instanceof Exif) {
        // Read the image in llj and get a Thumbnail Image from it.
        //
        // In the regular usage you can save the image in llj to an
        // OutputStream.
        //
        // However since llj implements an IterativeWriter the image can be
        // directly read
        InStreamFromIterativeWriter iwip = new InStreamFromIterativeWriter();
        IterativeWriter iWriter = llj.initWrite(iwip.getWriterOutputStream(),
                LLJTran.NONE, LLJTran.OPT_WRITE_ALL,
                null, 0, false);
        iwip.setIterativeWriter(iWriter);

        if(newThumbnail == null){
          newThumbnail = getThumbnailImage(iwip);
        }

        llj.wrapupIterativeWrite(iWriter);

        // Set the new Thumbnail
        if (llj.setThumbnail(newThumbnail, 0, newThumbnail.length, ImageResources.EXT_JPG)) {
          System.out.println("Successfully Set New Thumbnail");

        } else {
          System.out.println("Error Setting New Thumbnail");

        }
      } else {
        System.out.println("Cannot Set Thumbnail Since There is no EXIF Header");

        // 4. Save the Image with the new Thumbnail
     /* String originalName = originalFile.getName();
        String newName = originalName.substring(0,originalName.lastIndexOf("."));
        String ext = originalName.substring(originalName.lastIndexOf("."));
        newName = newName + "_x_" +ext;
        File newOriginalFile = new File(originalFile.getParentFile(),newName);*/



      }
      baos = new ByteArrayOutputStream();
      OutputStream out = new BufferedOutputStream(baos);
      llj.save(out, LLJTran.OPT_WRITE_ALL);
      out.close();
    } catch (LLJTranException lLJTranException) {
      throw new RuntimeException(lLJTranException);
    } catch (IOException iOException) {
      throw new RuntimeException(iOException);
    }
      // Cleanup
      llj.freeMemory();
      return baos.toByteArray();
  }


  private static byte[] getThumbnailImage(InputStream ip) throws IOException
  {
      ImageReader reader;
      ImageInputStream iis = ImageIO.createImageInputStream(ip);
      reader = ImageIO.getImageReaders(iis).next();
      reader.setInput(iis);
      BufferedImage image = reader.read(0);
      iis.close();

      /*/
      // Scale the image to around 160x120/120x160 pixels, may not conform
      // exactly to Thumbnail requirements of 160x120.
      int t, longer, shorter;
      longer = image.getWidth();
      shorter = image.getHeight();
      if(shorter > longer)
      {
          t = longer;
          longer = shorter;
          shorter = t;
      }
      double factor = 160/(double)longer;
      double factor1 = 120/(double)shorter;
      if(factor1 > factor) {
        factor = factor1;
      }
      AffineTransform tx = new AffineTransform();
      tx.scale(factor, factor);
      AffineTransformOp affineOp = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      image = affineOp.filter(image, null);

      // Write Out the Scaled Image to a ByteArrayOutputStream and return the
      // bytes
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);
      String format = "JPG";
      ImageIO.write(image, format, byteStream);

      return byteStream.toByteArray();
      //*/

      return getThumbnailImageInByteArray(image);
    }


  public static byte[] getThumbnailImageInByteArray(BufferedImage image)
  {
      // Scale the image to around 160x120/120x160 pixels, may not conform
      // exactly to Thumbnail requirements of 160x120.
      int t, longer, shorter;
      longer = image.getWidth();
      shorter = image.getHeight();
      if(shorter > longer)
      {
          t = longer;
          longer = shorter;
          shorter = t;
      }
      double factor = 160/(double)longer;
      double factor1 = 120/(double)shorter;
      if(factor1 > factor) {
        factor = factor1;
      }

      AffineTransform tx = new AffineTransform();
      tx.scale(factor, factor);
      AffineTransformOp affineOp = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      image = affineOp.filter(image, null);

      // Write Out the Scaled Image to a ByteArrayOutputStream and return the
      // bytes
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);
      String format = "JPG";
      
      try {
        ImageIO.write(image, format, byteStream);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }

      return byteStream.toByteArray();
    }


  /***********************************************************/
  public static Entry getIccProfile(File originalFile){
      LLJTran llj = new LLJTran(originalFile);
    try {
        llj.read(LLJTran.READ_HEADER, true);
    } catch (LLJTranException ex) {
        ex.printStackTrace();
    }
    AbstractImageInfo imageInfo = llj.getImageInfo();
    Exif exif = (Exif) imageInfo;
    //Entry iccEntry = new Entry(Exif.INTERCOLORPROFILE,icc.toString());
    Entry iccEntry = exif.getTagValue(Exif.INTERCOLORPROFILE,true);
    //exif.setTagValue(Exif.INTERCOLORPROFILE,-1,iccEntry,true);
    if(iccEntry!=null){
        System.out.println(iccEntry.toString());
    }else{
        System.out.println("iccEntry is null");
    }
    return iccEntry;
  }


  /*****************************************************************/
  private static IIOMetadata getMetadata(File originalFile) throws FileNotFoundException, IOException, Exception {
    ImageReader reader = getCoreJpegReader();
    reader.setInput(new FileImageInputStream(originalFile));

    IIOMetadata metadata;
    try{
      metadata = reader.getImageMetadata(0);
    }catch(Exception ex){
      metadata = reader.getImageMetadata(1);
      throw ex;
    }
    return metadata;
  }



  /*****************************************************************/
  //NOT WORKING BECAUSE can't find where THE ICC_PROFILE is....
  private static void setNewJpegHeader(String originalFilePath,String finalFilePath) throws LLJTranException, FileNotFoundException,
       IOException {

    //effettuo il resize dell'immagine originale e la salvo(senza header) in finalPath
    BufferedImage bi = ImageIO.read(new File(originalFilePath));
    BufferedImage scaledBi = JAIUtils.resize(bi,1024,768);
    BufferedImageUtils.saveImage(scaledBi,finalFilePath);


    //leggo le info dall'header dell'immagine originale per recuperare il colorSpace
    LLJTran llj = new LLJTran(new File(originalFilePath));
    llj.read(LLJTran.READ_HEADER, true);
    AbstractImageInfo imageInfo = llj.getImageInfo();
    Exif exif = (Exif) imageInfo;
    //Entry iccEntry = new Entry(Exif.INTERCOLORPROFILE,icc.toString());
    Entry colorSpaceEntry = exif.getTagValue(Exif.COLORSPACE,true);
    //exif.setTagValue(Exif.INTERCOLORPROFILE,-1,iccEntry,true);

    //ora leggo l'immagine di cui ho fatto il resize ed aggiungo il dummy header
    LLJTran lljScaled = new LLJTran(new File(finalFilePath));
    lljScaled.read(LLJTran.READ_ALL, true);
    AbstractImageInfo scaledImageInfo = lljScaled.getImageInfo();
    // 3. If the Image does not have an Exif Header create a dummy Exif Header
    if(!(scaledImageInfo instanceof Exif)){


      lljScaled.addAppx(LLJTran.dummyExifHeader, 0, LLJTran.dummyExifHeader.length, true);
      scaledImageInfo = lljScaled.getImageInfo(); // This would have changed

      Exif scaledExif = (Exif) scaledImageInfo;
      scaledExif.setTagValue(Exif.COLORSPACE, -1, colorSpaceEntry, true);

      FileOutputStream fos = new FileOutputStream(finalFilePath);
      lljScaled.save(fos);
      lljScaled.freeMemory();
      llj.freeMemory();
    }
    /*
    if((imageInfo instanceof Exif)) {

      exif.getTagValue(new Integer(Exif.INTERCOLORPROFILE),Exif.COLORSPACE,true);
      // Get the colorProfile
      Entry entry = exif.getTagValue(Exif.INTERCOLORPROFILE, true);
      if(entry != null)
        entry.setValue(0, "1998:08:18 11:15:00");

      int imageWidth = llj.getWidth();
      int imageHeight = llj.getHeight();
      if(imageWidth > 0 && imageHeight > 0) {
        entry = exif.getTagValue(Exif.EXIFIMAGEWIDTH, true);
        if(entry != null)
          entry.setValue(0, new Integer(imageWidth));
        entry = exif.getTagValue(Exif.EXIFIMAGELENGTH, true);
        if(entry != null)
          entry.setValue(0, new Integer(imageHeight));
      }

    }*/
  }

  /*************************************************************************/
  private static ImageWriter getCoreJpegWriter() throws Exception {
      // Get core JPEG writer.
      Iterator writers = ImageIO.getImageWritersByFormatName("jpeg");
      ImageWriter writer = null;
      while(writers.hasNext()) {
          writer = (ImageWriter)writers.next();
          if(writer.getClass().getName().startsWith("com.sun.imageio")) {
              // Break on finding the core provider.
              break;
          }
      }
      if(writer == null) {
          throw new Exception("Cannot find core JPEG writer!");
      }
      return writer;
  }

  /*************************************************************************/
  private static ImageReader getCoreJpegReader() throws Exception {
      Iterator readers = ImageIO.getImageReadersByFormatName("jpeg");
      ImageReader reader = null;
      while(readers.hasNext()) {
          reader = (ImageReader)readers.next();
          if(reader.getClass().getName().startsWith("com.sun.imageio")) {
              // Break on finding the core provider.
              break;
          }
      }
      if(reader == null) {
          throw new Exception("Cannot find core JPEG reader!");
      }
      return reader;
  }



  /**
   * Utility Method to get a Thumbnail Image in a byte array from an
   * InputStream to a full size image. The full size image is read and scaled
   * to a Thumbnail size using Java API.
   */
  private static byte[] getScaledlImage(InputStream ip,int w,int h) throws IOException
  {
    // Scale the image to around w*h/h*w pixels
      ImageReader reader;
      ImageInputStream iis = ImageIO.createImageInputStream(ip);
      reader = (ImageReader) ImageIO.getImageReaders(iis).next();
      reader.setInput(iis);
      BufferedImage image = reader.read(0);
      iis.close();


      int t, longer, shorter;
      longer = image.getWidth();
      shorter = image.getHeight();
      if(shorter > longer)
      {
          t = longer;
          longer = shorter;
          shorter = t;
      }
      double factor = w/(double)longer;
      double factor1 = h/(double)shorter;
      if(factor1 > factor)
          factor = factor1;
      AffineTransform tx = new AffineTransform();
      tx.scale(factor, factor);
      AffineTransformOp affineOp = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
      image = affineOp.filter(image, null);

      // Write Out the Scaled Image to a ByteArrayOutputStream and return the
      // bytes
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream(2048);
      String format = "JPG";
      ImageIO.write(image, format, byteStream);

      return byteStream.toByteArray();
  }


  public static ImageIcon getThumbnailImageIcon(String filePath,boolean openingFile){
      ImageIcon imageIconToReturn = null;
      LLJTran llj = null;

      String strTmp = filePath.toLowerCase();
      if( strTmp.endsWith(".tif") || strTmp.endsWith("tiff")){
        return imageIconToReturn;
      }

      try {

          llj = new LLJTran(new File(filePath));
          // If you pass the 2nd parameter as false, Exif information is not
          // loaded and hence will not be written.
          llj.read(LLJTran.READ_HEADER, true);

          imageIconToReturn = (ImageIcon) llj.getImageInfo().getThumbnailIcon();
      }catch(Throwable t){
          //t.printStackTrace();
      }
      if(llj!=null){
          llj.freeMemory();
      }
      if(imageIconToReturn==null && openingFile){
          //USO IL VECCHIO METODO nel caso in cui non c'� la thumbnail nell'header
          imageIconToReturn = getScaledImageIconTraditional(filePath, 120,90);
      }
      return imageIconToReturn;
  }

  /**
   * Return the thumbnail of a jpeg file reading from the header(if any)
   *
   * @param filePath String, the image file path
   *
   * @return ImageIcon, the thumbnail
   */

  public static ImageIcon getThumbnailImageIcon(String filePath){
      
      return getThumbnailImageIcon(filePath, true);
      /*ImageIcon imageIconToReturn = null;
      LLJTran llj = null;
      try {

          llj = new LLJTran(new File(filePath));
          // If you pass the 2nd parameter as false, Exif information is not
          // loaded and hence will not be written.
          llj.read(LLJTran.READ_HEADER, true);

          imageIconToReturn = (ImageIcon) llj.getImageInfo().getThumbnailIcon();
      }catch(Throwable t){
          //t.printStackTrace();
      }
      if(llj!=null){
          llj.freeMemory();
      }
      if(imageIconToReturn==null){
          //USO IL VECCHIO METODO nel caso in cui non c'� la thumbnail nell'header
          imageIconToReturn = getScaledImageIconTraditional(filePath, 120,90);
      }
      return imageIconToReturn;
       */
  }

  /**
   * Return the thumbnail of a jpeg file reading from the header(if any)
   *
   * @param filePath String, the image file path
   * @param w int, max width of the wanted thumbnail
   * @param h int, max height of the wanted thumbnail
   *
   * @return ImageIcon, the thumbnail
   */
  public static ImageIcon getThumbnailImageIcon(String filePath, int w, int h) {

      ImageIcon imageIconToReturn = null;
      ImageIcon imageIcon = null;

      if( FileUtilities.isFileWithPreview(filePath) ){

        imageIcon = getThumbnailImageIcon(filePath);

        if(imageIcon==null){
            //USO IL VECCHIO METODO nel caso in cui non c'� la thumbnail nell'header
            imageIconToReturn = getScaledImageIconTraditional(filePath, w,h);
        }else{
            imageIconToReturn = new ImageIcon(BufferedImageUtils.resizeImage(imageIcon.getImage(), w, h));
        }
      }
      return imageIconToReturn;
  }


  /***********************************************************/
  private static ImageIcon getScaledImageIconTraditional(String filePath,int w,int h){
      BufferedImage inputImage = BufferedImageUtils.readImage(filePath);
      return  new ImageIcon(BufferedImageUtils.scaleImage(inputImage, w, h));
  }



  /**
   * op = LLJTran.ROT_180,LLJTran.ROT_270,LLJTran.ROT_90
   * */
  public static void rotateImage(String originalFilePath,String destinationFilePath,int op) throws LLJTranException, IOException {
      String strTmp = originalFilePath.toLowerCase();
      if( strTmp.endsWith(".tif") || strTmp.endsWith("tiff")){
        throw new RuntimeException("impossibile eseguire l'operazione sui tiff");
      }

      // 1. Initialize LLJTran and Read the entire Image including Appx markers
      LLJTran llj = new LLJTran(new File(originalFilePath));
      // If you pass the 2nd parameter as false, Exif information is not
      // loaded and hence will not be written.
      llj.read(LLJTran.READ_ALL, true);
      llj.transform(op, LLJTran.OPT_DEFAULTS);
      OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFilePath));
      llj.save(out, LLJTran.OPT_WRITE_ALL);
      out.close();

      // Cleanup
      llj.freeMemory();
  }

  public static void cropImageFile(String originalFilePath,String destinationFilePath,Rectangle cropRect) throws IOException, LLJTranException {
      cropImageFile(originalFilePath,destinationFilePath,cropRect.x,cropRect.y,cropRect.width,cropRect.height);

  }

  /**
   * Crop the image denoted by "originalFilePath" according the rectangle (x,y,w,h).
   * Save the new thumbnail in the header of the jpeg
   *
   * @param destinationFilePath , denotes the path where to save the image File modified
   *
   * */

  public static void cropImageFile(String originalFilePath,String destinationFilePath,int x,int y,int w,int h) throws LLJTranException, IOException {
    // 1. Initialize LLJTran and Read the entire Image including Appx markers
    LLJTran llj = new LLJTran(new File(originalFilePath));
    // If you pass the 2nd parameter as false, Exif information is not
    // loaded and hence will not be written.
    llj.read(LLJTran.READ_ALL, true);
    // 2. Crop it to the specified Bounds
    Rectangle cropArea = new Rectangle(x,y,w,h);
    llj.transform(LLJTran.CROP, LLJTran.OPT_DEFAULTS, cropArea);
    // 3. If Image has an Exif header set/change the Thumbnail to the
    // downscale of new Image
    if(llj.getImageInfo() instanceof Exif) {
      // Read the image in llj and get a Thumbnail Image from it.
      // In the regular usage you can save the image in llj to an
      // OutputStream.
      // However since llj implements an IterativeWriter the image can be
      // directly read
      InStreamFromIterativeWriter iwip = new InStreamFromIterativeWriter();
      IterativeWriter iWriter = llj.initWrite(iwip.getWriterOutputStream(),
                                              LLJTran.NONE, LLJTran.OPT_WRITE_ALL,
                                              null, 0, false);
      iwip.setIterativeWriter(iWriter);
      byte newThumbnail[] = getScaledlImage(iwip,160,120);
      llj.wrapupIterativeWrite(iWriter);

      // Set the new Thumbnail
      if(llj.setThumbnail(newThumbnail, 0, newThumbnail.length,ImageResources.EXT_JPG))
        System.out.println("Successfully Set New Thumbnail");
      else
        System.out.println("Error Setting New Thumbnail");
    } else
      System.out.println("Cannot Set Thumbnail Since There is no EXIF Header");

    // 4. Save the Image with the new Thumbnail
    OutputStream out = new BufferedOutputStream(new FileOutputStream(destinationFilePath));
    llj.save(out, LLJTran.OPT_WRITE_ALL);
    out.close();

    // Cleanup
    llj.freeMemory();
  }


  /*****************************************************************************/
  public static Date getDate(String filePath){
      LLJTran llj = null;
      Entry entry;
      String strDate = null;

      String strTmp = filePath.toLowerCase();
      if( strTmp.endsWith(".tif") || strTmp.endsWith("tiff")){
        return null;
      }

      Date d = new Date(System.currentTimeMillis());
      try {
          llj = new LLJTran(new File(filePath));
          // If you pass the 2nd parameter as false, Exif information is not
          // loaded and hence will not be written.

          llj.read(LLJTran.READ_HEADER, true);
          AbstractImageInfo imageInfo = llj.getImageInfo();
          if(imageInfo instanceof Exif){
              Exif exif = (Exif) imageInfo;
              if(exif!=null){
                  entry = exif.getTagValue(Exif.DATETIMEORIGINAL,true);
                  strDate =(String) entry.getValue(0);

                  DateFormat formatter ;

                  formatter = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
                  d = (Date)formatter.parse(strDate);
              }
          }
    } catch (Throwable ex) {
        //ex.printStackTrace();
    }
    return d;
  }



  /*****************************************************************************/
  public static String getOrientationFlag(String filePath){
      String strTmp = filePath.toLowerCase();
      if( strTmp.endsWith(".tif") || strTmp.endsWith("tiff")){
        return null;
      }

      LLJTran llj = null;
      String orientation = "TopLeft",retValue="1";
      try {
          llj = new LLJTran(new File(filePath));
          // If you pass the 2nd parameter as false, Exif information is not
          // loaded and hence will not be written.

          llj.read(LLJTran.READ_HEADER, true);
          AbstractImageInfo imageInfo = llj.getImageInfo();
          if(imageInfo instanceof Exif){
              Exif exif = (Exif) imageInfo;
              if(exif!=null){
                  orientation = exif.getOrientation();
              }
          }
    } catch (Throwable ex) {
        //ex.printStackTrace();
    }
    //orientation = orientation.toUpperCase();
    if(orientation.equalsIgnoreCase("TopLeft")){
        retValue="1";
    }else if(orientation.equals("TopRight")){
        retValue="2";
    }else if(orientation.equals("BotRight")){
        retValue="3";
    }else if(orientation.equals("BotLeft")){
        retValue="4";
    }else if(orientation.equals("LeftTop")){
        retValue="5";
    }else if(orientation.equals("RightTop")){
        retValue="6";
    }else if(orientation.equals("RightBot")){
        retValue="7";
    }else if(orientation.equals("LeftBot")){
        retValue="8";
    }

    llj.freeMemory();
    return retValue;

  }

  /***********************************************************************/
  public static String getOrientationMarker(String orientation){
    String orientationMarker = "S";
    if(orientation == null || orientation.length() == 0) {
      orientationMarker = "S";
    }else{

      if(orientation.equals("1") || orientation.equals("2")) {
        orientationMarker = "S";
      } else if(orientation.equals("3") || orientation.equals("4")) {
        orientationMarker = "P";
      } else if(orientation.equals("5") || orientation.equals("8")) {
        orientationMarker = "O";
      } else if(orientation.equals("6") || orientation.equals("7")) {
        orientationMarker = "R";
      }
    }
    return orientationMarker;
  }

    /*****************************************************************************/
    public static Dimension getImageSize(String filePath) {
        LLJTran llj = null;
        int w,h;
        Dimension imageSize=null;

        String strTmp = filePath.toLowerCase();

        if( !strTmp.endsWith(".tif") && !strTmp.endsWith("tiff")){

          try {
              llj = new LLJTran(new File(filePath));
              // If you pass the 2nd parameter as false, Exif information is not
              // loaded and hence will not be written.

              llj.read(LLJTran.READ_HEADER, true);
              AbstractImageInfo imageInfo = llj.getImageInfo();
              if (imageInfo instanceof Exif) {
                  Exif exif = (Exif) imageInfo;
                  if (exif != null) {
                      w = exif.getResolutionX();
                      h = exif.getResolutionY();
                      if(w>0 && h>0){
                        imageSize = new Dimension(w,h);
                      }
                  }
              }
              llj.freeMemory();
          } catch (Throwable ex) {
              //se vado in eccezione imageSize=null-> al passaggio successivo la calcoler� nel modo tradizionale
          }
        }

        if(imageSize==null){
            imageSize = BufferedImageUtils.getImageSize(filePath);
        }
        return imageSize;
    }
}
