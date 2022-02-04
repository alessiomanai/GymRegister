package com.alessiomanai.gymregister;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.ListatoreIscritti;
import com.alessiomanai.gymregister.utils.activity.GymRegisterBaseActivity;


public class Risultati extends GymRegisterBaseActivity {

    private ArrayList<Iscritto> risultati = new ArrayList<>();
    EditText search;
    ImageButton cerca;
    Corso palestra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risultati);

        palestra = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);
        risultati = (ArrayList<Iscritto>) getIntent().getExtras().get(ExtrasConstants.ISCRITTO);

        search = findViewById(R.id.search1);
        cerca = findViewById(R.id.buttoncerca1);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                avviaricerca();
            }
        });

        if (risultati.size() == 0) {
            Toast.makeText(this, R.string.notfund, Toast.LENGTH_SHORT).show();
        } else {

            ListView list1 = this.findViewById(R.id.results);
            ListatoreIscritti adapter = new ListatoreIscritti(Risultati.this, risultati);
            list1.setAdapter(adapter);

            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long idonet) {    //a seconda della posizione (rilevata automaticamente) apro un profilo

                    getDettagliActivity(position, risultati.get(position), risultati.get(position).getPalestra());

                    risultati.clear();

                    finish();
                }
            });


        }
    }

    void avviaricerca() {

        String chiave;

        chiave = search.getText().toString();

        QueryIscritto database = (QueryIscritto) QueryIscritto.getInstance(this);
        risultati = database.cercaIscritto(palestra, chiave);

        if (risultati.size() != 0) {

            getRisultatiRicerca(palestra, risultati);

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

        getGestioneIscritti(palestra);

        finish();

    }    //fine tasto back
}
