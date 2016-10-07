package com.alessiomanai.gymregister.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;

import java.util.ArrayList;


public class QueryPagamentoCustom extends QueryPagamento {

    private String nomeTabella = "PagamentiCustom";

    public QueryPagamentoCustom(Context context) {
        super(context);

    }

    public void creaNuovoPagamento(Query context, Corso corso, String nuovoPagamento) {

        SQLiteDatabase database = context.getWritableDatabase();

        database.execSQL("CREATE TABLE PagamentiCustom(corso INTEGER, pagamento TEXT NOT NULL, " +
                "FOREIGN KEY(corso) REFERENCES Corso(id) ON UPDATE CASCADE ON DELETE NO ACTION," +
                "PRIMARY KEY (corso, pagamento) )");

        database.execSQL("INSERT INTO PagamentiCustom(corso, pagamento) " +
                "VALUES (" + corso.getId() + ", '" + nuovoPagamento + "')");


        database.execSQL("CREATE TABLE " + nuovoPagamento + "(iscritto INTEGER, " +
                "corso INTEGER, " +
                "pagato INTEGER, " +
                "FOREIGN KEY(iscritto) REFERENCES Iscritto(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                "FOREIGN KEY(corso) REFERENCES Corso(id) ON UPDATE CASCADE ON DELETE NO ACTION, " +
                "PRIMARY KEY (iscritto, corso) )");

    }

    public void aggiornaPagamento(Query context, String pagamento, Iscritto iscritto, int tipo) {

        SQLiteDatabase database = context.getWritableDatabase();

        database.execSQL("INSERT INTO " + pagamento + " (iscritto, corso, pagato) VALUES " +
                "(" + iscritto.getIdDatabase() + ", " + iscritto.getIdCorso() + ", " +
                "" + tipo + ") ");

    }

    /**
     * carica tutti i caricamenti custom esistenti
     */
    public ArrayList<String> carica(Query context, Corso id) {

        ArrayList<String> pagamentiCustom = new ArrayList<>();

        SQLiteDatabase database = context.getReadableDatabase();

        Cursor risultati = database.rawQuery("SELECT pagamento FROM PagamentiCustom " +
                "WHERE corso=" + id.getId(), null);


        if (risultati.getCount() == 0) {
            Log.v("Risultati join", "nessun risultato ");
            return pagamentiCustom;
        }

        risultati.moveToFirst();

        do {

            pagamentiCustom.add(risultati.getString(0));

        } while (risultati.moveToNext());

        risultati.close();

        return pagamentiCustom;

    }

    public int caricaPagamentiIscritto(Query context, String pagamento, Iscritto iscritto) {

        int pagato;

        SQLiteDatabase database = context.getReadableDatabase();

        Cursor risultati = database.rawQuery("SELECT pagato FROM " + pagamento + " " +
                "WHERE corso=" + iscritto.getPalestra().getId() +
                " AND iscritto=" + iscritto.getIdDatabase(), null);

        risultati.moveToFirst();

        if (risultati.getCount() == 0) {
            Log.v("Risultati join", "nessun risultato ");
            return 0;
        }

        pagato = Integer.parseInt(risultati.getString(0));

        risultati.close();

        return pagato;

    }

}
