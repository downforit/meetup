package utils.comparators;

import java.util.Comparator;
import java.io.File;
import java.util.Date;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class FileDateLastModifiedComparator implements Comparator {
    public FileDateLastModifiedComparator() {

    }


    /**
     * Compares its two arguments for order.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * @todo Implement this java.util.Comparator method
     */
    public int compare(Object o1, Object o2) {
        Date  date_1 = null, date_2 = null;

        if(o1 instanceof File){
            File item_1= (File)o1,item_2 =(File)o2;
            date_1 = new Date(item_1.lastModified());
            date_2 = new Date(item_2.lastModified());
        }

        if(date_1==null){
            return  1;
        }else if(date_2==null){
            return -1;
        }else{

            return date_1.compareTo(date_2);
        }

    }

}
