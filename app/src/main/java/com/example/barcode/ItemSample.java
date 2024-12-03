package com.example.barcode;

public class ItemSample {
    private int barcode;
    private String productName;
    private float price;

    public ItemSample(int barcode, String productName, float price) {
        this.barcode = barcode;
        this.productName = productName;
        this.price = price;
    }
    public ItemSample() {}
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    @Override
    public String toString() {
        return "ItemSample{" +
                "barcode=" + barcode +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }
}
