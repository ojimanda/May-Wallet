package id.yozi.may_wallet.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import id.yozi.may_wallet.adapter.HistoryTopupAdapter;
import id.yozi.may_wallet.adapter.HistoryTransferAdapter;
import id.yozi.may_wallet.model.HistoryTransferModel;
import id.yozi.may_wallet.pages.Dashboard;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryTransferFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryTransferFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<HistoryTransferModel> element;


    public HistoryTransferFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryTransferFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryTransferFragment newInstance(String param1, String param2) {
        HistoryTransferFragment fragment = new HistoryTransferFragment();
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
        View view = inflater.inflate(R.layout.fragment_history_transfer, container, false);

        Dashboard dashboard = (Dashboard) getActivity();
        assert dashboard != null;
        String email = dashboard.getEmail();

        db.collection("transaction")
                .whereEqualTo("hal", "transfer")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> list = task.getResult().getDocuments();
                            element = new ArrayList<>();

                            for(DocumentSnapshot document : list) {

                                String amount = document.get("jumlah").toString();
                                String date = document.get("tanggalTerima").toString();

                                if(document.get("penerima").toString().equals(email)) {
                                    String pengirimOrPenerima = "Pengirim";
                                    String e_mail = document.get("user").toString();
                                    element.add(new HistoryTransferModel(pengirimOrPenerima, e_mail, date, amount));

                                } else if(document.get("user").toString().equals(email)) {

                                    String pengirimOrPenerima = "Penerima";
                                    String e_mail = document.get("penerima").toString();
                                    element.add(new HistoryTransferModel(pengirimOrPenerima, e_mail, date, amount));
                                }

                            }

                            HistoryTransferAdapter adapter = new HistoryTransferAdapter(element, getContext());
                            RecyclerView recyclerView = getActivity().findViewById(R.id.rv_transfer_history);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerView.setAdapter(adapter);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Gagal menampilkan data", Toast.LENGTH_SHORT).show();
                    }
                });

        return view;
    }
}