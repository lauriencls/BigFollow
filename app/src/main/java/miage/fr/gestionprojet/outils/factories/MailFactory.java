package miage.fr.gestionprojet.outils.factories;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by robin_delaporte on 13/04/2018.
 */

public class MailFactory {
    public void sendMailWithAttachment(String pathToFile, String subject, String title, Context context, List<String> filePaths){
        File filelocation = new File(pathToFile);
        Uri path = Uri.fromFile(filelocation);
        Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        emailIntent.setType("vnd.android.cursor.dir/email");
        ArrayList<Uri> uris = new ArrayList<Uri>();
        for (String file : filePaths)
        {
            File fileIn = new File(file);
            Uri u = Uri.fromFile(fileIn);
            uris.add(u);
        }
        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(emailIntent , title));
    }
}
