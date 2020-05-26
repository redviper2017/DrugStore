package com.clairvoyant.drugstore.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clairvoyant.drugstore.Adapters.CartProductAdapter;
import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AddToCartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    public TextView subtotalText, deliveryText;
    private RelativeLayout contentLayout, noContentLayout;
    private CardView proceedCheckoutButton;
    List<CartProduct> productList;

    CartProductAdapter cartProductAdapter;

    private List<CartProduct> cardProducts = new ArrayList<>();

    private static final String TAG = "AddToCartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);
        proceedCheckoutButton = findViewById(R.id.checkout_button);
        recyclerView = findViewById(R.id.car_product_list_view);
        subtotalText = findViewById(R.id.subtotal_text);
        deliveryText = findViewById(R.id.deliver_fee_text);
        contentLayout = findViewById(R.id.content_relative_layout_cart_activity);
        noContentLayout = findViewById(R.id.no_content_relative_layout_cart_activity);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Your Cart");
        getCartProducts();

        proceedCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Clicked!",Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddToCartActivity.this,CheckoutActivity.class));
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
                Collections.sort(cartProducts, new Comparator<CartProduct>() {
                    @Override
                    public int compare(CartProduct o1, CartProduct o2) {
                        return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
                    }
                });
                productList = cartProducts;

                int totalNumberOfProducts = 0;
                for (int i = 0; i < cartProducts.size(); i++)
                    totalNumberOfProducts += cartProducts.get(i).getSelectedQty();
                Log.d(TAG, "total number of products added to cart = " + totalNumberOfProducts);
                if (totalNumberOfProducts != 0) {
                    deliveryText.setText("50");
                    cartProductAdapter = new CartProductAdapter(AddToCartActivity.this, (ArrayList<CartProduct>) productList);
                    cartProductAdapter.notifyDataSetChanged();

                    recyclerView.setAdapter(cartProductAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(AddToCartActivity.this));
                } else {
                    contentLayout.setVisibility(View.GONE);
                    noContentLayout.setVisibility(View.VISIBLE);
                }
            }
        }

        GetCartProducts gcp = new GetCartProducts();
        gcp.execute();
    }

    public void changeSubtotalText(double subtotal) {
        subtotalText.setText(String.valueOf(subtotal));
    }

    public void showRemoveProductDialog(final int position, final double price) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setCancelable(false)
                .setTitle("Remove Product")
                .setMessage("Remove this product from my cart?")
                // Add the buttons
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Log.d(TAG, "position of item to be removed = " + position);
                        removeProductFromCartInLocalDb(productList.get(position));
                        productList.remove(position);

                        if (productList.size() != 0) {
                            cartProductAdapter.notifyItemRemoved(position);
                            subtotalText.setText(String.valueOf(Double.parseDouble(subtotalText.getText().toString()) - price));
                        } else {
                            contentLayout.setVisibility(View.GONE);
                            noContentLayout.setVisibility(View.VISIBLE);
                            subtotalText.setText("0.00");
                            deliveryText.setText("0.00");
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void addProductToCartInLocalDb(final String product, final double price, final int quantity) {
        Log.d(TAG, "addProductToCartInLocalDb method called = " + "YES");
        @SuppressLint("StaticFieldLeak")
        class AddProductToCart extends AsyncTask<Void, Void, Void> {

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
                Log.d(TAG, "products saved to cart in db = " + "successfully");
            }
        }

        AddProductToCart addProductToCart = new AddProductToCart();
        addProductToCart.execute();
    }

    public void removeProductFromCartInLocalDb(final CartProduct cartProduct) {
        Log.d(TAG, "removeProductFromCartInLocalDb method called = " + "YES");

        @SuppressLint("StaticFieldLeak")
        class RemoveProductFromCart extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .cartDao()
                        .delete(cartProduct);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "products removed from cart in db = " + "successfully");
            }
        }

        RemoveProductFromCart removeProductFromCart = new RemoveProductFromCart();
        removeProductFromCart.execute();
    }

    public void updateCartInLocalDb(final CartProduct cartProduct) {
        Log.d(TAG, "updateCartInLocalDb method called with product named = " + cartProduct.getSelectedQty());
        @SuppressLint("StaticFieldLeak")
        class UpdateCart extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .cartDao()
                        .update(cartProduct);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "products update in cart in db = " + "successfully");
            }
        }
        UpdateCart updateCart = new UpdateCart();
        updateCart.execute();
    }
}
