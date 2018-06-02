package miage.fr.gestionprojet.vues;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.model.people.PersonBuffer;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import miage.fr.gestionprojet.R;
import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.outils.UpdaterTask;

public class ActivityConnexion extends AppCompatActivity  implements View.OnClickListener, ResultCallback<People.LoadPeopleResult>{
    SignInButton signIn_btn;
    Button signOut_btn;
    private static final int RC_SIGN_IN = 0;
    ProgressDialog progress_dialog;
    public static GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        //Customize sign-in button.a red button may be displayed when Google+ scopes are requested
        signIn_btn = (SignInButton) findViewById(R.id.sign_in_button);
        signOut_btn = (Button) findViewById(R.id.next_button);
        signIn_btn.setOnClickListener(this);
        signOut_btn.setOnClickListener(this);
        progress_dialog = new ProgressDialog(this);
        progress_dialog.setMessage("Chargement....");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        signIn();
    }




    protected void onStart() {
        super.onStart();
    }

    /*
      Will receive the activity result and check which request we are responding to
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.next_button:
                Intent intent = new Intent(ActivityConnexion.this,ActivityGestionDesInitials.class);
                startActivity(intent);
                break;
        }
    }





    /*
     Show and hide of the Views according to the user login status
     */


    @Override
    public void onResult(People.LoadPeopleResult peopleData) {
        if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            List<String> list = new ArrayList<String>();
            List<String> img_list= new ArrayList<String>();
            try {
                int count = personBuffer.getCount();

                for (int i = 0; i < count; i++) {
                    list.add(personBuffer.get(i).getDisplayName());
                    img_list.add(personBuffer.get(i).getImage().getUrl());
                }

            } finally {
                personBuffer.release();
            }
        } else {
            Log.e("circle error", "Error requesting visible circles: " + peopleData.getStatus());
        }
    }





    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            LoggedUser.getInstance().setCurrentUser(account);

            signIn_btn.setVisibility(View.GONE);
            signOut_btn.setVisibility(View.VISIBLE);
            Intent intent = new Intent(ActivityConnexion.this,ActivityGestionDesInitials.class);
            startActivity(intent);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("ERROR", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

}
