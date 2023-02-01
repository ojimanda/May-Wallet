package id.yozi.may_wallet.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import id.yozi.may_wallet.R;

public class SliderAdapterExample extends SliderViewAdapter<SliderAdapterExample.SliderViewHolder>{
    private int images[];
    private String text[];
    public SliderAdapterExample(int[] images, String[] text) {
        this.images = images;
        this.text = text;
    }
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item_layout,null);
        return new SliderViewHolder(view);
    }
    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        viewHolder.imageView.setImageResource(images[position]);
        viewHolder.textView.setText(text[position]);
    }
    @Override
    public int getCount() {
        return images.length;
    }
    public class SliderViewHolder extends ViewHolder {
        private  ImageView imageView;
        private TextView textView;
        public SliderViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            textView=itemView.findViewById(R.id.textdescription);
        }
    }
}