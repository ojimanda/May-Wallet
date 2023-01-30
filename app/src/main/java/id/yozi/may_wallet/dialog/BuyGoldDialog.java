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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import id.yozi.may_wallet.Login;
import id.yozi.may_wallet.R;
import id.yozi.may_wallet.pages.Dashboard;
import id.yozi.may_wallet.pages.Transfer;

public class BuyGoldDialog {

    EditText etGoldBuy;
    TextView buyBtn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    public void showDialog(Activity activity, String email) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_buygold);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        etGoldBuy = dialog.findViewById(R.id.etGoldBuy);
        buyBtn = dialog.findViewById(R.id.buyBtn);

        progressDialog = new ProgressDialog(activity);
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Buy Gold...");

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String getBuyGold = etGoldBuy.getText().toString();

                if(getBuyGold.equals("")) {
                    etGoldBuy.setError("Please input your gold");
                } else {
                    progressDialog.show();

                    db.collection("users")
                            .document(email)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()) {

                                        DocumentSnapshot document = task.getResult();
                                        String oldGold = document.get("gold").toString();
                                        String newGold = String.valueOf(Double.parseDouble(oldGold) + Double.parseDouble(getBuyGold));

                                        Integer costBuy = (int) (1000000 * Double.parseDouble(getBuyGold));

                                        Integer newSaldo = Integer.parseInt(document.get("saldo").toString()) - costBuy;

                                        if(newSaldo < 0) {
                                            progressDialog.dismiss();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                            builder.setTitle("ERROR");
                                            builder.setMessage("Insufficient amount");
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

                                            Map<String, String> buyGold = new HashMap<>();
                                            Date date = new Date();
                                            buyGold.put("tanggal", date.toString());
                                            buyGold.put("hal", "buy");
                                            buyGold.put("jumlah", getBuyGold);
                                            buyGold.put("cost", costBuy.toString());

                                            db.collection("users")
                                                    .document(email)
                                                    .collection("gold")
                                                    .add(buyGold)
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
                                                                            builder.setMessage("Success buy gold");
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
                                                                            builder.setMessage("Gagal membeli gold");
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
                                                            Toast.makeText(activity, "Gagal membeli emas", Toast.LENGTH_SHORT).show();
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
