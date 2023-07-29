package ru.serujimir.cardholder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText edNumber, edCvv, edExpiration;
    Button btnPOST, btnGET;

    DBhelper dBhelper;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edNumber = findViewById(R.id.edCardNumber);
        edCvv = findViewById(R.id.edCardCVVcode);
        edExpiration = findViewById(R.id.edCardExpirationDate);

        btnPOST = findViewById(R.id.btnPOST);
        btnGET = findViewById(R.id.btnGET);
        context = this;
        dBhelper = new DBhelper(this);

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();

        btnPOST.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();

                String number = edNumber.getText().toString();
                String cvv = edCvv.getText().toString();
                String expiration = edExpiration.getText().toString();

                contentValues.put("number", number);
                contentValues.put("cvv", cvv);
                contentValues.put("expiration", expiration);
                long rowID = sqLiteDatabase.insert("DBcard", null, contentValues);
            }

        });
        btnGET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.query("DBcard", null, null, null, null, null, null);
                if(cursor.moveToFirst()) {
                    int idColIndex = cursor.getColumnIndex("id");
                    int numberColIndex = cursor.getColumnIndex("number");
                    int cvvColIndex = cursor.getColumnIndex("cvv");
                    int expiration = cursor.getColumnIndex("expiration");

                    do {
                        Log.d("test", "" +
                                "ID = " + cursor.getInt(idColIndex) +
                                ", number = " + cursor.getString(numberColIndex) +
                                ", cvv = " + cursor.getString(cvvColIndex) +
                                ", expiration = " + cursor.getString(expiration));
                    }while (cursor.moveToNext());
                }else
                    Log.d("test", "0 rows");
                cursor.close();
                dBhelper.close();
            }
        });
    }

    class DBhelper extends SQLiteOpenHelper {

        public DBhelper(@Nullable Context context) {
            super(context, "DBcard", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("test", "Created DB!");
            db.execSQL("create table DBcard ("
            + "id integer primary key autoincrement,"
            + "number text,"
                    + "cvv text,"
                    + "expiration text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}