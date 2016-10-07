package com.alessiomanai.gymregister;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.utils.ListatoreIscritti;
import com.alessiomanai.gymregister.utils.Search;


public class Risultati extends Activity {

    public static ArrayList<Iscritto> risultati = new ArrayList<>();
    TextView notfound;
    static int posiz;
    EditText search;
    ImageButton cerca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati);

        search = (EditText) findViewById(R.id.search1);
        cerca = (ImageButton) findViewById(R.id.buttoncerca1);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                avviaricerca();

            }
        });

        if (risultati.size() == 0) {

            Toast.makeText(this, R.string.notfund, Toast.LENGTH_SHORT).show();

        } else {

            ListView list1 = (ListView) this.findViewById(R.id.results);
            ListatoreIscritti adapter = new ListatoreIscritti(Risultati.this, risultati);
            list1.setAdapter(adapter);

            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long idonet) {    //a seconda della posizione (rilevata automaticamente) apro un profilo

                    Dettagli.iscritto = risultati.get(position);

                    Dettagli.palestra = GestioneIscritti.palestra;

                    //avvia i dettagli utente
                    Intent dettagli = new Intent(getBaseContext(), Dettagli.class);
                    startActivity(dettagli);

                    risultati.clear();

                    finish();
                }
            });


        }
    }

    void avviaricerca() {

        String chiave;

        chiave = search.getText().toString();

        QueryIscritto database = new QueryIscritto(this);
        risultati = database.cercaIscritto(database, GestioneIscritti.palestra, chiave);

        if (risultati.size() != 0) {

            Intent risultatiIntent = new Intent(getBaseContext(), Risultati.class);
            //avvia la finestra corrispondente
            startActivity(risultatiIntent);

            finish();
        } else {
            Toast.makeText(this, R.string.notfund, Toast.LENGTH_SHORT).show();

            Intent risultatiIntent = new Intent(getBaseContext(), Risultati.class);
            //avvia la finestra corrispondente
            startActivity(risultatiIntent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        risultati.clear();

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);

        finish();

    }    //fine tasto back
}
