package com.alessiomanai.gymregister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.alessiomanai.gymregister.classi.Presenza;

import java.util.ArrayList;

public class CercaPresenze extends Activity {

    static public ArrayList<Presenza> elencoPresenze;
    ArrayList<Presenza> risultati = new ArrayList<>();
    EditText search;
    ImageButton cerca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_presenze);

        search = (EditText) findViewById(R.id.search2);
        cerca = (ImageButton) findViewById(R.id.buttoncerca2);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                String chiave;

                chiave = search.getText().toString();

            }
        });

    }
}
