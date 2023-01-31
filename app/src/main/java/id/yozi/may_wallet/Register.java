package id.yozi.may_wallet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Register extends AppCompatActivity {

    Button btRegister;
    EditText username, email, password, confPassword;
    ImageButton btBack;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btRegister = findViewById(R.id.btRegister);
        username = findViewById(R.id.etRegisterUsername);
        email = findViewById(R.id.etRegisterEmail);
        password = findViewById(R.id.etRegisterPassword);
        confPassword = findViewById(R.id.etRegisterConfirmPassword);
        btBack = findViewById(R.id.btRegisterBack);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Mohon Menunggu");
        progressDialog.setMessage("Menyimpan data");

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUsername = username.getText().toString();
                String getEmail = email.getText().toString();
                String getPassword = password.getText().toString();
                String getConfPassword = confPassword.getText().toString();

                if(getUsername.equals("")) {
                    username.setError("Username cannot be empty");
                } else if(getEmail.equals("")) {
                    email.setError("Email cannot be empty");
                } else if(getPassword.equals("")) {
                    password.setError("Password cannot be empty");
                } else if(getConfPassword.equals("")) {
                    confPassword.setError("Confirmation password cannot be empty");
                } else {
                    if(getUsername.length() < 6 || getUsername.length() > 15) {
                        username.setError("Username length must be around 6 to 15 character");
                    } else if(!utils.emailValidation(getEmail)) {
                        email.setError("Email not valid");
                    } else if(!utils.passwordValidation(getPassword)) {
                        password.setError("Password not valid");
                    } else if(!getConfPassword.equals(getPassword)) {
                        confPassword.setError("Confirm password must me same with password");
                    } else {
                        try {
                            saveData(getUsername, getEmail, getPassword);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void saveData(String username, String email, String password) throws NoSuchAlgorithmException {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, String> user =new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("password", utils.hashPassword(password));
        user.put("saldo", String.valueOf(0));
        user.put("role", "user");
        user.put("gold", String.valueOf(0));

        Map<String, String> userQR = new HashMap<>();
        userQR.put("email", email);
        userQR.put("username", username);

        progressDialog.show();
        db.collection("users").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            QuerySnapshot res = task.getResult();
                            List<DocumentSnapshot> document = res.getDocuments();
                            List<DocumentSnapshot> getDoc = new ArrayList<>();
                            for(DocumentSnapshot doc : document) {
                                if(doc.get("email").equals(email)) {
                                    getDoc.add(doc);
                                }
                            }
                            if(getDoc.size() == 0) {
                                db.collection("users")
                                        .document(email).set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                progressDialog.dismiss();

                                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                                builder.setMessage("Success Registered account");
                                                builder.setTitle("Success");
                                                builder.setCancelable(true);
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                startActivity(new Intent(Register.this, Login.class));
                                                finish();
                                                }
                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                                }
                                                }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                                progressDialog.dismiss();
                                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                                builder.setTitle("Fail");
                                                builder.setMessage("Error Registered account");
                                                builder.setCancelable(true);
                                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                                }
                                                });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                                }
                                                });
                            } else {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setTitle("Fail");
                                builder.setMessage("Email has been registered. Please use another email");
                                builder.setCancelable(true);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, "ERROR APP", Toast.LENGTH_SHORT).show();
                    }
                });



    }

}