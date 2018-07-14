package com.example.ericr.bakersgonnabake.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {

    private double quantity;
    @SerializedName("measure")
    private String measurement;
    @SerializedName("ingredient")
    private String ingredientName;

    public Ingredient(int quantity, String measurement, String ingredientName) {
        this.quantity = quantity;
        this.measurement = measurement;
        this.ingredientName = ingredientName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getIngredientName() {
        return ingredientName;
    }

}
