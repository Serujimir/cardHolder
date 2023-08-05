package ru.serujimir.cardholder;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder>  {

    private final LayoutInflater inflater;
    public final List<Card> cards;
    CardListActivity.DBhelper dBhelper;

    ProgressDialog progressDialog;


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
    public void onBindViewHolder(@NonNull CardAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Card card = cards.get(position);
        holder.number.setText(card.getNumber());
        if(card.getCvv().equals("") || card.getCvv().equals(null)) {
            holder.cvv.setText("YOU delete all cards...");
        }
        else{
            holder.cvv.setText("***");
        }
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
                Button btnEdit = (Button) view.findViewById(R.id.btnEdit);

                AlertDialog alertDialog = builder.create();

                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder1 = new MaterialAlertDialogBuilder(inflater.getContext());
                        builder1.setTitle("Do you wanna delete card?");


                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.dismiss();
                                progressDialog = new ProgressDialog(inflater.getContext());
                                progressDialog.setTitle("In progress...");
                                progressDialog.show();
                                dBhelper = new CardListActivity.DBhelper(view.getContext());
                                SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();
                                sqLiteDatabase.delete("DBcard", "id = " + card.getId(), null);
                                dBhelper = new CardListActivity.DBhelper(inflater.getContext());
                                sqLiteDatabase.close();
                                progressDialog.dismiss();
                                alertDialog.dismiss();
                                Update();
                            }
                        });
                        AlertDialog alertDialog1 = builder1.create();
                        alertDialog1.show();
                    }
                });
                btnEdit.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        dBhelper = new CardListActivity.DBhelper(view.getContext());
                        SQLiteDatabase sqLiteDatabase = dBhelper.getReadableDatabase();

                        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext() );
                        ConstraintLayout view = (ConstraintLayout) inflater.inflate(R.layout.add_edit_card, null);
                        builder.setView(view);

                        EditText edCardNumber1 = (EditText) view.findViewById(R.id.edCardNumber1);
                        EditText edCardNumber2 = (EditText) view.findViewById(R.id.edCardNumber2);
                        EditText edCardNumber3 = (EditText) view.findViewById(R.id.edCardNumber3);
                        EditText edCardNumber4 = (EditText) view.findViewById(R.id.edCardNumber4);

                        EditText edCardCVVcode = (EditText) view.findViewById(R.id.edCardCVVcode);

                        EditText edCardExpirationDate1 = (EditText) view.findViewById(R.id.edCardExpiration1);
                        EditText edCardExpirationDate2 = (EditText) view.findViewById(R.id.edCardExpiration2);

                        edCardNumber1.setText(card.getNumber().substring(0,4));
                        edCardNumber2.setText(card.getNumber().substring(5,10));
                        edCardNumber3.setText(card.getNumber().substring(10,14));
                        edCardNumber4.setText(card.getNumber().substring(15,19));

                        edCardCVVcode.setText(card.getCvv());

                        edCardExpirationDate1.setText(card.getExpiration().substring(0,2));
                        edCardExpirationDate2.setText(card.getExpiration().substring(3,5));


                        Button btnEdit = (Button) view.findViewById(R.id.btnAdd);
                        btnEdit.setText("Edit");
                        AlertDialog alertDialog = builder.create();

                        btnEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(edCardNumber1.length() == 4 && edCardNumber2.length() == 4 && edCardNumber3.length() == 4 && edCardNumber4.length() == 4 && edCardCVVcode.length() == 3 && edCardExpirationDate1.length() == 2 && edCardExpirationDate2.length() == 2)
                                {
                                    AlertDialog.Builder builder2 = new MaterialAlertDialogBuilder(inflater.getContext());
                                    builder2.setTitle("Save Changes?");

                                    builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    });

                                    builder2.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ContentValues contentValues = new ContentValues();
                                            SQLiteDatabase sqLiteDatabase = dBhelper.getWritableDatabase();

                                            String number = edCardNumber1.getText().toString() + " " + edCardNumber2.getText().toString() + " " + edCardNumber3.getText().toString() + " " + edCardNumber4.getText().toString();
                                            String cvv = edCardCVVcode.getText().toString();
                                            String expiration = edCardExpirationDate1.getText().toString() + "/" + edCardExpirationDate2.getText().toString();

                                            contentValues.put("number", number);
                                            contentValues.put("cvv", cvv);
                                            contentValues.put("expiration", expiration);
                                            sqLiteDatabase.update("DBcard", contentValues, "id = " + card.getId(), null);

                                            Update();
                                            edCardNumber1.setText(card.getNumber().substring(0,4));
                                            edCardNumber2.setText(card.getNumber().substring(5,10));
                                            edCardNumber3.setText(card.getNumber().substring(10,14));
                                            edCardNumber4.setText(card.getNumber().substring(15,19));

                                            edCardCVVcode.setText(card.getCvv());

                                            edCardExpirationDate1.setText(card.getExpiration().substring(0,2));
                                            edCardExpirationDate2.setText(card.getExpiration().substring(3,5));


                                            alertDialog.dismiss();
                                        }
                                    });

                                    AlertDialog alertDialog1 = builder2.create();
                                    alertDialog1.show();
                                }
                                else {
                                    Toast.makeText(inflater.getContext(), "Fill all fields!", Toast.LENGTH_SHORT).show();
                                }
                                
                            }
                        });
                        alertDialog.show();
                    }
                });
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

    public void Update() {
        cards.clear();
        dBhelper = new CardListActivity.DBhelper(inflater.getContext());
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

        cursor.close();
        dBhelper.close();
        notifyDataSetChanged();
    }

}
