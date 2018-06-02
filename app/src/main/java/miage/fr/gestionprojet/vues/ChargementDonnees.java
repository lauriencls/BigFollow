
      package miage.fr.gestionprojet.vues;

        import com.activeandroid.ActiveAndroid;
        import com.activeandroid.query.Delete;
        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.GoogleApiAvailability;
        import com.google.api.client.extensions.android.http.AndroidHttp;
        import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
        import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
        import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;

        import com.google.api.client.http.HttpTransport;
        import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.jackson2.JacksonFactory;
        import com.google.api.client.util.ExponentialBackOff;

        import com.google.api.services.sheets.v4.Sheets;
        import com.google.api.services.sheets.v4.SheetsScopes;

        import com.google.api.services.sheets.v4.model.*;

        import android.Manifest;
        import android.accounts.AccountManager;
        import android.app.Activity;
        import android.app.Dialog;
        import android.app.ProgressDialog;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.support.annotation.NonNull;
        import android.text.TextUtils;
        import android.text.method.ScrollingMovementMethod;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.IOException;
        import java.text.ParseException;
        import java.text.SimpleDateFormat;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Arrays;
        import java.util.Date;

        import miage.fr.gestionprojet.models.Formation;
        import miage.fr.gestionprojet.models.Action;
        import miage.fr.gestionprojet.models.Domaine;
        import miage.fr.gestionprojet.models.LoggedUser;
        import miage.fr.gestionprojet.models.Mesure;
        import miage.fr.gestionprojet.models.Projet;
        import miage.fr.gestionprojet.models.Ressource;
        import miage.fr.gestionprojet.models.SaisieCharge;
        import miage.fr.gestionprojet.models.dao.DaoAction;

        import miage.fr.gestionprojet.models.dao.DaoDomaine;
        import miage.fr.gestionprojet.models.dao.DaoProjet;
        import miage.fr.gestionprojet.models.dao.DaoRessource;
        import miage.fr.gestionprojet.models.dao.DaoSaisieCharge;
        import miage.fr.gestionprojet.outils.Outils;
        import pub.devrel.easypermissions.AfterPermissionGranted;
        import pub.devrel.easypermissions.EasyPermissions;

