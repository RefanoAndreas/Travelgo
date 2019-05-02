package com.qreatiq.travelgo;

import android.content.Intent;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

public class TrainSearch extends AppCompatActivity {

    MaterialButton searchTrainBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_search);

        link.setToolbar(this);

        final CardView jumlahpenumpang = findViewById(R.id.TrainSearch_jumlahPenumpang);
        jumlahpenumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainSearchJumlahPenumpang jumlahPenumpangSheet = new TrainSearchJumlahPenumpang();
                jumlahPenumpangSheet.show(getSupportFragmentManager(), "Pilih Jumlah Penumpang");
                jumlahPenumpangSheet.setStyle(jumlahPenumpangSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        final CardView kelas = findViewById(R.id.TrainSearch_kelas);
        kelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TrainSearchKelas kelasPesawatSheet = new TrainSearchKelas();
                kelasPesawatSheet.show(getSupportFragmentManager(), "Pilih Kelas Pesawat");
                kelasPesawatSheet.setStyle(kelasPesawatSheet.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
            }
        });

        Switch aSwitch = findViewById(R.id.TrainSearch_switch);
        final LinearLayout kembali = findViewById(R.id.TrainSearch_tanggalKembali);
        final LinearLayout tanggalContainer = findViewById(R.id.TrainSearch_tanggalContainer);
//        tanggalContainer.getWeightSum();
        tanggalContainer.setWeightSum(1);
        kembali.setVisibility(View.GONE);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
//                    tanggalContainer.getWeightSum();
                    tanggalContainer.setWeightSum(2);
                    kembali.setVisibility(View.VISIBLE);
                }else{
//                    tanggalContainer.getWeightSum();
                    tanggalContainer.setWeightSum(1);
                    kembali.setVisibility(View.GONE);
                }
            }
        });

        searchTrainBtn = (MaterialButton) findViewById(R.id.search_train);
        searchTrainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TrainSearch.this, TrainSearchJadwal.class));
            }
        });

    }
}
