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

    private CardView allPharmaProducts, aciProducts, acmeProducts, beximcoProducts, ibnsinaProducts, popularProducts, radiantProducts, squareProducts;

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
        acmeProducts = view.findViewById(R.id.acme_card);
        beximcoProducts = view.findViewById(R.id.beximco_card);
        ibnsinaProducts = view.findViewById(R.id.ibnsina_card);
        popularProducts = view.findViewById(R.id.popular_card);
        radiantProducts = view.findViewById(R.id.radiant_card);
        squareProducts = view.findViewById(R.id.square_card);

        allPharmaProducts.setOnClickListener(this);
        aciProducts.setOnClickListener(this);
        acmeProducts.setOnClickListener(this);
        beximcoProducts.setOnClickListener(this);
        ibnsinaProducts.setOnClickListener(this);
        popularProducts.setOnClickListener(this);
        radiantProducts.setOnClickListener(this);
        squareProducts.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
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
            case R.id.acme_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","acme");
                break;
            case R.id.ibnsina_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","ibn sina");
                break;
            case R.id.popular_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","popular");
                break;
            case R.id.radiant_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","radiant");
                break;
            case R.id.square_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","square");
                break;
            case R.id.beximco_card:
                intent = new Intent(getActivity(), BrandProductActivity.class);
                intent.putExtra("brand","beximco");
                break;
        }
        startActivity(intent);
    }
}
