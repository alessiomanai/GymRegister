package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;

import java.util.ArrayList;

/**
 * Created by alessio on 02/09/16.
 */
public class QueryIscritto extends Query {

    private String[] iscritto = new String[]{"id", "nome", "dataDiNascita", "telefono", "indirizzo",
            "citta", "corso", "fotoProfilo", "notePath"};

    private static QueryIscritto instance;

    public static QueryIscritto getInstance(Context context) {
        if (instance == null) {
            instance = new QueryIscritto(context);
        }

        return instance;
    }

    protected QueryIscritto(Context context) {
        super(context);
    }

    /**
     * aggiunge un iscritto alla palestra
     * creazione utente con prepared statement anti sqlinjection
     */
    public void nuovo(Iscritto iscritto, Corso corso) {

        SQLiteDatabase database = instance.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("INSERT INTO " + Tabelle.InfoTabelle.tabelle[0] +
                "(nome, dataDiNascita, telefono, indirizzo, " +
                "citta, corso) VALUES (" +
                "?, '" + iscritto.getDataDiNascita() +
                "', '" + iscritto.getTelefono() + "', ?" +
                ", ?, " + corso.getId()
                + ")");

        stmt.bindString(1, iscritto.getId());
        stmt.bindString(2, iscritto.getIndirizzo());
        stmt.bindString(3, iscritto.getCitta());
        stmt.execute();

    }

    /**
     * carica la lista di iscritti
     */
    public ArrayList<Iscritto> caricaIscritti(Corso id) {

        ArrayList<Iscritto> iscritti = new ArrayList<>();

        SQLiteDatabase database = instance.getReadableDatabase();

        //estraggo i dati degli iscritti e dei pagamenti
        Cursor risultati = database.rawQuery(
                "SELECT id, nome, dataDiNascita, telefono, indirizzo, " +
                        "citta, Iscritto.corso, " +
                        "iscrizione, " +
                        "settembre, ottobre, novembre, dicembre, gennaio, febbraio, " +
                        "marzo, aprile, maggio, " +
                        "giugno, luglio, agosto, fotoProfilo " +
                        "FROM Iscritto, Pagamento " +
                        "WHERE Iscritto.id=Pagamento.iscritto " +
                        "AND Iscritto.corso=" + id.getId() + " ORDER BY nome", null);

        if (risultati.getCount() == 0) {
            Log.v("Risultati join", "nessun risultato ");
            return iscritti;
        }

        risultati.moveToFirst();

        do {

            Iscritto selezionato = new Iscritto();

            selezionato.setIdDatabase(Integer.parseInt(risultati.getString(0)));
            selezionato.setId(risultati.getString(1));
            selezionato.setDataDiNascita(risultati.getString(2));
            selezionato.setTelefono(risultati.getString(3));
            selezionato.setIndirizzo(risultati.getString(4));
            selezionato.setCitta(risultati.getString(5));
            selezionato.setIdCorso(Integer.parseInt(risultati.getString(6)));
            selezionato.setIscrizione(risultati.getString(7));
            selezionato.setSettembre(risultati.getString(8));
            selezionato.setOttobre(risultati.getString(9));
            selezionato.setNovembre(risultati.getString(10));
            selezionato.setDicembre(risultati.getString(11));
            selezionato.setGennaio(risultati.getString(12));
            selezionato.setFebbraio(risultati.getString(13));
            selezionato.setMarzo(risultati.getString(14));
            selezionato.setAprile(risultati.getString(15));
            selezionato.setMaggio(risultati.getString(16));
            selezionato.setGiugno(risultati.getString(17));
            selezionato.setLuglio(risultati.getString(18));
            selezionato.setAgosto(risultati.getString(19));
            selezionato.setUrlFoto(risultati.getString(20));

            iscritti.add(selezionato);

        } while (risultati.moveToNext());

        risultati.close();

        return iscritti;
    }

    public boolean aggiorna(Iscritto iscritto) {

        SQLiteDatabase database = instance.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("UPDATE " + Tabelle.InfoTabelle.tabelle[0] +
                " SET nome=?," +
                " dataDiNascita='" + iscritto.getDataDiNascita() + "'," +
                " telefono='" + iscritto.getTelefono() + "'," +
                " indirizzo=?," +
                " citta=? " +
                "WHERE id=" + iscritto.getIdDatabase() + " AND " +
                "corso=" + iscritto.getPalestra().getId() + ";");

        stmt.bindString(1, iscritto.getId());
        stmt.bindString(2, iscritto.getIndirizzo());
        stmt.bindString(3, iscritto.getCitta());
        stmt.execute();

        return true;

    }

    public void elimina(Iscritto iscritto) {

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("DELETE FROM Iscritto " +
                "WHERE id=" +
                iscritto.getIdDatabase() +
                " AND corso=" +
                iscritto.getPalestra().getId() +
                ";");

    }

