package miage.fr.gestionprojet.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Audrey on 25/02/2017.
 */

@Table(name="Ressource")
public class Ressource extends Model{

    @Column(name="initiales")
    private String initiales;

    @Column(name="prenom")
    private String prenom;

    @Column(name="nom")
    private String nom;

    @Column(name="entreprise")
    private String entreprise;

    @Column(name="fonction")
    private String fonction;

    @Column(name="email")
    private String email;

    @Column(name="telephone_fixe")
    private String telephoneFixe;

    @Column(name="telephone_mobile")
    private String telephoneMobile;

    @Column(name="informations_diverses")
    private String InformationsDiverses;

    public String getInitiales() {
        return initiales;
    }

    public void setInitiales(String initiales) {
        this.initiales = initiales;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(String entreprise) {
        this.entreprise = entreprise;
    }

    public String getFonction() {
        return fonction;
    }

    public void setFonction(String fonction) {
        this.fonction = fonction;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephoneFixe() {
        return telephoneFixe;
    }

    public void setTelephoneFixe(String telephoneFixe) {
        this.telephoneFixe = telephoneFixe;
    }

    public String getTelephoneMobile() {
        return telephoneMobile;
    }

    public void setTelephoneMobile(String telephoneMobile) {
        this.telephoneMobile = telephoneMobile;
    }

    public String getInformationsDiverses() {
        return InformationsDiverses;
    }

    public void setInformationsDiverses(String informationsDiverses) {
        InformationsDiverses = informationsDiverses;
    }

    @Override
    public String toString() {
        return this.initiales + "  " + this.prenom + " " + this.nom;
    }
}
