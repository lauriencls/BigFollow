package miage.fr.gestionprojet.models.dao;

import android.database.Cursor;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Projet;

/**
 * Created by Audrey on 07/04/2017.
 */

public class DaoAction {
    public static List<Action> loadActionsByCode(String code) {
        //getAll
        List<Action> actions = new Select()
                .from(Action.class)
                .where("code=?",code)
                .execute();
        return actions;
    }

    public static List<Action> loadActionsByType(String type, long idProjet) {
        Projet proj = Model.load(Projet.class, idProjet);
        List<Action> lstActions = new ArrayList<>();
        for(Domaine d : proj.getLstDomaines()) {
            List<Action> actions = new Select().from(Action.class)
                    .where("typeTravail = ? and domaine=?", type, d.getId())
                    .execute();
            lstActions.addAll(actions);
        }
        return lstActions;
    }

    public static List<Action> loadActionsByPhaseAndDate(String phase,Date d, long idProjet) {
        Projet proj = Model.load(Projet.class, idProjet);
        List<Action> lstActions = new ArrayList<>();
        for(Domaine dom : proj.getLstDomaines()) {
            List<Action> actions = new Select().from(Action.class)
                    .where("phase = ? and dt_fin_prevue>=? and dt_debut<=? and domaine=?", phase, d.getTime(), d.getTime(), dom.getId())
                    .execute();
            lstActions.addAll(actions);
        }
        return lstActions;
    }

    public static List<Action> loadActionsByDate(Date d, long idProjet) {
        Projet proj = Model.load(Projet.class, idProjet);
        List<Action> lstActions = new ArrayList<>();
        for(Domaine dom : proj.getLstDomaines()) {
            List<Action> actions = new Select()
                    .from(Action.class)
                    .where("dt_fin_prevue>=? and dt_debut<=? and domaine = ?", d.getTime(), d.getTime(), dom.getId())
                    .execute();
            lstActions.addAll(actions);
        }
        return lstActions;
    }

    public static List<Action> loadAll(){
        List<Action> actions = new Select().from(Action.class).execute();
        return actions;
    }

    public static List<Action> loadActionsOrderByNomAndDate(Date d, long idProjet){
        Projet proj = Model.load(Projet.class, idProjet);
        List<Action> lstActions = new ArrayList<>();
        for(Domaine dom : proj.getLstDomaines()) {
            List<Action> actions = new Select()
                    .from(Action.class)
                    .where("dt_fin_prevue>=? and dt_debut<=? and domaine=?", d.getTime(), d.getTime(), dom.getId())
                    .orderBy("code ASC")
                    .execute();
            lstActions.addAll(actions);
        }
        return lstActions;
    }

    public static List<Action> loadActionsByDomaineAndDate(int idDomaine,Date d, long idProjet){
        Projet proj = Model.load(Projet.class, idProjet);
        List<Action> lstActions = new ArrayList<>();
        for(Domaine dom : proj.getLstDomaines()) {
            List<Action> result = new Select()
                    .from(Action.class)
                    .where("domaine=? and dt_fin_prevue>=? and dt_debut<=? and domaine=?", idDomaine, d.getTime(), d.getTime(),dom.getId())
                    .execute();
            lstActions.addAll(result);
        }
        return lstActions;
    }
    public static List<Action> getActionbyCode(String id) {
        return new Select()
                .from(Action.class)
                .where("code = ?", id)
                .execute();
    }


    public static HashMap<String,Integer> getNbActionRealiseeGroupByDomaine(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total, domaine FROM " + new Action().getTableName() + " WHERE reste_a_faire=0 GROUP BY domaine", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
               lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static HashMap<String,Integer> getNbActionTotalGroupByDomaine(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total, domaine FROM " + new Action().getTableName() + " GROUP BY domaine", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }


    public static HashMap<String,Integer> getNbActionRealiseeGroupByTypeTravail(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total,typeTravail FROM " + new Action().getTableName() + " WHERE reste_a_faire=0 GROUP BY typeTravail", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static HashMap<String,Integer> getNbActionTotalGroupByTypeTravail(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total, typeTravail FROM " + new Action().getTableName() + " GROUP BY typeTravail", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static List<String> getLstTypeTravail(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT DISTINCT(typeTravail) FROM " + new Action().getTableName(), null);
        List<String> lstResults = new ArrayList<>();

        try {
            while (c.moveToNext()) {
                lstResults.add(c.getString(0));
            }
        } finally {
            c.close();
        }

        return lstResults;
    }


    public static HashMap<String,Integer> getNbActionRealiseeGroupByUtilisateurOeu(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total,resp_oeu FROM " + new Action().getTableName() + " WHERE reste_a_faire=0 GROUP BY resp_oeu", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static HashMap<String,Integer> getNbActionRealiseeGroupByUtilisateurOuv(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total,resp_ouv FROM " + new Action().getTableName() + " WHERE reste_a_faire=0 GROUP BY resp_ouv", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static HashMap<String,Integer> getNbActionTotalGroupByUtilisateurOeu(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total, resp_oeu FROM " + new Action().getTableName() + " GROUP BY resp_oeu", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static HashMap<String,Integer> getNbActionTotalGroupByUtilisateurOuv(){
        Cursor c = ActiveAndroid
                .getDatabase()
                .rawQuery("SELECT COUNT(*) as total, resp_ouv FROM " + new Action().getTableName() + " GROUP BY resp_ouv", null);
        HashMap<String,Integer> lstResult = new HashMap<>();

        try {
            while (c.moveToNext()) {
                lstResult.put(c.getString(1),c.getInt(0));
            }
        } finally {
            c.close();
        }

        return lstResult;
    }

    public static List<Action> getActionRealiseesByProjet(long idProjet){
        Projet proj = Model.load(Projet.class,idProjet);
        List<Domaine> lstDomaines = proj.getLstDomaines();
        List<Action> lstActionRealisees = new ArrayList<>();
        List<Action> lstActionRecuperees = new ArrayList<>();
        for(Domaine d: lstDomaines){
            lstActionRecuperees = new Select()
                    .from(Action.class)
                    .where("reste_a_faire=0 and domaine=?",d.getId())
                    .execute();
            lstActionRealisees.addAll(lstActionRecuperees);
        }
        return lstActionRealisees;
    }

    public static List<Action> getAllActionsByProjet(long idProjet){
        Projet proj = Model.load(Projet.class,idProjet);
        List<Domaine> lstDomaines = proj.getLstDomaines();
        List<Action> lstAction = new ArrayList<>();
        List<Action> lstActionRecuperees = new ArrayList<>();
        for(Domaine d: lstDomaines){
            lstActionRecuperees = new Select()
                    .from(Action.class)
                    .where("domaine=?",d.getId())
                    .orderBy("dt_debut")
                    .execute();
            lstAction.addAll(lstActionRecuperees);
        }
        return lstAction;
    }




}
