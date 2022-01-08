package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.classi.Presenza;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alessio on 02/09/16.
 *
 */
public class QueryPresenze extends Query {

    public QueryPresenze(Context context) {
        super(context);
    }

    /**
     * restituisce tutta la tabella delle presenze
     */
    public Cursor getPresenze(Query context) {

        SQLiteDatabase database = context.getReadableDatabase();
        Cursor informazioni = database.query(Tabelle.InfoTabelle.tabelle[2], Tabelle.InfoTabelle.presenze, null,
                null, null, null, null);

        return informazioni;

    }

    public ArrayList<Presenza> presenzeIscritto(Query context, Iscritto iscritto) {

        ArrayList<Presenza> elenco = new ArrayList<>();
        SQLiteDatabase database = context.getReadableDatabase();

        Cursor informazioni = database.rawQuery(
                "SELECT iscritto, giornoPresenza FROM Presenze WHERE iscritto=" + iscritto.getIdDatabase() +
                        " AND corso=" + iscritto.getIdCorso()
                , null);

        if (informazioni.getCount() == 0) {
            Log.v("Risultati ", "nessuna presenza ");
            return elenco;
        }

        informazioni.moveToFirst();

        do {

            Presenza selezionato = new Presenza();

            selezionato.setIscritto(iscritto);
            selezionato.setData(informazioni.getString(1));

            elenco.add(selezionato);

        } while (informazioni.moveToNext());

        informazioni.close();

        return elenco;
    }

    public ArrayList<Presenza> presenzeIscrittoOrdineDecrescente(Query context, Iscritto iscritto) {

        ArrayList<Presenza> elenco = new ArrayList<>();
        SQLiteDatabase database = context.getReadableDatabase();

        Cursor informazioni = database.rawQuery(
                "SELECT iscritto, giornoPresenza FROM Presenze WHERE iscritto=" + iscritto.getIdDatabase() +
                        " AND corso=" + iscritto.getIdCorso() + " ORDER BY giornoPresenza DESC"
                , null);

        if (informazioni.getCount() == 0) {
            Log.v("Risultati ", "nessuna presenza ");
            return elenco;
        }

        informazioni.moveToFirst();

        do {

            Presenza selezionato = new Presenza();

            selezionato.setIscritto(iscritto);
            selezionato.setData(informazioni.getString(1));

            elenco.add(selezionato);

        } while (informazioni.moveToNext());

        informazioni.close();

        return elenco;
    }

    public ArrayList<Presenza> presenzeCorso(Query context, Corso corso) {

        ArrayList<Presenza> elenco = new ArrayList<>();
        SQLiteDatabase database = context.getReadableDatabase();
        Cursor informazioni = database.rawQuery(
                "SELECT iscritto, corso, giornoPresenza FROM Presenze WHERE corso=" + corso.getId()
                , null);

        if (informazioni.getCount() == 0) {
            Log.v("Risultati ", "nessuna presenza ");
            return elenco;
        }

        informazioni.moveToFirst();

        do {

            Presenza selezionato = new Presenza();

            selezionato.setIdIscritto(Integer.parseInt(informazioni.getString(0)));
            selezionato.setIdCorso(Integer.parseInt(informazioni.getString(1)));
            selezionato.setData(informazioni.getString(2));

            elenco.add(selezionato);

        } while (informazioni.moveToNext());

        informazioni.close();

        return elenco;

    }

    public void aggiungi(Query context, Iscritto iscritto, Corso corso) {

        SQLiteDatabase database = context.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
        String data = sdf.format(new Date());

        database.execSQL("INSERT INTO " + Tabelle.InfoTabelle.tabelle[2] +
                "(" + Tabelle.InfoTabelle.presenze[0] + ", " +
                Tabelle.InfoTabelle.presenze[1] + ", " +
                Tabelle.InfoTabelle.presenze[2] +
                ") VALUES (" + iscritto.getIdDatabase() + "" +
                ", " + corso.getId() +
                ", '" + data + "')");

    }

