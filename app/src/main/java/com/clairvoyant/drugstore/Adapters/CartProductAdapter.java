package com.clairvoyant.drugstore.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clairvoyant.drugstore.Activities.AddToCartActivity;
import com.clairvoyant.drugstore.Activities.ProductsActivity;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;
import com.google.android.material.snackbar.Snackbar;

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

    private class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView nameText, priceText, totalPriceText, quantity, numberOfProductText;
        private ImageView addProductButton, removeProductButton;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.product_name_text);
            priceText = itemView.findViewById(R.id.product_price_text);
            totalPriceText = itemView.findViewById(R.id.product_total_price_text);
            quantity = itemView.findViewById(R.id.number_of_cart_product_text);

            numberOfProductText = itemView.findViewById(R.id.number_of_cart_product_text);
            addProductButton = itemView.findViewById(R.id.add_product_button);
            removeProductButton = itemView.findViewById(R.id.remove_product_button);

            addProductButton.setOnClickListener(this);
            removeProductButton.setOnClickListener(this);
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

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.add_product_button:
                    int number = Integer.parseInt(numberOfProductText.getText().toString());
                    number++;
                    @SuppressLint("DefaultLocale") String totalPriceNew = String.format("%.2f", (Double.parseDouble(totalPriceText.getText().toString()) + Double.parseDouble(priceText.getText().toString())));
                    totalPriceText.setText(totalPriceNew);
                    @SuppressLint("DefaultLocale") String subTotal = String.format("%.2f", (subtotal + Double.parseDouble(priceText.getText().toString())));
                    ((AddToCartActivity) context).changeSubtotalText(Double.parseDouble(subTotal));
                    subtotal += Double.parseDouble(priceText.getText().toString());
                    numberOfProductText.setText(String.valueOf(number));

                    ((AddToCartActivity) context).addProductToCartInLocalDb(nameText.getText().toString(),Double.parseDouble(priceText.getText().toString()),Integer.parseInt(numberOfProductText.getText().toString()));
                    break;
                case R.id.remove_product_button:
                    int number1 = Integer.parseInt(numberOfProductText.getText().toString());
                    if (number1 != 1) {
                        number1--;
                        numberOfProductText.setText(String.valueOf(number1));
                        @SuppressLint("DefaultLocale") String totalPriceNew1 = String.format("%.2f", Double.parseDouble(totalPriceText.getText().toString()) - Double.parseDouble(priceText.getText().toString()));
                        totalPriceText.setText(totalPriceNew1);
                        @SuppressLint("DefaultLocale") String subTotal1 = String.format("%.2f", (subtotal - Double.parseDouble(priceText.getText().toString())));
                        ((AddToCartActivity) context).changeSubtotalText(Double.parseDouble(subTotal1));
                        subtotal -= Double.parseDouble(priceText.getText().toString());

                        CartProduct product1 = new CartProduct();
                        product1.setType(cartProductArrayList.get(getAdapterPosition()).getType());
                        product1.setName(nameText.getText().toString());
                        product1.setPrice(Double.parseDouble(priceText.getText().toString()));
                        product1.setSelectedQty(Integer.parseInt(numberOfProductText.getText().toString()));

                        ((AddToCartActivity) context).addProductToCartInLocalDb(nameText.getText().toString(),Double.parseDouble(priceText.getText().toString()),Integer.parseInt(numberOfProductText.getText().toString()));
                    }else {
                        ((AddToCartActivity) context).showRemoveProductDialog(getAdapterPosition(), Double.parseDouble(priceText.getText().toString()));
                    }
                    break;
            }
        }
    }
}
