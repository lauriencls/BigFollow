package miage.fr.gestionprojet.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Audrey on 25/02/2017.
 */

@Table(name="Mesure")
public class Mesure extends Model {

    @Column(name="action", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private SaisieCharge action;

    @Column(name="nb_unites_mesures")
    private int nbUnitesMesures;

    @Column(name="dt_mesure")
    private Date dtMesure;

    public Mesure() {
        super();
    }

    public SaisieCharge getAction() {
        return action;
    }

    public void setAction(SaisieCharge action) {
        this.action = action;
    }

    public int getNbUnitesMesures() {
        return nbUnitesMesures;
    }

    public void setNbUnitesMesures(int nbUnitesMesures) {
        this.nbUnitesMesures = nbUnitesMesures;
    }

    public Date getDtMesure() {
        return dtMesure;
    }

    public void setDtMesure(Date dtMesure) {
        this.dtMesure = dtMesure;
    }
}
