package com.alessiomanai.gymregister;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.classi.Presenza;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPresenze;
import com.alessiomanai.gymregister.utils.AppPermissionsUtils;
import com.alessiomanai.gymregister.utils.DocumentCreator;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.ListatorePresenze;
import com.alessiomanai.gymregister.utils.GymRegisterConstants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Presenze extends Activity {

    private Corso corso;
    EditText ricerca;
    ImageButton bottone;
    boolean risultatiDefault = true;
    private String toExportMemory;
    private final int WRITE_REQUEST_CODE = 100;
    private final int REQUEST_WRITE_STORAGE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenze);

        corso = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);

        mostraData();

        ricerca = findViewById(R.id.searchPresenze);
        bottone = findViewById(R.id.buttoncercaPresenze);

        bottone.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                cercaUtente(ricerca.getText().toString());

            }
        });

        caricaPresenze();

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

    public void caricaPresenze() {

        int j = 0;
        int i = 0;
        ArrayList<Presenza> presenzeOdierne = new ArrayList<>();
        ArrayList<Iscritto> elencoIscritti = new ArrayList<>();

        try {

            QueryIscritto database = (QueryIscritto) QueryIscritto.getInstance(this);
            elencoIscritti = database.caricaIscritti(corso);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        ArrayList<Presenza> listaPresenze = new ArrayList<>();

        QueryPresenze db = (QueryPresenze) QueryPresenze.getInstance(this);
        presenzeOdierne = db.presenzeOdierne(corso);


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
                editor.putInt("Presenze", 1);    //associa all' hash di Application il valore
                editor.apply();    //convalida le modifiche

                dialog.dismiss();

            }
        });
        builder.show();

    }

    public void cercaUtente(String chiave) {

        ArrayList<Iscritto> risultati = new ArrayList<Iscritto>();

        QueryIscritto database = QueryIscritto.getInstance(getApplicationContext());
        risultati = database.cercaIscritto(corso, chiave);

        ArrayList<Presenza> presenzeOdierne = new ArrayList<>();

        QueryPresenze db = QueryPresenze.getInstance(this);
        presenzeOdierne = db.presenzeOdierne(corso);

        ArrayList<Presenza> listaPresenze = new ArrayList<>();

        listaPresenze.clear();

        int j = 0;
        int i = 0;
        for (i = 0; i < risultati.size(); i++) {

            listaPresenze.add(new Presenza(risultati.get(i), corso));

            if (presenzeOdierne.size() != 0) {

                for (j = 0; j < presenzeOdierne.size(); j++) {

                    if (listaPresenze.get(i).getIdIscritto() == presenzeOdierne.get(j).getIdIscritto()) {

                        listaPresenze.get(i).setData(presenzeOdierne.get(j).getData());

                    }
                }

            }

        }

        risultatiDefault = false;

        ListView list1 = this.findViewById(R.id.listViewPresenze);
        ListatorePresenze adapter = new ListatorePresenze(Presenze.this, listaPresenze);
        list1.setAdapter(adapter);

    }


    @Override
    public void onBackPressed() {

        if (!risultatiDefault) {
            caricaPresenze();
            risultatiDefault = true;
        } else {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(R.string.men1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                try {
                    esportaPresenzePDF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });


        return true;
    }


    void esportaPdfQ(String fileName) {

        // when you create document, you need to add Intent.ACTION_CREATE_DOCUMENT
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        // filter to only show openable items.
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // Create a file with the requested Mime type
        intent.setType("txt/plain");
        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /***
     * esporta l'elenco su file .pdf
     */
    void esportaPresenzePDF() throws IOException {

        String filez = getString(R.string.registroPresenze) + " " + corso.getNome() + ".pdf";    //nome del file
        String percorso;    //percorso di salvataggio file
        File sdCard;
        File dir;
        File file;

        /* Checks if external storage is available for read and write */
        String state = Environment.getExternalStorageState();

        /**stratagemma adottato per scrivere nella memoria interna sui nuovi
         * dispositivi */

        if (!Environment.MEDIA_MOUNTED.equals(state)) { //scrivi su internal

            sdCard = Environment.getDataDirectory();

            dir = new File(sdCard.getAbsolutePath() + GymRegisterConstants.APP_DIRECTORY);
            dir.mkdirs();
            file = new File(dir, filez);

            percorso = dir.toString();    //trasforma il nome del file in una stringa

        } else {

            //scrivi su sd se ne hai il permesso
            if (!AppPermissionsUtils.hasPermissions(this)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);

                Toast.makeText(this, R.string.richiestaPermessiFile, Toast.LENGTH_LONG).show();

                return;

            }

            sdCard = Environment.getExternalStorageDirectory();    //ottiene il percorso della memoria esteran
            dir = new File(sdCard.getAbsolutePath() + GymRegisterConstants.APP_DIRECTORY);    //seleziona la cartella
            dir.mkdirs();    //crea la cartella
            file = new File(dir, filez);

            percorso = dir.toString();    //trasforma il nome del file in una stringa

        }

        DocumentCreator document = new DocumentCreator(getApplicationContext(), file);

        document.setTitle(getString(R.string.registroPresenze) + " " + corso.getNome());

        ArrayList<Iscritto> elencoIscritti = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM", new Locale("Rome"));
        String data = "/" + sdf.format(new Date()) + "/";

        try {

            QueryIscritto database = (QueryIscritto) QueryIscritto.getInstance(this);
            elencoIscritti = database.caricaIscritti(corso);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < elencoIscritti.size(); i++) {

            document.setH4Chapter(elencoIscritti.get(i).getId());

            QueryPresenze database = (QueryPresenze) QueryPresenze.getInstance(this);
            ArrayList<Presenza> presenze = database.presenzeCorsoMese(elencoIscritti.get(i), corso, data);

            for (int j = 0; j < presenze.size(); j++) {
                document.addLine(presenze.get(j).getData());
            }
        }

        document.lastLine("Grazie per aver usato Gym Register!");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            document.endDocument();
            this.toExportMemory = document.getDocument();
            esportaPdfQ(getString(R.string.registroPresenze) + " " + corso.getNome() + ".html");
            return;
        }

        document.getPDF();

        //mostro il percorso in cui ho salvato il file
        String toasttext = getString(R.string.datiexp) + "\n" + percorso;

        Toast.makeText(this, toasttext, Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == WRITE_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    if (data != null
                            && data.getData() != null) {
                        writeInFile(data.getData(), toExportMemory);
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
    }

    private void writeInFile(@NonNull Uri uri, @NonNull String text) {
        OutputStream outputStream;
        try {
            outputStream = getContentResolver().openOutputStream(uri);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(outputStream));
            bw.write(text);
            bw.flush();
            bw.close();
            Toast.makeText(this, getString(R.string.fsave), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.ferror), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ricarica();
                } else {
                    Toast.makeText(this, R.string.permessiError, Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    void ricarica() {

        Intent presenze = new Intent(getBaseContext(), Presenze.class);
        startActivity(presenze);
        finish();
    }
}
