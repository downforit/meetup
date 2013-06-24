/*
 * RecursiveFileOutput.java
 *
 * Created on 8 giugno 2007, 11.50
 *
 */

package rfile;

/**
 *
 * @author gundam
 */
public interface RecursiveFileOutput {
    public void updatePercentage(String percentage, String fileName);
    public void updateFileNumber(String actualFileNumber, String totalFileInDir, String fileName);
}
