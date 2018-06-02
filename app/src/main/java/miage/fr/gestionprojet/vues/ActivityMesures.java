package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.activeandroid.Model;

import java.util.List;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.adapter.AdapterBudgetType;
import miage.fr.gestionprojet.adapter.AdapterMesure;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Mesure;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.models.dao.DaoMesure;

public class ActivityMesures extends AppCompatActivity {
    public static final String EXTRA_SAISIECHARGE = "saisie charge";
    public String initialUtilisateur =null;
    private SaisieCharge saisieCharge = null;
    MenuItem addMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesures);
        Intent intent = getIntent();
        long id = intent.getLongExtra(ActivityIndicateursSaisieCharge.SAISIECHARGE,0);
        initialUtilisateur = LoggedUser.getInstance().getInitials();


        if(id > 0) {
            saisieCharge = Model.load(SaisieCharge.class, id);
            ListView lstViewMesures = (ListView) findViewById(R.id.lstViewMesures);
            List<Mesure> lstMesures = DaoMesure.getListtMesureByAction(id);
            final AdapterMesure adapter = new AdapterMesure(this, R.layout.lst_view_mesures, lstMesures);
            lstViewMesures.setAdapter(adapter);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.initial_utilisateur, menu);
        menu.findItem(R.id.initial_utilisateur).setTitle(initialUtilisateur);
        addMenuItem = menu.findItem(R.id.add);
        addMenuItem.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.initial_utilisateur:
                intent = new Intent(ActivityMesures.this, ActivityMenuInitiales.class);
                startActivity(intent);
                return true;
            case R.id.charger_donnees:
                intent = new Intent(ActivityMesures.this, ChargementDonnees.class);
                startActivity(intent);
                return true;
            case R.id.add:
                intent = new Intent(this, ActivitySaisieMesure.class);
                intent.putExtra(EXTRA_SAISIECHARGE, saisieCharge.getId());
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
