package com.example.controledeextintor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.example.controledeextintor.databinding.ActivityQrcodeBinding;
import com.example.controledeextintor.databinding.ActivityScanBinding;

public class QrcodeActivity extends AppCompatActivity {

    private ActivityQrcodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mantém a orientação da tela em modo retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}