package com.alessiomanai.gymregister.utils;

import com.alessiomanai.gymregister.classi.Iscritto;

import java.util.ArrayList;


public class Search {

    /**
     * semplice funzione ricerca utente
     */

    public static ArrayList<Iscritto> ricerca(String key, ArrayList<Iscritto> iscritti) {

        ArrayList<Iscritto> risultati = new ArrayList<>();
        String tuttoMinuscolo;

        for (int i = 0; i < iscritti.size(); i++) {

            tuttoMinuscolo = iscritti.get(i).getId().toLowerCase(); //rende i caratteri tutti minuscoli

            if (iscritti.get(i).getId().contains(key) || tuttoMinuscolo.contains(key))
                risultati.add(iscritti.get(i));

        }

        return risultati;

    }

}
