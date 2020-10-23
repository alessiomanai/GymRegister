package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryCorso;
import com.alessiomanai.gymregister.database.QueryIscritto;

import java.util.ArrayList;

public class CambiaCorso extends Activity {

    static Iscritto iscritto;
    private ArrayList<Corso> corsi = new ArrayList<>();
    private TextView istruzioni;
    private String testo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cambia_corso);

        istruzioni = findViewById(R.id.istruzioniCambio);

        testo = getString(R.string.istruzioniCambio) + " " + iscritto.getId();
        istruzioni.setText(testo);

        QueryCorso database = new QueryCorso(this);
        corsi = database.getElencoCorsi(database);

        if (corsi.size() > 0) {    //se ci sono palestre in memoria mostra la lista di selezione

            //collego la listview dell'inferfaccia
            ListView list1 = this.findViewById(R.id.listViewCambiaCorso);
            ListatorePalestre adapter = new ListatorePalestre(CambiaCorso.this, corsi);
            list1.setAdapter(adapter);

            //rendo la lista clickabile
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {    //a seconda della posizione (rilevata automaticamente) apro una palestra

                    //finestra di conferma
                    AlertDialog.Builder builder = new AlertDialog.Builder(CambiaCorso.this);

                    builder.setTitle(R.string.conferma);
                    builder.setMessage(getString(R.string.haiSelezionato) + ": " +
                            corsi.get(position).getNome());

                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.conferma, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Confermato!

                            QueryIscritto database = new QueryIscritto(getApplicationContext());

                            if (database.cambiaCorsoUtente(database, iscritto, corsi.get(position))) {   //cambia il corso all'utente selezionato

                                Toast.makeText(CambiaCorso.this, R.string.opdone, Toast.LENGTH_SHORT).show();
                            }

                            finish();

                            dialog.dismiss();

                        }
                    });
                    //negativa
                    builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Annullato!
                            Toast.makeText(CambiaCorso.this, R.string.opannul, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });
                    //visualizza la finestra
                    builder.show();

                }
            });        //fine lista clickabile

        } else
            Toast.makeText(this, R.string.nopal, Toast.LENGTH_SHORT).show();


    }


    /***
     * alla pressione del tasto back
     */
    @Override
    public void onBackPressed() {

        corsi.clear();    //dealloca l'array

        finish();
    }    //fine tasto back


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        iscritto.setId(savedInstanceState.getString("iduser"));

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("iduser", iscritto.getId());
        super.onSaveInstanceState(outState);
    }

}
