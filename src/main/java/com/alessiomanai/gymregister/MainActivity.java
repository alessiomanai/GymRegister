package com.alessiomanai.gymregister;

/**
 * Gym Register
 *
 * Copyright (C) 2014-2020  Alessio Manai
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * alessiomanai.ml      alessio@manai.tk
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


public class MainActivity extends Activity {

    ImageView icona;
    Intent palestre;
    CountDownTimer time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ///permette di avviare la finestra in fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        icona = (ImageView) findViewById(R.id.gymregister);

        //dopo tre secondi chiude la schermata
        time = new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {


            }

            @Override
            public void onFinish() {

                palestre = new Intent(getBaseContext(), Gestionepalestre.class);
                //avvia la finestra corrispondente
                startActivity(palestre);
                finish();    //termina la activity

            }

        }.start();

    }

}
