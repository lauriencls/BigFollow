package miage.fr.gestionprojet.models;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Rushnak on 10/04/2018.
 */

public class LoggedUser {

    GoogleSignInAccount currentUser;

    private LoggedUser( )
    {
    }

    /** Instance unique pré-initialisée */
    private static LoggedUser INSTANCE = new LoggedUser();


    public static LoggedUser getInstance()
    {   return INSTANCE;
    }

    public void setCurrentUser(GoogleSignInAccount currentUser){
        this.currentUser = currentUser;
    }
}
