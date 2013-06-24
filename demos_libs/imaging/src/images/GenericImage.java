/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package images;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.RescaleOp;
import javax.media.jai.TiledImage;
import rfile.RecursiveFileImpl;
import utils.BufferedImageUtils;
import utils.FileUtilities;
import utils.LLJTUtils;


/**
 *
 * @author developer
 */
public abstract class GenericImage {

  public static final float QUALITY_DEFAULT_FOR_WEB = 0.95F;

    abstract public BufferedImage getBufferedImage();

    abstract public TiledImage getTiledImage();

    abstract public int getWidth();

    abstract public int getHeight();

    abstract public int getNumLivelli();

    abstract public long getRGB(int x, int y);

    abstract public Graphics2D createGraphics();

    abstract public RenderedImage getRenderedImage();

    abstract public float[] getGrayOffsets();

    abstract public float[] getCyanOffsets();

    abstract public float[] getSeppiaOffsets();

    abstract public void setData(Raster r);

    abstract public Raster getData();

    abstract public void setRGBLevels(GenericImage srcRGB);

    abstract public void saveAs(String filePath);

    abstract public void saveAs(String filePath, float quality);

    abstract public byte[] saveToByteArray(Float quality, boolean addMetadata);

    abstract public void saveAsWithoutIcc(String filePath);

    abstract public void saveAsWithoutIcc(String filePath, float quality);

    @Override
    abstract public GenericImage clone();

    abstract public GenericImage grayScale();

    abstract public GenericImage grayScaleSingleLayer();
    //abstract public GenericImage adjustColorsLinearly();

    abstract public GenericImage rotate(double angleRotation);

    abstract public GenericImage rotate(double angleRotation, Dimension size);

    abstract public GenericImage scale(double scaleFactor);

    abstract public GenericImage resizeImageStretched(int width, int height);

    abstract public GenericImage addTrasparency();


    abstract public GenericImage addTrasparencyMask(GenericImage trasparencyMask);

    abstract public GenericImage applyTrasparencyMask(GenericImage trasparencyMask);

    abstract public GenericImage getSubimage(int x, int y, int width, int height);

    abstract public GenericImage getShadowMask(int shadowLengthPixels, int transpValue);
    abstract public GenericImage getShadowMask(int shadowLengthPixels, int transpValue, int fattoreRisparmioOmbra);

    abstract public GenericImage getAlphaLaier();

    abstract public GenericImage translate(Point translation);



    public static final int QUALITY_TYPE_INDEFINITE = -1;
    public static final int QUALITY_TYPE_THUMB = 1;
    public static final int QUALITY_TYPE_WORKING = 2;
    public static final int QUALITY_TYPE_ORIGINAL = 3;

    private int qualityTipe = QUALITY_TYPE_INDEFINITE;


    public boolean isDrawable(){
      return true;
    }


    public void saveAsSafety(String filePath){
      String tmpName = filePath + ".tmp";
      RecursiveFileImpl fdTmpFile = new RecursiveFileImpl(tmpName);

      this.saveAs(tmpName);

      fdTmpFile.renameThis(filePath, false);
    }



    public void cloneGeneric(GenericImage destinationImage){
      destinationImage.setQualityTipe(this.getQualityTipe());
    }


    //*************************************************/
    public GenericImage scale(int maxWidth, int maxHeight) {
        return this.scale(maxWidth, maxHeight, true);
    }

    //*****************************************************/
    public GenericImage applyTrasparency(int opacityLevel) {
      GenericImage imgTmp;
      if(this.getNumLivelli() == 3){
         imgTmp = this.addTrasparency();
      } else {
        imgTmp = this;
      }

      GenericImage imgMask = GenericImageFactory.buildGenericImage(this.getWidth(), this.getHeight(), 3);
      imgMask.fill(new Color(opacityLevel, opacityLevel, opacityLevel));

      imgTmp = imgTmp.applyTrasparencyMask(imgMask);
      return imgTmp;
    }


