package miage.fr.gestionprojet.models;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by Rushnak on 10/04/2018.
 */

public class LoggedUser {



    private GoogleSignInAccount currentUser;
    private String initials = "";

    /** Instance unique pré-initialisée */
    private static LoggedUser INSTANCE = new LoggedUser();


    public static LoggedUser getInstance()
    {   return INSTANCE;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    private LoggedUser( )
    {
    }

    public GoogleSignInAccount getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(GoogleSignInAccount currentUser){
        this.currentUser = currentUser;
    }
}
