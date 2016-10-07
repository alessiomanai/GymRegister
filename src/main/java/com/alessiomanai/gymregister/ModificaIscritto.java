package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.utils.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ModificaIscritto extends Activity {

    static Iscritto iscritto;
    static Corso palestra;
    static int posizione;    //posizione dell'iscritto all'interno della listview
    String nome, indirizzo, telefono, datadinascita, citta;
    EditText nomed, indirizzod, telefonod, datadinascitad, cittad;
    DatePickerFragment datePickerFragment;
    boolean isResumed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_iscritto);

        isResumed = false;  // utilizzo questo per gestire il focus in modo corretto
        datePickerFragment = new DatePickerFragment();

        //elimino l'ultimo spazio dalle stringhe acquisite
        nome = iscritto.getId();
        indirizzo = iscritto.getIndirizzo();
        telefono = iscritto.getTelefono();
        datadinascita = iscritto.getDataDiNascita();
        citta = iscritto.getCitta();


        //collego gli oggetti alla gui
        nomed = (EditText) findViewById(R.id.nec5);
        indirizzod = (EditText) findViewById(R.id.address5);
        telefonod = (EditText) findViewById(R.id.telefonoedit5);
        datadinascitad = (EditText) findViewById(R.id.detnasc5);
        cittad = (EditText) findViewById(R.id.detcit5);

        //inserisco le stringhe da modificare nell edittext
        nomed.setText(nome);
        indirizzod.setText(indirizzo);
        telefonod.setText(telefono);
        datadinascitad.setText(datadinascita);
        cittad.setText(citta);

        ImageButton conferma = (ImageButton) findViewById(R.id.confermabut5);

        // il popup con il date picker si lancia al click sul campo di testo...
        datadinascitad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // ... ed anche al focus tramite tastiera
        datadinascitad.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus && isResumed) {
                    datePickerFragment.show(getFragmentManager(), "datePicker");
                }
            }
        });

        // ci registriamo agli eventi del popup (ok e annulla)
        datePickerFragment.setOnDatePickerFragmentChanged(new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDatePickerFragmentOkButton(DialogFragment dialog, Calendar date) {
                // trasferiamo il valore sul campo di testo
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                datadinascitad.setText(format.format(date.getTime()));
            }

            @Override
            public void onDatePickerFragmentCancelButton(DialogFragment dialog) {
                // non facciamo nulla
            }
        });

        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if (isEmpty(nomed)) {

                    nomed.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    nomed.setError(getResources().getString(R.string.noName));

                    Toast.makeText(getApplicationContext(), R.string.noName, Toast.LENGTH_LONG).show();

                } else {

                    iscritto.setId(nomed.getText().toString());
                    iscritto.setIndirizzo(indirizzod.getText().toString());
                    iscritto.setTelefono(telefonod.getText().toString());
                    iscritto.setDataDiNascita(datadinascitad.getText().toString());
                    iscritto.setCitta(cittad.getText().toString());

                    salva();

                    conferma();

                    Intent asd = new Intent(getBaseContext(), GestioneIscritti.class);

                    startActivity(asd);        //avvia la finestra corrispondente

                    finish();

                }
            }
        });
    }

    /**
     * toast di conferma
     */
    void conferma() {

        Toast.makeText(this, "Modifica effettuata", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);

        finish();
    }    //fine tasto back


    /**
     * salva i dati nella struttura precedente
     */
    void salva() {

        QueryIscritto database = new QueryIscritto(this);

        database.aggiorna(database, iscritto);

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
