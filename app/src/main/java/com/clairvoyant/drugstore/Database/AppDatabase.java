package com.clairvoyant.drugstore.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.clairvoyant.drugstore.Entities.CartProduct;
import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Entities.User;
import com.clairvoyant.drugstore.Interfaces.CartDao;
import com.clairvoyant.drugstore.Interfaces.ProductDao;
import com.clairvoyant.drugstore.Interfaces.UserDao;

@Database(entities = {Product.class, CartProduct.class, User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract CartDao cartDao();

    public abstract UserDao userDao();
}
