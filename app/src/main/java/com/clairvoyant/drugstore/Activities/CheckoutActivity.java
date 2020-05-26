package com.clairvoyant.drugstore.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clairvoyant.drugstore.Adapters.CartProductAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private ListView listView;
    private CardView confirmOrderButton;
    public EditText deliveryAddressText;
    public TextView totalPriceText;
    public List<CartProduct> cartProductsFinal;
    private static final String TAG = "CheckoutActivity";
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        listView = findViewById(R.id.order_summary_list);
        confirmOrderButton = findViewById(R.id.place_order_card);
        deliveryAddressText = findViewById(R.id.delivery_address_text);
        totalPriceText = findViewById(R.id.total_price_text);
        getCartProducts();
        confirmOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartProductsFinal != null)
                    storeOrderToFirestore();
            }
        });
    }

    public void getCartProducts() {
        @SuppressLint("StaticFieldLeak")
        class GetCartProducts extends AsyncTask<Void, Void, List<CartProduct>> {

            @Override
            protected List<CartProduct> doInBackground(Void... voids) {
                //                cardProducts.addAll(cartProductList);
                return DatabaseClient
                        .getInstance(getApplicationContext())
                        .getAppDatabase()
                        .cartDao()
                        .getAll();
            }

            @Override
            protected void onPostExecute(List<CartProduct> cartProducts) {
                super.onPostExecute(cartProducts);
                cartProductsFinal = cartProducts;
                Collections.sort(cartProducts, new Comparator<CartProduct>() {
                    @Override
                    public int compare(CartProduct o1, CartProduct o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                });
                CustomListAdapter customListAdapter = new CustomListAdapter(cartProducts);
                listView.setAdapter(customListAdapter);

                for (CartProduct cartProduct: cartProducts)
                    totalPrice += cartProduct.getPrice() * cartProduct.getSelectedQty();
                totalPriceText.setText(String.valueOf(totalPrice));
            }
        }

        GetCartProducts gcp = new GetCartProducts();
        gcp.execute();
    }

    public void storeOrderToFirestore() {
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MMM/yyyy hh:mm aa");
        String orderDate = dateformat.format(Calendar.getInstance().getTime());

        Log.d(TAG,"order date = "+orderDate);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Create new order in Firestore
        Log.d(TAG, "final ordered products list size = " + cartProductsFinal.size());

        int i = 1;

        Map<String, Object> nestedFinalOrder = new HashMap<>();

        for (CartProduct cartProduct : cartProductsFinal) {
            double orderPriceProduct = cartProduct.getPrice() * cartProduct.getSelectedQty();
            Map<String, Object> finalOrder = new HashMap<>();
            finalOrder.put("id", cartProduct.getId());
            finalOrder.put("name", cartProduct.getName());
            finalOrder.put("price", cartProduct.getPrice());
            finalOrder.put("quantity", cartProduct.getSelectedQty());
            finalOrder.put("productId", cartProduct.getId());
            finalOrder.put("total", orderPriceProduct);


            nestedFinalOrder.put(String.valueOf("product:"+i),finalOrder);
            i++;
        }
        nestedFinalOrder.put("time",orderDate);
        nestedFinalOrder.put("subTotal",totalPriceText.getText().toString());
        if (!TextUtils.isEmpty(deliveryAddressText.getText().toString())) {
            nestedFinalOrder.put("address", deliveryAddressText.getText().toString());
            db.collection("orders").document()
                    .set(nestedFinalOrder)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });
        }else
            Toast.makeText(getApplicationContext(),"You must provide delivery address in order to place this order!",Toast.LENGTH_SHORT).show();
//        finalOrder.put("orderPrice",productOrderPrice);
//        finalOrder.put("deliveryAddress",deliveryAddressText.getText().toString());
    }

    class CustomListAdapter extends BaseAdapter {

        private List<CartProduct> cartProductList;

        public CustomListAdapter(List<CartProduct> cartProductList) {
            this.cartProductList = cartProductList;
        }

        @Override
        public int getCount() {
            return cartProductList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CartProduct cartProduct = cartProductList.get(position);
            if (convertView == null)
                convertView = getLayoutInflater().inflate(R.layout.row_order_summary_list, parent, false);
            TextView name = convertView.findViewById(R.id.product_name_text_order_summary);
            TextView price = convertView.findViewById(R.id.product_price_text_order_summary);
            TextView quantity = convertView.findViewById(R.id.product_quantity_text_order_summary);

            name.setText(cartProduct.getName());
            String priceTotal = String.format("%.2f", (Double.parseDouble(String.valueOf(cartProduct.getPrice())) * cartProduct.getSelectedQty()));
            price.setText(String.valueOf(priceTotal));
            quantity.setText(String.valueOf("Qty " + cartProduct.getSelectedQty()));
            return convertView;
        }
    }
}


