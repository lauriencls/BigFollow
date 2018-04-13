package miage.fr.gestionprojet.outils.Pdf;

import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import miage.fr.gestionprojet.models.Projet;

import static miage.fr.gestionprojet.MyApplication.getContext;


/**
 * Created by robin_delaporte on 10/04/2018.
 */

public abstract class InterfacePdf {
    public static final String DEST = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +"/mail.pdf";
    private Document document;
    protected Projet projet;

    InterfacePdf(Projet projet) throws
            DocumentException, IOException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        file.createNewFile();
        this.projet = projet;
    }

    public void createPdf() throws IOException, DocumentException {
        this.instatiate(DEST);

        this.constructPdf();

        this.close();

    }

    protected void instatiate(String dest) throws IOException, DocumentException {
        document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
    }

    protected void close(){
        document.close();
    }

    protected abstract void constructPdf() throws DocumentException;

    protected void addTitles(String title) throws DocumentException {
        Font font = new Font(Font.FontFamily.HELVETICA, 36, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph p = new Paragraph(title, font);
        this.document.add(p);
    }

    protected void addParagraph(String content) throws DocumentException {
        Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
        Paragraph p = new Paragraph(content, font);
        this.document.add(p);
    }

    protected void addTable(List<String> columsName, List<String> cellsValues) throws DocumentException {
        PdfPTable table = new PdfPTable(columsName.size());

        addHeaderToTable(columsName, table);

        addCellToTable(cellsValues, table);

        document.add(table);
    }

    private void addCellToTable(List<String> cellsValues, PdfPTable table) {
        for (String cell:
                cellsValues) {
            table.addCell(cell);
        }
    }

    private void addHeaderToTable(List<String> columsName, PdfPTable table) {
        for (String header:
             columsName) {
            PdfPCell cell = new PdfPCell();
            Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, GrayColor.BLUE);
            Paragraph p = new Paragraph(header, font);
            cell.addElement(p);
            cell.setFixedHeight(20);
            table.addCell(cell);
        }
    }

}
