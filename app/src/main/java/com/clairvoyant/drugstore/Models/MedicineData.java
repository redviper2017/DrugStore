package com.clairvoyant.drugstore.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineData implements Parcelable{
    private String name, genericName, code, brand, category, saleUnit;
    private double cost, price;
    private int availableQty;

    public MedicineData(Parcel in) {
        name = in.readString();
        genericName = in.readString();
        code = in.readString();
        brand = in.readString();
        category = in.readString();
        saleUnit = in.readString();
        cost = in.readDouble();
        price = in.readDouble();
        availableQty = in.readInt();
    }

    public static final Creator<MedicineData> CREATOR = new Creator<MedicineData>() {
        @Override
        public MedicineData createFromParcel(Parcel in) {
            return new MedicineData(in);
        }

        @Override
        public MedicineData[] newArray(int size) {
            return new MedicineData[size];
        }
    };

    public MedicineData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenericName() {
        return genericName;
    }

    public void setGenericName(String genericName) {
        this.genericName = genericName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSaleUnit() {
        return saleUnit;
    }

    public void setSaleUnit(String saleUnit) {
        this.saleUnit = saleUnit;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    @Override
    public String toString() {
        return "MedicineData{" +
                "name='" + name + '\'' +
                "genericName='" + genericName + '\'' +
                ", code='" + code + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", saleUnit='" + saleUnit + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", availableQty=" + availableQty +
                '}';
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(genericName);
        dest.writeString(code);
        dest.writeString(brand);
        dest.writeString(category);
        dest.writeString(saleUnit);
        dest.writeDouble(cost);
        dest.writeDouble(price);
        dest.writeInt(availableQty);
    }
}
