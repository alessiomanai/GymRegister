package com.alessiomanai.gymregister;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alessiomanai.gymregister.utils.activity.GymRegisterBaseActivity;

public class InfoGDPRActivity extends GymRegisterBaseActivity {

    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_gdpractivity);

        View root = findViewById(R.id.gdprLayout);

        ViewCompat.setOnApplyWindowInsetsListener(root, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                v.setPadding(0, top, 0, 0);
                return insets;
            }
        });

        confirm = findViewById(R.id.confirmGDPR);

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("GDPR", Context.MODE_PRIVATE);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("consenso", true);
                editor.apply();

                Intent intent = new Intent(getBaseContext(), Gestionepalestre.class);
                startActivity(intent);
                finish();
            }
        });
    }
}