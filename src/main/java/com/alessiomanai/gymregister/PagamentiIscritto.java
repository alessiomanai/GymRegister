package com.alessiomanai.gymregister;

import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.classi.Iscritto;
import com.alessiomanai.gymregister.database.QueryImporti;
import com.alessiomanai.gymregister.database.QueryPagamento;
import com.alessiomanai.gymregister.utils.activity.ExtrasConstants;
import com.alessiomanai.gymregister.utils.activity.GymRegisterBaseActivity;

public class PagamentiIscritto extends GymRegisterBaseActivity {

    private int posizione;
    private Corso corso;
    private Iscritto iscritto;
    private EditText feeIscrizione, feeSettembre, feeOttobre, feeNovembre, feeDicembre,
            feeGennaio, feeFebbraio, feeMarzo, feeAprile, feeMaggio, feeGiugno, feeLuglio, feeAgosto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagamenti_iscritto);

        corso = (Corso) getIntent().getExtras().get(ExtrasConstants.CORSO);
        posizione = (int) getIntent().getExtras().get(ExtrasConstants.POSITION);
        iscritto = (Iscritto) getIntent().getExtras().get(ExtrasConstants.ISCRITTO);

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
        feeAgosto = findViewById(R.id.feeAgo);

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

        try {
            //se la prima lettera è "non pagato" rende il check clickabile
            if (iscritto.getIscrizione().charAt(0) == 'n') {
                iscrizione.setChecked(false);
                feeIscrizione.setVisibility(View.GONE);

            }
        } catch (NullPointerException e) {

            iscrizione.setChecked(false);
            feeIscrizione.setVisibility(View.GONE);

        }

        iscrizione.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iscrizione.isChecked() == false) {    //se non è spuntato lo rende spuntabile
                    iscrizione.setChecked(true);
                    iscritto.setIscrizione("pagato");
                    notificaPagamento(R.string.iscrizione);
                    feeIscrizione.setVisibility(View.VISIBLE);
                } else {
                    iscrizione.setChecked(false);    //altrimenti si può modificare
                    iscritto.setIscrizione("nonpagato");
                    feeIscrizione.setVisibility(View.GONE);

                }
            }
        });

        //settembre
        final CheckedTextView settembre = findViewById(R.id.checkedTextView2);

        try {
            if (iscritto.getSettembre().charAt(0) == 'n') {
                settembre.setChecked(false);
                feeSettembre.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            settembre.setChecked(false);
            feeSettembre.setVisibility(View.GONE);
        }

        settembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (settembre.isChecked() == false) {
                    settembre.setChecked(true);
                    iscritto.setSettembre("pagato");
                    notificaPagamento(R.string.sept);
                    feeSettembre.setVisibility(View.VISIBLE);
                } else {
                    settembre.setChecked(false);
                    iscritto.setSettembre("nonpagato");
                    feeSettembre.setVisibility(View.GONE);

                }

            }
        });

        //ottobre
        final CheckedTextView ottobre = findViewById(R.id.checkedTextView3);

        try {
            if (iscritto.getOttobre().charAt(0) == 'n') {
                ottobre.setChecked(false);
                feeOttobre.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            ottobre.setChecked(false);
            feeOttobre.setVisibility(View.GONE);
        }

        ottobre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ottobre.isChecked() == false) {
                    ottobre.setChecked(true);
                    iscritto.setOttobre("pagato");
                    notificaPagamento(R.string.oct);
                    feeOttobre.setVisibility(View.VISIBLE);

                } else {
                    ottobre.setChecked(false);
                    iscritto.setOttobre("nonpagato");
                    feeOttobre.setVisibility(View.GONE);

                }
            }
        });


        final CheckedTextView novembre = findViewById(R.id.checkedTextView4);

        try {
            if (iscritto.getNovembre().charAt(0) == 'n') {
                novembre.setChecked(false);
                feeNovembre.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            novembre.setChecked(false);
            feeNovembre.setVisibility(View.GONE);
        }

        novembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (novembre.isChecked() == false) {
                    novembre.setChecked(true);
                    iscritto.setNovembre("pagato");
                    notificaPagamento(R.string.nov);
                    feeNovembre.setVisibility(View.VISIBLE);

                } else {
                    novembre.setChecked(false);
                    iscritto.setNovembre("nonpagato");
                    feeNovembre.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView dicembre = findViewById(R.id.checkedTextView5);
        try {
            if (iscritto.getDicembre().charAt(0) == 'n') {
                dicembre.setChecked(false);
                feeDicembre.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            dicembre.setChecked(false);
            feeDicembre.setVisibility(View.GONE);
        }

        dicembre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (dicembre.isChecked() == false) {
                    dicembre.setChecked(true);
                    iscritto.setDicembre("pagato");
                    notificaPagamento(R.string.dec);
                    feeDicembre.setVisibility(View.VISIBLE);

                } else {
                    dicembre.setChecked(false);
                    iscritto.setDicembre("nonpagato");
                    feeDicembre.setVisibility(View.GONE);

                }

            }
        });

        final CheckedTextView gennaio = findViewById(R.id.checkedTextView6);

        try {
            if (iscritto.getGennaio().charAt(0) == 'n') {
                gennaio.setChecked(false);
                feeGennaio.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            gennaio.setChecked(false);
            feeGennaio.setVisibility(View.GONE);
        }

        gennaio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gennaio.isChecked() == false) {
                    gennaio.setChecked(true);
                    iscritto.setGennaio("pagato");
                    notificaPagamento(R.string.jan);
                    feeGennaio.setVisibility(View.VISIBLE);

                } else {
                    gennaio.setChecked(false);
                    iscritto.setGennaio("nonpagato");
                    feeGennaio.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView febbraio = findViewById(R.id.checkedTextView7);

        try {
            if (iscritto.getFebbraio().charAt(0) == 'n') {
                febbraio.setChecked(false);
                feeFebbraio.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            febbraio.setChecked(false);
            feeFebbraio.setVisibility(View.GONE);
        }

        febbraio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (febbraio.isChecked() == false) {
                    febbraio.setChecked(true);
                    iscritto.setFebbraio("pagato");
                    notificaPagamento(R.string.feb);
                    feeFebbraio.setVisibility(View.VISIBLE);

                } else {
                    febbraio.setChecked(false);
                    iscritto.setFebbraio("nonpagato");
                    feeFebbraio.setVisibility(View.GONE);

                }

            }
        });

        final CheckedTextView marzo = findViewById(R.id.checkedTextView8);

        try {
            if (iscritto.getMarzo().charAt(0) == 'n') {
                marzo.setChecked(false);
                feeMarzo.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            marzo.setChecked(false);
            feeMarzo.setVisibility(View.GONE);
        }

        marzo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (marzo.isChecked() == false) {
                    marzo.setChecked(true);
                    iscritto.setMarzo("pagato");
                    notificaPagamento(R.string.mar);
                    feeMarzo.setVisibility(View.VISIBLE);

                } else {
                    marzo.setChecked(false);
                    iscritto.setMarzo("nonpagato");
                    feeMarzo.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView aprile = findViewById(R.id.checkedTextView9);

        try {
            if (iscritto.getAprile().charAt(0) == 'n') {
                aprile.setChecked(false);
                feeAprile.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            aprile.setChecked(false);
            feeAprile.setVisibility(View.GONE);
        }

        aprile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aprile.isChecked() == false) {
                    aprile.setChecked(true);
                    iscritto.setAprile("pagato");
                    notificaPagamento(R.string.apr);
                    feeAprile.setVisibility(View.VISIBLE);

                } else {
                    aprile.setChecked(false);
                    iscritto.setAprile("nonpagato");
                    feeAprile.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView maggio = findViewById(R.id.checkedTextView10);

        try {
            if (iscritto.getMaggio().charAt(0) == 'n') {
                maggio.setChecked(false);
                feeMaggio.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            maggio.setChecked(false);
            feeMaggio.setVisibility(View.GONE);
        }

        maggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (maggio.isChecked() == false) {
                    maggio.setChecked(true);
                    iscritto.setMaggio("pagato");
                    notificaPagamento(R.string.mag);
                    feeMaggio.setVisibility(View.VISIBLE);

                } else {
                    maggio.setChecked(false);
                    iscritto.setMaggio("nonpagato");
                    feeMaggio.setVisibility(View.GONE);

                }
            }
        });

        final CheckedTextView giugno = findViewById(R.id.checkedTextView11);

        try {
            if (iscritto.getGiugno().charAt(0) == 'n') {
                giugno.setChecked(false);
                feeGiugno.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            giugno.setChecked(false);
            feeGiugno.setVisibility(View.GONE);
        }

        giugno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (giugno.isChecked() == false) {
                    giugno.setChecked(true);
                    iscritto.setGiugno("pagato");
                    notificaPagamento(R.string.jun);
                    feeGiugno.setVisibility(View.VISIBLE);

                } else {
                    giugno.setChecked(false);
                    iscritto.setGiugno("nonpagato");
                    feeGiugno.setVisibility(View.GONE);
                }
            }
        });

        final CheckedTextView luglio = findViewById(R.id.checkedTextView12);

        try {
            if (iscritto.getLuglio().charAt(0) == 'n') {
                luglio.setChecked(false);
                feeLuglio.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            luglio.setChecked(false);
            feeLuglio.setVisibility(View.GONE);
        }

        luglio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (luglio.isChecked() == false) {
                    luglio.setChecked(true);
                    iscritto.setLuglio("pagato");
                    notificaPagamento(R.string.jul);
                    feeLuglio.setVisibility(View.VISIBLE);

                } else {
                    luglio.setChecked(false);
                    iscritto.setLuglio("nonpagato");
                    feeLuglio.setVisibility(View.GONE);
                }
            }
        });

        final CheckedTextView agosto = findViewById(R.id.checkedTextView13);

        try {
            if (iscritto.getAgosto().charAt(0) == 'n') {
                agosto.setChecked(false);
                feeAgosto.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            agosto.setChecked(false);
            feeAgosto.setVisibility(View.GONE);
        }

        agosto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (agosto.isChecked() == false) {
                    agosto.setChecked(true);
                    iscritto.setAgosto("pagato");
                    notificaPagamento(R.string.ago);
                    feeAgosto.setVisibility(View.VISIBLE);

                } else {
                    agosto.setChecked(false);
                    iscritto.setAgosto("nonpagato");
                    feeAgosto.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        salvaModifiche();

        getDettagliActivity(posizione, iscritto, corso);

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

        QueryPagamento pagamento = QueryPagamento.getInstance(this);

        try {
            pagamento.inizializza(iscritto, corso);
        } catch (SQLException e){
            e.printStackTrace();
            pagamento.update(iscritto);
        }

        salvaDatabaseImporti();
    }

    void caricaDatabaseImporti() {
        QueryImporti importi = QueryImporti.getInstance(this);

        iscritto.setImporti(importi.caricaImporti(iscritto, corso));

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
        feeAgosto.setText(iscritto.getImporti().getAgosto());

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
        iscritto.getImporti().setAgosto(feeAgosto.getText().toString());

        QueryImporti database = QueryImporti.getInstance(this);
        try {
            database.inserisciImporti(iscritto, corso);

        } catch (SQLiteConstraintException exception) {
            exception.printStackTrace();

            database.updateImporti(iscritto);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        salvaModifiche();
    }
}
