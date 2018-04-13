package miage.fr.gestionprojet.outils.Pdf;

import android.content.Context;

import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.outils.factories.SaisieChargeFactory;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class IndicateurDeSaisiesPdf extends InterfacePdf {


    public IndicateurDeSaisiesPdf(Projet projet, Context context) throws DocumentException, IOException {
        super(projet, context);
    }

    @Override
    protected void constructPdf() throws DocumentException {
        this.addTitles("Résumé de l'avancée des saisies de charge");
        this.addParagraph("Ce document résume l'avancée de la saisie des charges du projet : " + projet.getNom() + "\n\n");

        List<String> lstSaisieCharge = new ArrayList<>();
        for (SaisieCharge saisieCharge:
                new SaisieChargeFactory(projet).getSaisiesCharge()) {
            lstSaisieCharge.add(saisieCharge.toPrintable());
        }
        List<String> headers = new ArrayList<String>();
        headers.add("Liste des saisies");

        this.addTable(headers, lstSaisieCharge);
    }
}
