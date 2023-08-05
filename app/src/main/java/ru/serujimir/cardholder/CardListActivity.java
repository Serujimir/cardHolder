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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class CardListActivity extends AppCompatActivity {

    ArrayList<Card> cards = new ArrayList<Card>();
    RecyclerView rv;

    DBhelper dBhelper;

    CardAdapter cardAdapter;

    SQLiteDatabase sqLiteDatabase;
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
        ConstraintLayout view = (ConstraintLayout) getLayoutInflater().inflate(R.layout.add_edit_card, null);
        builder.setView(view);

        EditText edCardNumber1 = (EditText) view.findViewById(R.id.edCardNumber1);
        EditText edCardNumber2 = (EditText) view.findViewById(R.id.edCardNumber2);
        EditText edCardNumber3 = (EditText) view.findViewById(R.id.edCardNumber3);
        EditText edCardNumber4 = (EditText) view.findViewById(R.id.edCardNumber4);

        EditText edCardCVVcode = (EditText) view.findViewById(R.id.edCardCVVcode);

        EditText edCardExpirationDate1 = (EditText) view.findViewById(R.id.edCardExpiration1);
        EditText edCardExpirationDate2 = (EditText) view.findViewById(R.id.edCardExpiration2);

        edCardNumber1.requestFocus();
        edCardNumber1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    edCardNumber2.requestFocus();
                }
            }
        });

        edCardNumber2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    edCardNumber3.requestFocus();
                }
            }
        });

        edCardNumber3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    edCardNumber4.requestFocus();
                }
            }
        });

        edCardNumber4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 4) {
                    edCardCVVcode.requestFocus();
                }
            }
        });

        edCardCVVcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 3) {
                    edCardExpirationDate1.requestFocus();
                }
            }
        });

        edCardExpirationDate1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2) {
                    edCardExpirationDate2.requestFocus();
                }
            }
        });

        edCardExpirationDate2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 2) {

                }
            }
        });



        Button btnAdd = (Button) view.findViewById(R.id.btnAdd);
        AlertDialog alertDialog = builder.create();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                    if(edCardNumber1.length() == 4 && edCardNumber2.length() == 4 && edCardNumber3.length() == 4 && edCardNumber4.length() == 4 && edCardCVVcode.length() == 3 && edCardExpirationDate1.length() == 2 && edCardExpirationDate2.length() == 2) {
                        progressDialog = new ProgressDialog(CardListActivity.this);
                        progressDialog.setTitle("In progress...");
                        progressDialog.show();
                        ContentValues contentValues = new ContentValues();
                        SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();

                        String number = edCardNumber1.getText().toString() + " " + edCardNumber2.getText().toString() + " " + edCardNumber3.getText().toString() + " " + edCardNumber4.getText().toString();
                        String cvv = edCardCVVcode.getText().toString();
                        String expiration = edCardExpirationDate1.getText().toString() + "/" + edCardExpirationDate2.getText().toString();

                        contentValues.put("number", number);
                        contentValues.put("cvv", cvv);
                        contentValues.put("expiration", expiration);
                        sqLiteDatabase.insert("DBcard", null, contentValues);
                        alertDialog.dismiss();
                        progressDialog.dismiss();
                        cardAdapter.Update();
                    }
                    else {
                        Toast.makeText(CardListActivity.this, "Fill all fields!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        
        alertDialog.show();
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void showSoftKeyboard(View view){
        if(view.requestFocus()){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
        }
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