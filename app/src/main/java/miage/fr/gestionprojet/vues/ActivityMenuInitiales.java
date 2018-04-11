package miage.fr.gestionprojet.vues;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.dao.DaoRessource;

/**
 * Created by Rushnak on 11/04/2018.
 */

public class ActivityMenuInitiales  extends AppCompatActivity  {

    private Button logOutButton;
    private Button modifyButton;
    private EditText initialesEdit;
    private String initialUtilisateur = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_initiales);

        initialUtilisateur = LoggedUser.getInstance().getInitials();
        logOutButton = (Button) findViewById(R.id.log_out_button);
        modifyButton = (Button) findViewById(R.id.initiale_edit_button);
        initialesEdit = (EditText) findViewById(R.id.initiale_edit);

        initialesEdit.setText(LoggedUser.getInstance().getInitials());

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(ActivityMenuInitiales.this,ActivityConnexion.class);
                startActivity(intent);
            }
        });
        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaoRessource daoRessource = new DaoRessource();
                daoRessource.modifyInitiale(initialUtilisateur, initialesEdit.getText().toString());
                LoggedUser.getInstance().setInitials(initialesEdit.getText().toString());
                Toast toast = Toast.makeText(ActivityMenuInitiales.this.getBaseContext(), "Modifications effectuées, elles s'afficheront au prochain démarrage de l'application", Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }



    private void signOut() {
        ActivityConnexion.mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        LoggedUser.getInstance().setCurrentUser(null);
                    }
                });
    }
}
