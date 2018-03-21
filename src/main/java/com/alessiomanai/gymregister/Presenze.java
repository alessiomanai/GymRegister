package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.classi.Presenza;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPresenze;
import com.alessiomanai.gymregister.utils.ListatoreIscritti;
import com.alessiomanai.gymregister.utils.ListatorePresenze;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Presenze extends Activity {

    static Corso corso;
    private ArrayList<Presenza> listaPresenze = new ArrayList<>();
    ArrayList<Iscritto> elencoIscritti = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenze);

        mostraData();

        int j = 0;
        int i = 0;
        ArrayList<Presenza> presenzeOdierne = new ArrayList<>();

        try {

            QueryIscritto database = new QueryIscritto(this);
            elencoIscritti = database.caricaIscritti(database, corso);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        QueryPresenze db = new QueryPresenze(this);
        presenzeOdierne = db.presenzeOdierne(db, corso);


        for (i = 0; i < elencoIscritti.size(); i++) {

            listaPresenze.add(new Presenza(elencoIscritti.get(i), corso));

            if (presenzeOdierne.size() != 0) {

                for (j = 0; j < presenzeOdierne.size(); j++) {

                    if (listaPresenze.get(i).getIdIscritto() == presenzeOdierne.get(j).getIdIscritto()) {

                        listaPresenze.get(i).setData(presenzeOdierne.get(j).getData());

                    }
                }

            }

        }

        ListView list1 = this.findViewById(R.id.listViewPresenze);
        ListatorePresenze adapter = new ListatorePresenze(this, listaPresenze);
        list1.setAdapter(adapter);

        SharedPreferences preferences = getSharedPreferences("RegistroPresenze", Activity.MODE_PRIVATE);    //cerca la sharedPreferecens boot all'avvio del codice

        int appID = preferences.getInt("Presenze", -1);

        if (appID != 1) {

            mostraGuida();
        }

    }

    /**
     * mostra il numero degli iscritti sullo schermo
     */
    public void mostraData() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", new Locale("Rome"));
        String data = sdf.format(new Date());

        //trova la stringa sul layout
        TextView asd = findViewById(R.id.dataPresenze);
        //setta la stringa sul layout
        asd.setText(data);

    }

    public void mostraGuida() {

        //finestra di conferma
        AlertDialog.Builder builder = new AlertDialog.Builder(Presenze.this);

        builder.setTitle(R.string.registroPresenze);
        builder.setMessage(R.string.guidaPres);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Confermato!

                SharedPreferences pref = getSharedPreferences("RegistroPresenze", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit(); //edita il valore di Boot
                editor.putInt("Presenze", 1);    //associa all' hash di Application il valore MP3PLAYER
                editor.apply();    //convalida le modifiche

                dialog.dismiss();

            }
        });
        builder.show();

    }
}
