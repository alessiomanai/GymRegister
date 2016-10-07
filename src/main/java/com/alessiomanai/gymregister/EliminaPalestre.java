package com.alessiomanai.gymregister;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.database.QueryCorso;

import java.util.ArrayList;

public class EliminaPalestre extends Activity {

    private ArrayList<Corso> palestre = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elimina_palestre);

        QueryCorso database = new QueryCorso(this);
        palestre = database.getElencoCorsi(database);

        if (palestre.size() > 0) {    //se ci sono palestre in memoria mostra la lista di selezione

            //collego la listview dell'inferfaccia
            ListView list1 = (ListView) this.findViewById(R.id.listView);
            ListatorePalestre adapter = new ListatorePalestre(EliminaPalestre.this, palestre);
            list1.setAdapter(adapter);

            //rendo la lista clickabile
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {    //a seconda della posizione (rilevata automaticamente) apro una palestra

                    //finestra di conferma
                    AlertDialog.Builder builder = new AlertDialog.Builder(EliminaPalestre.this);

                    builder.setTitle(R.string.conferma);
                    builder.setMessage(getString(R.string.haiSelezionato) + ": " +
                            palestre.get(position).getNome() + "\n" + getString(R.string.cleall));

                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Confermato!

                            cancellaCorsoDatabase(position);

                            Intent asd = new Intent(getBaseContext(), Gestionepalestre.class);
                            //avvia la finestra corrispondente
                            startActivity(asd);

                            finish();

                            dialog.dismiss();

                        }
                    });
                    //negativa
                    builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Annullato!
                            Toast.makeText(EliminaPalestre.this, R.string.opannul, Toast.LENGTH_SHORT).show();

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

    //messaggio di conferma
    void conferma() {
        Toast.makeText(this, R.string.corcan, Toast.LENGTH_SHORT).show();

    }


    /***
     * alla pressione del tasto back
     */
    @Override
    public void onBackPressed() {

        Intent asd = new Intent(getBaseContext(), Gestionepalestre.class);

        //avvia la finestra corrispondente
        startActivity(asd);

        palestre.clear();    //dealloca l'array

        finish();
    }    //fine tasto back


    void cancellaCorsoDatabase(int posizione) {

        QueryCorso database = new QueryCorso(this);

        database.eliminaCorso(database, palestre.get(posizione));

        conferma();
    }

}
