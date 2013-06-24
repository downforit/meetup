package utils;

import java.awt.Component;
import java.io.*;
import java.net.URL;
import java.util.Properties;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

import java.util.Arrays;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import javax.swing.JFileChooser;
import utils.comparators.ComparatorFileNamesLikeWindows;
import utils.comparators.FileDateLastModifiedComparator;

public class FileUtilities {
    public static final String ZIPPED_FILE_EXTENSION = ".piz";


    public static void main(String[] args) {



//        String sourceFilePath = "/902104.xml";
//
//        Document doc = xmlUtilities.fileToXmlDocument(sourceFilePath, false);
//        Document clonedDoc = (Document) doc.cloneNode(true);
//
//
//        xmlUtilities.addNodeTo(doc, "root", "prova","1" );
//        xmlUtilities.addNodeTo(clonedDoc, "root", "prova","2" );
//
//        xmlUtilities.writeXmlFile(doc, sourceFilePath+".1.xml", false);
//        xmlUtilities.writeXmlFile(clonedDoc, sourceFilePath+".2.xml", false);




        writeFilePerCollection(1);
        writeFilePerCollection(2);
        writeFilePerCollection(3);
        writeFilePerCollection(4);
        writeFilePerCollection(5);
        writeFilePerCollection(6);
    }




    public static void writeFilePerCollection(int numFotoPerBimbo){
        int NUM_BIMBI = 100;
        String resultCop="",result = "";
        int counter=0;
        for(int i=1;i<=NUM_BIMBI;i++){
            resultCop=resultCop+(++counter)+",";
            resultCop=resultCop+(++counter)+",";
            for(int j=0;j<numFotoPerBimbo;j++){
                result=result+(++counter)+",";
            }

        }

        stringToFile(result, "/"+numFotoPerBimbo+"_.txt");
        stringToFile(resultCop, "/"+numFotoPerBimbo+"_cop.txt");

    }



    //*********************************************/
    public static void cifraFile(String pathDecifrato, String pathCifrato) {
        byte[] baFileDecifrato = FileUtilities.getByteArrayFromFile(pathDecifrato);

        byte[] baFileCifrato = FileUtilities.zipFileInBinaryMode(baFileDecifrato);

        FileUtilities.saveFileFromBuffer(baFileCifrato, pathCifrato);
    }

    /****************************************************************************************/
    public static byte[] cifraByteArray(byte []buff){
      buff = FileUtilities.zipFileInBinaryMode(buff);

      return buff;
    }



    /***************************************************************************************/
    public static Properties fileIniToProperties(String inputFilePath) {

        String fileContent = fileToStringRaw(inputFilePath);

        fileContent = fileContent.replace('\\', '/');

        ByteArrayInputStream bais = new ByteArrayInputStream(fileContent.getBytes());

        Properties prop = new Properties();
        try {
            prop.load(bais);
        } catch (IOException ex) {
            throw new RuntimeException("Errore nella lettura del file " + inputFilePath, ex);
        }
        return prop;

    }

    //restituisce l'istanza del file con nome "searchedFileName" nella cartella "folder"
    //null se nn esiste
    public static File getFileInFolder(File folder, String searchedFileName) {
        File searchedFile = null;
        File[] folderContent = folder.listFiles();
        if (folderContent != null) {
            for (int i = 0; i < folderContent.length; i++) {
                if (folderContent[i].getName().equals(searchedFileName)) {
                    searchedFile = folderContent[i];
                    break;
                }
            }
        }

        return searchedFile;
    }

