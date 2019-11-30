package com.example.orderspot_general.Main;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderspot_general.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MenulistAdapter extends RecyclerView.Adapter<MenulistAdapter.ViewHolder>{

    private List<Menulist> items; // = new ArrayList<>();

    public MenulistAdapter(List<Menulist> items){
        this.items = items;
    }


    @NonNull
    @Override
    public MenulistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MenulistAdapter.ViewHolder viewHolder, final int position) {

        final Menulist item = items.get(position);
        viewHolder.mname.setText(item.getMenuName());
        viewHolder.mprice.setText(item.getMenuPrice());
        viewHolder.mprod.setText(item.getProductid());

        //checkBox
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.checkBox.setSelected(item.isSelected());

        //
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    item.setSelected(true);
                } else {
                    item.setSelected(false);
                }
            }
        });
        viewHolder.checkBox.setChecked(item.isSelected());

        //picasso 이미지 불러오기
        String imageUrl = item.getMenuImage();
        Picasso.get().load(imageUrl).into(viewHolder.itemImage);


        // 이미지 클릭했을때
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), ViewActivity.class);
                it.putExtra("img", items.get(position).getMenuImage());
                v.getContext().startActivity(it);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Menulist> items){
        this.items = items;
    }

class ViewHolder extends RecyclerView.ViewHolder {
    public ImageView itemImage;
    public TextView mname;
    public TextView mprice;
    public TextView mprod;
    public CheckBox checkBox;

    public ViewHolder(View itemView) {
        super(itemView);

        itemImage = itemView.findViewById(R.id.itemImage);
        mname = itemView.findViewById(R.id.mname);
        mprice = itemView.findViewById(R.id.mprice);
        mprod = itemView.findViewById(R.id.mprod);
        checkBox = itemView.findViewById(R.id.checkbox1);

    }
}

    public  List<Menulist> getMenulist() {
        return items;
    }

}