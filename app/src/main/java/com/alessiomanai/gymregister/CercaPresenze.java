package com.alessiomanai.gymregister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alessiomanai.gymregister.classi.Presenza;

import java.util.ArrayList;

public class CercaPresenze extends Activity {

    ArrayList<Presenza> risultati = new ArrayList<>();
    EditText search;
    ImageButton cerca;
    private ArrayList<Presenza> elencoPresenze;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerca_presenze);

        View root = findViewById(R.id.linearLayoutCercaPresenze);

        ViewCompat.setOnApplyWindowInsetsListener(root, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                v.setPadding(0, top, 0, 0);
                return insets;
            }
        });

        search = findViewById(R.id.search2);
        cerca = findViewById(R.id.buttoncerca2);

        cerca.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View arg0) {

                String chiave;

                chiave = search.getText().toString();

            }
        });

    }
}
