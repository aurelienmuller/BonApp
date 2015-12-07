package Model;

import java.io.Serializable;

/**
 * Created by aurelienmuller on 7/12/15.
 */
public class Userfavorite implements Serializable{

    private int userid_fav;
    private String recipeid_fav;

    public Userfavorite() {

    }
    public Userfavorite(int userid_fav, String recipeid_fav) {
        this.setUserid_fav(userid_fav);
        this.setRecipeid_fav(recipeid_fav);
    }

    public int getUserid_fav() {
        return userid_fav;
    }

    public void setUserid_fav(int userid_fav) {
        this.userid_fav = userid_fav;
    }

    public String getRecipeid_fav() {
        return recipeid_fav;
    }

    public void setRecipeid_fav(String recipeid_fav) {
        this.recipeid_fav = recipeid_fav;
    }
}
