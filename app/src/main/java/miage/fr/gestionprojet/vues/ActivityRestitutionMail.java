package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;

import com.activeandroid.Model;
import com.itextpdf.text.DocumentException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.outils.Pdf.IndicateurDeSaisiesPdf;
import miage.fr.gestionprojet.outils.Pdf.InterfacePdf;
import miage.fr.gestionprojet.outils.Pdf.PlanningDetaillePdf;
import miage.fr.gestionprojet.outils.Pdf.SuiviBudgetPdf;
import miage.fr.gestionprojet.outils.Pdf.SuiviFormationsPdf;
import miage.fr.gestionprojet.outils.factories.MailFactory;

public class ActivityRestitutionMail extends AppCompatActivity {
    CheckBox saisie;
    CheckBox formation;
    CheckBox planning;
    CheckBox budget;
    private Projet proj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restitution_mail);
        saisie = (CheckBox) findViewById(R.id.saisieCharge);
        formation = (CheckBox) findViewById(R.id.formations);
        planning = (CheckBox) findViewById(R.id.planning);
        budget = (CheckBox) findViewById(R.id.budget);

        Intent intent = getIntent();
        //on récupère le projet sélectionné
        long id =  intent.getLongExtra(ActivityDetailsProjet.PROJET,0);

        if (id > 0 ) {
            // on récupère les données associées à ce projet
            proj = Model.load(Projet.class, id);
        }

        Button btnRestit = (Button) findViewById(R.id.btnRestit);
        btnRestit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    List<String> paths = new ArrayList<>();
                     if(saisie.isChecked()) {
                        IndicateurDeSaisiesPdf pdf = new IndicateurDeSaisiesPdf(proj, ActivityRestitutionMail.this);
                        paths.add(pdf.createPdf("saisie.pdf"));

                    }
                    if(formation.isChecked()) {
                        SuiviFormationsPdf formation = new SuiviFormationsPdf(proj, ActivityRestitutionMail.this);
                        paths.add(formation.createPdf("formation.pdf"));
                    }
                    if(planning.isChecked()) {
                        PlanningDetaillePdf planning = new PlanningDetaillePdf(proj, ActivityRestitutionMail.this);
                        paths.add(planning.createPdf("planning.pdf"));
                    }
                    if(budget.isChecked()) {
                        SuiviBudgetPdf budget = new SuiviBudgetPdf(proj, ActivityRestitutionMail.this);
                        paths.add(budget.createPdf("budget.pdf"));
                    }
                    if(paths.size()>0) {
                        MailFactory mf = new MailFactory();
                        mf.sendMailWithAttachment(InterfacePdf.DEST, "Résumé du projet", "Envoyer un email", ActivityRestitutionMail.this, paths);
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
