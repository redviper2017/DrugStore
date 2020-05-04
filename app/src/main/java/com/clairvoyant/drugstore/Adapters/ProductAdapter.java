package com.clairvoyant.drugstore.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<MedicineData> productList;
    private static final String TAG = "ProductAdapter";

    public ProductAdapter(Context context, ArrayList<MedicineData> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products_list,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MedicineData product = productList.get(position);
        ((ProductHolder) viewHolder).bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ProductHolder extends RecyclerView.ViewHolder{

        private TextView nameText, priceText;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.product_name_text);
            priceText = itemView.findViewById(R.id.product_price_text);
        }

        void bind(MedicineData product){
            String name = product.getName();
            String price = String.valueOf(product.getPrice());

            nameText.setText(name);
            priceText.setText(price);
        }
    }
}
