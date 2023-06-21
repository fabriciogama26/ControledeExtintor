package com.example.controledeextintor;

import androidx.annotation.NonNull;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.controledeextintor.databinding.ActivityScanBinding;
import com.google.zxing.Result;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


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
                            String[] qrData = cleanText.split(";"); // Divide a string em um array com os dados separados por ';'

                            if (qrData.length == 8) { // Verifica se o array tem exatamente 8 elementos

                                int id = Integer.parseInt(qrData[0]);
                                // Instancia um objeto DbDados para inserir os dados no banco de dados
                                DbDados dbHelper = new DbDados(ScanActivity.this);

                                String datas = dbHelper.buscarDadosPorId(id);

                                if (!datas.isEmpty()) {
                                    String[] arrayDatas = datas.split(";"); // Divide a string de datas em um array

                                    if (arrayDatas.length == 2) { // Verifica se o array de datas tem exatamente 2 elementos

                                        String DataRecarga = arrayDatas[0];
                                        String DataInspecao = arrayDatas[1];

                                        // Obtém a data atual
                                        Calendar calendar = Calendar.getInstance();
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                                        String dataAtual = dateFormat.format(calendar.getTime());

                                        // Calcula a diferença em dias entre a data atual e a data de recarga
                                        long diffRecarga = 0;
                                        long diffInspecao = 0;
                                        try {
                                            Date dateAtual = dateFormat.parse(dataAtual);
                                            Date dateRecarga = dateFormat.parse(DataRecarga);
                                            Date dateInspecao = dateFormat.parse(DataInspecao);
                                            diffInspecao = TimeUnit.DAYS.convert(dateInspecao.getTime() - dateAtual.getTime(), TimeUnit.MILLISECONDS);
                                            diffRecarga = TimeUnit.DAYS.convert(dateRecarga.getTime() - dateAtual.getTime(), TimeUnit.MILLISECONDS);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        // Inicie a nova Activity passando a mensagem através do Intent
                                        Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                                        intent.putExtra("mensagem", "Troca do extintor confirmada!" + " " + diffRecarga + " " + diffInspecao);
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