/*
 *  Copyright (C) 1998  Jesper Pedersen <jews@imada.ou.dk>
 *  This code is released under GNU GPL version 2 or later
 */
package streamUtils;
import java.io.*;

public class ReadTextFile {
  private File inputfile;
  private FileReader fr;

  public ReadTextFile (String filename) throws FileNotFoundException, IOException {
    inputfile = new File(filename);
    fr = new FileReader(inputfile);
  }

  public String readFile() throws IOException {
    int c;

    StringBuffer sb = new StringBuffer( (int)inputfile.length() );
    while ((c = fr.read()) != -1) {
      sb.append((char)c);
    }
    close();
    return sb.toString();
  }

  public int readChar() throws IOException {
    return fr.read();
  }

  public String readLine() throws IOException {
    int c;

    StringBuffer sb = new StringBuffer();
    while ((c = fr.read()) != -1) {
      if (c == '\n')
	return sb.toString();
      else
	sb.append((char)c);
    }
      return sb.toString();
  }

  public void close() throws IOException {
    fr.close();
  }
}
