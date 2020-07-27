package com.alessiomanai.gymregister;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryCorso;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPagamento;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * questa classe gestisce i metodi per il passaggio da file a database
 * contiene tutte le funzioni utilizzate in precedenza per gestire i dati utente delle activity
 **/

public class Trasferimento extends Activity {

    private ArrayList<String> palestre = new ArrayList<>();
    private Corso palestra;
    ArrayList<Corso> elencoCorsi = new ArrayList<>();
    private ArrayList<Iscritto> iscritti = new ArrayList<>();
    static String elencopalestre = "nomipalestre.txt";    //file contenente i nomi delle palestre
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasferimento);

        Button button = (Button) findViewById(R.id.bottoneConfermaT);


        int permissionCheck = ContextCompat.checkSelfPermission(Trasferimento.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Trasferimento.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Trasferimento.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Trasferimento.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    caricaPalestre();
                    trasferisciPalestre();
                    ricaricaPalestre();
                    System.out.println("Palestre caricate");

                    try {

                        for (int i = 0; i < elencoCorsi.size(); i++) {

                            ArrayList<Iscritto> tempo = caricadafileIscritti(elencoCorsi.get(i).getNome());

                            for (int m = 0; m < tempo.size(); m++) {

                                for (int j = 0; j < elencoCorsi.size(); j++) {

                                    if (tempo.get(m).getCorso().equals(elencoCorsi.get(j).getNome())) {

                                        tempo.get(m).setPalestra(elencoCorsi.get(j));
                                    }

                                }

                            }

                            trasferisciIscritti(tempo);

                        }


                        System.out.println("Iscritti caricati");

                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Eccezione iscritti");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Eccezione palestre");
                }


                SharedPreferences pref = getSharedPreferences("Boot", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit(); //edita il valore di Boot
                editor.putInt("Application", 1);    //associa all' hash di Application il valore MP3PLAYER
                editor.apply();    //convalida le modifiche

                Intent a = new Intent(getBaseContext(), TrasferimentiNote.class);
                //avvia la finestra corrispondente
                startActivity(a);
                finish();    //termina la activity

            }
        });

    }


    /**********************************Gestionepalestre.java*********************************************/

    /***
     * Prende in ingresso i nomi dei file da caricare
     */

    void palestra(String nomepal) {

        //carica i nomi dai file
        try {
            caricadafileIscritti(nomepal);
        } catch (Exception e) {
            //Toast.makeText(this, "Nessun file letto", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }


    /***
     * carica l'elenco dal file selezionato - finalmente funziona
     */
    public ArrayList<Iscritto> caricadafileIscritti(String nomepal) throws IOException {

        ArrayList<Iscritto> iscritti = new ArrayList<>();

        String s;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;

        //carica i nomi
        FileInputStream in;
        FileInputStream asd = null;
        FileInputStream w = null;
        int i;

        try {
            in = openFileInput(nomepal + "id.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);

            while ((s = bufferedReader.readLine()) != null) {
                iscritti.add(new Iscritto(s + "\n"));    //aggiunge un iscritto

            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            //carica gli indirizzi
            asd = openFileInput(nomepal + "address.txt");
            inputStreamReader = new InputStreamReader(asd);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setIndirizzo(s + "\n");
                i++;
            }
            asd.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione address");
        }

        try {
            //carica i numeri di telefono
            w = openFileInput(nomepal + "tel.txt");
            inputStreamReader = new InputStreamReader(w);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setTelefono(s + "\n");
                i++;
            }
            w.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione tel");
        }

        try {
            //carica le date di nascita
            w = openFileInput(nomepal + "nascita.txt");
            inputStreamReader = new InputStreamReader(w);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setDataDiNascita(s + "\n");
                i++;
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {

            //carica la città
            w = openFileInput(nomepal + "citta.txt");
            inputStreamReader = new InputStreamReader(w);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setCitta(s + "\n");
                i++;
            }

            w.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            //carica i dati sull'scrizione
            w = openFileInput(nomepal + "iscrizione.txt");
            inputStreamReader = new InputStreamReader(w);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setIscrizione(s + "\n");
                i++;
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            //carica mensilità
            in = openFileInput(nomepal + "settembre.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setSettembre(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "ottobre.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setOttobre(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "novembre.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setNovembre(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "dicembre.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setDicembre(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "gennaio.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setGennaio(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "febbraio.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setFebbraio(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "marzo.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setMarzo(s + "\n");
                i++;
            }
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {

            in = openFileInput(nomepal + "aprile.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setAprile(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "maggio.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setMaggio(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }


        try {
            in = openFileInput(nomepal + "giugno.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setGiugno(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        try {
            in = openFileInput(nomepal + "luglio.txt");
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            i = 0;
            while ((s = bufferedReader.readLine()) != null) {
                iscritti.get(i).setLuglio(s + "\n");
                i++;
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Eccezione id");
        }

        for (int j = 0; j < iscritti.size(); j++) {

            iscritti.get(j).setCorso(nomepal);
        }

        return iscritti;

    }    //fine caricadafile


    /***
     * avvia l'intent degli iscritti
     */
    void avvia() {

        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);

        //avvia la finestra corrispondente
        startActivity(gestioneiscritti);
    }

    /***
     * pulsante del menù
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.infos:
                Intent info = new Intent(getBaseContext(), InfoApp.class);
                //avvia la finestra corrispondente
                startActivity(info);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void addpalEx() {

        Toast.makeText(this, "Non puoi creare più di cinque corsi", Toast.LENGTH_SHORT).show();

    }


    /***
     * gestione dei bottoni
     */
    void bottoni() {

        ImageButton aggiungipalestra = (ImageButton) findViewById(R.id.paladd);
        ImageButton eliminapalestra = (ImageButton) findViewById(R.id.palelim);

        aggiungipalestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent aggiungi = new Intent(getBaseContext(), AggiungiPalestra.class);

                //avvia la finestra corrispondente
                startActivity(aggiungi);

                palestre.clear();

                finish();

            }
        });  //fine bottone


        eliminapalestra.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                Intent elimina = new Intent(getBaseContext(), EliminaPalestre.class);

                //avvia la finestra corrispondente
                startActivity(elimina);

                palestre.clear();

                finish();
            }
        });

    }

    /***
     * carica i nomi delle palestre
     *
     * @throws IOException
     */
    void caricaPalestre() throws IOException {

        String s;

        //carica i nomi
        FileInputStream in = openFileInput(elencopalestre);

        InputStreamReader inputStreamReader = new InputStreamReader(in);

        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        while ((s = bufferedReader.readLine()) != null) {
            palestre.add(s + "\n");

        }

        in.close();

    }


    /************************************GestioneIscritti.java*********************************************************/


    /**
     * salva i dati di tutti gli utenti sui file
     */

    public void salvaFileIscritti() {

        String[] filemesi = new String[12];

        //scrive file
        try {

            /***dati utenti*/
            OutputStreamWriter fileid = new OutputStreamWriter(openFileOutput(palestra.getFileUsers(), Context.MODE_PRIVATE));
            OutputStreamWriter fileaddress = new OutputStreamWriter(openFileOutput(palestra.getFileIndirizzi(), Context.MODE_PRIVATE));
            OutputStreamWriter filetel = new OutputStreamWriter(openFileOutput(palestra.getFileNumeriTelefono(), Context.MODE_PRIVATE));
            OutputStreamWriter filedatn = new OutputStreamWriter(openFileOutput(palestra.getFiledatanascita(), Context.MODE_PRIVATE));
            OutputStreamWriter filecit = new OutputStreamWriter(openFileOutput(palestra.getFilecitta(), Context.MODE_PRIVATE));

            /***info pagamenti*/
            OutputStreamWriter filepag = new OutputStreamWriter(openFileOutput(palestra.getFileiscrizione(), Context.MODE_PRIVATE));

            //creazione e dichiarazione dei file contenenti le mensilità pagate
            OutputStreamWriter file0 = new OutputStreamWriter(openFileOutput(palestra.getFileSettembre(), Context.MODE_PRIVATE));
            OutputStreamWriter file1 = new OutputStreamWriter(openFileOutput(palestra.getFileOttobre(), Context.MODE_PRIVATE));
            OutputStreamWriter file2 = new OutputStreamWriter(openFileOutput(palestra.getFileNovembre(), Context.MODE_PRIVATE));
            OutputStreamWriter file3 = new OutputStreamWriter(openFileOutput(palestra.getFileDicembre(), Context.MODE_PRIVATE));
            OutputStreamWriter file4 = new OutputStreamWriter(openFileOutput(palestra.getFileGennaio(), Context.MODE_PRIVATE));
            OutputStreamWriter file5 = new OutputStreamWriter(openFileOutput(palestra.getFileFebbraio(), Context.MODE_PRIVATE));
            OutputStreamWriter file6 = new OutputStreamWriter(openFileOutput(palestra.getFileMarzo(), Context.MODE_PRIVATE));
            OutputStreamWriter file7 = new OutputStreamWriter(openFileOutput(palestra.getFileAprile(), Context.MODE_PRIVATE));
            OutputStreamWriter file8 = new OutputStreamWriter(openFileOutput(palestra.getFileMaggio(), Context.MODE_PRIVATE));
            OutputStreamWriter file9 = new OutputStreamWriter(openFileOutput(palestra.getFileGiugno(), Context.MODE_PRIVATE));
            OutputStreamWriter file10 = new OutputStreamWriter(openFileOutput(palestra.getFileLuglio(), Context.MODE_PRIVATE));


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
    }


    /***
     * scrive sul file palestra
     */
    void scritturafile(String s) throws IOException {

        OutputStreamWriter file = new OutputStreamWriter(openFileOutput(elencopalestre, Context.MODE_APPEND));

        file.write(s);

        file.close();

    }

    void trasferisciPalestre() {

        QueryCorso database = new QueryCorso(this);

        for (int i = 0; i < palestre.size(); i++) {

            elencoCorsi.add(new Corso(palestre.get(i)));

            database.nuovo(database, elencoCorsi.get(i));

        }

    }

    void trasferisciIscritti(ArrayList<Iscritto> iscritti) {

        QueryIscritto database = new QueryIscritto(this);
        QueryPagamento pagamento = new QueryPagamento(this);

        for (int i = 0; i < iscritti.size(); i++) {
            database.nuovo(database, iscritti.get(i), iscritti.get(i).getPalestra());

            iscritti.get(i).setIdDatabase(database.selectLastIDIscritto(database));

            pagamento.inizializza(pagamento, iscritti.get(i), iscritti.get(i).getPalestra());
            pagamento.update(pagamento, iscritti.get(i));

        }
    }

    void ricaricaPalestre() {

        QueryCorso database = new QueryCorso(this);
        elencoCorsi = database.getElencoCorsi(database);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
