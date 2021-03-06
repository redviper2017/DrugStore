package com.clairvoyant.drugstore.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.clairvoyant.drugstore.Activities.ProductsActivity;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "HomeFragment";
    List<Product> productList;

    private CardView capsuleAndTabletProducts, injectionProducts, liquidProducts, dropProducts, topicalProducts, allPharmaProducts, aciProducts, acmeProducts, beximcoProducts, ibnsinaProducts, popularProducts, radiantProducts, squareProducts;

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

        capsuleAndTabletProducts = view.findViewById(R.id.capcule_card);
        injectionProducts = view.findViewById(R.id.injection_card);
        liquidProducts = view.findViewById(R.id.syrup_card);
        dropProducts = view.findViewById(R.id.drop_card);
        topicalProducts = view.findViewById(R.id.topical_card);

        allPharmaProducts = view.findViewById(R.id.all_pharma_products_category_card);

        aciProducts = view.findViewById(R.id.aci_card);
        acmeProducts = view.findViewById(R.id.acme_card);
        beximcoProducts = view.findViewById(R.id.beximco_card);
        ibnsinaProducts = view.findViewById(R.id.ibnsina_card);
        popularProducts = view.findViewById(R.id.popular_card);
        radiantProducts = view.findViewById(R.id.radiant_card);
        squareProducts = view.findViewById(R.id.square_card);

        capsuleAndTabletProducts.setOnClickListener(this);
        injectionProducts.setOnClickListener(this);
        liquidProducts.setOnClickListener(this);
        dropProducts.setOnClickListener(this);
        topicalProducts.setOnClickListener(this);

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
            case R.id.capcule_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","Capsules & Tablets");
                break;
            case R.id.injection_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","Injections");
                break;
            case R.id.syrup_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","Syrups & Suspensions");
                break;
            case R.id.drop_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","Drops");
                break;
            case R.id.topical_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","Topicals");
                break;

            case R.id.aci_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","aci");
                break;
            case R.id.acme_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","acme");
                break;
            case R.id.ibnsina_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","ibn sina");
                break;
            case R.id.popular_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","popular");
                break;
            case R.id.radiant_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","radiant");
                break;
            case R.id.square_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","square");
                break;
            case R.id.beximco_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","beximco");
                break;

            case R.id.all_pharma_products_category_card:
                intent = new Intent(getActivity(), ProductsActivity.class);
                intent.putExtra("ProductType","pharma");
                break;
        }
        startActivity(intent);
    }
}
