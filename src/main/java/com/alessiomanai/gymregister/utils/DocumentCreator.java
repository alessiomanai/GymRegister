package com.alessiomanai.gymregister.utils;

import android.content.Context;
import android.print.PdfConverter;

import java.io.File;

public class DocumentCreator {

    private String document;
    private Context context;
    private File outputFile;

    public DocumentCreator(Context context, File file){
        this.context = context;
        this.outputFile = file;
        this.document = this.newDocument();
    }

    private String newDocument(){
        return "<html><body>";
    }

    public void endDocument(){
        this.document += "</body></html>";
    }

    public void setTitle(String title){
        this.document += "<h2>" + title + "</h2>";
    }

    public void setChapter(String chapter){
        this.document += "<h3>" + chapter + "</h3>";
    }

    public void setH4Chapter(String chapter){
        this.document += "<h4>" + chapter + "</h4>";
    }

    public void newLine(String line){
        this.document += line + "<br/>";
    }

    public void addLine(String line){
        this.document += line + " ";
    }

    public void lastLine(String line) {
        this.document += "<br/> <br/>" + line + "<br/>";
    }

    public Context getContext() {
        return this.context;
    }

    public String getDocument() {
        return document;
    }

    public void getPDF() {
        this.endDocument();
        PdfConverter converter = PdfConverter.getInstance();
        converter.convert(this.getContext(), this.document, this.outputFile);
    }
}
