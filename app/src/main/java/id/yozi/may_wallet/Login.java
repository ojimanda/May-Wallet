package id.yozi.may_wallet;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.NoSuchAlgorithmException;

import id.yozi.may_wallet.pages.Dashboard;

public class Login extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btLogin;
    TextView btRegister;
    ProgressDialog progressDialog;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        btLogin = findViewById(R.id.btLogin);
        btRegister = findViewById(R.id.btLoginRegister);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Memeriksa data");

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = etEmail.getText().toString();
                String getPassword = etPassword.getText().toString();

                if(getEmail.equals("")) {
                    etEmail.setError("Input your email");
                } else if(getPassword.equals("")) {
                    etPassword.setError("Input your password");
                } else {
                    progressDialog.show();
                    try {
                        db.collection("users")
                                .whereEqualTo("email", getEmail)
                                .whereEqualTo("password", utils.hashPassword(getPassword))
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            QuerySnapshot res = task.getResult();
                                            if(res.getDocuments().size() == 0) {
                                                progressDialog.dismiss();
                                                Toast.makeText(Login.this, "Wrong username/password", Toast.LENGTH_SHORT).show();
                                            } else {
                                                DocumentSnapshot document = res.getDocuments().get(0);
                                                String uuid = document.getId();
                                                String email = document.get("email").toString();
                                                String username = document.get("username").toString();
                                                String password = document.get("password").toString();
                                                String role = document.get("role").toString();
                                                if(role.equals("admin")) {
                                                    progressDialog.dismiss();
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                                    builder.setTitle("Login Success");
                                                    builder.setMessage("Hello " + username);
                                                    builder.setCancelable(true);
                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Login.this, Dashboard.class);
                                                            intent.putExtra("username", username);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                } else {
                                                    progressDialog.dismiss();
                                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Login.this);
                                                    builder1.setTitle("Login Success");
                                                    builder1.setMessage("Hello " + username);
                                                    builder1.setCancelable(true);
                                                    builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(Login.this, Dashboard.class);
                                                            intent.putExtra("email", uuid);
                                                            startActivity(intent);
                                                            finish();
                                                        }

                                                    });
                                                    AlertDialog alert = builder1.create();
                                                    alert.show();
                                                }
                                            }
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(Login.this);
                                        builder2.setTitle("Login Fail");
                                        builder2.setMessage("Email/Password anda salah");
                                        builder2.setCancelable(true);
                                        builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                etEmail.setText("");
                                                etPassword.setText("");
                                            }
                                        });
                                        AlertDialog alert = builder2.create();
                                        alert.show();
                                    }
                                });
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}