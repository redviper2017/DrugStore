package com.clairvoyant.drugstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.clairvoyant.drugstore.Adapters.ProductAdapter;
import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity {

    private static final String TAG = "ProductsActivity";
    private RecyclerView recyclerView;
    private ArrayList<MedicineData> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        productList = getIntent().getParcelableArrayListExtra("Products");
        recyclerView = findViewById(R.id.product_list_view);

        ProductAdapter adapter = new ProductAdapter(getApplicationContext(),productList);
        adapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));

        Log.d(TAG,"productList received length = "+productList.size());
    }
}
