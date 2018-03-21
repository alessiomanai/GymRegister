package com.alessiomanai.gymregister;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryCorso;
import com.alessiomanai.gymregister.database.QueryIscritto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class TrasferimentiNote extends Activity {

    ArrayList<Corso> corsi = new ArrayList<>();
    ArrayList<Iscritto> iscritti = new ArrayList<>();
    String testoDaCopiare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasferimenti_note);

        System.out.println("Trasferimento note");

        corsi = caricaDatabase();

        for (int i = 0; i < corsi.size(); i++) {

            iscritti = caricaIscritti(corsi.get(i));

            //copia i testi dei file palestra
            try {
                testoDaCopiare = carica(getFileName(corsi.get(i).getNome(),
                        corsi.get(i).getNome(), ""));

                salva(corsi.get(i).getNoteCorso(), testoDaCopiare);

            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < iscritti.size(); j++) {

                //copia i testi dei file iscritti
                try {
                    testoDaCopiare = carica(getFileName(corsi.get(i).getNome(),
                            iscritti.get(j).getId(), iscritti.get(j).getIndirizzo() + iscritti.get(j).getTelefono()));

                    salva(iscritti.get(j).getNote(), testoDaCopiare);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        SharedPreferences pref = getSharedPreferences("Note", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit(); //edita il valore di Boot
        editor.putInt("Note", 1);    //associa all' hash di Application il valore
        editor.apply();    //convalida le modifiche

        Intent a = new Intent(getBaseContext(), Gestionepalestre.class);
        //avvia la finestra corrispondente
        startActivity(a);
        finish();    //termina la activity

    }


    //carica il testo dal file
    public String carica(String file) throws IOException {

        String nita;
        String totale = "";

        try {

            FileInputStream in = openFileInput(file);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


            while ((nita = bufferedReader.readLine()) != null) {
                totale += (nita + "\n");
            }

            in.close();

        } catch (IllegalArgumentException e) {

            Log.v("Error", "nomenclatura file errata - " + e);
            totale = " ";
        }

        return totale;
    }


    public void salva(String file, String testo) throws IOException {

        OutputStreamWriter fileasd = new OutputStreamWriter(openFileOutput(file, Context.MODE_WORLD_READABLE));

        fileasd.write(testo);

        fileasd.close();

    }


    public String getFileName(String palestra, String nomeid, String data) {

        return "note" + palestra + nomeid + data + ".txt";

    }


    ArrayList<Corso> caricaDatabase() {

        QueryCorso database = new QueryCorso(this);
        return database.getElencoCorsi(database);
    }


    public ArrayList<Iscritto> caricaIscritti(Corso palestra) {

        QueryIscritto database = new QueryIscritto(this);

        return database.caricaIscritti(database, palestra);

    }

}
