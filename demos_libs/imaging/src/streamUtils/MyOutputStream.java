/*
 *  Copyright (C) 1998  Jesper Pedersen <jews@imada.ou.dk>
 *  This code is released under GNU GPL version 2 or later
 */
package streamUtils;
import java.io.*;

public class MyOutputStream extends BufferedOutputStream {
  boolean eos = false;
  
  public MyOutputStream(OutputStream out) {
    super(out);
  }
  
  public synchronized void writeChar(int i) throws IOException {
    out.write(i);
  }
}

