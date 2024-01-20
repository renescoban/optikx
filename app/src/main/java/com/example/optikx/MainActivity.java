package com.example.optikx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Boolean cevaplarOlustumu= false;
    private Button changeActivityBTN;
    public int soru;
    private Button btnShowDialog;
    private ImageButton btnGalery;
    private ImageButton btnImage;
    private Switch switchButton;
    private EditText soruSayisi;

    List<String> dogruCevaplar2;
    ScrollView containerScrollView;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //CHANGE ACTIVITY - SAYFA DEGİSTİRME
        changeActivityBTN = findViewById(R.id.button_first);
        changeActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (  soru > 0 && soru <= 40 && cevaplarOlustumu){
                    changeActivity();

                    }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Soru Sayısı ve Cevapları Belirle!" , Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        // ALERT BUILDER
        builder = DialogHelper.alertBuilder(MainActivity.this);





        /* CEVAP AYARLAMA DIALOG -    */
        btnShowDialog= findViewById(R.id.buttonCevap);
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "ClICKED!");

                if (switchButton.isChecked()){
                    soruAyarla(soru);
                    //showDialog(view);
                    cevaplarOlustumu = true;

                    Log.d("Button", "Soru Ayarlandı!");
                }else{
                    builder.setMessage("Lütfen Soru Sayısını Kilitleyiniz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Kilitlenmedi!");
                    alert.show();
                    //Toast toast = Toast.makeText(getApplicationContext(), "Soru Sayısını Kilitle!" , Toast.LENGTH_LONG);
                    //toast.show();
                }

            }
        });



        /*  IMAGE BUTTONS   */
        btnImage = findViewById(R.id.imageButton);
        btnImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("clicked!");

                Snackbar.make(view, "Kamera Açıldı!", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.imageButton)
                        .setAction("Action", null).show();
            }
        });

        btnGalery = findViewById(R.id.galleryButton);
        btnGalery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println("clicked!");
                Snackbar.make(view, "Galeri Açıldı!", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.galleryButton)
                        .setAction("Action", null).show();
            }
        });

        /*  SORU SAYISI ALMA - SWITCH    */
        switchButton = findViewById(R.id.soruKilit);
        soruSayisi = findViewById(R.id.soruSayi);

        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // girdiyi int' e çevir
                String inputText = soruSayisi.getText().toString().trim();
                int value;
                try {
                    value = Integer.parseInt(inputText);
                } catch (NumberFormatException e) {
                    //input int değilse
                    value = 0; // Soru Sayısı DEF değer
                }

                // Switch 'true' Olma Durumu Ayarlama
                if ( value > 0 && value <= 40 ) {
                    soruSayisi.setEnabled(!isChecked);
                    soru = value;
                    Log.d("Switch","Soru Sayısı: " +  Integer.toString(soru));
                }else {// 0 İLA 40 ARASI DEĞİLSE ALERT
                    switchButton.setChecked(false);

                    builder.setMessage("Lütfen o ila 40 arasında bir sayı giriniz.")
                            .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.setTitle("Yanlış Girdi");
                    alert.show();

                }





            }
        });



    }











    private void showDialog(View view){
        if (switchButton.isChecked()) {
            System.out.println("dialog run!!");
            Dialog dialog = new Dialog(this);
            //dialog.setContentView(R.layout.custom_dialog);
            soruAyarla(soru);
            dialog.show();
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Soru Sayısını Kilitle!" , Toast.LENGTH_LONG);
            toast.show();
        }

    }

    private void soruAyarla(int soru){
        String[] options = {"A", "B", "C", "D", "E"};
        Log.d("Button", "Soru Ayarlanma Başladı!");

        containerScrollView = findViewById(R.id.radiosLayout ) ;
        LinearLayout containerLinearLayout = findViewById(R.id.radiosLLayout);


        if (containerLinearLayout != null){

            for (int i = 1; i <= soru; i++){
                Log.d("Button", "Soru Üretme Başladı!");

                TextView labelTextView = new TextView(this);
                labelTextView.setText(i + ": ");
                labelTextView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                containerLinearLayout.addView(labelTextView);

                // Create a new RadioGroup
                RadioGroup optionsRadioGroup = new RadioGroup(this);
                optionsRadioGroup.setId( i );
                optionsRadioGroup.setLayoutParams(new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                ));
                optionsRadioGroup.setOrientation(LinearLayout.HORIZONTAL);



                // Create RadioButtons
                for (int j = 1; j <= 5; j++) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText( options[j-1] );
                    radioButton.setId( (100 + j+ (10*i) ) );
                    if(j == 1) radioButton.setSelected(true);

                    optionsRadioGroup.addView(radioButton);

                }
                Log.d("Button", "Soru Group Üretme Bitti!");
                containerLinearLayout.addView(optionsRadioGroup);
            }





        } else {
            // Log an error or display a message if the ScrollView is not found
            Log.e("YourActivity", "ScrollView not found with ID R.id.radiosLayout");
            // or Toast.makeText(this, "ScrollView not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeActivity(){
        Intent intent = new Intent(this, MainActivity2.class);
        int toplamSoru = soru;

        List<Character> dogruCevaplar = new ArrayList<>();
        dogruCevaplar = cevaplariAl(dogruCevaplar);
        Log.d("ACTIVITY CHANGE", dogruCevaplar.toString());
        char[] charArray = new char[dogruCevaplar.size()];
        for (int i = 0; i < dogruCevaplar.size(); i++) {
            charArray[i] = dogruCevaplar.get(i);
        }

        intent.putExtra("Aktarilan_Cevaplar", charArray);
        intent.putExtra("Aktarilan_Soru", toplamSoru );
        Log.d("Aktarılan_Soru", Integer.toString(toplamSoru) );
        startActivity(intent);
    }

    List<Character> cevaplariAl( List<Character> dogruCevapL){

        for (int i = 1; i <= soru; i++) {
            RadioGroup radioGroup = findViewById(i);

            // Check if the RadioGroup is found
            if (radioGroup != null) {
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();

                // Add the checked RadioButton's ID to the list
                if (checkedRadioButtonId != -1) {

                    RadioButton checkedRadioButton = findViewById(checkedRadioButtonId);
                    if (checkedRadioButton != null) {
                        Log.d("CEVAPLAR", "checkedRadioButton.getText() ");
                        dogruCevapL.add(checkedRadioButton.getText().charAt(0));
                    }
                }else {
                    // BOŞ BIRAKILMASI DURUMUNDA E OLARAK AYARLANIR
                    dogruCevapL.add('E');
                }
            } else Log.d("CEVAPLAR", "RADIO GROUP ID BULUNAMADI. ");


        }

        Log.d("CEVAPLAR", dogruCevapL.toString());

        return dogruCevapL;
    }

}

