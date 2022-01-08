package com.alessiomanai.gymregister;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.database.QueryCorso;
import com.alessiomanai.gymregister.utils.BackupManager;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;

import java.util.ArrayList;


public class Gestionepalestre extends Activity {

    private ArrayList<Corso> palestre = new ArrayList<>(); //stringhe selezione palestre
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1000;

    /***
     * avvio del codice
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionepalestre);

        View noPalestre = findViewById(R.id.addGYM);

        palestre = caricaDatabase();

        bottoni();    //avvia i bottoni interattivi

        //richiedere permessi scrittura
        int permissionCheck = ContextCompat.checkSelfPermission(Gestionepalestre.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(Gestionepalestre.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(Gestionepalestre.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(Gestionepalestre.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        if (palestre.size() > 0) {    //se ci sono palestre in memoria mostra la lista di selezione

            noPalestre.setVisibility(View.GONE);

            //collego la listview dell'inferfaccia
            ListView list1 = (ListView) this.findViewById(R.id.listView);
            ListatorePalestre adapter = new ListatorePalestre(Gestionepalestre.this, palestre);
            list1.setAdapter(adapter);

            //rendo la lista clickabile
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {    //a seconda della posizione (rilevata automaticamente) apro una palestra

                    Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);
                    gestioneiscritti.putExtra(ExtrasConstants.CORSO, palestre.get(position));
                    //avvia la finestra corrispondente
                    startActivity(gestioneiscritti);

                }
            });        //fine lista clickabile


        } else {

            noPalestre.setVisibility(View.VISIBLE);

            noPalestre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {

                    Intent aggiungi = new Intent(getBaseContext(), AggiungiPalestra.class);

                    //avvia la finestra corrispondente
                    startActivity(aggiungi);

                    palestre.clear();

                    finish();

                }
            });  //fine bottone

        }
    }    //fine oncreate


    /**
     * carica i dettagli delle palestre da database
     */
    ArrayList<Corso> caricaDatabase() {

        QueryCorso database = new QueryCorso(this);
        return database.getElencoCorsi(database);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if (item.getItemId() == R.id.infos) {
            Intent info = new Intent(getBaseContext(), InfoApp.class);
            //avvia la finestra corrispondente
            startActivity(info);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /***
     * gestione dei bottoni
     */
    void bottoni() {

        ImageButton aggiungipalestra = findViewById(R.id.paladd);
        ImageButton eliminapalestra = findViewById(R.id.palelim);
        ImageButton rinominaPalestra = findViewById(R.id.renameCorso);
        ImageButton backupRestore = findViewById(R.id.backupData);


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


        rinominaPalestra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent aggiungi = new Intent(getBaseContext(), RinominaCorso.class);

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

        backupRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                backupData();

            }
        });  //fine bottone

    }

    void backupData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Gestionepalestre.this);

        builder.setTitle(R.string.backupmenu);
        builder.setMessage(R.string.backupdescription);

        builder.setCancelable(false);

        builder.setNeutralButton(R.string.annulla, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Confermato!

                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.ripristina, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Confermato!

                restoreDialogGym();

                dialog.dismiss();
            }
        });
        //negativa
        builder.setNegativeButton(R.string.backup, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                BackupManager bm = new BackupManager();

                bm.doBackup(getApplicationContext());

                dialog.dismiss();

            }
        });
        //visualizza la finestra
        builder.show();

    }

    void restoreDialogGym() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Gestionepalestre.this);

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
                Toast.makeText(Gestionepalestre.this, R.string.opannul, Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });
        //visualizza la finestra
        builder.show();

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