    /*************************************************************************************/
    //cancella una directory, anche non vuota
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory() && dir.exists()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so delete it
        return dir.delete();
    }

    /*************************************************************************************/
    public static void moveDirectory(File sourceDir, File destinationDir) {
        int i;
        File[] list = sourceDir.listFiles();
        for (i = 0; i < list.length; i++) {
            File dest = new File(destinationDir, list[i].getName());
            if (list[i].isDirectory()) {
                dest.mkdir();
                moveDirectory(list[i], dest);
            } else {
                FileUtilities.copyFile(list[i].getAbsolutePath(), dest);
                list[i].delete();
            }
        }
        /*if(list.length == i) {
        File sourceDirRenamed = new File(sourceDir.getParent(), sourceDir.getName() + "_moved");
        sourceDir.renameTo(sourceDirRenamed);
        }*/
    }

    /*************************************************************************************/
    public static void copyDirectory(File sourceDir, File destinationDir) {
        if (sourceDir != null && destinationDir != null) {
            File dest = new File(destinationDir.getAbsolutePath() + "\\" + sourceDir.getName());
            dest.mkdir();
            int i;
            File[] list = sourceDir.listFiles();
            for (i = 0; i < list.length; i++) {
                File toCopy = new File(dest, list[i].getName());
                if (list[i].isDirectory()) {
                    toCopy.mkdir();
                    copyDirectory(list[i], toCopy);
                } else {
                    FileUtilities.copyFile(list[i].getAbsolutePath(), toCopy);
                }
            }
        } else {
            System.out.println("source o destination sono nulli...");
        }
    }

    /****************************************************************************************/
    public static void copyFile(String srcFilePath, String dstFilePath) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(srcFilePath);

            streamtoFile(in, dstFilePath);
            /*
            out = new FileOutputStream(dstFilePath);
            byte buffer[] = new byte[5 * 1024];
            int n;
            while( (n = in.read(buffer)) > -1)
            out.write(buffer, 0, n);
            out.close();
             */
            in.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            throw new RuntimeException("File non trovato nella copia del file " + srcFilePath + " in " + dstFilePath, ex);
        } catch (IOException ex) {
            throw new RuntimeException("I/O exception nella copia del file " + srcFilePath, ex);
        }
    }

    //****************************************
    public static int numberOfFileInDir(File dir) {
        int num = 0;
        if (dir == null || !dir.exists()) {
            //niente
        } else {
            File[] list = dir.listFiles();
            for (int i = 0; i < list.length; i++) {
                //File dest = new File(dir.getAbsolutePath(), list[i].getName());
                if (list[i].isDirectory()) {
                    num = num + numberOfFileInDir(list[i]);
                } else {
                    num++;
                }
            }
        }
        return num;
    }
    //****************************************

    public static int numberOfDirsInDir(File dir) {
        int num = 0;
        if (dir == null || !dir.exists()) {
            //niente
        } else {
            File[] list = dir.listFiles();
            for (int i = 0; i < list.length; i++) {
                if (list[i].isDirectory()) {
                    num = num + 1 + numberOfDirsInDir(list[i]);
                }
            }
        }
        return num;
    }
    //****************************************

    public static int numberOfDirsAndFilesInDir(File dir) {
        int num = 0;
        if (dir == null || !dir.exists()) {
        } else {
            File[] list = dir.listFiles();
            for (int i = 0; i < list.length; i++) {
                //File dest = new File(dir.getAbsolutePath(), list[i].getName());
                if (list[i].isDirectory()) {
                    num = num + 1 + numberOfDirsAndFilesInDir(list[i]);
                } else {
                    num++;
                }
            }
        }
        return num;

    }

    /****************************************************************************************/
    public static void streamtoFile(FileInputStream in, String dstFilePath) throws FileNotFoundException, IOException {
        FileOutputStream out = null;
        out = new FileOutputStream(dstFilePath);
        byte buffer[] = new byte[5 * 1024];
        int n;
        while ((n = in.read(buffer)) > -1) {
            out.write(buffer, 0, n);
        }
        out.close();

    }

    /****************************************************************************************/
    public static void copyFile(String srcFilePath, File dstFile) {
        copyFile(srcFilePath, dstFile.getAbsolutePath());
    }

    /****************************************************************************************/
    public static void copyFile(File srcFile, File dstFile) {
        copyFile(srcFile.getAbsolutePath(), dstFile.getAbsolutePath());
    }

    /****************************************************************************************/
    public static String fileToStringRaw(String fileName) {
        String result = "";
        try {
            byte byteArray[] = getByteArrayFromFile(fileName);
            result = new String(byteArray);
        } catch (Throwable ex) {
            //nn deve fare niente
        }


        return result;
    }

    /****************************************************************************************/
    public static byte[] getByteArrayFromFile(String filePath) {
        File file = new File(filePath);

        FileInputStream in = null;
        byte fileBuffer[] = new byte[(int) file.length()];
        try {
            in = new FileInputStream(filePath);
            in.read(fileBuffer);
            in.close();
        } catch (FileNotFoundException ex) {
            //ex.printStackTrace();
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex1) {
            }
            throw new RuntimeException("File non trovato nel passaggio dal file " + filePath + " in buffer di byte", ex);
        } catch (IOException ex) {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex1) {
            }
            throw new RuntimeException("I/O exception nel passaggio del file " + filePath + " in buffer", ex);
        }

        return fileBuffer;
    }

    /****************************************************************************************/
    public static void saveFileFromBuffer(byte[] fileBuffer, String dstFilePath) {

        File dstFile = new File(dstFilePath);
        if (dstFile.getParentFile() != null) {
            dstFile.getParentFile().mkdirs();
        }
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(dstFilePath);
            out.write(fileBuffer);
            out.close();
        } catch (Throwable e){
          try{
            out.close();
          }catch(Throwable e1){
            //non faccio niente;
          }
          //questa situazione serve nel caso in cui il buffer sia di dimensioni molto grandi (80/100 MB) e una sola write potrebbe andare in errore
          saveFileFromBufferV2(fileBuffer, dstFilePath);
        }
    }

    /****************************************************************************************/
    //questa situazione serve nel caso in cui il buffer sia di dimensioni molto grandi (80/100 MB) e una sola write potrebbe andare in errore
    public static void saveFileFromBufferV2(byte[] fileBuffer, String dstFilePath) {
        int fileLenght = fileBuffer.length;
        int currentPosition = 0;
        int amountOfBytesPerTime = 1024 * 1024;
        int currentSizeToWrite;


        File dstFile = new File(dstFilePath);
        if (dstFile.getParentFile() != null) {
            dstFile.getParentFile().mkdirs();
        }
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(dstFilePath);
            while(currentPosition < fileLenght){
              if( (fileLenght - currentPosition) < amountOfBytesPerTime ){
                currentSizeToWrite = fileLenght - currentPosition;
              } else {
                currentSizeToWrite = amountOfBytesPerTime;
              }
              out.write(fileBuffer, currentPosition, currentSizeToWrite );
              currentPosition += currentSizeToWrite;
            }
            out.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File non trovato nella copia dal buffer al file " + dstFilePath, ex);
        } catch (IOException ex) {
            throw new RuntimeException("I/O exception nella copia del buffer al " + dstFilePath, ex);
        }
    }

    /****************************************************************************************/
    public static void saveFileFromBuffer(byte[] fileBufferHeader, byte[] fileBufferBody, String dstFilePath) {

        FileOutputStream out = null;

        try {
            out = new FileOutputStream(dstFilePath);
            out.write(fileBufferHeader);
            out.write(fileBufferBody);
            out.close();
        } catch (FileNotFoundException ex) {
            throw new RuntimeException("File non trovato nella copia dal buffer al file " + dstFilePath, ex);
        } catch (IOException ex) {
            throw new RuntimeException("I/O exception nella copia del buffer al " + dstFilePath, ex);
        }
    }

    /****************************************************************************************/
    public static void getBufferedImage(byte[] fileBuffer) {
        //BufferedImage bufImage = new BufferedImage();
    }

    /****************************************************************************************/
    public static String fileToString(String fileName) {
        return fileToString(fileName, false);
    }

    /****************************************************************************************/
    public static String fileToString(String fileName, boolean holdLineFeed, boolean isDosFormat) {
        //TODO: far in modo ke restituisca null e non ""... controllare altri utilizzi di tale metodo
        String result = "";

        File f = new File(fileName);
        try {
            if (f != null && f.exists()) {
                FileInputStream fis = new FileInputStream(f);
                result = streamToString(fis, result, holdLineFeed, isDosFormat);
                fis.close();
            }
            if (fileName.endsWith(".gns")) {
                //throw new IOException("");
            }
        } catch (IOException ex) {
            throw new RuntimeException("Errore nella trasformaizone del file in stringa", ex);
        }
        return result;
    }

    /****************************************************************************************/
    public static String fileToString(String fileName, boolean holdLineFeed) {
        return fileToString(fileName, holdLineFeed, true);
    }

    /****************************************************************************************/
    public static String streamToString(final InputStream fis, String result) {
        return streamToString(fis, result, false, false);
    }

    /****************************************************************************************/
    public static String streamToString(final InputStream fis, String result, boolean holdLineFeed, boolean isDosFormat) {
        try {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String linea = br.readLine();
            while (linea != null) {
                result = result + linea;
                linea = br.readLine();
                if (holdLineFeed) {
                    if (isDosFormat) {
                        result = result + "\r";
                    }
                    result = result + "\n";
                }
            }
            br.close();
            isr.close();
        } catch (IOException ex) {
            throw new RuntimeException("Errore nella trasformaizone del file in stringa", ex);
        }
        return result;
    }

    /****************************************************************************************/
    public static void stringToFile(String strContent, String filePath) {
        stringBufferToFile(new StringBuffer(strContent), filePath);
    }

    /****************************************************************************************/
    public static void stringBufferToFile(StringBuffer sbContent, String filePath) {
        String fileName = filePath;
        try {
              File f = new File(filePath);
              fileName = f.getName();
              f.createNewFile();
              FileOutputStream fos = new FileOutputStream(f);
              PrintStream ps = new PrintStream(fos);
              ps.print(sbContent);
              ps.close();
              fos.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + fileName + " non trovato", e);
        } catch (IOException e) {
            throw new RuntimeException("Errore I/O durante la scrittura del file " + fileName, e);
        }

    }

    /****************************************************************************************/
    public static File creaDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            if (dir.mkdirs() == false) {
                throw new RuntimeException("Errore nella crezione della directory " + dirName);
            }
        }
        return dir;
    }

    //****************************************
    public static boolean isHardDrive(String unitLetter) {
        boolean retValue = false;

        if (System.getProperties().getProperty("os.name").toLowerCase().startsWith("win")) {
            try {
                unitLetter = unitLetter.substring(0, 1);
                Process ej = null;
                ej = Runtime.getRuntime().exec("fsutil fsinfo drivetype " + unitLetter + ":");
                int out = ej.waitFor();
                InputStreamReader isr = new InputStreamReader(ej.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                String tipoUnita = br.readLine();
                if (tipoUnita.endsWith("fissa")) {
                    retValue = true;
                }

                //TODO: gestire meglio le eccezioni, per ora se va in errore ritorna sempre false
         /*
                if(out != 0) {
                throw new RuntimeException("errore nella verifica delle unita' " + out);
                }
                 */
            } catch (IOException ex) {
                //      throw new RuntimeException("errore IO nella verifica delle unita'.", ex);
            } catch (InterruptedException ex) {
                //      throw new RuntimeException("errore nella waitFor durante la verifica delle unita'.", ex);
            }
        } else {
            if (unitLetter.equals("/")) {
                retValue = true;
            }
        }

        return retValue;
    }

    /**
     *
     * @param dir File
     * @param fileFilter FileFilter
     */
    public static File[] myListFiles(File dir, FileFilter fileFilter) {
        File[] dirContents = dir.listFiles(fileFilter);
        /*
        Arrays.sort(dirContents, new Comparator() {
        public int compare(Object f1, Object f2) {
        String s1 = ((File) f1).getName();
        String s2 = ((File) f2).getName();
        return s1.toLowerCase().compareTo(s2.toLowerCase());
        }
        });*/
        return dirContents;
    }

    /**
     * get the file system icon for a specific file
     */
    public static Icon getSystemIcon(File file) {
        FileSystemView view = FileSystemView.getFileSystemView();
        Icon icon = view.getSystemIcon(file);
        return icon;
    }

    /*****************************************************/
    public static File getFileFromUrl(Object myThis, String relativePath) {
        URL url = myThis.getClass().getClassLoader().getResource(relativePath);
        File searchedFile = new File(url.getFile());
        return searchedFile;
    }

    /*****************************************************/
    public static ImageIcon getIconFromJar(Object myThis, String relativePath) {
        try {
            URL url = myThis.getClass().getClassLoader().getResource(relativePath);
            //System.out.println("relativePath="+relativePath+"___url ="+url.getFile());
            return new ImageIcon(url);
        } catch (Exception ex) {
            System.out.println(relativePath + " non trovato");
            return null;
        }
    }

    /*****************************************************/
    public static void ordinaVettoreFilePerNumerazione(File[] listaFiles) {
        int[] idFiles = new int[listaFiles.length];

        String nome;
        for (int i = 0; i < listaFiles.length; i++) {
            nome = listaFiles[i].getName();
            nome = nome.substring(0, nome.lastIndexOf("."));

            idFiles[i] = StringUtilities.exstractNumberFromString(nome);
            if (idFiles[i] < 0) {
                idFiles[i] = 99999;
            }
        }//for


        File fdTmp = null;
        int idTmp = -1;
        boolean continueSort = true;

        while (continueSort) {
            continueSort = false;
            for (int i = 1; i < idFiles.length; i++) {
                if (idFiles[i - 1] > idFiles[i]) {
                    idTmp = idFiles[i - 1];
                    idFiles[i - 1] = idFiles[i];
                    idFiles[i] = idTmp;


                    fdTmp = listaFiles[i - 1];
                    listaFiles[i - 1] = listaFiles[i];
                    listaFiles[i] = fdTmp;

                    continueSort = true;
                }
            }//for
        }//while
    }

    /*****************************************************/
    public static void ordinaVettoreFileStileWindows(File[] listaFiles) {
        //ordinaVettoreFile(listaFiles);
        //Arrays.sort(listaFiles, Collator.getInstance());
        //Arrays.sort(listaFiles, new ComparatorFilesStileWindows());
        //Arrays.sort(listaFiles, new ComparatorCollator());
        Arrays.sort(listaFiles, new ComparatorFileNamesLikeWindows());

    }

    /*****************************************************/
    public static void ordinaVettoreFileStileWindows(Object[] listaFiles) {
        //ordinaVettoreFile(listaFiles);
        //Arrays.sort(listaFiles, Collator.getInstance());
        //Arrays.sort(listaFiles, new ComparatorFilesStileWindows());
        //Arrays.sort(listaFiles, new ComparatorCollator());
        Arrays.sort(listaFiles, new ComparatorFileNamesLikeWindows());

    }

    /*****************************************************/
    public static void ordinaVettoreFile(File[] listaFiles) {
        File fdTmp = null;

        boolean continueSort = true;

        while (continueSort) {
            continueSort = false;
            for (int i = 1; i < listaFiles.length; i++) {
                if (listaFiles[i - 1].getName().compareTo(listaFiles[i].getName()) > 0) {
                    fdTmp = listaFiles[i - 1];
                    listaFiles[i - 1] = listaFiles[i];
                    listaFiles[i] = fdTmp;
                    continueSort = true;
                }
            }//for
        }//while
    }


    /*****************************************************/
    public static void ordinaListaFilePerDataUltimaModifica(File[] listaFiles) {
        Arrays.sort(listaFiles, new FileDateLastModifiedComparator());
    }

    public static File showDirChooser( String currentPath, Component parentComponent) {
        return showDirChooser("", currentPath, parentComponent);
    }
    /*****************************************************/
    public static File showDirChooser(String titoloFinestra, String currentPath, Component parentComponent) {
        File ret = null;
        JFileChooser chooser = new JFileChooser();
        //Non do la possibilita'di poter selezionare ogni tipo di file
        //chooser.setAcceptAllFileFilterUsed(false);
        if (currentPath != null && currentPath.length() > 0) {
            chooser.setCurrentDirectory(new File(currentPath));
        }

        if(titoloFinestra != null){
          chooser.setDialogTitle(titoloFinestra);
        }

        chooser.setAlignmentX(JFileChooser.CENTER_ALIGNMENT);
        chooser.setAlignmentY(JFileChooser.CENTER_ALIGNMENT);
        chooser.setApproveButtonText("OK");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setMultiSelectionEnabled(false);
//        chooser.setDialogTitle("Seleziona la directory del template da caricare");
        chooser.setApproveButtonMnemonic(1);
        chooser.setDragEnabled(true);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);

        // Show the dialog; wait until dialog is closed
        int result = chooser.showOpenDialog(parentComponent);
        // Determine which button was clicked to close the dialog
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                // Approve (Open or Save) was clicked
                ret = chooser.getSelectedFile();
                break;
            case JFileChooser.CANCEL_OPTION:
                // Cancel or the close-dialog icon was clicked
                ret = null;
                break;
            case JFileChooser.ERROR_OPTION:
                // The selection process did not complete successfully
                ret = null;
                break;
        }       //chooser.is
        return ret;
    }

    /*****************************************************/
    public static File showJpgChooser(String currentPath, String title, Component parentComponent) {
        FileChooserFilter jpgff = new FileChooserFilter(new String[]{"jpg", "jpeg"}, "Tutte le immagini jpg");

        return showFileChooser(title, jpgff, currentPath != null ? new File(currentPath) : null, JFileChooser.FILES_ONLY, true);
    }

    /*****************************************************/
    public static File showJpgChooser(String currentPath, Component parentComponent) {
        FileChooserFilter jpgff = new FileChooserFilter(new String[]{"jpg", "jpeg"}, "Tutte le immagini jpg");

        return showFileChooser("Selezionare un file .jpg", jpgff, new File(currentPath), JFileChooser.FILES_ONLY, true);
    }

    /*****************************************************/
    public static File showXmlChooser(String currentPath, Component parentComponent) {
        FileChooserFilter xmlff = new FileChooserFilter(new String[]{"xml"}, "File xml");

        return showFileChooser("Selezionare un file .xml", xmlff, new File(currentPath), JFileChooser.FILES_ONLY, true);
    }

    /*****************************************************/
    public static File showJpgTifChooser(String currentPath, Component parentComponent) {
        FileChooserFilter jpgff = new FileChooserFilter(new String[]{"jpg", "jpeg", "tif", "tiff"}, "Tutte le immagini jpg e tif");

        return showFileChooser("Selezionare un file .jpg,.tif", jpgff, new File(currentPath), JFileChooser.FILES_ONLY, true);
    }
    /*****************************************************/
    public static File showPdfChooser(String currentPath, Component parentComponent) {
        FileChooserFilter jpgff = new FileChooserFilter(new String[]{"pdf"}, "Tutti i documenti PDF");

        return showFileChooser("Selezionare un file .jpg come intestazione", jpgff, new File(currentPath), JFileChooser.FILES_ONLY, true);
    }

    //***************************************************************************//

    public static File showFileChooser(String titolo, FileChooserFilter fileFilrer, File curDir, int selectionMode, boolean withPreview) {
        File ret = null;
        JFileChooser chooser = new JFileChooser();
        //Non do la possibilita'di poter selezionare ogni tipo di file
        //chooser.setAcceptAllFileFilterUsed(false);
        chooser.setAlignmentX(JFileChooser.CENTER_ALIGNMENT);
        chooser.setAlignmentY(JFileChooser.CENTER_ALIGNMENT);

        if (withPreview) {
            FilePreviewer previewer = new FilePreviewer(chooser);
            chooser.setAccessory(previewer);
        }
        chooser.setApproveButtonText("OK");

        /*
        if (showFile) {
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        } else {
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        }
         */

        chooser.setFileSelectionMode(selectionMode);

        chooser.setFileFilter(fileFilrer);
        chooser.setMultiSelectionEnabled(false);
        if (!titolo.equals(null)) {
            chooser.setDialogTitle(titolo);
        }

        if (curDir != null) {
            chooser.setCurrentDirectory(curDir);
        }

        chooser.setApproveButtonMnemonic(1);
        chooser.setDragEnabled(true);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);

        // Show the dialog; wait until dialog is closed
        int result = chooser.showOpenDialog(null);
        // Determine which button was clicked to close the dialog
        switch (result) {
            case JFileChooser.APPROVE_OPTION:
                // Approve (Open or Save) was clicked
                ret = chooser.getSelectedFile();
                break;
            case JFileChooser.CANCEL_OPTION:
                // Cancel or the close-dialog icon was clicked
                ret = null;
                break;
            case JFileChooser.ERROR_OPTION:
                // The selection process did not complete successfully
                ret = null;
                break;
        }       //chooser.is
        return ret;
    }

    //***************************************************************************//
    public static String rendiPathCompatibileLinux(String stringa) {
        if (stringa != null) {
            //x sostituire un singolo backslash devo usarne 4
            //vedi http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4783892
            stringa = stringa.replaceAll("\\\\", "/");
        }

        return stringa;
    }

    //***************************************************************************//
    public static boolean areEqualsAbsolutePaths(String path1, String path2) {
        boolean result = false;

        path1 = FileUtilities.rendiPathCompatibileLinux(path1);
        path2 = FileUtilities.rendiPathCompatibileLinux(path2);

        if (path1.equals(path2)) {
            result = true;
        }

        return result;
    }

    //***************************************************************************//
    public static String changeNameExtensionFromTiffToJpg(String fileName) {
        String strTmp = fileName.toLowerCase();
        if (strTmp.substring(strTmp.length() - 4).toLowerCase().endsWith(".tif")) {
            strTmp = strTmp.substring(0, strTmp.length() - 4) + ".jpg";
        } else if (strTmp.substring(strTmp.length() - 5).toLowerCase().endsWith(".tiff")) {
            strTmp = strTmp.substring(0, strTmp.length() - 5) + ".jpg";
        }
        return strTmp;
    }


    //***************************************************************************//
    public static boolean isTifFile(String fileName){
      boolean result = false;

      fileName = fileName.toLowerCase();
      if(fileName.endsWith(".tif") || fileName.endsWith(".tiff")){
        result = true;
      }

      return result;
    }

    //***************************************************************************//
    public static boolean isFileWithPreview(String fileName){
      boolean result = false;

      fileName = fileName.toLowerCase();
      //if(fileName.endsWith(".ai") || fileName.endsWith(".eps") || fileName.endsWith(".pdf") || fileName.endsWith(".cdr") ){
      //  result = true;
      //}

      if(fileName.endsWith(".tif") || fileName.endsWith(".tiff") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") ){
        result = true;
      }

      return result;

    }
    //***************************************************************************//
    public static byte[] zipFileInBinaryMode(String filePath) {
      byte[] input = FileUtilities.getByteArrayFromFile(filePath);

      return zipFileInBinaryMode(input);
    }

    //***************************************************************************//
    public static byte[] zipFileInBinaryMode(byte[] input) {
        // Compress the bytes
        byte[] buffer = new byte[100 * 1024];
        ByteArrayOutputStream baFileZippato = new ByteArrayOutputStream();

        Deflater compresser = new Deflater();
        compresser.setLevel(Deflater.BEST_SPEED);
        compresser.setInput(input);
        compresser.finish();//dice al compressore che non ci sono altri bytes
        int compressedDataLength;
        while(compresser.finished() == false){//significa che ancora non ha compresso tutti i bytes che gli sono stati consegnati
          compressedDataLength = compresser.deflate(buffer);
          if(compressedDataLength > 0){
            baFileZippato.write(buffer, 0, compressedDataLength);
          }
        }
        compresser.end();


        byte[] output = baFileZippato.toByteArray();

        return output;
    }

    //***************************************************************************//
    public static byte[] unzipBinaryFile(String filePath) {
      byte[] input = FileUtilities.getByteArrayFromFile(filePath);

      return unzipBinaryFile(input);
    }

    //***************************************************************************//
    public static byte[] unzipBinaryFile(byte[] fileCompressed) {
        byte[] buffer = new byte[100 * 1024];

        try{

          ByteArrayOutputStream baFileUnzippato = new ByteArrayOutputStream();

          // Decompress the bytes
          Inflater decompresser = new Inflater();
          decompresser.setInput(fileCompressed);
          int resultLength;
          while(decompresser.finished() == false){//significa che ancora non ha decompresso tutti i bytes che gli sono stati consegnati
            resultLength = decompresser.inflate(buffer);
            if(resultLength > 0){
              baFileUnzippato.write(buffer, 0, resultLength);
            }
          }
          decompresser.end();

          byte[] fileUncompressed = baFileUnzippato.toByteArray();


          return fileUncompressed;
        }catch(Throwable ex){
          throw new RuntimeException("impossibile decomprimere il file", ex);
        }
    }

}