public class ChargementDonnees extends Activity implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    private TextView mOutputText;
    private Button mCallApiButton;
    ProgressDialog mProgress;
    private Button idButtonParDefaut;
    private EditText buttonInput;

    private static String spreadsheetId ;
    public static String spreadsheetIdParDefaut= "18OGZnKyjQKxSLgI2DWDUspvgLMh7ooVVVQXxMkw-w2g";

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;


    private static final String BUTTON_TEXT = "Charger la base de données ";
    private static final String BUTTON_ID = "Id par defaut";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS_READONLY};

    /**
     * Create the main activity.
     *
     * @param savedInstanceState previously saved instance data.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout activityLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        activityLayout.setLayoutParams(lp);
        activityLayout.setOrientation(LinearLayout.VERTICAL);
        activityLayout.setPadding(16, 16, 16, 16);

        ViewGroup.LayoutParams tlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mCallApiButton = new Button(this);
        mCallApiButton.setText(BUTTON_TEXT);
        mCallApiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallApiButton.setEnabled(false);
                mOutputText.setText("");
                getResultsFromApi();
                mCallApiButton.setEnabled(true);
            }
        });
        activityLayout.addView(mCallApiButton);

        mOutputText = new TextView(this);
        mOutputText.setLayoutParams(tlp);
        mOutputText.setPadding(16, 16, 16, 16);
        mOutputText.setVerticalScrollBarEnabled(true);
        mOutputText.setMovementMethod(new ScrollingMovementMethod());
        mOutputText.setText(
                "Clicker sur \'" + BUTTON_TEXT + "\' pour charger ou mettre à jour les données .");
        activityLayout.addView(mOutputText);

        buttonInput=new EditText(this);
        activityLayout.addView(buttonInput);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("préparation de la base de données  ...");
        idButtonParDefaut = new Button(this);
        idButtonParDefaut.setText(BUTTON_ID);
        idButtonParDefaut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getIdProjetParDefaut();
            }


            private void getIdProjetParDefaut() {
                buttonInput.setText(spreadsheetIdParDefaut);
            }
        });
        activityLayout.addView(idButtonParDefaut);

        setContentView(activityLayout);

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        boolean projetIdVide=true;

        if (buttonInput.length()>0){
            projetIdVide=false;
        };

        if(!projetIdVide) {
            spreadsheetId=buttonInput.getText().toString();
            if (!isGooglePlayServicesAvailable()) {
                acquireGooglePlayServices();
            } else if (mCredential.getSelectedAccountName() == null) {
                chooseAccount();
            } else if (!isDeviceOnline()) {
                mOutputText.setText("No network connection available.");
            } else {
                new MakeRequestTask(mCredential).execute();
            }
        }else{
            Toast.makeText(this, "Renseignez Id du projet", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     *
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode  code indicating the result of the incoming
     *                    activity result.
     * @param data        Intent (containing result data) returned by incoming
     *                    activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     *
     * @param requestCode  The request code passed in
     *                     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *                     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     *
     * @param requestCode The request code associated with the requested
     *                    permission
     * @param list        The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     *
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     *
     * @return true if Google Play Services is available and up to
     * date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     *
     * @param connectionStatusCode code describing the presence (or lack of)
     *                             Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                ChargementDonnees.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    public class MakeRequestTask extends AsyncTask<Void, Void, List<String>> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Big Follow")
                    .build();

        }

        /**
         * Background task to call Google Sheets API.
         *
         * @param params no parameters needed for this task.
         */
        @Override
        protected List<String> doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1yw_8OO4oFYR6Q25KH0KE4LOr86UfwoNl_E6hGgq2UD4/edit
         *
         * @return List of names and majors
         * @throws IOException
         */
        private List<String> getDataFromApi() throws IOException, ParseException {
            /*tables des feuilles à parcourir
            HashMap<String,String>feuilles= new HashMap<>();
            feuilles.put("rangeActions","Liste des actions projet!A3:Z");
            feuilles.put("rangeRessources","Ressources!A2:Z");
            */
            String spreadsheetId = buttonInput.getText().toString();
            Outils.savePreference("spreadSheetId", spreadsheetId, getBaseContext());
            String rangeProject = "Informations générales!A2:E";
            String rangeActions = "Liste des actions projet!A3:Z";
            String rangeDcConso = "DC et détails conso!A5:Z";
            String rangeSaisieCharge = "Indicateurs de saisie/charge!A5:Z";

            String rangeRessources = "Ressources!A2:Z";
            String rangeformation = "Indicateurs formation!A3:Z";
            String rangeMesure = "Mesures de saisie/charge!A2:D";
            ValueRange reponsesmesure = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeMesure)
                    .execute();
            List<String> results = new ArrayList<String>();
            ValueRange responseproject = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeProject)
                    .execute();
            mProgress.setProgress(Outils.calculerPourcentage(0, 7));
            ValueRange responseAction = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeActions)
                    .execute();
            mProgress.setProgress(Outils.calculerPourcentage(1, 7));
            ValueRange responseDcConso = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeDcConso)
                    .execute();
            mProgress.setProgress(Outils.calculerPourcentage(2, 7));
            ValueRange responseressources = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeRessources)
                    .execute();
            ValueRange responsesaisieCharge = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeSaisieCharge)
                    .execute();
            mProgress.setProgress(Outils.calculerPourcentage(3, 7));
            ValueRange responseformation = this.mService.spreadsheets().values()
                    .get(spreadsheetId, rangeformation)
                    .execute();
            mProgress.setProgress(Outils.calculerPourcentage(4, 7));
            List<List<Object>> values = responseAction.getValues();
            List<List<Object>> valueproject = responseproject.getValues();
            List<List<Object>> valuesSaisieCharge = responsesaisieCharge.getValues();
            List<List<Object>> valuesDcConso = responseDcConso.getValues();
            List<List<Object>> valuesMEsure = reponsesmesure.getValues();
            mProgress.setProgress(Outils.calculerPourcentage(5, 7));
            List<List<Object>> valuesressources = responseressources.getValues();
            if (valueproject != null) {
                initialiserPojet(valueproject);
            }
            if (valuesressources != null) {
                initialiserressource(reglerDonnees(valuesressources));


            }
            mProgress.setProgress(Outils.calculerPourcentage(6, 7));
            if (values != null && valuesDcConso != null) {


                initialiserAction(reglerDonnees(values), reglerDonnees(valuesDcConso));

            }

            mProgress.setProgress(Outils.calculerPourcentage(7, 7));
            List<List<Object>> valuesformation = responseformation.getValues();
            if (valuesformation != null) {
                intialiserFormation(reglerDonnees(valuesformation));


            }

            if (valuesSaisieCharge != null) {
                initialiserSaisieCharge(reglerDonnees(valuesSaisieCharge));
            }
            if (valuesMEsure != null) {
                initialiserMesures(reglerDonnees(valuesMEsure));
            }
            for (List row : valuesformation) {


            }
            return results;
        }

        /*
            homogéner les données
         */
        public List<List<Object>> reglerDonnees(List<List<Object>> values) {
            for (List row : values) {
                int indexe = 26 - row.size();
                for (int i = 0; i < indexe; i++) {
                    row.add("");

                }

            }
            return values;
        }


        public Date chainetoDate(String s) throws ParseException {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date resultat;
            if (s.equals("") || s.equals(null) || s.equals("NON PREVU")) {
                resultat = sdf.parse("00/00/0000");

            } else {
                resultat = sdf.parse(s);
            }
            ;
            return resultat;
        }


        public Boolean chainetoBoolean(String s) {
            Boolean resultat;
            if (s.equals("1")) {
                resultat = true;

            } else {
                resultat = false;
            }

            return resultat;
        }

        public int chainetoint(String s) {
            int resultat;
            boolean flag = s.matches("%[a-zA-Z]%");
            if (s.equals("") || (flag)) {
                resultat = 0;

            } else {
                resultat = new Integer(s);
            }
            ;
            return resultat;
        }

        public Float chainetofloat(String s) {
            Float resultat;

            boolean flag = s.matches(".*[a-zA-Z]+.*");
            if (s.equals("") || (s == null) || s.equals("-") || flag || s.equals("RETARD") || s.equals("#DIV/0!")) {
                resultat = (float)0.0;

            } else {

                resultat = Float.parseFloat(s.replace(',', '.'));
            }
            ;
            return resultat;
        }

        public void initialiserressource(List<List<Object>> values) {
            new Delete().from(Ressource.class).execute();

            Ressource resource = new Ressource();
            resource.setNom("");
            resource.setEmail("");
            resource.setEntreprise("");
            resource.setFonction("");
            resource.setInformationsDiverses("");
            resource.setInitiales("");
            resource.setPrenom("");
            resource.setTelephoneFixe("");
            resource.setTelephoneMobile("");
            resource.save();
            ActiveAndroid.beginTransaction();
            try {
                for (List row : values) {
                    resource.setNom(row.get(2).toString());
                    resource.setEmail(row.get(5).toString());
                    resource.setEntreprise(row.get(3).toString());
                    resource.setFonction(row.get(4).toString());
                    resource.setInformationsDiverses(row.get(8).toString());
                    resource.setInitiales(row.get(0).toString());
                    resource.setPrenom(row.get(1).toString());
                    resource.setTelephoneFixe(row.get(6).toString());
                    resource.setTelephoneMobile(row.get(7).toString());
                    resource.save();

                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

        }

        public void initialiserAction(List<List<Object>> values, List<List<Object>> valuesDcConso) throws ParseException {
            new Delete().from(Action.class).execute();
            new Delete().from(Domaine.class).execute();

            /*
             */
            Projet projet = DaoProjet.loadAll().get(0);
            /*
             */
            ActiveAndroid.beginTransaction();
            try {
                for (List row : values) {
                    Action action = new Action();
                    action.setCode(row.get(5).toString());
                    action.setOrdre(chainetoint(row.get(1).toString()));
                    action.setTarif(row.get(2).toString());

                    action.setTypeTravail(row.get(0).toString());
                    action.setPhase(row.get(4).toString());
                    action.setCode(row.get(5).toString());

                    Domaine domaine = DaoDomaine.getByName(row.get(3).toString());
                    if (domaine == null) {
                        domaine = new Domaine(row.get(3).toString(), "description demo", projet);
                        domaine.save();
                    }
                    Ressource respOuv;
                    if (row.get(13).toString() == null || row.get(13).toString().length() == 0) {
                        respOuv = new Ressource();
                        respOuv.setInitiales("");
                    }
                    respOuv = DaoRessource.getRessourceByInitial(row.get(13).toString());
                    if (respOuv == null) {
                        respOuv = new Ressource();
                        respOuv.setInitiales(row.get(13).toString());
                        respOuv.setNom("");
                        respOuv.setEmail("");
                        respOuv.setEntreprise("");
                        respOuv.setFonction("");
                        respOuv.setInformationsDiverses("");
                        respOuv.setPrenom("");
                        respOuv.setTelephoneFixe("");
                        respOuv.setTelephoneMobile("");
                        respOuv.save();
                    }
                    action.setRespOuv(respOuv);
                    Ressource respOeu;
                    if (row.get(12).toString() == null || row.get(12).toString().length() == 0) {
                        respOeu = new Ressource();
                        respOeu.setInitiales("");
                    }
                    respOeu = DaoRessource.getRessourceByInitial(row.get(12).toString());
                    if (respOeu == null) {
                        respOeu = new Ressource();
                        respOeu.setInitiales(row.get(12).toString());
                        respOeu.setNom("");
                        respOeu.setEmail("");
                        respOeu.setEntreprise("");
                        respOeu.setFonction("");
                        respOeu.setInformationsDiverses("");
                        respOeu.setPrenom("");
                        respOeu.setTelephoneFixe("");
                        respOeu.setTelephoneMobile("");
                        respOeu.save();
                    }
                    action.setRespOuv(respOuv);

                    action.setDomaine(domaine);

                    action.setApparaitrePlanning(chainetoBoolean(row.get(6).toString()));
                    action.setTypeFacturation(row.get(7).toString());
                    action.setNbJoursPrevus(chainetofloat(row.get(8).toString()));
                    action.setCoutParJour(chainetofloat(row.get(11).toString()));
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
                    Date datedebut = chainetoDate(row.get(9).toString());
                    Date datefin = chainetoDate(row.get(10).toString());
                    action.setDtDeb(datedebut);
                    action.setDtFinPrevue(datefin);
                    action.setDtFinReelle(datefin);

                    for (List row_Dc : valuesDcConso) {

                        if (action.getCode().equals(row_Dc.get(5).toString())) {

                            if (row_Dc.get(20).toString() == null || row_Dc.get(20).toString().length() == 0) {
                                action.setEcartProjete(0);
                            } else {
                                action.setEcartProjete(chainetofloat(row_Dc.get(20).toString()));
                            }

                            if (row_Dc.get(18).toString() == null || row_Dc.get(18).toString().length() == 0) {
                                action.setResteAFaire(0);
                            } else {
                                action.setResteAFaire(chainetofloat(row_Dc.get(18).toString()));
                            }
                        }
                    }

               /*  */
                    action.save();
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

        }

        public void intialiserFormation(List<List<Object>> values) {
            new Delete().from(Formation.class).execute();


            List<Action> actionList = new ArrayList<>();
            Action action = new Action();
            ActiveAndroid.beginTransaction();
            try {

                for (List row : values) {
                    Formation formation = new Formation();

                    ;

                    actionList = DaoAction.getActionbyCode(row.get(5).toString());

                    if (actionList.size() >0){

                        action = actionList.get(0);

                        formation.setAction(action);
                        formation.setAvancementObjectif(chainetofloat(row.get(8).toString().replace('%', '0')));
                        formation.setAvancementTotal(chainetofloat(row.get(6).toString().replace('%', '0')));
                        formation.setAvancementPreRequis(chainetofloat(row.get(7).toString().replace('%', '0')));

                        formation.setAvancementPostFormation(chainetofloat(row.get(9).toString().replace('%', '0')));

                    }
                    formation.save();



                }




                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }

        public void initialiserPojet(List<List<Object>> values) throws ParseException {
            new Delete().from(Projet.class).execute();
            Projet projet = new Projet();
            projet.setDescription("");
            projet.setNom("");
            projet.setDateDebut(chainetoDate("20/01/2018"));
            projet.setDateFinReelle(chainetoDate("20/05/2018"));
            projet.setDateFinInitiale(chainetoDate("20/05/2018"));
            ActiveAndroid.beginTransaction();
            try {

                for (List row : values) {
                    projet.setNom(row.get(0).toString());
                    projet.setDescription("Projet_Master2_MIAGE");
                    projet.save();
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }

        public void initialiserSaisieCharge(List<List<Object>> values) throws ParseException {
            new Delete().from(SaisieCharge.class).execute();





            for (List row : values) {
                SaisieCharge saisiecharge = new SaisieCharge();
                if (!row.get(0).equals("")) {
                    ActiveAndroid.beginTransaction();
                    try {
                        saisiecharge.setNbSemainePassee(chainetoint(row.get(11).toString()));
                        saisiecharge.setNbSemaines(chainetofloat(row.get(8).toString()));
                        saisiecharge.setChargeEstimeeParSemaine(chainetofloat(row.get(9).toString()));
                        saisiecharge.setChargeRestanteEstimeeEnHeure(chainetofloat(row.get(12).toString()));
                        saisiecharge.setChargeTotaleEstimeeEnHeure(chainetofloat(row.get(5).toString()));
                        saisiecharge.setHeureParUnite(chainetofloat(row.get(4).toString()));

                        saisiecharge.setNbUnitesCibles(chainetoint(row.get(3).toString()));

                        saisiecharge.setChargeRestanteParSemaine(chainetofloat(row.get(15).toString()));

                        saisiecharge.setPrctChargeFaiteParSemaineParChargeEstimee(chainetofloat(row.get(17).toString().replace('%', ' ')));
                        List<Action> listesActions = DaoAction.getActionbyCode(row.get(2).toString());

                        if (listesActions.size() > 0) {
                            Action actionsaisie = new Action();
                            actionsaisie = listesActions.get(0);
                            saisiecharge.setAction(actionsaisie);
                        }


                        saisiecharge.save();
                        List<SaisieCharge> listes = DaoSaisieCharge.loadAll();


                        ActiveAndroid.setTransactionSuccessful();
                    } finally {
                        ActiveAndroid.endTransaction();
                    }
                }
            }
        }

        public void initialiserMesures(List<List<Object>> values) throws ParseException {
            new Delete().from(Mesure.class).execute();

            List<Mesure>listfi = new ArrayList<>();

            SaisieCharge action = new SaisieCharge();
            ActiveAndroid.beginTransaction();
            try {
                for (List row : values) {
                    Mesure mesure = new Mesure();
                    List<SaisieCharge> listsaisieCharges= new ArrayList<>();
                    List<Action> listeaction = DaoAction.getActionbyCode(row.get(0).toString());
                    if (listeaction.size() > 0){
                        listsaisieCharges=DaoSaisieCharge.loadSaisiebyAction(listeaction.get(0));
                    }

                    if(listsaisieCharges.size()>0) {
                        action =  listsaisieCharges.get(0);
                        mesure.setAction(action);
                    }

                    mesure.setDtMesure(chainetoDate(row.get(2).toString()));
                    mesure.setNbUnitesMesures(chainetoint(row.get(1).toString()));
                    mesure.save();
                }

                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }
        }

        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(List<String> output) {
            mProgress.hide();
            if (output == null || output.size() == 0) {
                mOutputText.setText("No results returned.");
            } else {
                output.add(0, "Data retrieved using the Google Sheets API:");
                mOutputText.setText(TextUtils.join("\n", output));

            }
            String initialUtilisateur = LoggedUser.getInstance().getInitials();
            Intent intent = new Intent(ChargementDonnees.this,ActivityGestionDesInitials.class);
            startActivity(intent);
        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            ChargementDonnees.REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n"
                            + mLastError.getMessage());
                }

            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }
}
