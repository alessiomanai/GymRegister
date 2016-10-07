package com.alessiomanai.gymregister;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.database.QueryCorso;

import java.util.ArrayList;


public class Gestionepalestre extends Activity {

    private ArrayList<Corso> palestre = new ArrayList<>(); //stringhe selezione palestre

    /***
     * avvio del codice
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionepalestre);

        View noPalestre = findViewById(R.id.addGYM);

        palestre = caricaDatabase();

        bottoni();    //avvia i bottoni interattivi

        if (palestre.size() > 0) {    //se ci sono palestre in memoria mostra la lista di selezione

            noPalestre.setVisibility(View.GONE);

            //collego la listview dell'inferfaccia
            ListView list1 = (ListView) this.findViewById(R.id.listView);
            ListatorePalestre adapter = new ListatorePalestre(Gestionepalestre.this, palestre);
            list1.setAdapter(adapter);

            //rendo la lista clickabile
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {    //a seconda della posizione (rilevata automaticamente) apro una palestra

                    GestioneIscritti.palestra = palestre.get(position);

                    avvia();

                }
            });        //fine lista clickabile


        } else {

            noPalestre.setVisibility(View.VISIBLE);

            noPalestre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    Intent aggiungi = new Intent(getBaseContext(), AggiungiPalestra.class);

                    //avvia la finestra corrispondente
                    startActivity(aggiungi);

                    palestre.clear();

                    finish();

                }
            });  //fine bottone

        }
    }    //fine oncreate


    /**
     * carica i dettagli delle palestre da database
     */
    ArrayList<Corso> caricaDatabase() {

        QueryCorso database = new QueryCorso(this);
        return database.getElencoCorsi(database);
    }

    /***
     * avvia l'intent degli iscritti
     */
    void avvia() {

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);
    }

    /***
     * pulsante del menù
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.infos:
                Intent info = new Intent(getBaseContext(), InfoApp.class);
                //avvia la finestra corrispondente
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /***
     * gestione dei bottoni
     */
    void bottoni() {

        ImageButton aggiungipalestra = (ImageButton) findViewById(R.id.paladd);
        ImageButton eliminapalestra = (ImageButton) findViewById(R.id.palelim);
        ImageButton rinominaPalestra = (ImageButton) findViewById(R.id.renameCorso);


        aggiungipalestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent aggiungi = new Intent(getBaseContext(), AggiungiPalestra.class);

                //avvia la finestra corrispondente
                startActivity(aggiungi);

                palestre.clear();

                finish();

            }
        });  //fine bottone


        rinominaPalestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent aggiungi = new Intent(getBaseContext(), RinominaCorso.class);

                //avvia la finestra corrispondente
                startActivity(aggiungi);

                palestre.clear();

                finish();

            }
        });  //fine bottone


        eliminapalestra.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                Intent elimina = new Intent(getBaseContext(), EliminaPalestre.class);

                //avvia la finestra corrispondente
                startActivity(elimina);

                palestre.clear();

                finish();
            }
        });

    }

}
