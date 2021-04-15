package com.example.nextlive;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import com.example.nextlive.model.EventoModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PubblicaEventoActivity extends AppCompatActivity implements View.OnClickListener, OnItemSelectedListener {
    public static final String USER_EMAIL = "utente_email";
    public static final String USER_ID = "utente_id";
    private static final int choose_image = 101;
    private static final String TAG = "";
    //questo codice serve per l'autocompletamento, per maggiori informazioni clickate sul seguente link:
    //serve un account business(pagamento)
    //https://www.youtube.com/watch?v=nDC-frkH_LA&ab_channel=GoogleMapsPlatform
    //private static final int AUTOCOMPLETE_REQUEST_CODE = 23487;

    Toolbar return_toolbar;
    //DatePicker
    private TextView pubblicaDataEvento;
    private DatePickerDialog.OnDateSetListener dataEventoSetListener;
    //Spinner
    private Spinner spinnerGenereMusicale;
    final String [] generi = {"Blues", "Country", "Disco", "Electro", "Folk",
        "Funk", "Hip-Hop", "House", "Jazz", "Pop", "Rythm and Blues", "Rap", "Reggae", "Rock"};
    private String genereText="";
    //IngaggioCantanteVisible
    private CheckBox ingaggio;
    private TextView cantanteText;
    private EditText pubblicaCantante, pubblicaNomeEvento, pubblicaInformazione;
    private EditText pubblicaIndirizzo;
    //Immagine
    private ImageView immagineEvento;
    private Uri uriEventImage;
    private String uriImage;
    //Data
    private DatePickerDialog dataEvento;
    private DatabaseReference database;
    private String stampoData;
    //Button
    private Button btnPubblicaEvento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pubblica_evento);

        /*-------------------HOOKS-------------------*/
        return_toolbar = findViewById(R.id.toolbar_return);
        ingaggio = findViewById(R.id.pubblica_singerVisibleChecked);
        cantanteText = findViewById(R.id.pubblica_visibleCantanteLabel);
        btnPubblicaEvento = findViewById(R.id.pubblica_buttonPubblica);
        spinnerGenereMusicale = findViewById(R.id.pubblica_genereMusicale); //lista genere musicale
        pubblicaDataEvento = findViewById(R.id.pubblica_eventDate);
        pubblicaNomeEvento = findViewById(R.id.pubblica_eventName);
        pubblicaIndirizzo = findViewById(R.id.pubblica_address);
        pubblicaInformazione = findViewById(R.id.pubblica_informazione);
        pubblicaCantante = findViewById(R.id.pubblica_visibleCantanteInput);
        immagineEvento = findViewById(R.id.pubblica_eventImage);

        /*----------------Tool Bar------------------*/
        setSupportActionBar(return_toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        /*--------------SPINNER-----------------*/
        ArrayList<String> listaGeneriMusicali = new ArrayList<>(Arrays.asList(generi));
        ArrayAdapter<String> listaAdapter = new ArrayAdapter<>(this,R.layout.spinner_genere_musicale_single_choice, R.id.genere, listaGeneriMusicali);
        spinnerGenereMusicale.setAdapter(listaAdapter);
        spinnerGenereMusicale.setOnItemSelectedListener(this);

        pubblicaDataEvento.setOnClickListener(this);
        btnPubblicaEvento.setOnClickListener(this);
        immagineEvento.setOnClickListener(this);

        //una volta selezionata la data, viene settato la textView
        dataEventoSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month += 1;
                stampoData = dayOfMonth+"/"+month+"/"+year;
                pubblicaDataEvento.setText(stampoData);
            }
        };

        //Ingaggio utente
        cantanteText.setVisibility(View.INVISIBLE);
        pubblicaCantante.setVisibility(View.INVISIBLE);
        ingaggio.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pubblica_buttonPubblica:
                verificaInput();
                break;

            case R.id.pubblica_eventImage:
                showImageChooser();
                break;

            case R.id.pubblica_singerVisibleChecked:
                ingaggioUtentePresente();
                break;

            case R.id.pubblica_eventDate:
                selezionaDataEvento();
                break;
        }
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
                immagineEvento.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Tornare indietro
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //Metodo che serve a caricare l'immagine selezionata nel DB
    private void uploadPicture(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Caricando Immagine...");
        pd.show();
        final String randomKey = UUID.randomUUID().toString();
        StorageReference eventImageRef = FirebaseStorage.getInstance().getReference("eventpics/"+ randomKey);
        eventImageRef.putFile(uriEventImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                //prendo come stringa il mio path di download, per poter caricare la mia immagine al momento del
                //caricamento della interfaccia home
                Task<Uri> getPathDownloadUri = taskSnapshot.getStorage().getDownloadUrl();
                Snackbar.make(findViewById(android.R.id.content), "Immagine caricata.", Snackbar.LENGTH_LONG).show();
                if(getPathDownloadUri.isSuccessful()){
                   uriImage = getPathDownloadUri.getResult().toString();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PubblicaEventoActivity.this, "errore al caricare l'immagine.", Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Percentuale: " + (int) progressPercent + "%");
            }
        });
    }

    private void selezionaDataEvento(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        dataEvento =  new DatePickerDialog(PubblicaEventoActivity.this,
                android.R.style.Theme_Holo_Dialog_MinWidth, dataEventoSetListener,
                year, month, day);
        dataEvento.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dataEvento.show();
    }

    //Se il locale vuole si è già accordato con un cantante precedentemente può spuntare la checkbox
    //per poter inserire l'email del cantante
    private void ingaggioUtentePresente(){
        if (ingaggio.isChecked()) {
            pubblicaCantante.setVisibility(View.VISIBLE);
            cantanteText.setVisibility(View.VISIBLE);
        }else{
            cantanteText.setVisibility(View.INVISIBLE);
            pubblicaCantante.setVisibility(View.INVISIBLE);
        }
    }


    //VerificaInput: Esegue una verifica dei campi che devono essere obbligatoriamente compilati prima della pubblicazione di un evento
    //il come viene verificata è ancora da definire, se qualcuno di voi lo fa è pregato di descrivere il procedimento
    //Campo ImageView: non verrà considerato un campo obbligatorio, se il locale non carica un'immagine, verrà caricata quella di default
    private void verificaInput(){
        String evento, informazione, indirizzo, data;
        evento = pubblicaNomeEvento.getText().toString();
        informazione = pubblicaInformazione.getText().toString();
        indirizzo = pubblicaIndirizzo.getText().toString();

        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        try {
            int day = dataEvento.getDatePicker().getDayOfMonth();
            int month = dataEvento.getDatePicker().getMonth();
            int year = dataEvento.getDatePicker().getYear();
            cal.set(year, month, day);
        }catch (Exception e){
            Toast.makeText(this, "devi inserire una data, deve essere maggiore di quella corrente", Toast.LENGTH_LONG).show();
            return;
        }
        Date eventoData = cal.getTime();
        data = eventoData.toString();

        //Verifica se i campi sono vuoti (tranne ImmagineEvento e Genere e Data)
        if(TextUtils.isEmpty(evento) && TextUtils.isEmpty(informazione) && TextUtils.isEmpty(indirizzo)){
            Toast.makeText(this, "uno o più campi non sono stati compilati, pubblicazione non effettuata", Toast.LENGTH_LONG).show();
            return;
        }

        //verifica se la data di pubblicazione è maggiore della data corrente
        //compareTo(): return positive value if equals else return negative value
        if(currentDate.after(eventoData)){
            Toast.makeText(this, "la Data inserita deve essere maggiore della data odierna", Toast.LENGTH_LONG).show();
            return;
        }

        //Verifica Indirizzo: verifica se esiste l'indirizzo, se non esiste, non avviene la pubblicazione
        if(!geoLocate()) {
            Toast.makeText(this, "l'indirizzo inserito non esiste", Toast.LENGTH_LONG).show();
            return;
        }

        saveUserInformation(evento, informazione, indirizzo);
        Intent intent = new Intent(PubblicaEventoActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.USER_EMAIL, getIntent().getStringExtra("utente_email"));
        intent.putExtra(MainActivity.USER_ID, getIntent().getStringExtra("utente_id"));
        Toast.makeText(this, " Pubblicazione Effettuata", Toast.LENGTH_LONG).show();
       // startActivity(intent);
    }

    //metodo che verifica se esiste (come indirizzo) la stringa inserita nel campo 'indirizzo'
    private boolean geoLocate(){
        String cercaInd = pubblicaIndirizzo.getText().toString();
        Geocoder geo = new Geocoder(PubblicaEventoActivity.this);
        List<Address> indirizzi = new ArrayList<>();
        System.out.println(indirizzi);
        try{
            indirizzi = geo.getFromLocationName(cercaInd, 1);
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

    //Metodo Implementati da OnItemSelectedListener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        genereText = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) { }//Metodo Implementati da OnItemSelectedListener

    //Metodo che salva nel database l'oggetto EventoModel
    private void saveUserInformation(String titoloEvento, String descrizione, String indirizzo){
        uploadPicture();

       EventoModel evento = new EventoModel(getIntent().getStringExtra("utente_id"), titoloEvento, descrizione, indirizzo, stampoData, genereText, uriImage);
        database = FirebaseDatabase.getInstance().getReference().child("eventi");
        database.push().setValue(evento);

        Toast.makeText(PubblicaEventoActivity.this, "Dati inseriti", Toast.LENGTH_LONG).show();
/*
Uri uri;
String stringUri;
uri = Uri.parse(stringUri);
*/
    }
}