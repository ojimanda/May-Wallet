package id.yozi.may_wallet.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import id.yozi.may_wallet.R;
import id.yozi.may_wallet.pages.Dashboard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ImageView qrUser;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txUsername, txEmail, txSaldo, txGold;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        Dashboard dashboard =(Dashboard) getActivity();
        assert dashboard != null;
        String email = dashboard.getEmail();

        qrUser = view.findViewById(R.id.qrUser);
        txEmail = view.findViewById(R.id.txProfileEmail);
        txGold = view.findViewById(R.id.txProfileGold);
        txUsername = view.findViewById(R.id.txProfileUsername);
        txSaldo = view.findViewById(R.id.txProfileSaldo);

        db.collection("users")
                .document(email)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            Object document = task.getResult().getData();

                            txEmail.setText(task.getResult().get("email").toString());
                            txGold.setText(task.getResult().get("gold").toString()+ " kg");
                            txUsername.setText(task.getResult().get("username").toString());
                            txSaldo.setText("Rp. "+task.getResult().get("saldo").toString());

                            MultiFormatWriter formatWriter = new MultiFormatWriter();

                            try {
                                BitMatrix bitMatrix = formatWriter.encode(document.toString(), BarcodeFormat.QR_CODE, 400, 400);
                                BarcodeEncoder encoder = new BarcodeEncoder();
                                Bitmap bitmap = encoder.createBitmap(bitMatrix);

                                qrUser.setImageBitmap(bitmap);

                            } catch (WriterException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "User Not Found", Toast.LENGTH_SHORT).show();
                    }
                });


        return view;
    }
}