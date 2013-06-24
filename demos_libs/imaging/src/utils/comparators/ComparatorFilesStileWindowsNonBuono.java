/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mlpks.utils.comparators;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author developer
 */
public class ComparatorFilesStileWindowsNonBuono implements Comparator {

  Collator defaultCollator = Collator.getInstance(Locale.US);

  public int compare(Object o1, Object o2) {
    defaultCollator.setDecomposition(Collator.FULL_DECOMPOSITION);

    String nomeFile1 = ((File) o1).getName();
    String nomeFile2 = ((File) o2).getName();
    return compare(nomeFile1, nomeFile2);
  }

  /*/
  public int compare(String a, String b) {
    String[] tokensA = tokenize(withoutExtension(a));
    String[] tokensB = tokenize(withoutExtension(b));
    int max = Math.min(tokensA.length, tokensB.length);
    for (int i = 0; i < max; i++) {
      if (tokensA[i].equalsIgnoreCase(tokensB[i])) {
        continue;
      } else if (tokensA[i].matches("\\d+") && tokensB[i].matches("\\D+")) {
        return -1;
      } else if (tokensA[i].matches("\\D+") && tokensB[i].matches("\\d+")) {
        return 1;
      } else if (tokensA[i].matches("\\d+") && tokensB[i].matches("\\d+")) {
        return Integer.valueOf(tokensA[i]) - Integer.valueOf(tokensB[i]);
      } else {
        return tokensA[i].compareTo(tokensB[i]);
      }
    }
    return tokensA.length - tokensB.length;
  }
  //*/

  public int compare(String a, String b) {

    String[] tokensA = tokenize(withoutExtension(a));
    String[] tokensB = tokenize(withoutExtension(b));
    int max = Math.min(tokensA.length, tokensB.length);
    for (int i = 0; i < max; i++) {
      if (tokensA[i].equalsIgnoreCase(tokensB[i])) {
        continue;
      } else if (tokensA[i].matches("\\d+") && tokensB[i].matches("\\D+")) {
        return -1;
      } else if (tokensA[i].matches("\\D+") && tokensB[i].matches("\\d+")) {
        return 1;
      } else if (tokensA[i].matches("\\d+") && tokensB[i].matches("\\d+")) {
        return Integer.valueOf(tokensA[i]) - Integer.valueOf(tokensB[i]);
      } else {
        defaultCollator.compare(tokensA[i], tokensB[i]);
      }
    //return tokensA[i].compareTo(tokensB[i]);
    }
    return tokensA.length - tokensB.length;
  }

  private String[] tokenize(String s) {
    List<String> tokens = new ArrayList<String>();
    Matcher m = Pattern.compile("\\d+|\\D+").matcher(s);
    while (m.find()) {
      tokens.add(m.group());
    }
    return tokens.toArray(new String[]{});
  }

  private String withoutExtension(String s) {
    int lastDot = s.lastIndexOf('.');
    return lastDot < 0 ? s : s.substring(0, lastDot);
  }
}
