package com.clairvoyant.drugstore.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.clairvoyant.drugstore.Adapters.ProductListAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class BrandProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private static final String TAG = "BrandProductActivity";
    public String brand;
    List<Product> brandProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_layout);

        brand = getIntent().getStringExtra("brand");

        Objects.requireNonNull(getSupportActionBar()).setTitle(brand.toUpperCase()+" Products");

        Log.d(TAG,"brand name in BrandProductActivity = "+brand);

        recyclerView = findViewById(R.id.product_list_layout_recyclerview);
        getProducts();
    }

    private void getProducts() {
        @SuppressLint("StaticFieldLeak")
        class GetTasks extends AsyncTask<Void, Void, List<Product>> {

            @Override
            protected List<Product> doInBackground(Void... voids) {
                List<Product> productList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .getAll();
                return productList;
            }

            @Override
            protected void onPostExecute(List<Product> products) {
                super.onPostExecute(products);

                Collections.sort(products, new Comparator<Product>() {
                    @Override
                    public int compare(Product o1, Product o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                });

                for (int i=0; i<products.size(); i++) {
                    Log.d(TAG, "product brand = " + products.get(i).getBrand());
                    if (products.get(i).getBrand().toLowerCase().contains(brand.toLowerCase())) {
                        Log.d(TAG, "passed brand = " + brand.toString());
                        brandProducts.add(products.get(i));
                    }
                }
                Log.d(TAG,"brandProducts length = "+brandProducts.size());

                ProductListAdapter adapter = new ProductListAdapter(getApplicationContext(), (ArrayList<Product>) brandProducts);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(BrandProductActivity.this));
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
}
