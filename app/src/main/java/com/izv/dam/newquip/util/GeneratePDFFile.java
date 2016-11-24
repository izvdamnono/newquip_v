package com.izv.dam.newquip.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.izv.dam.newquip.vistas.notas.VistaNota;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class GeneratePDFFile extends AsyncTask<Void,Void,Void> {

    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
    private static final String PDFS = "PDFGenerados";
    private String titulo;
    private String nota;
    private String imagen;
    private String nombre_completo;
    private Context contexto;

    public GeneratePDFFile(String titulo, String nota, String imagen, String nombre_completo, Context contexto) {
        this.titulo = titulo;
        this.nota = nota;
        this.imagen = imagen;
        this.nombre_completo = nombre_completo;
        this.contexto = contexto;

    }

    public GeneratePDFFile() {

    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        final File pdfNewFile = new File(nombre_completo);
        if (pdfNewFile.exists()) {
            pdfNewFile.delete();
        }
        try {
            Document document = new Document();
            try {

                PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));

            } catch (FileNotFoundException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            document.open();

            Chunk chunk = new Chunk(titulo, chapterFont);
            Chapter chapter = new Chapter(new Paragraph(chunk), 0);
            chapter.setNumberDepth(0);

            chapter.add(new Paragraph(nota, paragraphFont));
            document.add(chapter);
//            Chapter chapter1 = new Chapter(new Paragraph(nota, paragraphFont), 0);
//            document.add(chapter1);
            Image image;
            if (imagen != null) {
                Chapter chapter2 = new Chapter(new Paragraph(), 1);
                chapter2.setNumberDepth(0);
                try {
                    image = Image.getInstance(imagen);
                    image.setAbsolutePosition(40, 200);
                    int indentation = 0;
                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin() - indentation) / image.getWidth()) * 100;
                    image.scalePercent(scaler);
                    chapter2.add(image);
                } catch (BadElementException | IOException ex) {
                    ex.printStackTrace();
                }
                document.add(chapter2);
            }
            document.close();
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values){

    }

    @Override
    protected void onPostExecute(Void aVoid){
        mostrarPDF(nombre_completo,contexto);
    }

    private void mostrarPDF(String archivo, Context c) {
//        Toast.makeText(c, "Cargando el documento", Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            c.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(c, "No dispone de una APP para abrir este PDF", Toast.LENGTH_LONG).show();

        }
    }

}
