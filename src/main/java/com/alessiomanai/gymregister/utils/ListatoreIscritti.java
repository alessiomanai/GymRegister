package com.alessiomanai.gymregister.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alessiomanai.gymregister.R;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

        View rowView = inflater.inflate(R.layout.listatoreiscritti, null, true);	//infila il file xml nell' intent

        TextView txtTitle = rowView.findViewById(R.id.nomeIscrittoPreview);

        try {
            txtTitle.setText(iscritti.get(position).toString());

            try {
                loadImageFromStorage(iscritti.get(position).getUrlFoto(), rowView);
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        } catch (RuntimeException re) {
            re.printStackTrace();
            txtTitle.setText("error");
        }

        return rowView;

    }

    private void loadImageFromStorage(String path, View rowView) {

        try {

            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView previewImg = rowView.findViewById(R.id.fotoProfiloPreview);
            previewImg.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
