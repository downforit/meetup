package streamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ProcessStreamReader extends Thread {
    
    String                  name;
    StringBuffer            stream;
    InputStreamReader       in;
    final static int        BUFFER_SIZE     = 256;
    
    public ProcessStreamReader(String name, InputStream in) {
        super();
        this.name = name;
        this.in = new InputStreamReader(in);
        this.stream = new StringBuffer();
    }
    
    public void run() {
        try {
            int read;
            char[] c = new char[BUFFER_SIZE];
            while ((read=in.read(c,0,BUFFER_SIZE-1)) > 0) {
                stream.append(c, 0, read);
                if (read < BUFFER_SIZE - 1)
                    break;
            }
        } catch (IOException io) {}
    }
    
    public String getString() {
        return stream.toString();
    }
    
}
