/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images.text;

import java.awt.Font;
import rfile.RecursiveFileImpl;

/**
 *
 * @author developer
 */
public class FontInfos {
  public Font font;
  public RecursiveFileImpl file;

  public FontInfos(Font font, RecursiveFileImpl file){
    this.font = font;
    this.file = file;
  }

}
