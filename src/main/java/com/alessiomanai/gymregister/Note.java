package com.alessiomanai.gymregister;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;


public class Note extends Activity {

    static Corso corso = null;
    static Iscritto iscritto = null;
    static boolean noteIscritto;
    String nomeid;
    TextView nomeT;
    EditText testo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        final String file;
        ImageButton salva;

        //nome del file
        if (noteIscritto) {

            file = iscritto.getNote();
            nomeid = iscritto.getId();

        } else {

            file = corso.getNoteCorso();
            nomeid = corso.getNome();
        }

        nomeT = (TextView) findViewById(R.id.noteT);
        nomeT.setText(nomeid);

        testo = (EditText) findViewById(R.id.noteline);

        //carica eventuali note esistenti
        try {
            carica(file);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //salvataggio
        salva = (ImageButton) findViewById(R.id.salvanota);

        salva.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                try {
                    salva(file);
                } catch (IOException e) {

                    e.printStackTrace();
                }

                Toast.makeText(Note.this, R.string.notesav, Toast.LENGTH_SHORT).show();

            }

        });
    }

    //carica il testo dal file
    void carica(String file) throws IOException {

        String nita;
        FileInputStream in = openFileInput(file);
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String totale = "";

        while ((nita = bufferedReader.readLine()) != null) {
            totale += (nita + "\n");
        }

        testo.setText(totale);

        in.close();

    }

    void salva(String file) throws IOException {

        String tx;
        tx = testo.getText().toString();

        OutputStreamWriter fileasd = new OutputStreamWriter(openFileOutput(file, Context.MODE_PRIVATE));

        fileasd.write(tx);

        fileasd.close();

    }

}
