package com.alessiomanai.gymregister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryPagamento;

public class PagamentiIscritto extends Activity {

    static int posizione;
    static Corso corso;
    static Iscritto iscritto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamenti_iscritto);

        TextView nomePagante = (TextView) findViewById(R.id.nomePagante);
        nomePagante.setText(iscritto.getId());

        /*
        ImageButton aggiungiPagamento = (ImageButton) findViewById(R.id.aggiungiPagamento);

        aggiungiPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Funzione al momento non disponibile", Toast.LENGTH_SHORT).show();


            }
        });*/

        /***serie di dettagli sui pagamenti*/

        final CheckedTextView iscrizione = (CheckedTextView) findViewById(R.id.checkedTextView1);

        iscrizione.setClickable(true);

        //se la prima lettera è "non pagato" rende il check clickabile
        if (iscritto.getIscrizione().charAt(0) == 'n')
            iscrizione.setChecked(false);

        iscrizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iscrizione.isChecked() == false) {    //se non è spuntato lo rende spuntabile
                    iscrizione.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setIscrizione("pagato");
                    notificaPagamento(R.string.iscrizione);
                } else {
                    iscrizione.setChecked(false);    //altrimenti si può modificare
                    GestioneIscritti.iscritti.get(posizione).setIscrizione("nonpagato");
                }
            }
        });

        //settembre
        final CheckedTextView settembre = (CheckedTextView) findViewById(R.id.checkedTextView2);

        if (iscritto.getSettembre().charAt(0) == 'n')
            settembre.setChecked(false);

        settembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settembre.isChecked() == false) {
                    settembre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setSettembre("pagato");
                    notificaPagamento(R.string.sept);
                } else {
                    settembre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setSettembre("nonpagato");
                }

            }
        });

        //ottobre
        final CheckedTextView ottobre = (CheckedTextView) findViewById(R.id.checkedTextView3);

        if (iscritto.getOttobre().charAt(0) == 'n')
            ottobre.setChecked(false);

        ottobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ottobre.isChecked() == false) {
                    ottobre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setOttobre("pagato");
                    notificaPagamento(R.string.oct);

                } else {
                    ottobre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setOttobre("nonpagato");
                }
            }
        });


        final CheckedTextView novembre = (CheckedTextView) findViewById(R.id.checkedTextView4);

        if (iscritto.getNovembre().charAt(0) == 'n')
            novembre.setChecked(false);

        novembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (novembre.isChecked() == false) {
                    novembre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setNovembre("pagato");
                    notificaPagamento(R.string.nov);

                } else {
                    novembre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setNovembre("nonpagato");
                }
            }
        });

        final CheckedTextView dicembre = (CheckedTextView) findViewById(R.id.checkedTextView5);

        if (iscritto.getDicembre().charAt(0) == 'n')
            dicembre.setChecked(false);

        dicembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dicembre.isChecked() == false) {
                    dicembre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setDicembre("pagato");
                    notificaPagamento(R.string.dec);

                } else {
                    dicembre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setDicembre("nonpagato");
                }

            }
        });

        final CheckedTextView gennaio = (CheckedTextView) findViewById(R.id.checkedTextView6);

        if (iscritto.getGennaio().charAt(0) == 'n')
            gennaio.setChecked(false);

        gennaio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gennaio.isChecked() == false) {
                    gennaio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setGennaio("pagato");
                    notificaPagamento(R.string.jan);

                } else {
                    gennaio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setGennaio("nonpagato");
                }
            }
        });

        final CheckedTextView febbraio = (CheckedTextView) findViewById(R.id.checkedTextView7);

        if (iscritto.getFebbraio().charAt(0) == 'n')
            febbraio.setChecked(false);

        febbraio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (febbraio.isChecked() == false) {
                    febbraio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setFebbraio("pagato");
                    notificaPagamento(R.string.feb);

                } else {
                    febbraio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setFebbraio("nonpagato");
                }

            }
        });

        final CheckedTextView marzo = (CheckedTextView) findViewById(R.id.checkedTextView8);

        if (iscritto.getMarzo().charAt(0) == 'n')
            marzo.setChecked(false);

        marzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (marzo.isChecked() == false) {
                    marzo.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setMarzo("pagato");
                    notificaPagamento(R.string.mar);

                } else {
                    marzo.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setMarzo("nonpagato");
                }
            }
        });

        final CheckedTextView aprile = (CheckedTextView) findViewById(R.id.checkedTextView9);

        if (iscritto.getAprile().charAt(0) == 'n')
            aprile.setChecked(false);

        aprile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aprile.isChecked() == false) {
                    aprile.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setAprile("pagato");
                    notificaPagamento(R.string.apr);

                } else {
                    aprile.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setAprile("nonpagato");
                }
            }
        });

        final CheckedTextView maggio = (CheckedTextView) findViewById(R.id.checkedTextView10);

        if (iscritto.getMaggio().charAt(0) == 'n')
            maggio.setChecked(false);

        maggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (maggio.isChecked() == false) {
                    maggio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setMaggio("pagato");
                    notificaPagamento(R.string.mag);

                } else {
                    maggio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setMaggio("nonpagato");
                }
            }
        });

        final CheckedTextView giugno = (CheckedTextView) findViewById(R.id.checkedTextView11);

        if (iscritto.getGiugno().charAt(0) == 'n')
            giugno.setChecked(false);

        giugno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (giugno.isChecked() == false) {
                    giugno.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setGiugno("pagato");
                    notificaPagamento(R.string.jun);

                } else {
                    giugno.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setGiugno("nonpagato");
                }
            }
        });

        final CheckedTextView luglio = (CheckedTextView) findViewById(R.id.checkedTextView12);

        if (iscritto.getLuglio().charAt(0) == 'n')
            luglio.setChecked(false);

        luglio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (luglio.isChecked() == false) {
                    luglio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setLuglio("pagato");
                    notificaPagamento(R.string.jul);

                } else {
                    luglio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setLuglio("nonpagato");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        salvaModifiche();

        Dettagli.iscritto.setPagamenti(GestioneIscritti.iscritti.get(posizione).getPagamenti());

        Dettagli.caricaRiepilogoPagamenti(this);

        finish();
    }

    void notificaPagamento(int mese) {

        Toast.makeText(this, getResources().getString(mese) + " "
                + getResources().getString(R.string.pagato), Toast.LENGTH_SHORT).show();

    }

    /**
     * salva i dati di tutti gli utenti
     */
    void salvaModifiche() {

        QueryPagamento pagamento = new QueryPagamento(this);
        pagamento.update(pagamento, iscritto);

    }

}
