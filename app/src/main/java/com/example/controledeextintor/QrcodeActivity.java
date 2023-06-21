package com.example.controledeextintor;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;
import com.example.controledeextintor.databinding.ActivityQrcodeBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.ByteArrayOutputStream;


public class QrcodeActivity extends AppCompatActivity {
    private ActivityQrcodeBinding binding;
    private String itemQrcode; // Variável para armazenar o item do qrcode

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

        binding.buttonGerarQr.setOnClickListener(view -> {
            try (DbDados dbHelper = new DbDados(QrcodeActivity.this)) {
                itemQrcode = dbHelper.buscarDados(); // Altere o retorno do método para String

                // Verifica se há dados no resultado da busca
                if (itemQrcode != null && !itemQrcode.isEmpty()) { // Verifica se a String não está vazia

                    Toast.makeText(QrcodeActivity.this, "Dados encontrados", Toast.LENGTH_SHORT).show();
                    // Cria o objeto BarcodeEncoder e gera o QR Code
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    // Tenta gerar o QR Code com os dados obtidos
                    try {
                        Bitmap qrCode = barcodeEncoder.encodeBitmap(itemQrcode, BarcodeFormat.QR_CODE, 500, 500);
                        binding.imageViewQRCode.setImageBitmap(qrCode);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        binding.buttonShare.setOnClickListener(view -> {
            if (binding.imageViewQRCode.getDrawable() == null) {
                Toast.makeText(QrcodeActivity.this, "Primeiro gere o QR Code.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Obtém o bitmap do ImageView
            Bitmap qrCode = ((BitmapDrawable) binding.imageViewQRCode.getDrawable()).getBitmap();

            try {
                // Cria o intent de compartilhamento
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain"); // Define o tipo de conteúdo como texto

                // Extrai os dados da string itemQrcode
                String[] dados = itemQrcode.split(";");

                // Obtém o texto da terceira posição
                String texto = dados[2];

                // Adicione o texto desejado
                shareIntent.putExtra(Intent.EXTRA_TEXT, texto);

                // Adicione a imagem ao intent de compartilhamento
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                qrCode.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(QrcodeActivity.this.getContentResolver(), qrCode, "QR Code", null);
                Uri imageUri = Uri.parse(path);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                startActivity(Intent.createChooser(shareIntent, "Compartilhar Qrcode"));

                // Inicie a nova Activity passando a mensagem através do Intent
                Intent intent = new Intent(QrcodeActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(QrcodeActivity.this, "Não foi possível compartilhar o QR Code", Toast.LENGTH_SHORT).show();
            }
        });
    }
}