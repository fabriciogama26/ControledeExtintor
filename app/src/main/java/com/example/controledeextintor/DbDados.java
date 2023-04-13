package com.example.controledeextintor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.provider.BaseColumns;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbDados extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dbextintores.db";
    private static final int DATABASE_VERSION = 1;

    public DbDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela
        String SQL_CREATE_QRCODE_TABLE = "CREATE TABLE " + QrCodeEntry.TABLE_NAME + " ("
                + QrCodeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + QrCodeEntry.COLUMN_QRCODE_CLASS + " TEXT NOT NULL, "
                + QrCodeEntry.COLUMN_QRCODE_SETOR + " TEXT NOT NULL, "
                + QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA + " TEXT NOT NULL, "
                + QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO + " TEXT NOT NULL,"
                + QrCodeEntry.COLUMN_QRCODE_DATE_QRCODE + " TEXT NOT NULL);";
        ;

        db.execSQL(SQL_CREATE_QRCODE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implemente este método se quiser atualizar o banco de dados
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implemente este método se quiser rebaixar o banco de dados
    }

    public void insertData(String qrCodeClass, String qrCodeSetor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QrCodeEntry.COLUMN_QRCODE_CLASS, qrCodeClass);
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_SETOR, qrCodeSetor);

        // Obtenha a data atual em formato de string com o padrão desejado
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String currentDate = dateFormat.format(new Date());

        // Obtenha a data corrente
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        // Adicione 1 ano à data corrente para a data de recarga
        calendar.add(Calendar.YEAR, 1);
        String qrCodeDateRecarga = dateFormat.format(calendar.getTime());

        // Adicione 5 anos à data corrente para a data de inspeção
        calendar.add(Calendar.YEAR, 5);
        String qrCodeDateInspecao = dateFormat.format(calendar.getTime());

        // Insira a data de troca
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA, qrCodeDateRecarga);

        // Insira a data de inspeçao
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO, qrCodeDateInspecao);

        // Insira a data gerada do qrcode
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_DATE_QRCODE, currentDate);

        db.insert(QrCodeEntry.TABLE_NAME, null, contentValues);
        db.close();
    }
    public String buscarDados() {
        StringBuffer dados = new StringBuffer();

        // Consulta para buscar os dados do banco de dados
        String query = "SELECT * FROM " + QrCodeEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // Verifica se o cursor é nulo
        if (cursor != null) {
            // Percorre o cursor e obtém os dados
            if (cursor.moveToLast()) {
                do {
                    // Obtém o ID e os valores das colunas desejadas
                    int id = cursor.getInt(cursor.getColumnIndex(QrCodeEntry._ID));
                    String qrCodeClass = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_CLASS));
                    String qrCodeSetor = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_SETOR));
                    String qrCodeDateRecarga = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA));
                    String qrCodeDateInspecao = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO));
                    String qrCodeDateQrcode = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_QRCODE));

                    // Formata os dados no formato desejado
                    String dadosSalvos = "mp_" + id + ";" + qrCodeClass + ";" + qrCodeSetor + ";" + qrCodeDateRecarga + ";" + qrCodeDateInspecao + ";" + qrCodeDateQrcode + "\n";

                    // Adiciona os dados formatados ao StringBuffer
                    dados.append(dadosSalvos);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }

        db.close();

        return dados.toString();
    }
    public List<String> getDistinctProductNames(String searchText) {
        // Obtém uma instância legível do banco de dados
        SQLiteDatabase db = this.getReadableDatabase();

        // Lista para armazenar as informações dos produtos únicos
        List<String> productList = new ArrayList<>();

        // Define as colunas que serão retornadas na consulta
        String[] columns = new String[]{QrCodeEntry._ID, QrCodeEntry.COLUMN_QRCODE_SETOR, QrCodeEntry.COLUMN_QRCODE_CLASS, QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA, QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO, QrCodeEntry.COLUMN_QRCODE_DATE_QRCODE};

        // Define a cláusula WHERE para filtrar os resultados pelo termo de busca fornecido
        String selection = QrCodeEntry.COLUMN_QRCODE_SETOR + " LIKE ?";

        // Define os argumentos da cláusula WHERE
        String[] selectionArgs = new String[]{"%" + searchText + "%"};

        // Realiza a consulta para obter os produtos que contêm o termo de busca
        Cursor cursor = db.query(true, QrCodeEntry.TABLE_NAME, columns, selection, selectionArgs, null, null, null, null);

        // Adiciona os dados de cada produto na lista
        if (cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex(QrCodeEntry._ID);
            int indexSetor = cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_SETOR);
            int indexClass = cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_CLASS);
            int indexDateRecarga = cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA);
            int indexDateInspecao = cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO);
            int indexDateQrcode = cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_QRCODE);
            if (indexId >= 0 && indexSetor >= 0 && indexClass >= 0 && indexDateRecarga >= 0 && indexDateInspecao >= 0 && indexDateQrcode >= 0) {
                do {
                    int id = cursor.getInt(indexId);
                    String setor = cursor.getString(indexSetor);
                    String qrCodeClass = cursor.getString(indexClass);
                    String qrCodeDateRecarga = cursor.getString(indexDateRecarga);
                    String qrCodeDateInspecao = cursor.getString(indexDateInspecao);
                    String qrCodeDateQrcode = cursor.getString(indexDateQrcode);
                    // Concatena as colunas retornadas em uma única string
                    String productInfo = "ID: " + id + "\n" +
                            "Classe: " + qrCodeClass + "\n" +
                            "Setor: " + setor + "\n" +
                            "Data de Recarga: " + qrCodeDateRecarga + "\n" +
                            "Data de Inspeção: " + qrCodeDateInspecao + "\n" +
                            "Data do QRCode: " + qrCodeDateQrcode;
                    productList.add(productInfo);
                } while (cursor.moveToNext());
            }
        }

        // Fecha o cursor e a conexão com o banco de dados
        cursor.close();
        db.close();

        // Retorna a lista de informações dos produtos únicos
        return productList;
    }



    public String buscarDadosPorId(int id) {
        StringBuffer dados = new StringBuffer();

        // Consulta para buscar os dados do banco de dados pelo ID
        String query = "SELECT " + QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA + ", " + QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO +
                " FROM " + QrCodeEntry.TABLE_NAME +
                " WHERE " + QrCodeEntry._ID + " = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

        // Verifica se o cursor é nulo
        if (cursor != null && cursor.moveToFirst()) {
            // Obtém os valores das colunas desejadas
            String DateRecarga = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_RECARGA));
            String DateInspecao = cursor.getString(cursor.getColumnIndex(QrCodeEntry.COLUMN_QRCODE_DATE_INSPECAO));

            String datas = DateRecarga + ";" + DateInspecao;

            dados.append(datas);

            cursor.close();
        }

        db.close();

        return dados.toString();
    }

    public void deletarBancoDados() {
        SQLiteDatabase db = this.getWritableDatabase();

        // Deleta a tabela
        db.execSQL("DROP TABLE IF EXISTS " + QrCodeEntry.TABLE_NAME);

        // Chama o método onCreate() para criar novamente a tabela
        onCreate(db);

        db.close();
    }

    public static void delete(Context context, DbDados dbHelper, String value) {
        // Recebe como parâmetros o contexto da aplicação, o helper do banco de dados, o valor e a data do registro a ser deletado
        SQLiteDatabase db = null;
        try {
            // Obtém uma instância do banco de dados em modo escrita
            db = dbHelper.getWritableDatabase();
            // Define a cláusula WHERE para selecionar o registro a ser deletado
            String whereClause = DbDados.QrCodeEntry.COLUMN_QRCODE_SETOR + " = ?";
            // Define os argumentos da cláusula WHERE
            String[] whereArgs = {value};
            // Executa a query para buscar o ID do primeiro registro encontrado com base nos argumentos informados
            Cursor cursor = db.query(DbDados.QrCodeEntry.TABLE_NAME,
                    new String[]{DbDados.QrCodeEntry._ID},
                    whereClause,
                    whereArgs,
                    null,
                    null,
                    null,
                    "1");
            // Verifica se o cursor retornou algum registro
            if (cursor.moveToFirst()) {
                // Obtém o ID do registro retornado pelo cursor
                long id = cursor.getLong(cursor.getColumnIndex(DbDados.QrCodeEntry._ID));
                // Deleta o registro do banco de dados com base no ID obtido
                int rowsDeleted = db.delete(DbDados.QrCodeEntry.TABLE_NAME, DbDados.QrCodeEntry._ID + " = ?", new String[]{String.valueOf(id)});
                // Verifica se o registro foi deletado com sucesso
                if (rowsDeleted > 0) {
                    Toast.makeText(context, "Registro deletado com sucesso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context,"Nenhum registro encontrado para deletar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context,"Nenhum registro encontrado para deletar", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(context,"Erro ao deletar registro", Toast.LENGTH_SHORT).show();
        } finally {
            // Fecha a conexão com o banco de dados
            if (db != null) {
                db.close();
            }
        }
    }

    public static class QrCodeEntry implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "extintores_table";
        public static final String COLUMN_QRCODE_CLASS = "classe";
        public static final String COLUMN_QRCODE_SETOR = "local";
        public static final String COLUMN_QRCODE_DATE_RECARGA = "data_recarga";
        public static final String COLUMN_QRCODE_DATE_INSPECAO = "data_inspecao";
        public static final String COLUMN_QRCODE_DATE_QRCODE = "data_qrcode";

    }
}
