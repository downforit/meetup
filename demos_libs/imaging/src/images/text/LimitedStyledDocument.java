/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package images.text;

import java.awt.Toolkit;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;

/**
 *
 * @author gino
 */
public class LimitedStyledDocument extends DefaultStyledDocument {
  private int maxCharacters = -1;
  private boolean disabledRitornoACapo = false;

  public LimitedStyledDocument() {
    super();
  }
  public LimitedStyledDocument(StyleContext styles) {
    super(styles);
  }

  /**
   * @return the maxCharacters
   */
  public int getMaxCharacters() {
    return maxCharacters;
  }

  /**
   * @param maxCharacters the maxCharacters to set
   */
  public void setMaxCharacters(int maxCharacters) {
    this.maxCharacters = maxCharacters;
  }


  @Override
  public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException {
    boolean discardText = false;

    int currLenght = this.getText(0, this.getLength()).length();
    if(maxCharacters > 0 && (currLenght + str.length()) > maxCharacters){
      discardText = true;
    }

    if(this.isDisabledRitornoACapo()){
      if(str.contains("\n") || str.contains("\r")){
        discardText = true;
      }
    }

    if(discardText){
      Toolkit.getDefaultToolkit().beep();
    } else {
      super.insertString(offs, str, attribute);
    }

  }

  /**
   * @return the disabledRitornoACapo
   */
  public boolean isDisabledRitornoACapo() {
    return disabledRitornoACapo;
  }

  /**
   * @param disabledRitornoACapo the disabledRitornoACapo to set
   */
  public void setDisabledRitornoACapo(boolean disabledRitornoACapo) {
    this.disabledRitornoACapo = disabledRitornoACapo;
  }

}