    //*************************************************/
    public GenericImage scaleAndCropToFit(int widthToFit, int heightToFit) {
      Dimension dimResult = calcolaMisureRiempimento(this.getWidth(), this.getHeight(), widthToFit, heightToFit);
      GenericImage imgScaled = scale(dimResult.width, dimResult.height);

      Rectangle areaToCrop = new Rectangle(0, 0, imgScaled.getWidth(), imgScaled.getHeight());
      boolean needCrop = false;

      if(imgScaled.getWidth()>widthToFit){
        needCrop = true;
        areaToCrop.x = (imgScaled.getWidth() - widthToFit) / 2;
        areaToCrop.width = widthToFit;
      }

      if(imgScaled.getHeight()>heightToFit){
        needCrop = true;
        areaToCrop.y = (imgScaled.getHeight() - heightToFit) / 2;
        areaToCrop.height = heightToFit;
      }

      if(needCrop){
        imgScaled = imgScaled.getSubimage(areaToCrop.x, areaToCrop.y, areaToCrop.width, areaToCrop.height);
      }

      return imgScaled;
    }

    //*************************************************/
    public GenericImage scale(int maxWidth, int maxHeight, boolean canIncreaseSize) {
        float scaleFactor = (float) getScaleFactor(maxWidth, maxHeight, this.getWidth(), this.getHeight());

        if (!canIncreaseSize && scaleFactor >= 1) {
            return this.clone();
        } else {
            return this.scale(scaleFactor);
        }
    }

    //*************************************************/
    public GenericImage resize(int maxWidth, int maxHeight) {
        //float scaleFactor = (float) Sizes.getScaleFactor(maxWidth, maxHeight, this.getWidth(), this.getHeight());
        //return this.scale(scaleFactor);

      return scale(maxWidth, maxHeight, true);
    }


    //*************************************************/
    public GenericImage removeTransparency(Color backgroundColor) {
      if(this.getNumLivelli() < 4){
        return this.clone();
      }
      GenericImage result = GenericImageFactory.buildGenericImage(this.getWidth(), this.getHeight(), 3);
      result.fill(backgroundColor);

      result.DrawImage(this, 0, 0);

      return result;
    }


    //*************************************************/
    public GenericImage applyRGBOffset(float[] offset) {
        GenericImage rgbImage = GenericImageFactory.buildGenericImage(this.getWidth(), this.getHeight(), 3); //new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
        Graphics2D grRgbImage = rgbImage.createGraphics();
        grRgbImage.setRenderingHint(RenderingHints.KEY_RENDERING, BufferedImageUtils.getRenderParamenter());

        AffineTransform a = new AffineTransform();
        grRgbImage.drawRenderedImage(this.getRenderedImage(), a);
        grRgbImage.dispose();

        float[] multiply = new float[3];
        multiply[0] = 1.0f;
        multiply[2] = 1.0f;
        multiply[1] = 1.0f;

        RescaleOp op = new RescaleOp(multiply, offset, null); //regola il rosso
        rgbImage.setData(op.filter(rgbImage.getData(), null));

        return rgbImage;
    }

    //*************************************************/
    public GenericImage getSubimage(int x, int y, int width, int height, boolean avoidExceptionsOnOutbound) {

        if (avoidExceptionsOnOutbound) {
            if (x < 0) {
                x = 0;
            }
            if (y < 0) {
                y = 0;
            }

            int intTmp = this.getWidth() - x - width;
            if (intTmp < 0) {
                width = width + intTmp;
            }

            intTmp = this.getHeight() - y - height;
            if (intTmp < 0) {
                height = height + intTmp;
            }

        }

        return getSubimage(x, y, width, height);
    }

    //*************************************************/
    public double getProportions() {
        return calculateProportions(this.getWidth(), this.getHeight());
    }

    //*************************************************/
    public Dimension getDimension() {
        Dimension result;

        result = new Dimension(this.getWidth(), this.getHeight());

        return result;
    }

    //*************************************************/
    public void fill(Color color) {
        fill(color, false);
    }

    //*************************************************/
    public void fill(Color color, boolean keepTransparency) {
        if(keepTransparency){
            GenericImage transparencyImage = getAlphaLaier();
            this.DrawRectangle(color, 0, 0, this.getWidth(), this.getHeight(), true);
            applyTrasparencyMask(transparencyImage);
        }else{
            this.DrawRectangle(color, 0, 0, this.getWidth(), this.getHeight(), true);
        }

    }
    //*************************************************/
    public void DrawLine(Color color, int x1, int y1, int x2, int y2) {
        DrawLine(color, x1, y1, x2, y2, 1, false);
    }

