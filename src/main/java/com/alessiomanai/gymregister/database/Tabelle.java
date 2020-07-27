package com.alessiomanai.gymregister.database;

import android.provider.BaseColumns;

/**
 * Created by alessio on 01/09/16.
 *
 */
public class Tabelle {

    public Tabelle() {
    }

    public static abstract class InfoTabelle implements BaseColumns {

        public static final String DATABASE_NAME = "gymRegister.db";

        public static final String[] tabelle = new String[]{"Iscritto", "Corso", "Presenze", "Pagamento", "Importi", "Certificato"};

        /**
         * campi delle tabelle
         */
        public static final String[] iscritto = new String[]{"id", "nome", "dataDiNascita", "telefono", "indirizzo",
                "citta", "corso", "fotoProfilo", "notePath"};

        public static final String[] corso = new String[]{"id", "nome", "notePath"};

        public static final String[] presenze = new String[]{"iscritto", "corso", "giornoPresenza"};

        public static final String[] pagamento = new String[]{"iscritto", "corso", "iscrizione",
                "settembre", "ottobre", "novembre", "dicembre", "gennaio", "febbraio", "marzo", "aprile", "maggio",
                "giugno", "luglio"};

        public static final String[] importi = new String[]{"iscritto", "corso", "iscrizione",
                "settembre", "ottobre", "novembre", "dicembre", "gennaio", "febbraio", "marzo", "aprile", "maggio",
                "giugno", "luglio"};

        public static final String[] certificati = new String[]{"iscritto", "data"};

    }

}
