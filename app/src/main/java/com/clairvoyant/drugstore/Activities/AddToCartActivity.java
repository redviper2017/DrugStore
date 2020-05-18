package com.clairvoyant.drugstore.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.clairvoyant.drugstore.Adapters.CartProductAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AddToCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public TextView subtotalText;

    private static final String TAG = "AddToCartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        recyclerView = findViewById(R.id.car_product_list_view);
        subtotalText = findViewById(R.id.subtotal_text);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Your Cart");
        getCartProducts();
    }

    private void getCartProducts(){
        @SuppressLint("StaticFieldLeak")
        class GetCartProducts extends AsyncTask<Void, Void, List<CartProduct>>{

            @Override
            protected List<CartProduct> doInBackground(Void... voids) {
                List<CartProduct> cartProductList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAll();
                return cartProductList;
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
                int totalNumberOfProducts = 0;
                for (int i=0; i<cartProducts.size(); i++)
                    totalNumberOfProducts += cartProducts.get(i).getSelectedQty();
                Log.d(TAG,"total number of products added to cart = "+totalNumberOfProducts);

                CartProductAdapter cartProductAdapter = new CartProductAdapter(AddToCartActivity.this,(ArrayList<CartProduct>) cartProducts);
                cartProductAdapter.notifyDataSetChanged();

                recyclerView.setAdapter(cartProductAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(AddToCartActivity.this));
            }
        }

        GetCartProducts gcp = new GetCartProducts();
        gcp.execute();
    }

    public void changeSubtotalText(double subtotal){
        subtotalText.setText(String.valueOf(subtotal));
    }
}
