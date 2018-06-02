package miage.fr.gestionprojet.outils;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import miage.fr.gestionprojet.models.LoggedUser;
import miage.fr.gestionprojet.models.Mesure;
import miage.fr.gestionprojet.models.dao.DaoMesure;

/**
 * Created by Rushnak on 30/05/2018.
 */

public class UpdaterTask extends AsyncTask<Void, Void, List<String>> {

    private static final String[] SCOPES = {SheetsScopes.SPREADSHEETS};
    private com.google.api.services.sheets.v4.Sheets mService = null;
    private String spreadSheetId;
    private ValueRange valueRange;
    private Activity context;
    static final int REQUEST_AUTHORIZATION = 1001;

    public UpdaterTask(Activity context, String spreadsheetId, Mesure mesure){
        this.spreadSheetId = spreadsheetId;
        this.context = context;
        GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(
                context.getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
        mCredential.setSelectedAccount(LoggedUser.getInstance().getCurrentUser().getAccount());
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                transport, jsonFactory, mCredential)
                .setApplicationName("Big Follow")
                .build();
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        Object[] val = {
                mesure.getAction().getAction().getCode(),
                String.valueOf(mesure.getNbUnitesMesures()),
                fmt.format(mesure.getDtMesure())
        };
        List<List<Object>> values = Arrays.asList(
                Arrays.asList(
                    val
                )
        );
        valueRange = new ValueRange().setValues(values);



    }

    @Override
    protected List<String> doInBackground(Void... params) {
        UpdateValuesResponse result = null;
        String line = String.valueOf(DaoMesure.loadAll().size()+2);
        try {
            result = mService.spreadsheets().values().update(spreadSheetId, "Mesures de saisie/charge!A"+line+":C"+line, valueRange)
                    .setValueInputOption("RAW")
                    .execute();
            Log.v("debug","Mesure ajoutée ");
        } catch (UserRecoverableAuthIOException e1){
            context.startActivityForResult(e1.getIntent(), REQUEST_AUTHORIZATION);
            this.execute(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
