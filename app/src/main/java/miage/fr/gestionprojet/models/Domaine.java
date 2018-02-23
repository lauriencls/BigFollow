package miage.fr.gestionprojet.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;
import java.util.List;
import java.util.List;

/**
 * Created by Audrey on 23/01/2017.
 */

@Table(name="Domaine")
public class Domaine extends Model {

    @Column(name="nom")
    private String nom;

    @Column(name="description")
    private String description;

    @Column(name="projet", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Projet projet;

    private List<Action> lstActions;

    private List<SaisieCharge> lstSaisieCharge;

    public Domaine(String nom, String description, Projet projet) {
        super();
        this.nom = nom;
        this.description = description;
        this.projet = projet;

    }

    public Domaine() {
        super();
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Projet getProjet() {
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public List<Action> getLstActions() {
        this.lstActions = getMany(Action.class, "domaine");
        return this.lstActions;
    }


    @Override
    public String toString() {
        return this.nom.toUpperCase();
    }
}




