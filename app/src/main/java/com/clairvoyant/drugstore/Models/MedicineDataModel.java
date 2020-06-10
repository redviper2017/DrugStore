package com.clairvoyant.drugstore.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineDataModel implements Parcelable{
    private String name, genericName, brand, category, strength;
    private String price;

    public MedicineDataModel(Parcel in) {
        name = in.readString();
        genericName = in.readString();
        brand = in.readString();
        category = in.readString();
        strength = in.readString();
        price = in.readString();
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

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static final Creator<MedicineDataModel> CREATOR = new Creator<MedicineDataModel>() {
        @Override
        public MedicineDataModel createFromParcel(Parcel in) {
            return new MedicineDataModel(in);
        }

        @Override
        public MedicineDataModel[] newArray(int size) {
            return new MedicineDataModel[size];
        }
    };

    public MedicineDataModel() {

    }



    @Override
    public String toString() {
        return "MedicineDataModel{" +
                "name='" + name + '\'' +
                "genericName='" + genericName + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", strength=" + strength +
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
        dest.writeString(brand);
        dest.writeString(category);
        dest.writeString(price);
        dest.writeString(strength);
    }
}
