package com.alessiomanai.gymregister.utils;

import android.app.Activity;

import com.alessiomanai.gymregister.classi.Iscritto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Load extends Activity {

    final int NUMEROCAMPI = 15;

    public ArrayList<Iscritto> usersFromFile(String nomepal) {

        ArrayList<Iscritto> iscritti = new ArrayList<>();

        String[] buffer = new String[NUMEROCAMPI]; //stringa di buffer informazioni lette
        FileInputStream[] asd = new FileInputStream[NUMEROCAMPI];
        InputStreamReader[] inputStreamReader = new InputStreamReader[NUMEROCAMPI];
        BufferedReader[] bufferedReader = new BufferedReader[NUMEROCAMPI];

        int i = 0; //contatore posizione

        try {

            /**apre in sequenza tutti i file*/
            asd[0] = openFileInput(nomepal + "id.txt");
            inputStreamReader[0] = new InputStreamReader(asd[0]);
            bufferedReader[0] = new BufferedReader(inputStreamReader[0]);

            asd[1] = openFileInput(nomepal + "address.txt");
            inputStreamReader[1] = new InputStreamReader(asd[1]);
            bufferedReader[1] = new BufferedReader(inputStreamReader[1]);

            asd[2] = openFileInput(nomepal + "tel.txt");
            inputStreamReader[2] = new InputStreamReader(asd[2]);
            bufferedReader[2] = new BufferedReader(inputStreamReader[2]);

            asd[3] = openFileInput(nomepal + "nascita.txt");
            inputStreamReader[3] = new InputStreamReader(asd[3]);
            bufferedReader[3] = new BufferedReader(inputStreamReader[3]);

            asd[4] = openFileInput(nomepal + "citta.txt");
            inputStreamReader[4] = new InputStreamReader(asd[4]);
            bufferedReader[4] = new BufferedReader(inputStreamReader[4]);

            asd[5] = openFileInput(nomepal + "iscrizione.txt");
            inputStreamReader[5] = new InputStreamReader(asd[5]);
            bufferedReader[5] = new BufferedReader(inputStreamReader[5]);

            while ((buffer[0] = bufferedReader[0].readLine()) != null) {

                //aggiunge un nuovo utente dandogli solo il nome
                iscritti.add(new Iscritto(buffer[0] + "\n"));

                //carica gli indirizzi
                buffer[1] = bufferedReader[1].readLine();
                iscritti.get(i).setIndirizzo(buffer[1] + "\n");

                //carica i numeri di telefono
                buffer[2] = bufferedReader[2].readLine();
                iscritti.get(i).setTelefono(buffer[2] + "\n");

                //carica le date di nascita
                buffer[3] = bufferedReader[3].readLine();
                iscritti.get(i).setDataDiNascita(buffer[3] + "\n");

                //carica la città
                buffer[4] = bufferedReader[4].readLine();
                iscritti.get(i).setCitta(buffer[4] + "\n");

                //carica i dati sull'scrizione
                buffer[5] = bufferedReader[5].readLine();
                iscritti.get(i).setIscrizione(buffer[5] + "\n");

                i++;
            }


            /**pagamenti mensili
             * scritti in questo modo perché stava diventando snervante con l'altro metodo*/

            asd[6] = openFileInput(nomepal + "settembre.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setSettembre(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "ottobre.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setOttobre(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "novembre.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setNovembre(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "dicembre.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setDicembre(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "gennaio.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setGennaio(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "febbraio.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setFebbraio(buffer[6]);
            }


            asd[6] = openFileInput(nomepal + "marzo.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setMarzo(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "aprile.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setAprile(buffer[6]);
            }


            asd[6] = openFileInput(nomepal + "maggio.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setMaggio(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "giugno.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setGiugno(buffer[6]);
            }

            asd[6] = openFileInput(nomepal + "luglio.txt");
            inputStreamReader[6] = new InputStreamReader(asd[6]);
            bufferedReader[6] = new BufferedReader(inputStreamReader[6]);

            for (i = 0; i < iscritti.size(); i++) {

                buffer[6] = bufferedReader[6].readLine();
                iscritti.get(i).setLuglio(buffer[6]);
            }

            /**chiude tutti i lettori utilizzati*/
            for (i = 0; i < NUMEROCAMPI; i++) {

                asd[i].close();
                inputStreamReader[i].close();
                bufferedReader[i].close();
            }


        } catch (FileNotFoundException fnfe) {

        } catch (IOException ioe) {


        }

        return iscritti;

    }
}
