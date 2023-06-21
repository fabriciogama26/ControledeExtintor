package com.example.controledeextintor;
import androidx.appcompat.app.AppCompatActivity;


import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.controledeextintor.databinding.ActivityListBinding;


import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ActivityListBinding binding;
    private static DbDados dbHelper;
    private List<String> extintores = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mantém a orientação da tela em modo retrato
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Recupera a referência ao ListView
        listView = binding.listView;

        // Recupera a referência ao SearchView
        searchView = binding.searchView;

        // Cria um adapter para o ListView
        adapter = new ProductAdapter(ListActivity.this, new ArrayList<>());

        // Define o adapter criado como o adapter do ListView
        listView.setAdapter(adapter);

        // Cria uma instância do banco de dados
        dbHelper = new DbDados(this);

        // Define o listener para o SearchView
        searchView.setOnQueryTextListener(onQueryTextListener);

    }

    private final SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            // Chamado quando o usuário clica no botão de pesquisa
            // Aqui você deve realizar a pesquisa no banco de dados e exibir os resultados
            // Recupera a lista de produtos do banco de dados, com base no texto da pesquisa
            if (query != null) {
                extintores = dbHelper.getDistinctProductNames(query);
            } else {
                extintores = new ArrayList<>();
            }

            // Limpa o adapter existente e adiciona os produtos recuperados
            adapter.clear();
            adapter.addAll(extintores);
            adapter.notifyDataSetChanged();

            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            // Chamado quando o texto da pesquisa é alterado
            // Aqui você deve implementar o efeito de autocompletar

            // Recupera a lista de produtos do banco de dados, com base no texto da pesquisa
            if (newText != null) {
                extintores = dbHelper.getDistinctProductNames(newText);
            } else {
                extintores = new ArrayList<>();
            }

            // Limpa o adapter existente e adiciona os produtos recuperados
            adapter.clear();
            adapter.addAll(extintores);
            adapter.notifyDataSetChanged();

            return true;
        }
    };

    public static class ProductAdapter extends ArrayAdapter<String> {
        private List<String> mExtintor;
        private Context mContext;


        // Construtor da classe que recebe o contexto e a lista de produtos como parâmetros
        public ProductAdapter(Context context, List<String> extintores) {
            super(context, R.layout.item_list, extintores);
            mContext = context;
            mExtintor = extintores;
        }

        // Sobrescrita do método getView que é responsável por retornar a View correspondente a um item da lista
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            // inflar o layout do item da lista
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_list, null);

                // criar um objeto ViewHolder para guardar as referências dos elementos do layout
                holder = new ViewHolder();
                holder.item_image_tipo = convertView.findViewById(R.id.item_image_Tipo);
                holder.item_text_local = convertView.findViewById(R.id.item_Local);
                holder.item_text_troca = convertView.findViewById(R.id.item_Data_troca);
                holder.item_text_inspe = convertView.findViewById(R.id.item_Data_inspe);
                holder.item_image_class = convertView.findViewById(R.id.item_image_Class);
                holder.item_text_medida = convertView.findViewById(R.id.item_image_Medida);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // obter os dados do extintor para a posição atual
            String extintor = mExtintor.get(position);
            String[] infoArray = extintor.split("\n");

            // extrair os dados do extintor
            String id = "";
            String classe = "";
            String setor ="";
            String tipo ="";
            String medida ="";
            String dataRecarga = "";
            String dataInspecao = "";
            String dataQrcode = "";

            // Loop para extrair informações do array de informações do extintor:
            // O valor "2" é adicionado ao índice do caractere de dois pontos (:) encontrado na string, porque a intenção é pular esse caractere
            // e também o espaço em branco que vem após ele.
            for (String info : infoArray) {
                if (info.contains("ID:")) {
                    id = info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Classe:")) {
                    classe = info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Setor:")) {
                    setor = "Setor: " + info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Tipo:")) {
                    tipo = info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Medida:")) {
                    medida = info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Data de Recarga:")) {
                    dataRecarga = "Recarga: " + info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Data de Inspeção:")) {
                    dataInspecao = "Insp.: " + info.substring(info.indexOf(":") + 2);
                }else if (info.contains("Data do QRCode:")) {
                    dataQrcode = info.substring(info.indexOf(":") + 2);
                }
            }

            // Define o texto e a imagem do item da lista com base nos dados do produto
            holder.item_text_local.setText(setor);
            holder.item_text_medida.setText(medida);
            holder.item_text_troca.setText(dataRecarga);
            holder.item_text_inspe.setText(dataInspecao);
            int imageClassId = getImageClassId(classe);
            int imageTipoId = getImageTipoId(tipo);
            holder.item_image_tipo.setImageResource(imageTipoId);
            holder.item_image_class.setImageResource(imageClassId);

            final String finalId = id;

            // Adiciona um listener de clique no item da lista
            convertView.setOnClickListener(v -> {

                // Cria um diálogo de confirmação
                new AlertDialog.Builder(getContext())
                        .setTitle("Deletar produto")
                        .setMessage("Deseja deletar o produto ?")
                        .setPositiveButton("Sim", (dialog, which) -> {
                            dbHelper.delete(dbHelper, mContext, finalId);
                        })
                        .setNegativeButton("Não", null)
                        .show();
            });

            return convertView;
        }

        private static class ViewHolder {
            ImageView item_image_tipo; // Referência para a ImageView do item da lista
            TextView item_text_local; // Referência para a TextView do local do item da lista
            TextView item_text_troca; // Referência para a TextView da data de troca do item da lista
            TextView item_text_medida; // Referência para a TextView da medida do item da lista
            TextView item_text_inspe; // Referência para a TextView da data de inspeção do item da lista
            ImageView item_image_class; // Referência para a ImageView do item da lista
        }

            // Método privado que retorna o ID da imagem correspondente ao produto
        private int getImageClassId(String classe) {
            switch (classe) {
                case "A":
                    return R.drawable.classe_a;
                case "AB":
                    return R.drawable.classe_b;
                case "BC":
                    return R.drawable.classe_c;
                case "ABC":
                    return R.drawable.classe_b;
                case "D":
                    return R.drawable.classe_d;
                case "K":
                    return R.drawable.classe_k;
                default:
                    // Retorne o valor padrão caso nenhum caso seja correspondido
                    return R.drawable.ic_launcher_background;
            }
        }

        private int getImageTipoId(String tipo) {
            switch (tipo) {
                case "AGUA":
                    return R.drawable.ic_agua;
                case "ESPUMA":
                    return R.drawable.ic_espuma;
                case "QUIMICO":
                    return R.drawable.ic_po_quimico;
                case "ESPECIAL":
                    return R.drawable.ic_po_especial;
                case "CO2":
                    return R.drawable.ic_co2;
                case "HALON":
                    return R.drawable.ic_halon;
                default:
                    // Retorne o valor padrão caso nenhum caso seja correspondido
                    return R.drawable.ic_launcher_background;
            }
        }
    }
}