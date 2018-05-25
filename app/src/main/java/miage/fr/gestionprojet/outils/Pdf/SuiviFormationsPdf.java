package miage.fr.gestionprojet.outils.Pdf;

import android.content.Context;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.models.Formation;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.models.dao.DaoFormation;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class SuiviFormationsPdf extends InterfacePdf {


    public SuiviFormationsPdf(Projet projet, Context context) throws DocumentException, IOException {
        super(projet, context);
    }

    @Override
    protected void constructPdf() throws DocumentException {
        this.addTitles("Résumé du suivi des formations");
        this.addParagraph("Ce document résume le suivi des formations du projet : " + projet.getNom() + "\n\n");
        List<String> lstSuiviFormation = new ArrayList<>();
        for (Formation formation:
                DaoFormation.getFormations()) {
            lstSuiviFormation.add(formation.toPrintable());
        }

        List<String> headers = new ArrayList<String>();
        headers.add("Liste des formations");

        this.addTable(headers, lstSuiviFormation);

        this.addParagraph("\n\nRestitution effectuée par " + LoggedUser.getInstance().getInitials());

    }
}
