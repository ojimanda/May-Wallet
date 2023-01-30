package id.yozi.may_wallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
import java.util.List;
import java.util.Map;

import id.yozi.may_wallet.R;
import id.yozi.may_wallet.pages.Dashboard;

public class TopupDialog {

    EditText txRekening, txAmount;
    Button topup;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void showDialog(Activity activity, String email, String bank) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_topup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txRekening = dialog.findViewById(R.id.etTopupRekening);
        txAmount = dialog.findViewById(R.id.etTopupAmount);
        topup = dialog.findViewById(R.id.btnTopupAdd);

        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getRekening = txRekening.getText().toString();
                String getAmount = txAmount.getText().toString();

                db.collection("users")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()) {
                                    QuerySnapshot res = task.getResult();
                                    DocumentSnapshot result = res.getDocuments().get(0);
                                    Integer oldAmount = Integer.valueOf(result.get("saldo").toString());
                                    Integer newAmount = oldAmount + Integer.parseInt(getAmount);

                                    db.collection("users")
                                            .document(email)
                                            .update("saldo", newAmount)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Map<String, String> addSaldo = new HashMap<>();
                                                    Date date = new Date();
                                                    addSaldo.put("tanggalTerima", date.toString());
                                                    addSaldo.put("bank", bank);
                                                    addSaldo.put("jumlah", getAmount);
                                                    addSaldo.put("noRekening", getRekening);
                                                    addSaldo.put("user", email);
                                                    addSaldo.put("hal", "topup");

                                                    db.collection("transaction")
                                                            .add(addSaldo)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Toast.makeText(activity, "Success Top up", Toast.LENGTH_SHORT).show();
                                                                    Intent intent = new Intent(activity, Dashboard.class);
                                                                    intent.putExtra("email", email);
                                                                    activity.startActivity(intent);
                                                                    activity.finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(activity, "Error Top up", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(activity, "Error update saldo", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "Error get user", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        dialog.show();

    }
}
