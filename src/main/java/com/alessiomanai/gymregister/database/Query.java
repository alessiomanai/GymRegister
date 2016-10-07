package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by alessio on 01/09/16.
 * Classe di gestione query
 */

public class Query extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    public Query(Context context) {
        super(context, Tabelle.InfoTabelle.DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("Database", "creato");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {

        sdb.execSQL(creaTabellaIscritto());
        sdb.execSQL(creaTabellaCorso());
        sdb.execSQL(creaTabellaPagamento());
        sdb.execSQL(creaTabellaPresenze());
        Log.v("Tabelle", "creato");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

    private String creaTabellaIscritto() {

        return "CREATE TABLE Iscritto (" +
                Tabelle.InfoTabelle.iscritto[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " + //id
                Tabelle.InfoTabelle.iscritto[1] + " TEXT NOT NULL, " + //nome
                Tabelle.InfoTabelle.iscritto[2] + " TEXT      , " + //data di nascita
                Tabelle.InfoTabelle.iscritto[3] + " TEXT      , " + //telefono
                Tabelle.InfoTabelle.iscritto[4] + " TEXT      , " + //indirizzo
                Tabelle.InfoTabelle.iscritto[5] + " TEXT      , " + //citta
                Tabelle.InfoTabelle.iscritto[6] + " INTEGER      , " + //corso
                Tabelle.InfoTabelle.iscritto[7] + " TEXT      , " +
                Tabelle.InfoTabelle.iscritto[8] + " TEXT      , " +
                "FOREIGN KEY(corso) REFERENCES Corso(id) ON UPDATE CASCADE ON DELETE NO ACTION);";

    }

    private String creaTabellaCorso() {

        return "CREATE TABLE Corso (" +
                Tabelle.InfoTabelle.corso[0] + " INTEGER PRIMARY KEY   AUTOINCREMENT, " + //id
                Tabelle.InfoTabelle.corso[1] + " TEXT      NOT NULL, " + //nome
                Tabelle.InfoTabelle.corso[2] + " TEXT      DEFAULT NULL )";  //NOTE PATH
    }

    private String creaTabellaPresenze() {

        return "CREATE TABLE Presenze (" +
                Tabelle.InfoTabelle.presenze[0] + " INTEGER , " + //iscritto
                Tabelle.InfoTabelle.presenze[1] + " INTEGER, " + //corso
                Tabelle.InfoTabelle.presenze[2] + " TEXT NOT NULL, " + //giorno presenza
                "FOREIGN KEY (iscritto) REFERENCES Iscritto(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                "FOREIGN KEY (corso) REFERENCES Corso(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                "PRIMARY KEY (iscritto, corso, " + Tabelle.InfoTabelle.presenze[2] + ") );";

    }

    private String creaTabellaPagamento() {

        return "CREATE TABLE Pagamento (" +
                Tabelle.InfoTabelle.pagamento[0] + " INTEGER , " + //iscritto
                Tabelle.InfoTabelle.pagamento[1] + " INTEGER, " + //corso
                Tabelle.InfoTabelle.pagamento[2] + " TEXT NOT NULL DEFAULT nonpagato, " + //giorno presenza
                Tabelle.InfoTabelle.pagamento[3] + " TEXT NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[4] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[5] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[6] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[7] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[8] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[9] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[10] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[11] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[12] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                Tabelle.InfoTabelle.pagamento[13] + " TEXT      NOT NULL DEFAULT nonpagato, " +
                "FOREIGN KEY(iscritto) REFERENCES Iscritto(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                "FOREIGN KEY(corso) REFERENCES Corso(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                "PRIMARY KEY (iscritto, corso) )";
    }


}
