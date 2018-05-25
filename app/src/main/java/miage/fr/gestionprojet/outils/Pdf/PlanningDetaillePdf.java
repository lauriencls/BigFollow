package miage.fr.gestionprojet.outils.Pdf;

import android.content.Context;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Formation;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.dao.DaoAction;
import miage.fr.gestionprojet.models.dao.DaoFormation;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class PlanningDetaillePdf extends InterfacePdf {


    public PlanningDetaillePdf(Projet projet, Context context) throws DocumentException, IOException {
        super(projet, context);
    }

    @Override
    protected void constructPdf() throws DocumentException {
        this.addTitles("Résumé du planning détaillé");
        this.addParagraph("Ce document rend compte du planning détaillé du projet : " + projet.getNom() + "\n\n");
        List<String> lstSuiviFormation = new ArrayList<>();
        for (Action action:
                DaoAction.getAllActionsByProjet(projet.getId())) {
            lstSuiviFormation.add(action.toPrintable());
        }

        List<String> headers = new ArrayList<String>();
        headers.add("Planning détaillé");

        this.addTable(headers, lstSuiviFormation);

        this.addParagraph("\n\nRestitution effectuée par " + LoggedUser.getInstance().getInitials());

    }
}
