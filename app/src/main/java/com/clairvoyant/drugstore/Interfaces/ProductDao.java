package com.clairvoyant.drugstore.Interfaces;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.clairvoyant.drugstore.Entities.Product;
import com.google.android.gms.tasks.Task;

import java.util.List;

public interface ProductDao {
    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Insert
    void insert(Task task);

    @Delete
    void delete(Task task);

    @Update
    void update(Task task);
}
