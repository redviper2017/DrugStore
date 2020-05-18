package com.clairvoyant.drugstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clairvoyant.drugstore.Activities.AddToCartActivity;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;

public class CartProductAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<CartProduct> cartProductArrayList;
    private static final String TAG = "ProductAdapter";

    public double subtotal = 0.0;

    public CartProductAdapter(Context context, ArrayList<CartProduct> cartProductArrayList) {
        this.context = context;
        this.cartProductArrayList = cartProductArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart_products_list,parent,false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        CartProduct cartProduct = cartProductArrayList.get(position);
        ((ProductHolder) viewHolder).bind(cartProduct);
    }

    @Override
    public int getItemCount() {
        return cartProductArrayList.size();
    }

    private class ProductHolder extends RecyclerView.ViewHolder{

        private TextView nameText, priceText, totalPriceText, quantity;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.product_name_text);
            priceText = itemView.findViewById(R.id.product_price_text);
            totalPriceText = itemView.findViewById(R.id.product_total_price_text);
            quantity = itemView.findViewById(R.id.number_of_cart_product_text);
        }

        void bind(CartProduct cartProduct){
            String name = cartProduct.getName();
            String price = String.valueOf(cartProduct.getPrice());
            String totalPrice = String.valueOf(cartProduct.getPrice() * cartProduct.getSelectedQty());

            nameText.setText(name);
            priceText.setText(price);
            totalPriceText.setText(totalPrice);
            Log.d(TAG,"cart product quantity = "+cartProduct.getSelectedQty());
            quantity.setText(String.valueOf(cartProduct.getSelectedQty()));

            subtotal += Double.parseDouble(totalPrice);
            ((AddToCartActivity) context).changeSubtotalText(subtotal);
        }
    }
}
