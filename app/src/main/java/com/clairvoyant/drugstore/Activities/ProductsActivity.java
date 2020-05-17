package com.clairvoyant.drugstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clairvoyant.drugstore.Adapters.ProductAdapter;
import com.clairvoyant.drugstore.Adapters.ProductListAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {

    private static final String TAG = "ProductsActivity";
    private RecyclerView recyclerView;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private String intentTag;

    private ArrayList<Product> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        intentTag = getIntent().getStringExtra("ProductType");
        recyclerView = findViewById(R.id.product_list_view);

        drawerLayout = findViewById(R.id.drawer_layout_products_activity);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        navigationView = findViewById(R.id.nav_view_products_activity);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_my_orders:
                        break;
                    case R.id.nav_profile:
                        break;
                    case R.id.nav_contact_us:
                        break;
                    case R.id.nav_sign_out:
                        break;
                    default:
                        return true;
                }

                return true;
            }
        });
        getProducts();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()) {
            case R.id.action_cart:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getProducts() {
        @SuppressLint("StaticFieldLeak")
        class GetProducts extends AsyncTask<Void, Void, List<Product>> {

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

                for (int i = 0; i < products.size(); i++) {
                    switch (intentTag) {
                        case "all":
                        case "pharma":
                            productList.add(products.get(i));
                            Objects.requireNonNull(getSupportActionBar()).setTitle("Pharma Products");
                            break;
                        case "Capsules & Tablets":
                        case "Injections":
                        case "Syrups & Suspensions":
                        case "Drops":
                        case "Topicals":
                            if (intentTag.equals("Syrups & Suspensions")){
                                String tag = "Liquid";
                                if (tag.toLowerCase().contains(products.get(i).getCategory().toLowerCase())){
                                    Log.d(TAG,"category in ProductsActivity= "+products.get(i).getCategory().toLowerCase());
                                    Objects.requireNonNull(getSupportActionBar()).setTitle(intentTag);
                                    productList.add(products.get(i));
                                }
                            }
                            else if (intentTag.toLowerCase().contains(products.get(i).getCategory().toLowerCase())) {
                                Log.d(TAG,"category in ProductsActivity= "+products.get(i).getCategory().toLowerCase());
                                Objects.requireNonNull(getSupportActionBar()).setTitle(intentTag);
                                productList.add(products.get(i));
                            }
                            break;
                        case "aci":
                        case "acme":
                        case "ibn sina":
                        case "popular":
                        case "radiant":
                        case "square":
                        case "beximco":
                            if (products.get(i).getBrand().toLowerCase().contains(intentTag.toLowerCase())) {
                                Log.d(TAG,"brand in ProductsActivity= "+products.get(i).getBrand().toLowerCase());
                                char first = Character.toUpperCase(intentTag.charAt(0));
                                String rest = intentTag.substring(1);
                                Objects.requireNonNull(getSupportActionBar()).setTitle(first + rest +" Products");
                                productList.add(products.get(i));
                            }
                            break;
                    }
                }
                Log.d(TAG, "number of products = " + productList.size());

                ProductListAdapter adapter = new ProductListAdapter(getApplicationContext(), productList);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
            }
        }

        GetProducts gp = new GetProducts();
        gp.execute();
    }
}
