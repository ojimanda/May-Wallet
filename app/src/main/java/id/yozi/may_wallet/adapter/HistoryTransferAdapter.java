package id.yozi.may_wallet.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import id.yozi.may_wallet.model.HistoryTransferModel;
import id.yozi.may_wallet.pages.Dashboard;
import id.yozi.may_wallet.pages.Topup;

public class HistoryTransferAdapter extends RecyclerView.Adapter<HistoryTransferAdapter.ViewHolder> {

    private List<HistoryTransferModel> mData;
    private LayoutInflater inflater;
    private Context context;

    public HistoryTransferAdapter(List<HistoryTransferModel> mData, Context context) {
        this.mData = mData;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public HistoryTransferAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_history_transfer, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryTransferAdapter.ViewHolder holder, int position) {
        holder.bindData(mData.get(position));

        if(holder.pengirimOrPenerima.getText().equals("Pengirim")) {
            holder.layoutTransfer.setBackgroundColor(Color.parseColor("#42ba96"));
        } else {
            holder.layoutTransfer.setBackgroundColor(Color.parseColor("#df4759"));
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setItems(List<HistoryTransferModel> list) {
        mData = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pengirimOrPenerima, e_mail, tanggal, amount;
        LinearLayout layoutTransfer;


        public ViewHolder(@NonNull View itemView){
            super(itemView);

            pengirimOrPenerima = itemView.findViewById(R.id.textEmail);
            e_mail = itemView.findViewById(R.id.textPenerimaTransfer);
            tanggal = itemView.findViewById(R.id.dateTransfer);
            amount = itemView.findViewById(R.id.textAmountTransfer);
            layoutTransfer = itemView.findViewById(R.id.layoutHistoryTransfer);

        }

        public void bindData(HistoryTransferModel model) {

            pengirimOrPenerima.setText(model.getPengirimOrPenerima());
            e_mail.setText(model.getE_mail());
            tanggal.setText(model.getTanggal());
            amount.setText(model.getAmount());

        }
    }

}
