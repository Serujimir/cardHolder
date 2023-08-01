package ru.serujimir.cardholder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity implements DataChanged{

    ArrayList<Card> cards = new ArrayList<Card>();
    RecyclerView rv;

    DBhelper dBhelper;

    CardAdapter cardAdapter;

    SQLiteDatabase sqLiteDatabase;
    DataChanged dataChanged;
    ProgressDialog progressDialog;
    Context context0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_list);
        init();
    }


    public void init() {
        context0 = getApplicationContext();
        dBhelper = new DBhelper(this);
        setInitionalData();
        rv = findViewById(R.id.cardRV);
        cardAdapter = new CardAdapter(this, cards);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(cardAdapter);
        sqLiteDatabase = dBhelper.getReadableDatabase();
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
            return;
        cursor.close();
        dBhelper.close();
    }


    public void AddCard(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this );
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        builder.setView(view);

        EditText edCardNumber = (EditText) view.findViewById(R.id.edCardNumber);
        EditText edCardCVVcode = (EditText) view.findViewById(R.id.edCardCVVcode);
        EditText edCardExpirationDate = (EditText) view.findViewById(R.id.edCardExpirationDate);

        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        AlertDialog alertDialog = builder.create();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CardListActivity.this);
                progressDialog.setTitle("In progress...");
                progressDialog.show();
                ContentValues contentValues = new ContentValues();
                SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();

                String number = edCardNumber.getText().toString();
                String cvv = edCardCVVcode.getText().toString();
                String expiration = edCardExpirationDate.getText().toString();

                contentValues.put("number", number);
                contentValues.put("cvv", cvv);
                contentValues.put("expiration", expiration);
                sqLiteDatabase.insert("DBcard", null, contentValues);
                alertDialog.dismiss();
                progressDialog.dismiss();
                cardAdapter.Update();
            }
        });
        alertDialog.show();
    }

    @Override
    public void Deleted() {

    }

    @Override
    public void Changed() {

    }

    @Override
    public void Added() {

    }

    public void Restart() {
        Intent intent = new Intent(context0 ,CardListActivity.class);
        getApplicationContext().startActivity(intent);
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
}