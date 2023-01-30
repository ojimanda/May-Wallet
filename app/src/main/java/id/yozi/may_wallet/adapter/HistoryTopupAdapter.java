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
import id.yozi.may_wallet.model.HistoryTopupModel;
import id.yozi.may_wallet.pages.Dashboard;
import id.yozi.may_wallet.pages.Topup;

public class HistoryTopupAdapter extends RecyclerView.Adapter<HistoryTopupAdapter.ViewHolder> {

    private List<HistoryTopupModel> mData;
    private LayoutInflater inflater;
    private Context context;

    public HistoryTopupAdapter(List<HistoryTopupModel> mData, Context context) {
        this.mData = mData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryTopupAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_history_topup, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTopupAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<HistoryTopupModel> list) {
        mData = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bank, rekening, amount, tanggal;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            bank = itemView.findViewById(R.id.textBank);
            rekening = itemView.findViewById(R.id.textRekening);
            amount = itemView.findViewById(R.id.textAmount);
            tanggal = itemView.findViewById(R.id.dateTopup);

        }

        public void bindData(HistoryTopupModel model) {

            bank.setText(model.getBank());
            rekening.setText(model.getRekening());
            amount.setText(model.getAmount());
            tanggal.setText(model.getTanggal());

        }
    }

}
