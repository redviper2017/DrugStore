package com.clairvoyant.drugstore.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.clairvoyant.drugstore.Activities.ProductsActivity;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Collection;

public class ProductListAdapter extends RecyclerView.Adapter implements Filterable {
    private Context context;
    private ArrayList<Product> productList;
    private ArrayList<Product> productListAll;
    //    private ArrayList<Product> productListFiltered;
    private static final String TAG = "ProductAdapter";

    public ProductListAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        productListAll = new ArrayList<>(productList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_products_list, parent, false);
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

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //runs on a background thread
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Product> productListFiltered = new ArrayList<>();
            if (charSequence.toString().isEmpty()) {
                Log.d(TAG, "Search text is empty = " + "YES");
                Log.d(TAG, "product list size in search = " + productList.size());
                productListFiltered.addAll(productListAll);
            } else {
                Log.d(TAG, "Search text is empty = " + "NO");
                Log.d(TAG, "product list size in search = " + productList.size());
                for (Product product : productListAll) {
                    if (product.getName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        productListFiltered.add(product);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = productListFiltered;

            return filterResults;
        }

        //runs on a ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            productList.clear();
            productList.addAll((Collection<? extends Product>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static String toCamelCase(final String init) {
        if (init == null)
            return null;

        final StringBuilder ret = new StringBuilder(init.length());

        for (final String word : init.split(" ")) {
            if (!word.isEmpty()) {
                ret.append(Character.toUpperCase(word.charAt(0)));
                ret.append(word.substring(1).toLowerCase());
            }
            if (!(ret.length() == init.length()))
                ret.append(" ");
        }

        return ret.toString();
    }

    private class ProductListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView nameText, brandNameText, genericNameText, priceText,numberOfProductTitleText, numberOfProductText, addToCartFinalButton;
        private ImageView image, addToCartIconButton, addProductButton, removeProductButton;
        private LinearLayout addRemoveProductLayout;

        public ProductListHolder(@NonNull View itemView) {
            super(itemView);

            nameText = itemView.findViewById(R.id.product_name_text);
            brandNameText = itemView.findViewById(R.id.product_brand_name_text);
            genericNameText = itemView.findViewById(R.id.product_generic_name_text);
            priceText = itemView.findViewById(R.id.product_price_text);
            image = itemView.findViewById(R.id.product_image_view);
            addToCartIconButton = itemView.findViewById(R.id.add_to_cart_icon_image_view);
            addRemoveProductLayout = itemView.findViewById(R.id.linear_layout_add_remove_product);
            addProductButton = itemView.findViewById(R.id.add_product_button);
            removeProductButton = itemView.findViewById(R.id.remove_product_button);
            numberOfProductTitleText = itemView.findViewById(R.id.number_of_product_title_text);
            numberOfProductText = itemView.findViewById(R.id.number_of_product_text);
            addToCartFinalButton = itemView.findViewById(R.id.add_to_cart_final_card);

            addToCartIconButton.setOnClickListener(this);
            addToCartFinalButton.setOnClickListener(this);
            addRemoveProductLayout.setOnClickListener(this);
            addProductButton.setOnClickListener(this);
            removeProductButton.setOnClickListener(this);
        }

        void bind(Product product) {
            String name = product.getName();
            String brandName = toCamelCase(product.getBrand());
            Log.d(TAG, "brand name in camel case = " + brandName);
            String genericName = product.getGenericName();
            String price = String.valueOf(product.getPrice());

            nameText.setText(name.toUpperCase());
            brandNameText.setText(brandName);
            genericNameText.setText(genericName.toLowerCase());
            priceText.setText(price);

            Log.d(TAG, "product type = " + product.getCategory().toLowerCase());
            switch (product.getCategory().toLowerCase()) {
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

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_to_cart_icon_image_view:
                    addToCartIconButton.setVisibility(View.GONE);
                    addRemoveProductLayout.setVisibility(View.VISIBLE);
                    numberOfProductText.setText("1");
                    numberOfProductTitleText.setVisibility(View.VISIBLE);
                    addToCartFinalButton.setVisibility(View.VISIBLE);
                    break;
                case R.id.add_product_button:
                    int number = Integer.parseInt(numberOfProductText.getText().toString());
                    number++;
                    numberOfProductText.setText(String.valueOf(number));
                    break;
                case R.id.remove_product_button:
                    int number1 = Integer.parseInt(numberOfProductText.getText().toString());
                    if (number1 != 0) {
                        number1--;
                        if (number1 == 0){
                            number1 = 1;
                            addRemoveProductLayout.setVisibility(View.GONE);
                            numberOfProductTitleText.setVisibility(View.GONE);
                            addToCartFinalButton.setVisibility(View.GONE);
                            addToCartIconButton.setVisibility(View.VISIBLE);
                        }
                    }

                    numberOfProductText.setText(String.valueOf(number1));
                    break;
                case R.id.add_to_cart_final_card:
                    Log.d(TAG,"final add to cart clicked = "+"YES");

                    ((ProductsActivity) context).setupBadge(Integer.parseInt(numberOfProductText.getText().toString()));
                    ((ProductsActivity) context).addProductToCartInLocalDb(nameText.getText().toString(),Double.parseDouble(priceText.getText().toString()), Integer.parseInt(numberOfProductText.getText().toString()));

                    addRemoveProductLayout.setVisibility(View.GONE);
                    numberOfProductTitleText.setVisibility(View.GONE);
                    addToCartFinalButton.setVisibility(View.GONE);
                    addToCartIconButton.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
}
