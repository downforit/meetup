package utils;

import java.awt.*;
import java.io.*;
import java.util.*;

import javax.swing.AbstractButton;
import java.net.*;
import javax.swing.ButtonGroup;
import org.w3c.dom.Document;
import rfile.RecursiveFileImpl;
import streamUtils.MyInputStream;

public class SystemUtils {

    public static final String DRIVE_TYPE_CD = "ROM";
    public static final String DRIVE_TYPE_HD = "FISSA";

    public static void main(String[] args) {
        /*SystemTray s = SystemTray.getSystemTray();
        try {
        Image image = Toolkit.getDefaultToolkit().createImage("/foto.jpg");
        TrayIcon t = new TrayIcon(image);
        //s.add(t);
        s.remove(t);

        TrayIcon ts[] =s.getTrayIcons();
        for(int i=0;i<ts.length;i++){
        if(ts[i].getPopupMenu()==null){
        int a = 0;
        }
        }
        } catch (Throwable ex) {
        ex.printStackTrace();
        }*/
        System.out.println(getHDSerialNumber());
    }

    /**************************************************************************************************/
    public static void eseguiShutdown() {

        String command;
        Process ej = null;

        if (isWindows()) {
            command = "shutdown -s -t 2";
        } else if (isUnix()) {
            command = "poweroff -p";
        } else {
            //todo:
            command = "";
        }
        try {
            ej = Runtime.getRuntime().exec(command);
        } catch (IOException ex) {
            ex.printStackTrace();

        }
    }

    /**************************************************************************************************/
    public static void openBrowser(String url) {
        if (isMac()) {
            openBrowserForMac(url);
        } else if (isWindows()) {
            openBrowserForWindows(url);
        } else {
            throw new RuntimeException("Operazione non supportata per il sistema specificato!");
        }
    }

