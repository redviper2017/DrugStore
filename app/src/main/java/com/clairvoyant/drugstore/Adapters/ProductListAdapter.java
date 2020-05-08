package com.clairvoyant.drugstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter{
    private Context context;
    private ArrayList<Product> productList;
    private static final String TAG = "ProductAdapter";

    public ProductListAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products_list,parent,false);
        return new ProductListAdapter.ProductListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        Product product = productList.get(position);
        ((ProductListAdapter.ProductListHolder) viewHolder).bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    private class ProductListHolder extends RecyclerView.ViewHolder{

        private TextView nameText, genericNameText, priceText;
        private ImageView image;

        public ProductListHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.product_name_text);
            genericNameText = itemView.findViewById(R.id.product_generic_name_text);
            priceText = itemView.findViewById(R.id.product_price_text);
            image = itemView.findViewById(R.id.product_image_view);
        }

        void bind(Product product){
            String name = product.getName();
            String genericName = product.getGenericName();
            String price = String.valueOf(product.getPrice());

            nameText.setText(name.toUpperCase());
            genericNameText.setText(genericName.toLowerCase());
            priceText.setText(price);

            Log.d(TAG,"product type = "+product.getCategory().toLowerCase());
            switch (product.getCategory().toLowerCase()){
                case "tablet":
                    image.setImageResource(R.drawable.tablet_icon);
                    break;
                case "capsules":
                    image.setImageResource(R.drawable.capsule_icon);
                    break;
                case "liquid":
                    image.setImageResource(R.drawable.syrup_icon);
                    break;
                case "injection":
                    image.setImageResource(R.drawable.injection_icon);
                    break;
                case "drop":
                    image.setImageResource(R.drawable.eyedrop_icon);
                    break;
                case "topical":
                    image.setImageResource(R.drawable.topical_icon);
                    break;
            }
        }
    }
}
