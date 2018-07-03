package com.example.ericr.bakersgonnabake.model;

import java.util.List;

public class Recipe {

    private int id;
    private String name;
    private List<Ingredient> ingredients; // TODO: verify sort order is maintained, evaluate options
    private List<Step> steps;
    private int servings;
    private String image; // TODO: json data has empty value for all of these, look at free images to include instead

    public Recipe(int id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public int getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

}
