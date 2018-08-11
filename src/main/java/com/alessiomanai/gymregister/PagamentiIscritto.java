package com.alessiomanai.gymregister;

import android.app.Activity;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryImporti;
import com.alessiomanai.gymregister.database.QueryPagamento;

import java.util.ArrayList;

public class PagamentiIscritto extends Activity {

    static int posizione;
    static Corso corso;
    static Iscritto iscritto;
    private EditText feeIscrizione, feeSettembre, feeOttobre, feeNovembre, feeDicembre,
            feeGennaio, feeFebbraio, feeMarzo, feeAprile, feeMaggio, feeGiugno, feeLuglio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamenti_iscritto);

        TextView nomePagante = findViewById(R.id.nomePagante);
        nomePagante.setText(iscritto.getId());

        //caselle per gli importi
        feeIscrizione = findViewById(R.id.feeIscrizione);
        feeSettembre = findViewById(R.id.feeSette);
        feeOttobre = findViewById(R.id.feeOtto);
        feeNovembre = findViewById(R.id.feeNove);
        feeDicembre = findViewById(R.id.feeDice);
        feeGennaio = findViewById(R.id.feeGen);
        feeFebbraio = findViewById(R.id.feeFeb);
        feeMarzo = findViewById(R.id.feeMar);
        feeAprile = findViewById(R.id.feeApr);
        feeMaggio = findViewById(R.id.feeMag);
        feeGiugno = findViewById(R.id.feeGiu);
        feeLuglio = findViewById(R.id.feeLug);

        caricaDatabaseImporti();

        /*
        ImageButton aggiungiPagamento = (ImageButton) findViewById(R.id.aggiungiPagamento);

        aggiungiPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(v.getContext(), "Funzione al momento non disponibile", Toast.LENGTH_SHORT).show();


            }
        });*/

        /***serie di dettagli sui pagamenti*/

        final CheckedTextView iscrizione = findViewById(R.id.checkedTextView1);

        iscrizione.setClickable(true);

        //se la prima lettera è "non pagato" rende il check clickabile
        if (iscritto.getIscrizione().charAt(0) == 'n') {
            iscrizione.setChecked(false);
            feeIscrizione.setVisibility(View.GONE);

        }

        iscrizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iscrizione.isChecked() == false) {    //se non è spuntato lo rende spuntabile
                    iscrizione.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setIscrizione("pagato");
                    notificaPagamento(R.string.iscrizione);
                    feeIscrizione.setVisibility(View.VISIBLE);
                } else {
                    iscrizione.setChecked(false);    //altrimenti si può modificare
                    GestioneIscritti.iscritti.get(posizione).setIscrizione("nonpagato");
                    feeIscrizione.setVisibility(View.GONE);

                }
            }
        });

        //settembre
        final CheckedTextView settembre = findViewById(R.id.checkedTextView2);

        if (iscritto.getSettembre().charAt(0) == 'n') {
            settembre.setChecked(false);
            feeSettembre.setVisibility(View.GONE);
        }

        settembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settembre.isChecked() == false) {
                    settembre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setSettembre("pagato");
                    notificaPagamento(R.string.sept);
                    feeSettembre.setVisibility(View.VISIBLE);
                } else {
                    settembre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setSettembre("nonpagato");
                    feeSettembre.setVisibility(View.GONE);

                }

            }
        });

        //ottobre
        final CheckedTextView ottobre = findViewById(R.id.checkedTextView3);

        if (iscritto.getOttobre().charAt(0) == 'n') {
            ottobre.setChecked(false);
            feeOttobre.setVisibility(View.GONE);
        }

        ottobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ottobre.isChecked() == false) {
                    ottobre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setOttobre("pagato");
                    notificaPagamento(R.string.oct);
                    feeOttobre.setVisibility(View.VISIBLE);

                } else {
                    ottobre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setOttobre("nonpagato");
                    feeOttobre.setVisibility(View.GONE);

                }
            }
        });


        final CheckedTextView novembre = findViewById(R.id.checkedTextView4);

        if (iscritto.getNovembre().charAt(0) == 'n') {
            novembre.setChecked(false);
            feeNovembre.setVisibility(View.GONE);
        }

        novembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (novembre.isChecked() == false) {
                    novembre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setNovembre("pagato");
                    notificaPagamento(R.string.nov);
                    feeNovembre.setVisibility(View.VISIBLE);

                } else {
                    novembre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setNovembre("nonpagato");
                    feeNovembre.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView dicembre = findViewById(R.id.checkedTextView5);

        if (iscritto.getDicembre().charAt(0) == 'n') {
            dicembre.setChecked(false);
            feeDicembre.setVisibility(View.GONE);
        }

        dicembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dicembre.isChecked() == false) {
                    dicembre.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setDicembre("pagato");
                    notificaPagamento(R.string.dec);
                    feeDicembre.setVisibility(View.VISIBLE);

                } else {
                    dicembre.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setDicembre("nonpagato");
                    feeDicembre.setVisibility(View.GONE);

                }

            }
        });

        final CheckedTextView gennaio = findViewById(R.id.checkedTextView6);

        if (iscritto.getGennaio().charAt(0) == 'n') {
            gennaio.setChecked(false);
            feeGennaio.setVisibility(View.GONE);
        }

        gennaio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gennaio.isChecked() == false) {
                    gennaio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setGennaio("pagato");
                    notificaPagamento(R.string.jan);
                    feeGennaio.setVisibility(View.VISIBLE);

                } else {
                    gennaio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setGennaio("nonpagato");
                    feeGennaio.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView febbraio = findViewById(R.id.checkedTextView7);

        if (iscritto.getFebbraio().charAt(0) == 'n') {
            febbraio.setChecked(false);
            feeFebbraio.setVisibility(View.GONE);
        }

        febbraio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (febbraio.isChecked() == false) {
                    febbraio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setFebbraio("pagato");
                    notificaPagamento(R.string.feb);
                    feeFebbraio.setVisibility(View.VISIBLE);

                } else {
                    febbraio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setFebbraio("nonpagato");
                    feeFebbraio.setVisibility(View.GONE);

                }

            }
        });

        final CheckedTextView marzo = findViewById(R.id.checkedTextView8);

        if (iscritto.getMarzo().charAt(0) == 'n') {
            marzo.setChecked(false);
            feeMarzo.setVisibility(View.GONE);
        }

        marzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (marzo.isChecked() == false) {
                    marzo.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setMarzo("pagato");
                    notificaPagamento(R.string.mar);
                    feeMarzo.setVisibility(View.VISIBLE);

                } else {
                    marzo.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setMarzo("nonpagato");
                    feeMarzo.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView aprile = findViewById(R.id.checkedTextView9);

        if (iscritto.getAprile().charAt(0) == 'n') {
            aprile.setChecked(false);
            feeAprile.setVisibility(View.GONE);
        }

        aprile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aprile.isChecked() == false) {
                    aprile.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setAprile("pagato");
                    notificaPagamento(R.string.apr);
                    feeAprile.setVisibility(View.VISIBLE);

                } else {
                    aprile.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setAprile("nonpagato");
                    feeAprile.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView maggio = findViewById(R.id.checkedTextView10);

        if (iscritto.getMaggio().charAt(0) == 'n') {
            maggio.setChecked(false);
            feeMaggio.setVisibility(View.GONE);
        }

        maggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (maggio.isChecked() == false) {
                    maggio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setMaggio("pagato");
                    notificaPagamento(R.string.mag);
                    feeMaggio.setVisibility(View.VISIBLE);

                } else {
                    maggio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setMaggio("nonpagato");
                    feeMaggio.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView giugno = findViewById(R.id.checkedTextView11);

        if (iscritto.getGiugno().charAt(0) == 'n') {
            giugno.setChecked(false);
            feeGiugno.setVisibility(View.GONE);
        }

        giugno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (giugno.isChecked() == false) {
                    giugno.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setGiugno("pagato");
                    notificaPagamento(R.string.jun);
                    feeGiugno.setVisibility(View.VISIBLE);

                } else {
                    giugno.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setGiugno("nonpagato");
                    feeGiugno.setVisibility(View.GONE);
                }
            }
        });

        final CheckedTextView luglio = findViewById(R.id.checkedTextView12);

        if (iscritto.getLuglio().charAt(0) == 'n') {
            luglio.setChecked(false);
            feeLuglio.setVisibility(View.GONE);
        }

        luglio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (luglio.isChecked() == false) {
                    luglio.setChecked(true);
                    GestioneIscritti.iscritti.get(posizione).setLuglio("pagato");
                    notificaPagamento(R.string.jul);
                    feeLuglio.setVisibility(View.VISIBLE);

                } else {
                    luglio.setChecked(false);
                    GestioneIscritti.iscritti.get(posizione).setLuglio("nonpagato");
                    feeLuglio.setVisibility(View.GONE);
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

        salvaDatabaseImporti();
    }

    void caricaDatabaseImporti() {
        QueryImporti importi = new QueryImporti(this);

        iscritto.setImporti(importi.caricaImporti(importi, iscritto, corso));

        setOnEditText();

    }

    void setOnEditText() {

        feeIscrizione.setText(iscritto.getImporti().getIscrizione());
        feeSettembre.setText(iscritto.getImporti().getSettembre());
        feeOttobre.setText(iscritto.getImporti().getOttobre());
        feeNovembre.setText(iscritto.getImporti().getNovembre());
        feeDicembre.setText(iscritto.getImporti().getDicembre());
        feeGennaio.setText(iscritto.getImporti().getGennaio());
        feeFebbraio.setText(iscritto.getImporti().getFebbraio());
        feeMarzo.setText(iscritto.getImporti().getMarzo());
        feeAprile.setText(iscritto.getImporti().getAprile());
        feeMaggio.setText(iscritto.getImporti().getMaggio());
        feeGiugno.setText(iscritto.getImporti().getGiugno());
        feeLuglio.setText(iscritto.getImporti().getLuglio());

    }

    void salvaDatabaseImporti() {

        iscritto.getImporti().setIscrizione(feeIscrizione.getText().toString());
        iscritto.getImporti().setSettembre(feeSettembre.getText().toString());
        iscritto.getImporti().setOttobre(feeOttobre.getText().toString());
        iscritto.getImporti().setNovembre(feeNovembre.getText().toString());
        iscritto.getImporti().setDicembre(feeDicembre.getText().toString());
        iscritto.getImporti().setGennaio(feeGennaio.getText().toString());
        iscritto.getImporti().setFebbraio(feeFebbraio.getText().toString());
        iscritto.getImporti().setMarzo(feeMarzo.getText().toString());
        iscritto.getImporti().setAprile(feeAprile.getText().toString());
        iscritto.getImporti().setMaggio(feeMaggio.getText().toString());
        iscritto.getImporti().setGiugno(feeGiugno.getText().toString());
        iscritto.getImporti().setLuglio(feeLuglio.getText().toString());

        QueryImporti database = new QueryImporti(this);
        try {
            database.inserisciImporti(database, iscritto, corso);

        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();

            database.updateImporti(database, iscritto, corso);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        salvaModifiche();
    }
}
