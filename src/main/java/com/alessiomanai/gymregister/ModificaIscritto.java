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
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.activity.GymRegisterBaseActivity;

import java.util.Calendar;


public class ModificaIscritto extends GymRegisterBaseActivity {

    private Iscritto iscritto;
    private Corso palestra;
    private int posizione;    //posizione dell'iscritto all'interno della listview
    String nome, indirizzo, telefono, datadinascita, citta, stringCertificatoMedico;
    EditText nomed, indirizzod, telefonod, datadinascitad, cittad, certificatoMedico;
    private DatePickerDialog.OnDateSetListener mDateSetListener, certificatoDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_iscritto);

        palestra = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);
        posizione = (int) getIntent().getExtras().get(ExtrasConstants.POSITION);
        iscritto = (Iscritto) getIntent().getExtras().get(ExtrasConstants.ISCRITTO);

        //elimino l'ultimo spazio dalle stringhe acquisite
        nome = iscritto.getId();
        indirizzo = iscritto.getIndirizzo();
        telefono = iscritto.getTelefono();
        datadinascita = iscritto.getDataDiNascita();
        citta = iscritto.getCitta();
        stringCertificatoMedico = iscritto.getCertificatoMedico();


        //collego gli oggetti alla gui
        nomed = findViewById(R.id.nec5);
        indirizzod = findViewById(R.id.address5);
        telefonod = findViewById(R.id.telefonoedit5);
        datadinascitad = findViewById(R.id.detnasc5);
        cittad = findViewById(R.id.detcit5);
        certificatoMedico = findViewById(R.id.modificaCertificato);

        //inserisco le stringhe da modificare nell edittext
        nomed.setText(nome);
        indirizzod.setText(indirizzo);
        telefonod.setText(telefono);
        datadinascitad.setText(datadinascita);
        cittad.setText(citta);
        certificatoMedico.setText(stringCertificatoMedico);

        ImageButton conferma = findViewById(R.id.confermabut5);

        // il popup con il date picker si lancia al click sul campo di testo...
        datadinascitad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ModificaIscritto.this,
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
                        ModificaIscritto.this,
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
                datadinascitad.setText(date);
            }
        };

        certificatoDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                certificatoMedico.setText(date);
            }
        };

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
                    iscritto.setCertificatoMedico(certificatoMedico.getText().toString());

                    salva();

                    conferma();

                    getGestioneIscritti(palestra);

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

        getGestioneIscritti(palestra);

        finish();
    }    //fine tasto back


    /**
     * salva i dati nella struttura precedente
     */
    void salva() {

        QueryIscritto database = (QueryIscritto) QueryIscritto.getInstance(this);

        database.aggiorna(iscritto);

        QueryCertificati certificati = (QueryCertificati) QueryCertificati.getInstance(this);

        if (certificati.certificatoExists(iscritto)) {
            certificati.update(iscritto, iscritto.getCertificatoMedico());
        } else {
            certificati.nuovo(iscritto, iscritto.getCertificatoMedico());
        }

    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

}
