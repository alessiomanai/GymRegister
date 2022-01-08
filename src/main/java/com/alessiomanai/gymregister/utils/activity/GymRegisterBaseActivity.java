package com.alessiomanai.gymregister.utils.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alessiomanai.gymregister.Aggiungi;
import com.alessiomanai.gymregister.CambiaCorso;
import com.alessiomanai.gymregister.Dettagli;
import com.alessiomanai.gymregister.GestioneIscritti;
import com.alessiomanai.gymregister.ModificaIscritto;
import com.alessiomanai.gymregister.Note;
import com.alessiomanai.gymregister.PagamentiIscritto;
import com.alessiomanai.gymregister.Presenze;
import com.alessiomanai.gymregister.PresenzeUtente;
import com.alessiomanai.gymregister.R;
import com.alessiomanai.gymregister.Risultati;
import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;

import java.util.ArrayList;
import java.util.List;

public class GymRegisterBaseActivity extends Activity {

    protected void getDettagliActivity(int position, List<Iscritto> iscritti, Corso palestra) {
        Intent dettagli = new Intent(getBaseContext(), Dettagli.class);

        dettagli.putExtra(ExtrasConstants.POSITION, position);
        dettagli.putExtra(ExtrasConstants.ISCRITTO, iscritti.get(position));
        dettagli.putExtra(ExtrasConstants.CORSO, palestra);
        startActivity(dettagli);
    }

    protected void getDettagliActivity(int position, Iscritto iscritto, Corso palestra) {
        Intent dettagli = new Intent(getBaseContext(), Dettagli.class);

        dettagli.putExtra(ExtrasConstants.POSITION, position);
        dettagli.putExtra(ExtrasConstants.ISCRITTO, iscritto);
        dettagli.putExtra(ExtrasConstants.CORSO, palestra);
        startActivity(dettagli);
    }

    protected void getAggiungiIscritto(Corso palestra) {
        Intent crea = new Intent(getBaseContext(), Aggiungi.class);
        crea.putExtra(ExtrasConstants.CORSO, palestra);
        startActivity(crea);
    }

    protected void getGestioneIscritti(Corso palestra) {
        Intent gestioneiscritti = new Intent(getBaseContext(), GestioneIscritti.class);
        gestioneiscritti.putExtra(ExtrasConstants.CORSO, palestra);
        startActivity(gestioneiscritti);
    }

    protected void getPagamentiIscritto(int posizione, Iscritto iscritto, Corso palestra) {
        Intent asd = new Intent(getBaseContext(), PagamentiIscritto.class);
        asd.putExtra(ExtrasConstants.POSITION, posizione);
        asd.putExtra(ExtrasConstants.ISCRITTO, iscritto);
        asd.putExtra(ExtrasConstants.CORSO, palestra);
        startActivity(asd);
    }

    protected void getModificaIscritto(int posizione, Iscritto iscritto, Corso palestra) {
        Intent modifica = new Intent(getBaseContext(), ModificaIscritto.class);
        modifica.putExtra(ExtrasConstants.POSITION, posizione);
        modifica.putExtra(ExtrasConstants.ISCRITTO, iscritto);
        modifica.putExtra(ExtrasConstants.CORSO, palestra);
        startActivity(modifica);
    }

    protected void getPresenzeIscritto(Iscritto iscritto) {
        Intent asd = new Intent(getBaseContext(), PresenzeUtente.class);
        asd.putExtra(ExtrasConstants.ISCRITTO, iscritto);
        startActivity(asd);
    }

    protected void getCambiaCorso(Iscritto iscritto) {
        Intent cambiapalestra = new Intent(getBaseContext(), CambiaCorso.class);

        cambiapalestra.putExtra(ExtrasConstants.ISCRITTO, iscritto);

        startActivity(cambiapalestra);
    }

    protected void getNote(Iscritto iscritto) {
        Intent note = new Intent(getBaseContext(), Note.class);
        note.putExtra(ExtrasConstants.ISCRITTO, iscritto);
        note.putExtra(ExtrasConstants.NOTE_ISCRITTO, true);
        startActivity(note);
    }

    protected void getNote(Corso corso) {
        Intent note = new Intent(getBaseContext(), Note.class);
        note.putExtra(ExtrasConstants.CORSO, corso);
        note.putExtra(ExtrasConstants.NOTE_ISCRITTO, false);
        startActivity(note);
    }

    protected void getPresenzeCorso(Corso corso) {
        Intent presenzew = new Intent(getBaseContext(), Presenze.class);
        presenzew.putExtra(ExtrasConstants.CORSO, corso);

        startActivity(presenzew);
    }

    protected void getRisultatiRicerca(Corso corso, ArrayList<Iscritto> risultati) {
        Intent risultatiIntent = new Intent(getBaseContext(), Risultati.class);
        risultatiIntent.putExtra(ExtrasConstants.CORSO, corso);
        risultatiIntent.putExtra(ExtrasConstants.ISCRITTO, risultati);
        startActivity(risultatiIntent);

    }

    protected String caricaRiepilogoPagamenti(Iscritto iscritto, Context context) {

        String riepilogo = context.getResources().getString(R.string.riepilogo);
        boolean nessunPagamento = true;

        riepilogo += " " + context.getResources().getString(R.string.pay) + ":\n";

        try {

            if (iscritto.getIscrizione().charAt(0) == 'p') {
                riepilogo += "" + context.getResources().getString(R.string.iscrizione);
                nessunPagamento = false;
            }

            if (iscritto.getSettembre().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.sept);
                nessunPagamento = false;
            }

            if (iscritto.getOttobre().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.oct);
                nessunPagamento = false;
            }

            if (iscritto.getNovembre().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.nov);
                nessunPagamento = false;
            }

            if (iscritto.getDicembre().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.dec);
                nessunPagamento = false;
            }

            if (iscritto.getGennaio().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.jan);
                nessunPagamento = false;
            }

            if (iscritto.getFebbraio().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.feb);
                nessunPagamento = false;
            }

            if (iscritto.getMarzo().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.mar);
                nessunPagamento = false;
            }

            if (iscritto.getAprile().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.apr);
                nessunPagamento = false;
            }

            if (iscritto.getMaggio().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.mag);
                nessunPagamento = false;
            }

            if (iscritto.getGiugno().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.jun);
                nessunPagamento = false;
            }

            if (iscritto.getLuglio().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.jul);
                nessunPagamento = false;
            }

            if (iscritto.getAgosto().charAt(0) == 'p') {
                riepilogo += " " + context.getResources().getString(R.string.ago);
                nessunPagamento = false;
            }

            if (nessunPagamento) {
                riepilogo += context.getResources().getString(R.string.nopay);
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return riepilogo;

    }
}
