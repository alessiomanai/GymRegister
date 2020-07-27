package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Iscritto;

public class QueryCertificati extends Query {

    public QueryCertificati(Context context) {
        super(context);
    }

    public void nuovo(Query context, Iscritto iscritto, String data) {

        SQLiteDatabase database = context.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + Tabelle.InfoTabelle.tabelle[5] +
                " (iscritto, data) VALUES (" + iscritto.getIdDatabase() +
                ", ?)");

        stmt.bindString(1, data);

        stmt.execute();

    }

    public String getCertificatoMedico(Query context, Iscritto iscritto) {

        try {

            SQLiteDatabase database = context.getReadableDatabase();

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

    public boolean certificatoExists(Query context, Iscritto iscritto) {

        String result;

        result = this.getCertificatoMedico(context, iscritto);

        return (result != null);

    }

    public void update(Query context, Iscritto iscritto, String data) {

        SQLiteDatabase database = context.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("UPDATE " + Tabelle.InfoTabelle.tabelle[5] +
                " SET data = ? where iscritto =" + iscritto.getIdDatabase() +
                ";");

        stmt.bindString(1, data);

        stmt.execute();

    }
}
