package com.bonapp.app.Model;

import java.io.Serializable;
import java.util.ArrayList;

//correspond to the User table in the database
// userid is provided by facebook
//username is the facebook name
//listFav does not serve any purpose here but could be useful in case of future changes
public class User implements Serializable {

    private int userid;
    private String username;
    private ArrayList<Userfavorite> listFav;

    public User() {

    }

    public User(int userid, String username) {
        this.userid = userid;
        this.username = username;
    }
    public User(int userid, String username, ArrayList<Userfavorite> listFav) {
        this.listFav = new ArrayList<Userfavorite>();
        this.setUserid(userid);
        this.setUsername(username);
        this.setListFav(listFav);
    }


    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Userfavorite> getListFav() {
        return listFav;
    }

    public void setListFav(ArrayList<Userfavorite> listFav) {
        this.listFav = listFav;
    }
}
