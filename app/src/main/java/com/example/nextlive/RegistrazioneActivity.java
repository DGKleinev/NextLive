package com.example.nextlive;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrazioneActivity extends AppCompatActivity {
    private DatabaseReference Utente;
    private RadioGroup radioGenereGroup;
    private RadioButton radioGenereButton;
    private Button registrazioneButton;
    private TextInputEditText txtNome, txtCognome, txtEmail, txtPassword, txtUsername, txtConferma_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);

        Utente = FirebaseDatabase.getInstance().getReference();

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
                registrazioneUtente();
            }
        });
    }

    public void registrazioneUtente(){
        String nome, cognome, email, password, username, conferma_pwd;
        nome = txtNome.getText().toString();
        cognome = txtCognome.getText().toString();
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        username = txtUsername.getText().toString();
        conferma_pwd = txtConferma_pwd.getText().toString();
        if((!TextUtils.isEmpty(nome)) && (!TextUtils.isEmpty(cognome)) && (!TextUtils.isEmpty(email))
          && (!TextUtils.isEmpty(password)) && (!TextUtils.isEmpty(conferma_pwd))
          && (!TextUtils.isEmpty(username)) &&(radioGenereGroup.getCheckedRadioButtonId() != -1)){

            if(password.equals(conferma_pwd)) {
                String id = Utente.push().getKey();
                User nUser = new User(id, nome, cognome, username, email, password);
                Utente.child("user").child(id).setValue(nUser);
                Toast.makeText(this, "Registrazione effettuata", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "le password non coincidono", Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Devi compilare tutti i campi per completare la registrazione", Toast.LENGTH_LONG).show();
        }

    }
}