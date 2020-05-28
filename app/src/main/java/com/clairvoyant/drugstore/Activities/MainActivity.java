package com.clairvoyant.drugstore.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Fragments.HomeFragment;
import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FrameLayout fragmentHolder;
    private static final String TAG = "MainActivity";

    private ArrayList<MedicineData> productListFromServer = new ArrayList<>();
    private TextView textCartItemCount;
    private int mCartItemCount = 0;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        switch (item.getItemId()){
            case R.id.action_cart_main:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataFromFirestore();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment("Home");
//        setupBadge();

        drawerLayout = findViewById(R.id.drawer_layout_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        navigationView = findViewById(R.id.nav_view);
        fragmentHolder = findViewById(R.id.fragment_holder_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Home");

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actions_menu_main, menu);

        MenuItem cartItem = menu.findItem(R.id.action_cart_main);
        View actionView = cartItem.getActionView();
        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddToCartActivity.class));
                finish();
            }
        });
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        getCartProducts();

        return true;
    }

    public void setFragment(String fragmentNname){
        Fragment fragment;
        switch (fragmentNname){
            case "Home":
                fragment = new HomeFragment();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + fragmentNname);
        }
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_holder_main, fragment).commit();
    }

    private void fetchDataFromFirestore(){
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, "document = " + " => " + document.getData());

                                MedicineData medicineData;
                                medicineData  = document.toObject(MedicineData.class);
                                Log.d(TAG,"medicine = "+medicineData);
                                productListFromServer.add(medicineData);
                                storeProductsToLocalDB(medicineData);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        Log.d(TAG,"number of products fetched in onStart() of Main Activity = " + productListFromServer.size());
                    }
                });
    }

    private void storeProductsToLocalDB(final MedicineData medicineData){
        @SuppressLint("StaticFieldLeak")
        class SaveProduct extends AsyncTask<Void, Void, Void>{
            @Override
            protected Void doInBackground(Void... voids) {
                Product product = new Product();
                product.setName(medicineData.getName());
                product.setGenericName(medicineData.getGenericName());
                product.setBrand(medicineData.getBrand());
                product.setCategory(medicineData.getCategory());
                product.setSaleUnit(medicineData.getSaleUnit());
                product.setPrice(String.valueOf(medicineData.getPrice()));
                product.setAvailableQty(String.valueOf(medicineData.getAvailableQty()));

                //adding to database
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .productDao()
                        .insert(product);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG,"products saved to local db = "+"successfully");
            }
        }

        SaveProduct saveProduct = new SaveProduct();
        saveProduct.execute();
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
}
