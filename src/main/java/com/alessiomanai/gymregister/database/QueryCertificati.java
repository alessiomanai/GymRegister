package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Iscritto;

public class QueryCertificati extends Query {

    private static QueryCertificati instance;

    protected QueryCertificati(Context context) {
        super(context);
    }

    public static QueryCertificati getInstance(Context context) {
        if (instance == null) {
            instance = new QueryCertificati(context);
        }

        return instance;
    }

    public void nuovo(Iscritto iscritto, String data) {

        SQLiteDatabase database = instance.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + Tabelle.InfoTabelle.tabelle[5] +
                " (iscritto, data) VALUES (" + iscritto.getIdDatabase() +
                ", ?)");

        stmt.bindString(1, data);

        stmt.execute();

    }

    public String getCertificatoMedico(Iscritto iscritto) {

        try {

            SQLiteDatabase database = instance.getReadableDatabase();

            Cursor risultati = database.rawQuery("SELECT data FROM Certificato WHERE iscritto =" + iscritto.getIdDatabase(), null);

            if (!risultati.isAfterLast()) {

                risultati.moveToFirst();

                return risultati.getString(0);
            } else {
                return null;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean certificatoExists(Iscritto iscritto) {

        String result;

        result = this.getCertificatoMedico(iscritto);

        return (result != null);

    }

    public void update(Iscritto iscritto, String data) {

        SQLiteDatabase database = instance.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("UPDATE " + Tabelle.InfoTabelle.tabelle[5] +
                " SET data = ? where iscritto =" + iscritto.getIdDatabase() +
                ";");

        stmt.bindString(1, data);

        stmt.execute();

    }
}
