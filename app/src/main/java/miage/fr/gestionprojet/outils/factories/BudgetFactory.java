package miage.fr.gestionprojet.outils.factories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.dao.DaoAction;

/**
 * Created by robin_delaporte on 25/05/2018.
 */

public class BudgetFactory {
    public List<Integer> getListActionRealise(List<Domaine> lstDomaines){

        List<Integer> lstNbActionsRealisees = new ArrayList<>();
        HashMap<String, Integer> results= DaoAction.getNbActionRealiseeGroupByDomaine();
        if(results.size()>0){
            for(Domaine d : lstDomaines){
                if(results.get(String.valueOf(d.getId()))!=null) {
                    lstNbActionsRealisees.add(results.get(String.valueOf(d.getId())));
                }else{
                    lstNbActionsRealisees.add(0);
                }
            }

        }

        return lstNbActionsRealisees;
    }

    public List<Integer> getListAction(List<Domaine> lstDomaines){
        List<Integer> lstNbActions = new ArrayList<>();
        HashMap<String, Integer> results= DaoAction.getNbActionTotalGroupByDomaine();
        if(results.size()>0){
            for(Domaine d : lstDomaines){
                if(results.get(String.valueOf(d.getId()))!=null) {
                    lstNbActions.add(results.get(String.valueOf(d.getId())));
                }else{
                    lstNbActions.add(0);
                }
            }

        }
        return lstNbActions;
    }
}