    public int selectLastIDIscritto() {

        SQLiteDatabase database = instance.getReadableDatabase();

        //estraggo i dati degli iscritti e dei pagamenti
        Cursor risultati = database.rawQuery("SELECT id FROM Iscritto ORDER BY id DESC LIMIT 1", null);
        risultati.moveToFirst();

        int last = Integer.parseInt(risultati.getString(0));

        return last;

    }

    public int selectLastIDCorso() {

        SQLiteDatabase database = instance.getReadableDatabase();

        //estraggo i dati degli iscritti e dei pagamenti
        Cursor risultati = database.rawQuery("SELECT corso FROM Iscritto ORDER BY id DESC LIMIT 1", null);
        risultati.moveToFirst();

        int last = Integer.parseInt(risultati.getString(0));

        return last;

    }

    /**
     * cerca nella lista di iscritti
     */
    public ArrayList<Iscritto> cercaIscritto(Corso id, String chiave) {

        ArrayList<Iscritto> iscritti = new ArrayList<>();

        SQLiteDatabase database = instance.getReadableDatabase();

        //estraggo i dati degli iscritti e dei pagamenti
        Cursor risultati = database.rawQuery(
                "SELECT id, nome, dataDiNascita, telefono, indirizzo, " +
                        "citta, Iscritto.corso, " +
                        "iscrizione, " +
                        "settembre, ottobre, novembre, dicembre, gennaio, febbraio, " +
                        "marzo, aprile, maggio, " +
                        "giugno, luglio, agosto " +
                        "FROM Iscritto, Pagamento " +
                        "WHERE (Iscritto.id=Pagamento.iscritto " +
                        "AND Iscritto.corso=" + id.getId() + ") AND nome LIKE " +
                        "'%" + chiave + "%' ORDER BY nome", null);

        if (risultati.getCount() == 0) {
            Log.v("Risultati join", "nessun risultato ");
            return iscritti;
        }

        risultati.moveToFirst();

        do {

            Iscritto selezionato = new Iscritto();

            selezionato.setIdDatabase(Integer.parseInt(risultati.getString(0)));
            selezionato.setId(risultati.getString(1));
            selezionato.setDataDiNascita(risultati.getString(2));
            selezionato.setTelefono(risultati.getString(3));
            selezionato.setIndirizzo(risultati.getString(4));
            selezionato.setCitta(risultati.getString(5));
            selezionato.setIdCorso(Integer.parseInt(risultati.getString(6)));
            selezionato.setIscrizione(risultati.getString(7));
            selezionato.setSettembre(risultati.getString(8));
            selezionato.setOttobre(risultati.getString(9));
            selezionato.setNovembre(risultati.getString(10));
            selezionato.setDicembre(risultati.getString(11));
            selezionato.setGennaio(risultati.getString(12));
            selezionato.setFebbraio(risultati.getString(13));
            selezionato.setMarzo(risultati.getString(14));
            selezionato.setAprile(risultati.getString(15));
            selezionato.setMaggio(risultati.getString(16));
            selezionato.setGiugno(risultati.getString(17));
            selezionato.setLuglio(risultati.getString(18));
            selezionato.setAgosto(risultati.getString(19));

            iscritti.add(selezionato);

        } while (risultati.moveToNext());

        risultati.close();

        return iscritti;
    }

    public boolean salvaNoteFile(Iscritto iscritto) {

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("UPDATE " + Tabelle.InfoTabelle.tabelle[0] +
                " SET notePath='" + iscritto.getNote() + "' " +
                "WHERE id=" + iscritto.getIdDatabase());

        return true;
    }

    public boolean aggiornaFotoProfilo(Iscritto iscritto) {

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("UPDATE " + Tabelle.InfoTabelle.tabelle[0] +
                " SET fotoProfilo='" + iscritto.getUrlFoto() + "' " +
                "WHERE id=" + iscritto.getIdDatabase());

        return true;
    }

    public String getUrlPhoto(Iscritto iscritto) {

        SQLiteDatabase database = instance.getReadableDatabase();

        Cursor risultati = database.rawQuery("SELECT fotoProfilo FROM Iscritto WHERE id =" + iscritto.getIdDatabase(), null);
        risultati.moveToFirst();

        return risultati.getString(0);

    }

    public boolean cambiaCorsoUtente(Iscritto iscritto, Corso nuovoCorso) {

        SQLiteDatabase database = instance.getWritableDatabase();

        database.execSQL("UPDATE " + Tabelle.InfoTabelle.tabelle[0] +
                " SET corso='" + nuovoCorso.getId() + "' " +
                "WHERE id=" + iscritto.getIdDatabase());

        return true;

    }

}
