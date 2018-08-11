package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.provider.ContactsContract;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Importi;
import com.alessiomanai.gymregister.classi.Iscritto;

public class QueryImporti extends Query {

    public QueryImporti(Context context) {
        super(context);
    }

    public void inserisciImporti(Query context, Iscritto iscritto, Corso corso) {

        SQLiteDatabase database = context.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("INSERT INTO Importi (" +
                "iscritto, corso, iscrizione," +
                "settembre, ottobre, novembre, dicembre, gennaio, febbraio, marzo, aprile, maggio," +
                "giugno, luglio) VALUES (" + iscritto.getIdDatabase() +
                ", " + corso.getId() +
                ", ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? "
                + ")");

        stmt.bindString(1, iscritto.getImporti().getIscrizione());
        stmt.bindString(2, iscritto.getImporti().getSettembre());
        stmt.bindString(3, iscritto.getImporti().getOttobre());
        stmt.bindString(4, iscritto.getImporti().getNovembre());
        stmt.bindString(5, iscritto.getImporti().getDicembre());
        stmt.bindString(6, iscritto.getImporti().getGennaio());
        stmt.bindString(7, iscritto.getImporti().getFebbraio());
        stmt.bindString(8, iscritto.getImporti().getMarzo());
        stmt.bindString(9, iscritto.getImporti().getAprile());
        stmt.bindString(10, iscritto.getImporti().getMaggio());
        stmt.bindString(11, iscritto.getImporti().getGiugno());
        stmt.bindString(12, iscritto.getImporti().getLuglio());
        stmt.execute();

    }

    public void updateImporti(Query context, Iscritto iscritto, Corso corso) {

        SQLiteDatabase database = context.getWritableDatabase();

        SQLiteStatement stmt = database.compileStatement("UPDATE Importi " +
                " SET iscrizione=?," +
                " settembre=?," +
                " ottobre=?," +
                " novembre=?," +
                " dicembre=?," +
                " gennaio=?," +
                " febbraio=?," +
                " marzo=?," +
                " aprile=?," +
                " maggio=?," +
                " giugno=?," +
                " luglio=? " +
                "WHERE iscritto=" + iscritto.getIdDatabase() + " AND " +
                "corso=" + iscritto.getPalestra().getId() + ";");

        stmt.bindString(1, iscritto.getImporti().getIscrizione());
        stmt.bindString(2, iscritto.getImporti().getSettembre());
        stmt.bindString(3, iscritto.getImporti().getOttobre());
        stmt.bindString(4, iscritto.getImporti().getNovembre());
        stmt.bindString(5, iscritto.getImporti().getDicembre());
        stmt.bindString(6, iscritto.getImporti().getGennaio());
        stmt.bindString(7, iscritto.getImporti().getFebbraio());
        stmt.bindString(8, iscritto.getImporti().getMarzo());
        stmt.bindString(9, iscritto.getImporti().getAprile());
        stmt.bindString(10, iscritto.getImporti().getMaggio());
        stmt.bindString(11, iscritto.getImporti().getGiugno());
        stmt.bindString(12, iscritto.getImporti().getLuglio());
        stmt.execute();

    }

    public Importi caricaImporti(Query context, Iscritto iscritto, Corso corso) {

        Importi pagamenti = new Importi();

        SQLiteDatabase database = context.getReadableDatabase();

        try {
            //estraggo i dati degli iscritti e dei pagamenti
            Cursor risultati = database.rawQuery(
                    "SELECT " +
                            "iscrizione, " +
                            "settembre, ottobre, novembre, dicembre, gennaio, febbraio, " +
                            "marzo, aprile, maggio, " +
                            "giugno, luglio " +
                            "FROM Importi " +
                            "WHERE iscritto=" + iscritto.getIdDatabase() +
                            " AND corso=" + corso.getId(), null);

            if (risultati.getCount() == 0) {
                Log.v("Risultati", "nessun risultato ");
                return pagamenti;
            }

            risultati.moveToFirst();

            pagamenti.setIscrizione(risultati.getString(0));
            pagamenti.setSettembre(risultati.getString(1));
            pagamenti.setOttobre(risultati.getString(2));
            pagamenti.setNovembre(risultati.getString(3));
            pagamenti.setDicembre(risultati.getString(4));
            pagamenti.setGennaio(risultati.getString(5));
            pagamenti.setFebbraio(risultati.getString(6));
            pagamenti.setMarzo(risultati.getString(7));
            pagamenti.setAprile(risultati.getString(8));
            pagamenti.setMaggio(risultati.getString(9));
            pagamenti.setGiugno(risultati.getString(10));
            pagamenti.setLuglio(risultati.getString(11));

            risultati.close();

        } catch (SQLiteException e) {    //se non è stata trovata nessuna tabella importi

            e.printStackTrace();

            SQLiteDatabase db = context.getWritableDatabase();

            db.execSQL("CREATE TABLE Importi (" +
                    Tabelle.InfoTabelle.pagamento[0] + " INTEGER , " + //iscritto
                    Tabelle.InfoTabelle.pagamento[1] + " INTEGER, " + //corso
                    Tabelle.InfoTabelle.pagamento[2] + " TEXT DEFAULT 0, " + //giorno presenza
                    Tabelle.InfoTabelle.pagamento[3] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[4] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[5] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[6] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[7] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[8] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[9] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[10] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[11] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[12] + " TEXT DEFAULT 0, " +
                    Tabelle.InfoTabelle.pagamento[13] + " TEXT DEFAULT 0, " +
                    "FOREIGN KEY(iscritto) REFERENCES Iscritto(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "FOREIGN KEY(corso) REFERENCES Corso(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                    "PRIMARY KEY (iscritto, corso) )");

            db.close();
        }

        return pagamenti;

    }

}
