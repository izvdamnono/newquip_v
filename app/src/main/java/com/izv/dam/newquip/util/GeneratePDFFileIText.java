package com.izv.dam.newquip.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

public class GeneratePDFFileIText implements Runnable{

    private static final Font chapterFont = FontFactory.getFont(FontFactory.HELVETICA, 26, Font.BOLDITALIC);
    private static final Font paragraphFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.NORMAL);
    private static final String PDFS = "PDFGenerados";

    private String titulo;
    private String nota;
    private String imagen;
    private String nombre_completo;
    private Context contexto;

    public GeneratePDFFileIText(String titulo, String nota, String imagen, String nombre_completo){
        this.titulo = titulo;
        this.nota = nota;
        this.imagen = imagen;
        this.nombre_completo = nombre_completo;

    }
    public GeneratePDFFileIText(){


    }

    @Override
    public void run() {
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
            chunk.setBackground(BaseColor.GRAY);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph(nota, paragraphFont));
            Image image;
            if (imagen!=null) {
                try {
                    image = Image.getInstance(imagen);
                    image.setAbsolutePosition(2, 150);
                    chapter.add(image);
                } catch (BadElementException ex) {
                    System.out.println("Image BadElementException" + ex);
                } catch (IOException ex) {
                    System.out.println("Image IOException " + ex);
                }
            }
            document.add(chapter);
            document.close();
        } catch (DocumentException documentException) {
            documentException.printStackTrace();
        }
    }

    public void mostrarPDF(String archivo, Context c) {
        Toast.makeText(c, "Cargando el documento",Toast.LENGTH_LONG).show();
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try{
            c.startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(c, "No dispone de una APP para abrir este PDF",Toast.LENGTH_LONG).show();

        }
    }

     /*public void createPDF(File pdfNewFile, Context contexto, String nombreCompleto, String nota, String titulo, String imagen) {
        try {
            Document document = new Document();
            try {

                PdfWriter.getInstance(document, new FileOutputStream(pdfNewFile));

            } catch (FileNotFoundException fileNotFoundException) {
                Toast.makeText(contexto, "No se encontró el fichero para generar el PDF", Toast.LENGTH_LONG).show();
            }
            document.open();

            Chunk chunk = new Chunk(titulo, chapterFont);
            chunk.setBackground(BaseColor.GRAY);
            Chapter chapter = new Chapter(new Paragraph(chunk), 1);
            chapter.setNumberDepth(0);
            chapter.add(new Paragraph(nota, paragraphFont));
            Image image;
            if (imagen!=null) {
                try {
                    image = Image.getInstance(imagen);
                    image.setAbsolutePosition(2, 150);
                    chapter.add(image);
                } catch (BadElementException ex) {
                    System.out.println("Image BadElementException" + ex);
                } catch (IOException ex) {
                    System.out.println("Image IOException " + ex);
                }
            }
            document.add(chapter);
            document.close();
            //System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");
            Toast.makeText(contexto, "El PDF ha sido guardado", Toast.LENGTH_LONG).show();
            mostrarPDF(nombreCompleto,contexto);
        } catch (DocumentException documentException) {
            Toast.makeText(contexto, "Se ha producido un error al generar el PDF", Toast.LENGTH_LONG).show();
            //System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        }
    }*/
}
