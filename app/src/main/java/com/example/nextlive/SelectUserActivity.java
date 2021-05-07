package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.nextlive.signup.RegistrazioneLocaleActivity;
import com.example.nextlive.signup.RegistrazioneUserActivity;

public class SelectUserActivity extends AppCompatActivity {
    private CardView regLocale;
    private CardView regUtente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        regUtente = findViewById(R.id.reguser);
        regLocale = findViewById(R.id.reglocale);

        regUtente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this, RegistrazioneUserActivity.class);
                startActivity(intent);
            }
        });

        regLocale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this, RegistrazioneLocaleActivity.class);
                startActivity(intent);
            }
        });
    }
}