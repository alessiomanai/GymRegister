package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.classi.Presenza;
import com.alessiomanai.gymregister.database.QueryPresenze;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.ListatoreDettaglioPresenze;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class PresenzeUtente extends Activity implements AdapterView.OnItemSelectedListener {

    private Iscritto iscritto;
    private ArrayList<Presenza> elencoPresenze = new ArrayList<>();
    private ListView list1;
    private ListatoreDettaglioPresenze adapter;
    ArrayList<Presenza> presenzeSelezionate = new ArrayList<>();
    ImageButton ordinaAsc, ordinaDesc, aggiungiPresenza;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenze_utente);

        iscritto = (Iscritto) getIntent().getExtras().get(ExtrasConstants.ISCRITTO);

        ordinaAsc = findViewById(R.id.asc);
        ordinaDesc = findViewById(R.id.desc);
        aggiungiPresenza = findViewById(R.id.aggiungiPresenzaIcon);


        TextView testo = findViewById(R.id.userPres);

        try {
            testo.setText(iscritto.getId());
        } catch (NullPointerException e) {
            testo.setText(" ");
        }

        Spinner spinner = findViewById(R.id.meseSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapterSpinner);

        try {
            QueryPresenze database = (QueryPresenze) QueryPresenze.getInstance(this);
            elencoPresenze = database.presenzeIscritto(iscritto);
        } catch (NullPointerException ne){
            ne.printStackTrace();
            Toast.makeText(this,
                    "Something went wrong. Please delete user", Toast.LENGTH_LONG).show();
        }

        Collections.sort(elencoPresenze, new Comparator<Presenza>() {
            public int compare(Presenza s1, Presenza s2) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
                Date convertedDate = new Date();
                Date convertedDate2 = new Date();

                try {
                    convertedDate = dateFormat.parse(s1.getData());
                    convertedDate2 = dateFormat.parse(s2.getData());

                } catch (ParseException e) {

                    e.printStackTrace();
                }

                return convertedDate.compareTo(convertedDate2);
            }
        });

        list1 = this.findViewById(R.id.listViewPresenzeD);
        adapter = new ListatoreDettaglioPresenze(this, elencoPresenze);
        list1.setAdapter(adapter);
        mostranumero(presenzeSelezionate);


        if (elencoPresenze.size() == 0) {
            Toast.makeText(this, R.string.noPres, Toast.LENGTH_SHORT).show();
        }

        ordinaAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(elencoPresenze, new Comparator<Presenza>() {
                    public int compare(Presenza s1, Presenza s2) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
                        Date convertedDate = new Date();
                        Date convertedDate2 = new Date();

                        try {
                            convertedDate = dateFormat.parse(s1.getData());
                            convertedDate2 = dateFormat.parse(s2.getData());

                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        return convertedDate.compareTo(convertedDate2);
                    }
                });

                Collections.sort(presenzeSelezionate, new Comparator<Presenza>() {
                    public int compare(Presenza s1, Presenza s2) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
                        Date convertedDate = new Date();
                        Date convertedDate2 = new Date();

                        try {
                            convertedDate = dateFormat.parse(s1.getData());
                            convertedDate2 = dateFormat.parse(s2.getData());

                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        return convertedDate.compareTo(convertedDate2);
                    }
                });

                list1.setAdapter(adapter);

            }
        });

        ordinaDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Collections.sort(elencoPresenze, new Comparator<Presenza>() {
                    public int compare(Presenza s1, Presenza s2) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
                        Date convertedDate = new Date();
                        Date convertedDate2 = new Date();

                        try {
                            convertedDate = dateFormat.parse(s1.getData());
                            convertedDate2 = dateFormat.parse(s2.getData());

                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        return convertedDate2.compareTo(convertedDate);
                    }
                });

                Collections.sort(presenzeSelezionate, new Comparator<Presenza>() {
                    public int compare(Presenza s1, Presenza s2) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
                        Date convertedDate = new Date();
                        Date convertedDate2 = new Date();

                        try {
                            convertedDate = dateFormat.parse(s1.getData());
                            convertedDate2 = dateFormat.parse(s2.getData());

                        } catch (ParseException e) {

                            e.printStackTrace();
                        }

                        return convertedDate2.compareTo(convertedDate);
                    }
                });

                list1.setAdapter(adapter);

            }
        });

        aggiungiPresenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        PresenzeUtente.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String data;

                String mese = Integer.toString(month);

                if (month >= 1 && month <= 9){  //per visualizzare lo zero iniziale

                    mese = "0" + mese;

                }

                if (day < 10) {
                    String giorno = "0" + day;
                    data = giorno + "/" + mese + "/" + year;

                } else {
                    data = day + "/" + mese + "/" + year;
                }

                try {

                    QueryPresenze database = (QueryPresenze) QueryPresenze.getInstance(getApplicationContext());

                    database.aggiungiPresenzaPrecedente(iscritto, iscritto.getPalestra(), data);

                    Presenza nuovaPresenza = new Presenza();
                    nuovaPresenza.setData(data);

                    elencoPresenze.add(nuovaPresenza);

                    list1.setAdapter(adapter);

                    mostranumero(elencoPresenze);

                    Toast.makeText(getApplicationContext(), R.string.presAdd, Toast.LENGTH_SHORT).show();

                } catch (SQLiteConstraintException error) {

                    error.printStackTrace();

                    //finestra di conferma
                    AlertDialog.Builder builder = new AlertDialog.Builder(PresenzeUtente.this);

                    builder.setTitle(R.string.presenzagiadatabase);
                    builder.setMessage(R.string.presefrase);
                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Confermato!

                            dialog.dismiss();

                        }
                    });
                    builder.show();

                }
            }
        };

        spinner.setOnItemSelectedListener(this);

    }

    /**
     * a seconda del mese elezionato mostra le presenze
     */
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

        presenzeSelezionate.clear();

        if (pos == 0) {

            adapter = new ListatoreDettaglioPresenze(this, elencoPresenze);
            list1.setAdapter(adapter);
            mostranumero(elencoPresenze);

        }

        if (pos == 1) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/09") ||
                        elencoPresenze.get(i).getData().contains("/9/")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }
            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 2) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/10")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 3) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/11")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 4) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/12")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 5) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/01") ||
                        elencoPresenze.get(i).getData().contains("/1/")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 6) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/02") ||
                        elencoPresenze.get(i).getData().contains("/2/")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 7) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/03") ||
                        elencoPresenze.get(i).getData().contains("/3/")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 8) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/04") ||
                        elencoPresenze.get(i).getData().contains("/4")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 9) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/05") ||
                        elencoPresenze.get(i).getData().contains("/5")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 10) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/06") ||
                        elencoPresenze.get(i).getData().contains("/6")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 11) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/07") ||
                        elencoPresenze.get(i).getData().contains("/7")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 12) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/08") ||
                        elencoPresenze.get(i).getData().contains("/8")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        adapter = new ListatoreDettaglioPresenze(this, elencoPresenze);
        list1.setAdapter(adapter);

        mostranumero(elencoPresenze);

    }

    void mostranumero(ArrayList<Presenza> presenzes) {

        int num = presenzes.size();

        //converte il size dell'array in stringa
        String numero = String.valueOf(num);

        //testo della stringa a schermo
        String testo = " " + numero;

        //trova la stringa sul layout
        TextView asd = findViewById(R.id.numeroPresenze);
        //setta la stringa sul layout
        asd.setText(testo);

    }

}
