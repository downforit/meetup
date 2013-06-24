package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import javax.swing.JComboBox;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sql.rowset.CachedRowSet;


public class StringUtilities {


    //RFC 2822 token definitions for valid email - only used together to form a java Pattern object:
    private static final String sp = "\\!\\#\\$\\%\\&\\'\\*\\+\\-\\/\\=\\?\\^\\_\\`\\{\\|\\}\\~";
    private static final String atext = "[a-zA-Z0-9" + sp + "]";
    private static final String atom = atext + "+"; //one or more atext chars
    private static final String dotAtom = "\\." + atom;
    private static final String localPart = atom + "(" + dotAtom + ")*";
    //RFC 1035 tokens for domain names:
    private static final String digit = "[0-9]";
    private static final String aNumber = digit+"+";
    private static final String letter = "[a-zA-Z]";
    private static final String letDig = "[a-zA-Z0-9]";
    private static final String letDigHyp = "[a-zA-Z0-9-]";
    public static final String rfcLabel = letDig + letDigHyp + "{0,61}" + letDig;
    private static final String domain = rfcLabel + "(\\." + rfcLabel + ")*\\." + letter + "{2,6}";
//Combined together, these form the allowed email regexp allowed by RFC 2822:
    private static final String addrSpec = "^" + localPart + "@" + domain + "$";
//now compile it:
    public static final Pattern VALID_PATTERN = Pattern.compile( addrSpec );
    public static final String SYMBOLO_EURO = "�";
    public static final String SYMBOLO_COPYRIGHT = "�";


    

    public static boolean IsEmail(String Email) {
        return VALID_PATTERN.matcher( Email ).matches();
    }


    private static String normalizzaParametroPerSql(String parametro) {
        StringBuffer strTmp = new StringBuffer(parametro);
        int i = 0;
        while (strTmp.length() > i) {
            char chrTmp = strTmp.charAt(i);
            if (chrTmp == '\\' || chrTmp == '\'') {
                strTmp.insert(i, '\\');
                i++;
            }
            i++;
        }
        return strTmp.toString();
    }

    public static ArrayList<Integer> findNumbersInString(String inputString) {
        ArrayList<Integer> result = new ArrayList<Integer>();

        String currNumber = "";
        for (int i = 0; i < inputString.length(); i++) {
            char c = inputString.charAt(i);
            if (Character.isDigit(c)) {
                currNumber = currNumber + c;
            } else {
                if (currNumber.length() > 0) {
                    result.add(new Integer(currNumber));
                    currNumber = "";
                }
            }
        }
        if (currNumber.length() > 0) {
            result.add(new Integer(currNumber));
            currNumber = "";
        }
        return result;
    }

    public static Integer findMaxNumberInString(String inputString) {

        Integer max = -1;
        ArrayList<Integer> result = findNumbersInString(inputString);

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).intValue() > max.intValue()) {
                max = result.get(i);
            }
        }
        return max;
    }

