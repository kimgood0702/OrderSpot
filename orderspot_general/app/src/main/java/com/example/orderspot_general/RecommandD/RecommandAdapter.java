package com.example.orderspot_general.RecommandD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.orderspot_general.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecommandAdapter extends RecyclerView.Adapter<RecommandAdapter.ViewHolder> {

    private List<Recommandlist> items; // = new ArrayList<>();

    public RecommandAdapter(List<Recommandlist> items){
        this.items = items;

    }
    @NonNull
    @Override
    public RecommandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu3_item,parent,false);
        RecommandAdapter.ViewHolder viewHolder = new RecommandAdapter.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecommandAdapter.ViewHolder viewHolder, int position) {
        final Recommandlist item = items.get(position);
        viewHolder.mname3.setText(item.getReName());
        viewHolder.mscore3.setText("score : "+item.getReScore());
        //picasso 이미지 불러오기
        String imageUrl = item.getReImage();
        Picasso.get().load(imageUrl).into(viewHolder.itemImage3);


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Recommandlist> items){
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView itemImage3;
        public TextView mname3;
        public TextView mscore3;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage3 = itemView.findViewById(R.id.itemImage3);
            mname3 = itemView.findViewById(R.id.mname3);
            mscore3 = itemView.findViewById(R.id.mscore3);

        }
    }

}
