package miage.fr.gestionprojet.outils.factories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class MailFactory {
    public void sendMailWithAttachment(String pathToFile, String subject, String title, Context context){
        File filelocation = new File(pathToFile);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("vnd.android.cursor.dir/email");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(emailIntent , title));
    }
}