//    private static String cifraString(String strToCrypt) {
//
//        Encrypter encrypter = new Encrypter();
//        // Encrypt
//        String encrypted = encrypter.encrypt(strToCrypt);
//
//        return encrypted;
//    }
//
//    private static String decifraString(String strDoDecrypt) {
//
//        Encrypter encrypter = new Encrypter();
//        // Decrypt
//        String decrypted = encrypter.decrypt(strDoDecrypt);
//
//        return decrypted;
//    }
//
//    private static String cifraContenutoFile(String filePath, String destinationFilePath) {
//
//        String fileContent = FileUtilities.fileToString(filePath);
//        String testoCifrato = cifraString(fileContent);
//        FileUtilities.stringBufferToFile(new StringBuffer(testoCifrato), destinationFilePath);
//        return testoCifrato;
//    }
//
//    private static String decifraContenutoFile(String filePath) {
//
//        String fileContent = FileUtilities.fileToString(filePath);
//        String testoInChiaro = decifraString(fileContent);
//        return testoInChiaro;
//    }


    /****************************************************************************************/
    public static String doAnagrammaForSiteName(String name) {
        if (name == null || name.length() == 0) {
            throw new RuntimeException("input non valido");
        }
        if (name.indexOf("www.") != -1) {
            name = name.substring(4);
        }
        int dotIndex = name.indexOf(".");
        if (dotIndex != -1) {
            name = name.substring(0, dotIndex);
        }

        char[] tmpResult = new char[name.length()];
        String result = null;
        int n = name.length();

        for (int i = 0; i < n / 2; i++) {
            tmpResult[2 * i] = name.charAt(n - 1 - i);
            tmpResult[2 * i + 1] = name.charAt(i);
        }
        if ((n % 2) == 1) {
            tmpResult[n - 1] = name.charAt(n / 2);
        }
        result = new String(tmpResult);

        return result;

    }

    /****************************************************************************************/
    public static String replaceLastDirName(String sourceFullPath, String dirNameToReplace, String newDirName) {
        String result = sourceFullPath;
        boolean dirNameFound = false;
        char[] sourceChars = sourceFullPath.toCharArray();
        int posDirName = -1;

        while (!dirNameFound) {
            if (posDirName == -1) {
                posDirName = sourceFullPath.lastIndexOf(dirNameToReplace);
            } else {
                posDirName = sourceFullPath.lastIndexOf(dirNameToReplace, posDirName - 1);
            }

            if (posDirName < 0) {
                break;
            }

            dirNameFound = true;
            if ((posDirName - 1) >= 0 && sourceChars[posDirName - 1] != '/' && sourceChars[posDirName - 1] != '\\') {
                dirNameFound = false;
            }

            if ((posDirName + dirNameToReplace.length()) < sourceChars.length && sourceChars[posDirName + dirNameToReplace.length()] != '/' && sourceChars[posDirName + dirNameToReplace.length()] != '\\') {
                dirNameFound = false;
            }

            if (dirNameFound) {
                result = result.substring(0, posDirName) + newDirName + result.substring(posDirName + dirNameToReplace.length());
            }
        }

        return result;
    }

    /****************************************************************************************/
    public static String settaInizialiMaiuscole(String string) {

        if (string == null || string.length() == 0) {
            return string;
        }

        ArrayList<String> alParoleDaEvitare = new ArrayList<String>();
        alParoleDaEvitare.add("in");
        alParoleDaEvitare.add("del");
        alParoleDaEvitare.add("d'");
        alParoleDaEvitare.add("di");
        alParoleDaEvitare.add("della");
        alParoleDaEvitare.add("dei");
        alParoleDaEvitare.add("dell'");
        alParoleDaEvitare.add("degli");
        alParoleDaEvitare.add("delle");
        alParoleDaEvitare.add("dello");
        alParoleDaEvitare.add("da");
        alParoleDaEvitare.add("sui");
        alParoleDaEvitare.add("a");
        alParoleDaEvitare.add("e");
        alParoleDaEvitare.add("allo");
        alParoleDaEvitare.add("alla");
        alParoleDaEvitare.add("ai");
        alParoleDaEvitare.add("agli");
        alParoleDaEvitare.add("alle");
        alParoleDaEvitare.add("al");



        String iniziale = String.valueOf(string.charAt(0)).toUpperCase();


        if (string.length() == 1) {
            string = iniziale;
        } else {
            string = iniziale + string.substring(1);
        }
        String currentLetter, letterToCapitalize;
        for (int i = 1; i < string.length(); i++) {
            currentLetter = String.valueOf(string.charAt(i));
            if (currentLetter.equals(" ") || currentLetter.equals("'")) {
                if (string.length() > i + 1) {
                    int nextSpaceIndex = string.indexOf(" ", i + 1);

                    if (nextSpaceIndex != -1) {
                        String nextWord = string.substring(i + 1, nextSpaceIndex);
                        if (alParoleDaEvitare.contains(nextWord)) {
                            continue;
                        }
                    }
                    letterToCapitalize = String.valueOf(string.charAt(i + 1));
                    letterToCapitalize = letterToCapitalize.toUpperCase();
                    string = string.substring(0, i) + currentLetter + letterToCapitalize + string.substring(i + 2);
                }
            }
        }
        return string;
    }

    /****************************************************************************************/
    public static int countCharInString(char searchedChar, String entireString) {

        int counter = 0;
        for (int i = 0; i < entireString.length(); i++) {
            if (entireString.charAt(i) == searchedChar) {
                counter++;
            }
        }
        return counter;
    }

    /****************************************************************************************/
    public static String removeCharFromString(String str, char charToRemove) {
        StringBuffer sb = new StringBuffer(str), result = new StringBuffer();

        for (int i = 0; i < sb.length(); i++) {
            if (sb.charAt(i) != charToRemove) {
                result.append(sb.charAt(i));
            }
        }
        return result.toString();
    }

    /****************************************************************************************/
    public static boolean isStringInArray(String[] arrayString, String str) {
        boolean found = false;
        if (arrayString != null) {
            for (int i = 0; i < arrayString.length; i++) {
                if (arrayString[i].equals(str)) {
                    found = true;
                    break;
                }
            }
        }

        return found;
    }

    /****************************************************************************************/
    public static void addArrayStringToComboBox(JComboBox jCombo, String[] listElements) {
        if (listElements != null) {
            jCombo.removeAllItems();
            for (int i = 0; i < listElements.length; i++) {
                jCombo.addItem(listElements[i]);
            }
        }
    }

    /****************************************************************************************/
    public static String normalizzaParametro(String strParam) {

        //TODO: UTILIZZATO SOLO X IL NOME DELL'ALBUM NEL SITO....
        //CONTROLLARE SE CONVIENE UTILIZZARE "normalizzaNomeFile"
        strParam = strParam.replace('\\', '_');
        strParam = strParam.replace('/', '_');
        strParam = strParam.replace(':', '_');
        strParam = strParam.replace('*', '_');
        strParam = strParam.replace('|', '_');
        strParam = strParam.replace('<', '_');
        strParam = strParam.replace('>', '_');
        strParam = strParam.replace('?', '_');
        strParam = strParam.replace('"', '_');

        return strParam;
    }

    /**************************************************************************************************/
    public static String normalizzaNomeFile(String strToChange, int maxLenght) {
      String result;

      result = normalizzaNomeFile(strToChange);

      if(result.length() > maxLenght){
        String puntiniSospensivi = "_CUTED_";
        result = result.substring(0, ( (maxLenght - puntiniSospensivi.length()) / 2) -1 ) + puntiniSospensivi + result.substring(result.length() - (( (maxLenght - puntiniSospensivi.length()) / 2)) );
      }

      return result;
    }


    /**************************************************************************************************/
    public static String normalizzaNomeFile(String strToChange) {

        return normalizzaNomeFile(strToChange, 'X');
        /*
        for(int i = 0; i < strToChange.length(); i++) {
        if(!isAllowedChar(strToChange.charAt(i))) {
        strToChange = strToChange.replace(strToChange.charAt(i), 'X');
        }
        }
        return strToChange;*/
    }

    /**************************************************************************************************/
    public static String normalizzaNomeFile(String strToChange, char replacingChar) {

        for (int i = 0; i < strToChange.length(); i++) {
            if (!isAllowedChar(strToChange.charAt(i))) {
                strToChange = strToChange.replace(strToChange.charAt(i), replacingChar);
            }
        }

        return strToChange;
    }




    /**************************************************************************************************/
    public static boolean isAllowedChar(char ch) {
        if (ch > 127) {
            return false;
        }
        if (ch == ':' || ch == '!' || ch == ',' || ch == '^' || ch == ';' || ch == '\'' || ch == '%' ||
                ch == '�' || ch == '�' || ch == '�' || ch == '�' || ch == '�' || ch == '�' || ch == '�' ||
                ch == '�' || ch == '@' || ch == '�' || ch == '?' || ch == '<' || ch == '>' || ch == '&' || ch == '$' ||
                ch == '\\' || ch == '/' || ch == '#' || ch > 127) {
            return false;
        } else {
            return true;
        }
    }

    /***********************************************************************************/
    public static String rendiNumeroA3Cifre(int num) {
        String strNumPaginaCorrente;
        if (num < 10) {
            strNumPaginaCorrente = "00" + num;
        } else if (num >= 10 && num < 100) {
            strNumPaginaCorrente = "0" + num;
        } else {
            strNumPaginaCorrente = String.valueOf(num);
        }
        return strNumPaginaCorrente;
    }

    /***********************************************************************************/
    public static String rendiNumeroA2Cifre(int num) {
        String strNumPaginaCorrente;
        if (num < 10) {
            strNumPaginaCorrente = "0" + num;
        } else {
            strNumPaginaCorrente = String.valueOf(num);
        }
        return strNumPaginaCorrente;
    }

    /***********************************************************************************/
    public static String rendiNumeroA1Cifra(int num) {
      if(num >= 10){
        throw new RuntimeException("impossibile continuare, il numero e' a due cifre");
      } else {
        return String.valueOf(num);
      }
    }

    /***********************************************************************************/
    public static String rendiNumeroA4Cifre(int num) {
        String strNumPaginaCorrente;
        if (num < 10) {
            strNumPaginaCorrente = "000" + num;
        } else if (num >= 10 && num < 100) {
            strNumPaginaCorrente = "00" + num;
        } else if (num >= 100 && num < 1000) {
            strNumPaginaCorrente = "0" + num;
        } else {
            strNumPaginaCorrente = String.valueOf(num);
        }
        return strNumPaginaCorrente;
    }

    /**
     * riempe la stringa strToFill con tanti char chFilling davanti fino a raggiungere la lunghezza finalLength
     * 
     * @param strToFill
     * @param chFilling
     * @param finalLength
     * @return
     */

    public static String fillStringWithChar(String strToFill, char chFilling, int finalLength) {

        return fillStringWithChar(strToFill, chFilling, finalLength, false);        
//        int strInitialLen = strToFill.length();
//
//        String strFilling = String.valueOf(chFilling);
//        String strPadding = "";
//        for (int i = 0; i < finalLength - strInitialLen; i++) {
//            strPadding = strPadding + strFilling;
//        }
//        return (strPadding.concat(strToFill));

    }


    /**
     * riempe la stringa strToFill con tanti char chFilling davanti fino a raggiungere la lunghezza finalLength
     *
     * @param strToFill
     * @param chFilling
     * @param finalLength
     * @param append -> se true, appende in coda
     * @return
     */

    public static String fillStringWithChar(String strToFill, char chFilling, int finalLength,boolean append) {
        
        int strInitialLen = strToFill.length();

        String strFilling = String.valueOf(chFilling);
        String strPadding = "";
        for (int i = 0; i < finalLength - strInitialLen; i++) {
            strPadding = strPadding + strFilling;
        }
        if(append){
            return (strToFill.concat(strPadding));
        }else{
            return (strPadding.concat(strToFill));
        }
        

    }

    /***********************************************************************************/
    public static int exstractNumberFromString(String strToElaborate) {
        int result = -1;
        /*/
        char charTmp;
        boolean charIsADigit = false;

        int startPosNumber = -1, endPosNumber = -1;

        if (strToElaborate != null) {
            for (int i = 0; i < strToElaborate.length(); i++) {
                charIsADigit = false;
                charTmp = strToElaborate.charAt(i);
                if (charTmp >= '0' && charTmp <= '9') {
                    charIsADigit = true;
                }

                if (startPosNumber < 0 && charIsADigit) {
                    startPosNumber = i;
                }

                if (startPosNumber >= 0 && endPosNumber < 0 && !charIsADigit) {
                    endPosNumber = i - 1;
                }

                if (endPosNumber >= 0 && endPosNumber >= 0 && charIsADigit) {
                    endPosNumber = -1;
                    startPosNumber = i;
                }
            }//for
        }//if


        if ((startPosNumber >= 0 && endPosNumber < 0) || endPosNumber < startPosNumber) {
            endPosNumber = strToElaborate.length() - 1;
        }

        if (startPosNumber >= 0 && endPosNumber >= 0) {
            String strTmp = strToElaborate.substring(startPosNumber, endPosNumber + 1);
            result = Integer.parseInt(strTmp);
        }
        //*/

        String strTmp = exstractStringNumberFromString(strToElaborate);
        if(strTmp != null){
          result = Integer.parseInt(strTmp);
        }

        return result;
    }

    /***********************************************************************************/
    public static String exstractStringNumberFromString(String strToElaborate) {
        String result = null;
        char charTmp;
        boolean charIsADigit = false;

        int startPosNumber = -1, endPosNumber = -1;

        if (strToElaborate != null) {
            for (int i = 0; i < strToElaborate.length(); i++) {
                charIsADigit = false;
                charTmp = strToElaborate.charAt(i);
                if (charTmp >= '0' && charTmp <= '9') {
                    charIsADigit = true;
                }

                if (startPosNumber < 0 && charIsADigit) {
                    startPosNumber = i;
                }

                if (startPosNumber >= 0 && endPosNumber < 0 && !charIsADigit) {
                    endPosNumber = i - 1;
                }

                if (endPosNumber >= 0 && endPosNumber >= 0 && charIsADigit) {
                    endPosNumber = -1;
                    startPosNumber = i;
                }
            }//for
        }//if


        if ((startPosNumber >= 0 && endPosNumber < 0) || endPosNumber < startPosNumber) {
            endPosNumber = strToElaborate.length() - 1;
        }

        if (startPosNumber >= 0 && endPosNumber >= 0) {
            result = strToElaborate.substring(startPosNumber, endPosNumber + 1);
        }

        return result;
    }



    public static String exstractStringFirstNumberFromString(String strToElaborate){
        String result = null;
        char charTmp;
        boolean charIsADigit = false;

        int startPosNumber = -1, endPosNumber = -1;

        if (strToElaborate != null) {
            for (int i = 0; i < strToElaborate.length(); i++) {
                charIsADigit = false;
                charTmp = strToElaborate.charAt(i);
                if (charTmp >= '0' && charTmp <= '9') {
                    charIsADigit = true;
                }

                if (startPosNumber < 0 && charIsADigit) {
                    startPosNumber = i;
                }

                if (startPosNumber >= 0 && endPosNumber < 0 && !charIsADigit) {
                    endPosNumber = i - 1;
                }

                //if(endPosNumber >= 0  && endPosNumber>=0 && charIsADigit){
                //  endPosNumber = -1;
                //  startPosNumber = i;
                //}

                if (!charIsADigit && endPosNumber >= 0 && endPosNumber >= 0) {
                    break;
                }

            }//for
        }//if


        if ((startPosNumber >= 0 && endPosNumber < 0) || endPosNumber < startPosNumber) {
            endPosNumber = strToElaborate.length() - 1;
        }

        if (startPosNumber >= 0 && endPosNumber >= 0) {
            String strTmp = strToElaborate.substring(startPosNumber, endPosNumber + 1);
            result = strTmp;
        }

        return result;
    }

    /***********************************************************************************/
    public static int exstractFirstNumberFromString(String strToElaborate) {

        int result = -1;
        String strTmp = exstractStringFirstNumberFromString(strToElaborate);

        if (strTmp !=null ) {
            result = Integer.parseInt(strTmp);
        }

        return result;
    }

    /***********************************************************************************/
    public static String getFileNameWithoutExtension(String strFileName) {
        String result = strFileName;

        if (result.lastIndexOf(".") >= 0) {
            result = result.substring(0, result.lastIndexOf("."));
        }

        return result;
    }


    /***********************************************************************************/
    public static String getFileExtensionWithoutDot(String strFileName) {
        String result = strFileName;

        if (result.lastIndexOf(".") >= 0) {
            result = result.substring(result.lastIndexOf(".") + 1);
        }

        return result;
    }

    /***********************************************************************************/
    public static String extractStringWithBoundariesForSetupFileName(String inputString,String strBoundaries){


        String pattern = strBoundaries+digit+digit+aNumber+ letter +letter+ strBoundaries;
        String result = null;

        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(inputString);

        if(matcher.find()){
            int start = matcher.start();
            int end = matcher.end();
            result = inputString.substring(start+strBoundaries.length(), end-strBoundaries.length());
        }

        return result;
    }


    /***********************************************************************************/
    public static String replaceSafety(String stringToElaborate, CharSequence oldValue, CharSequence newValue){
      String result;//non lo assegno con stringToElaborate perche' essendo stringa ogni assegnazione crea una nuova istanza e quindi sarebbe quasi sempre inutile e quindi faccio l'assegnazione direttamente nel catch

      try{
        result = stringToElaborate.replace(oldValue, newValue);
      }catch(Throwable ex){
        result = stringToElaborate;
      }

      return result;
    }


    /**
     *
     * @param listaNomi -> lista di stringhe separate da virgola
     * @return
     */
    public static ArrayList<String> getListaNomi_FromString(String listaSeparataDaVirgole) {
        ArrayList<String> listaNomi = null;
        if (listaSeparataDaVirgole != null) {
            listaNomi = new ArrayList<String>();
            while (true) {
                int index = listaSeparataDaVirgole.indexOf(",");
                String strTmp;
                if (index != -1) {
                    strTmp = listaSeparataDaVirgole.substring(0, index);
                    listaSeparataDaVirgole = listaSeparataDaVirgole.substring(index + 1);

                    listaNomi.add(strTmp);
                } else {
                    strTmp = listaSeparataDaVirgole;
                    listaNomi.add(strTmp);
                    break;
                }

            }
        }
        return listaNomi;

    }



    /*************************************************************************/
    public static ArrayList<Integer> getListaId_FromString(String listaId) {
        ArrayList<Integer> listaID = null;
        if (listaId != null) {
            listaID = new ArrayList<Integer>();
            while (true) {
                int index = listaId.indexOf(",");
                String strId;
                if (index != -1) {
                    strId = listaId.substring(0, index);
                    listaId = listaId.substring(index + 1);

                    listaID.add(Integer.parseInt(strId));
                } else {
                    strId = listaId;
                    listaID.add(Integer.parseInt(strId));
                    break;
                }

            }
        }
        return listaID;
    }


    public static String[] splitStringWithoutRegExpression(String inputString,char splitChar){

        int size = countCharInString(splitChar, inputString)+1;
        String[] result = new String[size];

        String tmpString = inputString;

        int index = inputString.indexOf(splitChar);
        int counter = 0;
        while(index>=0){
            String currStr = tmpString.substring(0, index);
            tmpString = tmpString.substring(index+1);
            result[counter]= currStr;
            counter++;
            index = tmpString.indexOf(splitChar);
        }
        result[size-1]=tmpString;



        return result;
    }


    public static boolean isOnlyDigits(String strToEvaluate, boolean spacesEnabled){
      boolean result = true;

      char []characters = strToEvaluate.toCharArray();

      for(int i = 0; i < characters.length; i++){
        if(characters[i] < '0' ||  characters[i] > '9'){
          if( !(spacesEnabled && characters[i] == ' ') ){
            result = false;
            break;
          }
        }
      }// for i

      return result;
    }

    public static boolean isTimeString(String strToEvaluate){
      boolean result = true;
      int posTmp;
      
      try{
        posTmp = strToEvaluate.indexOf(":");
        int ore = Integer.parseInt(strToEvaluate.substring(0, posTmp));
        if(ore < 0 || ore > 23){
          return false;
        }

        int minuti = Integer.parseInt(strToEvaluate.substring(posTmp + 1));
        if(minuti < 0 || minuti > 59){
          return false;
        }
      }catch(Throwable ex){
        result = false;
      }

      return result;
    }



}
