/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mlpks.utils.comparators;

import java.io.File;
import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

/**
 *
 * @author developer
 */
public class ComparatorCollator implements Comparator{
  Collator defaultCollator = Collator.getInstance(Locale.ITALIAN);

  public int compare(Object o1, Object o2) {
    defaultCollator.setDecomposition(Collator.FULL_DECOMPOSITION);
    String nomeFile1 = ((File)o1).getName();
    String nomeFile2 = ((File)o2).getName();
    return defaultCollator.compare(nomeFile1, nomeFile2);
  }

}
