/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package images.text;

import images.GenericImage;
import images.GenericImageFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.swing.JTextPane;
import javax.swing.plaf.basic.BasicTextUI;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;
import javax.media.jai.TiledImage;
import utils.JAIUtils;
import utils.Sizes;


/**
 *
 * @author developer
 */
public class TextUtil {

    private static int TOLLERANCE_HORIZONTAL = 16;
    private static int TOLLERANCE_VERTICAL = 4;
    public static final int ALIGN_CENTER = StyleConstants.ALIGN_CENTER;
    public static final int ALIGN_LEFT = StyleConstants.ALIGN_LEFT;
    public static final int ALIGN_RIGHT = StyleConstants.ALIGN_RIGHT;
    public static final int ALIGN_JUSTIFIED = StyleConstants.ALIGN_JUSTIFIED;

    /***********************************************************/
    public static TiledImage buildMinimumTiledImageJAIFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor) {
        return buildMinimumTiledImageJAIFromDocument(doc, alignment, width, height, scaleFactor, 3);
    }

    /***********************************************************/
    public static TiledImage buildMinimumTiledImageJAIFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor,int numLivelli) {
        String strDoc = StyledDocumentToRichText(doc);
        doc = RichTextToStyledDocument(strDoc);

        TiledImage result = null;

        BufferedImage imgOriginal = buildBufferedImageFromDocument(doc, alignment, width, height, 1,3, true);

        Dimension minimumSize = calculateMinimumArea(imgOriginal);

        result = buildTiledImageJAIFromDocument(doc, alignment, minimumSize.width, minimumSize.height, scaleFactor, 4);

        return result;
    }




    /***********************************************************/
    public static BufferedImage buildMinimumBufferedImageFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor) {

        String strDoc = StyledDocumentToRichText(doc);
        doc = RichTextToStyledDocument(strDoc);

        //String strDoc = StyledDocumentToHTML(doc);
        //doc = HTMLToStyledDocument(strDoc);

        BufferedImage imgOriginal = buildBufferedImageFromDocument(doc, alignment, width, height, 1, 3, true);
        //BufferedImageUtils.saveImage(imgOriginal, "c:/xx/a1.jpg");

        Dimension minimumSize = calculateMinimumArea(imgOriginal);
        //Dimension minimumSize = new Dimension( width, height);

        BufferedImage imgResunt = buildBufferedImageFromDocument(doc, alignment, minimumSize.width, minimumSize.height, scaleFactor, 4);
        //BufferedImageUtils.saveImage(imgResunt, "c:/xx/a2.png");

        /*
        FileWriter writer = null;
        try {
        writer = new FileWriter("c:/prova.html");
        MinimalHTMLWriter htmlWriter = new MinimalHTMLWriter(writer, doc);
        htmlWriter.write();
        } catch (BadLocationException ex) {
        ex.printStackTrace();
        } catch (IOException ex) {
        ex.printStackTrace();
        }
         */

        return imgResunt;
    }

    /***********************************************************/
    public static BufferedImage buildBufferedImageFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor) {
        return buildBufferedImageFromDocument(doc, alignment, width, height, scaleFactor, 4, false);
    }

    /***********************************************************/
    public static BufferedImage buildBufferedImageFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor, int numLivelli) {
        return buildBufferedImageFromDocument(doc, alignment, width, height, scaleFactor, numLivelli, false);
    }

    /***********************************************************/
    public static BufferedImage buildBufferedImageFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor, int numLivelli, boolean sfondoQuasiBianco) {

        int imageType = BufferedImage.TYPE_INT_ARGB;
        if (numLivelli == 0 || numLivelli == 4) {
            imageType = BufferedImage.TYPE_INT_ARGB;
        } else if (numLivelli == 3) {
            imageType = BufferedImage.TYPE_INT_BGR;
        }

        BufferedImage image = new BufferedImage(Math.round((float) width * scaleFactor), Math.round((float) height * scaleFactor), imageType);
        Graphics2D g2d = image.createGraphics();

        drawTextFromDocument(g2d, doc, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, 0);

        g2d.dispose();

        return image;
    }

    /***********************************************************/
    private static void drawTextFromDocument(Graphics2D g2d, StyledDocument doc, int alignment, int width, int height, float scaleFactor, int numLivelli, boolean sofondoQuasiBianco, int translateVeritcal) {
        int translateH = 0, translateV = 0;

        if (alignment == StyleConstants.ALIGN_LEFT) {
            translateH = TOLLERANCE_HORIZONTAL / 2;
        } else if (alignment == StyleConstants.ALIGN_RIGHT) {
            translateH = -TOLLERANCE_HORIZONTAL / 2;
        }

        translateV = TOLLERANCE_VERTICAL / 2 + translateVeritcal;

//    if(numLivelli == 0 || numLivelli == 4){
//      translateH = TOLLERANCE_HORIZONTAL / 2;
//      translateV = TOLLERANCE_VERTICAL / 2;
//    }

        if (alignment != StyleConstants.ALIGN_LEFT) {
            MutableAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setAlignment(attr, alignment);
            doc.setParagraphAttributes(0, doc.getLength(), attr, false);
        }

        Rectangle imageBounds = new Rectangle(width, height);


        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);


        //g2d.scale(2,2);
        g2d.scale(scaleFactor, scaleFactor);
        g2d.translate(translateH, translateV);
        AffineTransform old = g2d.getTransform();
        g2d.setTransform(old);

        if (numLivelli == 3) {
            if (sofondoQuasiBianco) {
                g2d.setColor(new Color(254, 254, 254));
            } else {
                g2d.setColor(Color.white);
            }
            g2d.fillRect(-translateH, -translateV, width, height);
            g2d.setColor(Color.black);
        }

        boolean retryPaintText = true;
        int numRetry = 0;
        Throwable lastEx = null;
        while (retryPaintText) { //pezzotto per certe situazioni strane
            numRetry++;
            if (numRetry > 5) {
                throw new RuntimeException("impossibile costruire il testo, controllare ANCHE i printStackTrace precedenti", lastEx);
            }
            retryPaintText = false;
            try {
                JTextPane docPane = new JTextPane();
                //JEditorPane docPane = new JEditorPane();
                //docPane.setContentType("text/html");
                docPane.setContentType("text/rtf");
                docPane.setBorder(null);
                docPane.setEditable(false);
                docPane.setStyledDocument(doc);
                docPane.repaint();
                /*
                try {
                Thread.sleep(50);
                } catch(InterruptedException ex1) {
                }
                //*/

                BasicTextUI btui = (BasicTextUI) docPane.getUI();
                View view = btui.getRootView(docPane);
                view.setSize(imageBounds.width, imageBounds.height);
                g2d.setClip(-translateH, -translateV, imageBounds.width, imageBounds.height);
                view.paint(g2d, imageBounds);
            } catch (Throwable ex) {
                lastEx = ex;
                retryPaintText = true;
                //ex.printStackTrace();
                //JOptionPane.showMessageDialog(null, "errore nella costruzione del testo");
                System.out.println("***errore nella costruzione del testo, riprovo!");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex1) {
                }
            }
        }

    }



    /***********************************************************/
    private static void drawTextCurvedFromString(Graphics2D g2d, String textToDraw, Font font, Color color, int width, int height, float scaleFactor, int numLivelli, boolean sofondoQuasiBianco, int translateVeritcal, Integer moltiplicatoreRaggio) {
        int translateH = 0, translateV = 0;

        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gvTotale = font.createGlyphVector(frc, textToDraw);
        Rectangle boundsPrimoCarattere = gvTotale.getGlyphPixelBounds(0, frc, 0, 0);
        int margineDiSicurezzaVerticale = (boundsPrimoCarattere.height * 2);
        int margineDiSicurezzaOrizzontale = (boundsPrimoCarattere.width * 2);

        float r;

        r = (Math.min(width, height) / 2) - margineDiSicurezzaVerticale;

        if(moltiplicatoreRaggio != null){
          r = r * moltiplicatoreRaggio;
        }

        translateH = 0; //margineDiSicurezzaOrizzontale;

        translateV = (int)r + margineDiSicurezzaVerticale;



        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //g2d.scale(2,2);
        g2d.scale(scaleFactor, scaleFactor);
        AffineTransform old = g2d.getTransform();
        g2d.setTransform(old);

        if (numLivelli == 3) {
            if (sofondoQuasiBianco) {
                g2d.setColor(new Color(254, 254, 254));
            } else {
                g2d.setColor(Color.white);
            }
            g2d.fillRect(-translateH, -translateV, width, height);
            g2d.setColor(Color.black);
        }

        g2d.setColor(color);

        g2d.translate(translateH, translateV);
        //g2d.drawOval(-(int)r, -(int)r, (int)r*2, (int)r*2);

        boolean retryPaintText = true;
        int numRetry = 0;
        Throwable lastEx = null;
        while (retryPaintText) { //pezzotto per certe situazioni strane
            numRetry++;
            if (numRetry > 5) {
                throw new RuntimeException("impossibile costruire il testo, controllare ANCHE i printStackTrace precedenti", lastEx);
            }
            retryPaintText = false;
            try {

              double arclength = 0;
              int length = textToDraw.length();
              for (int i = 0; i < length; i++) {
                GlyphVector gv = font.createGlyphVector(frc, textToDraw.substring(i, i + 1) );

                Point2D p = gv.getGlyphPosition(0);
                Rectangle bounds = gvTotale.getGlyphPixelBounds(i, frc, 0, 0);


                //arclength = (double)bounds.x + (double)bounds.width/2;
                arclength = (double)bounds.x;

                //double theta = Math.PI + arclength / r;
                double theta = Math.PI + Math.PI/2 + arclength / r;

                Point translatePoint = new Point((int)Math.round(r*Math.cos(theta)), (int) Math.round(r*Math.sin(theta)) );
                AffineTransform at = AffineTransform.getTranslateInstance(r*Math.cos(theta), r*Math.sin(theta));
                //AffineTransform at = AffineTransform.getRotateInstance(Math.atan2(translatePoint.x, translatePoint.y));

                at.rotate(theta + Math.PI/2);
                //at.rotate(Math.atan2(translatePoint.x, translatePoint.y)+Math.PI/2);


                Shape glyph = gv.getGlyphOutline(0);
                Shape transformedGlyph = at.createTransformedShape(glyph);

                g2d.fill(transformedGlyph);
              }

            } catch (Throwable ex) {
                lastEx = ex;
                retryPaintText = true;
                //ex.printStackTrace();
                //JOptionPane.showMessageDialog(null, "errore nella costruzione del testo");
                System.out.println("***errore nella costruzione del testo, riprovo!");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex1) {
                }
            }
        }

    }


    /***********************************************************/
    private static Rectangle drawTextCurvedFromStringTest(Graphics2D g2d, String textToDraw, Font font, Color color,int alignment, int width, int height, float scaleFactor, int numLivelli, boolean sofondoQuasiBianco, Double moltiplicatoreRaggio, Point translationSize) {
        int translateH = 0, translateV = 0;

        Point minPoint = new Point(999999999, 999999999);
        Point maxPoint = new Point(-999999999, -999999999);

        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector gvTotale = font.createGlyphVector(frc, textToDraw);
        Rectangle boundsPrimoCarattere = gvTotale.getGlyphPixelBounds(0, frc, 0, 0);
        Rectangle boundsUltimoCarattere = gvTotale.getGlyphPixelBounds(textToDraw.length()-1, frc, 0, 0);
        int margineDiSicurezzaVerticale = (boundsPrimoCarattere.height * 2);

        double r;

        r = (Math.min(width, height) / 2) - margineDiSicurezzaVerticale;

        r = r / scaleFactor;

        if(moltiplicatoreRaggio != null){
          r = r * moltiplicatoreRaggio;
        }

        translateH = 0;
        translateV = (int)r + margineDiSicurezzaVerticale;

        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        //g2d.scale(2,2);
        g2d.scale(scaleFactor, scaleFactor);
        AffineTransform old = g2d.getTransform();
        g2d.setTransform(old);

        int maxBound = 0;
        if(alignment == ALIGN_RIGHT){
          translateH = width - boundsUltimoCarattere.width;
          maxBound = -(boundsUltimoCarattere.x + boundsUltimoCarattere.width);
        } else if(alignment == ALIGN_CENTER){
          translateH = width / 2;
          maxBound = -(boundsUltimoCarattere.x + boundsUltimoCarattere.width)/2;
        }

        if(translationSize != null){
          translateH += Math.round((double)translationSize.x / scaleFactor);
          translateV += Math.round((double)translationSize.y / scaleFactor);
        }

        g2d.translate(translateH, translateV);

        if (numLivelli == 3 || DEBUG_CURVED_TEXT) {
            if (sofondoQuasiBianco) {
                g2d.setColor(new Color(254, 254, 254));
            } else {
                if(color.equals(Color.white)){
                  g2d.setColor(Color.BLACK);
                } else {
                  g2d.setColor(Color.white);
                }
            }
            g2d.fillRect(-translateH, -translateV, width, height);
            g2d.setColor(Color.black);
        }

        g2d.setColor(color);


        if(DEBUG_CURVED_TEXT){
          g2d.drawOval(-(int)r, -(int)r, (int)r*2, (int)r*2);
        }

        //maxBound = maxBound / 2;
        //Point lastPosition = new Point(0, 0);
        boolean retryPaintText = true;
        int numRetry = 0;
        Throwable lastEx = null;
        while (retryPaintText) { //pezzotto per certe situazioni strane
            numRetry++;
            if (numRetry > 5) {
                throw new RuntimeException("impossibile costruire il testo, controllare ANCHE i printStackTrace precedenti", lastEx);
            }
            retryPaintText = false;
            try {

              double arclength = 0;
              int length = textToDraw.length();
              for (int i = 0; i < length; i++) {
              //for (int i = length - 1; i >= 0; i--) {
                GlyphVector gv = font.createGlyphVector(frc, textToDraw.substring(i, i + 1) );

                Rectangle bounds = gvTotale.getGlyphPixelBounds(i, frc, 0, 0);

                //arclength = (double)bounds.x;
                arclength = (double)maxBound + (double)bounds.x;

                double theta = Math.PI + Math.PI/2 + arclength / r;

                Point translatePoint = new Point((int)Math.round(r*Math.cos(theta)), (int) Math.round(r*Math.sin(theta)) );
                AffineTransform at = AffineTransform.getTranslateInstance(r*Math.cos(theta), r*Math.sin(theta));

                at.rotate(theta + Math.PI/2);


                Shape glyph = gv.getGlyphOutline(0);
                Shape transformedGlyph = at.createTransformedShape(glyph);

                if(transformedGlyph.getBounds().width != 0 || transformedGlyph.getBounds().height != 0){//salto i caratteri non visibili
                  Sizes.updateMinPoint(minPoint, transformedGlyph.getBounds().getLocation());
                  Sizes.updateMaxPoint(maxPoint, new Point(transformedGlyph.getBounds().x + transformedGlyph.getBounds().width, transformedGlyph.getBounds().y + transformedGlyph.getBounds().height ) );
                }

                g2d.fill(transformedGlyph);
              }

            } catch (Throwable ex) {
                lastEx = ex;
                retryPaintText = true;
                //ex.printStackTrace();
                //JOptionPane.showMessageDialog(null, "errore nella costruzione del testo");
                System.out.println("***errore nella costruzione del testo, riprovo!");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex1) {
                }
            }

        }//while(retryPaintText
        
        Rectangle result = new Rectangle( (int) Math.round( (double)(minPoint.x + translateH) * scaleFactor) - 1, (int) Math.round( (double)(minPoint.y + translateV) * scaleFactor) - 1,(int) (Math.round((double) (maxPoint.x - minPoint.x) * scaleFactor) + 2), (int) (Math.round((double) (maxPoint.y - minPoint.y) * scaleFactor) + 2));
        //g2d.drawRect(minPoint.x, minPoint.y, maxPoint.x - minPoint.x, maxPoint.y - minPoint.y);

        return result;
    }


    public static Rectangle calculareSizeForTextCurved(String textToDraw, Font font, Color color,int alignment, int width, int height, float scaleFactor, int numLivelli, boolean sofondoQuasiBianco, Integer moltiplicatoreRaggio){
      Rectangle result = new Rectangle();


      return result;
    }



    /***********************************************************/
    public static TiledImage buildTiledImageJAICurved(String text, String fontName, int fontStyle, int fontSize, Color color, int alignment, int width, int height, float scaleFactor, Double moltiplicatoreRaggio, int sizeBehavior) {


        Font currentFont = new Font(fontName, fontStyle, fontSize);

        return buildTiledImageJAICurvedFromText(text, currentFont, color, alignment, width, height, scaleFactor, 4, false, moltiplicatoreRaggio, sizeBehavior);
    }


    /***********************************************************/
    public static TiledImage buildTiledImageJAI(String text, String fontName, int fontSize, Color color, int alignment, int width, int height, float scaleFactor) {


        StyledDocument doc = buildStyledDocument(text, fontName, fontSize, color);

        return buildTiledImageJAIFromDocument(doc, alignment, width, height, scaleFactor, 4);
    }

    /***********************************************************/
    public static BufferedImage buildBufferedImage(String text, String fontName, int fontSize, Color color, int alignment, int width, int height, float scaleFactor) {

        StyledDocument doc = buildStyledDocument(text, fontName, fontSize, color);

        return buildBufferedImageFromDocument(doc, alignment, width, height, scaleFactor);
    }

    /***********************************************************/
    public static BufferedImage buildMinimumBufferedImage(String text, String fontName, int fontSize, Color color, int alignment, int width, int height, float scaleFactor) {

        StyledDocument doc = buildStyledDocument(text, fontName, fontSize, color);

        return buildMinimumBufferedImageFromDocument(doc, alignment, width, height, scaleFactor);
    }




    /***********************************************************/
    public static TiledImage buildMinimumTiledImage(String text, String fontName, int fontSize, Color color, int alignment, int width, int height, float scaleFactor,int numLivelli) {

        StyledDocument doc = buildStyledDocument(text, fontName, fontSize, color);

        return buildMinimumTiledImageJAIFromDocument(doc, alignment, width, height, scaleFactor,numLivelli);
    }

    /***********************************************************/
    public static StyledDocument buildStyledDocument(String text, String fontName, int fontSize, Color color) {
        return buildStyledDocument(text, fontName, fontSize, false, color);
    }

    /***********************************************************/
    public static StyledDocument buildStyledDocument(String text, String fontName, int fontSize, boolean bold, Color color) {
        StyleContext context = new StyleContext();
        DefaultStyledDocument doc = new DefaultStyledDocument(context);
        //StyledDocument doc = HTMLToStyledDocument(text);

        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr, fontName);
        StyleConstants.setFontSize(attr, fontSize);

        StyleConstants.setBold(attr, bold);

        StyleConstants.setForeground(attr, color);

        try {
            doc.insertString(0, text, attr);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }

        return doc;
    }

    /***********************************************************/
    public static StyledDocument appendToStyledDocument(DefaultStyledDocument doc, String text, String fontName, int fontSize, Color color) {
        //StyledDocument doc = HTMLToStyledDocument(text);

        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attr, fontName);
        StyleConstants.setFontSize(attr, fontSize);
        StyleConstants.setForeground(attr, color);

        try {
            doc.insertString(doc.getLength(), "\n" + text, attr);
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }

        return doc;
    }

    /***********************************************************/
    public static TiledImage buildTiledImageJAIFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor) {
        return buildTiledImageJAIFromDocument(doc, alignment, width, height, scaleFactor, 4, false);
    }

    /***********************************************************/
    public static TiledImage buildTiledImageJAIFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor, int numLivelli) {
        return buildTiledImageJAIFromDocument(doc, alignment, width, height, scaleFactor, numLivelli, false);
    }

    /***********************************************************/
    public static TiledImage buildTiledImageJAIFromDocument(StyledDocument doc, int alignment, int width, int height, float scaleFactor, int numLivelli, boolean sfondoQuasiBianco) {
        Dimension resultSize = new Dimension(Math.round((float) width * scaleFactor), Math.round((float) height * scaleFactor));
        if(resultSize.width <= 0){
          resultSize.width = 1;
        }
        if(resultSize.height <= 0){
          resultSize.height = 1;
        }


        TiledImage image = JAIUtils.newTiledImage(resultSize.width, resultSize.height, numLivelli);
        //TiledImage image = new BufferedImage(Math.round((float) imageBounds.width * scaleFactor), Math.round((float) imageBounds.height * scaleFactor), imageType);
        Graphics2D g2d = image.createGraphics();
        drawTextFromDocument(g2d, doc, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, 0);
        g2d.dispose();


        //tutto l'if seguente e' una pezza per risolvere un problema con le stritte: Acluni fonts (per esempio "Champignon.ttf")
        //vengono tagliati a determinate misure (sono differeinti da font a font) e soprattutto il punto in cui vengono tagliate le
        //scritte varia anche semplicemente cambiando i DPI di montaggio di montagenius.
        if(scaleFactor > 1){
          int translationFactor = 2;

          TiledImage imageTranslateUp = JAIUtils.newTiledImage(resultSize.width, resultSize.height, 4);
          Graphics2D g2dUp = imageTranslateUp.createGraphics();
          //g2dUp.setColor(Color.yellow);
          //g2dUp.fillRect(0, 0, imageTranslateUp.getWidth(), imageTranslateUp.getHeight());

          drawTextFromDocument(g2dUp, doc, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, -height / translationFactor);
          g2dUp.dispose();

          //questa translateCompensation serve a risolvere la ifferenza di un pixel che a volte si presenta adottando questa pezza
          //pur avendo aggiunto questo codice cerve lote il problema si presenta lo stesso. la cosa non e' signficativa perche'
          //lo sbalzo di un pixel che si potrebbe verificare in pratica non e' visibile nella stampa.
          int translateCompensation = 0;
          double dblTmp = TOLLERANCE_VERTICAL / 2 -(height / translationFactor);
          dblTmp = dblTmp * (double)scaleFactor;
          if((int)dblTmp != (int)Math.round(dblTmp - 0.3)  ){
            translateCompensation = -1;
          }

          AffineTransform a;
          a = new AffineTransform();
          a.translate(0, (int)Math.round((double)(height / translationFactor)* (double)scaleFactor) + translateCompensation );

          g2d = image.createGraphics();

          g2d.drawRenderedImage(imageTranslateUp, a);
          g2d.dispose();
        }

        return image;
    }



    public static final int SIZE_BEHAVIOR_NONE = 0;
    public static final int SIZE_BEHAVIOR_MINIMUM_IMAGE = 1;
    public static final int SIZE_BEHAVIOR_DECREASE_IF_NEEDED_NO_CHANGE_SIZE = 2;
    private static final boolean DEBUG_CURVED_TEXT = false;
    /***********************************************************/
    public static TiledImage buildTiledImageJAICurvedFromText(String textToDraw, Font font, Color color, int alignment, int width, int height, float scaleFactor, int numLivelli, boolean sfondoQuasiBianco, Double moltiplicatoreRaggio, int sizeBehavior) {
      TiledImage image = null;
      Dimension resultSize = new Dimension(Math.round((float) width * scaleFactor), Math.round((float) height * scaleFactor));
      if(resultSize.width <= 0){
        resultSize.width = 1;
      }
      if(resultSize.height <= 0){
        resultSize.height = 1;
      }

      if(sizeBehavior == SIZE_BEHAVIOR_MINIMUM_IMAGE){
        image = JAIUtils.newTiledImage(10, 10, numLivelli);
        Graphics2D g2d = image.createGraphics();

        Rectangle sizeUsed = drawTextCurvedFromStringTest(g2d, textToDraw, font, color, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, moltiplicatoreRaggio, null);
        g2d.dispose();

        image = JAIUtils.newTiledImage(sizeUsed.width, sizeUsed.height, numLivelli);
        g2d = image.createGraphics();
        drawTextCurvedFromStringTest(g2d, textToDraw, font, color, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, moltiplicatoreRaggio, new Point(-sizeUsed.x, -sizeUsed.y) );
        g2d.dispose();

      } else if(sizeBehavior == SIZE_BEHAVIOR_DECREASE_IF_NEEDED_NO_CHANGE_SIZE){
        image = JAIUtils.newTiledImage(resultSize.width, resultSize.height, numLivelli);
        Graphics2D g2d = image.createGraphics();

        Rectangle sizeUsed = drawTextCurvedFromStringTest(g2d, textToDraw, font, color, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, moltiplicatoreRaggio, null);
        g2d.dispose();


        if(sizeUsed.width > resultSize.width || sizeUsed.height > resultSize.width){
          float scaleFactorH, scaleFactorV;

          scaleFactorH = (float)width / (sizeUsed.width * 1.02f);//1.02 e' una tolleranza, il testo tende sempre ad essere un poco piu' grande di quello che ci si aspetta dallo scaleFactor
          scaleFactorV = (float)height / (sizeUsed.height * 1.02f);//+1 e' la tolleranza

          scaleFactor = scaleFactorV < scaleFactorH ? scaleFactorV : scaleFactorH;


          int inizioTestoInVerticale = sizeUsed.y;

          image = JAIUtils.newTiledImage(resultSize.width + 1, resultSize.height, numLivelli);
          g2d = image.createGraphics();

          sizeUsed = drawTextCurvedFromStringTest(g2d, textToDraw, font, color, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, moltiplicatoreRaggio, null);
          g2d.dispose();

          image = JAIUtils.newTiledImage(resultSize.width + 1, resultSize.height, numLivelli);
          g2d = image.createGraphics();

          sizeUsed = drawTextCurvedFromStringTest(g2d, textToDraw, font, color, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, moltiplicatoreRaggio,  new Point(-sizeUsed.x + ((width - sizeUsed.width) / 2), inizioTestoInVerticale - sizeUsed.y ));//faccio la centratura solo in orizzontale volutamente
          g2d.dispose();

        }
        
        if(DEBUG_CURVED_TEXT){
          g2d = image.createGraphics();
          g2d.setColor(Color.YELLOW);
          g2d.drawRect(sizeUsed.x, sizeUsed.y, sizeUsed.width, sizeUsed.height);
          g2d.dispose();
        }


      } else {


        image = JAIUtils.newTiledImage(resultSize.width, resultSize.height, numLivelli);
        Graphics2D g2d = image.createGraphics();
        //g2d.setColor(Color.GREEN);
        //g2d.fillRect(0, 0, width, height);

        //drawTextCurvedFromString(g2d, textToDraw, font, color, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, 0, moltiplicatoreRaggio);
        Rectangle sizeUsed = drawTextCurvedFromStringTest(g2d, textToDraw, font, color, alignment, width, height, scaleFactor, numLivelli, sfondoQuasiBianco, moltiplicatoreRaggio, null);

        g2d.dispose();

        if(DEBUG_CURVED_TEXT){
          g2d = image.createGraphics();
          g2d.setColor(Color.GREEN);
          g2d.drawRect(sizeUsed.x, sizeUsed.y, sizeUsed.width, sizeUsed.height);
          g2d.dispose();
        }
      }



        return image;
    }




    /***********************************************************/
    public static Dimension calculateMinimumArea(BufferedImage image) {
        Dimension result = new Dimension(image.getWidth(), image.getHeight());

        boolean flgRigaBianga = true;

        int intColor;

        //ellimina il bianco in basso
        flgRigaBianga = true;
        do {
            for (int i = 0; i < result.width; i++) {
                intColor = image.getRGB(i, result.height - 1);
                //if (Color.white.getRGB() != intColor) {
                if ((new Color(254, 254, 254)).getRGB() != intColor) {
                    flgRigaBianga = false;
                    break;
                }
            }

            if (flgRigaBianga) {
                result.height--;
            }
            if (result.height <= 1) {
                flgRigaBianga = false;
            }

        } while (flgRigaBianga);



        //elimino il buanco a destra
        flgRigaBianga = true;
        do {
            for (int i = 0; i < result.height; i++) {
                intColor = image.getRGB(result.width - 1, i);
                //if (Color.white.getRGB() != intColor) {
                if ((new Color(254, 254, 254)).getRGB() != intColor) {
                    flgRigaBianga = false;
                    break;
                }
            }

            if (flgRigaBianga) {
                result.width--;
            }
            if (result.width <= 1) {
                flgRigaBianga = false;
            }

        } while (flgRigaBianga);


        //elimino il buanco a sinistra
        flgRigaBianga = true;
        int leftOffset = 0;
        do {
            for (int i = 0; i < result.height; i++) {
                intColor = image.getRGB(leftOffset, i);
                //if (Color.white.getRGB() != intColor) {
                if ((new Color(254, 254, 254)).getRGB() != intColor) {
                    flgRigaBianga = false;
                    break;
                }
            }

            if (flgRigaBianga) {
                result.width--;
                leftOffset++;
            }
            if (result.width <= 1) {
                flgRigaBianga = false;
            }

        } while (flgRigaBianga);


        result.width += TOLLERANCE_HORIZONTAL;
        result.height += TOLLERANCE_VERTICAL;
        return result;
    }

    /***********************************************************/
    public static StyledDocument RichTextToStyledDocument(String richTextString) {
        return RichTextToStyledDocument(richTextString, null);
    }

    /***********************************************************/
    public static StyledDocument RichTextToStyledDocument(String richTextString, DefaultStyledDocument lpd) {
        StyledEditorKit kit = new StyledEditorKit();

        RTFEditorKit rtf = new RTFEditorKit();
        StyleContext context = new StyleContext();
        if (lpd == null) {
            lpd = new LimitedStyledDocument(context);
        }

        try {
            InputStream in = new ByteArrayInputStream(richTextString.getBytes());
            //rtf.write(out, doc, 0, doc.getLength());
            rtf.read(in, lpd, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            //stranamente la conversione da richtext a styleddocument aggiunge un ritorno a capo in pi�
            lpd.remove(lpd.getLength() - 1, 1);
        } catch (BadLocationException ex1) {
            ex1.printStackTrace();
        }

        return lpd;
    }

    /***********************************************************/
    public static StyledDocument buildLimitedStyledDocument() {
        StyleContext context = new StyleContext();
        StyledDocument lpd = new LimitedStyledDocument(context);

        return lpd;
    }

    /***********************************************************/
    public static String StyledDocumentToRichText(StyledDocument doc) {
        String result = null;
        RTFEditorKit rtf = new RTFEditorKit();
        StyleContext context = new StyleContext();
        DefaultStyledDocument lpd = new DefaultStyledDocument(context);
        JTextPane tp = new JTextPane();

        tp.setEditorKit(rtf);
        tp.setDocument(lpd);
        tp.setDocument(doc);


        try {
            OutputStream out = new ByteArrayOutputStream();
            rtf.write(out, doc, 0, doc.getLength());
            result = out.toString();
            result = result.substring(0, result.length() - 1);
            //System.out.println("[" + result + "]");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /***********************************************************/
    public static StyledDocument HTMLToStyledDocument(String richTextString) {
        //StyledDocument docResult = null;
        HTMLEditorKit htmlKit = new HTMLEditorKit();




        //StyledEditorKit kit = new StyledEditorKit();
        //StyledDocument docResult = (StyledDocument) kit.createDefaultDocument();

        //JEditorPane editorPane = new JEditorPane();

        //HTMLEditorKit htmlKit = new HTMLEditorKit();
        HTMLDocument docResult = (HTMLDocument) htmlKit.createDefaultDocument();



        try {
            InputStream in = new ByteArrayInputStream(richTextString.getBytes());
            htmlKit.read(in, docResult, 0);


            /*
            JTextPane pane = new JTextPane();
            pane.setContentType("text/html");
            pane.setEditable(false);

            //String str = FileUtilities.fileToStringRaw("c:/prova.html");
            //pane.setText(str);
            pane.setStyledDocument(docResult);

            JFrame frame = new JFrame("J2TextPrinter test");
            frame.getContentPane().add(pane);
            frame.setSize(300,200);
            frame.setVisible(true);

            Thread.sleep(10000);
             */
            //docResult = pane.getStyledDocument();



            /*
            JEditorPane editorPane = new JEditorPane();
            editorPane.setContentType("text/html");
            String str = FileUtilities.fileToStringRaw("c:/prova.html");
            editorPane.setText(str);
            editorPane.repaint();
             */

            //editorPane.setEditorKit(new HTMLEditorKit());

            /*
            String filename = "c:/prova.html";

            FileReader reader = new FileReader(filename);
            editorPane.read(reader, filename);
             */

            //docResult = (StyledDocument)editorPane.getDocument();


            /*
            FileReader reader = new FileReader("c:/prova.html");
            HTMLEditorKit.Parser parser = new ParserDelegator();
            HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
            parser.parse(reader, callback, true);
            editorPane.setDocument(htmlDoc);
            return (StyledDocument)editorPane.getDocument();
             */


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return docResult;
    }

    /***********************************************************/
    public static String StyledDocumentToHTML(StyledDocument doc) {
        String result = null;
        HTMLEditorKit rtf = new HTMLEditorKit();
        //StyleContext context = new StyleContext();
        //DefaultStyledDocument lpd = new DefaultStyledDocument(context);
        //JTextPane tp = new JTextPane();

        //tp.setEditorKit(rtf);
        //tp.setDocument(lpd);
        //tp.setDocument(doc);


        try {
            OutputStream out = new ByteArrayOutputStream();
            rtf.write(out, doc, 0, doc.getLength());
            result = out.toString();
            System.out.println("[" + result + "]");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return result;
    }

    /************************************************************************************/
    /*
    public static Font[] getSystemFonts2() {
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font systemFonts[] = gEnv.getAllFonts();
        return systemFonts;
    }
    //*/

    /************************************************************************************/
    //*/
    public static String[] getSystemFonts() {
        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String systemFonts[] = gEnv.getAvailableFontFamilyNames();
        return systemFonts;
    }
    //*/

    /************************************************************************************/
    public static String[] getAllowedFonts() {

        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String systemFonts[] = gEnv.getAvailableFontFamilyNames();

        Vector vector = new Vector();

        Vector fonts = new Vector();
        fonts.add("Arial");
        fonts.add("Arial Black");
        fonts.add("Comic Sans MS");
        fonts.add("Courier New");
        fonts.add("Copperplate Gothic Bold");
        fonts.add("Dialog");
        fonts.add("DialogInput");
        fonts.add("Estrangelo Edessa");
        fonts.add("Franklin Gothic Medium");
        fonts.add("Gautami");
        fonts.add("Georgia");
        fonts.add("Harlow Solid Italic");
        fonts.add("Impact");
        fonts.add("Kartika");
        fonts.add("Kunstler Script");
        fonts.add("Latha");
        fonts.add("Lucida Console");
        fonts.add("Lucida Sans");
        fonts.add("Lucida Sans Unicode");
        fonts.add("Mangal");
        fonts.add("Marlett");
        fonts.add("Microsoft Sans Serif");
        fonts.add("Monospaced");
        fonts.add("MV Boli");
        fonts.add("Onyx");
        fonts.add("Palatino Linotype");
        fonts.add("Papyrus");
        fonts.add("Perpetua");
        fonts.add("Pristina");
        fonts.add("Raavi");
        fonts.add("SansSerif");
        fonts.add("Serif");
        fonts.add("Shelley Allegro BT");
        fonts.add("Shruti");
        fonts.add("Sylfaen");
        fonts.add("Symbol");
        fonts.add("Tahoma");
        fonts.add("Times New Roman");
        fonts.add("Trebuchet MS");
        fonts.add("Tunga");
        fonts.add("Verdana");
        fonts.add("Vrinda");
        fonts.add("Webdings");
        fonts.add("Wingdings");

        for (int i = 0; i < systemFonts.length; i++) {
            if (fonts.contains(systemFonts[i])) {
                vector.add(systemFonts[i]);
            }
        }


        String[] stringArray = new String[vector.size()];
        for (int i = 0; i < vector.size(); i++) {
            stringArray[i] = (String) vector.get(i);
        }
        return stringArray;
    }

    public static void main(String[] args) {

        GenericImage img = creaImmagineIntestazionePerNomeSito("la fotografia di amilcare Bonifacio aaaa", "via aaaaaaaaasds ecfnien edinei, 23, SOMMA VESUVIANA(NAPOLI)", "0818994455");
        img.saveAsWithoutIcc("/1.jpg");

        img = creaImmagineIntestazionePerNomeSito("Leo", "via aaaaaaaaas, 23, SOMMA VESUVIANA(NAPOLI)", "");
        img.saveAsWithoutIcc("/2.jpg");

        img = creaImmagineIntestazionePerNomeSito("Leo", "via aaaaaaaaas, 23, SOMMA VESUVIANA(NAPOLI)", "0811212121");
        img.saveAsWithoutIcc("/3.jpg");

    }

    //*********************************************************************/
    public static GenericImage creaImmagineIntestazionePerNomeSito(String nomeSito, String indirizzo, String tel) {
        GenericImage result = null, imgTmp;

        int maxW = 600, maxH = 70;

        result = GenericImageFactory.getInstance().buildGenericImage(maxW, maxH, 4);

        imgTmp = GenericImageFactory.getInstance().buildGenericImage(TextUtil.buildBufferedImage(nomeSito, "Pristina", 25, Color.red, TextUtil.ALIGN_CENTER, maxW, maxH / 2, 1));
        result.DrawImage(imgTmp, 0, 0);

        String str = indirizzo;
        if (tel != null && tel.length() > 1) {
            str = str + "\n" + "tel: " + tel;
        }
        imgTmp = GenericImageFactory.getInstance().buildGenericImage(TextUtil.buildBufferedImage(str, "", 12, Color.black, TextUtil.ALIGN_CENTER, maxW, maxH / 2, 1));
        result.DrawImage(imgTmp, 0, result.getHeight() - imgTmp.getHeight());

        imgTmp = result;

        result = GenericImageFactory.getInstance().buildGenericImage(maxW, maxH, 3);
        result.fill(Color.white);
        result.DrawImage(imgTmp, 0, 0);

        return result;
    }


    /***********************************************************/
    public static BufferedImage buildMaxBufferedImage(String text, String fontName, Color color, int alignment, int maxWidth, int maxHeight, int numLivelli) {

      StyledDocument doc = buildStyledDocument(text, fontName, 12, color);

      return buildMaxBufferedImageFromDocument(doc, alignment, maxWidth, maxHeight, numLivelli);

    }

    /***********************************************************/
    public static BufferedImage buildMaxBufferedImageFromDocument(StyledDocument styledDocument, int alignment, int maxW, int maxH, int numLivelli) {

        GenericImage bozzaImg = GenericImageFactory.getInstance().buildGenericImage(TextUtil.buildMinimumBufferedImageFromDocument(styledDocument, alignment, 1000, 1000, 1));
        float rappW = (float)maxW / bozzaImg.getWidth();
        float rappH = (float)maxH / bozzaImg.getHeight();
        float scaleFactor = Math.min(rappW, rappH);

        return TextUtil.buildBufferedImageFromDocument(styledDocument, alignment, bozzaImg.getWidth(), bozzaImg.getHeight(), scaleFactor, numLivelli);
    }


    /***********************************************************/
    public static TiledImage buildMaxTiledImageJAI(String text, String fontName, Color color, int alignment, int maxWidth, int maxHeight, int numLivelli) {

      StyledDocument doc = buildStyledDocument(text, fontName, 12, color);

      return buildMaxTiledImageJAIFromDocument(doc, alignment, maxWidth, maxHeight, numLivelli);

    }


    /***********************************************************/
    public static TiledImage buildMaxTiledImageJAIFromDocument(StyledDocument styledDocument, int alignment, int maxW, int maxH, int numLivelli) {

        GenericImage bozzaImg = GenericImageFactory.getInstance().buildGenericImage(TextUtil.buildMinimumBufferedImageFromDocument(styledDocument, alignment, 1000, 1000, 1));
        float rappW = (float)maxW / bozzaImg.getWidth();
        float rappH = (float)maxH / bozzaImg.getHeight();
        float scaleFactor = Math.min(rappW, rappH);
        
        return TextUtil.buildTiledImageJAIFromDocument(styledDocument, alignment, bozzaImg.getWidth(), bozzaImg.getHeight(), scaleFactor, numLivelli);
    }


    /***********************************************************/
    public static TiledImage buildFixedHeightTiledImage(String inputText,String fontName,int fontStyle,Color fontColor,int alignment, int maxWidthMM,int desiredHeightMM,int DPI,int numLivelli){
        
        int maxW_px = Sizes.mmTOpxByDPI(maxWidthMM, DPI);

        int desideredHeightPx = Sizes.mmTOpxByDPI(desiredHeightMM, DPI);

        int fontSize = FontsUtil.calculateFontSize(DPI,desiredHeightMM);//hMM_forNomeCliente -> MM altezza max per il nome

        int textWidthPx = FontsUtil.calculateTextWidthBasedOnFont(inputText, fontName, fontStyle, fontSize);
        
        TiledImage image = TextUtil.buildMinimumTiledImage(inputText, fontName, fontSize,fontColor, alignment, (int)(textWidthPx*1.2), desideredHeightPx*2, 1,numLivelli);

        if(image.getWidth()>maxW_px){
            float scaleFactor = (float)maxW_px/image.getWidth();
            image = TextUtil.buildMinimumTiledImage(inputText, fontName, fontSize, fontColor, alignment, (int)(textWidthPx*1.2), desideredHeightPx*2, scaleFactor,numLivelli);
        }

        return image;
    }




}
