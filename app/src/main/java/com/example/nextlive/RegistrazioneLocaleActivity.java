package com.example.nextlive;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.nextlive.fragment.MultipleChoiceDialogFragment;

import java.util.ArrayList;

public class RegistrazioneLocaleActivity extends AppCompatActivity implements MultipleChoiceDialogFragment.onMultiChoiceListener{

    private TextView tvselectedChoices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_locale);
        tvselectedChoices=findViewById(R.id.tvSelectedChoices);
        Button btnselectdChoices=findViewById(R.id.btnseletedChoices);
        btnselectdChoices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment multiChoiceDialog= new MultipleChoiceDialogFragment();
                multiChoiceDialog.setCancelable(false);
                multiChoiceDialog.show(getSupportFragmentManager(), "dialogo multichoice");
            }
        });
    }

    @Override
    public void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList) {
        StringBuilder stringBuilder= new StringBuilder();
        stringBuilder.append("seleziona choices");
        for (String str : selectedItemList) {
            stringBuilder.append(str+"");
        }
        tvselectedChoices.setText(stringBuilder);
    }

    @Override
    public void onNegatveButtonClicked() {
        tvselectedChoices.setText("dialogo cancellato");
    }
}