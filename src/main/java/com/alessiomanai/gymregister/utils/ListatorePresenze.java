package com.alessiomanai.gymregister.utils;


import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.alessiomanai.gymregister.PresenzeUtente;
import com.alessiomanai.gymregister.R;
import com.alessiomanai.gymregister.classi.Presenza;
import com.alessiomanai.gymregister.database.QueryPresenze;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ListatorePresenze extends ArrayAdapter<Presenza> {

    private Activity context;
    private ArrayList<Presenza> presenze;

    public ListatorePresenze(Activity context, ArrayList<Presenza> nome) {

        super(context, R.layout.activity_presenze_utente, nome);    //associo la classe al layout

        this.context = context;    //non ne ho idea, ma si
        this.presenze = nome;
    }

    /**
     * visualizza il nome nella lista indicata
     */
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
        String data = sdf.format(new Date());
        final int posizioneClick = position;

        final View rowView = inflater.inflate(R.layout.listatorepresenze, null, true);    //infila il file xml nell' intent

        TextView txtTitle = rowView.findViewById(R.id.nomeTP);
        final View presenza = rowView.findViewById(R.id.presenzaCheck);
        Button bottone = rowView.findViewById(R.id.bottonePresenze);

        bottone.setText(R.string.controllaPresenze);

        txtTitle.setText(presenze.get(position).getNomeIscritto());

        if (presenze.get(posizioneClick).getData().equals(data)) {

            presenza.setBackgroundColor(Color.GREEN);

        } else {

            presenza.setBackgroundColor(Color.RED);
        }


        presenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
                String data = sdf.format(new Date());

                if (presenze.get(posizioneClick).getData().equals(data)) {

                    QueryPresenze database = new QueryPresenze(v.getContext());

                    database.eliminaPresenzaOdierna(database, presenze.get(posizioneClick).getIscritto());

                    presenze.get(posizioneClick).setData("0");

                    presenza.setBackgroundColor(Color.RED);

                    Toast.makeText(v.getContext(), R.string.presRemove, Toast.LENGTH_SHORT).show();

                    Log.v("Presenza", "tolta");

                } else {

                    QueryPresenze database = new QueryPresenze(v.getContext());

                    try {
                        database.aggiungi(database, presenze.get(posizioneClick).getIscritto(),
                                presenze.get(posizioneClick).getCorso());

                    } catch (SQLiteConstraintException exception) {
                        exception.printStackTrace();
                    }

                    presenze.get(posizioneClick).setData(data);

                    presenza.setBackgroundColor(Color.GREEN);

                    Log.v("Presenza", "messa");

                    Toast.makeText(v.getContext(), R.string.presAdd, Toast.LENGTH_SHORT).show();

                }

            }
        });


        bottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //metodo per far partire activity da listview
                Intent intent = new Intent(v.getContext(), PresenzeUtente.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(ExtrasConstants.ISCRITTO, presenze.get(position).getIscritto());
                v.getContext().startActivity(intent);

            }
        });
        return rowView;
    }
}
