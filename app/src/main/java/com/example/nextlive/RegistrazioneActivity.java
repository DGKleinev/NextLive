package com.example.nextlive;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrazioneActivity extends AppCompatActivity {
    private DatabaseReference database;
    private RadioGroup radioGenereGroup;
    private RadioButton radioGenereButton;
    private Button registrazioneButton;
    private TextInputEditText txtNome, txtCognome, txtEmail, txtPassword, txtUsername, txtConferma_pwd;
    //http://wiki.c2.com/?GlobalVariablesAreBad
    //inserire immagini:
    //https://www.youtube.com/watch?v=qVaBY92sOSI&ab_channel=SimplifiedCoding
    private boolean existEmail = false;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        radioGenereGroup = findViewById(R.id.radioGenere);
        registrazioneButton = findViewById(R.id.registra);
        txtNome = findViewById(R.id.insert_nome);
        txtCognome = findViewById(R.id.insert_cognome);
        txtEmail = findViewById(R.id.insert_email);
        txtPassword = findViewById(R.id.insert_password);
        txtUsername = findViewById(R.id.insert_nickname);
        txtConferma_pwd = findViewById(R.id.conferma_password);

        registrazioneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGenereGroup.getCheckedRadioButtonId();
                radioGenereButton = findViewById(selectedId);
                //registrazioneUtente();
                registraUser();
            }
        });
    }

    public boolean verificaCampi(String nome, String cognome, String username, RadioGroup genere, String email, String password, String conferma_pwd){
        if ((!TextUtils.isEmpty(nome)) && (!TextUtils.isEmpty(cognome)) && (!TextUtils.isEmpty(email))
                && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(username))
                && (genere.getCheckedRadioButtonId() != -1) && (!TextUtils.isEmpty(conferma_pwd))) {
                    return true;
        }else {
            Toast.makeText(this, "Devi compilare tutti i campi per completare la registrazione", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void registraUser(){
        String nome, cognome, email,genere,password,username, conferma_pwd;
        nome = txtNome.getText().toString();
        cognome = txtCognome.getText().toString();
        email = txtEmail.getText().toString().trim();
        genere = radioGenereButton.getText().toString();
        password = txtPassword.getText().toString();
        username = txtUsername.getText().toString();
        conferma_pwd = txtConferma_pwd.getText().toString();

        //Le password devono contenere almeno 6 caratteri.
        //l'email deve avere un dominio ex: '@prova.com'
        if(verificaCampi(nome,cognome,username,radioGenereGroup,email,password, conferma_pwd)){
            if(password.equalsIgnoreCase(conferma_pwd)){
                firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //Toast.makeText(this,"prova",Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), "Reg  istrazione effettuata", Toast.LENGTH_LONG).show();
                                    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                                    if(currentUser != null){
                                        String id = currentUser.getUid();
                                        database.child(id).push();
                                        User2 nUser = new User2(nome, cognome, genere, username);
                                        database.child("user").child(id).setValue(nUser);
                                    }
                                } else {
                                    if(task.getException() instanceof FirebaseAuthUserCollisionException) //se è presente l'email nel db
                                        Toast.makeText(getApplicationContext(), "Email presente nel db", Toast.LENGTH_LONG).show();

                                    Toast.makeText(getApplicationContext(), "Problemi durante la registrazione, per favore riprova", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        }
    }
}