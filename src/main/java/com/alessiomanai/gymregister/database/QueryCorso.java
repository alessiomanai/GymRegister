package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alessiomanai.gymregister.classi.Corso;

import java.util.ArrayList;

/**
 * Created by alessio on 02/09/16.
 * <p>
 * Query di gestione corso
 */

public class QueryCorso extends Query {

    private static QueryCorso instance;

    public static QueryCorso getInstance(Context context) {
        if (instance == null) {
            instance = new QueryCorso(context);
        }

        return instance;
    }

    protected QueryCorso(Context context) {
        super(context);
    }

    /**
     * inserisce un nuovo corso all'interno del database
     */
    public void nuovo(Corso nuovoCorso) {

        SQLiteDatabase database = instance.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + Tabelle.InfoTabelle.tabelle[1] +
                "(" + Tabelle.InfoTabelle.corso[1] + ") VALUES (?)");

        stmt.bindString(1, nuovoCorso.getNome());
        stmt.execute();

    }

    /**
     * Cursor è un tipo di dato che riceve tutte le informazioni dal database
     * successivamente vanno scorse tramite e i metodi forniti dalla libreria java
     */
    public Cursor getCorso() {

        SQLiteDatabase database = instance.getReadableDatabase();
        Cursor informazioni = database.query(Tabelle.InfoTabelle.tabelle[1], Tabelle.InfoTabelle.corso, null,
                null, null, null, null);

        return informazioni;
    }

    public ArrayList<Corso> getElencoCorsi() {

        ArrayList<Corso> palestre = new ArrayList<>();

        Cursor risultati = this.getCorso();

        while (risultati.moveToNext()) {

            Corso corso = new Corso(Integer.parseInt(risultati.getString(0)), risultati.getString(1));

            palestre.add(corso);

        }

        return palestre;
    }

    public boolean eliminaCorso(Corso corso) {

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("DELETE FROM Corso " +
                "WHERE nome='" + corso.getNome() + "' AND id=" + corso.getId() + ";");

        return true;

    }

    public boolean rinominaCorso(Corso corso, String nuovoNome) {

        SQLiteDatabase database = instance.getWritableDatabase();

        try {
            SQLiteStatement stmt = database.compileStatement("UPDATE Corso " +
                    "SET nome= ? " +
                    "WHERE id=" + corso.getId());

            stmt.bindString(1, nuovoNome);
            stmt.execute();

            return true;

        } catch (SQLException e) {
            return false;
        }
    }

}
