package id.yozi.may_wallet.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import id.yozi.may_wallet.R;
import id.yozi.may_wallet.adapter.SliderAdapterExample;
import id.yozi.may_wallet.pages.Dashboard;
import id.yozi.may_wallet.pages.Gold;
import id.yozi.may_wallet.pages.Transfer;
import id.yozi.may_wallet.pages.Topup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout topup, transfer, electricity, gold;
    private TextView txAmount;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // slider
    private  int images[];
    private  String text[];
    private SliderAdapterExample adapter;
    private SliderView sliderView;


    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        topup = view.findViewById(R.id.top_up);
        transfer = view.findViewById(R.id.transfer);
        electricity = view.findViewById(R.id.electricity);
        gold = view.findViewById(R.id.gold);
        txAmount = view.findViewById(R.id.textAmount);

        Dashboard dashboard =(Dashboard) getActivity();
        assert dashboard != null;
        String email = dashboard.getEmail();


        sliderView = view.findViewById(R.id.imageSlider);
        images = new int[] {R.drawable.promoone, R.drawable.promotwo, R.drawable.promothree, R.drawable.promofour};
        text = new String[] {"", "", "", ""};
        adapter = new SliderAdapterExample(images, text);
        sliderView.setSliderAdapter(adapter);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
        sliderView.startAutoCycle();


        db.collection("users")
                        .whereEqualTo("email", email)
                                .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if(task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                                    txAmount.setText("Rp. "+document.get("saldo").toString());
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Cannot get user", Toast.LENGTH_SHORT).show();
                    }
                });



        topup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Topup.class);
                intent.putExtra("email", email);
                startActivity(intent);
                getActivity().finish();
            }
        });

        transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Transfer.class);
                intent.putExtra("email", email);
                startActivity(intent);
                getActivity().finish();
            }
        });

        gold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Gold.class);
                intent.putExtra("email", email);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }
}