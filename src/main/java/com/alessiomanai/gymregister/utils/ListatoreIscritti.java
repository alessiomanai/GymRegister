package com.alessiomanai.gymregister.utils;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alessiomanai.gymregister.R;
import com.alessiomanai.gymregister.classi.Iscritto;

import java.util.ArrayList;

public class ListatoreIscritti extends ArrayAdapter<Iscritto> {

    private Activity context;
    private ArrayList<Iscritto> iscritti;

    public ListatoreIscritti(Activity context, ArrayList<Iscritto> nome) {

        super(context, R.layout.listatore, nome);	//associo la classe al layout

        this.context = context;	//non ne ho idea, ma si
        this.iscritti = nome;
    }

    /**visualizza il nome nella lista indicata*/
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.listatore, null, true);	//infila il file xml nell' intent

        TextView txtTitle = rowView.findViewById(R.id.nomeT);

        txtTitle.setText(iscritti.get(position).toString());

        return rowView;

    }

}
