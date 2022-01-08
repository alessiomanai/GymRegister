package com.alessiomanai.gymregister.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.PresenzeUtente;
import com.alessiomanai.gymregister.R;
import com.alessiomanai.gymregister.classi.Presenza;
import com.alessiomanai.gymregister.database.QueryPresenze;

import java.util.ArrayList;


public class ListatoreDettaglioPresenze extends ArrayAdapter<Presenza> {

    private final Activity context;
    private final ArrayList<Presenza> presenze;

    public ListatoreDettaglioPresenze(Activity context, ArrayList<Presenza> nome) {

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

        final View rowView = inflater.inflate(R.layout.listatorepresenzeutente, null, true);    //infila il file xml nell' intent

        TextView txtTitle = rowView.findViewById(R.id.nomeTP);
        ImageButton bottone = rowView.findViewById(R.id.eliminaPresenza);

        txtTitle.setText(presenze.get(position).getData());

        bottone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finestra di conferma
                final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle(R.string.clearAttenda);
                builder.setMessage(getContext().getString(R.string.haiSelezionato) + ": " +
                        presenze.get(position).getData() + "\n" + getContext().getString(R.string.clearAttMes));
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Confermato!


                        QueryPresenze database = new QueryPresenze(builder.getContext());

                        database.eliminaPresenzaVecchia(database, presenze.get(position).getIscritto(),
                                presenze.get(position).getData());

                        presenze.remove(position);
                        notifyDataSetChanged();

                        Toast.makeText(builder.getContext(), R.string.presRemove, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();

                    }
                });
                //negativa
                builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Annullato!
                        Toast.makeText(builder.getContext(), R.string.opannul, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });
                //visualizza la finestra
                builder.show();

            }
        });


        return rowView;

    }


}
