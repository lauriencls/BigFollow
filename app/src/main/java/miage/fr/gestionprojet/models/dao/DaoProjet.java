package miage.fr.gestionprojet.models.dao;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Projet;

/**
 * Created by Audrey on 23/01/2017.
 */

public class DaoProjet {

    public List<Projet> getProjetEnCours(Date dateDuJour){
        return new Select()
                .from(Projet.class)
                //.where("date_fin_initiale>? or date_fin_reelle>?", dateDuJour.getTime(),dateDuJour.getTime())
                .execute();
    }
    public static List<Projet> loadAll(){
        List<Projet> projets = new Select().from(Projet.class).execute();
        return projets;
    }

    public static Date getDateFin(long idProjet){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT max(a.dt_fin_prevue) FROM " + new Action().getTableName()
                        + " a INNER JOIN Domaine d ON a.domaine = d.id INNER JOIN Projet p ON d.projet = p.id WHERE p.id = "+idProjet, null);
        Date dateFinPrevu;
        if(c.moveToFirst()){
            Calendar.getInstance().setTimeInMillis(c.getLong(0));
            dateFinPrevu = Calendar.getInstance().getTime();
            return dateFinPrevu;
        }
            return null;
    }

    public static Date getDateDebut(long idProjet){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT min(a.dt_debut) FROM " + new Action().getTableName()
                        + " a INNER JOIN Domaine d ON a.domaine = d.id INNER JOIN Projet p ON d.projet = p.id WHERE p.id = "+idProjet, null);
        Date dateDebut;
        if(c.moveToFirst()){
            Calendar.getInstance().setTimeInMillis(c.getLong(0));
            dateDebut = Calendar.getInstance().getTime();
            return dateDebut;
        }
        return null;
    }
}