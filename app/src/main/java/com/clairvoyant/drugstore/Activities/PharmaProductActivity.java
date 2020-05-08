package com.clairvoyant.drugstore.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import com.clairvoyant.drugstore.Adapters.ProductAdapter;
import com.clairvoyant.drugstore.Adapters.ProductListAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PharmaProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_layout);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Pharma Products:");

        recyclerView = findViewById(R.id.product_list_layout_recyclerview);
        getProducts();
    }

    private void getProducts() {
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

                ProductListAdapter adapter = new ProductListAdapter(getApplicationContext(), (ArrayList<Product>) products);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(PharmaProductActivity.this));
            }
        }

        GetTasks gt = new GetTasks();
        gt.execute();
    }
}
