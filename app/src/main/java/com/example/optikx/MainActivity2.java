package com.example.optikx;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity2 extends AppCompatActivity {
    private Button changeBackActivityBTN;
    private Button resetActivityBTN;
    private Button rndCevap_btn;
    private TextView dogruSayisi_txt;
    private TextView yanlisSayisi_txt;
    private TextView bosSayisi_txt;
    private TextView puanDurumu_txt;

    static  int dogruSayisi_int = 0;
    static  int yanlisSayisi_int = 0;
    static  int bosSayisi_int = 0;
    static  int puanDurumu_int = 0;
    static  int puan_carpani = 5;




    MainActivity mainActivity = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        int toplam_soru = getIntent().getIntExtra("Aktarilan_Soru",0);
        char[] charArray = getIntent().getCharArrayExtra("Aktarilan_Cevaplar");
        List<Character> dogruCevapCharacListesi =new ArrayList<>();
        for (char c : charArray) {
            dogruCevapCharacListesi.add(c);
        }
        Log.d("Aktarilan_Cevaplar", charArray.toString());
        Log.d("Aktarilan_Cevaplar", dogruCevapCharacListesi.toString());

        // PREVIOUS BUTTON
        changeBackActivityBTN = findViewById(R.id.button_second);
        changeBackActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        // RESET BUTTON
        resetActivityBTN = findViewById(R.id.btnReset);
        resetActivityBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("RESETLENDİ!");

            }
        });

        // CEVAP BUTTON

        ListView rndlisteLayout= (ListView) findViewById(R.id.rndCevapList);
        ListView dgrlisteLayout= (ListView) findViewById(R.id.dgrCevapList);

        rndCevap_btn = findViewById(R.id.rndCevap_btn);
        rndCevap_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> dogruCevaplar = new ArrayList<>();
                List<String> uretilenCevaplar = new ArrayList<>();
                dogruCevaplar= listeyeCevir(dogruCevapCharacListesi);
                uretilenCevaplar= listeyeCevir( cevap_uret(toplam_soru) );


                ListeAyarla(dgrlisteLayout, dogruCevaplar );
                ListeAyarla(rndlisteLayout, uretilenCevaplar );

                Log.d("TAG", cevap_uret(toplam_soru).toString() );
                cevap_uret(toplam_soru);
                Log.d("onClick: ","rastgele cevap üretildi" );


                compareLists( dogruCevaplar , uretilenCevaplar );
                setPuan();

            }
        });




    }


    // FUNCTIONS
    void showToast(String str){
        String toastV = str;
        Toast toast = Toast.makeText(getApplicationContext(), toastV , Toast.LENGTH_LONG);
        toast.show();
    }


    //Aktarılan Soru Sayısı kadar Rastgele Cevap Oluştur
    List<Character> cevap_uret(int toplam_soru){
        Log.d("Uretilen Soru Sayisi", Integer.toString(toplam_soru));
        List<Character> characters = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < toplam_soru; i++) {
            char randomChar = "ABCDE".charAt(random.nextInt(5));  // Generate a random character from the set
            characters.add(randomChar);
        }

        return characters;
    }


    // Cevaplar için Oluşturulan Char(karakter) listesini String Listesine Dönüştürme
    List<String> listeyeCevir(List<Character> liste){

        // Convert List<Character> to List<String>
        List<String> stringList = new ArrayList<>();
        for (int i = 1; i <= liste.size(); i++) {
            char character = liste.get(i-1);
            String stringWithIndex = i + ": " + character;
            stringList.add(stringWithIndex);
        }

        return stringList;
    }


    // Listeyi ListView'e Aktar
    private void ListeAyarla(ListView listTest ,List<String> cevaplar){
        ListView dofruListeLayout= listTest ;
        ArrayAdapter<String> veriAdaptoru = new ArrayAdapter<String>
                (this,
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        cevaplar);
        dofruListeLayout.setAdapter(veriAdaptoru);


    }


    // Listeleri Karşılatırma -- Dogru, Yanlış ve Boş sayılarını Bulma
    private static <T> void compareLists(List<T> list_dogru, List<T> list_rnd) {

        // Check if both lists have the same size
        if (list_dogru.size() != list_rnd.size()) {
            return ;
        }

        // Iterate through the lists and compare elements
        for (int i = 0; i < list_dogru.size(); i++) {
            if (!list_dogru.get(i).equals(list_rnd.get(i))) {
                if( list_rnd.get(i).equals('X')){
                    bosSayisi_int++;
                }else{
                    yanlisSayisi_int++;
                }
            }else {
                dogruSayisi_int++;
            }
        }

        puanDurumu_int = dogruSayisi_int * puan_carpani ;

    }

    // Doğru, Yanlış ve Boş Soru Durumu View'e Aktar
    void setPuan(){

        dogruSayisi_txt = findViewById(R.id.dogruSayisi);
        yanlisSayisi_txt  = findViewById(R.id.yanlisSayisi);
        bosSayisi_txt  = findViewById(R.id.bosSayisi);
        puanDurumu_txt  = findViewById(R.id.puanDurumu);



        dogruSayisi_txt.setText( String.valueOf(dogruSayisi_int));
        yanlisSayisi_txt.setText( String.valueOf(yanlisSayisi_int) );
        bosSayisi_txt.setText( String.valueOf(bosSayisi_int) );
        puanDurumu_txt.setText( String.valueOf(puanDurumu_int) );

    }

}