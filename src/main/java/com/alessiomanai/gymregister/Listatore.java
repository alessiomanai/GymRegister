package com.alessiomanai.gymregister;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Listatore extends ArrayAdapter<String>{

	static Activity context;
	static ArrayList<String> nome;

	@SuppressWarnings("static-access")
	public Listatore(Activity context, ArrayList<String> nome) {
		
		super(context, R.layout.listatore, nome);	//associo la classe al layout
		
		this.context = context;	//non ne ho idea, ma si
		this.nome = nome;
	}
	
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		LayoutInflater inflater = context.getLayoutInflater();	
		
		View rowView = inflater.inflate(R.layout.listatore, null, true);	//infila il file xml nell' intent
		
		TextView txtTitle = (TextView) rowView.findViewById(R.id.nomeT);

		txtTitle.setText(nome.get(position));
		
		return rowView;
		
	}
}
