package com.example.nextlive.signup;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextlive.LoginActivity;
import com.example.nextlive.R;
import com.example.nextlive.model.UserFanModel;
import com.example.nextlive.model.UserSingerModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class RegistrazioneUserActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    private TextView tipoUtente;
    private Button registra;
    private String nome;
    private String cognome;
    private String username;
    private String email;
    private String password;
    private String confirmPassword;
    private String dataNascita;
    private String tipo;
    private DatePickerDialog dataNascitaDate;
    private DatePickerDialog.OnDateSetListener dataSetListener;
    private ImageView imageUser;
    private static final int choose_image = 101;
    private Uri uriEventImage;
    private String urlImage = "";
    private TextInputEditText nomeUtente;
    private TextInputEditText cognomeUtente;
    private TextInputEditText usernameUtente;
    private TextInputEditText emailUtente;
    private TextInputEditText passwordUtente;
    private TextInputEditText passwordUtente2;
    private TextView dataNascitaUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_user);

        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        nomeUtente = findViewById(R.id.reguser_nome);
        cognomeUtente = findViewById(R.id.reguser_cognome);
        usernameUtente = findViewById(R.id.reguser_username);
        emailUtente = findViewById(R.id.reguser_email);
        passwordUtente = findViewById(R.id.reguser_password);
        passwordUtente2 = findViewById(R.id.reguser_password2);
        dataNascitaUtente = findViewById(R.id.reguser_dataNascita);
        tipoUtente = findViewById(R.id.reguser_seleziona_tipo);
        imageUser = findViewById(R.id.reguser_userImage);
        registra = findViewById(R.id.reguser_btn_registrati);

        registra.setOnClickListener(this);
        imageUser.setOnClickListener(this);
        tipoUtente.setOnClickListener(this);
        dataNascitaUtente.setOnClickListener(this);

        dataSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                dataNascita = dayOfMonth+"/"+month+"/"+year;
                dataNascitaUtente.setText(dataNascita);
            }
        };
    }

    private void verificaUserMaggiore(){
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int year = cal.get(java.util.Calendar.YEAR);
        int month = cal.get(java.util.Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dataNascitaDate =  new DatePickerDialog(RegistrazioneUserActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth, dataSetListener,
                year, month, day);
        dataNascitaDate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dataNascitaDate.show();
    }

    public void verificaCampi(){
        nome = nomeUtente.getText().toString();
        cognome = cognomeUtente.getText().toString();
        username = usernameUtente.getText().toString();
        email = emailUtente.getText().toString();
        password = passwordUtente.getText().toString();
        confirmPassword = passwordUtente2.getText().toString();
        dataNascita = dataNascitaUtente.getText().toString();
        tipo = tipoUtente.getText().toString();
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH);
        int currentYear = cal.get(Calendar.YEAR);
        int dayUser = 0;
        int monthUser = 0;
        int yearUser = 0;

        //Verifica se le password coincidono, se non coincidono la registrazione non viene considerata
        if(!(password.equals(confirmPassword))){
            Toast.makeText(this, "Le password non coincidono, la preghiamo di ritentare", Toast.LENGTH_LONG).show();
            return;
        }

        //Verifica se l'utente ha più di 18 anni
        try {
            dayUser = dataNascitaDate.getDatePicker().getDayOfMonth();
            monthUser = dataNascitaDate.getDatePicker().getMonth();
            yearUser = dataNascitaDate.getDatePicker().getYear();
            cal.set(yearUser, monthUser, dayUser);
        }catch (Exception e){
            Toast.makeText(this, "devi inserire una data e deve essere maggiore di quella corrente", Toast.LENGTH_LONG).show();
            return;
        }
        if(currentYear - yearUser < 18){
            Toast.makeText(this, "Per registrarti in questa applicazione devi avere almeno 18 anni", Toast.LENGTH_LONG).show();
            return;
        }else if(currentYear - yearUser == 18){
            if(monthUser - currentMonth > 0){
                Toast.makeText(this, "Per registrarti in questa applicazione devi avere almeno 18 anni", Toast.LENGTH_LONG).show();
                return;
            }else if(monthUser - currentMonth == 0){
                if(dayUser - currentDay > 0) {
                    Toast.makeText(this, "Per registrarti in questa applicazione devi avere almeno 18 anni", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

        //Verifica se i campi sono vuoti (tranne userImage, tipoUtente e DataNascita)
        if(TextUtils.isEmpty(nome) && TextUtils.isEmpty(cognome) &&
                TextUtils.isEmpty(username) && TextUtils.isEmpty(email) &&
                TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(this, "uno o più campi non sono stati compilati, registrazione non effettuata", Toast.LENGTH_LONG).show();
            return;
        }

        uploadPicture();

        registraUtente();
    }

    //Metodi che servono a caricare l'immagine e ad avere un'antemprima nell'applicazione
    //Questo metodo seleziona una immagine
    private void showImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Seleziona una immagine per l'evento"), choose_image);
    }

    //Quest'altro, invece, fa vedere l'immagine selezionata in una imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == choose_image && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriEventImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriEventImage);
                imageUser.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registraUtente(){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registrazione effettuata", Toast.LENGTH_LONG).show();
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if(currentUser != null){
                                String id = currentUser.getUid();
                                database.child(id).push();
                                //considero il tipo di utente: se è uguale a 'si' allora l'utente registrato è un cantante, altrimenti un utente normale
                                if(tipo.equals("si")){
                                    if(TextUtils.isEmpty(urlImage)){
                                        UserSingerModel user = new UserSingerModel(nome,"cantante",dataNascita,cognome,username);
                                        database.child("user").child(id).setValue(user);
                                        Intent intent = new Intent(RegistrazioneUserActivity.this, LoginActivity.class);
                                        Toast.makeText(RegistrazioneUserActivity.this, " Registrazione Effettuata", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }else{
                                        UserSingerModel user = new UserSingerModel(nome,urlImage,"cantante",dataNascita,cognome,username);
                                        database.child("user").child(id).setValue(user);
                                        Intent intent = new Intent(RegistrazioneUserActivity.this, LoginActivity.class);
                                        Toast.makeText(RegistrazioneUserActivity.this, " Registrazione Effettuata", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                }else{
                                    if(TextUtils.isEmpty(urlImage)){
                                        UserFanModel user = new UserFanModel(nome,"fan",dataNascita,cognome,username);
                                        database.child("user").child(id).setValue(user);
                                        Intent intent = new Intent(RegistrazioneUserActivity.this, LoginActivity.class);
                                        Toast.makeText(RegistrazioneUserActivity.this, " Registrazione Effettuata", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }else{
                                        UserFanModel user = new UserFanModel(nome,urlImage,"fan",dataNascita,cognome,username);
                                        database.child("user").child(id).setValue(user);
                                        Intent intent = new Intent(RegistrazioneUserActivity.this, LoginActivity.class);
                                        Toast.makeText(RegistrazioneUserActivity.this, " Registrazione Effettuata", Toast.LENGTH_LONG).show();
                                        startActivity(intent);
                                    }
                                }
                            }
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException) //se è presente l'email nel db
                                Toast.makeText(getApplicationContext(), "Email presente nel db", Toast.LENGTH_LONG).show();
                            Toast.makeText(getApplicationContext(), "Problemi durante la registrazione, per favore riprova", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.reguser_dataNascita:
                verificaUserMaggiore();
                break;
            case R.id.reguser_seleziona_tipo:
                setTipo();
                break;
            case R.id.reguser_userImage:
                showImageChooser();
                break;
            case R.id.reguser_btn_registrati:
                verificaCampi();
                break;
        }
    }

    //Metodo che serve a caricare l'immagine selezionata nel DB
    private void uploadPicture() {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Caricando Immagine...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        urlImage = randomKey;
        StorageReference eventImageRef = FirebaseStorage.getInstance().getReference("userImages/"+ randomKey);

        eventImageRef.putFile(uriEventImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Immagine caricata.", Snackbar.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistrazioneUserActivity.this, "errore al caricare l'immagine.", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentuale: " + (int) progressPercent + "%");
            }
        });
    }

    private void setTipo(){
        String utenteTipo = tipoUtente.getText().toString();
        if(utenteTipo.equalsIgnoreCase("no")){
            tipoUtente.setText("Si");
            tipo = "si";
        }else{
            tipoUtente.setText("No");
            tipo = "no";
        }
    }
}