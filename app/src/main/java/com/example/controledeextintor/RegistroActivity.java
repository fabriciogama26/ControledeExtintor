package com.example.controledeextintor;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.controledeextintor.databinding.ActivityRegistroBinding;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private static final int READ_MEDIA_IMAGES_PERMISSION_CODE = 100;
    private String itemSelecionado; // Variável para armazenar o item selecionado
    private String itemRadio; // Variável para armazenar o item Radio

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mantém a orientação da tela em modo retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Verifique se a permissão da câmera foi concedida
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Se a permissão não foi concedida, solicite-a
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, READ_MEDIA_IMAGES_PERMISSION_CODE);
        }

        // Defina o clique do botão
        binding.buttonSetor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crie uma caixa de diálogo de seleção
                AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
                builder.setTitle("Selecione o Setor")
                        .setItems(R.array.setores_array, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Lógica para tratar a seleção do item
                                String[] setores = getResources().getStringArray(R.array.setores_array);
                                itemSelecionado = setores[which];
                                Toast.makeText(RegistroActivity.this, "Setor selecionado: " + itemSelecionado, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancelar", null);

                // Mostre a caixa de diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        RadioButton radioClassA = binding.radioClassA;
        RadioButton radioClassB = binding.radioClassB;
        RadioButton radioClassC = binding.radioClassC;
        RadioButton radioClassD = binding.radioClassD;
        RadioButton radioClassK = binding.radioClassK;

        // Define o clique dos botões de rádio
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Marca o botão de rádio selecionado
                boolean checked = ((RadioButton) v).isChecked();

                // Desmarca os outros botões de rádio
                radioClassA.setChecked(v.getId() == R.id.radioClassA && checked);
                radioClassB.setChecked(v.getId() == R.id.radioClassB && checked);
                radioClassC.setChecked(v.getId() == R.id.radioClassC && checked);
                radioClassD.setChecked(v.getId() == R.id.radioClassD && checked);
                radioClassK.setChecked(v.getId() == R.id.radioClassK && checked);

                // Atualiza a variável itemSelecionado com o valor do item selecionado
                if (checked) {
                    switch (v.getId()) {
                        case R.id.radioClassA:
                            itemRadio = "A";
                            break;
                        case R.id.radioClassB:
                            itemRadio = "B";
                            break;
                        case R.id.radioClassC:
                            itemRadio = "C";
                            break;
                        case R.id.radioClassD:
                            itemRadio = "D";
                            break;
                        case R.id.radioClassK:
                            itemRadio = "K";
                            break;
                    }

                    // Exemplo de uso do valor selecionado
                    Toast.makeText(RegistroActivity.this, "Item selecionado: " + itemRadio, Toast.LENGTH_SHORT).show();
                }
            }
        };

        // Define o mesmo clique para todos os botões de rádio
        radioClassA.setOnClickListener(onClickListener);
        radioClassB.setOnClickListener(onClickListener);
        radioClassC.setOnClickListener(onClickListener);
        radioClassD.setOnClickListener(onClickListener);
        radioClassK.setOnClickListener(onClickListener);

        binding.buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifique se as variáveis estão vazias
                if (itemRadio != null && !itemRadio.isEmpty() && itemSelecionado != null && !itemSelecionado.isEmpty()) {
                    // Se as variáveis não estiverem vazias, chame o método insertData()
                    DbDados dbHelper = new DbDados(RegistroActivity.this);
                    dbHelper.insertData(itemRadio, itemSelecionado);

                    // Inicie a nova Activity passando a mensagem através do Intent
                    Intent intent = new Intent(RegistroActivity.this, QrcodeActivity.class);
                    intent.putExtra("mensagem", "Registro confirmado!");
                    startActivity(intent);

                } else {
                    // Caso contrário, exiba uma mensagem de erro ou realize outra ação apropriada
                    Toast.makeText(RegistroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
