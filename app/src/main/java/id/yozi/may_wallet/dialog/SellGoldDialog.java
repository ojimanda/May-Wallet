package id.yozi.may_wallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.yozi.may_wallet.R;

public class SellGoldDialog {

    EditText etSellGold;
    TextView btnSell;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;


    public void showDialog(Activity activity, String email) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_sellgold);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etSellGold = dialog.findViewById(R.id.etGoldSell);
        btnSell = dialog.findViewById(R.id.sellBtn);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Sell Gold...");

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getSellGold = etSellGold.getText().toString();

                progressDialog.show();

                if(getSellGold.equals("")) {
                    etSellGold.setError("Please input your gold");
                } else {
                    db.collection("users")
                            .document(email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if(task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        String oldGold = document.get("gold").toString();
                                        String oldSaldo = document.get("saldo").toString();

                                        String newGold = String.valueOf(Double.parseDouble(oldGold) - Double.parseDouble(getSellGold));
                                        Integer costSell = (int) (1000000 * Double.parseDouble(getSellGold));

                                        Integer newSaldo = Integer.parseInt(oldSaldo) + costSell;


                                        if(Double.parseDouble(newGold) < 0) {

                                            progressDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                            builder.setTitle("ERROR");
                                            builder.setMessage("Insufficient gold");
                                            builder.setCancelable(true);
                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                    Intent intent = activity.getIntent();
                                                    intent.putExtra("email", email);
                                                    activity.startActivity(intent);
                                                }
                                            });
                                            AlertDialog alert = builder.create();
                                            alert.show();

                                        } else {

                                            Map<String, String> sellGold = new HashMap<>();

                                            Date date = new Date();
                                            sellGold.put("tanggal", date.toString());
                                            sellGold.put("hal", "sell");
                                            sellGold.put("jumlah", getSellGold);
                                            sellGold.put("cost", costSell.toString());

                                            db.collection("users")
                                                    .document(email)
                                                    .collection("gold")
                                                    .add(sellGold)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {

                                                            db.collection("users")
                                                                    .document(email)
                                                                    .update("saldo", newSaldo,
                                                                            "gold", newGold)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            progressDialog.dismiss();
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                                                            builder.setTitle("SUCCESS");
                                                                            builder.setMessage("Success sell gold");
                                                                            builder.setCancelable(true);
                                                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                    Intent intent = activity.getIntent();
                                                                                    intent.putExtra("email", email);
                                                                                    activity.startActivity(intent);
                                                                                }
                                                                            });
                                                                            AlertDialog alert = builder.create();
                                                                            alert.show();

                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            progressDialog.dismiss();
                                                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                                                            builder.setTitle("ERROR");
                                                                            builder.setMessage("Gagal menjual gold");
                                                                            builder.setCancelable(true);
                                                                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    dialog.dismiss();
                                                                                    Intent intent = activity.getIntent();
                                                                                    intent.putExtra("email", email);
                                                                                    activity.startActivity(intent);
                                                                                }
                                                                            });
                                                                            AlertDialog alert = builder.create();
                                                                            alert.show();

                                                                        }
                                                                    });

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(activity, "Gagal menjual emas", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });

                                        }

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(activity, "ERROR", Toast.LENGTH_SHORT).show();
                                }
                            });
                }


            }
        });


        dialog.show();

    }

}
