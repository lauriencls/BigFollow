package miage.fr.gestionprojet.outils.Pdf;

import android.content.Context;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.models.Action;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.dao.DaoAction;
import miage.fr.gestionprojet.outils.factories.BudgetFactory;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class SuiviBudgetPdf extends InterfacePdf {


    public SuiviBudgetPdf(Projet projet, Context context) throws DocumentException, IOException {
        super(projet, context);
    }

    @Override
    protected void constructPdf() throws DocumentException {
        this.addTitles("Résumé du suivi du budget");
        this.addParagraph("Ce document résume le suivi de budget du projet : " + projet.getNom() + "\n\n");
        BudgetFactory bf = new BudgetFactory();
        List<Integer> actionRea = bf.getListActionRealise(projet.getLstDomaines());
        List<Integer> action = bf.getListAction(projet.getLstDomaines());
        List<String> lstSuiviFormation = new ArrayList<>();
        int index = 0;
        for (Domaine domaine:
                projet.getLstDomaines()) {
            lstSuiviFormation.add("nom : "+domaine.toString()+"\n" +
                    "nb Actions réalisées" + actionRea.get(index)+"/"+action.get(index));
            index++;
        }

        List<String> headers = new ArrayList<String>();
        headers.add("Suivi du budget par domaine");

        this.addTable(headers, lstSuiviFormation);

        this.addParagraph("\n\nRestitution effectuée par " + LoggedUser.getInstance().getInitials());

    }
}
