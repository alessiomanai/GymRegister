package com.alessiomanai.gymregister;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.utils.ListatoreIscritti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GestioneIscritti extends Activity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    public static ArrayList<Iscritto> iscritti = new ArrayList<>();
    public static Corso palestra;

    /***
     * avvio del codice
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_iscritti);

        iscritti = caricaDatabase();

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


    }    //fine onCreate


    /**
     * carica la struttura listview
     */
    void caricaelenco() {

        ListView list1 = (ListView) this.findViewById(R.id.listView);
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

    /**
     * carica i bottoni di gestione
     **/
    void gestionebottoni() {

        ImageButton aggiungi = (ImageButton) findViewById(R.id.bottonadd);
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


        ImageButton note = (ImageButton) findViewById(R.id.bottonord);
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

        ImageButton cerca = (ImageButton) findViewById(R.id.bottoncerca);
        cerca.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                ricerca();    //avvia la finestra di ricerca

            }
        });

        ImageButton export = (ImageButton) findViewById(R.id.bottonexpodt);
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
                    esportaodt();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        return true;
    }

    /**
     * toast di conferma eliminazione
     */
    void eliminato() {

        Toast.makeText(this, R.string.datidel, Toast.LENGTH_SHORT).show();

    }

    /***
     * esporta l'elenco su file .odt
     */
    void esportaodt() throws IOException {

        String filez = palestra.getNome().substring(0, palestra.getNome().length() - 1) + ".odt";    //nome del file
        String percorso;    //percorso di salvataggio file
        File sdCard;
        File dir;
        File file;

        FileOutputStream f;

        OutputStreamWriter odt;

		/* Checks if external storage is available for read and write */
        String state = Environment.getExternalStorageState();

        /**stratagemma adottato per scrivere nella memoria interna sui nuovi
         * dispositivi */

        if (!Environment.MEDIA_MOUNTED.equals(state)) { //scrivi su internal

            sdCard = Environment.getDataDirectory();

            dir = new File(sdCard.getAbsolutePath() + "/gymregister/");
            dir.mkdirs();
            file = new File(dir, filez);

            f = new FileOutputStream(file);

            odt = new OutputStreamWriter(f);

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

            f = new FileOutputStream(file);

            odt = new OutputStreamWriter(f);

            percorso = dir.toString();    //trasforma il nome del file in una stringa

        }


        odt.write("\t" + getString(R.string.nomepalestra) + " : " + palestra.getNome() + "\n\n");

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

        odt.write(totale);    //scrive il testo letto

        odt.write("\n");    //fine note palestra

        for (int i = 0; i < iscritti.size(); i++) {

            //if (id.size() != 0)
            odt.write(getString(R.string.id) + ": " + iscritti.get(i).getId());
            odt.write(getString(R.string.data) + ": " + iscritti.get(i).getDataDiNascita());
            odt.write(getString(R.string.address) + ": " + iscritti.get(i).getIndirizzo());
            odt.write(getString(R.string.citta) + ": " + iscritti.get(i).getCitta());
            odt.write(getString(R.string.phone) + ": " + iscritti.get(i).getTelefono());
            odt.write("\t" + getString(R.string.pay) + "\n");
            odt.write(getString(R.string.iscrizione) + ": " + iscritti.get(i).getIscrizione());
            odt.write(getString(R.string.sept) + ": " + iscritti.get(i).getSettembre());
            odt.write(getString(R.string.oct) + ": " + iscritti.get(i).getOttobre());
            odt.write(getString(R.string.nov) + ": " + iscritti.get(i).getNovembre());
            odt.write(getString(R.string.dec) + ": " + iscritti.get(i).getDicembre());
            odt.write(getString(R.string.jan) + ": " + iscritti.get(i).getGennaio());
            odt.write(getString(R.string.feb) + ": " + iscritti.get(i).getFebbraio());
            odt.write(getString(R.string.mar) + ": " + iscritti.get(i).getMarzo());
            odt.write(getString(R.string.apr) + ": " + iscritti.get(i).getAprile());
            odt.write(getString(R.string.mag) + ": " + iscritti.get(i).getMaggio());
            odt.write(getString(R.string.jun) + ": " + iscritti.get(i).getGiugno());
            odt.write(getString(R.string.jul) + ": " + iscritti.get(i).getLuglio());
            odt.write("\n");
        }

        odt.write("\n\n\t\t\tGrazie per aver usato Gym Register!");

        odt.close();    //chiudo il file in scrittura

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

    /**
     * salva il backup dei dati
     */
    public void salvaBackup() {

        //scrive file
        try {

            /***dati utenti*/
            OutputStreamWriter fileid = new OutputStreamWriter(openFileOutput("Backup" + palestra.getNome(), Context.MODE_PRIVATE));
            OutputStreamWriter fileaddress = new OutputStreamWriter(openFileOutput("Backup" + palestra.getFileIndirizzi(), Context.MODE_PRIVATE));
            OutputStreamWriter filetel = new OutputStreamWriter(openFileOutput("Backup" + palestra.getFileNumeriTelefono(), Context.MODE_PRIVATE));
            OutputStreamWriter filedatn = new OutputStreamWriter(openFileOutput("Backup" + palestra.getFiledatanascita(), Context.MODE_PRIVATE));
            OutputStreamWriter filecit = new OutputStreamWriter(openFileOutput("Backup" + palestra.getFilecitta(), Context.MODE_PRIVATE));

            /***info pagamenti*/
            OutputStreamWriter filepag = new OutputStreamWriter(openFileOutput("Backup" + palestra.getFileiscrizione(), Context.MODE_PRIVATE));

            //creazione e dichiarazione dei file contenenti le mensilità pagate
            OutputStreamWriter file0 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileSettembre()), Context.MODE_PRIVATE));
            OutputStreamWriter file1 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileOttobre()), Context.MODE_PRIVATE));
            OutputStreamWriter file2 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileNovembre()), Context.MODE_PRIVATE));
            OutputStreamWriter file3 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileDicembre()), Context.MODE_PRIVATE));
            OutputStreamWriter file4 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileGennaio()), Context.MODE_PRIVATE));
            OutputStreamWriter file5 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileFebbraio()), Context.MODE_PRIVATE));
            OutputStreamWriter file6 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileMarzo()), Context.MODE_PRIVATE));
            OutputStreamWriter file7 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileAprile()), Context.MODE_PRIVATE));
            OutputStreamWriter file8 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileMaggio()), Context.MODE_PRIVATE));
            OutputStreamWriter file9 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileGiugno()), Context.MODE_PRIVATE));
            OutputStreamWriter file10 = new OutputStreamWriter(openFileOutput(palestra.getBackupOf(palestra.getFileLuglio()), Context.MODE_PRIVATE));


            //salva i dati nel file
            for (int i = 0; i < iscritti.size(); i++) {

                fileid.write(iscritti.get(i).getId());    //salva gli id

                fileaddress.write(iscritti.get(i).getIndirizzo());   //salva gli indirizzi

                filetel.write(iscritti.get(i).getTelefono());    //salva i numeri di telefono

                filedatn.write(iscritti.get(i).getDataDiNascita());    //salva le date di nascita

                filecit.write(iscritti.get(i).getCitta());    //salva le citta

                /***salvataggio file pagamenti*/

                filepag.write(iscritti.get(i).getIscrizione());    //salva i dati dell'iscrizione

                file0.write(iscritti.get(i).getSettembre());    //salva i dati dell'iscrizione

                file1.write(iscritti.get(i).getOttobre());    //salva i dati dell'iscrizione

                file2.write(iscritti.get(i).getNovembre());    //salva i dati dell'iscrizione

                file3.write(iscritti.get(i).getDicembre());    //salva i dati dell'iscrizione

                file4.write(iscritti.get(i).getGennaio());    //salva i dati dell'iscrizione

                file5.write(iscritti.get(i).getFebbraio());    //salva i dati dell'iscrizione

                file6.write(iscritti.get(i).getMarzo());    //salva i dati dell'iscrizione

                file7.write(iscritti.get(i).getAprile());    //salva i dati dell'iscrizione

                file8.write(iscritti.get(i).getMaggio());    //salva i dati dell'iscrizione

                file9.write(iscritti.get(i).getGiugno());    //salva i dati dell'iscrizione

                file10.write(iscritti.get(i).getLuglio());    //salva i dati dell'iscrizione

            }

            //chiude i file aperti
            fileid.close();
            fileaddress.close();
            filetel.close();
            filedatn.close();
            filecit.close();

            filepag.close();

            //chiusura mesi
            file0.close();
            file1.close();
            file2.close();
            file3.close();
            file4.close();
            file5.close();
            file6.close();
            file7.close();
            file8.close();
            file9.close();
            file10.close();

            //Toast.makeText(this, R.string.fsave, Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            Toast.makeText(this, R.string.ferror, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, R.string.ferror, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Toast.makeText(this, R.string.backupdone, Toast.LENGTH_SHORT).show();

    }


    void caricaBackup() throws IOException {

        String s;

        //carica i nomi
        FileInputStream in = openFileInput(palestra.getBackupOf(palestra.getFileUsers()));
        FileInputStream asd = null;
        FileInputStream w = null;
        int i;

        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.add(new Iscritto(s + "\n"));    //aggiunge un iscritto
        }
        in.close();


        //carica gli indirizzi
        asd = openFileInput(palestra.getBackupOf(palestra.getFileIndirizzi()));
        inputStreamReader = new InputStreamReader(asd);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setIndirizzo(s + "\n");
            i++;
        }
        asd.close();


        //carica i numeri di telefono
        w = openFileInput(palestra.getBackupOf(palestra.getFileNumeriTelefono()));
        inputStreamReader = new InputStreamReader(w);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setTelefono(s + "\n");
            i++;
        }
        w.close();

        //carica le date di nascita
        w = openFileInput(palestra.getBackupOf(palestra.getFiledatanascita()));
        inputStreamReader = new InputStreamReader(w);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setDataDiNascita(s + "\n");
            i++;
        }
        w.close();

        //carica la città
        w = openFileInput(palestra.getBackupOf(palestra.getFilecitta()));
        inputStreamReader = new InputStreamReader(w);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setCitta(s + "\n");
            i++;
        }

        w.close();


        //carica i dati sull'scrizione
        w = openFileInput(palestra.getBackupOf(palestra.getFileiscrizione()));
        inputStreamReader = new InputStreamReader(w);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setIscrizione(s + "\n");
            i++;
        }
        w.close();


        //carica mensilità
        in = openFileInput(palestra.getBackupOf(palestra.getFileSettembre()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setSettembre(s + "\n");
            i++;
        }
        in.close();

        in = openFileInput(palestra.getBackupOf(palestra.getFileOttobre()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setOttobre(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileNovembre()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setNovembre(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileDicembre()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setDicembre(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileGennaio()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setGennaio(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileFebbraio()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setFebbraio(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileMarzo()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setMarzo(s + "\n");
            i++;
        }
        in.close();

        in = openFileInput(palestra.getBackupOf(palestra.getFileAprile()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setAprile(s + "\n");
            i++;
        }
        in.close();

        in = openFileInput(palestra.getBackupOf(palestra.getFileMaggio()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setMaggio(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileGiugno()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setGiugno(s + "\n");
            i++;
        }
        in.close();


        in = openFileInput(palestra.getBackupOf(palestra.getFileLuglio()));
        inputStreamReader = new InputStreamReader(in);
        bufferedReader = new BufferedReader(inputStreamReader);
        i = 0;
        while ((s = bufferedReader.readLine()) != null) {
            GestioneIscritti.iscritti.get(i).setLuglio(s + "\n");
            i++;
        }
        in.close();

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
