/*
 * Sizes.java
 *
 * Created on 12 febbraio 2008, 10.57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package utils;

import java.awt.Dimension;
import java.awt.Point;


/**
 *
 * @author developer
 */
public class Sizes {
  public static final float CENTIMETRI_PER_POLLICE = 2.54F;
  public static final String PROPORTIONS_TAG_DELIMITER = "-P-";

  /**
   * @return the language
   */
  public static String getLanguage() {
    return language;
  }

  /**
   * @param aLanguage the language to set
   */
  public static void setLanguage(String aLanguage) {
    language = aLanguage;
  }

  /** Creates a new instance of Sizes */
  Sizes() {
  }

  /***********************************************************************************/
  public static boolean is90degreesMultiple(double angleRotation, int degreesTollerance) {
    boolean result = false;

    double degree = toAbsoluteDegrees(angleRotation);

    if(degree >= (0 - degreesTollerance) && degree <= (0 + degreesTollerance) ||
       degree >= (90 - degreesTollerance) && degree <= (90 + degreesTollerance) ||
       degree >= (180 - degreesTollerance) && degree <= (180 + degreesTollerance) ||
       degree >= (270 - degreesTollerance) && degree <= (270 + degreesTollerance) ||
       degree >= (360 - degreesTollerance) && degree <= (360 + degreesTollerance)
       ){
      result = true;
    }

    return result;
  }



  /***********************************************************************************/
  public static double toAbsoluteDegrees(double angleRotation) {
      double angleRotationDegree = Math.toDegrees(angleRotation);
      if(angleRotationDegree < 0){
        angleRotationDegree = angleRotationDegree + 360;
      }
      return angleRotationDegree;
  }

  /***********************************************************************************/
  public static boolean areAnglesEquals(double angleDegree, double angleToCompare, int tollerance) {
    if(angleDegree >= (angleToCompare - tollerance) &&  angleDegree <= (angleToCompare + tollerance)){
      return true;
    }
    if(angleToCompare == 0 && angleDegree >= (360 - tollerance) &&  angleDegree <= (360 + tollerance) ){
      return true;
    }

    return false;

  }


    /***********************************************************************************/
  public static Dimension retrievePrintingSize(String strFormatoAlbum) {
    Dimension result;
    strFormatoAlbum = strFormatoAlbum.toLowerCase();
    String strAltezza = strFormatoAlbum.substring(0, strFormatoAlbum.indexOf("x"));
    String strLarghezza = strFormatoAlbum.substring(strFormatoAlbum.indexOf("x") + 1);

    int a = getRealSize(strAltezza);
    int b = getRealSize(strLarghezza);

    result = new Dimension(b, a);

    return result;
  }


  /***********************************************************************************/
  public static int getRealSize(String strSize) {
    int size;

    if(strSize.equals("15")) {
      size = 152;
    }else if(strSize.equals("20")) {
      size = 203;
    }else if(strSize.equals("24")) {
      size = 240;
    }else if(strSize.equals("28")) {
      size = 280;
    }else if(strSize.equals("30")) {
      size = 305;
    }else if(strSize.equals("35")) {
      size = 356;
    }else if(strSize.equals("36")) {
      size = 356;
    }else if(strSize.equals("40")) {
      size = 406;
    }else if(strSize.equals("45")) {
      size = 451;
    }else if(strSize.equals("48")){
      size = 481;
    }else if(strSize.equals("50")) {
      size = 508;
    }else if(strSize.equals("60")) {
      size = 610;
    }else if(strSize.equals("70")) {
      size = 710;
    }else if(strSize.equals("80")) {
      size = 810;
    }else if(strSize.equals("120")) {
      size = 1220;
    }else {
      /*
      int dotIndex = strSize.indexOf(".");
      if(dotIndex!=-1){
        strSize = strSize.substring(0,dotIndex);
      }
      size = Integer.valueOf(strSize).intValue() * 10;
      */

      strSize = StringUtilities.replaceSafety(strSize, ",", ".");
      size = (int)(Double.valueOf(strSize) * 10);

    }

    return size;
  }

  /***********************************************************************************/
  public static int compareProportionsByTag(int width, int height, String stringcontainsProportionsTagToCompare, int tollerance){
    int proportionsTagValueSrc = calculateProportionsTagValue(width, height);
    int proportionsTagValueToCompare = extractProportionsTagValue(stringcontainsProportionsTagToCompare, String.valueOf(proportionsTagValueSrc).length() );

    if(proportionsTagValueToCompare <= 0){
      return -1;
    }

    if( (proportionsTagValueSrc - tollerance ) <= proportionsTagValueToCompare && proportionsTagValueToCompare <= (proportionsTagValueSrc + tollerance ) ){
      return 0;
    } else if(proportionsTagValueSrc < proportionsTagValueToCompare ){
      return -1;
    } else {
      return 1;
    }
    //return
  }


  /***********************************************************************************/
  public static String calculateProportionsTag(int width, int height){
    return PROPORTIONS_TAG_DELIMITER + String.valueOf( calculateProportionsTagValue(width, height) ) + PROPORTIONS_TAG_DELIMITER;
  }

