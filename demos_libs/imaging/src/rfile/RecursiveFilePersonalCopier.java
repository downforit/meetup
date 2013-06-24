/*
 * RecursiveFilePersonalCopier.java
 *
 * Created on 15 novembre 2007, 12.32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rfile;

import java.io.File;

/**
 *
 * @author marber
 */
public interface RecursiveFilePersonalCopier {
  public boolean fileNeedPersonalCopying(File in);
  public void copyFile(File in, File out, RecursiveFile recursirveFile);

  public boolean dirNeedPersonalCopying(File in);
  public void copyDir(File in, File out, RecursiveFile recursirveFile);
}
