package id.yozi.may_wallet.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import id.yozi.may_wallet.R;

public class PenerimaDialog {

    TextView txUsername, txEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void showDialog(Activity activity, String email) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.custom_dialog_detail);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txEmail = dialog.findViewById(R.id.txEmailDetail);
        txUsername = dialog.findViewById(R.id.txUsernameDetail);

        db.collection("users")
                        .whereEqualTo("email", email)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().getDocuments().size() == 0) {
                                Toast.makeText(activity, "User not found", Toast.LENGTH_SHORT).show();
                            } else {
                                DocumentSnapshot result = task.getResult().getDocuments().get(0);
                                String getEmail = result.get("email").toString();
                                String getUsername = result.get("username").toString();
                                txEmail.setText(getEmail);
                                txUsername.setText(getUsername);
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        txEmail.setText("---");
                        txUsername.setText("---");
                    }
                });

        dialog.show();

    }
}
