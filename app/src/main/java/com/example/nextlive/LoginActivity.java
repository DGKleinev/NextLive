package com.example.nextlive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.security.DomainCombiner;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
/*
* DBNextLive
*
*   - user
*   - eventi
*   - commenti
*
* */
    TextView registra, txtEmail, txtPassword;
    Button btnLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        registra = findViewById(R.id.textButton_registrazione);
        btnLogin = findViewById(R.id.button_login);
        txtEmail = findViewById(R.id.textLogin_email);
        txtPassword = findViewById(R.id.textLogin_password);

        btnLogin.setOnClickListener(this);
        registra.setOnClickListener(this);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textButton_registrazione:
                //il clickListener funziona, non funziona la chiamata alla activity
                Intent intent = new Intent(LoginActivity.this, RegistrazioneActivity.class);
                startActivity(intent);
                break;
            case R.id.button_login:
                loginUser();
                break;
        }
    }

    private void loginUser(){
        String email,pwd;
        email = txtEmail.getText().toString().trim();
        pwd = txtPassword.getText().toString().trim();
        //Le password devono contenere almeno 6 caratteri.
        //l'email deve avere un dominio ex: '@prova.com'
        if(TextUtils.isEmpty(email) && TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "compila i seguenti campi", Toast.LENGTH_LONG).show();
            return;
        }else{
            firebaseAuth.signInWithEmailAndPassword(email,pwd)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //questo non funziona
                                //Toast.makeText(this,"prova",Toast.LENGTH_LONG).show();
                                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra(MainActivity.userEmail, email);
                                intent.putExtra(MainActivity.userID, currentUser.getUid());
                                startActivity(intent);
                            } else {
                                    Toast.makeText(getApplicationContext(), "Email non presente nel db", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}