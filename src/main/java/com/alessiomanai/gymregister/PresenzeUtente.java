package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.classi.Presenza;
import com.alessiomanai.gymregister.database.QueryPresenze;
import com.alessiomanai.gymregister.utils.DatePickerFragment;
import com.alessiomanai.gymregister.utils.ListatoreDettaglioPresenze;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class PresenzeUtente extends Activity implements AdapterView.OnItemSelectedListener {

    static public Iscritto iscritto;
    static public ArrayList<Presenza> elencoPresenze = new ArrayList<>();
    static public ListView list1;
    static public ListatoreDettaglioPresenze adapter;
    ArrayList<Presenza> presenzeSelezionate = new ArrayList<>();
    DatePickerFragment datePickerFragment;

    ImageButton ordinaAsc, ordinaDesc, aggiungiPresenza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenze_utente);

        datePickerFragment = new DatePickerFragment();

        ordinaAsc = (ImageButton) findViewById(R.id.asc);
        ordinaDesc = (ImageButton) findViewById(R.id.desc);
        aggiungiPresenza = (ImageButton) findViewById(R.id.aggiungiPresenzaIcon);


        TextView testo = (TextView) findViewById(R.id.userPres);
        testo.setText(iscritto.getId());

        Spinner spinner = (Spinner) findViewById(R.id.meseSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.month_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapterSpinner);

        QueryPresenze database = new QueryPresenze(this);
        elencoPresenze = database.presenzeIscritto(database, iscritto);

        Collections.sort(elencoPresenze, new Comparator<Presenza>() {
            public int compare(Presenza s1, Presenza s2) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

        list1 = (ListView) this.findViewById(R.id.listViewPresenzeD);
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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

                datePickerFragment.show(getFragmentManager(), "datePicker");

            }
        });

        // ci registriamo agli eventi del popup (ok e annulla)
        datePickerFragment.setOnDatePickerFragmentChanged(new DatePickerFragment.DatePickerFragmentListener() {
            @Override
            public void onDatePickerFragmentOkButton(DialogFragment dialog, Calendar date) {
                // trasferiamo il valore sul campo di testo
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));

                String data = format.format(date.getTime());

                System.out.println(data);

                try {

                    QueryPresenze database = new QueryPresenze(getApplicationContext());

                    database.aggiungiPresenzaPrecedente(database, iscritto, GestioneIscritti.palestra, data);

                    Presenza nuovaPresenza = new Presenza();
                    nuovaPresenza.setData(data);

                    elencoPresenze.add(nuovaPresenza);

                    list1.setAdapter(adapter);

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

            @Override
            public void onDatePickerFragmentCancelButton(DialogFragment dialog) {
                // non facciamo nulla
            }
        });

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

                if (elencoPresenze.get(i).getData().contains("/09")) {
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

                if (elencoPresenze.get(i).getData().contains("/01")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 6) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/02")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 7) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/03")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 8) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/04")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 9) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/05")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 10) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/06")) {
                    presenzeSelezionate.add(elencoPresenze.get(i));
                }
            }

            adapter = new ListatoreDettaglioPresenze(this, presenzeSelezionate);
            list1.setAdapter(adapter);
            mostranumero(presenzeSelezionate);

        }

        if (pos == 11) {

            for (int i = 0; i < elencoPresenze.size(); i++) {

                if (elencoPresenze.get(i).getData().contains("/07")) {
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
        TextView asd = (TextView) findViewById(R.id.numeroPresenze);
        //setta la stringa sul layout
        asd.setText(testo);

    }

}
