package com.example.controledeextintor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.controledeextintor.databinding.ActivityQrcodeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class QrcodeActivity extends AppCompatActivity {
    private ActivityQrcodeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrcodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mantém a orientação da tela em modo retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Verifica se há uma mensagem passada pela intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String mensagem = extras.getString("mensagem");
            // Mostra a mensagem em um Snackbar
            Snackbar.make(findViewById(android.R.id.content), mensagem, Snackbar.LENGTH_LONG)
                    .setBackgroundTint(getResources().getColor(R.color.green_snackbar))
                    .setTextColor(getResources().getColor(android.R.color.black))
                    .show();
        }

        binding.buttonGerarQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DbDados dbHelper = new DbDados(QrcodeActivity.this);
                String dados = dbHelper.buscarDados(); // Altere o retorno do método para String

                // Verifica se há dados no resultado da busca
                if (dados != null && !dados.isEmpty()) { // Verifica se a String não está vazia

                    // Obtém os dados do primeiro registro (ou do registro desejado)
                    String dadosSalvos = dados; // Altere para a String obtida no retorno do método

                    String dadosqr = dadosSalvos;

                    // Cria o objeto BarcodeEncoder e gera o QR Code
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    // Tenta gerar o QR Code com o produto informado
                    try {
                        Bitmap qrCode = barcodeEncoder.encodeBitmap(dadosqr, BarcodeFormat.QR_CODE, 500, 500);
                        binding.imageViewQRCode.setImageBitmap(qrCode);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        binding.buttonShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (binding.imageViewQRCode.getDrawable() == null) {
                    Toast.makeText(QrcodeActivity.this, "Gere o QR Code.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Obtém o bitmap do ImageView
                Bitmap qrCode = ((BitmapDrawable) binding.imageViewQRCode.getDrawable()).getBitmap();

                try {
                    // Cria o intent de compartilhamento
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    qrCode.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    String path = MediaStore.Images.Media.insertImage(QrcodeActivity.this.getContentResolver(), qrCode, "QR Code", null);
                    Uri imageUri = Uri.parse(path);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    startActivity(Intent.createChooser(shareIntent, "Compartilhar Qrcode"));


                } catch (Exception e) {
                    Toast.makeText(QrcodeActivity.this, "Não foi possível compartilhar o QR Code", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
}