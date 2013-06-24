/*
 *  Copyright (C) 1998  Jesper Pedersen <jews@imada.ou.dk>
 *  This code is released under GNU GPL version 2 or later
 */
package streamUtils;
import java.io.*;

public class WriteTextFile {
  private File outputfile;
  private FileWriter fw;

  public WriteTextFile (String filename) throws FileNotFoundException, IOException {
    outputfile = new File(filename);
    fw = new FileWriter(outputfile);
  }

  public void writeFile(String s) throws IOException {
    int slen = s.length();
    for (int i=0 ; i < slen ; i++) {
      fw.write(s.charAt(i));
    }
    close();
  }

  public void writeChar(int c) throws IOException {
    fw.write(c);
  }

  public void writeLine(String s) throws IOException {
    int slen = s.length();
    for (int i=0 ; i < slen ; i++) {
      fw.write(s.charAt(i));
    }
    fw.write('\n');
  }

  public void close() throws IOException {
    fw.flush();
    fw.close();
  }
}
