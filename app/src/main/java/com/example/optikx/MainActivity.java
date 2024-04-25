package com.example.optikx;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.content.PackageManagerCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Boolean cevaplarOlustumu= false;
    private Button changeActivityBTN;
    public int soru;
    private Button btnShowDialog;
    private ImageButton btnGalery;
    private ImageButton btnCamera;
    private Switch switchButton;
    private EditText soruSayisi;
    TextView uriLabel ;

    List<String> dogruCevaplar2;
    ScrollView containerScrollView;

    AlertDialog.Builder builder;

    ActivityResultLauncher<Uri> takePictureLauncher;
    ImageView image_view;
    Uri picUri;


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
                    //fonksiyona yönlendirme
                    changeActivity();

                    }else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Soru Sayısı ve Cevapları Belirle!" , Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        // ALERT DIALOG BUILDER
        builder = DialogHelper.alertBuilder(MainActivity.this);

        /* CEVAP AYARLAMA DIALOG -    */
        btnShowDialog= findViewById(R.id.buttonCevap);
        btnShowDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Button", "ClICKED!");

                if (switchButton.isChecked()){
                    soruAyarla(soru);
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
                }
            }
        });

        /*  IMAGE BUTTONS   */
        btnCamera = findViewById(R.id.cameraButton);
        btnGalery = findViewById(R.id.galleryButton);
        uriLabel = findViewById(R.id.picUriLabel);
        image_view =findViewById(R.id.imageView);

        picUri = createUri();
        registerPictureLauncher();

        btnCamera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Kamera Açıldı!", Snackbar.LENGTH_LONG)
                        .setAnchorView(R.id.cameraButton)
                        .setAction("Action", null).show();

                checkCameraPermissionAndOpenCamera();
            }
        });


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
    private void registerPictureLauncher() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean o) {
                        try {
                            image_view.setImageURI(null);
                            image_view.setImageURI(picUri);
                            uriLabel.setText(picUri.toString());
                            Log.d(" registerPictureLauncher ", " OKOKOKOKOK ActivityResultContracts");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
    private void checkCameraPermissionAndOpenCamera(){
        if(ActivityCompat.checkSelfPermission( MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CAMERA}, 1);
        }else {
            takePictureLauncher.launch(picUri);
        }
    }

    private Uri createUri(){
        File imageFile = new File(getApplicationContext().getFilesDir(),"camera_photo.jpg" );
        return FileProvider.getUriForFile( getApplicationContext(),"com.example.optikx.FileProvider" ,
                imageFile );
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

                // RadioGroup' ları Oluşturma
                RadioGroup optionsRadioGroup = new RadioGroup(this);
                optionsRadioGroup.setId( i );
                optionsRadioGroup.setLayoutParams(new RadioGroup.LayoutParams(
                        RadioGroup.LayoutParams.MATCH_PARENT,
                        RadioGroup.LayoutParams.WRAP_CONTENT
                ));
                optionsRadioGroup.setOrientation(LinearLayout.HORIZONTAL);



                // RadioButton' ları Oluşturma
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
            //Toast.makeText(this, "ScrollView not found", Toast.LENGTH_SHORT).show();
        }
    }

    // 'next' butonu ile aktiviteyi/sayafayı değiştirme ve gereklei parametreleri aktarma
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
                    // BOŞ BIRAKILMASI DURUMUNDA CEVABI 'E' OLARAK AYARLANIR
                    dogruCevapL.add('E');
                }
            } else Log.d("CEVAPLAR", "RADIO GROUP ID BULUNAMADI. ");


        }

        Log.d("CEVAPLAR", dogruCevapL.toString());

        return dogruCevapL;
    }

}

