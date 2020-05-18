package com.clairvoyant.drugstore.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "cartProduct", indices = {@Index(value = "name", unique = true)})
public class CartProduct implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "price")
    private double price;
    @ColumnInfo(name = "selectedQty")
    private int selectedQty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(int selectedQty) {
        this.selectedQty = selectedQty;
    }
}
