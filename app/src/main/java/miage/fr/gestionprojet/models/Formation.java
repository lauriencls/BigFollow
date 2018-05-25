package miage.fr.gestionprojet.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.List;

import miage.fr.gestionprojet.models.dao.DaoMesure;
import miage.fr.gestionprojet.outils.Outils;

/**
 * Created by Audrey on 25/02/2017.
 */

@Table(name = "Formation")
public class Formation extends Model {


    @Column(name="action", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    private Action action;

    @Column(name="avancement_total")
    private float avancementTotal;
    @Column(name="avancement_pre_requis")
    private float avancementPreRequis;
    @Column(name="avancement_objectif")
    private float avancementObjectif;
    @Column(name="avancement_post_formation")
    private float avancementPostFormation;

    private List<EtapeFormation> lstEtapesFormation;

    public Formation() {
        super();
    }

    public List<EtapeFormation> getLstEtapesFormations() {
        this.lstEtapesFormation = getMany(EtapeFormation.class, "formation");
        return this.lstEtapesFormation;
    }

    public float getAvancementTotal() {
        return avancementTotal;
    }

    public void setAvancementTotal(float avancementTotal) {
        this.avancementTotal = avancementTotal;
    }

    public float getAvancementPreRequis() {
        return avancementPreRequis;
    }

    public void setAvancementPreRequis(float avancementPreRequis) {
        this.avancementPreRequis = avancementPreRequis;
    }

    public float getAvancementObjectif() {
        return avancementObjectif;
    }

    public void setAvancementObjectif(float avancementObjectif) {
        this.avancementObjectif = avancementObjectif;
    }

    public float getAvancementPostFormation() {
        return avancementPostFormation;
    }

    public void setAvancementPostFormation(float avancementPostFormation) {
        this.avancementPostFormation = avancementPostFormation;
    }

    public List<EtapeFormation> getLstEtapesFormation() {
        return lstEtapesFormation;
    }

    public void setLstEtapesFormation(List<EtapeFormation> lstEtapesFormation) {
        this.lstEtapesFormation = lstEtapesFormation;
    }
    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }


    public String toPrintable() {
        String vretour = this.getAction().getCode();

        vretour += "\nPhase : "+ this.getAction().getPhase();
        vretour += "\nAvancement total : "+ (int) this.getAvancementTotal() + "%";
        vretour += "\nAvancement pré-requis : "+ (int) this.getAvancementPreRequis() + "%";
        vretour += "\nAvancement Objectif : "+ (int) this.getAvancementObjectif() + "%";
        vretour += "\nAvancement post-formation : "+ (int) this.getAvancementPostFormation() + "%";

        return vretour;
    }
}
