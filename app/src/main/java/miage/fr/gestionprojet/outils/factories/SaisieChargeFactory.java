package miage.fr.gestionprojet.outils.factories;

import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.models.dao.DaoSaisieCharge;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class SaisieChargeFactory {
    private Projet projet;

    public SaisieChargeFactory(Projet projet) {
        this.projet = projet;
    }

    public List<SaisieCharge> getSaisiesCharge(){
        List<SaisieCharge> lstSaisieCharge = new ArrayList<>();
        List<Domaine> lstDomaines = projet.getLstDomaines();
        for(Domaine d : lstDomaines){
            for(Action a: d.getLstActions()){
                if(a.getTypeTravail().equals("Saisie")||a.getTypeTravail().equals("Test")){
                    SaisieCharge s = DaoSaisieCharge.loadSaisieChargeByAction(a.getId());
                    if(s!=null){
                        lstSaisieCharge.add(s);
                    }
                }
            }
        }
        return lstSaisieCharge;
    }
}
