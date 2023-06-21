package com.example.controledeextintor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;
import com.example.controledeextintor.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
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

        // Configura o clique no botão de scanner
        binding.imageIconScan.setOnClickListener(view -> {
            // Abre a Activity de scanner
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        binding.imageIconRegistre.setOnClickListener(view -> {
            // Abre a Activity de registro
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });

        binding.imageIconList.setOnClickListener(view -> {
            // Abre a Activity de Lista
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });


//        binding.iconmotech.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setTitle("Confirmação")
//                        .setMessage("Deseja realmente deletar os dados?")
//                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Lógica de tratamento de clique para o menu
//                                // Faça a ação de exclusão aqui
//                                DbDados dbHelper = new DbDados(MainActivity.this);
//                                dbHelper.deletarBancoDados();
//                            }
//                        })
//                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Faça a ação desejada quando o usuário selecionar "Não" ou clicar fora do diálogo
//                            }
//                        })
//                        .show();
//            }
//
//        });

//        binding.iconmotech.setOnClickListener(view -> {
//            DbDados dbHelper = new DbDados(MainActivity.this);
//            SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//            File directory = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
//            File file = new File(directory, "dbextintores.csv");
//
//            FileWriter writer;
//
//            try {
//                writer = new FileWriter(file);
//
//                Cursor cursor = db.rawQuery("SELECT * FROM " + DbDados.QrCodeEntry.TABLE_NAME, null);
//
//                writer.append("id,CLASS,SETOR,TIPO,MEDIDA,DATE_RECARGA,DATE_INSPECAO,DATE_QRCODE \n");
//                if (cursor.moveToFirst()) {
//                    do {
//                        String id = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry._ID));
//                        String qrCodeClass = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_CLASS));
//                        String qrCodeSetor = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_SETOR));
//                        String qrCodeTipo = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_TIPO));
//                        String qrCodeMedida = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_MEDIDA));
//                        String qrCodeDateRecarga = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA));
//                        String qrCodeDateInspecao = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO));
//                        String qrCodeDateQrcode = cursor.getString(cursor.getColumnIndex(DbDados.QrCodeEntry.COLUMN_QRCODE_DATE_QRCODE));
//
//                        writer.append(id).append(",").append(qrCodeClass).append(",").append(qrCodeSetor).append(",").append(qrCodeTipo).append(",").append(qrCodeMedida).append(",").append(qrCodeDateRecarga).append(",").append(qrCodeDateInspecao).append(",").append(qrCodeDateQrcode).append("\n");
//                    } while (cursor.moveToNext());
//                }
//
//                writer.flush();
//                writer.close();
//                cursor.close();
//
//                Toast.makeText(getApplicationContext(), "Banco de dados exportado com sucesso para " + file, Toast.LENGTH_SHORT).show();
//
//                Intent shareIntent = new Intent(Intent.ACTION_SEND);
//                shareIntent.setType("text/csv");
//                Uri fileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
//                startActivity(Intent.createChooser(shareIntent, "Compartilhar arquivo CSV"));
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(getApplicationContext(), "Erro ao exportar banco de dados", Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}