package miage.fr.gestionprojet.models.dao;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.models.Ressource;

/**
 * Created by gamouzou on 01/03/2017.
 */

public class DaoRessource {


    public static List<Ressource> loadAll(){
        return new Select()
                .from(Ressource.class)
                .execute();
    }

    public  List<Ressource> loadAllWithInitialNotEmpty(){

        List<Ressource> listeRessource=loadAll();
        List<Ressource> listeRessourceFinal=new ArrayList<>();

        for ( int i=0; i < listeRessource.size();i++){
            if (!listeRessource.get(i).getInitiales().equals("") && listeRessource.get(i).getInitiales().length()>0) {
                listeRessourceFinal.add(listeRessource.get(i));
            }
        }
        return listeRessourceFinal;
    }

    public List<String> getAllRessourceInitials(){
        List<Ressource> listeRessource=new Select().from(Ressource.class).execute();
        List<String> listeInitials=new ArrayList<>();

        for ( int i=0; i < listeRessource.size();i++){
            if (!listeRessource.get(i).getInitiales().equals("") && listeRessource.get(i).getInitiales().length()>0) {
                listeInitials.add(listeRessource.get(i).getInitiales());
            }
        }
        return listeInitials;
    }

    public static Ressource getRessourceByInitial(String initiales){
        List<Ressource> lst = new Select()
                .from(Ressource.class)
                .where("initiales = ?", initiales)
                .execute();
        if(lst.size()>0){
            return lst.get(0);
        }else{
            return null;
        }
    }

    public void modifyInitiale(String baseInitiales, String newInitiales){
        new Update(Ressource.class)
                .set("initiales = ?", newInitiales)
                .where("initiales = ?", baseInitiales)
                .execute();
    }
}
