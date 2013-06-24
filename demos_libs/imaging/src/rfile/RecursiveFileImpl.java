package rfile;

//import mlpks.utils.imaging.*;
//import mlpks.gun.geniusutils.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import utils.FileUtilities;
import utils.SystemUtils;

public class RecursiveFileImpl extends File implements RecursiveFile {

    public static boolean stop = false;
    private static RecursiveFileOutput myOutput = null;
//
//    private static JTextArea txt_outputArea=null;
//    private static JProgressBar prg_copyFile=null;
//    private static JLabel lbl_copyFile=null;
//
    private Object customInfos = null;
    private int tot = 0;
    private int currFileNum = 1;
    //private String pathOfThis;
    private boolean dischargeExe = false;
    private RecursiveFilePersonalCopier personalCopier = null;
    private static boolean fileCounter = false;

    //******************************************************************************/
    public RecursiveFileImpl(String path) {
        super(path);
        //this.pathOfThis=path;
    }

    //******************************************************************************/
    public RecursiveFileImpl(String dirPath, String name) {
        super(dirPath, name);
        //this.pathOfThis=dirPath+name;
    }

    //******************************************************************************/
    public RecursiveFileImpl(String path, RecursiveFileOutput myOutput) {
        super(path);
        //this.pathOfThis=path;
        RecursiveFileImpl.myOutput = myOutput;
    }

    //******************************************************************************/
    public RecursiveFileImpl(String dirPath, String name, RecursiveFileOutput myOutput) {
        super(dirPath, name);
        //this.pathOfThis=dirPath+name;
        RecursiveFileImpl.myOutput = myOutput;
    }

    //******************************************************************************/
    public RecursiveFileImpl[] listRecursiveFiles() {
        return this.listRecursiveFiles(null, false);
    }

    //******************************************************************************/
    public RecursiveFileImpl[] listRecursiveFiles(String endsWith, boolean discardsDirs) {
        return listRecursiveFiles(endsWith, discardsDirs, true);
    }

    //******************************************************************************/
    public RecursiveFileImpl[] listRecursiveFilesImages(boolean listJPG, boolean listTIF, boolean listPNG) {
        String[] endsWithList = null;

        int numElementi = 0;
        if (listJPG) {
            numElementi += 2;
        }
        if (listTIF) {
            numElementi += 2;
        }
        if (listPNG) {
            numElementi += 1;
        }

        if (numElementi > 0) {
            endsWithList = new String[numElementi];
            int currentElement = 0;

            if (listJPG) {
                endsWithList[currentElement] = ".jpg";
                currentElement++;
                endsWithList[currentElement] = ".jpeg";
                currentElement++;
            }
            if (listTIF) {
                endsWithList[currentElement] = ".tif";
                currentElement++;
                endsWithList[currentElement] = ".tiff";
                currentElement++;
            }
            if (listPNG) {
                endsWithList[currentElement] = ".png";
                currentElement++;
            }
        }



        return listRecursiveFiles(endsWithList, true, true);
    }

    //******************************************************************************/
    public RecursiveFileImpl[] listRecursiveFiles(String endsWith, boolean discardsDirs, boolean sort) {
        String[] endsWithList = null;
        if (endsWith != null) {
            if (endsWith.toLowerCase().equals(".jpg")) {
                endsWithList = new String[]{endsWith, ".jpeg"};
            } else {
                endsWithList = new String[]{endsWith};
            }
        }

        return listRecursiveFiles(endsWithList, discardsDirs, sort);
    }

