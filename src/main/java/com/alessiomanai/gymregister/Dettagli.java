package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPagamento;

public class Dettagli extends Activity {

    static public TextView riepilogoPagamentiT;
    static int posizione;    //posizione all' interno dell array utenti
    static Corso palestra;        //nome palestra usata
    static Iscritto iscritto;   //dettagli utente
    ImageButton modifica;
    ImageButton elimina, pagamenti, presenze, note;

    public static void caricaRiepilogoPagamenti(Context context) {

        String riepilogo = context.getResources().getString(R.string.riepilogo);
        boolean nessunPagamento = true;

        riepilogo += " " + context.getResources().getString(R.string.pay) + ":\n";

        if (iscritto.getIscrizione().charAt(0) == 'p') {
            riepilogo += "" + context.getResources().getString(R.string.iscrizione);
            nessunPagamento = false;
        }

        if (iscritto.getSettembre().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.sept);
            nessunPagamento = false;
        }

        if (iscritto.getOttobre().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.oct);
            nessunPagamento = false;
        }

        if (iscritto.getNovembre().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.nov);
            nessunPagamento = false;
        }

        if (iscritto.getDicembre().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.dec);
            nessunPagamento = false;
        }

        if (iscritto.getGennaio().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.jan);
            nessunPagamento = false;
        }

        if (iscritto.getFebbraio().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.feb);
            nessunPagamento = false;
        }

        if (iscritto.getMarzo().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.mar);
            nessunPagamento = false;
        }

        if (iscritto.getAprile().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.apr);
            nessunPagamento = false;
        }

        if (iscritto.getMaggio().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.mag);
            nessunPagamento = false;
        }

        if (iscritto.getGiugno().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.jun);
            nessunPagamento = false;
        }

        if (iscritto.getLuglio().charAt(0) == 'p') {
            riepilogo += " " + context.getResources().getString(R.string.jul);
            nessunPagamento = false;
        }

        if (nessunPagamento) {
            riepilogo += context.getResources().getString(R.string.nopay);
        }

        riepilogoPagamentiT.setText(riepilogo);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli);

        //variabili textview
        TextView detid, detaddr, dettel, detnasc, detcit;

        //collego le textview alla gui
        detid = (TextView) findViewById(R.id.detid);
        detaddr = (TextView) findViewById(R.id.detaddr);
        dettel = (TextView) findViewById(R.id.dettel);
        detnasc = (TextView) findViewById(R.id.detnasc);
        detcit = (TextView) findViewById(R.id.detcit);

        //mostra a schermo i dettagli utente
        detid.setText(iscritto.getId());
        detaddr.setText(iscritto.getIndirizzo());
        dettel.setText(iscritto.getTelefono());
        detnasc.setText(iscritto.getDataDiNascita());
        detcit.setText(iscritto.getCitta());

        riepilogoPagamentiT = (TextView) findViewById(R.id.riepilogoPagamenti);
        caricaRiepilogoPagamenti(this);

        modifica();
        final QueryIscritto database = new QueryIscritto(this);

        elimina = (ImageButton) findViewById(R.id.buttoneli1);
        pagamenti = (ImageButton) findViewById(R.id.buttonpay);
        presenze = (ImageButton) findViewById(R.id.buttonPresenzeUtente);
        note = (ImageButton) findViewById(R.id.buttonNote);


        note.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                //invio i dati dell' utente
                Note.iscritto = iscritto;
                Note.noteIscritto = true;

                Intent noteI = new Intent(getBaseContext(), Note.class);
                startActivity(noteI);

            }
        });

        pagamenti.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                PagamentiIscritto.corso = palestra;
                PagamentiIscritto.iscritto = iscritto;
                PagamentiIscritto.posizione = posizione;

                Intent asd = new Intent(getBaseContext(), PagamentiIscritto.class);
                startActivity(asd);

            }
        });

        presenze.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                PresenzeUtente.iscritto = iscritto;

                Intent asd = new Intent(getBaseContext(), PresenzeUtente.class);
                startActivity(asd);

            }
        });

        //al click cancella l'utente visualizzato
        elimina.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Dettagli.this);

                builder.setTitle(R.string.conferma);
                builder.setMessage(R.string.elimusr);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Confermato!
                        database.elimina(database, iscritto);

                        confermaeli();    //conferma l'avvenuto eliminazione con un messaggio

                        Intent asd = new Intent(getBaseContext(), GestioneIscritti.class);
                        startActivity(asd);

                        finish();    //chiude l'activity

                        dialog.dismiss();    //chiude la finestra di dialogo
                    }
                });

                builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        // Annullato!
                        Toast.makeText(Dettagli.this, R.string.opannul, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                builder.show();


            }
        });

    }    //fine oncreate

    @Override
    public void onBackPressed() {

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);

        salvaModifiche();

        finish();
    }    //fine tasto back

    /**
     * messaggio di conferma eliminazione
     */
    void confermaeli() {
        Toast.makeText(this, R.string.utel, Toast.LENGTH_SHORT).show();
    }

    /***
     * pulsante del menù
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.note: {

                //invio i dati dell' utente
                Note.iscritto = iscritto;
                Note.noteIscritto = true;

                Intent note = new Intent(getBaseContext(), Note.class);

                //avvia la finestra corrispondente
                startActivity(note);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void modifica() {

        modifica = (ImageButton) findViewById(R.id.buttonmod1);

        modifica.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                ModificaIscritto.iscritto = iscritto;
                ModificaIscritto.palestra = palestra;
                ModificaIscritto.posizione = posizione;

                Intent modifica = new Intent(getBaseContext(), ModificaIscritto.class);
                startActivity(modifica);

                finish();
            }
        });

    }


    /**
     * salva i dati di tutti gli utenti
     */

    void salvaModifiche() {

        QueryPagamento pagamento = new QueryPagamento(this);
        pagamento.update(pagamento, iscritto);

    }

}
