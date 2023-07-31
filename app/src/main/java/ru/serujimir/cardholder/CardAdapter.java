package ru.serujimir.cardholder;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.zip.Inflater;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final List<Card> cards;
    CardListActivity.DBhelper dBhelper;

    CardAdapter(Context context, List<Card> cards) {
        this.cards=cards;
        this.inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.card_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, int position) {
        Card card = cards.get(position);
        holder.number.setText(card.getNumber());
        holder.cvv.setText("***");
        holder.expiration.setText(card.getExpiration());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());
                ConstraintLayout view = (ConstraintLayout)inflater.inflate(R.layout.card_list_item_click, null);
                builder.setView(view);

                TextView clickNumber = (TextView) view.findViewById(R.id.clickNumber);
                TextView clickCvv = (TextView) view.findViewById(R.id.clickCvv);
                TextView clickExpiration = (TextView) view.findViewById(R.id.clickExpiration);

                clickNumber.setText(card.getNumber());
                clickCvv.setText(card.getCvv());
                clickExpiration.setText(card.getExpiration());
                Button btnDelete = (Button) view.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dBhelper = new CardListActivity.DBhelper(view.getContext());
                        SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();
                        sqLiteDatabase.delete("DBcard", "id = " + card.getId(), null);
                    }
                });

                Button btnEdit = (Button) view.findViewById(R.id.btnEdit);
                btnEdit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dBhelper = new CardListActivity.DBhelper(view.getContext());
                        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();

                        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext() );
                        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.activity_main, null);
                        builder.setView(view);

                        EditText edCardNumber = (EditText) view.findViewById(R.id.edCardNumber);
                        EditText edCardCVVcode = (EditText) view.findViewById(R.id.edCardCVVcode);
                        EditText edCardExpirationDate = (EditText) view.findViewById(R.id.edCardExpirationDate);

                        edCardNumber.setText(card.getNumber());
                        edCardCVVcode.setText(card.getCvv());
                        edCardExpirationDate.setText(card.getExpiration());


                        Button btnEdit = (Button) view.findViewById(R.id.btnAdd);
                        btnEdit.setText("Edit");
                        AlertDialog alertDialog = builder.create();

                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ContentValues contentValues = new ContentValues();
                                SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();

                                String number = edCardNumber.getText().toString();
                                String cvv = edCardCVVcode.getText().toString();
                                String expiration = edCardExpirationDate.getText().toString();

                                contentValues.put("number", number);
                                contentValues.put("cvv", cvv);
                                contentValues.put("expiration", expiration);
                                sqLiteDatabase.update("DBcard", contentValues, "id = " + card.getId(), null);
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView number, cvv, expiration;
        ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.tvNumber);
            cvv = view.findViewById(R.id.tvCvv);
            expiration = view.findViewById(R.id.tvExpiration);
        }
    }
}
