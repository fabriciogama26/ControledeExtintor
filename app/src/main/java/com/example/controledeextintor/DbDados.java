package com.example.controledeextintor;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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
                + QrCodeEntry.COLUMN_QRCODE_DATE_TIME + " TEXT NOT NULL, "
                + QrCodeEntry.COLUMN_QRCODE_DATE_TIME_2 + " TEXT NOT NULL);";

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

    public void insertData(String qrCodeClass, String qrCodeSetor, String qrCodeDate, String qrCodeDateNext) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QrCodeEntry.COLUMN_QRCODE_CLASS, qrCodeClass);
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_SETOR, qrCodeSetor);

        // Insira a data atual
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_DATE_TIME, qrCodeDate);

        // Insira a data de inicio
        contentValues.put(QrCodeEntry.COLUMN_QRCODE_DATE_TIME_2, qrCodeDateNext);

        db.insert(QrCodeEntry.TABLE_NAME, null, contentValues);
        db.close();
    }

    public static class QrCodeEntry implements BaseColumns {
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "extintores_table";
        public static final String COLUMN_QRCODE_CLASS = "clase";
        public static final String COLUMN_QRCODE_SETOR = "local";
        public static final String COLUMN_QRCODE_DATE_TIME = "data_troca";
        public static final String COLUMN_QRCODE_DATE_TIME_2 = "data_de_inicio";
    }
}