    public boolean eliminaPresenzaOdierna(Query context, Iscritto iscritto) {

        SQLiteDatabase database = context.getWritableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
        String data = sdf.format(new Date());

        database.execSQL("DELETE FROM " + Tabelle.InfoTabelle.tabelle[2] + " " +
                "WHERE (iscritto=" + iscritto.getIdDatabase() + " AND giornoPresenza='" + data + "') " +
                "AND corso=" + iscritto.getIdCorso() + " ;");

        return true;

    }

    public Cursor presenzeOdierneCursor(Query context, Corso corso) {

        SQLiteDatabase database = context.getReadableDatabase();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
        String data = sdf.format(new Date());

        Cursor informazioni = database.rawQuery("SELECT iscritto, corso, giornoPresenza FROM Presenze WHERE " +
                "giornoPresenza='" + data + "' AND corso=" + corso.getId(), null);

        return informazioni;
    }

    public ArrayList<Presenza> presenzeOdierne(Query context, Corso corso) {

        ArrayList<Presenza> elenco = new ArrayList<>();

        Cursor informazioni = this.presenzeOdierneCursor(context, corso);

        if (informazioni == null || informazioni.getCount() == 0) {
            return elenco;
        }

        informazioni.moveToFirst();

        do {

            Presenza selezionato = new Presenza();

            selezionato.setIdIscritto(Integer.parseInt(informazioni.getString(0)));
            selezionato.setIdCorso(Integer.parseInt(informazioni.getString(1)));
            selezionato.setData(informazioni.getString(2));

            elenco.add(selezionato);

        } while (informazioni.moveToNext());

        informazioni.close();

        return elenco;

    }

    public boolean eliminaPresenzaVecchia(Query context, Iscritto iscritto, String data) {

        SQLiteDatabase database = context.getWritableDatabase();

        database.execSQL("DELETE FROM " + Tabelle.InfoTabelle.tabelle[2] + " " +
                "WHERE (iscritto=" + iscritto.getIdDatabase() + " AND giornoPresenza='" + data + "') " +
                "AND corso=" + iscritto.getIdCorso() + " ;");

        return true;

    }

    public void aggiungiPresenzaPrecedente(Query context, Iscritto iscritto, Corso corso, String data) {

        SQLiteDatabase database = context.getWritableDatabase();

        database.execSQL("INSERT INTO " + Tabelle.InfoTabelle.tabelle[2] +
                "(" + Tabelle.InfoTabelle.presenze[0] + ", " +
                Tabelle.InfoTabelle.presenze[1] + ", " +
                Tabelle.InfoTabelle.presenze[2] +
                ") VALUES (" + iscritto.getIdDatabase() + "" +
                ", " + corso.getId() +
                ", '" + data + "')");

    }

    public ArrayList<Presenza> presenzeCorsoMese(Query context, Iscritto iscritto, Corso corso, String mese) {

        ArrayList<Presenza> elenco = new ArrayList<>();
        SQLiteDatabase database = context.getReadableDatabase();
        Cursor informazioni = database.rawQuery(
                "SELECT iscritto, corso, giornoPresenza FROM Presenze WHERE corso=" + corso.getId() +
                        " AND giornoPresenza LIKE '%" + mese + "%' AND iscritto = " + iscritto.getIdDatabase()
                , null);

        if (informazioni.getCount() == 0) {
            Log.v("Risultati ", "nessuna presenza ");
            return elenco;
        }

        informazioni.moveToFirst();

        do {

            Presenza selezionato = new Presenza();

            selezionato.setIdIscritto(Integer.parseInt(informazioni.getString(0)));
            selezionato.setIdCorso(Integer.parseInt(informazioni.getString(1)));
            selezionato.setData(informazioni.getString(2));

            elenco.add(selezionato);

        } while (informazioni.moveToNext());

        informazioni.close();
        database.close();

        return elenco;

    }
}
