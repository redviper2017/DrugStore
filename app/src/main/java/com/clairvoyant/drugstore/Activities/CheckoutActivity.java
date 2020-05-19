package com.clairvoyant.drugstore.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clairvoyant.drugstore.Adapters.CartProductAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        listView = findViewById(R.id.order_summary_list);
        getCartProducts();
    }

    public void getCartProducts() {
        @SuppressLint("StaticFieldLeak")
        class GetCartProducts extends AsyncTask<Void, Void, List<CartProduct>> {

            @Override
            protected List<CartProduct> doInBackground(Void... voids) {
                //                cardProducts.addAll(cartProductList);
                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<CartProduct> cartProducts) {
                super.onPostExecute(cartProducts);
                Collections.sort(cartProducts, new Comparator<CartProduct>() {
                    @Override
                    public int compare(CartProduct o1, CartProduct o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                });
                CustomListAdapter customListAdapter = new CustomListAdapter(cartProducts);
                listView.setAdapter(customListAdapter);
            }
        }

        GetCartProducts gcp = new GetCartProducts();
        gcp.execute();
    }

    class CustomListAdapter extends BaseAdapter{

        private List<CartProduct> cartProductList;

        public CustomListAdapter(List<CartProduct> cartProductList) {
            this.cartProductList = cartProductList;
        }

        @Override
        public int getCount() {
            return cartProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CartProduct cartProduct = cartProductList.get(position);
            if (convertView ==null)
                convertView = getLayoutInflater().inflate(R.layout.row_order_summary_list, parent,false);
            TextView name = convertView.findViewById(R.id.product_name_text_order_summary);
            TextView price = convertView.findViewById(R.id.product_price_text_order_summary);
            TextView quantity = convertView.findViewById(R.id.product_quantity_text_order_summary);

            name.setText(cartProduct.getName());
            String priceTotal = String.format("%.2f",(Double.parseDouble(String.valueOf(cartProduct.getPrice())) * cartProduct.getSelectedQty()));
            price.setText(String.valueOf(priceTotal));
            quantity.setText(String.valueOf("Qty "+cartProduct.getSelectedQty()));
            return convertView;
        }
    }
}


