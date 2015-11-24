package Model;

/**
 * Created by etu25714 on 13/11/2015.
 */
public class Recipe {
    private int id;
    private String recipeName;
    private String[] ingredients;
    private String image;
    private int rating;

    public Recipe(int id, String recipeName, String[] ingredients, String image, int rating) {
        this.id = id;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.image = image;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

}
