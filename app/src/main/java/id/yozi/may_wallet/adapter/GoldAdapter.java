package id.yozi.may_wallet.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import id.yozi.may_wallet.R;
import id.yozi.may_wallet.model.BankModel;
import id.yozi.may_wallet.model.GoldModel;

public class GoldAdapter extends RecyclerView.Adapter<GoldAdapter.ViewHolder> {

    private List<GoldModel> list;
    private Context context;
    private LayoutInflater inflater;


    public GoldAdapter(List<GoldModel> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public GoldAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.layout_gold_history, parent, false);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GoldAdapter.ViewHolder holder, int position) {
        holder.bindData(list.get(position));

        String getHal = holder.hal.getText().toString();

        if(getHal.equals("BUY")) {
            holder.layoutHistoryGold.setBackgroundColor(Color.parseColor("#42ba96"));
        } else {
            holder.layoutHistoryGold.setBackgroundColor(Color.parseColor("#df4759"));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(List<GoldModel> data) {
        list = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hal, qty, cost, date;
        LinearLayout layoutHistoryGold;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            hal = itemView.findViewById(R.id.textHalGold);
            qty = itemView.findViewById(R.id.goldQty);
            cost = itemView.findViewById(R.id.goldCost);
            date = itemView.findViewById(R.id.dateGold);
            layoutHistoryGold = itemView.findViewById(R.id.layoutHistoryGold);
        }

        public void bindData(GoldModel model) {

            hal.setText(model.getHal());
            qty.setText(model.getTotal());
            cost.setText(model.getCost());
            date.setText(model.getDate());

        }
    }

}
