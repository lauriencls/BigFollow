package miage.fr.gestionprojet.outils;

import android.icu.text.LocaleDisplayNames;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Audrey on 27/02/2017.
 */

public class Outils {
    public static long CONST_DURATION_OF_DAY = 1000l * 60 * 60 * 24;

    public static int calculerPourcentage(double valeurReleve, double valeurCible){
        int result = (int) ((valeurReleve/valeurCible)*100);
        return result;
    }

    public static Date weekOfYearToDate(int year, int week){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.WEEK_OF_YEAR,week);
        c.set(Calendar.YEAR,year);
        return c.getTime();
    }

    public static long dureeEntreDeuxDate(Date dateInf, Date datePost){
        long duree = datePost.getTime() - dateInf.getTime();
        long numberOfDay = duree/CONST_DURATION_OF_DAY;
        return numberOfDay;
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static int toInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return -1;
        } catch(NullPointerException e) {
            return -1;
        }
        // only got here if we didn't return false
    }
}