    //*************************************************/
    public void DrawLine(Color color, int x1, int y1, int x2, int y2, int spessore) {
        DrawLine(color, x1, y1, x2, y2, spessore, false);
    }

    //*************************************************/
    public void DrawLine(Color color, int x1, int y1, int x2, int y2, int spessore, boolean bicolore) {
        Graphics2D g2 = this.createGraphics();

        g2.setStroke(new BasicStroke(spessore));
        g2.setColor(color);
        g2.drawLine(x1, y1, x2, y2);

        if(bicolore){
          g2.setColor( new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue()) );
          if(y1 == y2){
            g2.drawLine(x1, y1 + spessore, x2, y2 + spessore);
          } else {
            g2.drawLine(x1 + spessore, y1, x2 + spessore, y2);
          }
        }


        g2.dispose();
    }


    //*************************************************/
    public void DrawPolygon(Color color, int []x, int []y, int spessore, boolean fillPolygon) {
        Graphics2D g2 = this.createGraphics();

        g2.setStroke(new BasicStroke(spessore));
        g2.setColor(color);

        if(fillPolygon){
          g2.fillPolygon(x, y, x.length);
        } else {
          g2.drawPolygon(x, y, x.length);
        }

        g2.dispose();
    }


    //*************************************************/
    public void DrawRectangle(Color color, int x, int y, int width, int height, boolean filled) {
      DrawRectangle(color, x, y, width, height, filled, 1);
    }

    //*************************************************/
    public void DrawRectangle(Color color, int x, int y, int width, int height, boolean filled, int spessore) {
        Graphics2D g2 = this.createGraphics();

        g2.setStroke(new BasicStroke(spessore));
        g2.setColor(color);
        if (filled) {
            g2.fillRect(x, y, width, height);
        } else {
            g2.drawRect(x, y, width, height);
        }
        g2.dispose();
    }

    //*************************************************/
    public void DrawEllisse(Color color, int x, int y, int width, int height, boolean filled) {
        Graphics2D g2 = this.createGraphics();

        g2.setColor(color);
        if (filled) {
            g2.fillOval(x, y, width, height);
        } else {
            g2.drawOval(x, y, width, height);
        }
        g2.dispose();
    }

    //*************************************************/
    //se l'angolo di rotazione e' 0 oppure 360 restituisce l'immagine stessa senza duplicarla
    public GenericImage rotateDegreeIfNeeded(int degree) {
      if(degree == 0 || degree == 360 || degree == -360){
        return this;
      } else {
        return rotateDegree(degree);
      }
    }

    //*************************************************/
    public GenericImage rotateDegree(double degree) {
        return this.rotate(Math.toRadians(degree));
    }

    //*************************************************/
    public void DrawImageCentered(GenericImage imageToDraw) {
        if(!imageToDraw.isDrawable()){
          return;
        }

        Point pos = new Point( Math.round((float)(this.getWidth() - imageToDraw.getWidth()) / 2), Math.round((float)(this.getHeight() - imageToDraw.getHeight()) / 2));

        if (pos.x < 0 || pos.y < 0) {
            (new RuntimeException("l'immagine necessitada di essere ridimensionata")).printStackTrace();
        }

        this.DrawImage(imageToDraw, pos.x, pos.y);
    }


    //*************************************************/
    public void DrawImage(RenderedImage imageToDraw, int x, int y) {
        Graphics2D g2 = this.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform a;
        a = new AffineTransform();
        a.translate(x, y);

        g2.drawRenderedImage(imageToDraw, a);

        g2.dispose();
    }

    //*************************************************/
    public void DrawImage(Image imageToDraw, int x, int y) {
        Graphics2D g2 = this.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform a;
        a = new AffineTransform();
        a.translate(x, y);

        g2.drawImage(imageToDraw, a, null);

        g2.dispose();
    }


    //*************************************************/
    public void DrawImage(GenericImage imageToDraw, int x, int y) {
        if(!imageToDraw.isDrawable()){
          return;
        }

        Graphics2D g2 = this.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform a;
        a = new AffineTransform();
        a.translate(x, y);

        g2.drawRenderedImage(imageToDraw.getRenderedImage(), a);

        g2.dispose();
    }

    //*************************************************/
    public void DrawImageCenteredMM(GenericImage imageToDraw, int xMM, int yMM, int wMM, int hMM, double DPC) {
        if(!imageToDraw.isDrawable()){
          return;
        }

        Graphics2D g2 = this.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        xMM = (int) Math.round((xMM * DPC) / 10);
        yMM = (int) Math.round((yMM * DPC) / 10);
        wMM = (int) Math.round((wMM * DPC) / 10);
        hMM = (int) Math.round((hMM * DPC) / 10);

        xMM = xMM + (wMM - imageToDraw.getWidth()) / 2;
        yMM = yMM + (hMM - imageToDraw.getHeight()) / 2;

        AffineTransform a;
        a = new AffineTransform();
        a.translate(xMM, yMM);

        g2.drawRenderedImage(imageToDraw.getRenderedImage(), a);

        g2.dispose();
    }


    //*************************************************/
    public void saveAsWithThumbnail(String filePath) {
        saveAsWithThumbnail(filePath, 0F, true); //0F fa in modo che le librerie usino la qualit? di default
    }

    //*************************************************/
    public void saveAsWithThumbnail(String filePath, Float quality, boolean addMetadata) {
        byte[] baImage = this.saveToByteArray(quality, addMetadata);
        byte[] baThumbs = LLJTUtils.getThumbnailImageInByteArray(this.getBufferedImage());
        if (baImage != null) {
            baImage = LLJTUtils.resetThumbnailInByteArray(baImage, baThumbs);
            FileUtilities.saveFileFromBuffer(baImage, filePath);
        }//if(baImage != null)
    }


    public boolean isHorizontal(){
      return isOrizontal(this.getDimension());
    }

    //*************************************************/
  @Override
    public String toString(){
      return "size:[" + this.getDimension().toString() + "][NL: " + this.getNumLivelli() + "]";
    }

  /**
   * @return the qualityTipe
   */
  public int getQualityTipe() {
    return qualityTipe;
  }

  /**
   * @param qualityTipe the qualityTipe to set
   */
  public void setQualityTipe(int qualityTipe) {
    this.qualityTipe = qualityTipe;
  }
  
  
  //*********************************************
  //*********************************************
  //*********************************************
  //*********************************************

  //*****************************************************
  public static Dimension calcolaMisureRiempimento(int formatoWidth, int formatoHeight, int formatoDaRiempireWidth, int formatoDaRiempireHeight) {
    return calcolaMisureRiempimento(new Dimension(formatoWidth, formatoHeight), new Dimension(formatoDaRiempireWidth, formatoDaRiempireHeight));
  }

  //*****************************************************
  public static Dimension calcolaMisureRiempimento(Dimension formato, Dimension formatoDaRiempire) {
    Dimension formatoFinale = new Dimension();

    double differenzaLarghezze = ((double) formatoDaRiempire.width) / formato.width;
    double differenzaAltezze = ((double) formatoDaRiempire.height) / formato.height;

    if (differenzaLarghezze > differenzaAltezze) { //l'immagine deve avere un lato della misura giusta e uno piu' lungo della destinazione
      formatoFinale.width = formatoDaRiempire.width;
      formatoFinale.height = Math.round(((float)formato.height * formatoDaRiempire.width) / formato.width);
    } else {
      formatoFinale.height = formatoDaRiempire.height;
      formatoFinale.width = Math.round(((float)formato.width * formatoDaRiempire.height) / formato.height);
    }

    return formatoFinale;
  }

  
  //*****************************************************
  public static double getScaleFactor(int maxW, int maxH, int imageWidth, int imageHeight) {

    if( ( (double) imageWidth / (double) imageHeight) <
      ((double)maxW / (double)maxH)) {
      return (double)maxH / (double) imageHeight;
    }
    if( ( (double) imageHeight / (double) imageWidth) <
      ((double)maxH / (double)maxW)) {
      return (double)maxW / (double) imageWidth;
    }
    if(imageWidth > (double) imageHeight) {
      return (double)maxW / (double) imageWidth;
    } else {
      return (double)maxH / (double) imageHeight;
    }
  }
  
  /***********************************************************************************/
  public static double calculateProportions(int width, int height){
    double result;

    result = width > height ? width / (double)height : height / (double)width;

    return result;
  }

  //***************************************************************************
  public static boolean isOrizontal(Dimension size) {
    boolean result = false;

    if(size.width> size.height){
      result = true;
    }

    return result;
  }
  
}
