package com.alessiomanai.gymregister;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.database.QueryCorso;


public class AggiungiPalestra extends Activity {

    EditText nomepalestra;
    ImageButton conferma;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi_palestra);

        //collego il codice alla gui
        nomepalestra = (EditText) findViewById(R.id.nec1);
        conferma = (ImageButton) findViewById(R.id.confermabut1);

        final QueryCorso database = new QueryCorso(this);

        /**alla pressione del bottone salva i dati*/
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(nomepalestra)) {

                    nomepalestra.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    nomepalestra.setError(getResources().getString(R.string.noName));

                    Toast.makeText(AggiungiPalestra.this, R.string.noName, Toast.LENGTH_LONG).show();

                } else {

                    nomepalestra.getBackground().clearColorFilter();

                    s = nomepalestra.getText().toString();

                    s = s.replace("-", "_");    //sanitize input
                    s = s.replace("#", "_");

                    Corso nuovoCorso = new Corso(s);
                    database.nuovo(database, nuovoCorso);


                    //Toast
                    Toast.makeText(AggiungiPalestra.this, R.string.addgim, Toast.LENGTH_LONG).show();

                    //torna all'activity precedente
                    Intent gestione = new Intent(getBaseContext(), Gestionepalestre.class);
                    //avvia la finestra corrispondente
                    startActivity(gestione);

                    finish();
                }
            }
        });
    }


    /***
     * alla pressione del tasto back
     */
    @Override
    public void onBackPressed() {

        Intent asd = new Intent(getBaseContext(), Gestionepalestre.class);

        //avvia la finestra corrispondente
        startActivity(asd);

        finish();
    }    //fine tasto back


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
