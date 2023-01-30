package id.yozi.may_wallet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.yozi.may_wallet.Login;
import id.yozi.may_wallet.R;
import id.yozi.may_wallet.dialog.TopupDialog;
import id.yozi.may_wallet.model.BankModel;
import id.yozi.may_wallet.pages.Dashboard;
import id.yozi.may_wallet.pages.Topup;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.ViewHolder> {

    private List<BankModel> mData;
    private LayoutInflater inflater;
    private Context context;
    private Intent intent;

    public BankAdapter(List<BankModel> mData, Context context, Intent intent) {
        this.mData = mData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.intent = intent;
    }

    @NonNull
    @Override
    public BankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_topup, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindData(mData.get(position));

        holder.layoutBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = intent.getStringExtra("email");
                String bank = mData.get(position).getTextBank();
                TopupDialog dialog = new TopupDialog();
                dialog.showDialog((Activity) v.getContext(), email, bank);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<BankModel> list) {
        mData = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBank;
        TextView textBank;
        LinearLayout layoutBank;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            imageBank = itemView.findViewById(R.id.imageBank);
            textBank = itemView.findViewById(R.id.txBank);
            layoutBank = itemView.findViewById(R.id.layoutBank);
        }

        public void bindData(BankModel model) {
            imageBank.setImageResource(model.getImageBank());
            textBank.setText(model.getTextBank());
        }
    }

}
