package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.classi.Pagamento;

import java.util.ArrayList;

/**
 * Created by alessio on 02/09/16.
 * Completa
 */
public class QueryPagamento extends Query {

    private static QueryPagamento instance;

    protected QueryPagamento(Context context) {
        super(context);
    }

    public static QueryPagamento getInstance(Context context) {
        if (instance == null) {
            instance = new QueryPagamento(context);
        }

        return instance;
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

        db.close();
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

        database.close();
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

    public Pagamento getPagamenti(Iscritto iscritto) {

        SQLiteDatabase db = instance.getReadableDatabase();

        String selection = "iscritto = ? AND corso = ?";
        String[] selectionArgs = {String.valueOf(iscritto.getIdDatabase()), String.valueOf(iscritto.getIdCorso())};

        Cursor cursor = db.query(
                Tabelle.InfoTabelle.tabelle[3],
                Tabelle.InfoTabelle.pagamento,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        Pagamento pagamento = null;

        if (cursor != null && cursor.moveToFirst()) {
            String iscrizione = cursor.getString(cursor.getColumnIndexOrThrow("iscrizione"));
            String settembre = cursor.getString(cursor.getColumnIndexOrThrow("settembre"));
            String ottobre = cursor.getString(cursor.getColumnIndexOrThrow("ottobre"));
            String novembre = cursor.getString(cursor.getColumnIndexOrThrow("novembre"));
            String dicembre = cursor.getString(cursor.getColumnIndexOrThrow("dicembre"));
            String gennaio = cursor.getString(cursor.getColumnIndexOrThrow("gennaio"));
            String febbraio = cursor.getString(cursor.getColumnIndexOrThrow("febbraio"));
            String marzo = cursor.getString(cursor.getColumnIndexOrThrow("marzo"));
            String aprile = cursor.getString(cursor.getColumnIndexOrThrow("aprile"));
            String maggio = cursor.getString(cursor.getColumnIndexOrThrow("maggio"));
            String giugno = cursor.getString(cursor.getColumnIndexOrThrow("giugno"));
            String luglio = cursor.getString(cursor.getColumnIndexOrThrow("luglio"));
            String agosto = cursor.getString(cursor.getColumnIndexOrThrow("agosto"));

            pagamento = new Pagamento(iscrizione, settembre, ottobre, novembre, dicembre,
                    gennaio, febbraio, marzo, aprile, maggio, giugno, luglio, agosto);

            cursor.close();
        }

        db.close();
        return pagamento;
    }

    public Boolean controllaEsistenzaRecord(Iscritto iscritto) {

        SQLiteDatabase db = instance.getReadableDatabase();

        String selection = "iscritto = ? AND corso = ?";
        String[] selectionArgs = {String.valueOf(iscritto.getIdDatabase()), String.valueOf(iscritto.getIdCorso())};

        Cursor cursor = db.query(
                Tabelle.InfoTabelle.tabelle[3],
                Tabelle.InfoTabelle.pagamento,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        return cursor.getCount() > 0;
    }

}