  /***********************************************************************************/
  public static int calculateProportionsTagValue(int width, int height){
    return (int)Math.round(calculateProportions(width, height) * Math.pow(10, 2) );
  }
  
  /***********************************************************************************/
  public static int extractProportionsTagValue(String proportionsTag, int numDigits){
    int result = -1;
    int pos1, pos2;
    String strTmp;
    
    pos1 = proportionsTag.indexOf(PROPORTIONS_TAG_DELIMITER);
    if(pos1 >= 0){
      pos2 = proportionsTag.indexOf(PROPORTIONS_TAG_DELIMITER, pos1 + 1);
      if(pos2 >= 0){
        strTmp = proportionsTag.substring(pos1 + PROPORTIONS_TAG_DELIMITER.length(), pos2);
        
        if(numDigits > 0 && strTmp.length() != numDigits){
          if(strTmp.length() < numDigits){
            strTmp = strTmp + "000000000000".substring(0, numDigits + strTmp.length());
          } else if(strTmp.length() > numDigits){
            strTmp = strTmp.substring(0, numDigits);
          }
        }
        
        result = Integer.valueOf(strTmp);
      }
    }
    
    return result;
  }

  /***********************************************************************************/
//  public static int calculateProportionsTag(int width, int height, int numCifreDecimali){
//    return (int)Math.round(calculateProportions(width, height) * Math.pow(10, numCifreDecimali));
//  }

  
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

  //***************************************************************************
  public static Dimension makeOrizontal(Dimension size) {
    if(!isOrizontal(size)){
      size.setSize(size.height, size.width);
    }

    return size;
  }

  //***************************************************************************
  public static void makeHorizontal(Dimension size) {
    if(!isOrizontal(size)){
      size.setSize(size.height, size.width);
    }
  }

  //***************************************************************************
  public static void makeVertical(Dimension size) {
    if(isOrizontal(size)){
      size.setSize(size.height, size.width);
    }
  }

  //*********************************************
  public static Dimension calcolaMisureMassime(Dimension formato, Dimension maxFormato) {
    Dimension result = new Dimension();

    //Dimension imageSize = new Dimension(background.getWidth(), background.getHeight());

    double differenzaLarghezze = ((double) formato.width) / maxFormato.width;
    double differenzaAltezze = ((double) formato.height) / maxFormato.height;

    if (differenzaLarghezze > differenzaAltezze) {
      result.width = maxFormato.width;
      result.height = Math.round(((float) formato.height * maxFormato.width) / formato.width);
      if(result.height == 0){
        result.height = 1;
      }
    } else {
      result.height = maxFormato.height;
      result.width = Math.round(((float) formato.width * maxFormato.height) / formato.height);
      if(result.width == 0){
        result.width = 1;
      }
    }


    return result;
  }



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


  //*****************************************************
  public static int mmTOpxByDPI(int mm, double DPI) {
    return (int)Math.round((mm * DPI)/CENTIMETRI_PER_POLLICE/10);
  }


  //*****************************************************
  public static int pxTOmmByDPI(int px, int DPI) {
    double result = ((double)px / DPI) * CENTIMETRI_PER_POLLICE * 10;
    //result = result + 0.1;

    return (int)Math.round(result);
  }

  //*****************************************************
  public static int mmTOpx(int mm, double DPC) {
    return (int)Math.round(((double)mm * DPC)/10);
  }

  //*****************************************************
  public static int pxTOmm(int px, double DPC) {
    double result = ((double)px / DPC) * 10;
    //result = result + 0.1;

    return (int)Math.round(result);
  }


  //*****************************************************
  public static void updateMinPoint(Point currentMinPoint, Point point){
    if(point.x < currentMinPoint.x){
      currentMinPoint.x = point.x;
    }
    if(point.y < currentMinPoint.y){
      currentMinPoint.y = point.y;
    }
  }

  //*****************************************************
  public static void updateMaxPoint(Point currentMaxPoint, Point point){
    if(point.x > currentMaxPoint.x){
      currentMaxPoint.x = point.x;
    }
    if(point.y > currentMaxPoint.y){
      currentMaxPoint.y = point.y;
    }
  }


  //*****************************************************
  public static double getMegapixels(Dimension dimPX){
    float result = 0;

    result = (float)(dimPX.width * dimPX.height)/1000000;

    result = Math.round(result * 10) / 10;

    return result;
  }


  private static String language = "it";

  //*****************************************************
  public static double convertCMToLocale(double measureCM){
    double result = measureCM;

    if(getLanguage() != null){
      if(getLanguage().equalsIgnoreCase("en")){
        result = ((double)Math.round((measureCM / CENTIMETRI_PER_POLLICE) * 100)) / 100;
      }
    }

    return result;
  }


  //*****************************************************
  public static String getSiglaCMLocalized(){
    String result = "cm";

    if(getLanguage() != null){
      if(getLanguage().equalsIgnoreCase("en")){
        result = "in";
      }
    }


    return result;
  }

}
