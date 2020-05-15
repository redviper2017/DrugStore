package com.clairvoyant.drugstore.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.clairvoyant.drugstore.Models.MedicineData;
import com.clairvoyant.drugstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private List<MedicineData> medicineDataList = new ArrayList<>();
    private ArrayList<MedicineData> productListFromServer = new ArrayList<>();
    private static final String TAG = "AdminActivity";

    private MaterialCardView productsCard, ordersCard, customersCard, salesCard, suppliersCard, requestsCard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        productsCard  = findViewById(R.id.products_card);
        ordersCard    = findViewById(R.id.orders_card);
        customersCard = findViewById(R.id.customers_card);
        salesCard     = findViewById(R.id.sales_card);
        suppliersCard = findViewById(R.id.suppliers_card);
        requestsCard  = findViewById(R.id.requests_card);

        productsCard.setOnClickListener(this);

        isStoragePermissionGranted();
        // Access a Cloud Firestore instance from your Activity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    Uri fileUri = Objects.requireNonNull(data).getData();
                    try {
                        readMedicineData(fileUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.products_card:
                Intent intent = new Intent(AdminActivity.this,ProductsActivity.class);
                intent.putExtra("ProductType","all");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        productListFromServer = new ArrayList<>();
    }

    // written functions
    // -----------------------------------------------------------------------------
    public void isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("MainActivity", "Permission is granted");
                selectCSVFile();
            } else {

                Log.v("MainActivity", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("MainActivity", "Permission is granted");
        }
    }
    private void selectCSVFile() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);

        chooseFile.setType("*/*");

        chooseFile = Intent.createChooser(chooseFile, "Choose a file");

        startActivityForResult(chooseFile, 1);
    }
    private void readMedicineData(Uri uri) throws FileNotFoundException {
        Log.d("MainActivity", "inside readMedicineData method = " + "YES");
        InputStream is = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(is), StandardCharsets.UTF_8)
        );

        String line = "";
        try {
            // step over headers
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Log.d("MainActivity", "line: " + line);
                // split by commas
                String[] tokens = line.split(",");
                // read the data
                MedicineData data = new MedicineData();
                data.setName(tokens[0]);
                data.setGenericName(tokens[1]);
                data.setCode(tokens[2]);
                data.setBrand(tokens[3]);
                data.setCategory(tokens[4]);
                data.setSaleUnit(tokens[5]);
                data.setCost(Double.parseDouble(tokens[6]));
                data.setPrice(Double.parseDouble(tokens[7]));
                data.setAvailableQty(Integer.parseInt(tokens[8]));
                medicineDataList.add(data);
                Log.d("MainActivity", "just created: " + data);
            }
            storeDataInFirestore();
        } catch (IOException e) {
            Log.d("MainActivity", "error reading data file on line = " + line, e);
            e.printStackTrace();
        }

        Log.d("MainActivity", "medicine data list: " + medicineDataList.toString());
    }
    private void storeDataInFirestore(){
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Create a new user with a first and last name
        Log.d(TAG,"medicine list size = "+medicineDataList.size());
        for (int i=0; i<medicineDataList.size(); i++){
            Map<String, Object> product = new HashMap<>();
            product.put("name", medicineDataList.get(i).getName());
            product.put("genericName", medicineDataList.get(i).getGenericName());
            product.put("code", medicineDataList.get(i).getCode());
            product.put("brand", medicineDataList.get(i).getBrand());
            product.put("category", medicineDataList.get(i).getCategory());
            product.put("saleUnit", medicineDataList.get(i).getSaleUnit());
            product.put("cost", medicineDataList.get(i).getCost());
            product.put("price", medicineDataList.get(i).getPrice());
            product.put("availableQty", medicineDataList.get(i).getAvailableQty());

            // Add a new document with a generated ID
//            db.collection("products")
//                    .add(product)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error adding document", e);
//                        }
//                    });
            // Set a document
            db.collection("products").document(String.valueOf(i))
                    .set(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });
        }
    }
}
