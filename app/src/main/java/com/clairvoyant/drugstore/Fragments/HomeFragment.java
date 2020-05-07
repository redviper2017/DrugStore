package com.clairvoyant.drugstore.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.clairvoyant.drugstore.Activities.AdminActivity;
import com.clairvoyant.drugstore.Activities.BrandProductActivity;
import com.clairvoyant.drugstore.Activities.PharmaProductActivity;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.R;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    List<Product> productList;

    private CardView allPharmaProducts, aciProducts;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        allPharmaProducts = view.findViewById(R.id.all_pharma_products_category_card);
        aciProducts = view.findViewById(R.id.aci_card);


        allPharmaProducts.setOnClickListener(this);
        aciProducts.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    private void getProducts() {
        productList = DatabaseClient.getInstance(getContext()).getAppDatabase()
                .productDao().getAll();
        Log.d(TAG,"inside getProducts() = "+"YES");
        class GetProducts extends AsyncTask<Void, Void, List<Product>> {

            @Override
            protected List<Product> doInBackground(Void... voids) {
                productList =  DatabaseClient
                        .getInstance(Objects.requireNonNull(getActivity()).getApplicationContext())
                        .getAppDatabase()
                        .productDao()
                        .getAll();
                Log.d(TAG,"number of products inside HomeFragment = "+productList.size());
                return productList;
            }

            @Override
            protected void onPostExecute(List<Product> products) {
                super.onPostExecute(products);
            }
        }

        GetProducts gt = new GetProducts();
        gt.execute();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.all_pharma_products_category_card:
                intent = new Intent(getActivity(), PharmaProductActivity.class);
                break;
            case R.id.aci_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","aci");
                break;
        }
        startActivity(intent);
    }
}
