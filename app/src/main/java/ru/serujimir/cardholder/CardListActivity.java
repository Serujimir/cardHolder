package ru.serujimir.cardholder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {

    ArrayList<Card> cards = new ArrayList<Card>();
    RecyclerView rv;

    DBhelper dBhelper;

    CardAdapter cardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        init();
    }

    public void init() {
        dBhelper = new DBhelper(this);
        setInitionalData();
        rv = findViewById(R.id.cardRV);
        cardAdapter = new CardAdapter(this, cards);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(cardAdapter);
    }
    public void setInitionalData() {

        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("DBcard", null, null, null, null, null, null);

        if(cursor.moveToFirst()) {

            int idColIndex = cursor.getColumnIndex("id");
            int numberColIndex = cursor.getColumnIndex("number");
            int cvvColIndex = cursor.getColumnIndex("cvv");
            int expirationColIndex = cursor.getColumnIndex("expiration");



            do {
                String number = cursor.getString(numberColIndex);
                String cvv = cursor.getString(cvvColIndex);
                String expiration = cursor.getString(expirationColIndex);
                String id = cursor.getString(idColIndex);
                cards.add(new Card(number, cvv, expiration, id));
                Log.d("test", number.toString() + "/" + cvv.toString() + "/" + expiration.toString());
            }while (cursor.moveToNext());

        }else
            cards.add(new Card("23","32","23","null"));
        cursor.close();
        dBhelper.close();
    }
    static class DBhelper extends SQLiteOpenHelper {

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

    public void AddCard(View v) {
        Intent AddCard = new Intent(CardListActivity.this, MainActivity.class);
        startActivity(AddCard);
        finishAffinity();
    }
    public void Delete(View v) {
        SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("DBcard", null, null, null, null, null, null);
        sqLiteDatabase.delete("DBcard", null, null);
        cursor.close();
        dBhelper.close();
        rv.getAdapter().notifyDataSetChanged();
    }
}