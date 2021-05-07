package com.example.nextlive;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.nextlive.model.UserParentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SfondoActivity extends AppCompatActivity {

    public static final String USER_EMAIL = "utente_email";
    public static final String USER_ID = "utente_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sfondo);

        DatabaseReference userDatabase;
        userDatabase = FirebaseDatabase.getInstance().getReference().child("user").child(getIntent().getStringExtra("utente_id"));

        //In questa collection sono presenti tutti gli utenti e le rispettive informazioni
        userDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserParentModel user = snapshot.getValue(UserParentModel.class);
                Intent intent = new Intent(SfondoActivity.this, HomeActivity.class);
                intent.putExtra(HomeActivity.USER_EMAIL, getIntent().getStringExtra("utente_email"));
                intent.putExtra(HomeActivity.USER_ID, getIntent().getStringExtra("utente_id"));
                intent.putExtra(HomeActivity.USER_TYPE, user.getTipo());
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }
}