    /**************************************************************************************************/
    private static void openBrowserForWindows(String strUrl) {
        try {
            Desktop.getDesktop().browse(URI.create(strUrl));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void openBrowserForMac(String url) {
        String[] commands = new String[]{"open", url};

        Process ej = null;
        try {
            ej = Runtime.getRuntime().exec(commands);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void openJavaPreferencesForMac() {
        File fdJavaPreferences = new File("/Applications/Utilities/Java/Java Preferences.app");
        if (!fdJavaPreferences.exists()) {
            fdJavaPreferences = new File("/Applications/Utilities/Java Preferences.app");
        }

        String[] commands = new String[]{"open", fdJavaPreferences.getAbsolutePath()};

        Process ej = null;
        try {
            ej = Runtime.getRuntime().exec(commands);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static String getUserHome(){
      return System.getProperty("user.home");
    }

    public static String getJavaVersion() {
        String result = System.getProperties().getProperty("java.version");
        return result;
    }


    //ritorna false se la versione di java � maggiore di quella passata come parametro
    public static boolean checkJavaVersion(String maxJavaVersion){
      boolean result = true;
      String currentJavaVersion = getJavaVersion();

      try{
      int currentJavaVersionComponents[] = divideJavaVersion(currentJavaVersion);
      int maxJavaVersionComponents[] = divideJavaVersion(maxJavaVersion);

      for(int i = 0; i < currentJavaVersionComponents.length; i++){
        if(currentJavaVersionComponents[i] > maxJavaVersionComponents[i]){
          result = false;
        }
      }//for
      } catch(Throwable ex){
        System.out.println("impossibile confrontare le versioni di java");
        ex.printStackTrace();
        result = false;
      }

      return result;
    }

    public static int [] divideJavaVersion(String javaVersion){
      int result[] = new int[4];

      int intTmp;

      intTmp = javaVersion.indexOf(".");
      result[0] = Integer.parseInt(javaVersion.substring(0, intTmp));
      javaVersion = javaVersion.substring(intTmp + 1);

      intTmp = javaVersion.indexOf(".");
      result[1] = Integer.parseInt(javaVersion.substring(0, intTmp));
      javaVersion = javaVersion.substring(intTmp + 1);

      intTmp = javaVersion.indexOf("_");
      result[2] = Integer.parseInt(javaVersion.substring(0, intTmp));
      javaVersion = javaVersion.substring(intTmp + 1);

      result[3] = Integer.parseInt(javaVersion);
      return result;
    }



    public static String getOSName() {
      return System.getProperties().getProperty("os.name");
    }

    public static boolean isMac() {
        if (System.getProperties().getProperty("os.name").toLowerCase().startsWith("mac")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isWindows() {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();

        Properties p = System.getProperties();
        if (osName.startsWith("win")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isVistaLike() {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();

        Properties p = System.getProperties();
        if (osName.startsWith("win") && (osName.indexOf("Vista") >= 0 || osName.indexOf("7") >= 0)) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isUnix() {
        String osName = System.getProperty("os.name");
        osName = osName.toLowerCase();

        if (osName.startsWith("linux")) {
            return true;
        } else {
            return false;
        }
    }

    public static String[] getRmovableRootsList() {
        File[] fdRoots = File.listRoots();
        Vector<String> vRoots = new Vector<String>();
        for (int i = 0; i < fdRoots.length; i++) {
            if (!(fdRoots[i].getAbsolutePath().toLowerCase().startsWith("c:")) && !(fdRoots[i].getAbsolutePath().toLowerCase().startsWith("a:")) && !(fdRoots[i].getAbsolutePath().toLowerCase().startsWith("b:"))) {
                vRoots.add(fdRoots[i].getAbsolutePath());
            }
        }

        String[] ret = new String[vRoots.size()];

        for (int i = 0; i < vRoots.size(); i++) {
            ret[i] = vRoots.get(i);
        }

        return ret;
    }

    public static String[] getCdList() {
        return getDrivesList(DRIVE_TYPE_CD);
    }

    public static String[] getHardDriveList() {
        return getDrivesList(DRIVE_TYPE_HD);
    }

    public static boolean isPathInList(String path, String[] driveList) {
        String searchedDrive = path.substring(0, 2);
        /*System.err.println("path = "+path);
        System.err.println("searchedDrive = "+searchedDrive);
        System.err.println("driveList.length = "+driveList.length);*/
        for (int i = 0; i < driveList.length; i++) {
            //System.err.println("i="+i+"driveList[i]="+driveList[i]);
            if (driveList[i].startsWith(searchedDrive)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isPathLocale(String path) {
        String hardDrives[] = SystemUtils.getHardDriveList();
        if (isPathFromUnitaDiRete(path, hardDrives)) {
            return false;
        } else {
            String searchedDrive = path.substring(0, 2);
            for (int i = 0; i < hardDrives.length; i++) {
                if (hardDrives[i].startsWith(searchedDrive)) {
                    return true;
                }
            }
            return false;
        }
    }

    public static boolean isPathFromUnitaDiRete(String path) {
        String hardDrives[] = SystemUtils.getHardDriveList();
        return isPathFromUnitaDiRete(path, hardDrives);
    }

    public static boolean isPathFromUnitaDiRete(String path, String[] driveList) {
        String searchedDrive = path.substring(0, 2);
        if (searchedDrive.indexOf(":") != -1) {
            for (int i = 0; i < driveList.length; i++) {
                //System.err.println("i="+i+"driveList[i]="+driveList[i]);
                if (driveList[i].startsWith(searchedDrive)) {
                    return false;
                }
            }
            return true;
        } else { //nn contiene i due punti
            return false;
        }

    }

    //***************************************************************************//
    private static String[] getDrivesList(String type) {
        //todo: TROVARE SOLUZIONE X LINUX
        ArrayList a = new ArrayList();
        Process p;
        int i = 0, j = 0;
        File[] exUnit = File.listRoots();
        try {
            while (i < exUnit.length) {
                p = Runtime.getRuntime().exec("fsutil fsinfo drivetype " + exUnit[i].getAbsolutePath());
                InputStreamReader in = new InputStreamReader(p.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String ret = br.readLine();
                if (ret.toUpperCase().indexOf(type) != -1) {
                    a.add(exUnit[i].getAbsolutePath().toString());
                    j++;
                }
                i++;
            }
        } catch (IOException ex) {
            //System.err.println("Errore  nel recuperare  la drive list di tipo "+type);
        }
        String[] ToReturn = new String[j];
        Iterator it = a.iterator();
        i = 0;
        while (it.hasNext()) {
            ToReturn[i] = (String) it.next();
            i++;
        }
        return ToReturn;
    }

    /**
     * in java 5 nn c'e' il metodo clearSelection in buttonGroup
     * 
     * @param buttonGroup
     */

    public static void clearSelectionForButtonGroup(ButtonGroup buttonGroup) {

        //in java 5 nn c'e' il metodo clearSelection in buttonGroup
        Enumeration<AbstractButton> buttons = buttonGroup.getElements();
        while(buttons!=null && buttons.hasMoreElements()){
            AbstractButton b = buttons.nextElement();
            b.setSelected(false);
        }
    }

    /********************************************************************************************************/
    public static void setWaitCursorMouse(Component jComponent) {
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        jComponent.setCursor(hourglassCursor);
    }

    /********************************************************************************************************/
    public static void setNormalCursorMouse(Component jComponent) {
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        jComponent.setCursor(normalCursor);
    }

    /********************************************************************************************************/
    public static void setHandCursorMouse(Component jComponent) {
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        jComponent.setCursor(handCursor);
    }

    /********************************************************************************************************/
    public static void openWindowsExplorerWithSelection(String dirPathToOpen, String fileNameToSelect) {
        if (dirPathToOpen == null || !SystemUtils.isWindows()) {
            return;
        }
        String commands[] = new String[3];
        commands[0] = "explorer.exe";
        commands[1] = "/select,";
        commands[2] = dirPathToOpen + File.separator + fileNameToSelect;
        try {
            Runtime.getRuntime().exec(commands);
        } catch (IOException ex1) {
            ex1.printStackTrace();
            openWindowsExplorer(dirPathToOpen);
        }
    }

    /********************************************************************************************************/
    public static void openWindowsExplorer(String path) {
        if (path == null || !SystemUtils.isWindows()) {
            return;
        }
        String commands[] = new String[2];
        commands[0] = "explorer.exe";
        commands[1] = path;
        try {
            Runtime.getRuntime().exec(commands);
        } catch (IOException ex1) {
            ex1.printStackTrace();
        }
    }

    public static void eseguiComando(String cmd[]) {
        eseguiComando(cmd, true);

    }

//***************************************************************************//
    public static void eseguiComando(String cmd[], boolean waitForOutput) {
        try {
            for (int i = 0; i < cmd.length; i++) {
                System.out.println("parametro[" + i + "] = [" + cmd[i] + "]");
            }


            Process p = Runtime.getRuntime().exec(cmd);
            if (!waitForOutput) {
                return;
            }
            MyInputStream lineIn = new MyInputStream(p.getInputStream());
            int c;
            while ((c = lineIn.readChar()) != -1) {
                if (c == 45) {
                    String outs = String.valueOf((char) c);
                    for (int i = 1; i <= 4; i++) {
                        c = lineIn.readChar();
                        outs = outs.concat(String.valueOf((char) c));
                    }
                    if (outs.toLowerCase().equals("-err-")) {
                        System.out.println(outs);
                        throw new RuntimeException(" -=- Errore di Esecuzione " + cmd + " -=- ");
                    }
                }
            }
        } catch (IOException ex) {
            //ex.printStackTrace();
            throw new RuntimeException(" -=- Errore di Esecuzione " + cmd + " -=- ", ex);
        }
    }

    //***************************************************************************//
    public static String eseguiCmdScript(String commandHomePath, String command) {
        return eseguiScript(commandHomePath + File.separator + command, commandHomePath);
    }

    //***************************************************************************//
    public static String eseguiScript(String commandHomePath, String command, String workDir) {
        return eseguiScript(commandHomePath + File.separator + command, workDir);
    }

    //***************************************************************************//
    public static String eseguiScript(String scriptPath, String workDir) {
        return eseguiScriptWithOutput(scriptPath, workDir);
    }
    //***************************************************************************//

    public static String eseguiScriptWithOutput(String scriptPath, String workDir) {
        String[] cmd = new String[1];
        cmd[0] = scriptPath;
        return eseguiScriptWithOutput(cmd, workDir);
    }

    //***************************************************************************//
    public static String eseguiScriptWithOutput(String[] cmd) {
        return eseguiScriptWithOutput(cmd, null);
    }

    //***************************************************************************//
    public static String eseguiScriptWithOutput(String[] cmd, String workDir) {
        StringBuffer sbResult = new StringBuffer();
        try {
            Process p;
            if (workDir != null) {
                if (cmd.length == 1) {
                    p = Runtime.getRuntime().exec(cmd[0], null, new File(workDir));
                } else {
                    p = Runtime.getRuntime().exec(cmd, null, new File(workDir));
                }
            } else {
                if (cmd.length == 1) {
                    p = Runtime.getRuntime().exec(cmd[0]);
                } else {
                    p = Runtime.getRuntime().exec(cmd);
                }
            }
            MyInputStream lineIn = new MyInputStream(p.getInputStream());

            try {
                byte c[] = new byte[1024];
                int numByteLetti;
                while ((numByteLetti = lineIn.read(c)) > 0) {
                    sbResult.append(new String(c, 0, numByteLetti));
                    System.out.print(new String(c, 0, numByteLetti));
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                //TODO: gestire l'eccezione
            }

            MyInputStream errIn = new MyInputStream(p.getErrorStream());
            try {
                byte c[] = new byte[1024];
                int numByteLetti;
                boolean firstChar = true;
                while ((numByteLetti = errIn.read(c)) > 0) {
                    if (firstChar) {
                        firstChar = false;
                        sbResult.append("<error>:\n");
                    }
                    sbResult.append(new String(c, 0, numByteLetti));
                    System.out.print(new String(c, 0, numByteLetti));
                }
            } catch (Throwable ex) {
                ex.printStackTrace();
                //TODO: gestire l'eccezione
            }
//            OutputStreamWriter sw = new OutputStreamWriter( p.getOutputStream() );
            p.waitFor();
            if (p.exitValue() != 0) {
                System.out.println(sbResult.toString());
                throw new RuntimeException("Problemi durante l'esecuzione dello script: " + sbResult.toString());
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }

        return sbResult.toString();
    }

    //***************************************************************************//
    public static String getSerialeUnita(String driveLetter) {

        String output = SystemUtils.eseguiScriptWithOutput("cmd.exe /C \"vol " + driveLetter + ":\"", null);
        output = StringUtilities.replaceSafety(output, "\r\n", "\n");
        int startSeriale = output.indexOf(": ");
        if (startSeriale < 0) {
            throw new RuntimeException("impossibile ottenere il seriale della periferica");
        }
        startSeriale += 2;
        output = output.substring(startSeriale);

        int endSeriale = output.indexOf("\n");
        if (endSeriale >= 0) {
            output = output.substring(0, endSeriale);
        }

        return output;
    }

    //***************************************************************************//
    public static String getProgramDirPath() {
        String ret = "c:\\Programmi";

        String nomeSistema = System.getProperty("os.name");
        if (nomeSistema.toLowerCase().indexOf("vista") >= 0) {
            ret = "c:\\Program Files";
        }

        return ret;
    }

    //***************************************************************************//
    public static void freeMemory() {
        System.gc();
        System.runFinalization();
        System.gc();
    }

    //***************************************************************************//
    public static void printLog(String message) {
        System.out.println(message);
    }


    //***************************************************************************//
    public static void printCurrentTimeNoLockForThread() {
        long ms = System.currentTimeMillis();
        int intTmp = (int)(ms / 1000);

        int seconds = intTmp % 60;
        intTmp = intTmp / 60;

        int minutes = intTmp % 60;
        intTmp = intTmp / 60;

        int hours = intTmp % 24;

        System.out.println( String.valueOf(hours) + ":" + String.valueOf(minutes) + ":" + String.valueOf(seconds) + "[" + ms + "]");

    }

    //***************************************************************************//
    public static void printCurrentDate() {
        long ms = System.currentTimeMillis();
        System.out.println((new Date(ms)).toString() + "[" + ms + "]");

    }

    //***************************************************************************//
    public static void printElapsedTime(long startTime, long endTime) {
        long secondi = (endTime - startTime) / 1000;
        long millesimi = (endTime - startTime) - (secondi * 1000);
        long minuti = secondi / 60;
        secondi = secondi % 60;


        System.out.println(String.valueOf(minuti) + "m " + secondi + "s " + millesimi + "ms");

    }


    //***************************************************************************//
    public static int getRandomIntegerStartFromZero(int maxRandomNumber) {
        int result = 0;

        if (maxRandomNumber > 0) {
            result = ((int) Math.floor((Math.random() * 100 * (maxRandomNumber + 1)))) % (maxRandomNumber + 1);
        }

        return result;
    }

    //***************************************************************************//
    public static int getRandomIntegerStartFromOne(int maxRandomNumber) {
        int result = 1;
        double dblTmp;

        if (maxRandomNumber > 1) {
            dblTmp = ((int) Math.floor((Math.random() * 100)));
            dblTmp = Math.abs(Math.cos(dblTmp) * 100);
            result = ((int) Math.floor((Math.random() * maxRandomNumber * dblTmp))) % maxRandomNumber;
            result = result + 1;
        }

        return result;
    }

    /************************************************************************************************************/
    public static String getMacAddress() {
        String macAddress = null;
        ArrayList<String> alInterfaces = new ArrayList<String>();

        try {
            Enumeration<NetworkInterface> enumInt = NetworkInterface.getNetworkInterfaces();

            while (enumInt.hasMoreElements()) {
                NetworkInterface netInt = enumInt.nextElement();

                //System.out.println ("Interface: " + netInt.getDisplayName ().trim ()+" loopback:"+netInt.isLoopback()+"---virtual:"+netInt.isVirtual()+"---isUp():"+netInt.isUp()+"---isPointToPoint():"+netInt.isPointToPoint());
              /*byte[] hwaddr = netInt.getHardwareAddress ();

                if (hwaddr != null)
                {
                System.out.print ("    HW Addr: ");

                for (int i = 0; i < hwaddr.length; i++)
                System.out.format (i == 0 ? "%02X" : "-%02X", hwaddr[i]);

                System.out.println ();
                }*/
                if (netInt.isLoopback() || !netInt.isUp()) {
                    continue;
                }
                String interfaceName = netInt.getDisplayName().trim().toLowerCase();
                if (interfaceName.indexOf("virtual") != -1 || interfaceName.indexOf("tap") != -1) {
                    continue;
                }

                alInterfaces.add(netInt.getName());

                /*Enumeration<InetAddress> enumAddr = netInt.getInetAddresses ();

                while (enumAddr.hasMoreElements ())
                {
                InetAddress addr = enumAddr.nextElement ();

                System.out.println ("    IP: " + addr);
                }*/
            }
            Collections.sort(alInterfaces);

            if (alInterfaces.size() > 0) {
                NetworkInterface n = NetworkInterface.getByName(alInterfaces.get(0));
                byte[] hwaddr = n.getHardwareAddress();
                macAddress = "";
                if (hwaddr != null) {
                    for (int i = 0; i < hwaddr.length; i++) {
                        macAddress +=
                                String.format(i == 0 ? "%02X" : "-%02X", hwaddr[i]);
                    }
                }
            } else {
                macAddress = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return macAddress;

    }

    public static String getHDSerialNumber() {

        String result = null;
        try {
            String cmd[] = new String[3];
            cmd[0] = "cmd";
            cmd[1] = "/C";
            cmd[2] = "vol";
            String output = SystemUtils.eseguiScriptWithOutput(cmd);
            int index = output.indexOf(":");
            if (index != -1) {
                output = output.substring(index + 1);
                int index2 = output.indexOf("\r\n");
                result = output.substring(0, index2).trim();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return result;
        /*  try {
        String drive ="C"
        File file = File.createTempFile("realhowto",".vbs");
        file.deleteOnExit();
        FileWriter fw = new java.io.FileWriter(file);

        String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
        +"Set colDrives = objFSO.Drives\n"
        +"Set objDrive = colDrives.item(\"" + drive + "\")\n"
        +"Wscript.Echo objDrive.SerialNumber";  // see note
        fw.write(vbs);
        fw.close();
        Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
        BufferedReader input =
        new BufferedReader
        (new InputStreamReader(p.getInputStream()));
        String line;
        while ((line = input.readLine()) != null) {
        result += line;
        }
        input.close();
        }
        catch(Exception e){
        e.printStackTrace();
        }
        return result.trim();*/

    }



    public static boolean createShortcutForWindows(String pathShortcutExe, String pathFileToLink, String pathLinkToCreate, boolean replaceLink){
      boolean result = false;

      if(!isWindows()){
        System.out.println("ATTEZIONE: Sistema operativo non supportato per la creazione degli shortcut");
        return false;
      }

      RecursiveFileImpl fdShortcutExe = new RecursiveFileImpl(pathShortcutExe);
      RecursiveFileImpl fdFileToLink = new RecursiveFileImpl(pathFileToLink);
      RecursiveFileImpl fdLinkToCreate = new RecursiveFileImpl(pathLinkToCreate);

      if(!fdShortcutExe.exists()){
        System.out.println("ATTEZIONE: Impossibile trovare l'applicazione che crea gli shortcut");
        return false;
      }

      if(!fdFileToLink.exists()){
        System.out.println("ATTEZIONE: Impossibile trovare l'oggetto da linkare");
        return false;
      }


      if(fdLinkToCreate.exists() && !replaceLink){
        return false;
      }


      String []cmd = new String[5];

      cmd[0] = fdShortcutExe.getAbsolutePath();
      cmd[1] = "\"/f:"+ fdLinkToCreate.getAbsolutePath() + "\"";
      cmd[2] = "/a:c";

      if(fdFileToLink.isDirectory()){
        cmd[3] = "\"/w:"+ fdFileToLink.getAbsolutePath() + "\" ";
      } else {
        cmd[3] = "\"/w:"+ fdFileToLink.getParentFile().getAbsolutePath() + "\"";
      }

      cmd[4] = "\"/t:"+ fdFileToLink.getAbsolutePath() + "\"";


      try {
        eseguiScriptWithOutput(cmd);
        result = true;
      } catch (Throwable e) {
        System.out.println("PROBLEMA: impossibile creare lo shortcut per " + pathShortcutExe + "...:" + e.getMessage());
      }


      return result;
    }



    public static String getMenustartShortcutApplicationFolder(String applicationName, boolean useAllUsers){
      String result;

      if(isVistaLike()){
        result = getUserHome() + File.separator + ".." + File.separator + ".." + File.separator + "ProgramData" + File.separator + "Microsoft" + File.separator + "Windows" + File.separator + "Start Menu" + File.separator + "Programs" + File.separator + applicationName;
      } else if(useAllUsers){
        result = getUserHome() + File.separator + ".." + File.separator + "All Users" + File.separator + "Menu Avvio" + File.separator + "Programmi" + File.separator + applicationName;
      } else {
        result = getUserHome() + File.separator + "Menu Avvio" + File.separator + "Programmi" + File.separator + applicationName;
      }

      result = (new File(result)).getAbsolutePath();

      return result;
    }



    public static boolean setAdminisratorPrivilegesForVistaLike(String pathExe){
      boolean result = false;

      if(!isWindows() ){
        System.out.println("ATTEZIONE: Sistema operativo non supportato per la creazione degli shortcut");
        return false;
      }

      RecursiveFileImpl fdExe = new RecursiveFileImpl(pathExe);

      if(!fdExe.exists()){
        System.out.println("ATTEZIONE: Impossibile trovare l'applicazione che crea gli shortcut");
        return false;
      }



      String []cmd = new String[10];

      cmd[0] = "reg.exe";
      cmd[1] = "ADD";
      cmd[2] = "\"HKCU\\Software\\Microsoft\\Windows NT\\CurrentVersion\\AppCompatFlags\\Layers\"";
      cmd[3] = "/v";
      cmd[4] = "\"" + fdExe.getAbsolutePath() +  "\"";
      cmd[5] = "/t";
      cmd[6] = "REG_SZ";
      cmd[7] = "/d";
      cmd[8] = "RUNASADMIN";
      cmd[9] = "/f";


      try {
        eseguiScriptWithOutput(cmd);
        result = true;
      } catch (Throwable e) {
        e.printStackTrace();
      }


      return result;
    }

  public static void sleepSafety(long millis) {
      try {
        Thread.sleep(millis);
      } catch (InterruptedException interruptedException) {
      }
  }

}
