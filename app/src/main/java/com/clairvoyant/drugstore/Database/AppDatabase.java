package com.clairvoyant.drugstore.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Interfaces.ProductDao;

@Database(entities = {Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}
