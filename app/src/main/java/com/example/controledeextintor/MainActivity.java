package com.example.controledeextintor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

import com.example.controledeextintor.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

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
        binding.imageIconScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre a Activity de scanner
                Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent);

            }
        });

        binding.imageIconRegistre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abre a Activity de registro
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);

            }
        });

        binding.imageIconList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmação")
                        .setMessage("Deseja realmente deletar os dados?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lógica de tratamento de clique para o menu
                                // Faça a ação de exclusão aqui
                                DbDados dbHelper = new DbDados(MainActivity.this);
                                dbHelper.deletarBancoDados();
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Faça a ação desejada quando o usuário selecionar "Não" ou clicar fora do diálogo
                            }
                        })
                        .show();
            }

        });
    }
}