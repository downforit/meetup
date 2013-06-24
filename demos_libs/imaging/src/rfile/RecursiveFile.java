/*
 * RecursiveFile.java
 *
 * Created on 8 giugno 2007, 10.54
 *
 */

package rfile;

import java.io.File;

/**
 *
 * @author gundam
 */
public interface RecursiveFile {
    public void copyContentTo(String pathDestination);
    public void copyThisTo(String pathDestination);
    public void moveTo(String pathDestination);
    public boolean renameThis(String newPathName, boolean createFullPath);
    public void deleteThis();
    public void deleteThis(Boolean generateException);
    public void incrementsNumFilesCopied(int incrementValue);
    public void updateOutput(File in, File out);
}
