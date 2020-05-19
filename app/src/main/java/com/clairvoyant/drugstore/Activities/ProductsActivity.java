package com.clairvoyant.drugstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clairvoyant.drugstore.Adapters.CartProductAdapter;
import com.clairvoyant.drugstore.Adapters.ProductListAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.R;
import com.google.android.material.navigation.NavigationView;

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
    public TextView textCartItemCount;

    private ProductListAdapter adapter;

    private String intentTag;

    private ArrayList<Product> productList = new ArrayList<>();
    private int mCartItemCount = 0;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_menu,menu);

        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View view = cartItem.getActionView();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductsActivity.this,AddToCartActivity.class));
            }
        });

        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        textCartItemCount = (TextView) view.findViewById(R.id.cart_badge);
        getCartProducts();
//        setupBadge(0);
        return super.onCreateOptionsMenu(menu);
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

                adapter = new ProductListAdapter(ProductsActivity.this, productList);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
            }
        }

        GetProducts gp = new GetProducts();
        gp.execute();
    }

    public void setupBadge(int num) {
        mCartItemCount = Integer.parseInt(textCartItemCount.getText().toString()) + num;
        Log.d(TAG,"setupBadge method called = "+num);
        if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                Log.d(TAG,"mCartItemCount is equal to zero = "+"YES");
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                Log.d(TAG,"mCartItemCount is equal to zero = "+"NO");
                textCartItemCount.setText(String.valueOf(mCartItemCount));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void addProductToCartInLocalDb(final String product, final double price, final int quantity){
        Log.d(TAG,"addProductToCartInLocalDb method called = "+"YES");
        @SuppressLint("StaticFieldLeak")
        class AddProductToCart extends AsyncTask<Void, Void, Void>{

            @Override
            protected Void doInBackground(Void... voids) {
                CartProduct cartProduct = new CartProduct();
                cartProduct.setName(product);
                cartProduct.setType(product);
                cartProduct.setPrice(price);
                cartProduct.setSelectedQty(quantity);

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .cartDao()
                        .insert(cartProduct);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG,"products saved to cart in db = "+"successfully");
            }
        }

        AddProductToCart addProductToCart = new AddProductToCart();
        addProductToCart.execute();
    }

    public List<CartProduct> getCartProducts() {
        @SuppressLint("StaticFieldLeak")
        class GetCartProducts extends AsyncTask<Void, Void, List<CartProduct>> {

            @Override
            protected List<CartProduct> doInBackground(Void... voids) {
                List<CartProduct> cartProductList = DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAll();
//                cardProducts.addAll(cartProductList);
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
                for (int i = 0; i < cartProducts.size(); i++)
                    totalNumberOfProducts += cartProducts.get(i).getSelectedQty();
                Log.d(TAG, "total number of products added to cart = " + totalNumberOfProducts);
                setupBadge(totalNumberOfProducts);
            }
        }

        GetCartProducts gcp = new GetCartProducts();
        gcp.execute();
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }
}
