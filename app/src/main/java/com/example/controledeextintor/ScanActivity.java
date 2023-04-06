package com.example.controledeextintor;

import androidx.annotation.NonNull;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.controledeextintor.databinding.ActivityScanBinding;
import com.google.zxing.Result;


public class ScanActivity extends AppCompatActivity {
    private ActivityScanBinding binding;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Define a orientação da tela para retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Verifica se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Se a permissão não foi concedida, solicite-a
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }

        CodeScannerView scannerView = binding.scannerView;
        mCodeScanner = new CodeScanner(this, scannerView);

        // Configura o callback para a decodificação do código
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String text = result.getText();
                        if (text.startsWith("mp_")) { // Verifica se o texto começa com "mp_"
                            // Remove "mp_" da string
                            String cleanText = text.substring(3);
                            String[] qrData = cleanText.split(";"); // Divide a string em um array com os 4 dados separados por ';'

                            if (qrData.length == 4) { // Verifica se o array tem exatamente 4 elementos
                                // Instancia um objeto DbDados para inserir os dados no banco de dados
                                DbDados dbHelper = new DbDados(ScanActivity.this);
                                dbHelper.insertData(qrData[0], qrData[1], qrData[2], qrData[3]); // Insere os 4 dados no banco de dados

                                // Inicie a nova Activity passando a mensagem através do Intent
                                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                                intent.putExtra("mensagem", "Troca do extintor confirmada!");
                                startActivity(intent);
                            } else {
                                Toast.makeText(ScanActivity.this, "QR Code inválido", Toast.LENGTH_SHORT).show();
                                // Aguarda 3 segundos antes de reiniciar o scanner
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mCodeScanner.startPreview();
                                    }
                                }, 3000);
                            }
                        } else {
                            Toast.makeText(ScanActivity.this, "QR Code inválido", Toast.LENGTH_SHORT).show();
                            // Aguarda 3 segundos antes de reiniciar o scanner
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mCodeScanner.startPreview();
                                }
                            }, 3000);
                        }
                    }
                });
            }
        });
    }

    // Inicia a pré-visualização da câmera quando a Activity é retomada
    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    // Libera os recursos da câmera quando a Activity é pausada
    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}