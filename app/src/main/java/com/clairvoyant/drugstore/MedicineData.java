package com.clairvoyant.drugstore;

class MedicineData {
    private String name, code, brand, category, saleUnit;
    private double cost, price;
    private int availableQty;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
                ", code='" + code + '\'' +
                ", brand='" + brand + '\'' +
                ", category='" + category + '\'' +
                ", saleUnit='" + saleUnit + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", availableQty=" + availableQty +
                '}';
    }
}
