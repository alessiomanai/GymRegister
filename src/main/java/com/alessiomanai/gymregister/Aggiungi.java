package com.alessiomanai.gymregister;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryCertificati;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPagamento;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.activity.GymRegisterBaseActivity;

import java.util.Calendar;

public class Aggiungi extends GymRegisterBaseActivity {

    Corso palestra;
    EditText nomeecognome;
    EditText address;
    EditText telef, citf;
    View dantf, certificatoMedico;
    EditText datadinascita, editCertificatoMedico;
    String nec = null, addres = null, tel = null, citta, datanasc, stringCertificatoMedico = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener, certificatoDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aggiungi);

        palestra = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);

        //collego le edit text
        nomeecognome = findViewById(R.id.nec);
        address = findViewById(R.id.address);
        telef = findViewById(R.id.telefonoedit);
        dantf = findViewById(R.id.detnasc);
        citf = findViewById(R.id.detcit);
        datadinascita = findViewById(R.id.detnasc);
        certificatoMedico = findViewById(R.id.aggiungiCertificato);
        editCertificatoMedico = findViewById(R.id.aggiungiCertificato);

        ImageButton conferma = (ImageButton) findViewById(R.id.confermabut);

        // il popup con il date picker si lancia al click sul campo di testo...
        dantf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Aggiungi.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        certificatoMedico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Aggiungi.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        certificatoDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                datadinascita.setText(date);
            }
        };

        certificatoDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                editCertificatoMedico.setText(date);
            }
        };

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
                    stringCertificatoMedico = editCertificatoMedico.getText().toString();

                    nec = nec.replace("/", "_");    //sanitize input
                    addres = addres.replace("--", "-");    //sanitize input
                    addres = addres.replace("#", "-");    //sanitize input
                    tel = tel.replace("--", "-");    //sanitize input
                    datanasc = datanasc.replace("--", "-");    //sanitize input
                    citta = citta.replace("--", "-");    //sanitize input
                    citta = citta.replace("#", "-");    //sanitize input


                    if (nec == null || nec.equals(""))
                        nec = "<no name>";

                    if (addres == null || addres.equals(""))
                        addres = "<nessun indirizzo>";

                    if (tel == null || tel.equals(""))
                        tel = "<nessun numero>";

                    salva(new Iscritto(nec, tel, addres, citta, datanasc), palestra, stringCertificatoMedico);    //crea un nuovo utente

                    //Toast
                    Toast.makeText(Aggiungi.this, R.string.addurs, Toast.LENGTH_LONG).show();

                    getGestioneIscritti(palestra);
                    finish();
                }
            }
        });


    }

    //se viene premuto il tasto indietro torna alla precedente activity senza
    @Override
    public void onBackPressed() {
        getGestioneIscritti(palestra);
        finish();
    }

    void salva(Iscritto iscritto, Corso corso, String certificatoMedico) {

        QueryIscritto database = (QueryIscritto) QueryIscritto.getInstance(this);

        database.nuovo(iscritto, corso);
        iscritto.setIdDatabase(database.selectLastIDIscritto());

        QueryPagamento pagamento = QueryPagamento.getInstance(this);
        pagamento.inizializza(iscritto, corso);

        QueryCertificati certificati = QueryCertificati.getInstance(this);
        certificati.nuovo(iscritto, certificatoMedico);

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
