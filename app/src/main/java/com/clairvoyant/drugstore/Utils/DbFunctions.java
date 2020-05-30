package com.clairvoyant.drugstore.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.clairvoyant.drugstore.Database.DatabaseClient;
import com.clairvoyant.drugstore.Entities.User;

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
                nameTextView.setText(users.get(0).getName());
                phoneText.setText(users.get(0).getPhone());
            }
        }
        GetUser getUser = new GetUser();
        getUser.execute();
        return null;
    }
}
