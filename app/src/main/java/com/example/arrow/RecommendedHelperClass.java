package com.example.arrow;

import android.view.View;
import android.widget.LinearLayout;

public class RecommendedHelperClass {
    int image;
    float rating;
    String name,description;

    public RecommendedHelperClass(int image, String name, String description) {
        this.image = image;
        this.name = name;
        this.description = description;

    }

    public RecommendedHelperClass(int image, String name, String description, float rating) {
        this.image = image;
        this.name = name;
        this.description = description;
        this.rating = rating;

    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
