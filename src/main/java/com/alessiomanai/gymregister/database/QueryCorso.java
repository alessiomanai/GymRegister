package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alessiomanai.gymregister.classi.Corso;

import java.util.ArrayList;

/**
 * Created by alessio on 02/09/16.
 *
 * Query di gestione corso
 */

public class QueryCorso extends Query {

    public QueryCorso(Context context) {
        super(context);
    }

    /**
     * inserisce un nuovo corso all'interno del database
     */
    public void nuovo(Query context, Corso nuovoCorso) {

        SQLiteDatabase database = context.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + Tabelle.InfoTabelle.tabelle[1] +
                "(" + Tabelle.InfoTabelle.corso[1] + ") VALUES (?)");

        stmt.bindString(1, nuovoCorso.getNome());
        stmt.execute();

    }

    /**
     * Cursor è un tipo di dato che riceve tutte le informazioni dal database
     * successivamente vanno scorse tramite e i metodi forniti dalla libreria java
     */
    public Cursor getCorso(Query context) {

        SQLiteDatabase database = context.getReadableDatabase();
        Cursor informazioni = database.query(Tabelle.InfoTabelle.tabelle[1], Tabelle.InfoTabelle.corso, null,
                null, null, null, null);

        return informazioni;
    }

    public ArrayList<Corso> getElencoCorsi(Query context) {

        ArrayList<Corso> palestre = new ArrayList<>();

        Cursor risultati = this.getCorso(context);

        while (risultati.moveToNext()) {

            Corso corso = new Corso(risultati.getString(1));
            corso.setId(Integer.parseInt(risultati.getString(0)));

            palestre.add(corso);

        }

        return palestre;
    }

    public boolean eliminaCorso(Query context, Corso corso) {

        SQLiteDatabase database = context.getWritableDatabase();

        database.execSQL("DELETE FROM Corso " +
                "WHERE nome='" + corso.getNome() + "' AND id=" + corso.getId() + ";");

        return true;

    }

    public boolean rinominaCorso(Query context, Corso corso, String nuovoNome) {

        SQLiteDatabase database = context.getWritableDatabase();

        database.execSQL("UPDATE Corso " +
                "SET nome='" + nuovoNome + "' " +
                "WHERE id=" + corso.getId());

        return true;

    }

}
