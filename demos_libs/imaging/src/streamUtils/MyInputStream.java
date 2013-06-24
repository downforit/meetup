/*
 *  Copyright (C) 1998  Jesper Pedersen <jews@imada.ou.dk>
 *  This code is released under GNU GPL version 2 or later
 */
package streamUtils;

import java.io.*;

public class MyInputStream extends BufferedInputStream {

  boolean eos = false;

  public MyInputStream(InputStream in) {
    super(in);
  }

  public synchronized int readChar() throws IOException {
    int ch;

    ch = in.read();
    return ch;
  }

  public synchronized String readLine() throws IOException {
    StringBuffer buf = new StringBuffer(100);
    int ch;

    if (eos == false) {
      while ((ch = in.read()) != -1) {
        if (ch == '\n') {
          return buf.toString();
        } else {
          buf.append((char) ch);
        }
      }
      eos = true;
    } else {
      if (buf.length() > 0) {
        return buf.toString();
      }
    }
    throw new EOFException();
  }
}

