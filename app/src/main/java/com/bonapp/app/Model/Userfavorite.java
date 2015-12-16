package com.bonapp.app.Model;

import java.io.Serializable;

/**
 * Created by aurelienmuller on 7/12/15.
 */
public class Userfavorite implements Serializable{

    private String userfavorite_id;
    private int userid_fav;
    private String recipeid_fav;

    public Userfavorite() {

    }
    public Userfavorite(String userfavorite_id,  int userid_fav, String recipeid_fav) {
        this.setUserfavorite_id(userfavorite_id);
        this.setUserid_fav(userid_fav);
        this.setRecipeid_fav(recipeid_fav);
    }

    public String getUserfavorite_id() {
        return userfavorite_id;
    }

    public void setUserfavorite_id(String userfavorite_id) {
        this.userfavorite_id = userfavorite_id;
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
