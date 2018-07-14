package com.example.ericr.bakersgonnabake.util;

import com.example.ericr.bakersgonnabake.model.Ingredient;

import java.util.List;

public class SimpleHtmlUtility {

    public static String toUnorderedList(List<Ingredient> ingredients) {

        StringBuilder sb = new StringBuilder(); // this will be filled with html string

        for (Ingredient ingredient : ingredients) {
            sb.append("&#8226; ");
            sb.append(ingredient.getQuantity());
            sb.append(" ");
            sb.append(ingredient.getMeasurement());
            sb.append(" ");
            sb.append(ingredient.getIngredientName());
            sb.append("<br>");
        }

        return sb.toString();
    }
}
