package com.example.nextlive.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nextlive.LoginActivity;
import com.example.nextlive.R;
import com.example.nextlive.model.UserLocalModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegistrazioneLocaleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "";
    private DatabaseReference database;
    private FirebaseAuth firebaseAuth;
    private Button registra;
    private String locale;
    private String proprietario;
    private String indirizzo;
    private String email;
    private String password;
    private String confirmPassword;
    private static final String tipo = "locale";
    private ImageView imageLocale;
    private static final int choose_image = 101;
    private Uri uriEventImage;
    private String urlImage = "";
    private TextInputEditText nomeLocale;
    private TextInputEditText nomeProprietario;
    private TextInputEditText indirizzoLocale;
    private TextInputEditText emailLocale;
    private TextInputEditText passwordLocale;
    private TextInputEditText passwordLocale2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_locale);

        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        nomeLocale = findViewById(R.id.reglocale_nome_locale);
        nomeProprietario = findViewById(R.id.reglocale_nome_proprietario);
        indirizzoLocale = findViewById(R.id.reglocale_indirizzo);
        emailLocale = findViewById(R.id.reglocale_email);
        passwordLocale = findViewById(R.id.reglocale_password);
        passwordLocale2 = findViewById(R.id.reglocale_password2);
        imageLocale = findViewById(R.id.reglocale_image);
        registra = findViewById(R.id.reglocale_btn_registrati);

        registra.setOnClickListener(this);
        imageLocale.setOnClickListener(this);
    }



    public void verificaCampi(){
        //Verifica se le password coincidono, se non coincidono la registrazione non viene considerata
        locale = nomeLocale.getText().toString();
        proprietario = nomeProprietario.getText().toString();
        indirizzo = indirizzoLocale.getText().toString();
        email = emailLocale.getText().toString();
        password = passwordLocale.getText().toString();
        confirmPassword = passwordLocale2.getText().toString();

        //Verifica se i campi sono vuoti (tranne userImage)
        if(TextUtils.isEmpty(locale) && TextUtils.isEmpty(proprietario) && TextUtils.isEmpty(email) &&
                TextUtils.isEmpty(password) && TextUtils.isEmpty(confirmPassword) && TextUtils.isEmpty(indirizzo)){
            Toast.makeText(this, "uno o più campi non sono stati compilati, registrazione non effettuata", Toast.LENGTH_LONG).show();
            return;
        }

        if(!(password.equals(confirmPassword))){
            Toast.makeText(this, "Le password non coincidono, la preghiamo di ritentare", Toast.LENGTH_LONG).show();
            return;
        }

        //Verifica Indirizzo: verifica se esiste l'indirizzo, se non esiste, non avviene la pubblicazione
        if(!geoLocate()) {
            Toast.makeText(this, "l'indirizzo inserito non esiste", Toast.LENGTH_LONG).show();
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
                imageLocale.setImageBitmap(bitmap);
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
                            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                            if(currentUser != null){
                                String id = currentUser.getUid();
                                database.child(id).push();
                                //considero il tipo di utente: se è uguale a 'si' allora l'utente registrato è un cantante, altrimenti un utente normale

                                if(TextUtils.isEmpty(uriEventImage.toString())){
                                    UserLocalModel local = new UserLocalModel(locale, proprietario, "locale", indirizzo);
                                    database.child("user").child(id).setValue(local);
                                    Intent intent = new Intent(RegistrazioneLocaleActivity.this, LoginActivity.class);
                                    Toast.makeText(RegistrazioneLocaleActivity.this, "Registrazione Effettuata", Toast.LENGTH_LONG).show();
                                    startActivity(intent);
                                }else{
                                    UserLocalModel local = new UserLocalModel(locale, proprietario, urlImage, "locale", indirizzo);
                                    database.child("user").child(id).setValue(local);
                                    Intent intent = new Intent(RegistrazioneLocaleActivity.this, LoginActivity.class);
                                    Toast.makeText(RegistrazioneLocaleActivity.this, "Registrazione Effettuata", Toast.LENGTH_LONG).show();
                                    startActivity(intent);
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
            case R.id.reglocale_image:
                showImageChooser();
                break;
            case R.id.reglocale_btn_registrati:
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
                Toast.makeText(RegistrazioneLocaleActivity.this, "errore al caricare l'immagine.", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentuale: " + (int) progressPercent + "%");
            }
        });
    }

    //metodo che verifica se esiste (come indirizzo) la stringa inserita nel campo 'indirizzo'
    private boolean geoLocate(){
        Geocoder geo = new Geocoder(RegistrazioneLocaleActivity.this);
        List<Address> indirizzi = new ArrayList<>();
        try{
            indirizzi = geo.getFromLocationName(indirizzo, 1);
        }catch (IOException e){
            Log.e(TAG, "geolocate: error " + e.getMessage());
        }

        if(indirizzi.size() > 0){
            Address address = indirizzi.get(0);
            Log.d(TAG, "geolocate: " + address.toString());
            return true;
        }
        return false;
    }
}