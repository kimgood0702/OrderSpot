package com.example.orderspot_general.CountD;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.orderspot_general.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckAdapter extends RecyclerView.Adapter<CheckAdapter.ViewHolder> {

    private List<Checklist> items; // = new ArrayList<>();

    Main2Activity m2 = new Main2Activity();

    public CheckAdapter(List<Checklist> items) {
        this.items = items;

    }


    @NonNull
    @Override
    public CheckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu2_item, parent, false);
        CheckAdapter.ViewHolder viewHolder = new CheckAdapter.ViewHolder(itemView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CheckAdapter.ViewHolder viewHolder, final int position) {

        final Checklist item = items.get(position);

        viewHolder.mname.setText(item.getCheckName());
        viewHolder.mprice.setText(item.getCheckPrice());
        viewHolder.productID.setText(item.getProductid());
        //viewHolder.mprod.setText(item.getCheckID());

        //  + 버튼
        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(viewHolder.sumt.getText().toString());
                a = a + 1;
                //Log.e("da", String.valueOf(a));
                viewHolder.sumt.setText(a + "");
                m2.setProductAmount(viewHolder.productID.getText().toString(), a);

            }
        });
        // - 버튼
        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = Integer.parseInt(viewHolder.sumt.getText().toString());
                if (a != 0) a = a - 1;
                viewHolder.sumt.setText(a + "");
                m2.setProductAmount(viewHolder.productID.getText().toString(), a);
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Checklist> items) {
        this.items = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mname;
        public TextView mprice;
        public TextView mprod;
        public Button plus;
        public Button minus;
        public TextView sumt;
        public TextView productID;

        public ViewHolder(View itemView) {
            super(itemView);

            mname = itemView.findViewById(R.id.mname2);
            mprice = itemView.findViewById(R.id.mprice2);
            mprod = itemView.findViewById(R.id.mprod);
            plus = itemView.findViewById(R.id.buttonplus);
            minus = itemView.findViewById(R.id.buttonminus);
            sumt = itemView.findViewById(R.id.sumtext);
            productID = itemView.findViewById(R.id.productID);

        }
    }

    public static class Checklist {

        private String checkName;  // name
        private String checkPrice;   // price
        private String productid; // 상품 id
        private String Merchant; // 매장 id

        public Checklist(String checkName, String checkPrice, String productid, String Merchant) {

            this.checkName = checkName;
            this.checkPrice = checkPrice;
            this.productid = productid;
            this.Merchant = Merchant;
        }

        public String getCheckName() {
            return checkName;
        }

        public void setCheckName(String checkName) {
            this.checkName = checkName;
        }

        public String getCheckPrice() {
            return checkPrice;
        }

        public void setCheckPrice(String checkPrice) {
            this.checkPrice = checkPrice;
        }

        public String getProductid() {
            return productid;
        }

        public void setProductid(String productid) {
            this.productid = productid;
        }

        public String getMerchant() {
            return Merchant;
        }

        public void setMerchant(String merchant) {
            Merchant = merchant;
        }
    }
}