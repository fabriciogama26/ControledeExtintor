package com.example.controledeextintor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.util.Calendar;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.Date;

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
