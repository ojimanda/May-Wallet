package id.yozi.may_wallet.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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

import id.yozi.may_wallet.Login;
import id.yozi.may_wallet.R;
import id.yozi.may_wallet.dialog.ConfirmationDialog;
import id.yozi.may_wallet.dialog.PenerimaDialog;

public class Transfer extends AppCompatActivity {

    Button send, cekPenerima;
    EditText etEmail, etNominal;
    ImageButton btnBack;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        btnBack = findViewById(R.id.btnTransferBack);
        send = findViewById(R.id.btnTransfer);
        cekPenerima = findViewById(R.id.cekPenerima);
        etEmail = findViewById(R.id.emailTransfer);
        etNominal = findViewById(R.id.nominalTransfer);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Transfer.this, Dashboard.class);
                intent1.putExtra("email", email);
                startActivity(intent1);
                finish();
            }
        });

        cekPenerima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmailPenerima = etEmail.getText().toString();

                if(getEmailPenerima.equals("")) {
                    etEmail.setError("Fill your email transfer");
                } else {

                    if(email.equals(getEmailPenerima)) {
                        etEmail.setError("Cannot send to own account");
                    } else {
                        PenerimaDialog dialog = new PenerimaDialog();
                        dialog.showDialog(Transfer.this, getEmailPenerima);
                    }

                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmailPenerima = etEmail.getText().toString();
                String getNominal = etNominal.getText().toString();
                if(email.equals(getEmailPenerima)) {
                    etEmail.equals("Cannot send to own account");
                } else {
                    if(getEmailPenerima.equals("")) {
                        etEmail.setError("Fill your email transfer");
                    } else if(getNominal.equals("")) {
                        etNominal.setError("Fill your amount transfer");
                    } else {

                        if(Integer.parseInt(getNominal) < 10000) {
                            etNominal.setError("Minimum transfer is Rp. 10.000,-");
                        } else {
                            progressDialog = new ProgressDialog(Transfer.this);
                            progressDialog.setTitle("Mohon Menunggu");
                            progressDialog.setMessage("Send Amount...");

                            progressDialog.show();
                            db.collection("users")
                                    .document(email)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()) {
                                                String oldSaldoUser = task.getResult().get("saldo").toString();
                                                String newSaldoUser = String.valueOf(Integer.parseInt(oldSaldoUser) - Integer.parseInt(getNominal));
                                                if(Integer.parseInt(newSaldoUser) < 0) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Transfer.this);
                                                    builder.setTitle("ERROR");
                                                    builder.setMessage("Please check your amount. Cannot transfer more than your amount.");
                                                    builder.setCancelable(true);
                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                } else {
                                                    db.collection("users")
                                                            .document(getEmailPenerima)
                                                            .get()
                                                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                    if(task.isSuccessful()) {
                                                                        if(task.getResult().get("email") == null) {
                                                                            progressDialog.dismiss();
                                                                            etEmail.setError("Please input correct email transfer");
                                                                        } else {
                                                                            String oldSaldo = task.getResult().get("saldo").toString();
                                                                            String newSaldo = String.valueOf(Integer.parseInt(oldSaldo) + Integer.parseInt(getNominal));
                                                                            db.collection("users")
                                                                                    .document(getEmailPenerima)
                                                                                    .update("saldo", newSaldo)
                                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                        @Override
                                                                                        public void onSuccess(Void unused) {

                                                                                            db.collection("users")
                                                                                                            .document(email)
                                                                                                                    .update("saldo", newSaldoUser)
                                                                                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                                                                @Override
                                                                                                                                public void onSuccess(Void unused) {
                                                                                                                                    progressDialog.dismiss();
                                                                                                                                    Map<String, String> addTransfer = new HashMap<>();
                                                                                                                                    Date date = new Date();
                                                                                                                                    addTransfer.put("tanggalTerima", date.toString());
                                                                                                                                    addTransfer.put("penerima", getEmailPenerima);
                                                                                                                                    addTransfer.put("user", email);
                                                                                                                                    addTransfer.put("hal", "transfer");
                                                                                                                                    addTransfer.put("jumlah", getNominal);

                                                                                                                                    db.collection("transaction")
                                                                                                                                            .add(addTransfer)
                                                                                                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                                                                                @Override
                                                                                                                                                public void onSuccess(DocumentReference documentReference) {
                                                                                                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Transfer.this);
                                                                                                                                                    builder.setTitle("SUCCESS");
                                                                                                                                                    builder.setMessage("Success transfer amount");
                                                                                                                                                    builder.setCancelable(true);
                                                                                                                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                                                                                        @Override
                                                                                                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                                                                                                            Intent newIntent = new Intent(Transfer.this, Dashboard.class);
                                                                                                                                                            newIntent.putExtra("email", email);
                                                                                                                                                            startActivity(newIntent);
                                                                                                                                                            finish();
                                                                                                                                                        }
                                                                                                                                                    });
                                                                                                                                                    AlertDialog alert = builder.create();
                                                                                                                                                    alert.show();
                                                                                                                                                }
                                                                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                                                                                @Override
                                                                                                                                                public void onFailure(@NonNull Exception e) {
                                                                                                                                                    Toast.makeText(Transfer.this, "Cannot add transaction", Toast.LENGTH_SHORT).show();
                                                                                                                                                }
                                                                                                                                            });
                                                                                                                                }
                                                                                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                                                        @Override
                                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                                            progressDialog.dismiss();
                                                                                                            Toast.makeText(Transfer.this, "Error while update user amount", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    });

                                                                                            Toast.makeText(Transfer.this, "Success Transfer", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                                        @Override
                                                                                        public void onFailure(@NonNull Exception e) {
                                                                                            progressDialog.dismiss();
                                                                                            Toast.makeText(Transfer.this, "Error while update penerima amount", Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                        }
                                                                    }
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(Transfer.this, "Cannot get email penerima", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Transfer.this, "Cannot get user email", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                    }
                }
            }
        });

    }
}