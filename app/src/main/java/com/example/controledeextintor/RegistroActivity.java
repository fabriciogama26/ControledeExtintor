package com.example.controledeextintor;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.controledeextintor.databinding.ActivityRegistroBinding;
import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    private ActivityRegistroBinding binding;
    private static final int READ_MEDIA_IMAGES_PERMISSION_CODE = 100;
    private String itemTipo; // Variável para armazenar o item Radio
    private String itemSetor; // Variável para armazenar o item setor
    private String itemClass; // Variável para armazenar o item Class
    private String itemMedida; // Variável para armazenar o item Medida
    private String itemDataRecarga; // Variável para armazenar a data recarga
    private String itemDataInspe; // Variável para armazenar a data inspeçao


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
        binding.buttonSetor.setOnClickListener(view -> {
            // Crie uma caixa de diálogo de seleção
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("Selecione o Setor")
                    .setItems(R.array.setores_array, (dialog, which) -> {
                        // Lógica para tratar a seleção do item
                        String[] setores = getResources().getStringArray(R.array.setores_array);
                        itemSetor = setores[which];
                        Toast.makeText(RegistroActivity.this, "Setor selecionado: " + itemSetor, Toast.LENGTH_SHORT).show();

                        // Atualize o texto da caixa de texto (textbox) com o item selecionado
                        TextView textsetor = findViewById(R.id.textSetor);
                        textsetor.setText(itemSetor);
                    })
                    .setNegativeButton("Cancelar", null);

            // Mostre a caixa de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        binding.buttonClass.setOnClickListener(view -> {
            // Crie uma caixa de diálogo de seleção
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("Selecione a Classe")
                    .setItems(R.array.classes_array, (dialog, which) -> {
                        // Lógica para tratar a seleção do item
                        String[] classes = getResources().getStringArray(R.array.classes_array);
                        itemClass = classes[which];
                        Toast.makeText(RegistroActivity.this, "Classe selecionada: " + itemClass, Toast.LENGTH_SHORT).show();

                        // Atualize o texto da caixa de texto (textbox) com o item selecionado
                        TextView textClasse = findViewById(R.id.textClasse);
                        textClasse.setText(itemClass);
                    })
                    .setNegativeButton("Cancelar", null);

            // Mostre a caixa de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        binding.buttonTipo.setOnClickListener(view -> {
            // Crie uma caixa de diálogo de seleção
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("Selecione o Tipo")
                    .setItems(R.array.tipos_array, (dialog, which) -> {
                        // Lógica para tratar a seleção do item
                        String[] tipos = getResources().getStringArray(R.array.tipos_array);
                        itemTipo = tipos[which];
                        Toast.makeText(RegistroActivity.this, "Tipo selecionado: " + itemTipo, Toast.LENGTH_SHORT).show();

                        // Atualize o texto da caixa de texto (textbox) com o item selecionado
                        TextView texttipos = findViewById(R.id.texttipos);
                        texttipos.setText(itemTipo);
                    })
                    .setNegativeButton("Cancelar", null);

            // Mostre a caixa de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.buttonRecarga.setOnClickListener(view -> {
            // Crie uma caixa de diálogo de seleção
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("Selecione a Data de Recarga");

            // Defina o formato de data desejado
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            // Crie um DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, (datePickerView, year, monthOfYear, dayOfMonth) -> {
                // Aqui você pode implementar a lógica para lidar com a data selecionada
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                itemDataRecarga = dateFormat.format(selectedDate.getTime());
                Toast.makeText(RegistroActivity.this, "Data de Recarga selecionada", Toast.LENGTH_SHORT).show();

                // Atualize o texto da caixa de texto (textbox) com o item selecionado
                TextView textOperation = findViewById(R.id.textRecarga);
                textOperation.setText(itemDataRecarga);
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            // Exiba o DatePickerDialog
            datePickerDialog.show();

            builder.setNegativeButton("Cancelar", null);

            // Mostre a caixa de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.buttonInspe.setOnClickListener(view -> {
            // Crie uma caixa de diálogo de seleção
            AlertDialog.Builder builder = new AlertDialog.Builder(RegistroActivity.this);
            builder.setTitle("Selecione a Data de Inspeção");

            // Defina o formato de data desejado
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

            // Crie um DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(RegistroActivity.this, (datePickerView, year, monthOfYear, dayOfMonth) -> {
                // Aqui você pode implementar a lógica para lidar com a data selecionada
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, monthOfYear, dayOfMonth);
                itemDataInspe = dateFormat.format(selectedDate.getTime());
                Toast.makeText(RegistroActivity.this, "Data de Recarga selecionada", Toast.LENGTH_SHORT).show();

                // Atualize o texto da caixa de texto (textbox) com o item selecionado
                TextView textOperation = findViewById(R.id.textInspe);
                textOperation.setText(itemDataInspe);
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

            // Exiba o DatePickerDialog
            datePickerDialog.show();

            builder.setNegativeButton("Cancelar", null);

            // Mostre a caixa de diálogo
            AlertDialog dialog = builder.create();
            dialog.show();
        });

        binding.btRegistrar.setOnClickListener(view -> {
            EditText textMedida = findViewById(R.id.editMedida);

            // Atribua o valor da medida diretamente à variável itemMedida
            itemMedida = textMedida.getText().toString();

            // Verifique se as variáveis estão vazias
            if (itemSetor != null && !itemSetor.isEmpty() && itemTipo != null && !itemTipo.isEmpty() && !itemMedida.isEmpty() && itemClass != null && !itemClass.isEmpty() && itemDataInspe != null && !itemDataInspe.isEmpty() && itemDataRecarga != null && !itemDataRecarga.isEmpty()) {
                try (DbDados dbHelper = new DbDados(RegistroActivity.this)) {
                    // Se as variáveis não estiverem vazias, chame o método insertData()
                    dbHelper.insertData(itemClass, itemSetor, itemTipo, itemMedida, itemDataRecarga, itemDataInspe);

                    // Inicie a nova Activity passando a mensagem através do Intent
                    Intent intent = new Intent(RegistroActivity.this, QrcodeActivity.class);
                    intent.putExtra("mensagem", "Registro confirmado!");
                    startActivity(intent);
                } catch (Exception e) {
                    // Lida com exceções ocorridas durante a inserção dos dados
                    e.printStackTrace();
                }
            } else {
                // Caso contrário, exiba uma mensagem de erro ou realize outra ação apropriada
                Toast.makeText(RegistroActivity.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
