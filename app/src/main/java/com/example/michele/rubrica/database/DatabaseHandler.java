package com.example.michele.rubrica.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.michele.rubrica.models.Contatto;

import java.util.ArrayList;

/**
 * Created by Michele on 10/03/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_CELLULARE = "cellulare";
    private static final String COLUMN_SPECIALE = "speciale";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rubrica";
    private static final String TABLE_CONTATTI = "contatti";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABLE_CONTATTI + "( "
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOME + " TEXT, "
                + COLUMN_CELLULARE + " TEXT, " + COLUMN_SPECIALE + " INTEGER" + " )";
        sqLiteDatabase.execSQL(CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST " + TABLE_CONTATTI);
        onCreate(sqLiteDatabase);
    }

    public void addNote(Contatto contatto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOME, contatto.getNome());
        contentValues.put(COLUMN_CELLULARE, contatto.getNumero());
        contentValues.put(COLUMN_SPECIALE, contatto.getSpeciale());
        long id = db.insert(TABLE_CONTATTI, null, contentValues);
        contatto.setId((int)id);
        db.close();
    }

    public int updateNote(Contatto contatto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOME, contatto.getNome());
        contentValues.put(COLUMN_CELLULARE, contatto.getNumero());
        contentValues.put(COLUMN_SPECIALE, contatto.getSpeciale());
        return db.update(TABLE_CONTATTI, contentValues, COLUMN_ID + " = ?", new String[]{String.valueOf(contatto.getId())});
    }

    public void deleteNote(Contatto contatto) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTATTI, COLUMN_ID + " =? ", new String[]{String.valueOf(contatto.getId())});
        db.close();
    }


    public ArrayList<Contatto> getAllContatti() {
        ArrayList<Contatto> contattiList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + TABLE_CONTATTI;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        if (cursor.moveToFirst()) {
            do {
                Contatto contatto = new Contatto();
                contatto.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID))));
                contatto.setNome(cursor.getString(cursor.getColumnIndex(COLUMN_NOME)));
                contatto.setNumero(cursor.getString(cursor.getColumnIndex(COLUMN_CELLULARE)));
                contatto.setSpeciale(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_SPECIALE))));
                contattiList.add(contatto);
            } while(cursor.moveToNext());
        }
        db.close();
        return contattiList;
    }
}

