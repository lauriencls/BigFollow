package miage.fr.gestionprojet.models.dao;

import com.activeandroid.Model;
import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Formation;
import miage.fr.gestionprojet.models.Projet;

/**
 * Created by Romain on 27/04/2017.
 */
public class DaoFormation {

    public static List<Formation> getFormations() {
        return new Select().from(Formation.class).execute();
    }

    public static Formation getFormation(long id) {
        return new Select().from(Formation.class).where("Id = ?", id).executeSingle();
    }

    public static float getAvancementTotal (long idProjet){
        Projet proj = Model.load(Projet.class,idProjet);
        List<Domaine> lstDoms = proj.getLstDomaines();
        List<Action> lstActions = new ArrayList<>();
        for(Domaine d: lstDoms){
            lstActions.addAll(d.getLstActions());
        }
        List<Formation> lstFormation = new ArrayList<>();
        float avancementTotal = 0;
        for(Action a : lstActions){
            if(a.getTypeTravail().equalsIgnoreCase("Formation")){
                Formation form = new Select().from(Formation.class).where("action = ?",a.getId()).executeSingle();
                lstFormation.add(form);
                if(form!=null) {
                    avancementTotal += form.getAvancementTotal();
                }
            }
        }
        if(lstFormation.size()>0){
            avancementTotal /= lstFormation.size();
        }
        return avancementTotal;

    }
    public static List<Formation> loadAll() {
        List<Formation> formations= new Select().from(Formation.class).execute();
        return formations;

    }

}