    //******************************************************************************/
    public RecursiveFileImpl[] listRecursiveFiles(String[] endsWithList, boolean discardsDirs, boolean sort) {
        File[] files = this.listFiles();
        int totFilesRemoved = 0;

        if (files == null) {
            return null;
        }

        if ((endsWithList != null && endsWithList.length > 0)) {
            for (int i = 0; i < endsWithList.length; i++) {
                endsWithList[i] = endsWithList[i].toLowerCase();
            }
        }


        if ((endsWithList != null && endsWithList.length > 0) || discardsDirs) {
            String strName;
            Boolean conservateFile;
            for (int i = 0; i < files.length; i++) {
                conservateFile = true;
                strName = files[i].getName().toLowerCase();


                if (endsWithList != null && endsWithList.length > 0) {
                    conservateFile = false;
                    for (int j = 0; j < endsWithList.length; j++) {
                        if (strName.endsWith(endsWithList[j])) {
                            conservateFile = true;
                        }
                    } //for j
                }

                if (discardsDirs && files[i].isDirectory()) {
                    conservateFile = false;
                }

                if (!conservateFile) {
                    files[i] = null;
                    totFilesRemoved++;
                }
            }//for i
        }

        RecursiveFileImpl[] result = new RecursiveFileImpl[files.length - totFilesRemoved];

        int z = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i] != null) {
                result[z] = new RecursiveFileImpl(files[i].getAbsolutePath());
                z++;
            }
        }


        if (sort) {
            FileUtilities.ordinaVettoreFile(result);
        }

        return result;
    }

    //******************************************************************************/
  /*
    public RecursiveFileVital[] listRecursiveFiles(String endsWith, boolean discardsDirs, boolean sort) {
    File [] files = this.listFiles();
    int totFilesRemoved = 0;

    if(files == null){
    return null;
    }

    if(endsWith != null || discardsDirs){
    if(endsWith != null){
    endsWith = endsWith.toLowerCase();
    }

    for(int i = 0; i < files.length; i++){
    if(endsWith != null && !(files[i].getName().toLowerCase().endsWith(endsWith)) ){
    files[i] = null;
    totFilesRemoved++;
    } else if(discardsDirs && files[i].isDirectory()){
    files[i] = null;
    totFilesRemoved++;
    }
    }//for
    }
    
    RecursiveFileVital []result = new RecursiveFileVital[files.length - totFilesRemoved];
    
    int z = 0;
    for(int i = 0; i < files.length; i++){
    if(files[i] != null){
    result[z] = new RecursiveFileVital(files[i].getAbsolutePath());
    z++;
    }
    }


    if(sort){
    FileUtilities.ordinaVettoreFile(result);
    }

    return result;
    }
     */
    //******************************************************************************/
    public RecursiveFileImpl[] listRecursiveDirectoryes() {
        File[] files = this.listFiles();

        int numDirs = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                numDirs++;
            }
        }//for


        RecursiveFileImpl[] result = new RecursiveFileImpl[numDirs];

        int nextResult = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                result[nextResult] = new RecursiveFileImpl(files[i].getAbsolutePath());
                nextResult++;
            }
        }//

        return result;
    }

    //******************************************************************************/
    public ArrayList<RecursiveFileImpl> listRecursiveFileRecursively(boolean discardDirs) {
        return listRecursiveFileRecursively(null, discardDirs);
    }
    //******************************************************************************/

    public ArrayList<RecursiveFileImpl> listRecursiveFileRecursively(String[] endsWithList, boolean discardDirs) {
        ArrayList<RecursiveFileImpl> list = new ArrayList<RecursiveFileImpl>();


        if ((endsWithList != null && endsWithList.length > 0)) {
            for (int i = 0; i < endsWithList.length; i++) {
                endsWithList[i] = endsWithList[i].toLowerCase();
            }
        }


        if (this.isDirectory()) {
            addRecursiveFilesInDir(list, this, endsWithList, discardDirs);
        }


        return list;

    }

    //******************************************************************************/
    private void addRecursiveFilesInDir(ArrayList<RecursiveFileImpl> list, RecursiveFileImpl dir, String[] endsWithList, boolean discardDirs) {
        boolean filenameCompatibleWithEndsWithList;
        String strNameTmp;
        if (dir.isDirectory()) {

            RecursiveFileImpl[] recFiles = dir.listRecursiveFiles();
            for (int i = 0; i < recFiles.length; i++) {

                filenameCompatibleWithEndsWithList = false;
                if (endsWithList == null) {
                    filenameCompatibleWithEndsWithList = true;
                } else {
                    strNameTmp = recFiles[i].getName().toLowerCase();
                    for (int j = 0; j < endsWithList.length; j++) {
                        if (strNameTmp.endsWith(endsWithList[j])) {
                            filenameCompatibleWithEndsWithList = true;
                            break;
                        }
                    }// for j
                }

                if (recFiles[i].isDirectory()) {
                    if (!discardDirs) {
                        if (filenameCompatibleWithEndsWithList) {
                            list.add(recFiles[i]);
                        }
                    }
                    dir.addRecursiveFilesInDir(list, recFiles[i], endsWithList, discardDirs);
                } else {
                    if (filenameCompatibleWithEndsWithList) {
                        list.add(recFiles[i]);
                    }
                }
            }//for i

        }
    }

    //******************************************************************************/
    public void copyContentTo(String pathDestination) {
        File destination = new File(pathDestination);
        copyContentTo(destination);
    }

    //******************************************************************************/
    public void copyContentTo(File destination) {
        currFileNum = 1;
        tot = 0;
        //File destination = new File(pathDestination);
        //File thisFile = new File(pathOfThis);
        if (this.isDirectory() && fileCounter) {
            tot = FileUtilities.numberOfFileInDir(this);
        }
        if (this.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }
            if (!destination.isDirectory()) {
                throw new RecursiveFileException("La destinazione '" + destination.getName() + "' non e' una Directory!");
            }
            copyDirectory(this, destination);
        } else {
            if (destination.isDirectory()) {
                destination = new File(destination, this.getName());
            }
            copyFile(this, destination);
        }
    }

    //******************************************************************************/
    public void copyContentToByOperatingSystem(File destination) {
        String[] cmd = null;
        if (SystemUtils.isUnix() || SystemUtils.isMac()) {
            cmd = new String[4];
            cmd[0] = "cp";
            cmd[1] = "-rfp";
            cmd[2] = this.getAbsolutePath() + RecursiveFileImpl.separator + "*";
            cmd[3] = destination.getAbsolutePath();

        } else {
            cmd = new String[6];
            cmd[0] = "xcopy";
            cmd[1] = this.getAbsolutePath() + RecursiveFileImpl.separator + "*";
            cmd[2] = destination.getAbsolutePath();
            cmd[3] = "/E";
            cmd[4] = "/I";
            cmd[5] = "/Q";

        }
        SystemUtils.eseguiScriptWithOutput(cmd);
    }

    /**
     * @param pathDestination (x windows, quando si deve copiare un singolo file, path destination
     * deve essere il path della directory di destinazione senza il nome del file.
     */
    public void copyThisToByOperatingSystem(String pathDestination) {
        String[] cmd = null;
        if (SystemUtils.isUnix() || SystemUtils.isMac()) {
            cmd = new String[4];
            cmd[0] = "cp";
            cmd[1] = "-rfp";
            cmd[2] = this.getAbsolutePath();
            cmd[3] = pathDestination;

            SystemUtils.eseguiScriptWithOutput(cmd);
        } else {

            if (this.isFile()) {
                //se e' un file, l'opzione /E fa comportare la xcopy in modo strano
                //(copia anche eventuali directory che sono allo stesso livello del file!!!
                //forse e' un bug della xcopy
                cmd = new String[6];
                cmd[0] = "xcopy";
                cmd[1] = this.getAbsolutePath();
                cmd[2] = pathDestination;
                File f = new File(pathDestination);
                if (!f.exists()) {
                    try {
                        //se il file non esiste e il nome di quello d'origine e quello di destinazione
                        //sono diversi, xcopy si blocca perche' non sa se la directory specificata e' un file o una dir
                        //la createNewFile serve ad ovviare a questa situazione.
                        f.createNewFile();
                    } catch (IOException ex) {
                        throw new RuntimeException("Errore nella creazione del nuovo file!", ex);
                    }
                }
                cmd[3] = "/I";
                cmd[4] = "/Q";
                cmd[5] = "/Y";
            } else {

                cmd = new String[7];
                cmd[0] = "xcopy";
                cmd[1] = this.getAbsolutePath();

                cmd[2] = pathDestination + RecursiveFileImpl.separator + this.getName();

                cmd[3] = "/E";
                cmd[4] = "/I";
                cmd[5] = "/Q";
                cmd[6] = "/Y";
            }
            SystemUtils.eseguiScriptWithOutput(cmd);
        }


    }

    //******************************************************************************/
    public static void deleteFilesByOperatingSistem(String pathFilesToDelete) {
        String[] cmd = null;
        if (SystemUtils.isUnix()) {
            cmd = new String[3];
            cmd[0] = "rm";
            cmd[1] = "-rf";
            cmd[2] = pathFilesToDelete;

            SystemUtils.eseguiScriptWithOutput(cmd);
        } else {
            throw new RuntimeException("non ancora implementato per questo SISTEMA OPERATIVO");
        }
    }

    //******************************************************************************/
    public void copyThisTo(String pathDestination) {
        if (this.isDirectory()) {
            this.copyContentTo(pathDestination + "/" + this.getName());
        } else {
            this.copyContentTo(pathDestination);
        }
    }

    //******************************************************************************/
    public void moveTo(String pathDestination) {
        this.copyThisTo(pathDestination);
        this.deleteThis();
    }

    //***************************************************************************//
    public boolean renameThis(String newPathName, boolean createFullPath) {
        if (this.exists() && newPathName != null) {
            if (createFullPath) {
                new File(newPathName).getParentFile().mkdirs();
            }
            return this.renameTo(new File(newPathName));
        } else {
            throw new RecursiveFileException("File origine non esiste!");
        }
    }
    //***************************************************************************//

    public void deleteThis() {
        this.deleteThis(false);
    }

    //***************************************************************************//
    public void deleteThis(Boolean generateException) {
        if (this.isDirectory()) {
            File[] fl = this.listFiles();
            for (int i = 0; i < fl.length; i++) {
                (new RecursiveFileImpl(fl[i].getAbsolutePath())).deleteThis(generateException);
            }
        }
        if (this.delete() == false && generateException) {
            throw new RuntimeException("Impossibile cancellare il file: " + this.getAbsolutePath());
        }
    }

    //***************************************************************************//
    protected void copyDirectory(File source, File destination) {

        if (this.personalCopier != null && this.personalCopier.dirNeedPersonalCopying(source)) {
            this.personalCopier.copyDir(source, destination, this);
        } else {

            File[] list = source.listFiles();
            int i = 0;
            while (i < list.length) {
                File dest = new File(destination, list[i].getName());
                if (list[i].isDirectory()) {
                    dest.mkdir();
                    copyDirectory(list[i], dest);
                } else {
                    copyFile(list[i], dest);
                }
                i++;
            }
            //System.out.println("copyDir ret "+toReturn);
        } //if(this.personalCopier != null && this.perso ..........

    }

    //***************************************************************************//
    /**
     *Copia il file e crea il file or per l'orientamento
     **/
    /*
    private void copyFileWithFlag(File in, File out){
    String orientationDrew,orientationZonageek,orientation;
    byte fileBuffer[] = FileUtilities.getByteArrayFromFile(in.getAbsolutePath());
    ByteArrayInputStream bais = new ByteArrayInputStream(fileBuffer);
    orientationDrew = GeniusUtils.getOrientationDrew(bais);
    bais.reset();
    orientationZonageek = GeniusUtils.getOrientationZonageek(bais);
    if(orientationDrew==null || orientationZonageek==null || !orientationDrew.equals(orientationZonageek)){
    orientation = "1";
    }else{
    orientation = orientationDrew;
    }
    updateOutput(in,out);
    FileUtilities.saveFileFromBuffer(fileBuffer,out.getAbsolutePath());
    GeniusUtils.createXmlOrientationFile(out,orientation);
    }
     */
    //******************************************************************************//
