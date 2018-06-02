package miage.fr.gestionprojet.models.dao;

import com.activeandroid.query.Select;

import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Mesure;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.SaisieCharge;

/**
 * Created by Audrey on 27/02/2017.
 */

public class DaoMesure {

    public static Mesure getLastMesureBySaisieCharge(long idSaisieCharge){
        List<Mesure> lstMesures =
                new Select()
                .from(Mesure.class)
                .where("action=?", idSaisieCharge)
                .orderBy("dt_mesure DESC")
                .execute();
        if (lstMesures.size() > 0) {
            return lstMesures.get(0);
        } else {
            return new Mesure();
        }
    }

    public static List<Mesure> getListtMesureByAction(long idSaisieCharge) {
        List<Mesure> mesures = new Select().from(Mesure.class).execute();
        List<Mesure> lstMesures =
                new Select()
                        .from(Mesure.class)
                        .where("action=?", idSaisieCharge)
                        .execute();

        return lstMesures;
    }

    public static List<Mesure> loadAll() {
        List<Mesure> mesures = new Select().from(Mesure.class).execute();
        return mesures;

    }



}
