package com.example.ericr.bakersgonnabake.model;

public class Ingredient {

    private int quantity;
    private String measurement;
    private String ingredientName;

    public Ingredient(int quantity, String measurement, String ingredientName) {
        this.quantity = quantity;
        this.measurement = measurement;
        this.ingredientName = ingredientName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getIngredientName() {
        return ingredientName;
    }
}
