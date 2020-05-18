package com.clairvoyant.drugstore.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.clairvoyant.drugstore.Entities.Product;
import com.clairvoyant.drugstore.Entities.CartProduct;

import java.util.List;
@Dao
public interface CartDao {
    @Query("SELECT * FROM cartProduct")
    List<CartProduct> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartProduct cartProduct);

    @Delete
    void delete(CartProduct cartProduct);

    @Update
    void update(CartProduct cartProduct);
}
