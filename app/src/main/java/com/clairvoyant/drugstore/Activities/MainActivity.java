package com.clairvoyant.drugstore.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.clairvoyant.drugstore.Database.DatabaseClient;
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
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FrameLayout fragmentHolder;
    private static final String TAG = "MainActivity";

    private ArrayList<MedicineData> productListFromServer = new ArrayList<>();
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchDataFromFirestore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment("Home");

        drawerLayout = findViewById(R.id.drawer_layout_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        navigationView = findViewById(R.id.nav_view);
        fragmentHolder = findViewById(R.id.fragment_holder_main);

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
}
