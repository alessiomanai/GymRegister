package com.alessiomanai.gymregister.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.alessiomanai.gymregister.R;
import com.alessiomanai.gymregister.classi.Iscritto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Save extends Activity{

    public Save() {
    }

    boolean users(){



        return true;

    }

    /**
     * salva i dati di tutti gli utenti sui file
     */

    public void salvafile(ArrayList<Iscritto> iscritti, String filenomi, String fileaddr,
                          String filetelph, String filedatanascita, String filecitta, String fileiscrizione) {

        String[] filemesi = new String[12];

        //scrive file
        try {

            /***dati utenti*/
            OutputStreamWriter fileid = new OutputStreamWriter(openFileOutput(filenomi, Context.MODE_PRIVATE));
            OutputStreamWriter fileaddress = new OutputStreamWriter(openFileOutput(fileaddr, Context.MODE_PRIVATE));
            OutputStreamWriter filetel = new OutputStreamWriter(openFileOutput(filetelph, Context.MODE_PRIVATE));
            OutputStreamWriter filedatn = new OutputStreamWriter(openFileOutput(filedatanascita, Context.MODE_PRIVATE));
            OutputStreamWriter filecit = new OutputStreamWriter(openFileOutput(filecitta, Context.MODE_PRIVATE));

            /***info pagamenti*/
            OutputStreamWriter filepag = new OutputStreamWriter(openFileOutput(fileiscrizione, Context.MODE_PRIVATE));

            //creazione e dichiarazione dei file contenenti le mensilità pagate
            OutputStreamWriter file0 = new OutputStreamWriter(openFileOutput(filemesi[0], Context.MODE_PRIVATE));
            OutputStreamWriter file1 = new OutputStreamWriter(openFileOutput(filemesi[1], Context.MODE_PRIVATE));
            OutputStreamWriter file2 = new OutputStreamWriter(openFileOutput(filemesi[2], Context.MODE_PRIVATE));
            OutputStreamWriter file3 = new OutputStreamWriter(openFileOutput(filemesi[3], Context.MODE_PRIVATE));
            OutputStreamWriter file4 = new OutputStreamWriter(openFileOutput(filemesi[4], Context.MODE_PRIVATE));
            OutputStreamWriter file5 = new OutputStreamWriter(openFileOutput(filemesi[5], Context.MODE_PRIVATE));
            OutputStreamWriter file6 = new OutputStreamWriter(openFileOutput(filemesi[6], Context.MODE_PRIVATE));
            OutputStreamWriter file7 = new OutputStreamWriter(openFileOutput(filemesi[7], Context.MODE_PRIVATE));
            OutputStreamWriter file8 = new OutputStreamWriter(openFileOutput(filemesi[8], Context.MODE_PRIVATE));
            OutputStreamWriter file9 = new OutputStreamWriter(openFileOutput(filemesi[9], Context.MODE_PRIVATE));
            OutputStreamWriter file10 = new OutputStreamWriter(openFileOutput(filemesi[10], Context.MODE_PRIVATE));
            //OutputStreamWriter file11 = new OutputStreamWriter(openFileOutput(filemesi[11], Context.MODE_WORLD_READABLE));


            //salva i dati nel file
            for(int i = 0; i < iscritti.size(); i++) {

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


    /**
     * salva il backup dei dati
     */
    public void salvaBackup(ArrayList<Iscritto> iscritti, String filenomi, String fileaddr,
                            String filetelph, String filedatanascita, String filecitta, String fileiscrizione) {

        //String filenomi, fileaddr, filetelph, filedatanascita, filecitta, fileiscrizione;
        String[] filemesi = new String[12];

        //scrive file
        try {

            /***dati utenti*/
            OutputStreamWriter fileid = new OutputStreamWriter(openFileOutput("Backup" + filenomi, Context.MODE_PRIVATE));
            OutputStreamWriter fileaddress = new OutputStreamWriter(openFileOutput("Backup" + fileaddr, Context.MODE_PRIVATE));
            OutputStreamWriter filetel = new OutputStreamWriter(openFileOutput("Backup" + filetelph, Context.MODE_PRIVATE));
            OutputStreamWriter filedatn = new OutputStreamWriter(openFileOutput("Backup" + filedatanascita, Context.MODE_PRIVATE));
            OutputStreamWriter filecit = new OutputStreamWriter(openFileOutput("Backup" + filecitta, Context.MODE_PRIVATE));

            /***info pagamenti*/
            OutputStreamWriter filepag = new OutputStreamWriter(openFileOutput("Backup" + fileiscrizione, Context.MODE_PRIVATE));

            //creazione e dichiarazione dei file contenenti le mensilità pagate
            OutputStreamWriter file0 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[0], Context.MODE_PRIVATE));
            OutputStreamWriter file1 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[1], Context.MODE_PRIVATE));
            OutputStreamWriter file2 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[2], Context.MODE_PRIVATE));
            OutputStreamWriter file3 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[3], Context.MODE_PRIVATE));
            OutputStreamWriter file4 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[4], Context.MODE_PRIVATE));
            OutputStreamWriter file5 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[5], Context.MODE_PRIVATE));
            OutputStreamWriter file6 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[6], Context.MODE_PRIVATE));
            OutputStreamWriter file7 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[7], Context.MODE_PRIVATE));
            OutputStreamWriter file8 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[8], Context.MODE_PRIVATE));
            OutputStreamWriter file9 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[9], Context.MODE_PRIVATE));
            OutputStreamWriter file10 = new OutputStreamWriter(openFileOutput("Backup" + filemesi[10], Context.MODE_PRIVATE));
            //OutputStreamWriter file11 = new OutputStreamWriter(openFileOutput(filemesi[11], Context.MODE_WORLD_READABLE));


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
}
