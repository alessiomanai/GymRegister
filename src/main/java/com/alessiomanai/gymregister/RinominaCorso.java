package com.alessiomanai.gymregister;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alessiomanai.gymregister.classi.Corso;
import com.alessiomanai.gymregister.database.QueryCorso;

import java.util.ArrayList;

public class RinominaCorso extends Activity {

    private ArrayList<Corso> palestre = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rinomina_corso);

        QueryCorso database = (QueryCorso) QueryCorso.getInstance(this);
        palestre = database.getElencoCorsi();

        if (palestre.size() > 0) {    //se ci sono palestre in memoria mostra la lista di selezione

            //collego la listview dell'inferfaccia
            final ListView list1 = (ListView) this.findViewById(R.id.listViewRinomina);
            final ListatorePalestre adapter = new ListatorePalestre(RinominaCorso.this, palestre);
            list1.setAdapter(adapter);

            //rendo la lista clickabile
            list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        final int position, long id) {    //a seconda della posizione (rilevata automaticamente) apro una palestra

                    //finestra di conferma
                    AlertDialog.Builder builder = new AlertDialog.Builder(RinominaCorso.this);

                    builder.setTitle(R.string.rename);
                    builder.setMessage(R.string.isnp);

                    final EditText input = new EditText(RinominaCorso.this);
                    input.setText(palestre.get(position).getNome());
                    builder.setView(input);

                    builder.setCancelable(false);
                    builder.setPositiveButton(R.string.rename, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Confermato!

                            Editable value = input.getText();    //al click, prende in ingresso la stringa fornita

                            String nuovoNome = value.toString();

                            if (isEmpty(nuovoNome)) {

                                Toast.makeText(getApplicationContext(), R.string.noName, Toast.LENGTH_LONG).show();

                            } else {

                                QueryCorso database = (QueryCorso) QueryCorso.getInstance(getApplicationContext());
                                database.rinominaCorso(palestre.get(position), nuovoNome);

                                palestre.get(position).setNome(nuovoNome);
                                list1.setAdapter(adapter);

                                Toast.makeText(RinominaCorso.this, R.string.renamedone, Toast.LENGTH_SHORT).show();

                                dialog.dismiss();

                            }

                        }
                    });
                    //negativa
                    builder.setNegativeButton(R.string.annulla, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Annullato!
                            Toast.makeText(RinominaCorso.this, R.string.opannul, Toast.LENGTH_SHORT).show();

                            dialog.dismiss();
                        }
                    });

                    //visualizza la finestra
                    builder.show();

                }
            });        //fine lista clickabile

        } else {
            Toast.makeText(this, R.string.nopal, Toast.LENGTH_SHORT).show();
        }

    }



    /***
     * alla pressione del tasto back
     */
    @Override
    public void onBackPressed() {

        Intent asd = new Intent(getBaseContext(), Gestionepalestre.class);

        //avvia la finestra corrispondente
        startActivity(asd);

        finish();
    }    //fine tasto back


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private boolean isEmpty(String etText) {
        return etText.trim().length() == 0;
    }

}
