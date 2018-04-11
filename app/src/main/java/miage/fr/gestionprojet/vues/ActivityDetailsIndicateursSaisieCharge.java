package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.activeandroid.Model;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.List;

import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Mesure;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.models.dao.DaoMesure;
import miage.fr.gestionprojet.outils.Outils;
import miage.fr.gestionprojet.R;

public class ActivityDetailsIndicateursSaisieCharge extends AppCompatActivity {

    private SaisieCharge saisieCharge = null;
    private TextView txtSaisieCharge;
    public static final String EXTRA_SAISIECHARGE = "saisie charge";
    public String initialUtilisateur =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_indicateurs_saisie_charge);

        Intent intent = getIntent();
        long id = intent.getLongExtra(ActivityIndicateursSaisieCharge.SAISIECHARGE,0);
        initialUtilisateur = LoggedUser.getInstance().getInitials();


        if(id > 0){
            saisieCharge = Model.load(SaisieCharge.class, id);
            Mesure mesure = DaoMesure.getLastMesureBySaisieCharge(saisieCharge.getId());
            txtSaisieCharge = (TextView) findViewById(R.id.textViewSaisieCharge);
            txtSaisieCharge.setText(saisieCharge.toString());

            int progression = Outils.calculerPourcentage(mesure.getNbUnitesMesures(),saisieCharge.getNbUnitesCibles());
            CircularProgressBar circularProgressBar = (CircularProgressBar)findViewById(R.id.progressBarAvancement);
            circularProgressBar.setProgress(progression);

            TextView txtPrct = (TextView) findViewById(R.id.textViewPrct);
            txtPrct.setText("Heure/unite:"+saisieCharge.getHeureParUnite()+"\n"+"ChargeTotale:"+saisieCharge.getChargeTotaleEstimeeEnHeure()
            +"\n"+"Charge/semaine:"+saisieCharge.getChargeEstimeeParSemaine());

            TextView txtDateDeb = (TextView) findViewById(R.id.txtDtDeb);
            TextView txtDateFin = (TextView) findViewById(R.id.txtDtFin);
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            txtDateDeb.setText(df.format(saisieCharge.getAction().getDtDeb()));
            txtDateFin.setText(df.format(saisieCharge.getAction().getDtFinPrevue()));

            ProgressBar progressBarDate = (ProgressBar) findViewById(R.id.progressBarDate);
            Calendar c = Calendar.getInstance();
            int progress = Outils.calculerPourcentage(c.getTimeInMillis()-saisieCharge.getAction().getDtDeb().getTime(),saisieCharge.getAction().getDtFinPrevue().getTime()-saisieCharge.getAction().getDtDeb().getTime());
            progressBarDate.setProgress(progress);

            ListView lstViewIndicateur = (ListView) findViewById(R.id.ListViewDetailsSaisieCharge);
            List<String> indicateurs = new ArrayList<>();
            indicateurs.add("Nombre d'unités produites:"+mesure.getNbUnitesMesures()+"/"+saisieCharge.getNbUnitesCibles());
            indicateurs.add("Temps restant (semaines): "+saisieCharge.getNbSemainesRestantes());
            indicateurs.add("Dernière mesure saisie:"+mesure.getDtMesure());
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, indicateurs);
            lstViewIndicateur.setAdapter(adapter);

            Button btnMessures = (Button) findViewById(R.id.btnMesures);
            btnMessures.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ActivityDetailsIndicateursSaisieCharge.this, ActivityMesures.class);
                    intent.putExtra(EXTRA_SAISIECHARGE, saisieCharge.getId());
                    startActivity(intent);
                }
            });

        }


    }

    //ajout du menu
    @Override
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
                Intent intent = new Intent(ActivityDetailsIndicateursSaisieCharge.this, ChargementDonnees.class);
                startActivity(intent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
