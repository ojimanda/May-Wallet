package id.yozi.may_wallet.pages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.yozi.may_wallet.R;
import id.yozi.may_wallet.adapter.BankAdapter;
import id.yozi.may_wallet.model.BankModel;

public class Topup extends AppCompatActivity {

    private List<BankModel> element;
    ImageButton btBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        btBack = findViewById(R.id.btTopupBack);


        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra("email");

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Topup.this, Dashboard.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

        element = new ArrayList<>();
        element.add(new BankModel(R.drawable.gold, "Bank Rakyat Indonesia"));
        element.add(new BankModel(R.drawable.gold, "Bank Central Asia"));
        element.add(new BankModel(R.drawable.gold, "Bank Maybank Indonesia"));
        element.add(new BankModel(R.drawable.gold, "Bank Nasional Indonesia"));


        BankAdapter adapter = new BankAdapter(element, this, getIntent);
        RecyclerView recyclerView =findViewById(R.id.rv_topup);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}