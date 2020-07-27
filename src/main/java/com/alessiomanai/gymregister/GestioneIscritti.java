package com.alessiomanai.gymregister;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryCertificati;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPagamento;
import com.alessiomanai.gymregister.database.Tabelle;
import com.alessiomanai.gymregister.utils.BackupManager;
import com.alessiomanai.gymregister.utils.DocumentCreator;
import com.alessiomanai.gymregister.utils.ListatoreIscritti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;



public class GestioneIscritti extends Activity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    public static ArrayList<Iscritto> iscritti = new ArrayList<>();
    public static Corso palestra;
    String text = "";
    private View donotpay;

    /***
     * avvio del codice
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_iscritti);

        donotpay = findViewById(R.id.notpayView);
        donotpay.setVisibility(View.GONE);

        try {
            iscritti = caricaDatabase();

            iscritti = caricaCertificati(iscritti);
        } catch (NullPointerException np) {
            Toast.makeText(this,
                    "Something went wrong. Please delete gym", Toast.LENGTH_LONG).show();
        }


        /**ordina gli iscritti prima di mostrarli a schermo**/
        Collections.sort(iscritti, new Comparator<Iscritto>() {
            public int compare(Iscritto s1, Iscritto s2) {
                return s1.getId().compareTo(s2.getId());
            }
        });

        //mostra il numero di iscritti
        mostranumero();

        //carica l'elenco
        caricaelenco();

        //gestione bottoni
        gestionebottoni();

        try {
            pagamentiArretrati();   //funzionalità non presente in database corrotti
        } catch (NullPointerException ne) {
            ne.printStackTrace();
        }

    }    //fine onCreate


    /**
     * restore id over activity in pause for long time
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        palestra.setId(savedInstanceState.getInt("idCorso"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("idCorso", palestra.getId());
    }


    /**
     * carica la struttura listview
     */
    void caricaelenco() {

        ListView list1 = this.findViewById(R.id.listView);
        ListatoreIscritti adapter = new ListatoreIscritti(this, iscritti);
        list1.setAdapter(adapter);

        list1.deferNotifyDataSetChanged();

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long idonet) {    //a seconda della posizione (rilevata automaticamente) apro un profilo

                //invia i dati alla activity

                Dettagli.posizione = position;    //passa i dati relativi alla posizione
                Dettagli.iscritto = iscritti.get(position);
                Dettagli.palestra = palestra;    //dettagli palestra

                //avvia i dettagli utente
                Intent dettagli = new Intent(getBaseContext(), Dettagli.class);
                startActivity(dettagli);

                finish();
            }
        });        //fine lista clickabile

    }

    public ArrayList<Iscritto> caricaDatabase() {

        QueryIscritto database = new QueryIscritto(this);

        return database.caricaIscritti(database, palestra);

    }

    public ArrayList<Iscritto> caricaCertificati(ArrayList<Iscritto> iscritti) {

        QueryCertificati database = new QueryCertificati(this);

        for (int i = 0; i < iscritti.size(); i++) {
            iscritti.get(i).setCertificatoMedico(database.getCertificatoMedico(database, iscritti.get(i)));
        }

        return iscritti;
    }

    /**
     * carica i bottoni di gestione
     **/
    void gestionebottoni() {

        ImageButton aggiungi = findViewById(R.id.bottonadd);
        aggiungi.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View arg0) {

                Aggiungi.palestra = palestra;

                Intent crea = new Intent(getBaseContext(), Aggiungi.class);
                //avvia la finestra corrispondente
                startActivity(crea);

                finish();
            }
        });


        ImageButton note = findViewById(R.id.bottonord);
        note.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                Note.corso = palestra;
                Note.noteIscritto = false;

                Intent note = new Intent(getBaseContext(), Note.class);

                //avvia la finestra corrispondente
                startActivity(note);

            }
        });

        ImageButton cerca = findViewById(R.id.bottoncerca);
        cerca.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                ricerca();    //avvia la finestra di ricerca

            }
        });

        ImageButton export = findViewById(R.id.bottonexpodt);
        export.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                Presenze.corso = palestra;

                Intent presenzew = new Intent(getBaseContext(),
                        Presenze.class);

                //avvia la finestra corrispondente
                startActivity(presenzew);

            }
        });

    }    //fine gestione bottoni


    void foo2() {    //funzione di debug

        Toast.makeText(this, "Funzione al momento non disponibile", Toast.LENGTH_SHORT).show();
    }


    /**
     * se viene premuto il tasto indietro - funziona
     */
    @Override
    public void onBackPressed() {

        //libera la memoria
        iscritti.clear();    //svuota la lista

        finish();
    }    //fine tasto back


    /**
     * tasto menù
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(R.string.search1).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ricerca();
                return true;
            }
        });

        //esporta l'elenco su un file odt
        menu.add(R.string.men1).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                try {
                    esportaPDF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        menu.add(R.string.backup).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                BackupManager bm = new BackupManager();

                bm.doBackup(getApplicationContext());

                return true;
            }
        });

        menu.add(R.string.ripristina).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                restoreDialog();

                return true;
            }
        });

        return true;
    }

    void pagamentiArretrati() {

        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String mese;

        month += 1;

        if (month > 8) {
            mese = Tabelle.InfoTabelle.pagamento[month - 6];
        } else if (month < 8) {
            mese = Tabelle.InfoTabelle.pagamento[month + 6];    //bug bastardo
        } else {
            //System.out.println("Altro mese");
            mese = "agosto";
            return; //evito che crashi ad agosto
        }

        if (day > 10) {

            String m = Integer.toString(month);

            if (month >= 1 && month <= 9) {  //per visualizzare lo zero iniziale

                m = "0" + m;

            }

            QueryPagamento database = new QueryPagamento(this);
            ArrayList<String> lista = database.utentiNotPay(database, mese, m, palestra);

            if (lista.size() > 0) {

                for (int i = 0; i < lista.size(); i++) {
                    text += lista.get(i) + "\n";
                }

                donotpay.setVisibility(View.VISIBLE);

                donotpay.setOnClickListener(new View.OnClickListener() {
                    @Override

                    public void onClick(View arg0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(GestioneIscritti.this);

                        builder.setTitle(R.string.nopay);
                        builder.setMessage(text); //visualizza a schermo chi non ha ancora pagato

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
                });

            }

        }

    }

    void restoreDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(GestioneIscritti.this);

        builder.setTitle(R.string.conferma);
        builder.setMessage(R.string.ripristino);

        builder.setCancelable(false);
        builder.setPositiveButton(R.string.conferma, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Confermato!

                BackupManager bm = new BackupManager();

                bm.doRestore(getApplicationContext());

            }
        });
        //negativa
        builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Annullato!
                Toast.makeText(GestioneIscritti.this, R.string.opannul, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        //visualizza la finestra
        builder.show();

    }

    /**
     * toast di conferma eliminazione
     */
    void eliminato() {

        Toast.makeText(this, R.string.datidel, Toast.LENGTH_SHORT).show();

    }

    /***
     * esporta l'elenco su file .pdf
     */
    void esportaPDF() throws IOException {

        String filez = palestra.getNome() + ".pdf";    //nome del file
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

            dir = new File(sdCard.getAbsolutePath() + "/gymregister/");
            dir.mkdirs();
            file = new File(dir, filez);

            percorso = dir.toString();    //trasforma il nome del file in una stringa

        } else {

            //scrivi su sd se ne hai il permesso

            boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

            if (!hasPermission) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);

                Toast.makeText(this, "The app was not allowed to write to your storage. " +
                        "Hence, it cannot function properly. " +
                        "Please consider granting it this permission", Toast.LENGTH_LONG).show();

            }

            sdCard = Environment.getExternalStorageDirectory();    //ottiene il percorso della memoria esteran
            dir = new File(sdCard.getAbsolutePath() + "/gymregister/");    //seleziona la cartella
            dir.mkdirs();    //crea la cartella
            file = new File(dir, filez);

            percorso = dir.toString();    //trasforma il nome del file in una stringa

        }

        DocumentCreator document = new DocumentCreator(getApplicationContext(), file);

        document.setTitle(getString(R.string.nomepalestra) + ": " + palestra.getNome());

        //note relative alla palestra
        String file1 = "note" + palestra.getNome() + palestra.getNome() + ".txt";
        String nita;
        String totale = "";    //stringa di lettura
        File letturaNote = new File(file1);

        if (letturaNote.isFile() && letturaNote.canRead()) {    //se il file non esiste non lo scrive e continua la scrittura degli altri elementi

            FileInputStream IS = new FileInputStream(letturaNote);
            InputStreamReader inputStreamReader = new InputStreamReader(IS);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            while ((nita = bufferedReader.readLine()) != null) {
                totale += (nita + "\n");
            }

            IS.close();

        }

        document.newLine(totale);    //scrive il testo letto

        for (int i = 0; i < iscritti.size(); i++) {

            document.newLine(getString(R.string.id) + ": " + iscritti.get(i).getId());
            document.newLine(getString(R.string.data) + ": " + iscritti.get(i).getDataDiNascita());
            document.newLine(getString(R.string.address) + ": " + iscritti.get(i).getIndirizzo());
            document.newLine(getString(R.string.citta) + ": " + iscritti.get(i).getCitta());
            document.newLine(getString(R.string.phone) + ": " + iscritti.get(i).getTelefono());
            document.setH4Chapter(getString(R.string.pay));
            document.addLine(getString(R.string.iscrizione) + ": " + iscritti.get(i).getIscrizione());
            document.addLine(getString(R.string.sept) + ": " + iscritti.get(i).getSettembre());
            document.addLine(getString(R.string.oct) + ": " + iscritti.get(i).getOttobre());
            document.addLine(getString(R.string.nov) + ": " + iscritti.get(i).getNovembre());
            document.addLine(getString(R.string.dec) + ": " + iscritti.get(i).getDicembre());
            document.addLine(getString(R.string.jan) + ": " + iscritti.get(i).getGennaio());
            document.addLine(getString(R.string.feb) + ": " + iscritti.get(i).getFebbraio());
            document.addLine(getString(R.string.mar) + ": " + iscritti.get(i).getMarzo());
            document.addLine(getString(R.string.apr) + ": " + iscritti.get(i).getAprile());
            document.addLine(getString(R.string.mag) + ": " + iscritti.get(i).getMaggio());
            document.addLine(getString(R.string.jun) + ": " + iscritti.get(i).getGiugno());
            document.addLine(getString(R.string.jul) + ": " + iscritti.get(i).getLuglio());
            document.newLine("");
            document.newLine("");
            document.newLine("");
        }

        document.lastLine("Grazie per aver usato Gym Register!");

        document.getPDF();

        //mostro il percorso in cui ho salvato il file
        String toasttext = getString(R.string.datiexp) + "\n" + percorso;

        Toast.makeText(this, toasttext, Toast.LENGTH_SHORT).show();

    }

    /**
     * mostra a schermo un messaggio di errore backup
     */
    void messaggioBackupNonPresente() {

        Toast.makeText(this, R.string.restorerr, Toast.LENGTH_SHORT).show();

    }

    /**
     * serve a ricaricare lo schermo per aggiornare i dati
     */
    void ricarica() {

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);

        finish();
    }

    /**
     * mostra il numero degli iscritti sullo schermo
     */
    void mostranumero() {

        int num = iscritti.size();

        //converte il size dell'array in stringa
        String numero = String.valueOf(num);

        //testo della stringa a schermo
        String testo = " " + numero;

        //trova la stringa sul layout
        TextView asd = (TextView) findViewById(R.id.numisc);
        //setta la stringa sul layout
        asd.setText(testo);

    }

    /***
     * funzione che visualizza la finestra di ricerca
     */
    void ricerca() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.search1);    //titolo della finestra
        alert.setMessage(R.string.search2);    //testo della finestra

        // Set an EditText view to get user input
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton(R.string.search1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

                Editable value = input.getText();    //al click, prende in ingresso la stringa fornita

                ArrayList<Iscritto> risultati = new ArrayList<Iscritto>();

                String chiave;

                chiave = value.toString();

                QueryIscritto database = new QueryIscritto(alert.getContext());
                risultati = database.cercaIscritto(database, palestra, chiave);

                if (risultati.size() != 0) {

                    Risultati.risultati = risultati;

                    Intent risultatiIntent = new Intent(getBaseContext(), Risultati.class);
                    //avvia la finestra corrispondente
                    startActivity(risultatiIntent);

                    finish();
                } else
                    Toast.makeText(GestioneIscritti.this, R.string.notfund, Toast.LENGTH_SHORT).show();

            }
        });

        alert.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ricarica();
                } else {
                    Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

}
