package miage.fr.gestionprojet.outils.factories;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static miage.fr.gestionprojet.MyApplication.getContext;


/**
 * Created by robin_delaporte on 10/04/2018.
 */

public class PdfFactory {
    public static final String DEST = getContext().getFilesDir().getPath().toString() +"/mail.pdf";

    public static void create() throws
            DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        file.createNewFile();
        new PdfFactory().createPdf(DEST);
    }
    public void createPdf(String dest) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(8);
        for(int aw = 0; aw < 16; aw++){
            table.addCell("hi");
        }
        document.add(table);
        document.close();
    }
}
