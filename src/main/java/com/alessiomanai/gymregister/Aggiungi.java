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
import com.alessiomanai.gymregister.database.QueryPagamento;
import com.alessiomanai.gymregister.utils.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Aggiungi extends Activity {

    static Corso palestra;
    EditText nomeecognome;
    EditText address;
    EditText telef, citf;
    View dantf;
    EditText datadinascita;
    String nec = null, addres = null, tel = null, citta, datanasc;
    DatePickerFragment datePickerFragment;
    boolean isResumed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi);


        isResumed = false;  // utilizzo questo per gestire il focus in modo corretto
        datePickerFragment = new DatePickerFragment();

        //collego le edit text
        nomeecognome = (EditText) findViewById(R.id.nec);
        address = (EditText) findViewById(R.id.address);
        telef = (EditText) findViewById(R.id.telefonoedit);
        dantf = findViewById(R.id.detnasc);
        citf = (EditText) findViewById(R.id.detcit);
        datadinascita = (EditText) findViewById(R.id.detnasc);

        ImageButton conferma = (ImageButton) findViewById(R.id.confermabut);

        // il popup con il date picker si lancia al click sul campo di testo...
        dantf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // ... ed anche al focus tramite tastiera
        dantf.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                datadinascita.setText(format.format(date.getTime()));
            }

            @Override
            public void onDatePickerFragmentCancelButton(DialogFragment dialog) {
                // non facciamo nulla
            }
        });

        //se viene premuto il bottone conferma salva i dati del nuovo utente
        conferma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(nomeecognome)) {

                    nomeecognome.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_IN);
                    nomeecognome.setError(getResources().getString(R.string.noName));

                    Toast.makeText(getApplicationContext(), R.string.noName, Toast.LENGTH_LONG).show();

                } else {

                    //conferma i nomi e aggiunge agli array
                    nec = nomeecognome.getText().toString();
                    addres = address.getText().toString();
                    tel = telef.getText().toString();
                    datanasc = datadinascita.getText().toString();
                    citta = citf.getText().toString();

                    nec = nec.replace("/", "_");    //sanitize input
                    addres = addres.replace("--", "-");    //sanitize input
                    addres = addres.replace("#", "-");    //sanitize input
                    tel = tel.replace("--", "-");    //sanitize input
                    datanasc = datanasc.replace("--", "-");    //sanitize input
                    citta = citta.replace("--", "-");    //sanitize input
                    citta = citta.replace("#", "-");    //sanitize input


                    if (nec == null)
                        nec = "<no name>";

                    if (addres == null)
                        addres = "<nessun indirizzo>";

                    if (tel == null)
                        tel = "<nessun numero>";

                    salva(new Iscritto(nec, tel, addres, citta, datanasc), palestra);    //crea un nuovo utente

                    //Toast
                    Toast.makeText(Aggiungi.this, R.string.addurs, Toast.LENGTH_LONG).show();

                    //torna all'activity precedente
                    Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

                    //avvia la finestra corrispondente
                    startActivity(gestioneiscritti);

                    finish();

                }
            }
        });


    }

    //se viene premuto il tasto indietro torna alla precedente activity senza
    @Override
    public void onBackPressed() {

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);

        finish();
    }

    void salva(Iscritto iscritto, Corso corso) {

        QueryIscritto database = new QueryIscritto(this);

        database.nuovo(database, iscritto, corso);

        QueryPagamento pagamento = new QueryPagamento(this);
        pagamento.inizializza(pagamento, iscritto, corso);

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
