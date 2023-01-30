package id.yozi.may_wallet.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import id.yozi.may_wallet.R;
import id.yozi.may_wallet.adapter.GoldAdapter;
import id.yozi.may_wallet.dialog.BuyGoldDialog;
import id.yozi.may_wallet.dialog.SellGoldDialog;
import id.yozi.may_wallet.model.GoldModel;

public class Gold extends AppCompatActivity {

    TextView emasNow, emasUser;
    Button buy, sell;
    ImageButton btnBack;
    List<GoldModel> element;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gold);

        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");
        System.out.println(email);

        btnBack = findViewById(R.id.btnGoldBack);
        emasNow = findViewById(R.id.emasNow);
        emasUser = findViewById(R.id.emasUser);
        buy = findViewById(R.id.btnBuyGold);
        sell = findViewById(R.id.btnSellGold);

        db.collection("users")
                        .document(email)
                                .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    String goldUser = document.get("gold").toString();
                                                    emasUser.setText(goldUser);
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Gold.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                });


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyGoldDialog dialog = new BuyGoldDialog();
                dialog.showDialog(Gold.this, email);
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SellGoldDialog dialog = new SellGoldDialog();
                dialog.showDialog(Gold.this, email);
            }
        });




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Gold.this, Dashboard.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

        db.collection("users")
                .document(email)
                .collection("gold")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();
                            element = new ArrayList<>();
                            if (documents.size() != 0) {
                                for (DocumentSnapshot document : documents) {
                                    String hal = document.get("hal").toString().toUpperCase();
                                    String qty = document.get("jumlah").toString();
                                    String cost = document.get("cost").toString();
                                    String date = document.get("tanggal").toString();
                                    System.out.println(hal+ " "+ qty+ " "+cost + " "+ date);
                                    element.add(new GoldModel(cost, qty, date, hal));
                                }
                            }
                            GoldAdapter adapter = new GoldAdapter(element, Gold.this);
                            RecyclerView recyclerView = findViewById(R.id.rv_historyGold);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Gold.this));
                            recyclerView.setAdapter(adapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}