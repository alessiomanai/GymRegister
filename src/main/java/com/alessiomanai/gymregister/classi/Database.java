package com.alessiomanai.gymregister.classi;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * classe per facilitare l'utilizzo del database e creazione delle query
 * NON FUNZIONA
 */

public class Database extends SQLiteOpenHelper {

    private final String databasePath = "/data/data/com.alessiomanai.gymregister/databases/";
    private static final String DATABASE_NAME = "gymRegister.db";
    private SQLiteDatabase dataBase;
    private static final int DATABASE_VERSION = 1;

    /**
     * nomi delle tabelle
     */
    private String[] tabelle = new String[]{"Iscritto", "Corso", "Presenze", "Pagamento"};

    /**
     * campi delle tabelle
     */
    private String[] iscritto = new String[]{"id", "nome", "dataDiNascita", "telefono", "indirizzo",
            "citta", "corso", "fotoProfilo", "notePath"};

    private String[] corso = new String[]{"id", "nome", "notePath"};

    private String[] presenze = new String[]{"iscritto", "corso", "giornoPresenza"};

    private String[] pagamento = new String[]{"iscritto", "corso", "iscrizione",
            "settembre", "ottobre", "novembre", "dicembre", "gennaio", "febbraio", "marzo", "aprile", "maggio",
            "giugno", "luglio"};


    public Database(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //dataBase = SQLiteDatabase.openOrCreateDatabase(databasePath + DATABASE_NAME, null);    //apre o crea il database

    }


    // Questo metodo viene chiamato durante la creazione del database
    @Override
    public void onCreate(SQLiteDatabase database) {
        creaTabelle();
    }

    // Questo metodo viene chiamato durante l'upgrade del database, ad esempio quando viene incrementato il numero di versione
    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS contact");
        onCreate(database);

    }

    private void creaTabelle() {

        creaTabellaIscritto();
        creaTabellaCorso();
        creaTabellaPagamento();
        creaTabellaPresenze();

        dataBase.execSQL("SELECT * FROM Iscritto");
    }


    public void closeDB() {

        dataBase.close();   //chiude le connessioni al database
    }

    private void creaTabellaIscritto() {

        dataBase.execSQL("CREATE TABLE Iscritto (" +
                iscritto[0] + " INTEGER PRIMARY KEY   AUTOINCREMENT, " + //id
                iscritto[1] + " TEXT      NOT NULL, " + //nome
                iscritto[2] + " TEXT      , " + //data di nascita
                iscritto[3] + " TEXT      , " + //telefono
                iscritto[4] + " TEXT      , " + //indirizzo
                iscritto[5] + " TEXT      , " + //citta
                iscritto[6] + " INTEGER      , " + //corso
                iscritto[7] + " TEXT      , " +
                iscritto[8] + " TEXT      , " +
                "FOREIGN KEY(corso) REFERENCES Corso(id)");

    }

    private void creaTabellaCorso() {

        dataBase.execSQL("CREATE TABLE Corso (" +
                corso[0] + " INTEGER PRIMARY KEY   AUTOINCREMENT, " + //id
                corso[1] + " TEXT      NOT NULL, " + //nome
                corso[2] + " TEXT      NOT NULL ");  //NOTE PATH
    }

    private void creaTabellaPresenze() {

        dataBase.execSQL("CREATE TABLE Presenze (" +
                presenze[0] + " INTEGER , " + //iscritto
                presenze[1] + " INTEGER, " + //corso
                presenze[2] + " TEXT      NOT NULL, " + //giorno presenza
                "FOREIGN KEY(iscritto) REFERENCES Iscritto(id)" +
                "FOREIGN KEY(corso) REFERENCES Corso(id), " +
                "PRIMARY KEY (iscritto, corso)");

    }

    private void creaTabellaPagamento() {

        dataBase.execSQL("CREATE TABLE Presenze (" +
                pagamento[0] + " INTEGER , " + //iscritto
                pagamento[1] + " INTEGER, " + //corso
                pagamento[2] + " INTEGER      NOT NULL, " + //giorno presenza
                pagamento[3] + " INTEGER      NOT NULL, " +
                pagamento[4] + " INTEGER      NOT NULL, " +
                pagamento[5] + " INTEGER      NOT NULL, " +
                pagamento[6] + " INTEGER      NOT NULL, " +
                pagamento[7] + " INTEGER      NOT NULL, " +
                pagamento[8] + " INTEGER      NOT NULL, " +
                pagamento[9] + " INTEGER      NOT NULL, " +
                pagamento[10] + " INTEGER      NOT NULL, " +
                pagamento[11] + " INTEGER      NOT NULL, " +
                pagamento[12] + " INTEGER      NOT NULL, " +
                pagamento[13] + " INTEGER      NOT NULL, " +
                "FOREIGN KEY(iscritto) REFERENCES Iscritto(id)" +
                "FOREIGN KEY(corso) REFERENCES Corso(id), " +
                "PRIMARY KEY (iscritto, corso)");
    }

    public void aggiungiPalestra() {

        if (dataBase.isOpen()) {

            dataBase.execSQL("INSERT INTO Corso()" +
                    "VALUES ()");
        }

    }

}