/*  private void manageOrientation(){
    if(GeniusUtils.isOriginalFileFromGenius(in)){
    copyFileWithFlag(in,out);
    return;
    }
    }
     */
    //******************************************************************************//
    public void copyAndRename(File desstFile) {
        this.copyFile(this, desstFile);
    }

    //******************************************************************************//
    private void copyFile(File in, File out) {
        try {
            if (dischargeExe) {
                if (in.getName().toLowerCase().endsWith(".exe") || in.getName().toLowerCase().equals("thumbs.db") || in.getName().toLowerCase().equals(".bridgesort")) {
                    this.incrementsNumFilesCopied(1);
                    return;
                }
            }

            //***/
            updateOutput(in, out);
            //***/
            if (personalCopier != null && personalCopier.fileNeedPersonalCopying(in)) {
                personalCopier.copyFile(in, out, this);
            } else {
                FileInputStream fis = new FileInputStream(in);
                FileOutputStream fos = new FileOutputStream(out);
                byte[] buf = new byte[10240];
                int i = 0;
                while ((i = fis.read(buf)) != -1) {
                    fos.write(buf, 0, i);
                    if (stop) {
                        fis.close();
                        fos.close();
                        throw new AbortedByUserException("Operazione interrotta dall'utente.");
                    }
                }
                fis.close();
                fos.close();
            }
            this.incrementsNumFilesCopied(1);

        } catch (FileNotFoundException ex) {
            throw new RecursiveFileException("Errore! File non trovato!", ex);
        } catch (IOException ex) {
            throw new RecursiveFileException("Errore IO sul file " + in.getAbsolutePath() + "!", ex);
        } catch (Exception ex) {
            throw new RecursiveFileException("Errore sul File " + in.getPath() + "!", ex);
        }
    }

    //******************************************************************************//
    public static void removeOnFault(String Directory) {
        if (Directory != null) {
            File dir = new File(Directory);
            String[] figli = dir.list();
            for (int i = 0; i < figli.length; i++) {
                deleteDir(new File(dir, figli[i]));
            }
        }
    }

    //******************************************************************************//
    public void emptyThis() {
        if (!this.isDirectory()) {
            this.delete();
        } else {
            String[] children = this.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(this, children[i]));
            }
        }
    }

    //******************************************************************************//
    public static void deleteDir(File dir) {

      if(dir != null){
        if(dir.isDirectory()){
          String[] children = dir.list();
          for (int i = 0; i < children.length; i++) {
              deleteDir(new File(dir, children[i]));
          }//for
        }

        dir.delete();
      }

      /*/
        if (dir != null && !dir.isDirectory()) {
            dir.delete();
        } else {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                deleteDir(new File(dir, children[i]));
            }
        }
        dir.delete();
      //*/
    }

    //******************************************************************************//
    public void deleteEverywere(String fileOrDirToDelete) {
        deleteEverywere(this, fileOrDirToDelete, false);
    }

    //******************************************************************************//
    public void deleteEverywereEmptyFiles() {
        deleteEverywere(this, null, true);
    }

    //******************************************************************************//
    public static void deleteEverywere(File startDir, String fileOrDirToDelete, boolean flgDeleteEmptyFiles) {
        File child = null;
        File[] children = startDir.listFiles();
        for (int i = 0; i < children.length; i++) {
            child = children[i];
            if (fileOrDirToDelete != null && child.getName().equals(fileOrDirToDelete)) {
                deleteDir(child);
            } else if (!child.isDirectory() && child.length() == 0) {
                if(flgDeleteEmptyFiles){
                  deleteDir(child);
                }
            } else if (child.isDirectory()) {
                deleteEverywere(child, fileOrDirToDelete, flgDeleteEmptyFiles);
            }
        }//for

    }

    //******************************************************************************//
    public ArrayList<RecursiveFileImpl> findRecurdively(String fileNameToFind, boolean caseSensitive, boolean matchWholeName, boolean ignoreDirectories, boolean ignoreExtension, boolean ignoreSubdirectory) {
      return findRecurdively(fileNameToFind, caseSensitive, matchWholeName, ignoreDirectories, ignoreExtension, ignoreSubdirectory, false);
    }

    //******************************************************************************//
    public ArrayList<RecursiveFileImpl> findRecurdively(String fileNameToFind, boolean caseSensitive, boolean matchWholeName, boolean ignoreDirectories, boolean ignoreExtension, boolean ignoreSubdirectory, boolean ignoreSpaces) {
        ArrayList<RecursiveFileImpl> resultPaths = new ArrayList<RecursiveFileImpl>();
        findRecurdively(this, resultPaths, fileNameToFind, caseSensitive, matchWholeName, ignoreDirectories, ignoreExtension, ignoreSubdirectory, ignoreSpaces);

        return resultPaths;
    }

    //******************************************************************************//
    public static void findRecurdively(File startDir, ArrayList<RecursiveFileImpl> resultPaths, String fileNameToFind, boolean caseSensitive, boolean matchWholeName, boolean ignoreDirectories, boolean ignoreExtension, boolean ignoreSubdirectory, boolean ignoreSpaces) {
        File child = null;
        File[] children = startDir.listFiles();
        String nameTmp;
        String nameTmpNoSpaces = null;
        String FileNameToFindNoSpaces = null;

        if(ignoreSpaces){
          FileNameToFindNoSpaces = fileNameToFind.replaceAll(" ", "");
        }


        if (children != null) {
            for (int i = 0; i < children.length; i++) {
                child = children[i];
                nameTmp = child.getName();

                if (!caseSensitive) {
                    nameTmp = nameTmp.toLowerCase();
                    fileNameToFind = fileNameToFind.toLowerCase();
                }

                if (ignoreExtension) {
                    int posExt = nameTmp.lastIndexOf(".");
                    if (posExt >= 0) {
                        nameTmp = nameTmp.substring(0, posExt);
                    }
                }

                if(ignoreSpaces){
                  nameTmpNoSpaces = nameTmp.replaceAll(" ", "");
                }



                if ( !(ignoreDirectories && child.isDirectory()) ) {
                    if (matchWholeName) {
                        if ((ignoreSpaces && nameTmpNoSpaces.equals(FileNameToFindNoSpaces) )
                            || nameTmp.equals(fileNameToFind) ) {
                            resultPaths.add(new RecursiveFileImpl(child.getAbsolutePath()));
                        }
                    } else {
                        if ((ignoreSpaces && nameTmp.indexOf(FileNameToFindNoSpaces) >= 0 )
                            || nameTmp.indexOf(fileNameToFind) >= 0 ) {
                            resultPaths.add(new RecursiveFileImpl(child.getAbsolutePath()));
                        }
                    }

                }

                if (!ignoreSubdirectory && child.isDirectory()) {
                    findRecurdively(child, resultPaths, fileNameToFind, caseSensitive, matchWholeName, ignoreDirectories, ignoreExtension, ignoreSubdirectory, ignoreSpaces);
                }
            }//for
        }

    }

    //******************************************************************************//
    public String findEverywere(String fileOrDirToFind, boolean caseInsensitive) {
        return findEverywere(this, fileOrDirToFind, caseInsensitive);
    }
    //******************************************************************************//

    public static String findEverywere(File startDir, String fileOrDirToFind, boolean caseInsensitive) {
        String result = null;
        File child = null;
        String childName = null;
        File[] children = startDir.listFiles();

        if (children == null) {
            return null;
        }

        for (int i = 0; i < children.length; i++) {
            child = children[i];
            childName = child.getName();
            if (caseInsensitive) {
                childName = childName.toLowerCase();
                fileOrDirToFind = fileOrDirToFind.toLowerCase();
            }
            if (childName.equals(fileOrDirToFind)) {
                result = child.getAbsolutePath();
            } else if (child.isDirectory()) {
                result = findEverywere(child, fileOrDirToFind, caseInsensitive);
            }

            if (result != null) {
                break;
            }
        }//for

        return result;
    }

    //******************************************************************************//
    public void setStop(boolean stop) {
        RecursiveFileImpl.stop = stop;
    }

    //******************************************************************************//
    public void scartaExe(boolean scarta) {
        this.dischargeExe = scarta;
    }

    public void setRecursiveFilePersonalCopier(RecursiveFilePersonalCopier copier) {
        this.personalCopier = copier;
    }

    //******************************************************************************//
    public static void contaFile(boolean conta) {
        fileCounter = conta;
    }
    public static final int COMPARE_TYPE_DIFFERS = 0;
    public static final int COMPARE_FILE_IS_NEW = 1; //file non presente nella  cartella con cui si � effettuato il confronto
    public static final int COMPARE_DATE_DIFFERS = 2;
    public static final int COMPARE_REMOTE_FILE_IS_NEW = 3; //file prensente solo nella cartella con cui si � effettuato il confronto

    //******************************************************************************//
    public int compareNameTo(File fileToCompare) {
        String thisName = this.getName();
        String nameToCompare = fileToCompare.getName();
        if (SystemUtils.isWindows()) {
            thisName = thisName.toLowerCase();
            nameToCompare = nameToCompare.toLowerCase();
        }

        return thisName.compareTo(nameToCompare);
    }

    //******************************************************************************//
    public int compareDatetimeTo(File fileToCompare) {
        int result;

        if (this.lastModified() < fileToCompare.lastModified()) {
            result = -1;
        } else if (this.lastModified() == fileToCompare.lastModified()) {
            result = 0;
        } else {
            result = 1;
        }

        return result;
    }

    //******************************************************************************//
    public Hashtable<RecursiveFileImpl, Integer> getDifferencesInCompareByDateTimesNotRecursively(RecursiveFileImpl fdToCompare, boolean excludeCvsFiles) {
        Hashtable<RecursiveFileImpl, Integer> result = new Hashtable<RecursiveFileImpl, Integer>();

        if (!fdToCompare.exists()) {
            result.put(this, COMPARE_FILE_IS_NEW);
        } else if (!this.exists()) {
            result.put(this, COMPARE_REMOTE_FILE_IS_NEW);
        } else if (this.isDirectory() != fdToCompare.isDirectory()) {
            result.put(this, COMPARE_TYPE_DIFFERS);
        } else if (this.isFile()) {
            if (this.compareNameTo(fdToCompare) != 0) {
                result.put(this, COMPARE_FILE_IS_NEW);
            } else if (this.compareDatetimeTo(fdToCompare) != 0) {
                result.put(this, COMPARE_DATE_DIFFERS);
            }
        } else if (this.isDirectory()) {
            boolean fileFound = false;
            RecursiveFileImpl[] listLocalFiles = this.listRecursiveFiles();
            RecursiveFileImpl[] listFilesToCompare = fdToCompare.listRecursiveFiles();

            for (int i = 0; i < listLocalFiles.length; i++) {
                if (listLocalFiles[i].getName().compareTo("CVS") == 0 && listLocalFiles[i].isDirectory()) {
                    continue;
                }

                fileFound = false;
                for (int j = 0; j < listFilesToCompare.length; j++) {
                    if (listFilesToCompare[j] == null) {
                        continue;
                    }

                    if (listLocalFiles[i].compareNameTo(listFilesToCompare[j]) == 0) {
                        if (listLocalFiles[i].compareDatetimeTo(listFilesToCompare[j]) != 0) {
                            result.put(listLocalFiles[i], COMPARE_DATE_DIFFERS);
                        }
                        fileFound = true;
                        listFilesToCompare[j] = null;
                        break;
                    }

                } //for j
                if (!fileFound) {
                    result.put(listLocalFiles[i], COMPARE_FILE_IS_NEW);
                }

            }//for i


            for (int j = 0; j < listFilesToCompare.length; j++) {
                if (listFilesToCompare[j] != null) {
                    result.put(listFilesToCompare[j], COMPARE_REMOTE_FILE_IS_NEW);
                    listFilesToCompare[j] = null;
                }
            }//for j
        }

        return result;
    }
    //*****************************************************************************//

    public void updateOutput(File in, File out) {
        if (myOutput != null) {
            myOutput.updateFileNumber(String.valueOf(currFileNum), String.valueOf(tot), in.getName());
            myOutput.updatePercentage("---%", in.getName());
        }
    }

    //*****************************************************************************//
    public void incrementsNumFilesCopied(int incrementValue) {
        this.currFileNum += incrementValue;
    }

    //*****************************************************************************//
    public String getNameWithoutExtension() {
        String result = this.getName();
        result = result.substring(0, result.lastIndexOf("."));

        return result;
    }

    public String getExtension() {
        String result = null;
        String filename = this.getName();
        int index = filename.lastIndexOf(".");
        if (index > 0) {
            result = filename.substring(index);
        }
        return result;
    }

    //*****************************************************************************//
    public Object getCustomInfos() {
        return customInfos;
    }

    //*****************************************************************************//
    public void setCustomInfos(Object customInfos) {
        this.customInfos = customInfos;
    }

    public ArrayList<RecursiveFileImpl> getFilesStartingWith(String prefix) {

        ArrayList<RecursiveFileImpl> listaFiles = new ArrayList<RecursiveFileImpl>();
        if (!this.isDirectory()) {
            return listaFiles;
        }

        RecursiveFileImpl[] allFiles = this.listRecursiveFiles();
        for (int i = 0; allFiles != null && i < allFiles.length; i++) {
            if (allFiles[i].getName().startsWith(prefix)) {
                listaFiles.add(allFiles[i]);
            }
        }

        return listaFiles;

    }
}
