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

        /*menu.add(R.string.men3).setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
			public boolean onMenuItemClick(MenuItem item) {

				try {

					salvaBackup();

				} catch (Exception e) {

					Toast.makeText(GestioneIscritti.this, R.string.ferror, Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
				return true;
			}
		});

		menu.add(R.string.men4).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				AlertDialog.Builder builder = new AlertDialog.Builder(GestioneIscritti.this);

				builder.setTitle(R.string.conferma);
				builder.setMessage(R.string.ripristino);
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.ripristina, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Confermato!

						try {

							caricaBackup();

						} catch (IOException e) {

							messaggioBackupNonPresente();
						}
						ricarica();

						dialog.dismiss();
					}
				});

				builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Annullato!
						//Toast.makeText(GestioneIscritti.this, "Operazione annullata", Toast.LENGTH_SHORT).show();

						dialog.dismiss();
					}
				});

				builder.show();

				return true;
			}
		});

		menu.add(R.string.men5).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				//finestra di conferma
				AlertDialog.Builder builder = new AlertDialog.Builder(GestioneIscritti.this);

				builder.setTitle(R.string.conferma);
				builder.setMessage(R.string.cleall);
				builder.setCancelable(false);
				builder.setPositiveButton(R.string.elimina, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Confermato!

						cancelladati();
						eliminato();
						ricarica();

						dialog.dismiss();
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


				return true;
			}
		});*/

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
