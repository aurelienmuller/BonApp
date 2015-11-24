package Model;

/**
 * Created by etu25714 on 13/11/2015.
 */
public class Ingredient {
    private int id;
    private String name;

    public Ingredient(int id, String name){
        this.setId(id);
        this.setName(name);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
