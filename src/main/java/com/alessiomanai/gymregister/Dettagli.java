package com.alessiomanai.gymregister;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryIscritto;
import com.alessiomanai.gymregister.database.QueryPagamento;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.FileDialog;
import com.alessiomanai.gymregister.utils.activity.GymRegisterBaseActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class Dettagli extends GymRegisterBaseActivity {

    private static final String FTYPE = ".jpg";
    private static final int DIALOG_LOAD_FILE = 1000;
    private TextView riepilogoPagamentiT;
    private int posizione;    //posizione all' interno dell array utenti
    private Corso palestra;        //nome palestra usata
    private Iscritto iscritto;   //dettagli utente
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;
    ImageButton modifica;
    ImageButton elimina, pagamenti, presenze, note, cambia;
    ImageView fotoProfilo;  //foto profilo utente
    /**
     * per il caricamento della foto
     */
    private String[] mFileList;
    private File mPath = new File(Environment.getExternalStorageDirectory() + "//yourdir//");
    private String mChosenFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dettagli);

        palestra = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);
        posizione = (int) getIntent().getExtras().get(ExtrasConstants.POSITION);
        iscritto = (Iscritto) getIntent().getExtras().get(ExtrasConstants.ISCRITTO);

        //variabili textview
        TextView detid, detaddr, dettel, detnasc, detcit, detcert;

        //collego le textview alla gui
        detid = findViewById(R.id.detid);
        detaddr = findViewById(R.id.detaddr);
        dettel = findViewById(R.id.dettel);
        detnasc = findViewById(R.id.detnasc);
        detcit = findViewById(R.id.detcit);
        detcert = findViewById(R.id.detcert);

        //mostra a schermo i dettagli utente
        try {
            detid.setText(iscritto.getId());
        } catch (NullPointerException e) {
            detid.setText(" ");
        }

        try {
            detaddr.setText(iscritto.getIndirizzo());
        } catch (NullPointerException e) {
            detaddr.setText(" ");
        }

        try {
            dettel.setText(iscritto.getTelefono());
        } catch (NullPointerException e) {
            dettel.setText(" ");
        }

        try {
            detnasc.setText(iscritto.getDataDiNascita());
        } catch (NullPointerException e) {
            detnasc.setText(" ");
        }

        try {
            detcit.setText(iscritto.getCitta());
        } catch (NullPointerException e) {
            detcit.setText(" ");
        }

        try {
            detcert.setText(iscritto.getCertificatoMedico());
        } catch (NullPointerException e) {
            detcit.setText("No data");
        }

        riepilogoPagamentiT = findViewById(R.id.riepilogoPagamenti);

        try {
            riepilogoPagamentiT.setText(caricaRiepilogoPagamenti(iscritto, this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        modifica();     //carica il menù di modifica

        richiestaPermessiLettura();

        final QueryIscritto database = (QueryIscritto) QueryIscritto.getInstance(this);

        fotoProfilo = findViewById(R.id.fotoProfilo);
        elimina = findViewById(R.id.buttoneli1);
        pagamenti = findViewById(R.id.buttonpay);
        presenze = findViewById(R.id.buttonPresenzeUtente);
        note = findViewById(R.id.buttonNote);
        cambia = findViewById(R.id.buttonCambiaPalestra);

        try {   //sicuramente al primo avvio crasha perché non contiene foto
            iscritto.setUrlFoto(database.getUrlPhoto(iscritto));
            loadImageFromStorage(iscritto.getUrlFoto());
        } catch (NullPointerException e) {
            e.printStackTrace();

        }

        fotoProfilo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                try {

                    cambiaFotoProfilo();

                } catch (NullPointerException e) {

                    e.printStackTrace();

                    richiestaPermessiLettura();

                    dialogPermessi();
                }
            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                //invio i dati dell' utente
                Intent noteI = new Intent(getBaseContext(), Note.class);

                noteI.putExtra(ExtrasConstants.ISCRITTO, iscritto);
                noteI.putExtra(ExtrasConstants.NOTE_ISCRITTO, true);

                startActivity(noteI);

            }
        });

        pagamenti.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                getPagamentiIscritto(posizione, iscritto, palestra);
                finish();
            }
        });

        presenze.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                getPresenzeIscritto(iscritto);

            }
        });

        //al click cancella l'utente visualizzato
        elimina.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Dettagli.this);

                builder.setTitle(R.string.conferma);
                builder.setMessage(R.string.elimusr);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Confermato!
                        database.elimina(iscritto);

                        confermaeli();    //conferma l'avvenuto eliminazione con un messaggio

                        getGestioneIscritti(palestra);

                        finish();    //chiude l'activity

                        dialog.dismiss();    //chiude la finestra di dialogo
                    }
                });

                builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        // Annullato!
                        Toast.makeText(Dettagli.this, R.string.opannul, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                });

                builder.show();


            }
        });


        cambia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                getCambiaCorso(iscritto);

            }
        });

    }    //fine oncreate

    /**
     * onBackPressed
     */

    @Override
    public void onBackPressed() {

        getGestioneIscritti(palestra);

        try {
            salvaModifiche();
        } catch (NullPointerException npe) {
            Toast.makeText(this,
                    "Something went wrong. Please delete user", Toast.LENGTH_LONG).show();
        }
        finish();
    }    //fine tasto back

    /**
     * messaggio di conferma eliminazione
     */
    void confermaeli() {
        Toast.makeText(this, R.string.utel, Toast.LENGTH_SHORT).show();
    }

    /***
     * pulsante del menù
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.note: {

                //invio i dati dell' utente
                getNote(iscritto);
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Opzioni di modifica utente e relativa funzione
     */

    void modifica() {

        modifica = findViewById(R.id.buttonmod1);

        modifica.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                getModificaIscritto(posizione, iscritto, palestra);

                finish();
            }
        });

    }

    /**
     * salva i dati di tutti gli utenti
     */

    void salvaModifiche() {

        QueryIscritto fotoUpdate = QueryIscritto.getInstance(getApplicationContext());
        if (fotoUpdate.aggiornaFotoProfilo(iscritto)) {
            //Toast.makeText(getApplicationContext(), "Foto aggiornata", Toast.LENGTH_SHORT).show();
            Log.e("Foto", "aggiornata");
        }

    }

    /**
     * Funzioni per la gestione delle immagini del profilo
     * <p>
     * https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-from-internal-memory-in-android
     */

    void cambiaFotoProfilo() {

        loadFileList();

        File mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        FileDialog fileDialog = new FileDialog(this, mPath, ".jpg");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());
                //Toast.makeText(getApplicationContext(), "selected file " + file.toString(), Toast.LENGTH_SHORT).show();

                loadImageFromStorage(file.toString());  //Carica l'immagine dato in ingresso il percorso delle foto e le setta nella imageview

                Log.e("Foto presa da ", file.toString());

                fotoProfilo.buildDrawingCache();    //prepara la foto in bitmap
                aggiornaFoto(fotoProfilo.getDrawingCache());    //da in ingresso l'immagine bitmap e restituisce il percorso di salvataggio dopo averla settata nell'oggetto

            }
        });
        fileDialog.showDialog();

    }

    /**
     * salva nella memoria interna l'immagine selezionata e restituisce il percorso
     */

    private String saveToInternalStorage(Bitmap bitmapImage) {

        String nomeFoto = iscritto.getIdCorso() + " " + iscritto.getIdDatabase() + ".jpg";

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, nomeFoto);

        FileOutputStream fos = null;

        try {

            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 60, fos);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.e("patH", mypath.getPath());

        return mypath.getPath(); //corretto
    }

    /**
     * Aggiorna il parametro dell'oggetto iscritto e salva nella memoria interna l'immagine selezionata
     *
     * @param bitmapImage da in ingresso l'immagine bitmap e restituisce il percorso di salvataggio dopo averla settata nell'oggetto
     **/

    private void aggiornaFoto(Bitmap bitmapImage) {

        iscritto.setUrlFoto(saveToInternalStorage(bitmapImage));

    }

    /**
     * Carica l'immagine dato in ingresso il percorso delle foto e le setta nella imageview
     *
     * @param path il percorso dell'immagine
     */

    private void loadImageFromStorage(String path) {

        try {

            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            fotoProfilo = findViewById(R.id.fotoProfilo);
            fotoProfilo.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Carica la lista dei file da visualizzare
     **/

    private void loadFileList() {
        try {
            mPath.mkdirs();
        } catch (SecurityException e) {
            Log.e(TAG, "unable to write on the sd card " + e.toString());
        }
        if (mPath.exists()) {
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    return filename.contains(FTYPE) || sel.isDirectory();
                }

            };
            mFileList = mPath.list(filter);
        } else {
            mFileList = new String[0];
        }
    }


    /**
     * Crea la finestra di dialogo per la visualizzazione dellle cartelle
     */

    protected Dialog onCreateDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        switch (id) {
            case DIALOG_LOAD_FILE:
                builder.setTitle("Choose your file");
                if (mFileList == null) {
                    Log.e(TAG, "Showing file picker before loading the file list");
                    dialog = builder.create();
                    return dialog;
                }
                builder.setItems(mFileList, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mChosenFile = mFileList[which];
                        //you can do stuff with the file here too
                    }
                });
                break;
        }
        dialog = builder.show();
        return dialog;
    }

    /**richiesta permessi lettura memoria interna */

    void richiestaPermessiLettura() {

        //richiedere permessi scrittura
        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (permissionCheck
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Dettagli.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Dettagli.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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

                    finish();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    /**finestra di dialogo per la richiesta dei permessi, utente di merda che non ti fidi*/

    void dialogPermessi() {

        //finestra di conferma
        AlertDialog.Builder builder = new AlertDialog.Builder(Dettagli.this);

        builder.setTitle(R.string.profilePic);
        builder.setMessage(R.string.richiestaPermessi);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }
        });
        builder.show();

    }

}
