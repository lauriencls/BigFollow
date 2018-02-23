package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.activeandroid.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.adapter.AdapterBudgetDomaine;
import miage.fr.gestionprojet.adapter.AdapterBudgetType;
import miage.fr.gestionprojet.adapter.AdapterBudgetUtilisateur;
import miage.fr.gestionprojet.models.Domaine;
import miage.fr.gestionprojet.models.Projet;
import miage.fr.gestionprojet.models.Ressource;
import miage.fr.gestionprojet.models.dao.DaoAction;
import miage.fr.gestionprojet.models.dao.DaoDomaine;
import miage.fr.gestionprojet.models.dao.DaoRessource;

public class ActivityBudget extends AppCompatActivity {
    private Spinner spinChoixAffichage;
    private ListView liste;
    private List<String> lstChoixAffichage;
    private String initialUtilisateur;
    private Projet proj;

    private final static String DOMAINE = "Domaine";
    private final static String TYPE = "Type";
    private final static String UTILISATEUR = "Utilisateur";
    public final static String EXTRA_INITIAL = "initial";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        Intent intentInitial = getIntent();
        initialUtilisateur = intentInitial.getStringExtra(EXTRA_INITIAL);
        lstChoixAffichage = new ArrayList<>();
        lstChoixAffichage.add(DOMAINE);
        lstChoixAffichage.add(TYPE);
        lstChoixAffichage.add(UTILISATEUR);

        spinChoixAffichage = (Spinner) findViewById(R.id.spinnerChoixAffichage);
        this.liste = (ListView) findViewById(R.id.lstViewBudget);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lstChoixAffichage);
        spinChoixAffichage.setAdapter(adapter);
        //on récupère le projet sélectionné
        Intent intent = getIntent();
        long id =  intent.getLongExtra(ActivityDetailsProjet.PROJET,0);
        if(id>0){
            proj = Model.load(Projet.class, id);
        }else{
            proj = new Projet();
        }
        spinChoixAffichage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String valeurSelectionne = lstChoixAffichage.get(i);
                switch(valeurSelectionne){
                    case DOMAINE:
                        AffichageDomaine();
                        break;
                    case TYPE:
                        AffichageType();
                        break;
                    case UTILISATEUR:
                        AffichageUtilisateur();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void AffichageDomaine(){
        List<Domaine> lstDomaines = proj.getLstDomaines();
        AdapterBudgetDomaine adapter = new AdapterBudgetDomaine(ActivityBudget.this,R.layout.lst_view_budget,lstDomaines);
        this.liste.setAdapter(adapter);
    }

    private void AffichageType(){
        List<String> lstTypes = DaoAction.getLstTypeTravail();
        AdapterBudgetType adapter = new AdapterBudgetType(ActivityBudget.this,R.layout.lst_view_budget,lstTypes);
        this.liste.setAdapter(adapter);
    }

    private void AffichageUtilisateur(){
        List<Ressource> lstUtilisateurs = DaoRessource.loadAll();
        AdapterBudgetUtilisateur adapter = new AdapterBudgetUtilisateur(ActivityBudget.this,R.layout.lst_view_budget,lstUtilisateurs);
        this.liste.setAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.initial_utilisateur, menu);
        menu.findItem(R.id.initial_utilisateur).setTitle(initialUtilisateur);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.initial_utilisateur:
                return true;
            case R.id.charger_donnees:
                Intent intent = new Intent(ActivityBudget.this, ChargementDonnees.class);
                intent.putExtra(EXTRA_INITIAL, (initialUtilisateur));
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
