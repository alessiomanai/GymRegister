package com.alessiomanai.gymregister;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class InfoApp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_app);

        View root = findViewById(R.id.parentRelativeInfo);

        ViewCompat.setOnApplyWindowInsetsListener(root, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                v.setPadding(0, top, 0, 0);
                return insets;
            }
        });

        TextView version = findViewById(R.id.textVersionCode);

        version.append(BuildConfig.VERSION_NAME);
    }
}
