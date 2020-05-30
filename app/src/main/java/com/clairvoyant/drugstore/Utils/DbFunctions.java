package com.clairvoyant.drugstore.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DbFunctions {
    public List<User> getUsers(final Context context, final TextView nameTextView, final TextView phoneText){
        @SuppressLint("StaticFieldLeak")
        class GetUser extends AsyncTask<Void,Void,List<User>>{

            @Override
            protected List<User> doInBackground(Void... voids) {
                List<User> userList = DatabaseClient
                        .getInstance(context)
                        .getAppDatabase()
                        .userDao()
                        .getAll();

                return userList;
            }

            @Override
            protected void onPostExecute(List<User> users) {
                super.onPostExecute(users);
                Log.d("user function","user from local db = "+users);
                Log.d("user function","user from local db number = "+users.size());
                nameTextView.setText(users.get(users.size()-1).getName());
                phoneText.setText(users.get(users.size()-1).getPhone());
            }
        }
        GetUser getUser = new GetUser();
        getUser.execute();
        return null;
    }

    public void checkUserNumberAvailibility(final Context context, String phone){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("users").document(phone);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (!documentSnapshot.exists()){
                    Toast.makeText(context,"An account is already associated with this mobile number, try with another mobile number.",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
