package id.yozi.may_wallet.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import id.yozi.may_wallet.Fragment.HistoryFragment;
import id.yozi.may_wallet.Fragment.MainFragment;
import id.yozi.may_wallet.Fragment.ProfileFragment;
import id.yozi.may_wallet.Login;
import id.yozi.may_wallet.R;
import id.yozi.may_wallet.dialog.ConfirmationDialog;

public class Dashboard extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView btHamburger;
    String email;
    LinearLayout btnLogout;
    TextView txHeader;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent getIntent = getIntent();
        email = getIntent.getStringExtra("email");
        setFragment(new MainFragment());



        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_view);
        btHamburger = findViewById(R.id.btHamburger);
        btnLogout = findViewById(R.id.layoutLogout);


        View headerView = navigationView.getHeaderView(0);
        txHeader = headerView.findViewById(R.id.headerName);

        db.collection("users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String username = task.getResult().get("username").toString();
                            txHeader.setText("Welcome, "+ username.toUpperCase());
                        }
                    }
                });

        btHamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.START)){
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    drawerLayout.closeDrawer(GravityCompat.END);
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(GravityCompat.START);
                ConfirmationDialog dialog = new ConfirmationDialog();
                dialog.showDialog(Dashboard.this, Login.class, "LOG OUT", "", "");
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        setFragment(new MainFragment());
                        break;
                    case R.id.profile:
                        setFragment(new ProfileFragment());
                        break;
                    case R.id.history:
                        setFragment(new HistoryFragment());
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                item.setChecked(true);
                return true;
            }
        });


    }
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    public String getEmail() {
        return email;
    }
}