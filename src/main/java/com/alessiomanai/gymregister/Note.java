package com.alessiomanai.gymregister;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Note extends Activity {

    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;
    String nomeid;
    TextView nomeT;
    EditText testo;
    private Corso corso;
    private Iscritto iscritto;
    private boolean noteIscritto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        View root = findViewById(R.id.parentRelativeNote);

        ViewCompat.setOnApplyWindowInsetsListener(root, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                v.setPadding(0, top, 0, 0);
                return insets;
            }
        });

        noteIscritto = (boolean) getIntent().getExtras().get(ExtrasConstants.NOTE_ISCRITTO);
        iscritto = (Iscritto) getIntent().getExtras().get(ExtrasConstants.ISCRITTO);
        corso = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);

        final String file;
        ImageButton salva;
        int permissionCheck = ContextCompat.checkSelfPermission(Note.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //nome del file
        if (noteIscritto) {

            file = iscritto.getNote();
            nomeid = iscritto.getId();

        } else {

            file = corso.getNoteCorso();
            nomeid = corso.getNome();
        }

        nomeT = findViewById(R.id.noteT);
        nomeT.setText(nomeid);

        testo = findViewById(R.id.noteline);

        //richiedere permessi scrittura

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Note.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Note.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Note.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        //carica eventuali note esistenti
        try {
            carica(file);
        } catch (IOException e) {
            Log.e("Errore durante il caricamento del file", e.getMessage(), e);
        }


        //salvataggio
        salva = findViewById(R.id.salvanota);

        salva.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                try {
                    salva(file);

                    Toast.makeText(Note.this, R.string.notesav, Toast.LENGTH_SHORT).show();


                } catch (IOException e) {

                    Log.e("Errore durante il salvataggio del file", e.getMessage(), e);

                    Toast.makeText(Note.this, R.string.richiestaPermessi, Toast.LENGTH_SHORT).show();

                }


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
