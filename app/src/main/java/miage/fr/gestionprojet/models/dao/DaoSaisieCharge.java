package miage.fr.gestionprojet.models.dao;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Mesure;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.SaisieCharge;

/**
 * Created by Audrey on 07/04/2017.
 */

public class DaoSaisieCharge {


    public static List<SaisieCharge> loadSaisiebyAction(Action action) {
        List<SaisieCharge> saisieCharge= new Select().from(SaisieCharge.class).where("action=?",action.getId()).execute();
        return saisieCharge;

    }
    public static List<SaisieCharge> loadAll() {
        List<SaisieCharge> saisieCharge= new Select().from(SaisieCharge.class).execute();
        return saisieCharge;

    }

    public static List<SaisieCharge> loadSaisieChargesByDomaine(int idDomaine){
        List<SaisieCharge> lst = new ArrayList<>();
        List<Action> results = new Select()
                .from(Action.class)
                .where("domaine=?",idDomaine)
                .execute();
        for(Action a : results) {
            if(a.getTypeTravail().equalsIgnoreCase("Saisie")||a.getTypeTravail().equalsIgnoreCase("Test")) {
                SaisieCharge result = (SaisieCharge) new Select()
                        .from(SaisieCharge.class)
                        .where("domaine=?", a.getId())
                        .execute().get(0);
                lst.add(result);
            }

        }
        return lst;
    }

    public static List<SaisieCharge> loadSaisieChargeByUtilisateur(int idUser){


        List<SaisieCharge> lst = new ArrayList<>();
        List<Action> results = new Select()
                .from(Action.class)
                .where("resp_ouv=? or resp_oeu=?",idUser,idUser)
                .execute();
        for(Action a : results) {
            if(a.getTypeTravail().equalsIgnoreCase("Saisie")||a.getTypeTravail().equalsIgnoreCase("Test")) {
                SaisieCharge result = (SaisieCharge) new Select()
                        .from(SaisieCharge.class)
                        .where("domaine=?", a.getId())
                        .execute().get(0);
                lst.add(result);
            }

        }
        return lst;
    }

    public static SaisieCharge loadSaisieChargeByAction(long idAction){
        List<SaisieCharge> lst = new Select()
                .from(SaisieCharge.class)
                .where("action = ?", idAction)
                .execute();
        if(lst.size()>0) {
            return lst.get(0);
        }else{
            return null;
        }
    }

    public static int getNbUnitesSaisies(long idProjet){
        Projet projet = Model.load(Projet.class, idProjet);
        List<Domaine> doms = projet.getLstDomaines();
        List<Action> lstActions = new ArrayList<>();
        for(Domaine d : doms){
            lstActions.addAll(d.getLstActions());
        }
        int nbUnitesSaisies = 0;
        for(Action a : lstActions){
            if(a.getTypeTravail().equalsIgnoreCase("Saisie")||a.getTypeTravail().equalsIgnoreCase("Test")){
                SaisieCharge s = DaoSaisieCharge.loadSaisieChargeByAction(a.getId());
                if(s!=null) {
                    Mesure m = DaoMesure.getLastMesureBySaisieCharge(s.getId());
                    nbUnitesSaisies += m.getNbUnitesMesures();
                }
            }
        }
        return nbUnitesSaisies;
    }

    public static int getNbUnitesCibles(long idProjet){
        Projet projet = Model.load(Projet.class, idProjet);
        List<Domaine> doms = projet.getLstDomaines();
        List<Action> lstActions = new ArrayList<>();
        for(Domaine d : doms){
            lstActions.addAll(d.getLstActions());
        }
        int nbUnitesCibles = 0;
        for(Action a : lstActions){
            if(a.getTypeTravail().equalsIgnoreCase("Saisie")||a.getTypeTravail().equalsIgnoreCase("Test")){
                SaisieCharge s = DaoSaisieCharge.loadSaisieChargeByAction(a.getId());
                if(s!=null) {
                    nbUnitesCibles += s.getNbUnitesCibles();
                }
            }
        }
        return nbUnitesCibles;
    }

}
