package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.activeandroid.Model;

import java.util.Date;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Mesure;
import miage.fr.gestionprojet.models.SaisieCharge;
import miage.fr.gestionprojet.outils.Outils;
import miage.fr.gestionprojet.outils.UpdaterTask;

public class ActivitySaisieMesure extends AppCompatActivity {
    Button addSaisieBtn;
    EditText nbSaisieEditText;
    TextView errorTextView;

    SaisieCharge saisieCharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saisie_mesure);

        addSaisieBtn = findViewById(R.id.addSaisie);
        addSaisieBtn.setOnClickListener(onAddSaisieClicked());

        nbSaisieEditText = findViewById(R.id.nbSaisieMesure);

        errorTextView = findViewById(R.id.errorFormat);

        Intent intent = getIntent();
        long id = intent.getLongExtra(ActivityIndicateursSaisieCharge.SAISIECHARGE,0);

        if(id > 0) {
            saisieCharge = Model.load(SaisieCharge.class, id);
        }
    }

    @NonNull
    private View.OnClickListener onAddSaisieClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nbSaisie = Outils.toInteger(nbSaisieEditText.getText().toString());
                if (nbSaisie > -1  && nbSaisie <= saisieCharge.getNbUnitesCibles()){
                    Mesure mesure= new Mesure();
                    mesure.setAction(saisieCharge);
                    mesure.setDtMesure(new Date());
                    mesure.setNbUnitesMesures(nbSaisie);

                    new UpdaterTask(ActivitySaisieMesure.this, "18OGZnKyjQKxSLgI2DWDUspvgLMh7ooVVVQXxMkw-w2g", mesure).execute();

                    mesure.save();
                    finish();
                }else{
                    errorTextView.setText("Veuillez saisir un chiffre entre 0 et "+saisieCharge.getNbUnitesCibles()+".");
                    errorTextView.setVisibility(View.VISIBLE);
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.initial_utilisateur:
                intent = new Intent(ActivitySaisieMesure.this, ActivityMenuInitiales.class);
                startActivity(intent);
                return true;
            case R.id.charger_donnees:
                intent = new Intent(ActivitySaisieMesure.this, ChargementDonnees.class);
                startActivity(intent);
                return true;
            case R.id.add:
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
}
