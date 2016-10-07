package com.alessiomanai.gymregister;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alessiomanai.gymregister.classi.Corso;

import java.util.ArrayList;


public class ListatorePalestre extends ArrayAdapter<Corso> {

    private Activity context;
    private ArrayList<Corso> corso;

    public ListatorePalestre(Activity context, ArrayList<Corso> corso) {

        super(context, R.layout.listatore, corso);    //associo la classe al layout

        this.context = context;
        this.corso = corso;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();

        View rowView = inflater.inflate(R.layout.listatore, null, true);    //infila il file xml nell' intent

        TextView txtTitle = (TextView) rowView.findViewById(R.id.nomeT);

        txtTitle.setText(corso.get(position).toString());

        return rowView;

    }

}
