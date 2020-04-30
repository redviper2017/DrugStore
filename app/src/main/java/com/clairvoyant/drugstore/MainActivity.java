package com.clairvoyant.drugstore;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Uri fileUri;
    private List<MedicineData> medicineDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    try {
                        readMedicineData(fileUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v("MainActivity", "Permission: " + permissions[0] + "was " + grantResults[0]);

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
                new InputStreamReader(is, StandardCharsets.UTF_8)
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
                data.setCode(tokens[1]);
                data.setBrand(tokens[2]);
                data.setCategory(tokens[3]);
                data.setSaleUnit(tokens[4]);
                data.setCost(Double.parseDouble(tokens[5]));
                data.setPrice(Double.parseDouble(tokens[6]));
                data.setAvailableQty(Integer.parseInt(tokens[7]));
                medicineDataList.add(data);
                Log.d("MainActivity", "just created: " + data);
            }
        } catch (IOException e) {
            Log.d("MainActivity", "error reading data file on line = " + line, e);
            e.printStackTrace();
        }

        Log.d("MainActivity", "medicine data list: " + medicineDataList.toString());
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("MainActivity", "Permission is granted");
                selectCSVFile();
                return true;
            } else {
                Log.v("MainActivity", "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("MainActivity", "Permission is granted");
            return true;
        }
    }
}
