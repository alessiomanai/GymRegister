package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;

import java.util.ArrayList;

/**
 * Created by alessio on 02/09/16.
 * Completa
 */
public class QueryPagamento extends Query {

    private static QueryPagamento instance;

    public static QueryPagamento getInstance(Context context) {
        if (instance == null) {
            instance = new QueryPagamento(context);
        }

        return instance;
    }

    protected QueryPagamento(Context context) {
        super(context);
    }

    public void inizializza(Iscritto iscritto, Corso corso) {

        SQLiteDatabase db = instance.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id FROM Iscritto ORDER BY id DESC LIMIT 1;", null);

        c.moveToFirst();

        int i = Integer.parseInt(c.getString(0));

        iscritto.setIdDatabase(i);

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("INSERT INTO Pagamento" +
                "(iscritto, corso" +
                ") VALUES (" + i + "" +
                ", " + corso.getId() + ")");

        Log.v("Corso", "aggiunto");

    }

    /**
     * aggiorna i dati pagamento di un utente
     */
    public void update(Iscritto iscritto) {

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("UPDATE " + Tabelle.InfoTabelle.tabelle[3] +
                " SET iscrizione='" + iscritto.getIscrizione() + "'," +
                " settembre='" + iscritto.getSettembre() + "'," +
                " ottobre='" + iscritto.getOttobre() + "'," +
                " novembre='" + iscritto.getNovembre() + "'," +
                " dicembre='" + iscritto.getDicembre() + "'," +
                " gennaio='" + iscritto.getGennaio() + "'," +
                " febbraio='" + iscritto.getFebbraio() + "'," +
                " marzo='" + iscritto.getMarzo() + "'," +
                " aprile='" + iscritto.getAprile() + "'," +
                " maggio='" + iscritto.getMaggio() + "'," +
                " giugno='" + iscritto.getGiugno() + "'," +
                " luglio='" + iscritto.getLuglio() + "'," +
                " agosto='" + iscritto.getAgosto() + "' " +
                "WHERE iscritto=" + iscritto.getIdDatabase() + " AND " +
                "corso=" + iscritto.getPalestra().getId() + ";");

        Log.v("Corso", "aggiunto");

    }

    public Cursor getPagamenti() {

        SQLiteDatabase database = instance.getReadableDatabase();
        Cursor informazioni = database.query(Tabelle.InfoTabelle.tabelle[3], Tabelle.InfoTabelle.pagamento, null,
                null, null, null, null);

        return informazioni;
    }

    public ArrayList<String> utentiNotPay(String mese, String month, Corso corso) {

        ArrayList<String> nomi = new ArrayList<>();

        SQLiteDatabase db = instance.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT nome FROM Iscritto, Pagamento, Presenze " +
                "WHERE Pagamento.iscritto = Iscritto.id AND " +
                "Presenze.iscritto = Pagamento.iscritto AND " + mese + " = 'nonpagato' " +
                "AND Iscritto.corso = " + corso.getId() +
                " AND giornoPresenza like '%/" + month + "/%';", null);

        if (c.getCount() == 0) {
            Log.v("Risultati join", "nessun risultato ");
            return nomi;
        }

        c.moveToFirst();

        do {

            nomi.add(c.getString(0));

        } while (c.moveToNext());

        c.close();

        return nomi;

    }

}
